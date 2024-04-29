/*
 * Copyright (C) 2023 the original author or authors.
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
import org.caotc.unit4j.api.annotation.WithUnit;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.common.reflect.property.AccessibleProperty;
import org.caotc.unit4j.core.common.reflect.property.Property;
import org.caotc.unit4j.core.common.reflect.property.ReadableProperty;
import org.caotc.unit4j.core.common.reflect.property.WritableProperty;
import org.caotc.unit4j.core.common.util.ReflectionUtil;
import org.caotc.unit4j.core.exception.ReadablePropertyNotFoundException;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.support.common.property.AccessibleWithQuantityProperty;
import org.caotc.unit4j.support.common.property.ReadableWithQuantityProperty;
import org.caotc.unit4j.support.common.property.WritableWithQuantityProperty;
import org.caotc.unit4j.support.exception.NotQuantityPropertyException;

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
public class QuantityUtil {

    private static final TypeToken<Quantity> QUANTITY_TYPE_TOKEN = TypeToken.of(Quantity.class);

    public static void checkQuantityProperty(@NonNull Property<?, ?> quantityProperty) {
        Preconditions.checkArgument(isQuantityProperty(quantityProperty), "%s is not a QuantityProperty",
                quantityProperty);
    }

    /**
     * 从传入的类中获取包括所有超类和接口的可写{@link Quantity}属性{@link WritableProperty}集合
     *
     * @param clazz 需要获取可写{@link Quantity}属性的类
     * @return 可写{@link org.caotc.unit4j.core.Quantity}属性{@link WritableProperty}集合
     * @author caotc
     * @date 2019-10-26
     * @since 1.0.0
     */
    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, Quantity>> writableQuantityPropertiesFromClass(
            @NonNull Class<T> clazz) {
        return writableQuantityPropertyStream(clazz).collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ReadableProperty<T, Quantity> readableQuantityPropertyExact(
            @NonNull Class<T> type, @NonNull String fieldName) {
        ReadableProperty<T, ?> readableProperty = ReflectionUtil
                .readablePropertyExact(type, fieldName);
        if (!isQuantityProperty(readableProperty)) {
            throw ReadablePropertyNotFoundException
                    .create(TypeToken.of(type), fieldName);
        }
        return warp(readableProperty);
    }

    @NonNull
    public static <T> Optional<ReadableProperty<T, Quantity>> readableQuantityProperty(
            @NonNull Class<T> type, @NonNull String fieldName) {
        return ReflectionUtil.readableProperty(type, fieldName)
                .filter(QuantityUtil::isQuantityProperty)
                .map(QuantityUtil::warp);
    }

    @NonNull
    public static <T> Stream<ReadableProperty<T, Quantity>> readableQuantityPropertyStream(
            @NonNull Class<T> type) {
        return ReflectionUtil.readablePropertyStream(type)
                .filter(QuantityUtil::isQuantityProperty)
                .map(QuantityUtil::warp);
    }

    @NonNull
    public static <T> Stream<WritableProperty<T, Quantity>> writableQuantityPropertyStream(
            @NonNull Class<T> type) {
        return ReflectionUtil.writablePropertyStream(type)
                .filter(QuantityUtil::isQuantityProperty)
                .map(QuantityUtil::warp);
    }

    @NonNull
    public static <T> Stream<AccessibleProperty<T, Quantity>> accessibleQuantityPropertyStream(
            @NonNull Class<T> type) {
        return ReflectionUtil.accessiblePropertyStream(type)
                .filter(QuantityUtil::isQuantityProperty)
                .map(QuantityUtil::warp);
    }

    @NonNull
    public static <T> Stream<AccessibleProperty<T, Quantity>> accessibleQuantityPropertyStream(
            @NonNull T object) {
        return ReflectionUtil.accessiblePropertyStream(object)
                .filter(QuantityUtil::isQuantityProperty)
                .map(QuantityUtil::warp);
    }

    public static boolean isQuantityProperty(@NonNull Property<?, ?> property) {
        return property.type().equals(QUANTITY_TYPE_TOKEN)
                || property.annotation(WithUnit.class).isPresent();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private static <O, P> ReadableProperty<O, Quantity> warp(@NonNull ReadableProperty<O, P> readableValueProperty) {
        return readableValueProperty.type().equals(QUANTITY_TYPE_TOKEN) ?
                (ReadableProperty<O, Quantity>) readableValueProperty
                : new ReadableWithQuantityProperty<>(readableValueProperty);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private static <O, P> WritableProperty<O, Quantity> warp(@NonNull WritableProperty<O, P> writableValueProperty) {
        return writableValueProperty.type().equals(QUANTITY_TYPE_TOKEN) ?
                (WritableProperty<O, Quantity>) writableValueProperty
                : new WritableWithQuantityProperty<>(writableValueProperty);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private static <O, P> AccessibleProperty<O, Quantity> warp(@NonNull AccessibleProperty<O, P> accessibleValueProperty) {
        return accessibleValueProperty.type().equals(QUANTITY_TYPE_TOKEN) ?
                (AccessibleProperty<O, Quantity>) accessibleValueProperty
                : new AccessibleWithQuantityProperty<>(accessibleValueProperty);
    }

    @NonNull
    public static <T> Optional<Quantity> readQuantity(
            @NonNull ReadableProperty<T, ?> quantityReadableProperty,
            @NonNull T object) {
        Optional<?> optional = quantityReadableProperty.read(object);
        if (!optional.isPresent()) {
            return Optional.empty();
        }

        Object value = optional.get();
        if (value instanceof Quantity) {
            return Optional.of((Quantity) value);
        }

        WithUnit withUnit = quantityReadableProperty.annotation(WithUnit.class).orElseThrow(
                () -> NotQuantityPropertyException.of(quantityReadableProperty));
        Unit unit = readUnit(withUnit);

        if (value instanceof BigDecimal) {
            return Optional.of(Quantity.create((BigDecimal) value, unit));
        }

        if (value instanceof BigInteger) {
            return Optional.of(Quantity.create((BigInteger) value, unit));
        }

        if (value instanceof String) {
            return Optional.of(Quantity.create((String) value, unit));
        }

        if (value instanceof Long) {
            return Optional.of(Quantity.create(value, unit));
        }
        throw new IllegalArgumentException();
    }

    @SuppressWarnings("unchecked")
    public static <T> void writeQuantity(
            @NonNull WritableProperty<T, ?> quantityWritableProperty,
            @NonNull T object, @NonNull Quantity quantity) {
        checkQuantityProperty(quantityWritableProperty);
        Optional<Unit> targetUnit = readDeserializeTargetUnit(quantityWritableProperty);
        TypeToken<?> typeToken = quantityWritableProperty.type();
        boolean isQuantityType = typeToken.equals(TypeToken.of(Quantity.class));
        //TODO 配置对象指定功能
        Quantity actualQuantity = targetUnit.map(quantity::convertTo).orElse(quantity);
        if (isQuantityType) {
            WritableProperty<T, Quantity> actual = (WritableProperty<T, Quantity>) quantityWritableProperty;
            actual.write(object, actualQuantity);
        } else {
            WritableProperty<T, Object> actual = (WritableProperty<T, Object>) quantityWritableProperty;
            //TODO 上下文配置
            Object value = actualQuantity.value(typeToken.getRawType(), MathContext.UNLIMITED);
            actual.write(object, value);
            Optional<WithUnit> withUnit = quantityWritableProperty.annotation(WithUnit.class);
        }
    }

    @NonNull
    public static <T> Optional<Unit> readDeserializeTargetUnit(
            @NonNull Property<T, ?> quantityProperty) {
        checkQuantityProperty(quantityProperty);
        return quantityProperty.annotation(WithUnit.class)
                .map(WithUnit::value)
                //TODO 配置对象定制
                .map(Configuration::findUnitExact);
    }

    @NonNull
    public static Unit readUnit(
            @NonNull WithUnit withUnit) {
        String unitId = withUnit.value();
        return Configuration.findUnitExact(unitId);
    }

}
