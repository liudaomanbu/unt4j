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

package org.caotc.unit4j.core.math.number;

import com.google.common.annotations.Beta;
import com.google.common.reflect.TypeToken;
import lombok.NonNull;
import org.apache.commons.math3.fraction.BigFraction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author caotc
 * @date 2019-04-02
 * @implSpec
 * @implNote
 * @apiNote
 * @since 1.0.0
 */
@Beta
public interface Number extends Comparable<Number> {
  /**
   * Indicates if this rational number is equal to zero.
   *
   * @return <code>this == 0</code>
   * @author caotc
   * @date 2019-04-04
   * @since 1.0.0
   */
  boolean isZero();

  /**
   * Indicates if this rational number is greater than zero.
   *
   * @return <code>this &gt; 0</code>
   * @author caotc
   * @date 2019-04-04
   * @since 1.0.0
   */
  boolean isPositive();

  /**
   * Indicates if this rational number is less than zero.
   *
   * @return <code>this &lt; 0</code>
   * @author caotc
   * @date 2019-04-04
   * @since 1.0.0
   */
  boolean isNegative();

  /**
   * Returns a {@code AbstractNumber} whose value is {@code (this + augend)}.
   *
   * @param augend value to be added to this {@code AbstractNumber}.
   * @return {@code this + augend}
   * @author caotc
   * @date 2019-4-6
   * @since 1.0.0
   */
  @NonNull
  Number add(@NonNull Number augend);

  /**
   * Returns a {@code AbstractNumber} whose value is {@code (this - subtrahend)}.
   *
   * @param subtrahend value to be subtracted from this {@code AbstractNumber}.
   * @return {@code this - subtrahend}
   * @author caotc
   * @date 2019-4-6
   * @since 1.0.0
   */
  @NonNull
  default Number subtract(@NonNull Number subtrahend){
    return add(subtrahend.negate());
  }

  /**
   * Returns a {@code AbstractNumber} whose value is <tt>(this &times; multiplicand)</tt>.
   *
   * @param multiplicand value to be multiplied by this {@code AbstractNumber}.
   * @return {@code this * multiplicand}
   * @author caotc
   * @date 2019-4-6
   * @since 1.0.0
   */
  @NonNull
  Number multiply(@NonNull Number multiplicand);

  /**
   * Returns a {@code AbstractNumber} whose value is {@code (this / divisor)}.
   *
   * @param divisor value by which this {@code AbstractNumber} is to be divided.
   * @return {@code this / divisor}
   * @author caotc
   * @date 2019-4-6
   * @since 1.0.0
   */
  @NonNull
  default Number divide(@NonNull Number divisor) {
    return multiply(divisor.reciprocal());
  }

  @NonNull
  default Number divide(long divisor) {
    return divide(Numbers.valueOf(divisor));
  }

  /**
   * Returns a {@code AbstractNumber} whose value is
   * <tt>(this<sup>exponent</sup>)</tt>, The power is computed exactly, to
   * unlimited precision.
   *
   * <p>The parameter {@code exponent} must be in the range 0 through
   * 999999999, inclusive.  {@code ZERO.pow(0)} returns ONE
   *
   * Note that future releases may expand the allowable exponent range of this method.
   *
   * @param exponent power to raise this {@code AbstractNumber} to.
   * @return <tt>this<sup>exponent</sup></tt>
   * @throws ArithmeticException if {@code exponent} is out of range.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull Number pow(int exponent);

  /**
   * Returns a {@code AbstractNumber} whose value is {@code (-this)}, and whose scale is {@code
   * this.scale()}.
   *
   * @return {@code -this}.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull Number negate();

  /**
   * Returns a {@code AbstractNumber} whose value is the absolute value of this {@code
   * AbstractNumber}, and whose scale is {@code this.scale()}.
   *
   * @return {@code abs(this)}
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  default Number abs() {
    return isNegative() ? negate() : this;
  }

  /**
   * 倒数
   *
   * @author caotc
   * @date 2019-04-11
   * @since 1.0.0
   */
  @NonNull
  Number reciprocal();

  @Override
  int compareTo(@NonNull Number o);

  default byte byteValue() {
    return bigDecimalValue().byteValue();
  }

