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

package org.caotc.unit4j.support.common.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.caotc.unit4j.api.annotation.AmountDeserialize;
import org.caotc.unit4j.api.annotation.WithUnit;
import org.caotc.unit4j.api.annotation.WithUnit.ValueType;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.common.reflect.property.AccessibleProperty;
import org.caotc.unit4j.core.common.reflect.property.Property;
import org.caotc.unit4j.core.common.reflect.property.ReadableProperty;
import org.caotc.unit4j.core.common.reflect.property.WritableProperty;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyAccessorMethodFormat;
import org.caotc.unit4j.core.common.util.ReflectionUtil;
import org.caotc.unit4j.core.constant.StringConstant;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.support.exception.NotAmountPropertyException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2019-10-25
 * @since 1.0.0
 */
@UtilityClass
public class AmountUtil {

    private static final TypeToken<Amount> AMOUNT_TYPE_TOKEN = TypeToken.of(Amount.class);

    public static void checkAmountProperty(@NonNull Property<?, ?> amountProperty) {
        Preconditions.checkArgument(isAmountProperty(amountProperty), "%s is not a AmountProperty",
                amountProperty);
        Preconditions.checkState(!amountProperty.annotation(AmountDeserialize.class)
                        .map(AmountDeserialize::targetUnitId)
                        .filter(targetUnitId -> !targetUnitId.isEmpty())
                        .isPresent() ||
                        !amountProperty.annotation(WithUnit.class)
                                .filter(withUnit -> ValueType.ID == withUnit.valueType())
                                .map(WithUnit::value)
                                .isPresent(),
                "targetUnitId of AmountDeserialize and ID valueType of WithUnit cannot exist at the same time");
    }

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
     * @param clazz           需要获取可写{@link org.caotc.unit4j.core.Amount}属性的类
     * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
     * @return 可写{@link org.caotc.unit4j.core.Amount}属性{@link WritableProperty}集合
     * @author caotc
     * @date 2019-10-26
     * @since 1.0.0
     */
    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> amountWritablePropertiesFromClass(
            @NonNull Class<T> clazz, boolean fieldExistCheck) {
        return amountWritablePropertiesFromClass(clazz, fieldExistCheck,
                PropertyAccessorMethodFormat.values());
    }

    /**
     * 从传入的类中获取包括所有超类和接口的可写{@link org.caotc.unit4j.core.Amount}属性{@link WritableProperty}集合
     *
     * @param clazz                         需要获取可写{@link org.caotc.unit4j.core.Amount}属性的类
     * @param fieldExistCheck               是否检查是否有对应{@link Field}存在
     * @param propertyAccessorMethodFormats 属性写方法格式集合
     * @return 可写{@link org.caotc.unit4j.core.Amount}属性{@link WritableProperty}集合
     * @author caotc
     * @date 2019-10-26
     * @since 1.0.0
     */
    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> amountWritablePropertiesFromClass(
            @NonNull Class<T> clazz, boolean fieldExistCheck,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.writablePropertiesFromClass(clazz, fieldExistCheck,
                propertyAccessorMethodFormats)
                .stream().filter(AmountUtil::isAmountProperty)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> Stream<WritableProperty<T, ?>> writableAmountPropertyStreamFromClass(
            @NonNull Class<T> type) {
        return ReflectionUtil.writablePropertyStreamFromClass(type)
                .filter(AmountUtil::isAmountProperty);
    }

    @NonNull
    public static <T> Stream<AccessibleProperty<T, ?>> accessibleAmountPropertyStreamFromClass(
            @NonNull T object) {
        return ReflectionUtil.accessiblePropertyStreamFromClass(object)
                .filter(AmountUtil::isAmountProperty);
    }

    public static boolean isAmountProperty(@NonNull Property<?, ?> property) {
        return property.type().equals(AMOUNT_TYPE_TOKEN)
                || property.annotation(WithUnit.class).isPresent();
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

    @SuppressWarnings("unchecked")
    public static <T> void writeAmount(
            @NonNull WritableProperty<T, ?> amountWritableProperty,
            @NonNull T object, @NonNull Amount amount) {
        checkAmountProperty(amountWritableProperty);
        Optional<Unit> targetUnit = readDeserializeTargetUnit(amountWritableProperty);
        TypeToken<?> typeToken = amountWritableProperty.type();
        boolean isAmountType = typeToken.equals(TypeToken.of(Amount.class));
        //TODO 配置对象指定功能
        Amount actualAmount = targetUnit.map(amount::convertTo).orElse(amount);
        if (isAmountType) {
            WritableProperty<T, Amount> actual = (WritableProperty<T, Amount>) amountWritableProperty;
            actual.write(object, actualAmount);
        } else {
            WritableProperty<T, Object> actual = (WritableProperty<T, Object>) amountWritableProperty;
            //TODO 上下文配置
            Object value = actualAmount.value(typeToken.getRawType(), MathContext.UNLIMITED);
            actual.write(object, value);
            Optional<WithUnit> withUnit = amountWritableProperty.annotation(WithUnit.class);
            if (withUnit.isPresent() &&
                    withUnit.get().valueType() == ValueType.PROPERTY_NAME) {
                WritableProperty<T, String> unitWritableProperty = ReflectionUtil.writablePropertyFromClassExact((Class<T>) object.getClass(), withUnit.get().value());
                unitWritableProperty.write(object, actualAmount.unit().id());
            }
        }
    }

    @NonNull
    public static <T> Optional<Unit> readDeserializeTargetUnit(
            @NonNull Property<T, ?> amountProperty) {
        checkAmountProperty(amountProperty);
        Optional<Unit> fixedUnit = amountProperty.annotation(WithUnit.class)
                .filter(withUnit -> ValueType.ID.equals(withUnit.valueType()))
                .map(WithUnit::value)
                //TODO 配置对象定制
                .map(Configuration::getUnitByIdExact);
        if (fixedUnit.isPresent()) {
            return fixedUnit;
        }
        if (amountProperty.annotation(AmountDeserialize.class).isPresent()) {
            return amountProperty.annotation(AmountDeserialize.class)
                    .map(AmountDeserialize::targetUnitId)
                    .filter(targetUnitId -> !StringConstant.EMPTY.equals(targetUnitId))
                    //TODO 配置对象定制
                    .map(Configuration::getUnitByIdExact);
        }
        return Optional.empty();
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
    public static <T> Optional<? extends ReadableProperty<T, ?>> readableUnitProperty(
            @NonNull ReadableProperty<T, ?> amountReadableProperty, @NonNull T object) {
        if (amountReadableProperty.type().getRawType().equals(Amount.class)) {
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
