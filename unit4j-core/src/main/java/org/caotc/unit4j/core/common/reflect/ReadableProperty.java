/*
 * Copyright (C) 2019 the original author or authors.
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

package org.caotc.unit4j.core.common.reflect;

import com.google.common.reflect.TypeToken;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NonNull;

/**
 * 可读取属性
 *
 * @param <T> 拥有该属性的类
 * @param <R> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @see PropertyReader
 * @since 1.0.0
 */
public interface ReadableProperty<T, R> extends Property<T, R> {

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
  static <T, R> SimpleReadableProperty<T, R> create(
      @NonNull Iterable<PropertyReader<T, R>> propertyReaders) {
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
  static <T, R> SimpleReadableProperty<T, R> create(
      @NonNull Iterator<PropertyReader<T, R>> propertyReaders) {
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
  static <T, R> SimpleReadableProperty<T, R> create(
      @NonNull Stream<PropertyReader<T, R>> propertyReaders) {
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
  @NonNull Optional<R> read(@NonNull T target);

  /**
   * 读取参数对象属性值
   *
   * @param target 读取属性的对象
   * @return 参数对象的属性值
   * @author caotc
   * @date 2019-11-22
   * @since 1.0.0
   */
  @NonNull R readExact(@NonNull T target);

  /**
   * compose two {@link ReadableProperty} to a {@link CompositeReadableProperty}
   *
   * @param readableProperty readableProperty
   * @return {@link CompositeReadableProperty}
   * @author caotc
   * @date 2019-11-27
   * @since 1.0.0
   */
  @NonNull <S> CompositeReadableProperty<T, S, R> compose(ReadableProperty<R, S> readableProperty);

  /**
   * compose {@link ReadableProperty} and {@link WritableProperty} to a {@link
   * CompositeWritableProperty}
   *
   * @param writableProperty writableProperty
   * @return {@link CompositeWritableProperty}
   * @author caotc
   * @date 2019-11-27
   * @since 1.0.0
   */
  @NonNull <S> CompositeWritableProperty<T, S, R> compose(WritableProperty<R, S> writableProperty);

  /**
   * compose {@link ReadableProperty} and {@link AccessibleProperty} to a {@link
   * CompositeAccessibleProperty}
   *
   * @param accessibleProperty accessibleProperty
   * @return {@link CompositeAccessibleProperty}
   * @author caotc
   * @date 2019-11-27
   * @since 1.0.0
   */
  @NonNull <S> CompositeAccessibleProperty<T, S, R> compose(
      AccessibleProperty<R, S> accessibleProperty);

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
  @NonNull <R1 extends R> ReadableProperty<T, R1> type(
      @NonNull TypeToken<R1> propertyType);
}
