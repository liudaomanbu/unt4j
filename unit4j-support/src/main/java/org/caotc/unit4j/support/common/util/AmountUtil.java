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

package org.caotc.unit4j.support.common.util;

import com.google.common.collect.ImmutableSet;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.common.reflect.MethodNameStyle;
import org.caotc.unit4j.core.common.reflect.ReadableProperty;
import org.caotc.unit4j.core.common.reflect.WritableProperty;
import org.caotc.unit4j.core.common.util.ReflectionUtil;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.support.annotation.WithUnit;
import org.caotc.unit4j.support.annotation.WithUnit.ValueType;
import org.caotc.unit4j.support.exception.NotAmountPropertyException;

/**
 * @author caotc
 * @date 2019-10-25
 * @since 1.0.0
 */
@UtilityClass
public class AmountUtil {

  /**
   * 从传入的类中获取包括所有超类和接口的可写{@link org.caotc.unit4j.core.Amount}属性{@link WritableProperty}集合
   *
   * @param clazz 需要获取可写{@link org.caotc.unit4j.core.Amount}属性的类
   * @return 可写{@link org.caotc.unit4j.core.Amount}属性{@link WritableProperty}集合
   * @author caotc
   * @date 2019-10-26
   * @since 1.0.0
   */
  @NonNull
  public static <T> ImmutableSet<WritableProperty<T, ?>> amountWritablePropertiesFromClass(
      @NonNull Class<T> clazz) {
    return amountWritablePropertiesFromClass(clazz, true);
  }

  /**
   * 从传入的类中获取包括所有超类和接口的可写{@link org.caotc.unit4j.core.Amount}属性{@link WritableProperty}集合
   *
   * @param clazz 需要获取可写{@link org.caotc.unit4j.core.Amount}属性的类
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @return 可写{@link org.caotc.unit4j.core.Amount}属性{@link WritableProperty}集合
   * @author caotc
   * @date 2019-10-26
   * @since 1.0.0
   */
  @NonNull
  public static <T> ImmutableSet<WritableProperty<T, ?>> amountWritablePropertiesFromClass(
      @NonNull Class<T> clazz, boolean fieldExistCheck) {
    return amountWritablePropertiesFromClass(clazz, fieldExistCheck, MethodNameStyle.values());
  }

  /**
   * 从传入的类中获取包括所有超类和接口的可写{@link org.caotc.unit4j.core.Amount}属性{@link WritableProperty}集合
   *
   * @param clazz 需要获取可写{@link org.caotc.unit4j.core.Amount}属性的类
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @param methodNameStyles 属性写方法格式集合
   * @return 可写{@link org.caotc.unit4j.core.Amount}属性{@link WritableProperty}集合
   * @author caotc
   * @date 2019-10-26
   * @since 1.0.0
   */
  @NonNull
  public static <T> ImmutableSet<WritableProperty<T, ?>> amountWritablePropertiesFromClass(
      @NonNull Class<T> clazz, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {
    return ReflectionUtil.writablePropertiesFromClass(clazz, fieldExistCheck, methodNameStyles)
        .stream().filter(writableProperty ->
            Amount.class.equals(writableProperty.propertyType().getRawType())
                || writableProperty.annotation(WithUnit.class).isPresent())
        .collect(ImmutableSet.toImmutableSet());
  }

  public static boolean isAmount(@NonNull ReadableProperty<?, ?> readableProperty) {
    return readableProperty.propertyType().getRawType().equals(Amount.class)
        || readableProperty.annotation(WithUnit.class).isPresent();
  }

  @NonNull
  public static <T> Optional<Amount> readAmount(
      @NonNull ReadableProperty<T, ?> amountReadableProperty,
      @NonNull T object) {
    Optional<?> optional = amountReadableProperty.read(object);
    if (!optional.isPresent()) {
      return Optional.empty();
    }

    Object value = optional.get();
    if (value instanceof Amount) {
      return Optional.of((Amount) value);
    }

    WithUnit withUnit = amountReadableProperty.annotation(WithUnit.class).orElseThrow(
        () -> NotAmountPropertyException.create(amountReadableProperty));
    Unit unit = readUnit(withUnit, object);

    if (value instanceof BigDecimal) {
      return Optional.of(Amount.create((BigDecimal) value, unit));
    }

    if (value instanceof BigInteger) {
      return Optional.of(Amount.create((BigInteger) value, unit));
    }

    if (value instanceof String) {
      return Optional.of(Amount.create((String) value, unit));
    }

    if (value instanceof Long) {
      return Optional.of(Amount.create((Long) value, unit));
    }
    throw new IllegalArgumentException();
  }

  @NonNull
  public static <T> Unit readUnit(
      @NonNull WithUnit withUnit,
      @NonNull T object) {
    String unitId;
    if (withUnit.valueType() == ValueType.ID) {
      unitId = withUnit.value();
    } else {
      @SuppressWarnings("unchecked")
      ReadableProperty<T, String> unitIdReadableProperty = ReflectionUtil
          .readablePropertyFromClassExact(
              (Class<T>) object.getClass(), withUnit.value());
      unitId = unitIdReadableProperty.readExact(object);
    }
    return Configuration.getUnitByIdExact(unitId);
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public static <T> Optional<? extends ReadableProperty<T, ?>> unitReadableProperty(
      @NonNull ReadableProperty<T, ?> amountReadableProperty, @NonNull T object) {
    if (amountReadableProperty.propertyType().getRawType().equals(Amount.class)) {
      return Optional.empty();
    }
    WithUnit withUnit = amountReadableProperty.annotation(WithUnit.class).orElseThrow(
        () -> NotAmountPropertyException.create(amountReadableProperty));
    if (withUnit.valueType() == ValueType.ID) {
      return Optional.empty();
    }
    return ReflectionUtil
        .readablePropertyFromClass((Class<T>) object.getClass(), withUnit.value());
  }
}
