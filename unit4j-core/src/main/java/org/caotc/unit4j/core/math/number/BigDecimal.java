package org.caotc.unit4j.core.math.number;

import com.google.common.annotations.Beta;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import java.math.MathContext;

/**
 * @author caotc
 * @date 2019-04-06
 * @implSpec
 * @implNote
 * @apiNote
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
@AllArgsConstructor(staticName = "valueOf")
@Beta
public class BigDecimal implements Number {

  public static final BigDecimal ZERO = valueOf(java.math.BigDecimal.ZERO);
  public static final BigDecimal ONE = valueOf(java.math.BigDecimal.ONE);
  public static final BigDecimal TEN = valueOf(java.math.BigDecimal.TEN);

  /**
   * @author caotc
   * @date 2019-04-06
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal valueOf(java.math.BigInteger val) {
    return valueOf(new java.math.BigDecimal(val));
  }

  /**
   * @author caotc
   * @date 2019-04-06
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal valueOf(BigInteger val) {
    return valueOf(val.bigIntegerValue());
  }

  /**
   * @author caotc
   * @date 2019-04-06
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal valueOf(String val) {
    return valueOf(new java.math.BigDecimal(val));
  }

  /**
   * @author caotc
   * @date 2019-04-06
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal valueOf(long unscaledVal, int scale) {
    return valueOf(java.math.BigDecimal.valueOf(unscaledVal, scale));
  }

  /**
   * @author caotc
   * @date 2019-04-06
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal valueOf(long val) {
    return valueOf(java.math.BigDecimal.valueOf(val));
  }

  @NonNull
  public static BigDecimal valueOf(float val) {
    return valueOf(String.valueOf(val));
  }

  @NonNull
  public static BigDecimal valueOf(double val) {
    return valueOf(String.valueOf(val));
  }

  @NonNull
  java.math.BigDecimal value;

  @Override
  public @NonNull BigInteger toBigInteger() {
    return BigInteger.valueOf(value.toBigInteger());
  }

  @Override
  public @NonNull BigInteger toBigIntegerExact() {
    return BigInteger.valueOf(value.toBigIntegerExact());
  }

  @Override
  public @NonNull BigDecimal toBigDecimal() {
    return this;
  }

  @Override
  public @NonNull BigDecimal toBigDecimalExact() {
    return this;
  }

  @Override
  public @NonNull Fraction toFraction() {
    return toFractionExact();
  }

  @Override
  public @NonNull Fraction toFractionExact() {
    return Fraction.valueOf(movePointRight(scale()).toBigIntegerExact(),
        ONE.movePointRight(scale()).toBigIntegerExact());
  }

  @Override
  public boolean isZero() {
    return value.signum() == 0;
  }

  @Override
  public boolean isPositive() {
    return value.signum() == 1;
  }

  @Override
  public boolean isNegative() {
    return value.signum() == -1;
  }

  @Override
  public @NonNull Number add(@NonNull Fraction augend) {
    return toFractionExact().add(augend);
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (this + augend)}, and whose scale is {@code
   * max(this.scale(), augend.scale())}.
   *
   * @param augend value to be added to this {@code BigDecimal}.
   * @return {@code this + augend}
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @Override
  @NonNull
  public BigDecimal add(@NonNull BigDecimal augend) {
    return valueOf(value.add(augend.value));
  }

  @Override
  public @NonNull Number add(@NonNull BigInteger augend) {
    return valueOf(value.add(augend.bigDecimalValue()));
  }

  @Override
  public @NonNull Number subtract(@NonNull Fraction subtrahend) {
    return toFractionExact().subtract(subtrahend);
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (this + augend)}, and whose scale is {@code
   * max(this.scale(), augend.scale())}.
   *
   * @param augend value to be added to this {@code java.math.BigDecimal}.
   * @return {@code this + augend}
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal add(java.math.BigDecimal augend) {
    return valueOf(value.add(augend));
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (this + augend)}, with rounding according to
   * the context settings.
   *
   * If either number is zero and the precision setting is nonzero then the other number, rounded if
   * necessary, is used as the result.
   *
   * @param augend value to be added to this {@code BigDecimal}.
   * @param mc the context to use.
   * @return {@code this + augend}, rounded as necessary.
   * @throws ArithmeticException if the result is inexact but the rounding mode is {@code
   * UNNECESSARY}.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal add(BigDecimal augend, MathContext mc) {
    return valueOf(value.add(augend.value, mc));
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (this + augend)}, with rounding according to
   * the context settings.
   *
   * If either number is zero and the precision setting is nonzero then the other number, rounded if
   * necessary, is used as the result.
   *
   * @param augend value to be added to this {@code java.math.BigDecimal}.
   * @param mc the context to use.
   * @return {@code this + augend}, rounded as necessary.
   * @throws ArithmeticException if the result is inexact but the rounding mode is {@code
   * UNNECESSARY}.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal add(java.math.BigDecimal augend, MathContext mc) {
    return valueOf(value.add(augend, mc));
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (this - subtrahend)}, and whose scale is
   * {@code max(this.scale(), subtrahend.scale())}.
   *
   * @param subtrahend value to be subtracted from this {@code BigDecimal}.
   * @return {@code this - subtrahend}
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @Override
  @NonNull
  public BigDecimal subtract(@NonNull BigDecimal subtrahend) {
    return valueOf(value.subtract(subtrahend.value));
  }

  @Override
  public @NonNull Number multiply(@NonNull Fraction multiplicand) {
    return toFractionExact().multiply(multiplicand);
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (this - subtrahend)}, and whose scale is
   * {@code max(this.scale(), subtrahend.scale())}.
   *
   * @param subtrahend value to be subtracted from this {@code java.math.BigDecimal}.
   * @return {@code this - subtrahend}
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal subtract(java.math.BigDecimal subtrahend) {
    return valueOf(value.subtract(subtrahend));
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (this - subtrahend)}, with rounding
   * according to the context settings.
   *
   * If {@code subtrahend} is zero then this, rounded if necessary, is used as the result.  If this
   * is zero then the result is {@code subtrahend.negate(mc)}.
   *
   * @param subtrahend value to be subtracted from this {@code BigDecimal}.
   * @param mc the context to use.
   * @return {@code this - subtrahend}, rounded as necessary.
   * @throws ArithmeticException if the result is inexact but the rounding mode is {@code
   * UNNECESSARY}.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal subtract(BigDecimal subtrahend, MathContext mc) {
    return valueOf(value.subtract(subtrahend.value, mc));
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (this - subtrahend)}, with rounding
   * according to the context settings.
   *
   * If {@code subtrahend} is zero then this, rounded if necessary, is used as the result.  If this
   * is zero then the result is {@code subtrahend.negate(mc)}.
   *
   * @param subtrahend value to be subtracted from this {@code java.math.BigDecimal}.
   * @param mc the context to use.
   * @return {@code this - subtrahend}, rounded as necessary.
   * @throws ArithmeticException if the result is inexact but the rounding mode is {@code
   * UNNECESSARY}.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal subtract(java.math.BigDecimal subtrahend, MathContext mc) {
    return valueOf(value.subtract(subtrahend, mc));
  }

  /**
   * Returns a {@code BigDecimal} whose value is <tt>(this &times; multiplicand)</tt>, and whose
   * scale is {@code (this.scale() + multiplicand.scale())}.
   *
   * @param multiplicand value to be multiplied by this {@code BigDecimal}.
   * @return {@code this * multiplicand}
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @Override
  @NonNull
  public BigDecimal multiply(@NonNull BigDecimal multiplicand) {
    return valueOf(value.multiply(multiplicand.value));
  }

  @Override
  public @NonNull Number multiply(@NonNull BigInteger multiplicand) {
    return valueOf(value.multiply(multiplicand.bigDecimalValue()));
  }

  /**
   * Returns a {@code BigDecimal} whose value is <tt>(this &times; multiplicand)</tt>, and whose
   * scale is {@code (this.scale() + multiplicand.scale())}.
   *
   * @param multiplicand value to be multiplied by this {@code java.math.BigDecimal}.
   * @return {@code this * multiplicand}
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal multiply(@NonNull java.math.BigDecimal multiplicand) {
    return valueOf(value.multiply(multiplicand));
  }

  /**
   * Returns a {@code BigDecimal} whose value is <tt>(this &times; multiplicand)</tt>, with rounding
   * according to the context settings.
   *
   * @param multiplicand value to be multiplied by this {@code BigDecimal}.
   * @param mc the context to use.
   * @return {@code this * multiplicand}, rounded as necessary.
   * @throws ArithmeticException if the result is inexact but the rounding mode is {@code
   * UNNECESSARY}.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal multiply(BigDecimal multiplicand, MathContext mc) {
    return valueOf(value.multiply(multiplicand.value, mc));
  }

  /**
   * Returns a {@code BigDecimal} whose value is <tt>(this &times; multiplicand)</tt>, with rounding
   * according to the context settings.
   *
   * @param multiplicand value to be multiplied by this {@code java.math.BigDecimal}.
   * @param mc the context to use.
   * @return {@code this * multiplicand}, rounded as necessary.
   * @throws ArithmeticException if the result is inexact but the rounding mode is {@code
   * UNNECESSARY}.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal multiply(java.math.BigDecimal multiplicand, MathContext mc) {
    return valueOf(value.multiply(multiplicand, mc));
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (this / divisor)}, and whose preferred scale
   * is {@code (this.scale() - divisor.scale())}; if the exact quotient cannot be represented
   * (because it has a non-terminating decimal expansion) an {@code ArithmeticException} is thrown.
   *
   * @param divisor value by which this {@code BigDecimal} is to be divided.
   * @return {@code this / divisor}
   * @throws ArithmeticException if the exact quotient does not have a terminating decimal
   * expansion
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @Override
  @NonNull
  public Number divide(@NonNull BigDecimal divisor) {
    return toFractionExact().divide(divisor.toFractionExact());
  }

  @Override
  public @NonNull Number divide(@NonNull BigInteger divisor) {
    return toFractionExact().divide(divisor.toFractionExact());
  }

  @Override
  public @NonNull Number divide(@NonNull Fraction divisor) {
    return toFractionExact().divide(divisor);
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (this / divisor)}, and whose preferred scale
   * is {@code (this.scale() - divisor.scale())}; if the exact quotient cannot be represented
   * (because it has a non-terminating decimal expansion) an {@code ArithmeticException} is thrown.
   *
   * @param divisor value by which this {@code java.math.BigDecimal} is to be divided.
   * @return {@code this / divisor}
   * @throws ArithmeticException if the exact quotient does not have a terminating decimal
   * expansion
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @SuppressWarnings("BigDecimalMethodWithoutRoundingCalled")
  @NonNull
  public BigDecimal divide(java.math.BigDecimal divisor) {
    return valueOf(value.divide(divisor));
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (this / divisor)}, with rounding according
   * to the context settings.
   *
   * @param divisor value by which this {@code BigDecimal} is to be divided.
   * @param mc the context to use.
   * @return {@code this / divisor}, rounded as necessary.
   * @throws ArithmeticException if the result is inexact but the rounding mode is {@code
   * UNNECESSARY} or {@code mc.precision == 0} and the quotient has a non-terminating decimal
   * expansion.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal divide(BigDecimal divisor, MathContext mc) {
    return valueOf(value.divide(divisor.value, mc));
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (this / divisor)}, with rounding according
   * to the context settings.
   *
   * @param divisor value by which this {@code java.math.BigDecimal} is to be divided.
   * @param mc the context to use.
   * @return {@code this / divisor}, rounded as necessary.
   * @throws ArithmeticException if the result is inexact but the rounding mode is {@code
   * UNNECESSARY} or {@code mc.precision == 0} and the quotient has a non-terminating decimal
   * expansion.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal divide(java.math.BigDecimal divisor, MathContext mc) {
    return valueOf(value.divide(divisor, mc));
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (this % divisor)}.
   *
   * <p>The remainder is given by
   * {@code this.subtract(this.divideToIntegralValue(divisor).multiply(divisor))}. Note that this is
   * not the modulo operation (the result can be negative).
   *
   * @param divisor value by which this {@code BigDecimal} is to be divided.
   * @return {@code this % divisor}.
   * @throws ArithmeticException if {@code divisor==0}
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal remainder(BigDecimal divisor) {
    return valueOf(value.remainder(divisor.value));
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (this % divisor)}.
   *
   * <p>The remainder is given by
   * {@code this.subtract(this.divideToIntegralValue(divisor).multiply(divisor))}. Note that this is
   * not the modulo operation (the result can be negative).
   *
   * @param divisor value by which this {@code java.math.BigDecimal} is to be divided.
   * @return {@code this % divisor}.
   * @throws ArithmeticException if {@code divisor==0}
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal remainder(java.math.BigDecimal divisor) {
    return valueOf(value.remainder(divisor));
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (this % divisor)}, with rounding according
   * to the context settings. The {@code MathContext} settings affect the implicit divide used to
   * compute the remainder.  The remainder computation itself is by definition exact.  Therefore,
   * the remainder may contain more than {@code mc.getPrecision()} digits.
   *
   * <p>The remainder is given by
   * {@code this.subtract(this.divideToIntegralValue(divisor, mc).multiply(divisor))}.  Note that
   * this is not the modulo operation (the result can be negative).
   *
   * @param divisor value by which this {@code BigDecimal} is to be divided.
   * @param mc the context to use.
   * @return {@code this % divisor}, rounded as necessary.
   * @throws ArithmeticException if {@code divisor==0}
   * @throws ArithmeticException if the result is inexact but the rounding mode is {@code
   * UNNECESSARY}, or {@code mc.precision} {@literal >} 0 and the result of {@code
   * this.divideToIntgralValue(divisor)} would require a precision of more than {@code mc.precision}
   * digits.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal remainder(BigDecimal divisor, MathContext mc) {
    return valueOf(value.remainder(divisor.value, mc));
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (this % divisor)}, with rounding according
   * to the context settings. The {@code MathContext} settings affect the implicit divide used to
   * compute the remainder.  The remainder computation itself is by definition exact.  Therefore,
   * the remainder may contain more than {@code mc.getPrecision()} digits.
   *
   * <p>The remainder is given by
   * {@code this.subtract(this.divideToIntegralValue(divisor, mc).multiply(divisor))}.  Note that
   * this is not the modulo operation (the result can be negative).
   *
   * @param divisor value by which this {@code java.math.BigDecimal} is to be divided.
   * @param mc the context to use.
   * @return {@code this % divisor}, rounded as necessary.
   * @throws ArithmeticException if {@code divisor==0}
   * @throws ArithmeticException if the result is inexact but the rounding mode is {@code
   * UNNECESSARY}, or {@code mc.precision} {@literal >} 0 and the result of {@code
   * this.divideToIntgralValue(divisor)} would require a precision of more than {@code mc.precision}
   * digits.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal remainder(java.math.BigDecimal divisor, MathContext mc) {
    return valueOf(value.remainder(divisor, mc));
  }

  /**
   * Returns a two-element {@code BigDecimal} array containing the result of {@code
   * divideToIntegralValue} followed by the result of {@code remainder} on the two operands.
   *
   * <p>Note that if both the integer quotient and remainder are
   * needed, this method is faster than using the {@code divideToIntegralValue} and {@code
   * remainder} methods separately because the division need only be carried out once.
   *
   * @param divisor value by which this {@code BigDecimal} is to be divided, and the remainder
   * computed.
   * @return {@code DivideResult} : the quotient (the result of {@code divideToIntegralValue}) and
   * the remainder .
   * @throws ArithmeticException if {@code divisor==0}
   * @author caotc
   * @date 2019-04-07
   * @see #remainder(java.math.BigDecimal, java.math.MathContext)
   * @since 1.0.0
   */
  @NonNull
  public DivideResult divideAndRemainder(BigDecimal divisor) {
    java.math.BigDecimal[] divideAndRemainder = value.divideAndRemainder(divisor.value);
    return new DivideResult(valueOf(divideAndRemainder[0]), valueOf(divideAndRemainder[1]));
  }

  /**
   * Returns a two-element {@code BigDecimal} array containing the result of {@code
   * divideToIntegralValue} followed by the result of {@code remainder} on the two operands.
   *
   * <p>Note that if both the integer quotient and remainder are
   * needed, this method is faster than using the {@code divideToIntegralValue} and {@code
   * remainder} methods separately because the division need only be carried out once.
   *
   * @param divisor value by which this {@code java.math.BigDecimal} is to be divided, and the
   * remainder computed.
   * @return {@code DivideResult} : the quotient (the result of {@code divideToIntegralValue}) and
   * the remainder .
   * @throws ArithmeticException if {@code divisor==0}
   * @author caotc
   * @date 2019-04-07
   * @see #remainder(java.math.BigDecimal, java.math.MathContext)
   * @since 1.0.0
   */
  @NonNull
  public DivideResult divideAndRemainder(java.math.BigDecimal divisor) {
    java.math.BigDecimal[] divideAndRemainder = value.divideAndRemainder(divisor);
    return new DivideResult(valueOf(divideAndRemainder[0]), valueOf(divideAndRemainder[1]));
  }

  /**
   * Returns a two-element {@code BigDecimal} array containing the result of {@code
   * divideToIntegralValue} followed by the result of {@code remainder} on the two operands.
   *
   * <p>Note that if both the integer quotient and remainder are
   * needed, this method is faster than using the {@code divideToIntegralValue} and {@code
   * remainder} methods separately because the division need only be carried out once.
   *
   * @param divisor value by which this {@code BigDecimal} is to be divided, and the remainder
   * computed.
   * @param mc the context to use.
   * @return {@code DivideResult} : the quotient (the result of {@code divideToIntegralValue}) and
   * the remainder .
   * @throws ArithmeticException if {@code divisor==0}
   * @author caotc
   * @date 2019-04-07
   * @see #remainder(java.math.BigDecimal, java.math.MathContext)
   * @since 1.0.0
   */
  @NonNull
  public DivideResult divideAndRemainder(BigDecimal divisor,
      MathContext mc) {
    java.math.BigDecimal[] divideAndRemainder = value.divideAndRemainder(divisor.value, mc);
    return new DivideResult(valueOf(divideAndRemainder[0]), valueOf(divideAndRemainder[1]));
  }

  /**
   * Returns a two-element {@code BigDecimal} array containing the result of {@code
   * divideToIntegralValue} followed by the result of {@code remainder} on the two operands.
   *
   * <p>Note that if both the integer quotient and remainder are
   * needed, this method is faster than using the {@code divideToIntegralValue} and {@code
   * remainder} methods separately because the division need only be carried out once.
   *
   * @param divisor value by which this {@code BigDecimal} is to be divided, and the remainder
   * computed.
   * @param mc the context to use.
   * @return {@code DivideResult} : the quotient (the result of {@code divideToIntegralValue}) and
   * the remainder .
   * @throws ArithmeticException if {@code divisor==0}
   * @author caotc
   * @date 2019-04-07
   * @see #remainder(java.math.BigDecimal, java.math.MathContext)
   * @since 1.0.0
   */
  @NonNull
  public DivideResult divideAndRemainder(java.math.BigDecimal divisor,
      MathContext mc) {
    return divideAndRemainder(valueOf(divisor), mc);
  }

  /**
   * Returns a {@code BigDecimal} whose value is
   * <tt>(this<sup>n</sup>)</tt>, The power is computed exactly, to
   * unlimited precision.
   *
   * <p>The parameter {@code n} must be in the range 0 through
   * 999999999, inclusive.  {@code ZERO.pow(0)} returns {@link #ONE}.
   *
   * Note that future releases may expand the allowable exponent range of this method.
   *
   * @param exponent power to raise this {@code BigDecimal} to.
   * @return <tt>this<sup>n</sup></tt>
   * @throws ArithmeticException if {@code n} is out of range.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @Override
  @NonNull
  public Number pow(int exponent) {
    if (exponent > 0) {
      return valueOf(value.pow(exponent));
    }
    return valueOf(value.pow(-exponent)).reciprocal();
  }

  /**
   * Returns a {@code BigDecimal} whose value is the absolute value of this {@code BigDecimal}, and
   * whose scale is {@code this.scale()}.
   *
   * @return {@code abs(this)}
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @Override
  @NonNull
  public BigDecimal abs() {
    return valueOf(value.abs());
  }

  //TODO 是否仅在除不尽时才返回分数
  @Override
  public @NonNull Number reciprocal() {
    return toFractionExact().reciprocal();
  }

  /**
   * Returns a {@code BigDecimal} whose value is the absolute value of this {@code BigDecimal}, with
   * rounding according to the context settings.
   *
   * @param mc the context to use.
   * @return {@code abs(this)}, rounded as necessary.
   * @throws ArithmeticException if the result is inexact but the rounding mode is {@code
   * UNNECESSARY}.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal abs(MathContext mc) {
    return valueOf(value.abs(mc));
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (-this)}, and whose scale is {@code
   * this.scale()}.
   *
   * @return {@code -this}.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @Override
  @NonNull
  public BigDecimal negate() {
    return valueOf(value.negate());
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (-this)}, with rounding according to the
   * context settings.
   *
   * @param mc the context to use.
   * @return {@code -this}, rounded as necessary.
   * @throws ArithmeticException if the result is inexact but the rounding mode is {@code
   * UNNECESSARY}.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal negate(MathContext mc) {
    return valueOf(value.negate(mc));
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (+this)}, and whose scale is {@code
   * this.scale()}.
   *
   * <p>This method, which simply returns this {@code BigDecimal}
   * is included for symmetry with the unary minus method {@link #negate()}.
   *
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal plus() {
    return valueOf(value.plus());
  }

  /**
   * Returns a {@code BigDecimal} whose value is {@code (+this)}, with rounding according to the
   * context settings.
   *
   * <p>The effect of this method is identical to that of the {@link
   * #round(MathContext)} method.
   *
   * @param mc the context to use.
   * @return {@code this}, rounded as necessary.  A zero result will have a scale of 0.
   * @throws ArithmeticException if the result is inexact but the rounding mode is {@code
   * UNNECESSARY}.
   * @author caotc
   * @date 2019-04-07
   * @see #round(MathContext)
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal plus(MathContext mc) {
    return valueOf(value.plus(mc));
  }

  /**
   * Returns the <i>scale</i> of this {@code BigDecimal}.  If zero or positive, the scale is the
   * number of digits to the right of the decimal point.  If negative, the unscaled value of the
   * number is multiplied by ten to the power of the negation of the scale.  For example, a scale of
   * {@code -3} means the unscaled value is multiplied by 1000.
   *
   * @return the scale of this {@code BigDecimal}.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  public int scale() {
    return value.scale();
  }

  /**
   * Returns the <i>precision</i> of this {@code BigDecimal}.  (The precision is the number of
   * digits in the unscaled value.)
   *
   * <p>The precision of a zero value is 1.
   *
   * @return the precision of this {@code BigDecimal}.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  public int precision() {
    return value.precision();
  }

  /**
   * Returns a {@code BigInteger} whose value is the <i>unscaled value</i> of this {@code
   * BigDecimal}.  (Computes <tt>(this * 10<sup>this.scale()</sup>)</tt>.)
   *
   * @return the unscaled value of this {@code BigDecimal}.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigInteger unscaledValue() {
    return BigInteger.valueOf(value.unscaledValue());
  }

  /**
   * Returns a {@code BigDecimal} rounded according to the {@code MathContext} settings.  If the
   * precision setting is 0 then no rounding takes place.
   *
   * <p>The effect of this method is identical to that of the
   * {@link #plus(MathContext)} method.
   *
   * @param mc the context to use.
   * @return a {@code BigDecimal} rounded according to the {@code MathContext} settings.
   * @throws ArithmeticException if the rounding mode is {@code UNNECESSARY} and the {@code
   * BigDecimal}  operation would require rounding.
   * @author caotc
   * @date 2019-04-07
   * @see #plus(MathContext)
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal round(MathContext mc) {
    return valueOf(value.round(mc));
  }

  /**
   * Returns a {@code BigDecimal} which is equivalent to this one with the decimal point moved
   * {@code n} places to the left.  If {@code n} is non-negative, the call merely adds {@code n} to
   * the scale.  If {@code n} is negative, the call is equivalent to {@code movePointRight(-n)}. The
   * {@code BigDecimal} returned by this call has value <tt>(this &times; 10<sup>-n</sup>)</tt> and
   * scale {@code max(this.scale()+n, 0)}.
   *
   * @param n number of places to move the decimal point to the left.
   * @return a {@code BigDecimal} which is equivalent to this one with the decimal point moved
   * {@code n} places to the left.
   * @throws ArithmeticException if scale overflows.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal movePointLeft(int n) {
    return valueOf(value.movePointLeft(n));
  }

  /**
   * Returns a {@code BigDecimal} which is equivalent to this one with the decimal point moved
   * {@code n} places to the right. If {@code n} is non-negative, the call merely subtracts {@code
   * n} from the scale.  If {@code n} is negative, the call is equivalent to {@code
   * movePointLeft(-n)}.  The {@code BigDecimal} returned by this call has value <tt>(this &times;
   * 10<sup>n</sup>)</tt> and scale {@code max(this.scale()-n, 0)}.
   *
   * @param n number of places to move the decimal point to the right.
   * @return a {@code BigDecimal} which is equivalent to this one with the decimal point moved
   * {@code n} places to the right.
   * @throws ArithmeticException if scale overflows.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal movePointRight(int n) {
    return valueOf(value.movePointRight(n));
  }

  /**
   * Returns a BigDecimal whose numerical value is equal to ({@code this} * 10<sup>n</sup>).  The
   * scale of the result is {@code (this.scale() - n)}.
   *
   * @param n the exponent power of ten to scale by
   * @return a BigDecimal whose numerical value is equal to ({@code this} * 10<sup>n</sup>)
   * @throws ArithmeticException if the scale would be outside the range of a 32-bit integer.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal scaleByPowerOfTen(int n) {
    return valueOf(value.scaleByPowerOfTen(n));
  }

  /**
   * Returns a {@code BigDecimal} which is numerically equal to this one but with any trailing zeros
   * removed from the representation.  For example, stripping the trailing zeros from the {@code
   * BigDecimal} value {@code 600.0}, which has [{@code BigInteger}, {@code scale}] components
   * equals to [6000, 1], yields {@code 6E2} with [{@code BigInteger}, {@code scale}] components
   * equals to [6, -2].  If this BigDecimal is numerically equal to zero, then {@code
   * BigDecimal.ZERO} is returned.
   *
   * @return a numerically equal {@code BigDecimal} with any trailing zeros removed.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal stripTrailingZeros() {
    return valueOf(value.stripTrailingZeros());
  }

  /**
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal min(java.math.BigDecimal val) {
    return valueOf(value.min(val));
  }

  @NonNull
  public BigDecimal max(java.math.BigDecimal val) {
    return valueOf(value.max(val));
  }

  /**
   * Returns a string representation of this {@code BigDecimal}, using engineering notation if an
   * exponent is needed.
   *
   * <p>Returns a string that represents the {@code BigDecimal} as
   * described in the {@link #toString()} method, except that if exponential notation is used, the
   * power of ten is adjusted to be a multiple of three (engineering notation) such that the integer
   * part of nonzero values will be in the range 1 through 999.  If exponential notation is used for
   * zero values, a decimal point and one or two fractional zero digits are used so that the scale
   * of the zero value is preserved.  Note that unlike the output of {@link #toString()}, the output
   * of this method is <em>not</em> guaranteed to recover the same [integer, scale] pair of this
   * {@code BigDecimal} if the output string is converting back to a {@code BigDecimal} using the
   * {@linkplain #valueOf(String) string constructor}.  The result of this method meets the weaker
   * constraint of always producing a numerically equal result from applying the string constructor
   * to the method's output.
   *
   * @return string representation of this {@code BigDecimal}, using engineering notation if an
   * exponent is needed.
   * @author caotc
   * @date 2019-04-07
   * @since 1.0.0
   */
  @NonNull
  public String toEngineeringString() {
    return value.toEngineeringString();
  }

  /**
   * Returns a string representation of this {@code BigDecimal} without an exponent field.  For
   * values with a positive scale, the number of digits to the right of the decimal point is used to
   * indicate scale.  For values with a zero or negative scale, the resulting string is generated as
   * if the value were converted to a numerically equal value with zero scale and as if all the
   * trailing zeros of the zero scale value were present in the result.
   *
   * The entire string is prefixed by a minus sign character '-' (<tt>'&#92;u002D'</tt>) if the
   * unscaled value is less than zero. No sign character is prefixed if the unscaled value is zero
   * or positive.
   *
   * Note that if the result of this method is passed to the {@linkplain #valueOf(String) string
   * constructor}, only the numerical value of this {@code BigDecimal} will necessarily be
   * recovered; the representation of the new {@code BigDecimal} may have a different scale.  In
   * particular, if this {@code BigDecimal} has a negative scale, the string resulting from this
   * method will have a scale of zero when processed by the string constructor.
   *
   * (This method behaves analogously to the {@code toString} method in 1.4 and earlier releases.)
   *
   * @return a string representation of this {@code BigDecimal} without an exponent field.
   * @author caotc
   * @date 2019-04-07
   * @see #toString()
   * @see #toEngineeringString()
   * @since 1.5
   * @since 1.0.0
   */
  @NonNull
  public String toPlainString() {
    return value.toPlainString();
  }

  /**
   * Returns the size of an ulp, a unit in the last place, of this {@code BigDecimal}.  An ulp of a
   * nonzero {@code BigDecimal} value is the positive distance between this value and the {@code
   * BigDecimal} value next larger in magnitude with the same number of digits.  An ulp of a zero
   * value is numerically equal to 1 with the scale of {@code this}.  The result is stored with the
   * same scale as {@code this} so the result for zero and nonzero values is equal to {@code [1,
   * this.scale()]}.
   *
   * @return the size of an ulp of {@code this}
   * @author caotc
   * @date 2019-04-08
   * @since 1.0.0
   */
  @NonNull
  public BigDecimal ulp() {
    return valueOf(value.ulp());
  }

  @NonNull
  @Override
  public java.math.BigDecimal bigDecimalValue() {
    return value;
  }

  @NonNull
  @Override
  public java.math.BigDecimal bigDecimalValue(@NonNull MathContext mathContext) {
    return value.round(mathContext);
  }

  /**
   * Compares this {@code BigDecimal} with the specified {@code BigDecimal}.  Two {@code BigDecimal}
   * objects that are equal in value but have a different scale (like 2.0 and 2.00) are considered
   * equal by this method.  This method is provided in preference to individual methods for each of
   * the six boolean comparison operators ({@literal <}, ==, {@literal >}, {@literal >=}, !=,
   * {@literal <=}).  The suggested idiom for performing these comparisons is: {@code
   * (x.compareTo(y)} &lt;<i>op</i>&gt; {@code 0)}, where &lt;<i>op</i>&gt; is one of the six
   * comparison operators.
   *
   * @param val {@code BigDecimal} to which this {@code BigDecimal} is to be compared.
   * @return -1, 0, or 1 as this {@code BigDecimal} is numerically less than, equal to, or greater
   * than {@code val}.
   * @author caotc
   * @date 2019-04-08
   * @since 1.0.0
   */
  public int compareTo(java.math.BigDecimal val) {
    return value.compareTo(val);
  }

  @Override
  public int compareTo(@NonNull BigDecimal o) {
    return value.compareTo(o.value);
  }

  @Override
  public int compareTo(@NonNull BigInteger o) {
    return value.compareTo(o.bigDecimalValue());
  }

  @Override
  public int compareTo(@NonNull Fraction o) {
    return toFractionExact().compareTo(o);
  }

  @Value
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class DivideResult {

    @NonNull
    BigDecimal quotient;
    @NonNull
    BigDecimal remainder;
  }
}
