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
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;
import org.caotc.unit4j.core.exception.ReadablePropertyValueNotFoundException;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 可读取属性
 * todo 范型字母
 *
 * @param <O> 拥有该属性的类
 * @param <P> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @see PropertyReader
 * @since 1.0.0
 */
public interface ReadableProperty<O, P> extends Property<O, P> {

  /**
   * 工厂方法
   *
   * @param propertyReaders 属性获取器集合
   * @return 属性获取器
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  static <T, R> ReadableProperty<T, R> create(
      @NonNull Iterable<PropertyReader<? super T, R>> propertyReaders) {
    return new SimpleReadableProperty<>(propertyReaders);
  }

  /**
   * 工厂方法
   *
   * @param propertyReaders 属性获取器集合
   * @return 属性获取器
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  static <T, R> ReadableProperty<T, R> create(
      @NonNull Iterator<PropertyReader<? super T, R>> propertyReaders) {
    return new SimpleReadableProperty<>(propertyReaders);
  }

  /**
   * 工厂方法
   *
   * @param propertyReaders 属性获取器集合
   * @return 属性获取器
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  static <T, R> ReadableProperty<T, R> create(
      @NonNull Stream<PropertyReader<? super T, R>> propertyReaders) {
    return new SimpleReadableProperty<>(propertyReaders);
  }

  @Override
  default boolean readable() {
    return true;
  }


  /**
   * 读取参数对象属性值
   *
   * @param target 读取属性的对象
   * @return 参数对象的属性值的 {@link Optional}
   * @author caotc
   * @date 2019-11-22
   * @since 1.0.0
   */
  @NonNull Optional<P> read(@NonNull O target);

  /**
   * 读取参数对象属性值
   *
   * @param target 读取属性的对象
   * @return 参数对象的属性值
   * @author caotc
   * @date 2019-11-22
   * @since 1.0.0
   */
  @NonNull
  default P readExact(@NonNull O target) {
    return read(target)
            .orElseThrow(() -> ReadablePropertyValueNotFoundException.create(this, target));
  }

  /**
   * compose two {@link ReadableProperty} to a {@link ReadableProperty}
   *
   * @param readableProperty readableProperty
   * @return {@link ReadableProperty}
   * @author caotc
   * @date 2019-11-27
   * @since 1.0.0
   */
  @NonNull
  default <S> ReadableProperty<O, S> compose(ReadableProperty<P, S> readableProperty) {
    if (readableProperty.accessible()) {
      return compose(readableProperty.toAccessible());
    }
    return CompositeReadableProperty.create(this, readableProperty);
  }

  /**
   * compose {@link ReadableProperty} and {@link WritableProperty} to a {@link
   * WritableProperty}
   *
   * @param writableProperty writableProperty
   * @return {@link WritableProperty}
   * @author caotc
   * @date 2019-11-27
   * @since 1.0.0
   */
  @NonNull
  default <S> WritableProperty<O, S> compose(WritableProperty<P, S> writableProperty) {
    if (writableProperty.accessible()) {
      return compose(writableProperty.toAccessible());
    }
    return CompositeWritableProperty.create(this, writableProperty);
  }

  /**
   * compose {@link ReadableProperty} and {@link AccessibleProperty} to a {@link
   * AccessibleProperty}
   *
   * @param accessibleProperty accessibleProperty
   * @return {@link AccessibleProperty}
   * @author caotc
   * @date 2019-11-27
   * @since 1.0.0
   */
  @NonNull
  default <S> AccessibleProperty<O, S> compose(
          AccessibleProperty<P, S> accessibleProperty) {
    return new CompositeAccessibleProperty<>(this, accessibleProperty);
  }

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
  default <P1 extends P> ReadableProperty<O, P1> type(@NonNull Class<P1> propertyType) {
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
  default <P1 extends P> ReadableProperty<O, P1> type(
          @NonNull TypeToken<P1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(type())
            , "ReadableProperty is known type %s,not %s ", type(), propertyType);
    //noinspection unchecked
    return (ReadableProperty<O, P1>) this;
  }
}