  /**
   * 将该数字对象转换为{@code byte}
   *
   * @param roundingMode 舍入模式 {@code RoundingMode}
   * @return 转换后的{@code byte}
   * @throws ArithmeticException 如果值范围超过了{@code byte}范围或者舍入模式为{@code RoundingMode.UNNECESSARY}且不是整数
   * @author caotc
   * @date 2019-04-24
   * @since 1.0.0
   */
  default byte byteValue(@NonNull RoundingMode roundingMode) {
    return bigDecimalValue().setScale(0, roundingMode).byteValueExact();
  }

  /**
   * Converts this {@code AbstractNumber} to a {@code byte}, checking for lost information.  If this
   * {@code AbstractNumber} has a nonzero fractional part or is out of the possible range for a
   * {@code byte} result then an {@code ArithmeticException} is thrown.
   *
   * @return this {@code AbstractNumber} converted to a {@code byte}.
   * @throws ArithmeticException if {@code this} has a nonzero fractional part, or will not fit in a
   * {@code byte}.
   * @author caotc
   * @date 2019-04-03
   * @since 1.0.0
   */
  default byte byteValueExact() {
    return bigDecimalValueExact().byteValueExact();
  }

  default short shortValue() {
    return bigDecimalValue().shortValue();
  }

  /**
   * 将该数字对象转换为{@code short}
   *
   * @param roundingMode 舍入模式 {@code RoundingMode}
   * @return 转换后的{@code short}
   * @throws ArithmeticException 如果值范围超过了{@code short}范围或者舍入模式为{@code
   * RoundingMode.UNNECESSARY}且不是整数
   * @author caotc
   * @date 2019-04-24
   * @since 1.0.0
   */
  default short shortValue(@NonNull RoundingMode roundingMode) {
    return bigDecimalValue().setScale(0, roundingMode).shortValueExact();
  }

  /**
   * Converts this {@code AbstractNumber} to a {@code short}, checking for lost information.  If
   * this {@code AbstractNumber} has a nonzero fractional part or is out of the possible range for a
   * {@code short} result then an {@code ArithmeticException} is thrown.
   *
   * @return this {@code AbstractNumber} converted to a {@code short}.
   * @throws ArithmeticException if {@code this} has a nonzero fractional part, or will not fit in a
   * {@code short}.
   * @author caotc
   * @date 2019-04-02
   * @since 1.0.0
   */
  default short shortValueExact() {
    return bigDecimalValueExact().shortValueExact();
  }

  default int intValue() {
    return bigDecimalValue().intValue();
  }

  /**
   * 将该数字对象转换为{@code int}
   *
   * @param roundingMode 舍入模式 {@code RoundingMode}
   * @return 转换后的{@code int}
   * @throws ArithmeticException 如果值范围超过了{@code int}范围或者舍入模式为{@code RoundingMode.UNNECESSARY}且不是整数
   * @author caotc
   * @date 2019-04-24
   * @since 1.0.0
   */
  default int intValue(@NonNull RoundingMode roundingMode) {
    return bigDecimalValue().setScale(0, roundingMode).intValueExact();
  }

  /**
   * Converts this {@code AbstractNumber} to an {@code int}, checking for lost information.  If this
   * {@code AbstractNumber} has a nonzero fractional part or is out of the possible range for an
   * {@code int} result then an {@code ArithmeticException} is thrown.
   *
   * @return this {@code AbstractNumber} converted to an {@code int}.
   * @throws ArithmeticException if {@code this} has a nonzero fractional part, or will not fit in
   * an {@code int}.
   * @author caotc
   * @date 2019-04-02
   * @since 1.0.0
   */
  default int intValueExact() {
    return bigDecimalValueExact().intValueExact();
  }

  default long longValue() {
    return bigDecimalValue().longValue();
  }

  /**
   * 将该数字对象转换为{@code long}
   *
   * @param roundingMode 舍入模式 {@code RoundingMode}
   * @return 转换后的{@code long}
   * @throws ArithmeticException 如果值范围超过了{@code long}范围或者舍入模式为{@code RoundingMode.UNNECESSARY}且不是整数
   * @author caotc
   * @date 2019-04-24
   * @since 1.0.0
   */
  default long longValue(@NonNull RoundingMode roundingMode) {
    return bigDecimalValue().setScale(0, roundingMode).longValueExact();
  }

