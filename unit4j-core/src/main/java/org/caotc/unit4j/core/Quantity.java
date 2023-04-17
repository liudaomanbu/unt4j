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

package org.caotc.unit4j.core;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import lombok.experimental.FieldNameConstants;
import org.caotc.unit4j.core.convert.UnitConvertConfig;
import org.caotc.unit4j.core.math.number.AbstractNumber;
import org.caotc.unit4j.core.math.number.BigDecimal;
import org.caotc.unit4j.core.math.number.BigInteger;
import org.caotc.unit4j.core.math.number.UnkownNumber;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.UnknownUnit;

import java.math.MathContext;

/**
 * 数量类.数值+单位=数量.
 *
 * @author caotc
 * @date 2018-04-12
 * @since 1.0.0
 **/
@With
@Value(staticConstructor = "create")
@FieldNameConstants
public class Quantity {
    public static final Quantity UNKNOWN = create(UnkownNumber.INSTANCE, UnknownUnit.INSTANCE);

    @NonNull
    public static Quantity create(@NonNull Object value, @NonNull Unit unit) {
        if (value instanceof java.math.BigDecimal) {
            return create((java.math.BigDecimal) value, unit);
        }
        if (value instanceof java.math.BigInteger) {
            return create((java.math.BigInteger) value, unit);
        }
        if (value instanceof Long) {
            return create(((Long) value).longValue(), unit);
        }
        if (value instanceof Integer) {
            return create(((Integer) value).longValue(), unit);
        }
        if (value instanceof Short) {
            return create(((Short) value).longValue(), unit);
        }
        if (value instanceof Byte) {
            return create(((Byte) value).longValue(), unit);
        }
        if (value instanceof String) {
            return create((String) value, unit);
        }
        if (value instanceof AbstractNumber) {
            return create((AbstractNumber) value, unit);
        }
        throw new IllegalArgumentException(value + " can't convert to Number");
    }

    /**
     * 工厂方法
     *
     * @param value 数值
     * @param unit  单位
     * @return 创建的数量对象
     * @author caotc
     * @date 2019-04-23
     * @since 1.0.0
     */
    @NonNull
    public static Quantity create(@NonNull java.math.BigDecimal value, @NonNull Unit unit) {
        return create(BigDecimal.valueOf(value), unit);
    }

    /**
     * 工厂方法
     *
     * @param value 数值
     * @param unit  单位
     * @return 创建的数量对象
     * @author caotc
     * @date 2019-04-23
     * @since 1.0.0
     */
    @NonNull
    public static Quantity create(@NonNull java.math.BigInteger value, @NonNull Unit unit) {
        return create(BigInteger.valueOf(value), unit);
    }

    /**
     * 工厂方法
     *
     * @param value 数值
     * @param unit  单位
     * @return 创建的数量对象
     * @author caotc
     * @date 2019-04-23
     * @since 1.0.0
     */
    @NonNull
    public static Quantity create(long value, @NonNull Unit unit) {
        return create(BigInteger.valueOf(value), unit);
    }

    /**
     * 工厂方法
     *
     * @param value 数值
     * @param unit  单位
     * @return 创建的数量对象
     * @author caotc
     * @date 2019-04-23
     * @since 1.0.0
     */
    @NonNull
    public static Quantity create(@NonNull String value, @NonNull Unit unit) {
        return create(BigDecimal.valueOf(value), unit);
    }

    @NonNull
    AbstractNumber value;
    @NonNull
    Unit unit;

    /**
     * 转换至目标单位
     *
     * @param targetUnit 目标单位
     * @return 转换至目标单位的数量对象
     * @author caotc
     * @date 2018-12-01
     * @apiNote 使用的配置为 {@link Configuration#defaultInstance()}
     * @since 1.0.0
     */
    @NonNull
    public Quantity convertTo(@NonNull Unit targetUnit) {
        return convertTo(targetUnit, Configuration.defaultInstance());
    }

    /**
     * 使用参数的配置,转换至目标单位
     *
     * @param targetUnit    目标单位
     * @param configuration 配置
     * @return 转换至目标单位的数量对象
     * @author caotc
     * @date 2018-12-01
     * @since 1.0.0
     */
    @NonNull
    public Quantity convertTo(@NonNull Unit targetUnit, @NonNull Configuration configuration) {
        if (targetUnit.equals(this.unit())) {
            return this;
        }

        UnitConvertConfig convertConfig = configuration.getConvertConfig(this.unit(),
                targetUnit);
        return Quantity.create(convertConfig.apply(value), targetUnit);
    }

