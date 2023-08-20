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

package org.caotc.unit4j.core.convert;

import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.fraction.BigFraction;
import org.caotc.unit4j.core.math.number.Number;
import org.caotc.unit4j.core.math.number.Numbers;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 单位转换配置类
 *
 * @author caotc
 * @date 2018-03-29
 * @since 1.0.0
 **/
@Value
@Builder
@With
@Slf4j
public class UnitConvertConfig {

    /**
     * 空对象
     */
    private static final UnitConvertConfig EMPTY = builder().ratio(Numbers.ONE).build();

    /**
     * 空对象
     *
     * @return 空对象
     * @author caotc
     * @date 2019-05-24
     * @since 1.0.0
     */
    @NonNull
    public static UnitConvertConfig empty() {
        return EMPTY;
    }

    /**
     * 单位转换比例
     */
    @NonNull
    Number ratio;
    /**
     * 单位之间的常量差值(以源单位为标准,所以先加常数运算再比例运算)
     */
    @lombok.Builder.Default
    @NonNull
    Number constantDifference = Numbers.ZERO;

    /**
     * 工厂方法
     *
     * @param ratio 单位转换比例
     * @return 单位转换配置类
     * @author caotc
     * @date 2019-05-24
     * @since 1.0.0
     */
    @NonNull
    public static UnitConvertConfig of(@NonNull BigDecimal ratio) {
        return builder().ratio(Numbers.valueOf(ratio)).constantDifference(Numbers.ZERO).build();
    }

    /**
     * 工厂方法
     *
     * @param ratio 单位转换比例
     * @return 单位转换配置类
     * @author caotc
     * @date 2019-05-24
     * @since 1.0.0
     */
    @NonNull
    public static UnitConvertConfig of(@NonNull BigFraction ratio) {
        return builder().ratio(Numbers.valueOf(ratio)).constantDifference(Numbers.ZERO).build();
    }

    @NonNull
    public static UnitConvertConfig of(@NonNull Number ratio) {
        return builder().ratio(ratio).constantDifference(Numbers.ZERO).build();
    }

    @NonNull
    public static InternalBuilder builder() {
        return new InternalBuilder();
    }

    /**
     * 该类对象的合并方法
     *
     * @param other 需要合并的对象
     * @return 合并后的对象
     * @author caotc
     * @date 2019-05-24
     * @since 1.0.0
     */
    @NonNull
    public UnitConvertConfig reduce(@NonNull UnitConvertConfig other) {
        Number newZeroDifference = constantDifference().add(other.constantDifference().divide(ratio()));
        Number newRatio = ratio().multiply(other.ratio());
        return builder().ratio(newRatio).constantDifference(newZeroDifference).build();
    }

    /**
     * 倒数
     *
     * @return 当前对象的倒数, 即源单位和目标单位互换后的单位转换配置对象
     * @author caotc
     * @date 2019-05-24
     * @since 1.0.0
     */
    @NonNull
    public UnitConvertConfig reciprocal() {
        Number newRatio = ratio().reciprocal();
        return builder().ratio(newRatio).constantDifference(constantDifference().negate().multiply(newRatio))
                .build();
    }

    /**
     * 与数值对象相乘的方法
     *
     * @param multiplicand 被乘数
     * @return 单位转换比例和单位之间的零点差值使用乘积的单位转换配置对象
     * @author caotc
     * @date 2019-05-24
     * @since 1.0.0
     */
    @NonNull
    public UnitConvertConfig multiply(@NonNull Number multiplicand) {
        return builder().ratio(ratio().multiply(multiplicand)).constantDifference(constantDifference().multiply(multiplicand)).build();
    }

    /**
     * 与数值对象相除的方法
     *
     * @param divisor 除数
     * @return 单位转换比例和单位之间的零点差值使用商的单位转换配置对象
     * @author caotc
     * @date 2019-05-24
     * @since 1.0.0
     */
    @NonNull
    public UnitConvertConfig divide(@NonNull Number divisor) {
        return builder().ratio(ratio().divide(divisor)).constantDifference(constantDifference().divide(divisor)).build();
    }

    /**
     * {@code exponent}次幂
     *
     * @param exponent 指数
     * @return {@code exponent}次幂后的单位转换配置对象
     * @author caotc
     * @date 2019-05-25
     * @since 1.0.0
     */
    @NonNull
    public UnitConvertConfig pow(int exponent) {
        if (exponent == 1) {
            return this;
        }
        //有常量差值时不能指数计算
        Preconditions.checkState(isConstantDifferenceZero(), "constant difference is not 0");
        return of(ratio().pow(exponent));
    }

    /**
     * 运算
     *
     * @param value 需要运算的值
     * @return 运算后的值
     * @author caotc
     * @date 2019-05-25
     * @since 1.0.0
     */
    @NonNull
    public Number apply(@NonNull Number value) {
        return value.add(constantDifference()).multiply(ratio());
    }

    /**
     * 单位之间零点是否相同
     * todo 方法命名
     *
     * @return 单位之间零点是否相同
     * @author caotc
     * @date 2019-05-25
     * @since 1.0.0
     */
    public boolean isConstantDifferenceZero() {
        return Numbers.ZERO.compareTo(constantDifference()) == 0;
    }

    /**
     * 该对象是否为空对象
     *
     * @return 该对象是否为空对象
     * @author caotc
     * @date 2019-05-25
     * @since 1.0.0
     */
    public boolean isEmpty() {
        return Numbers.ONE.compareTo(ratio()) == 0
                && Numbers.ZERO.compareTo(constantDifference()) == 0;
    }

    @NonNull
    public UnitConvertConfig.InternalBuilder toBuilder() {
        return builder().ratio(this.ratio).constantDifference(this.constantDifference);
    }

    public static class InternalBuilder extends UnitConvertConfigBuilder {
        @Override
        public InternalBuilder ratio(@NonNull Number ratio) {
            super.ratio(ratio);
            return this;
        }

        @Override
        public InternalBuilder constantDifference(@NonNull Number constantDifference) {
            super.constantDifference(constantDifference);
            return this;
        }

        @NonNull
        public InternalBuilder ratio(long ratio) {
            return ratio(Numbers.valueOf(ratio));
        }

        @NonNull
        public InternalBuilder ratio(@NonNull BigInteger ratio) {
            return ratio(Numbers.valueOf(ratio));
        }

        @NonNull
        public InternalBuilder ratio(@NonNull String ratio) {
            return ratio(Numbers.valueOf(ratio));
        }

        @NonNull
        public InternalBuilder ratio(@NonNull BigDecimal ratio) {
            return ratio(Numbers.valueOf(ratio));
        }

        @NonNull
        public InternalBuilder ratio(@NonNull BigFraction ratio) {
            return ratio(Numbers.valueOf(ratio));
        }

        @NonNull
        public InternalBuilder constantDifference(long constantDifference) {
            return constantDifference(Numbers.valueOf(constantDifference));
        }

        @NonNull
        public InternalBuilder constantDifference(@NonNull BigInteger constantDifference) {
            return constantDifference(Numbers.valueOf(constantDifference));
        }

        @NonNull
        public InternalBuilder constantDifference(@NonNull String constantDifference) {
            return constantDifference(Numbers.valueOf(constantDifference));
        }

        @NonNull
        public InternalBuilder constantDifference(@NonNull BigDecimal constantDifference) {
            return constantDifference(Numbers.valueOf(constantDifference));
        }

        @NonNull
        public InternalBuilder constantDifference(@NonNull BigFraction constantDifference) {
            return constantDifference(Numbers.valueOf(constantDifference));
        }
    }
}
