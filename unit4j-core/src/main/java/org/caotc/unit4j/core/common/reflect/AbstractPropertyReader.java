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

import com.google.common.base.Preconditions;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Optional;
import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.Value;

/**
 * 属性获取器,可由get{@link Method}或者{@link Field}的包装实现,可以以统一的方式使用
 *
 * @param <T> 拥有该属性的类
 * @param <R> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@Data
public abstract class AbstractPropertyReader<T, R> extends AbstractPropertyElement<T, R> implements
    PropertyReader<T, R> {

  <M extends AccessibleObject & Member> AbstractPropertyReader(
      @NonNull M member) {
    super(member);
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
  @Override
  @NonNull
  public final Optional<R> read(@NonNull T object) {
    if (!accessible()) {
      accessible(true);
    }
    return readInternal(object);
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
  protected abstract Optional<R> readInternal(@NonNull T object);


  /**
   * 修改返回类型
   *
   * @param propertyType 新的返回类型
   * @return 修改返回类型的属性获取器
   * @author caotc
   * @date 2019-06-25
   * @since 1.0.0
   */
  @Override
  @NonNull
  public <R1 extends R> AbstractPropertyReader<T, R1> propertyType(
      @NonNull Class<R1> propertyType) {
    return (AbstractPropertyReader<T, R1>) super.propertyType(propertyType);
  }

  /**
   * 修改返回类型
   *
   * @param propertyType 新的返回类型
   * @return 修改返回类型的属性获取器
   * @author caotc
   * @date 2019-06-25
   * @since 1.0.0
   */
  @Override
  @NonNull
  public <R1 extends R> AbstractPropertyReader<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    return (AbstractPropertyReader<T, R1>) super.propertyType(propertyType);
  }

  /**
   * {@link Field}实现的属性获取器
   *
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @Value
  public static class FieldPropertyReader<T, R> extends AbstractPropertyReader<T, R> {

    /**
     * 属性
     */
    @NonNull
    Field field;

    FieldPropertyReader(@NonNull Field field) {
      super(field);
      this.field = field;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    @SneakyThrows
    public Optional<R> readInternal(@NonNull T object) {
      return Optional.ofNullable((R) field.get(object));
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NonNull TypeToken<? extends R> propertyType() {
      return TypeToken.of((Class<? extends R>) field.getType());
    }

    @Override
    public @NonNull String propertyName() {
      return field.getName();
    }

    @Override
    public @NonNull <R1 extends R> FieldPropertyReader<T, R1> propertyType(
        @NonNull Class<R1> propertyType) {
      return (FieldPropertyReader<T, R1>) super.propertyType(propertyType);
    }

    @Override
    public @NonNull <R1 extends R> FieldPropertyReader<T, R1> propertyType(
        @NonNull TypeToken<R1> propertyType) {
      return (FieldPropertyReader<T, R1>) super.propertyType(propertyType);
    }
  }

  /**
   * get{@link Invokable}实现的属性获取器
   *
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @Value
  public static class InvokablePropertyReader<T, R> extends AbstractPropertyReader<T, R> {

    /**
     * get方法
     */
    @NonNull Invokable<T, R> getInvokable;
    /**
     * 属性名称
     */
    @NonNull String propertyName;

    InvokablePropertyReader(@NonNull Invokable<T, R> invokable,
        @NonNull String propertyName) {
      super(invokable);
      Preconditions
          .checkArgument(!invokable.isStatic()
                  && invokable.getParameters().isEmpty()
                  && !invokable.getReturnType().getRawType().equals(void.class)
              , "%s is not a getInvokable",
              invokable);
      this.getInvokable = invokable;
      this.propertyName = propertyName;
    }

    @NonNull
    @Override
    @SneakyThrows
    public Optional<R> readInternal(@NonNull T object) {
      return Optional.ofNullable(getInvokable.invoke(object));
    }

    @Override
    public @NonNull TypeToken<? extends R> propertyType() {
      return getInvokable.getReturnType();
    }

    @Override
    public @NonNull <R1 extends R> InvokablePropertyReader<T, R1> propertyType(
        @NonNull Class<R1> propertyType) {
      return (InvokablePropertyReader<T, R1>) super.propertyType(propertyType);
    }

    @Override
    public @NonNull <R1 extends R> InvokablePropertyReader<T, R1> propertyType(
        @NonNull TypeToken<R1> propertyType) {
      return (InvokablePropertyReader<T, R1>) super.propertyType(propertyType);
    }
  }
}