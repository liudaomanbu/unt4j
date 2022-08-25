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
import com.google.common.reflect.TypeToken;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.common.reflect.FieldElement;
import org.caotc.unit4j.core.common.util.ReflectionUtil;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Optional;

/**
 * @author caotc
 * @date 2019-11-23
 * @since 1.0.0
 */
@Data
public abstract class PropertyAccessor<T, R> extends AbstractPropertyElement<T, R> implements
        PropertyReader<T, R>, PropertyWriter<T, R> {

  /**
   * 工厂方法
   *
   * @param field 属性
   * @return this
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public static <T, R> PropertyAccessor<T, R> from(@NonNull Field field) {
    return from(FieldElement.of(field));
  }

  /**
   * @author caotc
   * @date 2019-12-08
   * @implNote
   * @implSpec
   * @apiNote
   * @since 1.0.0
   */
  @NonNull
  public static <T, R> PropertyAccessor<T, R> from(@NonNull FieldElement<T, R> fieldElement) {
    return new FieldElementPropertyAccessor<>(fieldElement);
  }

  protected <M extends AccessibleObject & Member> PropertyAccessor(
      @NonNull M member) {
    super(member);
  }

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

  @Override
  @NonNull
  public final PropertyAccessor<T, R> write(@NonNull T object, @NonNull R value) {
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

  @Override
  @NonNull
  public final <R1 extends R> PropertyAccessor<T, R1> propertyType(
      @NonNull Class<R1> propertyType) {
    return propertyType(TypeToken.of(propertyType));
  }

  @SuppressWarnings("unchecked")
  @Override
  @NonNull
  public final <R1 extends R> PropertyAccessor<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(propertyType())
        , "PropertyAccessor is known propertyType %s,not %s ", propertyType(), propertyType);
    return (PropertyAccessor<T, R1>) this;
  }


  /**
   * {@link Field}实现的属性获取器
   *
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @Value
  public static class FieldElementPropertyAccessor<T, R> extends PropertyAccessor<T, R> {

    /**
     * 属性
     */
    @NonNull
    FieldElement<T, R> fieldElement;

    FieldElementPropertyAccessor(@NonNull FieldElement<T, R> fieldElement) {
      super(fieldElement);
      Preconditions.checkArgument(ReflectionUtil.isPropertyWriter(fieldElement),
          "%s is not a PropertyWriter");
      this.fieldElement = fieldElement;
    }

    @NonNull
    @Override
    public Optional<R> readInternal(@NonNull T object) {
      return Optional.ofNullable(fieldElement.get(object));
    }

    @Override
    protected void writeInternal(@NonNull T object, @NonNull R value) {
      fieldElement.set(object, value);
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
}
