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
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyElement;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyWriter;
import org.caotc.unit4j.core.common.util.ReflectionUtil;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Stream;

/**
 * 简单属性抽象类
 *
 * @param <O> 拥有该属性的类
 * @param <P> 属性类型
 * @author caotc
 * @date 2019-11-28
 * @since 1.0.0
 */
@EqualsAndHashCode
@ToString
public abstract class AbstractSimpleProperty<O, P> implements Property<O, P> {
    /**
     * 权限级别元素排序器,{@link AccessLevel#PUBLIC}最前+内存地址比较器
     */
    private static final Ordering<PropertyElement<?, ?>> ORDERING = Ordering.natural()
            .onResultOf(PropertyElement::accessLevel);

    @NonNull
    String name;
    @NonNull
    TypeToken<? extends P> type;
    boolean fieldExist;
    @NonNull
    protected ImmutableSortedSet<PropertyReader<? super O, P>> propertyReaders;
    @NonNull
    protected ImmutableSortedSet<PropertyWriter<? super O, P>> propertyWriters;

    protected AbstractSimpleProperty(
            @NonNull Iterable<? extends PropertyReader<? super O, P>> propertyReaders,
            @NonNull Iterable<? extends PropertyWriter<? super O, P>> propertyWriters) {
        this(ImmutableSortedSet.copyOf(ORDERING, propertyReaders),
                ImmutableSortedSet.copyOf(ORDERING, propertyWriters));
    }

    protected AbstractSimpleProperty(
        @NonNull Iterator<PropertyReader<? super O, P>> propertyReaders,
        @NonNull Iterator<PropertyWriter<? super O, P>> propertyWriters) {
        this(ImmutableSortedSet.copyOf(ORDERING, propertyReaders),
            ImmutableSortedSet.copyOf(ORDERING, propertyWriters));
    }

    protected AbstractSimpleProperty(
        @NonNull Stream<PropertyReader<? super O, P>> propertyReaders,
        @NonNull Stream<PropertyWriter<? super O, P>> propertyWriters) {
        this(propertyReaders
            .collect(ImmutableSortedSet.toImmutableSortedSet(ORDERING)), propertyWriters
            .collect(ImmutableSortedSet.toImmutableSortedSet(ORDERING)));
    }

    protected AbstractSimpleProperty(
        @NonNull ImmutableSortedSet<PropertyReader<? super O, P>> propertyReaders,
        @NonNull ImmutableSortedSet<PropertyWriter<? super O, P>> propertyWriters) {
        //属性读取器集合不能为空
        Preconditions
                .checkArgument(!propertyReaders.isEmpty() || !propertyWriters.isEmpty(),
                        "propertyReaders and propertyWriters can't be all empty");
        //属性只能有一个
        ImmutableSet<@NonNull String> propertyNames = Streams
                .concat(propertyReaders.stream(), propertyWriters.stream())
                .map(PropertyElement::propertyName).collect(ImmutableSet.toImmutableSet());
        //todo check declaringTypes
        Preconditions.checkArgument(propertyNames.size() == 1,
                "propertyReaders and propertyWriters not belong to a common property.propertyNames:%s", propertyNames);
        ImmutableSet<? extends TypeToken<? extends P>> propertyTypes = Streams
                .concat(propertyReaders.stream(), propertyWriters.stream())
                .map(PropertyElement::propertyType).collect(ImmutableSet.toImmutableSet());
        if (propertyTypes.size() > 1) {
            propertyTypes = propertyTypes.stream()
                    .map(type -> (TypeToken<? extends P>) ReflectionUtil.primitiveTypeToWrapperType(type))
                    .collect(ImmutableSet.toImmutableSet());
        }
        List<TypeToken<?>> lowestCommonAncestors = ReflectionUtil.lowestCommonAncestors(propertyTypes);

        Preconditions.checkArgument(!lowestCommonAncestors.isEmpty(),
                "lowestCommonAncestors is empty.propertyTypes:%s", propertyTypes);
        this.name = Iterables.getOnlyElement(propertyNames);
        this.type = (TypeToken<? extends P>) lowestCommonAncestors.get(0);//todo
        this.fieldExist = Streams.concat(propertyReaders.stream(), propertyWriters.stream()).anyMatch(PropertyElement::basedOnField);
        this.propertyReaders = propertyReaders;
        this.propertyWriters = propertyWriters;
    }

    @NonNull
    @Override
    public final String name() {
        return name;
    }

    @NonNull
    @Override
    public final TypeToken<? extends P> type() {
        return type;
    }

    @Override
    public final boolean fieldExist() {
        return fieldExist;
    }

    @NonNull
    @Override
    public final <X extends Annotation> Optional<X> annotation(
            @NonNull Class<X> annotationClass) {
        return Streams.concat(propertyReaders.stream(), propertyWriters.stream())
                .map(propertyElement -> AnnotatedElementUtils.findMergedAnnotation(propertyElement, annotationClass))
                .filter(Objects::nonNull)
                .findFirst();
    }

    @NonNull
    @Override
    public final <X extends Annotation> ImmutableList<X> annotations(
            @NonNull Class<X> annotationClass) {
        return Streams.concat(propertyReaders.stream(), propertyWriters.stream())
                .map(propertyGetter -> AnnotatedElementUtils.findAllMergedAnnotations(propertyGetter, annotationClass))
                .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
    }
}
