package org.caotc.unit4j.core.math.number;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.math.MathContext;
import lombok.NonNull;
import lombok.Value;

/**
 * @author caotc
 * @date 2019-04-03
 * @implSpec
 * @implNote
 * @apiNote
 * @since 1.0.0
 */
@Value
@Beta
public class Fraction extends AbstractNumber {

  @NonNull
  public static Fraction valueOf(long numerator, long denominator) {
    return valueOf(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
  }

  @NonNull
  public static Fraction valueOf(BigInteger numerator, BigInteger denominator) {
    return new Fraction(numerator, denominator);
  }

  /**
   * 分子
   */
  BigInteger numerator;
  /**
   * 分母,必然为正数
   */
  BigInteger denominator;

  private Fraction(BigInteger numerator, BigInteger denominator) {
    Preconditions.checkArgument(!denominator.isZero(), "denominator can't be zero");
    if (denominator.isNegative()) {
      this.denominator = denominator.negate();
      this.numerator = numerator.negate();
    } else {
      this.denominator = denominator;
      this.numerator = numerator;
    }
  }

  @Override
  public boolean isZero() {
    return numerator.isZero();
  }

  @Override
  public boolean isPositive() {
    return numerator.isPositive();
  }

  @Override
  public boolean isNegative() {
    return numerator.isNegative();
  }

  @Override
  public @NonNull Fraction pow(int exponent) {
//    return valueOf(numerator().pow(exponent),denominator().pow(exponent));
    return numerator().pow(exponent).divide(denominator().pow(exponent)).toFractionExact();
  }

  @Override
  public @NonNull AbstractNumber negate() {
    return valueOf(numerator.negate(), denominator);
  }

  /**
   * //TODO 超过上限问题
   *
   * @author caotc
   * @date 2019-04-03
   * @since 1.0.0
   */
  @NonNull
  public Fraction expand(long multiple) {
    return valueOf(numerator().multiply(BigInteger.valueOf(multiple)),
        denominator().multiply(BigInteger.valueOf(multiple)));
  }

  /**
   * @author caotc
   * @date 2019-04-03
   * @since 1.0.0
   */
  @NonNull
  public Fraction expand(BigInteger multiple) {
    return valueOf(numerator().multiply(multiple), denominator().multiply(multiple));
  }

  @Override
  public @NonNull BigInteger toBigInteger() {
    return toBigDecimal().toBigInteger();
  }

  @Override
  public @NonNull BigInteger toBigIntegerExact() {
    return toBigDecimalExact().toBigIntegerExact();
  }

  @Override
  public @NonNull BigDecimal toBigDecimal() {
    return BigDecimal
        .valueOf(numerator().bigDecimalValue()
            .divide(denominator().bigDecimalValue(), DEFAULT_MATH_CONTEXT));
  }


  @SuppressWarnings("BigDecimalMethodWithoutRoundingCalled")
  @Override
  public @NonNull BigDecimal toBigDecimalExact() {
    return BigDecimal
        .valueOf(numerator().bigDecimalValue().divide(denominator().bigDecimalValue()));
  }

  @Override
  public @NonNull Fraction toFraction() {
    return this;
  }

  @Override
  public @NonNull Fraction toFractionExact() {
    return this;
  }

  @Override
  @NonNull
  public Fraction reciprocal() {
    return valueOf(denominator(), numerator());
  }

  /**
   * Returns a {@code Fraction} whose value is {@code (this + augend)}. use global configuration.
   *
   * @param augend value to be added to this {@code Fraction}.
   * @return {@code this + augend}
   * @author caotc
   * @date 2019-4-6
   * @since 1.0.0
   */
  @Override
  @NonNull
  public Fraction add(@NonNull Fraction augend) {
    return add(augend, DEFAULT_MATH_CONTEXT);
  }

  @Override
  public @NonNull AbstractNumber add(@NonNull BigDecimal augend) {
    return add(augend.toFractionExact());
  }

  @Override
  public @NonNull AbstractNumber add(@NonNull BigInteger augend) {
    return valueOf(numerator().add(denominator().multiply(augend)), denominator());
  }

  /**
   * Returns a {@code Fraction} whose value is {@code (this + augend)}.
   *
   * @param augend value to be added to this {@code Fraction}.
   * @param mathContext the context to add.
   * @return {@code this + augend}
   * @author caotc
   * @date 2019-4-6
   * @since 1.0.0
   */
  @NonNull
  public Fraction add(@NonNull Fraction augend, @NonNull MathContext mathContext) {
    Fraction thisExpand = expand(augend.denominator());
    Fraction oExpand = augend.expand(denominator());
    return valueOf(thisExpand.numerator().add(oExpand.numerator()), thisExpand.denominator());
  }

  /**
   * Returns a {@code Fraction} whose value is {@code (this - subtrahend)}. use global
   * configuration.
   *
   * @param subtrahend value to be subtracted from this {@code Fraction}.
   * @return {@code this - subtrahend}
   * @author caotc
   * @date 2019-4-6
   * @since 1.0.0
   */
  @Override
  @NonNull
  public Fraction subtract(@NonNull Fraction subtrahend) {
    return subtract(subtrahend, DEFAULT_MATH_CONTEXT);
  }

  @Override
  public @NonNull AbstractNumber subtract(@NonNull BigDecimal subtrahend) {
    return subtract(subtrahend.toFractionExact());
  }

  /**
   * Returns a {@code Fraction} whose value is {@code (this - subtrahend)}.
   *
   * @param subtrahend value to be subtracted from this {@code Fraction}.
   * @param mathContext the context to subtract
   * @return {@code this - subtrahend}
   * @author caotc
   * @date 2019-4-6
   * @since 1.0.0
   */
  @NonNull
  public Fraction subtract(@NonNull Fraction subtrahend,
      @NonNull MathContext mathContext) {
    Fraction thisExpand = expand(subtrahend.denominator());
    Fraction oExpand = subtrahend.expand(denominator());
    return valueOf(thisExpand.numerator().subtract(oExpand.numerator()), thisExpand.denominator());
  }

  /**
   * Returns a {@code Fraction} whose value is <tt>(this &times; multiplicand)</tt>. use global
   * configuration.
   *
   * @param multiplicand value to be multiplied by this {@code Fraction}.
   * @return {@code this * multiplicand}
   * @author caotc
   * @date 2019-4-6
   * @since 1.0.0
   */
  @Override
  @NonNull
  public Fraction multiply(@NonNull Fraction multiplicand) {
    return multiply(multiplicand, DEFAULT_MATH_CONTEXT);
  }

  @Override
  public @NonNull AbstractNumber multiply(@NonNull BigDecimal multiplicand) {
    return multiply(multiplicand.toFractionExact());
  }

  @Override
  public @NonNull AbstractNumber multiply(@NonNull BigInteger multiplicand) {
    return valueOf(numerator().multiply(multiplicand), denominator());
  }

  /**
   * Returns a {@code Fraction} whose value is <tt>(this &times; multiplicand)</tt>.
   *
   * @param multiplicand value to be multiplied by this {@code Fraction}.
   * @param mathContext the context to multiply
   * @return {@code this * multiplicand}
   * @author caotc
   * @date 2019-4-6
   * @since 1.0.0
   */
  @NonNull
  public Fraction multiply(@NonNull Fraction multiplicand,
      @NonNull MathContext mathContext) {
    return valueOf(numerator().multiply(multiplicand.numerator()),
        denominator().multiply(multiplicand.denominator()));
  }

  /**
   * Returns a {@code Fraction} whose value is {@code (this / divisor)}. use global configuration.
   *
   * @param divisor value by which this {@code Fraction} is to be divided.
   * @return {@code this / divisor}
   * @author caotc
   * @date 2019-4-6
   * @since 1.0.0
   */
  @Override
  @NonNull
  public Fraction divide(@NonNull Fraction divisor) {
    return divide(divisor, DEFAULT_MATH_CONTEXT);
  }

  @Override
  public @NonNull Fraction divide(@NonNull BigDecimal divisor) {
    return divide(divisor.toFractionExact());
  }

  @Override
  public @NonNull Fraction divide(@NonNull BigInteger divisor) {
    return valueOf(numerator(), denominator().multiply(divisor));
  }

  /**
   * Returns a {@code Fraction} whose value is {@code (this / divisor)}.
   *
   * @param divisor value by which this {@code Fraction} is to be divided.
   * @param mathContext the context to divide
   * @return {@code this / divisor}
   * @author caotc
   * @date 2019-4-6
   * @since 1.0.0
   */
  @NonNull
  public Fraction divide(@NonNull Fraction divisor, @NonNull MathContext mathContext) {
    return multiply(divisor.reciprocal(), mathContext);
  }

  @Override
  public int compareTo(@NonNull Fraction o) {
    Fraction thisExpand = expand(o.denominator());
    Fraction oExpand = o.expand(denominator());
    return thisExpand.numerator().compareTo(oExpand.numerator());
  }

  @Override
  public int compareTo(@NonNull BigDecimal o) {
    return compareTo(o.toFractionExact());
  }

  @Override
  public int compareTo(@NonNull BigInteger o) {
    return compareTo(o.toFractionExact());
  }
}
