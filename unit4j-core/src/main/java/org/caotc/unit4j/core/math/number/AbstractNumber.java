package org.caotc.unit4j.core.math.number;

import com.google.common.annotations.Beta;
import lombok.Data;
import lombok.NonNull;

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
@Data
@Beta
public abstract class AbstractNumber extends Number implements Comparable<AbstractNumber> {

  protected static final MathContext DEFAULT_MATH_CONTEXT = MathContext.DECIMAL128;

  /**
   * Returns the value of the specified number as a {@code BigInteger}, which may involve rounding
   * or truncation.
   *
   * @return the numeric value represented by this object after conversion to type {@code
   * BigInteger}.
   * @author caotc
   * @date 2019-04-02
   * @since 1.0.0
   */
  @NonNull
  public abstract BigInteger toBigInteger();

  /**
   * Converts this {@code AbstractNumber} to a {@code BigInteger}, checking for lost information. If
   * this {@code AbstractNumber} has a nonzero fractional part or is out of the possible range for a
   * {@code BigInteger} result then an {@code ArithmeticException} is thrown.
   *
   * @return this {@code AbstractNumber} converted to a {@code BigInteger}.
   * @throws ArithmeticException if {@code this} has a nonzero fractional part, or will not fit in a
   * {@code BigInteger}.
   * @author caotc
   * @date 2019-04-02
   * @since 1.0.0
   */
  @NonNull
  public abstract BigInteger toBigIntegerExact();

  /**
   * Returns the value of the specified number as a {@code BigDecimal}, which may involve rounding
   * or truncation.
   *
   * @return the numeric value represented by this object after conversion to type {@code
   * BigDecimal}.
   * @author caotc
   * @date 2019-04-03
   * @since 1.0.0
   */
  @NonNull
  public abstract BigDecimal toBigDecimal();

  /**
   * Converts this {@code AbstractNumber} to a {@code BigDecimal}, checking for lost information. If
   * this {@code AbstractNumber} has a nonzero fractional part or is out of the possible range for a
   * {@code BigDecimal} result then an {@code ArithmeticException} is thrown.
   *
   * @return this {@code AbstractNumber} converted to a {@code BigDecimal}.
   * @throws ArithmeticException if {@code this} has a nonzero fractional part, or will not fit in a
   * {@code BigDecimal}.
   * @author caotc
   * @date 2019-04-03
   * @since 1.0.0
   */
  @NonNull
  public abstract BigDecimal toBigDecimalExact();

  /**
   * Returns the value of the specified number as a {@code abstract}, which may involve rounding or
   * truncation.
   *
   * @return the numeric value represented by this object after conversion to type {@code abstract}.
   * @author caotc
   * @date 2019-04-03
   * @since 1.0.0
   */
  @NonNull
  public abstract Fraction toFraction();

  /**
   * Converts this {@code AbstractNumber} to a {@code Fraction}, checking for lost information.  If
   * this {@code AbstractNumber} has a nonzero fractional part or is out of the possible range for a
   * {@code Fraction} result then an {@code ArithmeticException} is thrown.
   *
   * @return this {@code AbstractNumber} converted to a {@code Fraction}.
   * @throws ArithmeticException if {@code this} has a nonzero fractional part, or will not fit in a
   * {@code Fraction}.
   * @author caotc
   * @date 2019-04-03
   * @since 1.0.0
   */
  @NonNull
  public abstract Fraction toFractionExact();

  /**
   * Indicates if this rational number is equal to zero.
   *
   * @return <code>this == 0</code>
   * @author caotc
   * @date 2019-04-04
   * @since 1.0.0
   */
  public abstract boolean isZero();

  /**
   * Indicates if this rational number is greater than zero.
   *
   * @return <code>this &gt; 0</code>
   * @author caotc
   * @date 2019-04-04
   * @since 1.0.0
   */
  public abstract boolean isPositive();

