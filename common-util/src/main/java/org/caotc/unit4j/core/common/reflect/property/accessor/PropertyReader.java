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

package org.caotc.unit4j.core.common.reflect.property.accessor;

import com.google.common.reflect.TypeToken;
import lombok.NonNull;
import org.caotc.unit4j.core.common.reflect.GuavaInvokableProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * 属性读取器,可由get{@link Method}或者{@link Field}的包装实现,可以以统一的方式使用
 * todo 范型字母
 *
 * @param <T> 拥有该属性的类
 * @param <R> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
public interface PropertyReader<T, R> extends PropertyElement<T, R> {
  @SuppressWarnings("unchecked")
  @NonNull
  static <T, R> PropertyReader<T, R> from(@NonNull Type ownerType, @NonNull Method propertyReaderMethod,
                                          @NonNull String propertyName) {
    return from((TypeToken<T>) TypeToken.of(ownerType), propertyReaderMethod, propertyName);
  }

  @NonNull
  static <T, R> PropertyReader<T, R> from(@NonNull Class<T> ownerClass, @NonNull Method propertyReaderMethod,
                                          @NonNull String propertyName) {
    return from(TypeToken.of(ownerClass), propertyReaderMethod, propertyName);
  }

  /**
   * 工厂方法
   *
   * @param propertyReaderMethod get方法
   * @param propertyName         属性名称
   * @return 属性获取器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @NonNull
  static <T, R> PropertyReader<T, R> from(@NonNull TypeToken<T> ownerType, @NonNull Method propertyReaderMethod,
                                          @NonNull String propertyName) {
    return new AbstractPropertyReader.InvokablePropertyReader<>(GuavaInvokableProxy.from(propertyReaderMethod, ownerType), propertyName);
  }

  @NonNull
  static <T, R> PropertyReader<T, R> from(@NonNull Type ownerType, @NonNull Field field) {
    return PropertyAccessor.<T, R>from(ownerType, field).toReader();
  }

  @NonNull
  static <T, R> PropertyReader<T, R> from(@NonNull Class<T> ownerClass, @NonNull Field field) {
    return PropertyAccessor.<T, R>from(ownerClass, field).toReader();
  }

  /**
   * 工厂方法
   *
   * @param field 属性
   * @return 属性获取器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @NonNull
  static <T, R> PropertyReader<T, R> from(@NonNull TypeToken<T> ownerType, @NonNull Field field) {
    return PropertyAccessor.<T, R>from(ownerType, field).toReader();
  }

  /**
   * 从传入的对象中获取该属性的值
   *
   * @param object 对象
   * @return 对象中该属性的值
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  Optional<R> read(@NonNull T object);

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
  @NonNull <R1 extends R> PropertyReader<T, R1> propertyType(@NonNull Class<R1> propertyType);

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
  @NonNull <R1 extends R> PropertyReader<T, R1> propertyType(@NonNull TypeToken<R1> propertyType);

  @Override
  default boolean isReader() {
    return true;
  }

}