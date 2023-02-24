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
import com.google.common.collect.*;
import com.google.common.reflect.TypeToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.caotc.unit4j.core.common.base.AccessLevel;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyAccessor;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyElement;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyWriter;
import org.caotc.unit4j.core.common.util.ClassUtil;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.util.*;
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
@Getter(lombok.AccessLevel.PROTECTED)
public abstract class AbstractSimpleProperty<O, P> implements Property<O, P> {
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
    //todo 排除已被重写的方法？
    @NonNull
    ImmutableSortedSet<PropertyReader<O, P>> propertyReaders;
    @NonNull
    ImmutableSortedSet<PropertyWriter<O, P>> propertyWriters;
    @Getter(lombok.AccessLevel.PUBLIC)
    @NonNull
    TypeToken<O> ownerType;

    protected AbstractSimpleProperty(
            @NonNull Iterable<? extends PropertyAccessor<O, P>> propertyAccessors) {
        this(ImmutableSortedSet.copyOf(ORDERING, propertyAccessors),
                ImmutableSortedSet.copyOf(ORDERING, propertyAccessors));
    }

    protected AbstractSimpleProperty(
            @NonNull Iterable<? extends PropertyReader<O, P>> propertyReaders,
            @NonNull Iterable<? extends PropertyWriter<O, P>> propertyWriters) {
        this(ImmutableSortedSet.copyOf(ORDERING, propertyReaders),
                ImmutableSortedSet.copyOf(ORDERING, propertyWriters));
    }

    protected AbstractSimpleProperty(
            @NonNull Iterator<PropertyReader<O, P>> propertyReaders,
            @NonNull Iterator<PropertyWriter<O, P>> propertyWriters) {
        this(ImmutableSortedSet.copyOf(ORDERING, propertyReaders),
                ImmutableSortedSet.copyOf(ORDERING, propertyWriters));
    }

    protected AbstractSimpleProperty(
            @NonNull Stream<PropertyReader<O, P>> propertyReaders,
            @NonNull Stream<PropertyWriter<O, P>> propertyWriters) {
        this(propertyReaders
                .collect(ImmutableSortedSet.toImmutableSortedSet(ORDERING)), propertyWriters
                .collect(ImmutableSortedSet.toImmutableSortedSet(ORDERING)));
    }

    private AbstractSimpleProperty(
            @NonNull ImmutableSortedSet<PropertyReader<O, P>> propertyReaders,
            @NonNull ImmutableSortedSet<PropertyWriter<O, P>> propertyWriters) {
        //属性读取器集合不能为空
        Preconditions
                .checkArgument(!propertyReaders.isEmpty() || !propertyWriters.isEmpty(),
                        "propertyReaders and propertyWriters can't be all empty");
        //属性只能有一个
        ImmutableSet<String> propertyNames = Streams
                .concat(propertyReaders.stream(), propertyWriters.stream())
                .map(PropertyElement::propertyName).collect(ImmutableSet.toImmutableSet());
        Preconditions.checkArgument(propertyNames.size() == 1,
                "propertyReaders and propertyWriters not belong to a common property.propertyNames:%s", propertyNames);
        //ownerType只能有一个
        ImmutableSet<TypeToken<O>> ownerTypes = Streams.concat(propertyReaders.stream(), propertyWriters.stream())
                .map(PropertyElement::ownerType)
                .collect(ImmutableSet.toImmutableSet());
        Preconditions.checkArgument(ownerTypes.size() == 1,
                "propertyReaders and propertyWriters not belong to a common property.ownerTypes:%s", ownerTypes);
        this.name = Iterables.getOnlyElement(propertyNames);
        this.ownerType = Iterables.getOnlyElement(ownerTypes);
        this.propertyReaders = propertyReaders;
        this.propertyWriters = propertyWriters;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public final TypeToken<P> type() {
        //todo 泛型
        if (!propertyWriters().isEmpty()) {
            return (TypeToken<P>) propertyWriters().first().propertyType();
        }
        return (TypeToken<P>) propertyReaders().first().propertyType();
    }

    @Override
    public boolean canOwnBy(@NonNull TypeToken<?> newOwnerType) {
        return propertyWriters().stream().allMatch(propertyWriter -> propertyWriter.canOwnBy(newOwnerType))
                && propertyReaders().stream().allMatch(propertyReader -> propertyReader.canOwnBy(newOwnerType));
    }

    @NonNull
    @Override
    public final <X extends Annotation> Optional<X> annotation(
            @NonNull Class<X> annotationClass) {
        return Streams.concat(propertyReaders().stream(), propertyWriters().stream())
                .map(propertyElement -> AnnotatedElementUtils.findMergedAnnotation(propertyElement, annotationClass))
                .filter(Objects::nonNull)
                .findFirst();
    }

    @NonNull
    @Override
    public final <X extends Annotation> ImmutableList<X> annotations(
            @NonNull Class<X> annotationClass) {
        return Streams.concat(propertyReaders().stream(), propertyWriters().stream())
                .map(propertyGetter -> AnnotatedElementUtils.findAllMergedAnnotations(propertyGetter, annotationClass))
                .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
    }
}
