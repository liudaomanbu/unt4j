package org.caotc.unit4j.core.math.number;

import lombok.NonNull;
import lombok.Value;

/**
 * @author caotc
 * @date 2023-08-11
 * @since 1.0.0
 */
@Value
public class JavaNumberAdapter implements Number{
    @NonNull java.lang.Number delegate;

    @Override
    public @NonNull BigInteger toBigInteger() {
        return null;
    }

    @Override
    public @NonNull BigInteger toBigIntegerExact() {
        return null;
    }

    @Override
    public @NonNull BigDecimal toBigDecimal() {
        return null;
    }

    @Override
    public @NonNull BigDecimal toBigDecimalExact() {
        return null;
    }

    @Override
    public @NonNull Fraction toFraction() {
        return null;
    }

    @Override
    public @NonNull Fraction toFractionExact() {
        return null;
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean isPositive() {
        return false;
    }

    @Override
    public boolean isNegative() {
        return false;
    }

    @Override
    public @NonNull Number add(@NonNull Fraction augend) {
        return null;
    }

    @Override
    public @NonNull Number add(@NonNull BigDecimal augend) {
        return null;
    }

    @Override
    public @NonNull Number add(@NonNull BigInteger augend) {
        return null;
    }

    @Override
    public @NonNull Number multiply(@NonNull Fraction multiplicand) {
        return null;
    }

    @Override
    public @NonNull Number multiply(@NonNull BigDecimal multiplicand) {
        return null;
    }

    @Override
    public @NonNull Number multiply(@NonNull BigInteger multiplicand) {
        return null;
    }

    @Override
    public @NonNull Number divide(@NonNull Fraction divisor) {
        return null;
    }

    @Override
    public @NonNull Number divide(@NonNull BigDecimal divisor) {
        return null;
    }

    @Override
    public @NonNull Number divide(@NonNull BigInteger divisor) {
        return null;
    }

    @Override
    public @NonNull Number pow(int exponent) {
        return null;
    }

    @Override
    public @NonNull Number negate() {
        return null;
    }

    @Override
    public int compareTo(@NonNull Fraction o) {
        return 0;
    }

    @Override
    public int compareTo(@NonNull BigDecimal o) {
        return 0;
    }

    @Override
    public int compareTo(@NonNull BigInteger o) {
        return 0;
    }

    @Override
    public byte byteValue() {
        return delegate.byteValue();
    }

    @Override
    public short shortValue() {
        return delegate.shortValue();
    }

    @Override
    public int intValue() {
        return delegate.intValue();
    }

    @Override
    public long longValue() {
        return delegate.longValue();
    }

    @Override
    public float floatValue() {
        return delegate.floatValue();
    }

    @Override
    public double doubleValue() {
        return delegate.doubleValue();
    }

    @NonNull
    @Override
    public java.math.BigInteger bigIntegerValue() {
        return Number.super.bigIntegerValue();
    }

    @NonNull
    @Override
    public java.math.BigDecimal bigDecimalValue() {
        return Number.super.bigDecimalValue();
    }
}