    /**
     * 自动转换单位
     *
     * @return 自动转换单位后的数量对象
     * @author caotc
     * @date 2018-12-01
     * @apiNote 使用的配置为 {@link Configuration#defaultInstance()}
     * @since 1.0.0
     */
    @NonNull
    public Quantity autoConvert() {
        return autoConvert(Configuration.defaultInstance());
    }


    /**
     * 使用参数的配置,自动转换单位
     *
     * @param configuration 配置
     * @return 自动转换单位后的数量对象
     * @author caotc
     * @date 2019-01-10
     * @since 1.0.0
     */
    @NonNull
    public Quantity autoConvert(@NonNull Configuration configuration) {
        return convertTo(configuration.targetUnitChooser().targetUnitFromAmount(this, configuration),
                configuration);
    }

    /**
     * 加法{@code (this + augend)}
     *
     * @param augend 被加数
     * @return {@code this + augend}
     * @throws IllegalArgumentException 如果两个对象的类型不同
     * @author caotc
     * @date 2019-01-10
     * @apiNote 使用的配置为 {@link Configuration#defaultInstance()}
     * @since 1.0.0
     */
    @NonNull
    public Quantity add(@NonNull Quantity augend) {
        return add(augend, Configuration.defaultInstance());
    }

    /**
     * 加法{@code (this + augend)}
     *
     * @param augend        被加数
     * @param configuration 配置
     * @return {@code this + augend}
     * @throws IllegalArgumentException 如果两个对象的类型不同
     * @author caotc
     * @date 2019-01-10
     * @since 1.0.0
     */
    @NonNull
    public Quantity add(@NonNull Quantity augend, @NonNull Configuration configuration) {
        Preconditions.checkArgument(
                this.unit().type().equals(augend.unit().type()),
                "%s and %s can't add,%s and %s are not type equal",
                this, augend, this.unit(), augend.unit());
        augend = augend.convertTo(this.unit(), configuration);
        return create(value.add(augend.value), this.unit());
    }

    /**
     * 减法{@code (this - subtrahend)}
     *
     * @param subtrahend 减数
     * @return {@code this - subtrahend}
     * @throws IllegalArgumentException 如果两个对象的类型不同
     * @author caotc
     * @date 2019-01-10
     * @apiNote 使用的配置为 {@link Configuration#defaultInstance()}
     * @since 1.0.0
     */
    @NonNull
    public Quantity subtract(@NonNull Quantity subtrahend) {
        return subtract(subtrahend, Configuration.defaultInstance());
    }

    /**
     * 减法{@code (this - subtrahend)}
     *
     * @param subtrahend    减数
     * @param configuration 配置
     * @return {@code this - subtrahend}
     * @throws IllegalArgumentException 如果两个对象的类型不同
     * @author caotc
     * @date 2019-01-10
     * @since 1.0.0
     */
    @NonNull
    public Quantity subtract(@NonNull Quantity subtrahend,
                             @NonNull Configuration configuration) {
        Preconditions.checkArgument(
                this.unit().type().equals(subtrahend.unit().type()),
                "%s and %s can't subtract,%s and %s are not type equal",
                this, subtrahend, this.unit(), subtrahend.unit());
        subtrahend = subtrahend.convertTo(this.unit(), configuration);
        return create(value.subtract(subtrahend.value), this.unit());
    }

    /**
     * 乘法{@code this * multiplicand}
     *
     * @param multiplicand 被乘数
     * @return {@code this * multiplicand}
     * @author caotc
     * @date 2019-01-10
     * @apiNote 使用的配置为 {@link Configuration#defaultInstance()}
     * @since 1.0.0
     */
    @NonNull
    public Quantity multiply(@NonNull Quantity multiplicand) {
        return multiply(multiplicand, Configuration.defaultInstance());
    }

    /**
     * 乘法{@code this * multiplicand}
     *
     * @param multiplicand  被乘数
     * @param configuration 配置
     * @return {@code this * multiplicand}
     * @author caotc
     * @date 2019-01-10
     * @since 1.0.0
     */
    @NonNull
    public Quantity multiply(@NonNull Quantity multiplicand,
                             @NonNull Configuration configuration) {
        return create(value.multiply(multiplicand.value),
                this.unit().multiply(multiplicand.unit()));
    }

