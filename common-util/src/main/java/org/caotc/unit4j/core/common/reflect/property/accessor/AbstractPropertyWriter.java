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

import com.google.common.base.Preconditions;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.Value;
import org.caotc.unit4j.core.common.reflect.FieldElement;
import org.caotc.unit4j.core.common.util.ReflectionUtil;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * 属性设置器,可由get{@link Method}或者{@link Field}的包装实现,可以以统一的方式使用
 *
 * @param <T> 拥有该属性的类
 * @param <R> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@Data
public abstract class AbstractPropertyWriter<T, R> extends AbstractPropertyElement<T, R> implements
    PropertyWriter<T, R> {

  <M extends AnnotatedElement & Member> AbstractPropertyWriter(
          @NonNull M member) {
    super(member);
  }

  /**
   * 给传入对象的该属性设置传入的值
   *
   * @param object 设置属性值的对象
   * @param value 设置的属性值
   * @return {@code this}
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @Override
  @NonNull
  public final AbstractPropertyWriter<T, R> write(@NonNull T object, @NonNull R value) {
    if (!accessible()) {
      accessible(true);
    }
    writeInternal(object, value);
    return this;
  }

  /**
   * 给传入对象的该属性设置传入的值
   *
   * @param object 设置属性值的对象
   * @param value 设置的属性值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  protected abstract void writeInternal(@NonNull T object, @NonNull R value);

  /**
   * 属性名称
   *
   * @return 属性名称
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @Override
  @NonNull
  public abstract String propertyName();

  /**
   * 属性类型
   *
   * @return 属性类型
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @Override
  @NonNull
  public abstract TypeToken<? extends R> propertyType();

  @Override
  @NonNull
  public final <R1 extends R> PropertyWriter<T, R1> propertyType(
      @NonNull Class<R1> propertyType) {
    return propertyType(TypeToken.of(propertyType));
  }

  @SuppressWarnings("unchecked")
  @Override
  @NonNull
  public final <R1 extends R> PropertyWriter<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(propertyType())
        , "PropertyWriter is known propertyType %s,not %s ", propertyType(), propertyType);
    return (PropertyWriter<T, R1>) this;
  }

  @Override
  public final boolean isReader() {
    return false;
  }

  @Override
  public final boolean isWriter() {
    return true;
  }

  /**
   * {@link Field}实现的属性设置器
   *
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @Value
  public static class FieldElementPropertyWriter<T, R> extends AbstractPropertyWriter<T, R> {

    /**
     * 属性
     */
    @NonNull
    FieldElement<T, R> fieldElement;

    FieldElementPropertyWriter(@NonNull FieldElement<T, R> fieldElement) {
      super(fieldElement);
      Preconditions.checkArgument(ReflectionUtil.isPropertyWriter(fieldElement),
          "%s is not a PropertyWriter");
      this.fieldElement = fieldElement;
    }

    @Override
    public void writeInternal(@NonNull T obj, @NonNull R value) {
      fieldElement.set(obj, value);
    }

    @Override
    public @NonNull TypeToken<? extends R> propertyType() {
      return fieldElement.type();
    }

    @Override
    public boolean basedOnField() {
      return true;
    }

    @Override
    public @NonNull String propertyName() {
      return fieldElement.getName();
    }

  }

  /**
   * set{@link Invokable}实现的属性设置器
   *
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @Value
  public static class InvokablePropertyWriter<T, R> extends AbstractPropertyWriter<T, R> {

    /**
     * set方法
     */
    @NonNull
    Invokable<T, ?> setInvokable;
    /**
     * set方法名称风格
     */
    @NonNull
    String propertyName;

    InvokablePropertyWriter(@NonNull Invokable<T, ?> invokable,
        @NonNull String propertyName) {
      super(invokable);
      Preconditions
          .checkArgument(ReflectionUtil.isPropertyWriter(invokable), "%s is not a setInvokable",
              invokable);
      this.setInvokable = invokable;
      this.propertyName = propertyName;
    }

    @Override
    @SneakyThrows
    public void writeInternal(@NonNull T obj, @NonNull R value) {
      setInvokable.invoke(obj, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull TypeToken<? extends R> propertyType() {
      return (TypeToken<? extends R>) setInvokable.getParameters().get(0).getType();
    }

    @Override
    public boolean basedOnField() {
      return false;
    }
  }
}