  /**
   * Converts this {@code AbstractNumber} to a {@code long}, checking for lost information.  If this
   * {@code AbstractNumber} has a nonzero fractional part or is out of the possible range for a
   * {@code long} result then an {@code ArithmeticException} is thrown.
   *
   * @return this {@code AbstractNumber} converted to a {@code long}.
   * @throws ArithmeticException if {@code this} has a nonzero fractional part, or will not fit in a
   * {@code long}.
   * @author caotc
   * @date 2019-04-02
   * @since 1.0.0
   */
  default long longValueExact() {
    return bigDecimalValueExact().longValueExact();
  }

  /**
   * Returns the value of the specified number as a {@code java.math.BigInteger}, which may involve
   * rounding or truncation.
   *
   * @return the numeric value represented by this object after conversion to type {@code
   * java.math.BigInteger}.
   * @author caotc
   * @date 2019-04-02
   * @since 1.0.0
   */
  @NonNull
  default java.math.BigInteger bigIntegerValue() {
    return bigDecimalValue().toBigInteger();
  }

  /**
   * 将该数字对象转换为{@code java.math.BigInteger}
   *
   * @param roundingMode 舍入模式 {@code RoundingMode}
   * @return 转换后的{@code java.math.BigInteger}
   * @throws ArithmeticException 如果舍入模式为{@code RoundingMode.UNNECESSARY}且不是整数
   * @author caotc
   * @date 2019-04-24
   * @since 1.0.0
   */
  @NonNull
  default java.math.BigInteger bigIntegerValue(@NonNull RoundingMode roundingMode) {
    return bigDecimalValue().setScale(0, roundingMode).toBigIntegerExact();
  }

  /**
   * Returns the value of the specified number as a {@code java.math.BigInteger}, which may involve
   * rounding or truncation.
   *
   * @return the numeric value represented by this object after conversion to type {@code
   * java.math.BigInteger}.
   * @throws ArithmeticException if {@code this} has a nonzero fractional part, or will not fit in a
   * {@code java.math.BigInteger}.
   * @author caotc
   * @date 2019-04-02
   * @since 1.0.0
   */
  @NonNull
  default java.math.BigInteger bigIntegerValueExact() {
    return bigDecimalValueExact().toBigIntegerExact();
  }

  default float floatValue() {
    return bigDecimalValue().floatValue();
  }

  /**
   * Converts this {@code AbstractNumber} to a {@code float}, checking for lost information.  If
   * this {@code AbstractNumber} has a nonzero fractional part or is out of the possible range for a
   * {@code float} result then an {@code ArithmeticException} is thrown.
   *
   * @return this {@code AbstractNumber} converted to a {@code float}.
   * @throws ArithmeticException if {@code this} has a nonzero fractional part, or will not fit in a
   * {@code float}.
   * @author caotc
   * @date 2019-04-02
   * @since 1.0.0
   */
  default float floatValueExact() {
    java.math.BigDecimal value = bigDecimalValueExact();
    float floatValue = value.floatValue();
    if (new java.math.BigDecimal(String.valueOf(floatValue)).compareTo(value) != 0) {
      throw new ArithmeticException("Precision loss when converting BigDecimal to float");
    }
    return floatValue;
  }

  default double doubleValue() {
    return bigDecimalValue().doubleValue();
  }

  /**
   * Converts this {@code AbstractNumber} to a {@code double}, checking for lost information.  If
   * this {@code AbstractNumber} has a nonzero fractional part or is out of the possible range for a
   * {@code double} result then an {@code ArithmeticException} is thrown.
   *
   * @return this {@code AbstractNumber} converted to a {@code double}.
   * @throws ArithmeticException if {@code this} has a nonzero fractional part, or will not fit in a
   * {@code double}.
   * @author caotc
   * @date 2019-04-02
   * @since 1.0.0
   */
  default double doubleValueExact() {
    java.math.BigDecimal value = bigDecimalValueExact();
    double doubleValue = value.doubleValue();
    if (new java.math.BigDecimal(String.valueOf(doubleValue)).compareTo(value) != 0) {
      throw new ArithmeticException("Precision loss when converting BigDecimal to double");
    }
    return doubleValue;
  }