    /**
     * 除法{@code (this / divisor)}
     *
     * @param divisor 除数
     * @return {@code this / divisor}
     * @author caotc
     * @date 2019-01-10
     * @apiNote 使用的配置为 {@link Configuration#defaultInstance()}
     * @since 1.0.0
     */
    @NonNull
    public Quantity divide(@NonNull Quantity divisor) {
        return divide(divisor, Configuration.defaultInstance());
    }

    /**
     * 除法{@code (this / divisor)}
     *
     * @param divisor       除数
     * @param configuration 配置
     * @return {@code this / divisor}
     * @author caotc
     * @date 2019-01-10
     * @since 1.0.0
     */
    @NonNull
    public Quantity divide(@NonNull Quantity divisor, @NonNull Configuration configuration) {
        return create(value.divide(divisor.value),
                this.unit().divide(divisor.unit()));
    }

    /**
     * 数值转为byte类型的值
     *
     * @return 数值转为byte类型的值
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    public byte byteValue() {
        return value.byteValue();
    }

    /**
     * 数值
     *
     * @return 数值
     * @throws ArithmeticException 如果有精度损失或超出{@link Byte}的可表达范围
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    public byte byteValueExact() {
        return value.byteValueExact();
    }

    /**
     * 数值
     *
     * @return 数值
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    public short shortValue() {
        return value.shortValue();
    }

    /**
     * 数值
     *
     * @return 数值
     * @throws ArithmeticException 如果有精度损失或超出{@link Short}的可表达范围
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    public short shortValueExact() {
        return value.shortValueExact();
    }

    /**
     * 数值
     *
     * @return 数值
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    public int intValue() {
        return value.intValue();
    }

    /**
     * 数值
     *
     * @return 数值
     * @throws ArithmeticException 如果有精度损失或超出{@link Integer}的可表达范围
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    public int intValueExact() {
        return value.intValueExact();
    }

    /**
     * 数值
     *
     * @return 数值
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    public long longValue() {
        return value.longValue();
    }

    /**
     * 数值
     *
     * @return 数值
     * @throws ArithmeticException 如果有精度损失或超出{@link Long}的可表达范围
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    public long longValueExact() {
        return value.longValueExact();
    }

    /**
     * 数值
     *
     * @return 数值
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public java.math.BigInteger bigIntegerValue() {
        return value.bigIntegerValue();
    }

    /**
     * 数值
     *
     * @return 数值
     * @throws ArithmeticException 如果有精度损失
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public java.math.BigInteger bigIntegerValueExact() {
        return value.bigIntegerValueExact();
    }

    /**
     * 数值
     *
     * @return 数值
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    public float floatValue() {
        return value.floatValue();
    }

    /**
     * 数值
     *
     * @return 数值
     * @throws ArithmeticException 如果有精度损失或超出{@link Float}的可表达范围
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    public float floatValueExact() {
        return value.floatValueExact();
    }

    /**
     * 数值
     *
     * @return 数值
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    public double doubleValue() {
        return value.doubleValue();
    }

    /**
     * 数值
     *
     * @return 数值
     * @throws ArithmeticException 如果有精度损失或超出{@link Double}的可表达范围
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    public double doubleValueExact() {
        return value.doubleValueExact();
    }

    /**
     * 数值
     *
     * @return 数值
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public java.math.BigDecimal bigDecimalValue() {
        return value.bigDecimalValue();
    }

    /**
     * 数值
     *
     * @return 数值
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public java.math.BigDecimal bigDecimalValue(@NonNull MathContext mathContext) {
        return value.bigDecimalValue(mathContext);
    }

    /**
     * 数值
     *
     * @return 数值
     * @throws ArithmeticException 如果有精度损失
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public java.math.BigDecimal bigDecimalValueExact() {
        return value.bigDecimalValueExact();
    }

    /**
     * 数值
     *
     * @param valueType   数值转换的目标类型
     * @param mathContext 数值转换时的上下文
     * @return 转换为 {@code valueType}类型的数值
     * @throws IllegalArgumentException 如果不支持传入的目标类型
     * @author caotc
     * @date 2019-05-29
     * @apiNote 目前只支持数字的基本类型和对应包装类, 以及 {@link java.math.BigInteger}和{@link java.math.BigDecimal}
     * @since 1.0.0
     */
    @NonNull
    public <T> T value(@NonNull Class<T> valueType, @NonNull MathContext mathContext) {
        return value.value(valueType, mathContext);
    }
}
