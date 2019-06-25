package org.caotc.unit4j.core.math.number;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * @author caotc
 * @date 2019-04-14
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@AllArgsConstructor(staticName = "valueOf")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BigInteger extends AbstractNumber {

  public static final BigInteger ZERO = valueOf(java.math.BigInteger.ZERO);

  public static final BigInteger ONE = valueOf(java.math.BigInteger.ONE);

  public static final BigInteger TEN = valueOf(java.math.BigInteger.TEN);

  /**
   * @author caotc
   * @date 2019-04-14
   * @since 1.0.0
   */
  public static BigInteger valueOf(long val) {
    return valueOf(java.math.BigInteger.valueOf(val));
  }

  public BigInteger add(java.math.BigInteger val) {
    return valueOf(value.add(val));
  }

  public BigInteger subtract(java.math.BigInteger val) {
    return valueOf(value.subtract(val));
  }

  public BigInteger multiply(java.math.BigInteger val) {
    return valueOf(value.multiply(val));
  }

  public BigInteger divide(java.math.BigInteger val) {
    return valueOf(value.divide(val));
  }

  public java.math.BigInteger[] divideAndRemainder(java.math.BigInteger val) {
    return value.divideAndRemainder(val);
  }

  public java.math.BigInteger remainder(java.math.BigInteger val) {
    return value.remainder(val);
  }

  public java.math.BigInteger gcd(java.math.BigInteger val) {
    return value.gcd(val);
  }

  public java.math.BigInteger mod(java.math.BigInteger m) {
    return value.mod(m);
  }

  public java.math.BigInteger modPow(java.math.BigInteger exponent, java.math.BigInteger m) {
    return value.modPow(exponent, m);
  }

  public java.math.BigInteger modInverse(java.math.BigInteger m) {
    return value.modInverse(m);
  }

  public java.math.BigInteger shiftLeft(int n) {
    return value.shiftLeft(n);
  }

  public java.math.BigInteger shiftRight(int n) {
    return value.shiftRight(n);
  }

  public java.math.BigInteger and(java.math.BigInteger val) {
    return value.and(val);
  }

  public java.math.BigInteger or(java.math.BigInteger val) {
    return value.or(val);
  }

  public java.math.BigInteger xor(java.math.BigInteger val) {
    return value.xor(val);
  }

  public java.math.BigInteger not() {
    return value.not();
  }

  public java.math.BigInteger andNot(java.math.BigInteger val) {
    return value.andNot(val);
  }

  public boolean testBit(int n) {
    return value.testBit(n);
  }

  public java.math.BigInteger setBit(int n) {
    return value.setBit(n);
  }

  public java.math.BigInteger clearBit(int n) {
    return value.clearBit(n);
  }

  public java.math.BigInteger flipBit(int n) {
    return value.flipBit(n);
  }

  public int getLowestSetBit() {
    return value.getLowestSetBit();
  }

  public int bitLength() {
    return value.bitLength();
  }

  public int bitCount() {
    return value.bitCount();
  }

  public boolean isProbablePrime(int certainty) {
    return value.isProbablePrime(certainty);
  }

  public int compareTo(java.math.BigInteger val) {
    return value.compareTo(val);
  }

  public java.math.BigInteger min(java.math.BigInteger val) {
    return value.min(val);
  }

  public java.math.BigInteger max(java.math.BigInteger val) {
    return value.max(val);
  }

  @NonNull
  java.math.BigInteger value;


  @Override
  public @NonNull BigInteger toBigInteger() {
    return this;
  }

  @Override
  public @NonNull BigInteger toBigIntegerExact() {
    return this;
  }

  @Override
  public @NonNull BigDecimal toBigDecimal() {
    return toBigDecimalExact();
  }

  @Override
  public @NonNull BigDecimal toBigDecimalExact() {
    return BigDecimal.valueOf(value);
  }

  @Override
  public @NonNull Fraction toFraction() {
    return toFractionExact();
  }

  @Override
  public @NonNull Fraction toFractionExact() {
    return Fraction.valueOf(this, ONE);
  }

  @Override
  public boolean isZero() {
    return value.signum() == 0;
  }

  @Override
  public boolean isPositive() {
    return value.signum() > 0;
  }

  @Override
  public boolean isNegative() {
    return value.signum() < 0;
  }

  @Override
  public @NonNull Fraction add(@NonNull Fraction augend) {
    return toFractionExact().add(augend);
  }

  @Override
  public @NonNull BigDecimal add(@NonNull BigDecimal augend) {
    return toBigDecimalExact().add(augend);
  }

  @Override
  public @NonNull BigInteger add(@NonNull BigInteger augend) {
    return valueOf(value.add(augend.value));
  }

  @Override
  public @NonNull BigInteger subtract(@NonNull BigInteger subtrahend) {
    return valueOf(value.subtract(subtrahend.value));
  }

  @Override
  public @NonNull Fraction multiply(@NonNull Fraction multiplicand) {
    return toFractionExact().multiply(multiplicand);
  }

  @Override
  public @NonNull BigDecimal multiply(@NonNull BigDecimal multiplicand) {
    return toBigDecimalExact().multiply(multiplicand);
  }

  @Override
  public @NonNull BigInteger multiply(@NonNull BigInteger multiplicand) {
    return valueOf(value.multiply(multiplicand.value));
  }

  @Override
  public @NonNull Fraction divide(@NonNull Fraction divisor) {
    return toFractionExact().divide(divisor);
  }

  @Override
  public @NonNull Fraction divide(@NonNull BigDecimal divisor) {
    return toFractionExact().divide(divisor);
  }

  @Override
  public @NonNull Fraction divide(@NonNull BigInteger divisor) {
    return toFractionExact().divide(divisor);
  }

  @Override
  public @NonNull AbstractNumber pow(int exponent) {
    if (exponent > 0) {
      return valueOf(value.pow(exponent));
    }
    return valueOf(value.pow(-exponent)).reciprocal();
  }

  @Override
  public long longValue() {
    return value.longValue();
  }

  @Override
  public long longValueExact() {
    return value.longValueExact();
  }

  @Override
  public @NonNull BigInteger negate() {
    return valueOf(value.negate());
  }

  @Override
  public int compareTo(@NonNull Fraction o) {
    return toFractionExact().compareTo(o);
  }

  @Override
  public int compareTo(@NonNull BigDecimal o) {
    return toBigDecimalExact().compareTo(o);
  }

  @Override
  public int compareTo(@NonNull BigInteger o) {
    return value.compareTo(o.value);
  }

  @NonNull
  @Override
  public java.math.BigInteger bigIntegerValue() {
    return value;
  }

}
