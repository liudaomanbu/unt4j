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
import com.google.common.reflect.TypeToken;
import lombok.NonNull;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyAccessor;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyWriter;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * @param <O> owner type
 * @param <P> property type
 * @author caotc
 * @date 2019-11-22
 * @see ReadableProperty
 * @see WritableProperty
 * @since 1.0.0
 */
public interface AccessibleProperty<O, P> extends ReadableProperty<O, P>,
        WritableProperty<O, P> {

    @NonNull
    static <O, P> SimpleAccessibleProperty<O, P> create(
            @NonNull Iterable<? extends PropertyAccessor<O, P>> propertyAccessors) {
        return new SimpleAccessibleProperty<>(propertyAccessors, propertyAccessors);
    }

    @NonNull
    static <O, P> SimpleAccessibleProperty<O, P> create(
            @NonNull Iterable<? extends PropertyReader<O, P>> propertyReaders,
            @NonNull Iterable<? extends PropertyWriter<O, P>> propertyWriters) {
        return new SimpleAccessibleProperty<>(propertyReaders, propertyWriters);
    }

    @NonNull
    static <O, P> SimpleAccessibleProperty<O, P> create(
            @NonNull Iterator<PropertyReader<O, P>> propertyReaders,
            @NonNull Iterator<PropertyWriter<O, P>> propertyWriters) {
        return new SimpleAccessibleProperty<>(propertyReaders, propertyWriters);
    }

    @NonNull
    static <O, P> SimpleAccessibleProperty<O, P> create(
            @NonNull Stream<PropertyReader<O, P>> propertyReaders,
            @NonNull Stream<PropertyWriter<O, P>> propertyWriters) {
        return new SimpleAccessibleProperty<>(propertyReaders, propertyWriters);
    }

    @Override
    @NonNull AccessibleProperty<O, P> write(@NonNull O target, @NonNull P value);

    /**
     * 设置属性类型
     *
     * @param propertyType 属性类型
     * @return this
     * @author caotc
     * @date 2019-06-25
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <P1 extends P> AccessibleProperty<O, P1> type(@NonNull Class<P1> propertyType) {
        return type(TypeToken.of(propertyType));
    }

    /**
     * 设置属性类型
     *
     * @param propertyType 属性类型
     * @return this
     * @author caotc
     * @date 2019-11-22
     * @see Property#type
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <P1 extends P> AccessibleProperty<O, P1> type(
            @NonNull TypeToken<P1> propertyType) {
        Preconditions.checkArgument(propertyType.isSupertypeOf(type())
                , "AccessibleProperty is known type %s,not %s ", type(), propertyType);
        //noinspection unchecked
        return (AccessibleProperty<O, P1>) this;
    }

    default @NonNull <O1> AccessibleProperty<O1, P> ownerType(@NonNull Class<O1> ownerType) {
        return this.ownerType(TypeToken.of(ownerType));
    }

    @Override
    @NonNull <O1> AccessibleProperty<O1, P> ownerType(@NonNull TypeToken<O1> ownerType);
}
