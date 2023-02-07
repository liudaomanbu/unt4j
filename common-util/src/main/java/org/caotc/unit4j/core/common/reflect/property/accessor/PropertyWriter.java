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

/**
 * 属性编写器,可由set{@link Method}或者{@link Field}的包装实现,可以以统一的方式使用
 * todo 范型字母
 *
 * @param <T> 拥有该属性的类
 * @param <R> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
public interface PropertyWriter<T, R> extends PropertyElement<T, R> {

  @SuppressWarnings("unchecked")
  @NonNull
  static <T, R> PropertyWriter<T, R> from(@NonNull Type ownerType, @NonNull Method propertyWriterMethod,
                                          @NonNull String propertyName) {
    return from((TypeToken<T>) TypeToken.of(ownerType), propertyWriterMethod, propertyName);
  }

  @NonNull
  static <T, R> PropertyWriter<T, R> from(@NonNull Class<T> ownerClass, @NonNull Method propertyWriterMethod,
                                          @NonNull String propertyName) {
    return from(TypeToken.of(ownerClass), propertyWriterMethod, propertyName);
  }

  /**
   * 工厂方法
   *
   * @param propertyWriterMethod set方法
   * @param propertyName         属性名称
   * @return 属性设置器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @NonNull
  static <T, R> PropertyWriter<T, R> from(@NonNull TypeToken<T> ownerType, @NonNull Method propertyWriterMethod,
                                          @NonNull String propertyName) {
    return new AbstractPropertyWriter.InvokablePropertyWriter<>(GuavaInvokableProxy.from(propertyWriterMethod, ownerType), propertyName);
  }

  @NonNull
  static <T, R> PropertyWriter<T, R> from(@NonNull Type ownerType, @NonNull Field field) {
    return PropertyAccessor.<T, R>from(ownerType, field).toWriter();
  }

  @NonNull
  static <T, R> PropertyWriter<T, R> from(@NonNull Class<T> ownerClass, @NonNull Field field) {
    return PropertyAccessor.<T, R>from(ownerClass, field).toWriter();
  }

  /**
   * 工厂方法
   *
   * @param ownerType set方法
   * @param field     属性名称
   * @return 属性设置器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @NonNull
  static <T, R> PropertyWriter<T, R> from(@NonNull TypeToken<T> ownerType, @NonNull Field field) {
    return PropertyAccessor.<T, R>from(ownerType, field).toWriter();
  }

  /**
   * 给传入对象的该属性设置传入的值
   *
   * @param object 设置属性值的对象
   * @param value 设置的属性值
   * @return this
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  PropertyWriter<T, R> write(@NonNull T object, @NonNull R value);


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
  @NonNull <R1 extends R> PropertyWriter<T, R1> propertyType(@NonNull Class<R1> propertyType);

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
  @NonNull <R1 extends R> PropertyWriter<T, R1> propertyType(@NonNull TypeToken<R1> propertyType);

  @Override
  default boolean isWriter() {
    return true;
  }
}