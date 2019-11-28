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
import java.util.Optional;
import lombok.NonNull;
import org.caotc.unit4j.core.common.reflect.AbstractPropertyReader.FieldPropertyReader;
import org.caotc.unit4j.core.common.reflect.AbstractPropertyReader.InvokablePropertyReader;

/**
 * 属性读取器,可由get{@link Method}或者{@link Field}的包装实现,可以以统一的方式使用
 *
 * @param <T> 拥有该属性的类
 * @param <R> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
public interface PropertyReader<T, R> extends PropertyElement<T, R> {

  /**
   * 工厂方法
   *
   * @param getMethod get方法
   * @param methodNameStyle 方法名风格
   * @return 属性获取器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  static <T, R> PropertyReader<T, R> from(@NonNull Method getMethod,
      @NonNull MethodNameStyle methodNameStyle) {
    return from((Invokable<T, R>) Invokable.from(getMethod), methodNameStyle);
  }

  /**
   * 工厂方法
   *
   * @param getMethod get方法
   * @param propertyName 属性名称
   * @return 属性获取器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  static <T, R> PropertyReader<T, R> from(@NonNull Method getMethod,
      @NonNull String propertyName) {
    return from((Invokable<T, R>) Invokable.from(getMethod), propertyName);
  }

  /**
   * 工厂方法
   *
   * @param getInvokable get方法封装的Invokable
   * @param methodNameStyle 方法名风格
   * @return 属性获取器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @NonNull
  static <T, R> PropertyReader<T, R> from(@NonNull Invokable<T, R> getInvokable,
      @NonNull MethodNameStyle methodNameStyle) {
    return new InvokablePropertyReader<>(getInvokable,
        methodNameStyle.fieldNameFromGetInvokable(getInvokable));
  }

  /**
   * 工厂方法
   *
   * @param getInvokable get方法封装的Invokable
   * @param propertyName 属性名称
   * @return 属性获取器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @NonNull
  static <T, R> PropertyReader<T, R> from(@NonNull Invokable<T, R> getInvokable,
      @NonNull String propertyName) {
    return new InvokablePropertyReader<>(getInvokable, propertyName);
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
  static <T, R> PropertyReader<T, R> from(@NonNull Field field) {
    return new FieldPropertyReader<>(field);
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
}