  /**
   * Returns the value of the specified number as a {@code java.math.BigDecimal}, which may involve
   * rounding or truncation.
   *
   * @return the numeric value represented by this object after conversion to type {@code
   * java.math.BigDecimal}.
   * @author caotc
   * @date 2019-04-03
   * @since 1.0.0
   */
  @NonNull
  default java.math.BigDecimal bigDecimalValue() {
    return bigFractionValue().bigDecimalValue();
  }

  /**
   * Returns the value of the specified number as a {@code BigDecimal}, which may involve rounding
   * or truncation.
   *
   * @param mathContext the context to use.
   * @return the numeric value represented by this object after conversion to type {@code
   * BigDecimal}.
   * @author caotc
   * @date 2019-04-03
   * @since 1.0.0
   */
  @NonNull
  default java.math.BigDecimal bigDecimalValue(@NonNull MathContext mathContext) {
    BigFraction value = bigFractionValue();
    int bigIntegerPrecision = new java.math.BigDecimal(value.getNumerator().divide(value.getDenominator())).precision();
    int scale=mathContext.getPrecision()-bigIntegerPrecision;
    return value.bigDecimalValue(scale,mathContext.getRoundingMode().ordinal());
  }

  /**
   * Returns the value of the specified number as a {@code java.math.BigDecimal}, which may involve
   * rounding or truncation.
   *
   * @return the numeric value represented by this object after conversion to type {@code
   * java.math.BigDecimal}.
   * @throws ArithmeticException if {@code this} has a nonzero fractional part, or will not fit in a
   * {@code java.math.BigDecimal}.
   * @author caotc
   * @date 2019-04-03
   * @since 1.0.0
   */
  @NonNull
  default java.math.BigDecimal bigDecimalValueExact() {
    return bigFractionValueExact().bigDecimalValue(RoundingMode.UNNECESSARY.ordinal());
  }

  @NonNull
  BigFraction bigFractionValue();

  @NonNull
  BigFraction bigFractionValueExact();

  @NonNull
  default <T> T value(@NonNull Class<T> valueType, @NonNull MathContext mathContext) {
    return value(TypeToken.of(valueType), mathContext);
  }

  //TODO 封装MathContext，增加属性超出范围时的处理方式
  @NonNull
  @SuppressWarnings("unchecked")
  default <T> T value(@NonNull TypeToken<T> valueType, @NonNull MathContext mathContext) {
    if (byte.class.equals(valueType.getRawType()) || Byte.class.equals(valueType.getRawType())) {
      return (T) Byte.valueOf(byteValue(mathContext.getRoundingMode()));
    }
    if (short.class.equals(valueType.getRawType()) || Short.class.equals(valueType.getRawType())) {
      return (T) Short.valueOf(shortValue(mathContext.getRoundingMode()));
    }
    if (int.class.equals(valueType.getRawType()) || Integer.class.equals(valueType.getRawType())) {
      return (T) Integer.valueOf(intValue(mathContext.getRoundingMode()));
    }
    if (long.class.equals(valueType.getRawType()) || Long.class.equals(valueType.getRawType())) {
      return (T) Long.valueOf(longValue(mathContext.getRoundingMode()));
    }
    if (BigInteger.class.equals(valueType.getRawType())) {
      return (T) bigIntegerValue(mathContext.getRoundingMode());
    }
    if (float.class.equals(valueType.getRawType()) || Float.class.equals(valueType.getRawType())) {
      return (T) Float.valueOf(floatValue());
    }
    if (double.class.equals(valueType.getRawType()) || Double.class.equals(valueType.getRawType())) {
      return (T) Double.valueOf(doubleValue());
    }
    if (BigDecimal.class.equals(valueType.getRawType())) {
      return (T) bigDecimalValue(mathContext);
    }
    if (String.class.equals(valueType.getRawType())) {
      return (T) bigDecimalValue(mathContext).toPlainString();
    }
    if (BigFraction.class.equals(valueType.getRawType())) {
      return (T) bigFractionValue();
    }
    throw new IllegalArgumentException("can't convert to " + valueType);
  }
}