  /**
   * Indicates if this rational number is less than zero.
   *
   * @return <code>this &lt; 0</code>
   * @author caotc
   * @date 2019-04-04
   * @since 1.0.0
   */
  public abstract boolean isNegative();

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
  public AbstractNumber add(@NonNull AbstractNumber augend) {
    if (augend instanceof Fraction) {
      return add((Fraction) augend);
    }
    if (augend instanceof BigDecimal) {
      return add((BigDecimal) augend);
    }
    if (augend instanceof BigInteger) {
      return add((BigInteger) augend);
    }
    throw new IllegalArgumentException("this add method can't analyze " + augend);
  }

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
  public abstract AbstractNumber add(@NonNull Fraction augend);

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
  public abstract AbstractNumber add(@NonNull BigDecimal augend);

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
  public abstract AbstractNumber add(@NonNull BigInteger augend);

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
  public AbstractNumber subtract(@NonNull AbstractNumber subtrahend) {
    if (subtrahend instanceof Fraction) {
      return subtract((Fraction) subtrahend);
    }
    if (subtrahend instanceof BigDecimal) {
      return subtract((BigDecimal) subtrahend);
    }
    if (subtrahend instanceof BigInteger) {
      return subtract((BigInteger) subtrahend);
    }
    throw new IllegalArgumentException("this subtract method can't analyze " + subtrahend);
  }

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
  public AbstractNumber subtract(@NonNull Fraction subtrahend) {
    return add(subtrahend.negate());
  }

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
  public AbstractNumber subtract(@NonNull BigDecimal subtrahend) {
    return add(subtrahend.negate());
  }

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
  public AbstractNumber subtract(@NonNull BigInteger subtrahend) {
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
  public AbstractNumber multiply(@NonNull AbstractNumber multiplicand) {
    if (multiplicand instanceof Fraction) {
      return multiply((Fraction) multiplicand);
    }
    if (multiplicand instanceof BigDecimal) {
      return multiply((BigDecimal) multiplicand);
    }
    if (multiplicand instanceof BigInteger) {
      return multiply((BigInteger) multiplicand);
    }
    throw new IllegalArgumentException("this multiply method can't analyze " + multiplicand);
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
  public abstract AbstractNumber multiply(@NonNull Fraction multiplicand);

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
  public abstract AbstractNumber multiply(@NonNull BigDecimal multiplicand);

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
  public abstract AbstractNumber multiply(@NonNull BigInteger multiplicand);

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
  public AbstractNumber divide(@NonNull AbstractNumber divisor) {
    if (divisor instanceof Fraction) {
      return divide((Fraction) divisor);
    }
    if (divisor instanceof BigDecimal) {
      return divide((BigDecimal) divisor);
    }
    if (divisor instanceof BigInteger) {
      return divide((BigInteger) divisor);
    }
    throw new IllegalArgumentException("this divide method can't analyze " + divisor);
  }

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
  public abstract AbstractNumber divide(@NonNull Fraction divisor);

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
  public abstract AbstractNumber divide(@NonNull BigDecimal divisor);

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
  public abstract AbstractNumber divide(@NonNull BigInteger divisor);

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
  @NonNull
  public abstract AbstractNumber pow(int exponent);

  /**
   * Returns a {@code AbstractNumber} whose value is {@code (-this)}, and whose scale is {@code
   * this.scale()}.
   *
   * @return {@code -this}.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public abstract AbstractNumber negate();

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
  public AbstractNumber abs() {
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
  public AbstractNumber reciprocal() {
    return toFractionExact().reciprocal();
  }

  @Override
  public int compareTo(@NonNull AbstractNumber o) {
    if (o instanceof Fraction) {
      return compareTo((Fraction) o);
    }
    if (o instanceof BigDecimal) {
      return compareTo((BigDecimal) o);
    }
    return compareTo(o.toFraction());
  }

  public abstract int compareTo(@NonNull Fraction o);

  public abstract int compareTo(@NonNull BigDecimal o);

  public abstract int compareTo(@NonNull BigInteger o);

  @Override
  public byte byteValue() {
    return bigIntegerValue().byteValue();
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
  public byte byteValue(@NonNull RoundingMode roundingMode) {
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
  public byte byteValueExact() {
    return bigIntegerValueExact().byteValueExact();
  }

  @Override
  public short shortValue() {
    return bigIntegerValue().shortValue();
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
  public short shortValue(@NonNull RoundingMode roundingMode) {
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
  public short shortValueExact() {
    return bigIntegerValueExact().shortValueExact();
  }

  @Override
  public int intValue() {
    return bigIntegerValue().intValue();
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
  public int intValue(@NonNull RoundingMode roundingMode) {
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
  public int intValueExact() {
    return bigIntegerValueExact().intValueExact();
  }

  @Override
  public long longValue() {
    return bigIntegerValue().longValue();
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
  public long longValue(@NonNull RoundingMode roundingMode) {
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
  public long longValueExact() {
    return bigIntegerValueExact().longValueExact();
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
  public java.math.BigInteger bigIntegerValue() {
    return toBigInteger().bigIntegerValue();
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
  public java.math.BigInteger bigIntegerValue(@NonNull RoundingMode roundingMode) {
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
  public java.math.BigInteger bigIntegerValueExact() {
    return toBigIntegerExact().bigIntegerValue();
  }

  @Override
  public float floatValue() {
    return bigDecimalValue().floatValue();
  }

  /**
   * 将该数字对象转换为{@code float}
   *
   * @param mathContext 数学运算配置
   * @return 转换后的{@code float}
   * @throws ArithmeticException 如果值范围超过了{@code float}范围或者{@code float}无法精确表示舍入后的该值
   * @author caotc
   * @date 2019-04-24
   * @since 1.0.0
   */
  public float floatValue(@NonNull MathContext mathContext) {
    java.math.BigDecimal value = bigDecimalValue(mathContext);
    float floatValue = value.floatValue();
    if (BigDecimal.valueOf(String.valueOf(floatValue)).compareTo(value) != 0) {
      throw new ArithmeticException("精度不够");
    }
    return floatValue;
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
  public float floatValueExact() {
    BigDecimal value = toBigDecimalExact();
    float floatValue = value.floatValue();
    if (BigDecimal.valueOf(String.valueOf(floatValue)).compareTo(value) != 0) {
      throw new ArithmeticException("精度不够");
    }
    return floatValue;
  }

  @Override
  public double doubleValue() {
    return bigDecimalValue().doubleValue();
  }

  /**
   * 将该数字对象转换为{@code double}
   *
   * @param mathContext 数学运算配置
   * @return 转换后的{@code double}
   * @throws ArithmeticException 如果值范围超过了{@code double}范围或者{@code double}无法精确表示舍入后的该值
   * @author caotc
   * @date 2019-04-24
   * @since 1.0.0
   */
  public double doubleValue(@NonNull MathContext mathContext) {
    java.math.BigDecimal value = bigDecimalValue(mathContext);
    double doubleValue = value.doubleValue();
    if (BigDecimal.valueOf(String.valueOf(doubleValue)).compareTo(value) != 0) {
      throw new ArithmeticException("精度不够");
    }
    return doubleValue;
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
  public double doubleValueExact() {
    BigDecimal value = toBigDecimalExact();
    double doubleValue = value.doubleValue();
    if (BigDecimal.valueOf(String.valueOf(doubleValue)).compareTo(value) != 0) {
      throw new ArithmeticException("精度不够");
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
  public java.math.BigDecimal bigDecimalValue() {
    return toBigDecimal().bigDecimalValue();
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
  public java.math.BigDecimal bigDecimalValue(@NonNull MathContext mathContext) {
    return toBigDecimal().bigDecimalValue(mathContext);
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
  public java.math.BigDecimal bigDecimalValueExact() {
    return toBigDecimalExact().bigDecimalValue();
  }

  //TODO 封装MathContext，增加属性超出范围时的处理方式
  @NonNull
  @SuppressWarnings("unchecked")
  public <T> T value(@NonNull Class<T> valueType, @NonNull MathContext mathContext) {
    if (byte.class.equals(valueType) || Byte.class.equals(valueType)) {
      return (T) Byte.valueOf(byteValue(mathContext.getRoundingMode()));
    }
    if (short.class.equals(valueType) || Short.class.equals(valueType)) {
      return (T) Short.valueOf(shortValue(mathContext.getRoundingMode()));
    }
    if (int.class.equals(valueType) || Integer.class.equals(valueType)) {
      return (T) Integer.valueOf(intValue(mathContext.getRoundingMode()));
    }
    if (long.class.equals(valueType) || Long.class.equals(valueType)) {
      return (T) Long.valueOf(longValue(mathContext.getRoundingMode()));
    }
    if (java.math.BigInteger.class.equals(valueType)) {
      return (T) bigIntegerValue(mathContext.getRoundingMode());
    }
    if (float.class.equals(valueType) || Float.class.equals(valueType)) {
      return (T) Float.valueOf(floatValue(mathContext));
    }
    if (double.class.equals(valueType) || Double.class.equals(valueType)) {
      return (T) Double.valueOf(doubleValue(mathContext));
    }
    if (java.math.BigDecimal.class.equals(valueType)) {
      return (T) bigDecimalValue(mathContext);
    }
    if (String.class.equals(valueType)) {
      return (T) bigDecimalValue(mathContext).toPlainString();
    }
    throw new IllegalArgumentException("can't convert to " + valueType);
  }
}
