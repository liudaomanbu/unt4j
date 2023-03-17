/*
 * Copyright (C) 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.caotc.unit4j.core.common.reflect.property;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import com.google.common.reflect.TypeToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.caotc.unit4j.core.common.base.AccessLevel;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyElement;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyWriter;
import org.caotc.unit4j.core.common.util.ClassUtil;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 简单属性抽象类
 * 同一个{@link #ownerType}的同名属性就认为是同一个属性.
 * 但是在java中这种假设并非百分百成立,比如super class的private field和sub class的field可以同名,但是这两个field其实并非是同一个属性.
 *
 * @param <O> owner type
 * @param <P> property type
 * @author caotc
 * @date 2019-11-28
 * @since 1.0.0
 */
@EqualsAndHashCode
@ToString
@Getter
public class SimpleProperty<O, P> implements AccessibleProperty<O, P> {
    /**
     * {@link PropertyElement}排序器
     */
    private static final Comparator<PropertyElement<?, ?>> ORDERING = Comparator
            //优先比较权限修饰符
            .<PropertyElement<?, ?>, AccessLevel>comparing(PropertyElement::accessLevel)
            //其次比较在哪个类定义,子类优先,与父类和子类有同名field定义时的处理方式保持一致
            .thenComparing((p1, p2) -> p1.declaringType().equals(p2.declaringType()) ? 0 : p1.declaringType().isSubtypeOf(p2.declaringType()) ? -1 : 1)
            //最后比较属性类型,子类优先.因为是同一个属性的前提,认为子类拥有更具体的信息.
            .thenComparing((p1, p2) -> {
                if (p1.propertyType().equals(p2.propertyType())) {
                    return 0;
                }
                TypeToken<?> propertyType1 = p1.propertyType();
                TypeToken<?> propertyType2 = p2.propertyType();

                //当propertyType1和propertyType2都是数组或容器时,拆至非数组容器元素,以元素处理
                while ((propertyType1.isArray() && propertyType2.isArray())
                        || (List.class.equals(propertyType1.getRawType()) && List.class.equals(propertyType2.getRawType()))
                        || (Set.class.equals(propertyType1.getRawType()) && Set.class.equals(propertyType2.getRawType()))
                        || (Collection.class.equals(propertyType1.getRawType()) && Collection.class.equals(propertyType2.getRawType()))) {
                    propertyType1 = ClassUtil.unwrapContainer(propertyType1);
                    propertyType2 = ClassUtil.unwrapContainer(propertyType2);
                }

                TypeToken<?> propertyTypeWarp1 = propertyType1.wrap();
                TypeToken<?> propertyTypeWarp2 = propertyType2.wrap();
                //两者是对应的基本类型和包装类
                if (propertyTypeWarp1.equals(propertyTypeWarp2)) {
                    return propertyType1.isPrimitive() ? -1 : 1;
                }
                //之后把基本类型当作包装类对待
                if (propertyTypeWarp1.isSubtypeOf(propertyTypeWarp2)) {
                    return -1;
                }
                if (propertyTypeWarp2.isSubtypeOf(propertyTypeWarp1)) {
                    return 1;
                }
                return 0;
            })
            //非排序用,区分元素,否则在sortedSet中会认为是相等的元素.理论上hashCode存在重复可能,直接使用内存地址
            .thenComparing((p1, p2) -> p1.equals(p2) ? 0 : System.identityHashCode(p1) - System.identityHashCode(p2));
    @Getter(lombok.AccessLevel.PUBLIC)
    @NonNull
    String name;
    @NonNull
    ImmutableSortedSet<PropertyReader<O, P>> propertyReaders;
    @NonNull
    ImmutableSortedSet<PropertyWriter<O, P>> propertyWriters;
    @Getter(lombok.AccessLevel.PUBLIC)
    @NonNull
    TypeToken<O> ownerType;


    SimpleProperty(
            @NonNull Collection<? extends PropertyElement<O, P>> propertyElements) {
        //属性读取器集合不能为空
        Preconditions.checkArgument(!propertyElements.isEmpty(), "propertyElements can't be empty");
        //属性只能有一个
        ImmutableSet<String> propertyNames = propertyElements.stream()
                .map(PropertyElement::propertyName).collect(ImmutableSet.toImmutableSet());
        Preconditions.checkArgument(propertyNames.size() == 1,
                "propertyElements not belong to a common property.propertyNames:%s", propertyNames);
        //ownerType只能有一个
        ImmutableSet<TypeToken<O>> ownerTypes = propertyElements.stream()
                .map(PropertyElement::ownerType)
                .collect(ImmutableSet.toImmutableSet());
        Preconditions.checkArgument(ownerTypes.size() == 1,
                "propertyElements not belong to a common property.ownerTypes:%s", ownerTypes);
        this.name = Iterables.getOnlyElement(propertyNames);
        this.ownerType = Iterables.getOnlyElement(ownerTypes);
        this.propertyReaders = propertyElements.stream()
                .filter(PropertyElement::isReader)
                .map(PropertyElement::toReader)
                .collect(ImmutableSortedSet.toImmutableSortedSet(ORDERING));
        this.propertyWriters = propertyElements.stream()
                .filter(PropertyElement::isWriter)
                .map(PropertyElement::toWriter)
                .collect(ImmutableSortedSet.toImmutableSortedSet(ORDERING));
    }

    @NonNull
    @Override
    public TypeToken<P> type() {
        if (!propertyWriters().isEmpty()) {
            return propertyWriters().first().propertyType();
        }
        return propertyReaders().first().propertyType();
    }

    @Override
    public boolean checkOwnerType(@NonNull TypeToken<?> newOwnerType) {
        return propertyElements().allMatch(propertyElement -> propertyElement.checkOwnerType(newOwnerType));
    }

    @Override
    public @NonNull <O1> AccessibleProperty<O1, P> ownerType(@NonNull TypeToken<O1> ownerType) {
        Preconditions.checkArgument(checkOwnerType(ownerType), "%s can not owner by %s", ownerType);
        return new SimpleProperty<>(propertyElements()
                .map(propertyElement -> propertyElement.ownerType(ownerType))
                .collect(Collectors.toSet()));
    }

    @Override
    public boolean writable() {
        return !propertyWriters().isEmpty();
    }

    @Override
    public boolean readable() {
        return !propertyReaders().isEmpty();
    }

    @Override
    public @NonNull O write(@NonNull O target, @NonNull P value) {
        Preconditions.checkState(writable(), "%s can not write", this);
        //property writers should write to a same property
        return propertyWriters().first().write(target, value);
    }

    @Override
    public @NonNull Optional<P> read(@NonNull O target) {
        Preconditions.checkState(readable(), "%s can not read", this);
        return propertyReaders().stream().map(propertyGetter -> propertyGetter.read(target))
                .filter(Optional::isPresent).map(Optional::get).findFirst();
    }

    @NonNull
    @Override
    public <X extends Annotation> Optional<X> annotation(
            @NonNull Class<X> annotationClass) {
        return propertyElements()
                .map(propertyElement -> AnnotatedElementUtils.findMergedAnnotation(propertyElement, annotationClass))
                .filter(Objects::nonNull)
                .findFirst();
    }

    @NonNull
    @Override
    public <X extends Annotation> ImmutableList<X> annotations(
            @NonNull Class<X> annotationClass) {
        return propertyElements()
                .map(propertyGetter -> AnnotatedElementUtils.findAllMergedAnnotations(propertyGetter, annotationClass))
                .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
    }

    Stream<PropertyElement<O, P>> propertyElements() {
        return Streams.concat(propertyReaders().stream(), propertyWriters().stream());
    }
}
