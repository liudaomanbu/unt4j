/*
 * Copyright (C) 2020 the original author or authors.
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
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyWriter;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * 可写属性
 *
 * @param <O> 拥有该属性的类
 * @param <P> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @see PropertyWriter
 * @since 1.0.0
 */
public interface WritableProperty<O, P> extends Property<O, P> {

    /**
     * 工厂方法
     *
     * @param propertyWriters 属性设置器集合
     * @return 属性设置器
     * @author caotc
     * @date 2019-05-27
     * @since 1.0.0
     */
    @NonNull
    static <T, R> WritableProperty<T, R> create(
            @NonNull Iterable<PropertyWriter<T, R>> propertyWriters) {

        return new SimpleWritableProperty<>(propertyWriters);
    }

    /**
     * 工厂方法
     *
     * @param propertyWriters 属性设置器集合
     * @return 属性设置器
     * @author caotc
     * @date 2019-05-27
     * @since 1.0.0
     */
    @NonNull
    static <T, R> WritableProperty<T, R> create(
            @NonNull Iterator<PropertyWriter<T, R>> propertyWriters) {
        return new SimpleWritableProperty<>(propertyWriters);
    }

    /**
     * 工厂方法
     *
     * @param propertyWriters 属性设置器集合
     * @return 属性设置器
     * @author caotc
     * @date 2019-05-27
     * @since 1.0.0
     */
    @NonNull
    static <T, R> WritableProperty<T, R> create(
            @NonNull Stream<PropertyWriter<T, R>> propertyWriters) {
        return new SimpleWritableProperty<>(propertyWriters);
  }

  @Override
  default boolean writable() {
      return true;
  }

    /**
     * 将参数值设置到参数对象的该属性上
     *
     * @param target 被设置属性值的对象
     * @param value  被设置到属性的值
     * @return this
     * @author caotc
     * @date 2019-11-22
     * @since 1.0.0
     */
    @NonNull WritableProperty<O, P> write(@NonNull O target, @NonNull P value);

    /**
     * 设置属性类型
     *
     * @param propertyType 属性类型
     * @return this
     * @author caotc
     * @date 2019-06-25
     * @since 1.0.0
     */
    @NonNull
    default <P1 extends P> WritableProperty<O, P1> type(@NonNull Class<P1> propertyType) {
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
    default <P1 extends P> WritableProperty<O, P1> type(
            @NonNull TypeToken<P1> propertyType) {
        Preconditions.checkArgument(propertyType.isSupertypeOf(type())
                , "WritableProperty is known type %s,not %s ", type(), propertyType);
        //noinspection unchecked
    return (WritableProperty<O, P1>) this;
  }
}