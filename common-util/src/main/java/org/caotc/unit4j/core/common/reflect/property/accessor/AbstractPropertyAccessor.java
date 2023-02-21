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
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import org.caotc.unit4j.core.common.reflect.Element;
import org.caotc.unit4j.core.common.reflect.FieldElement;
import org.caotc.unit4j.core.common.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * @param <O> owner type
 * @param <P> property type
 * @author caotc
 * @date 2019-11-23
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = false)
public abstract class AbstractPropertyAccessor<O, P> extends AbstractPropertyElement<O, P> implements
        PropertyAccessor<O, P> {

  protected AbstractPropertyAccessor(@NonNull Element element) {
    super(element);
  }

  @NonNull
  public static <O, P> PropertyAccessor<O, P> from(@NonNull Type ownerType, @NonNull Field field) {
    return PropertyElement.<O, P>from(ownerType, field).toAccessor();
  }

  @NonNull
  public static <O, P> PropertyAccessor<O, P> from(@NonNull Class<O> ownerClass, @NonNull Field field) {
    return PropertyElement.<O, P>from(ownerClass, field).toAccessor();
  }

  /**
   * 工厂方法
   *
   * @param field 属性
   * @return this
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @NonNull
  public static <O, P> PropertyAccessor<O, P> from(@NonNull TypeToken<O> ownerType, @NonNull Field field) {
    return PropertyElement.<O, P>from(ownerType, field).toAccessor();
  }

  @Override
  @NonNull
  public final Optional<P> read(@NonNull O object) {
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
  protected abstract Optional<P> readInternal(@NonNull O object);

  @Override
  @NonNull
  public final PropertyAccessor<O, P> write(@NonNull O object, @NonNull P value) {
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
   * @param value  设置的属性值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  protected abstract void writeInternal(@NonNull O object, @NonNull P value);

  @Override
  @NonNull
  public final <P1 extends P> PropertyAccessor<O, P1> propertyType(
          @NonNull Class<P1> propertyType) {
    return propertyType(TypeToken.of(propertyType));
  }

  @SuppressWarnings("unchecked")
  @Override
  @NonNull
  public final <P1 extends P> PropertyAccessor<O, P1> propertyType(
          @NonNull TypeToken<P1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(propertyType())
            , "PropertyAccessor is known propertyType %s,not %s ", propertyType(), propertyType);
    return (PropertyAccessor<O, P1>) this;
  }

  @Override
  public @NonNull <O1> PropertyAccessor<O1, P> ownBy(@NonNull TypeToken<O1> ownerType) {
    if (!canOwnBy(ownerType)) {
      throw new IllegalArgumentException(String.format("%s is can not own by %s", this, ownerType));
    }
    return ownByInternal(ownerType);
  }

  @NonNull
  protected abstract <O1> PropertyAccessor<O1, P> ownByInternal(@NonNull TypeToken<O1> ownerType);

  /**
   * {@link Field}实现的属性获取器
   *
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = false)
  public static class FieldElementPropertyAccessor<O, P> extends AbstractPropertyAccessor<O, P> {
    @NonNull
    TypeToken<O> ownerType;
    /**
     * 属性
     */
    @NonNull
    FieldElement field;

    FieldElementPropertyAccessor(@NonNull TypeToken<O> ownerType, @NonNull FieldElement field) {
      super(field);
      Preconditions.checkArgument(ReflectionUtil.isPropertyWriter(field), "%s is not a PropertyWriter", field);
      Preconditions.checkArgument(ReflectionUtil.isPropertyReader(field), "%s is not a PropertyReader", field);
      this.ownerType = ownerType;
      this.field = field;
    }


    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public Optional<P> readInternal(@NonNull O object) {
      return Optional.ofNullable((P) field.get(object));
    }

    @Override
    protected void writeInternal(@NonNull O object, @NonNull P value) {
      field.set(object, value);
    }


    @SuppressWarnings("unchecked")
    @Override
    @NonNull
    public TypeToken<? extends P> propertyType() {
      return (TypeToken<? extends P>) ownerType().resolveType(field().genericType());
    }

    @Override
    protected @NonNull <O1> PropertyAccessor<O1, P> ownByInternal(@NonNull TypeToken<O1> ownerType) {
      return new FieldElementPropertyAccessor<>(ownerType, field());
    }

    @Override
    public @NonNull String propertyName() {
      return field.getName();
    }

  }
}
