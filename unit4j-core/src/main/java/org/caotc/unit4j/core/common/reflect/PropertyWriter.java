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

import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.NonNull;
import org.caotc.unit4j.core.common.reflect.AbstractPropertyWriter.FieldPropertyWriter;
import org.caotc.unit4j.core.common.reflect.AbstractPropertyWriter.InvokablePropertyWriter;

/**
 * 属性编写器,可由set{@link Method}或者{@link Field}的包装实现,可以以统一的方式使用
 *
 * @param <T> 拥有该属性的类
 * @param <R> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
public interface PropertyWriter<T, R> extends PropertyElement<T, R> {

  /**
   * 工厂方法
   *
   * @param setMethod set方法
   * @param methodNameStyle 方法名称风格
   * @return 属性设置器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  static <T, R> PropertyWriter<T, R> from(@NonNull Method setMethod,
      @NonNull MethodNameStyle methodNameStyle) {
    return from((Invokable<T, ?>) Invokable.from(setMethod), methodNameStyle);
  }

  /**
   * 工厂方法
   *
   * @param setMethod set方法
   * @param propertyName 属性名称
   * @return 属性设置器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  static <T, R> PropertyWriter<T, R> from(@NonNull Method setMethod,
      @NonNull String propertyName) {
    return from((Invokable<T, ?>) Invokable.from(setMethod), propertyName);
  }

  /**
   * 工厂方法
   *
   * @param setInvokable set方法
   * @param methodNameStyle 方法名称风格
   * @return 属性设置器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */

  @NonNull
  static <T, R> PropertyWriter<T, R> from(@NonNull Invokable<T, ?> setInvokable,
      @NonNull MethodNameStyle methodNameStyle) {
    return new InvokablePropertyWriter<>(setInvokable,
        methodNameStyle.fieldNameFromSetInvokable(setInvokable));
  }

  /**
   * 工厂方法
   *
   * @param setInvokable set方法
   * @param propertyName 属性名称
   * @return 属性设置器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @NonNull
  static <T, R> PropertyWriter<T, R> from(@NonNull Invokable<T, ?> setInvokable,
      @NonNull String propertyName) {
    return new InvokablePropertyWriter<>(setInvokable, propertyName);
  }

  /**
   * 工厂方法
   *
   * @param field 属性
   * @return 属性设置器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @NonNull
  static <T, R> PropertyWriter<T, R> from(@NonNull Field field) {
    return new FieldPropertyWriter<>(field);
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

}