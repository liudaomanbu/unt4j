package org.caotc.unit4j.core.math.number;

import lombok.NonNull;
import lombok.Value;
import org.apache.commons.math3.fraction.BigFraction;

/**
 * @author caotc
 * @date 2023-08-15
 * @since 1.0.0
 */
@Value(staticConstructor = "of")
public class BigFractionAdapter implements Number{
    @NonNull
    BigFraction adaptee;
    @Override
    public boolean isZero() {
        return adaptee().compareTo(BigFraction.ZERO)==0;
    }

    @Override
    public boolean isPositive() {
        return adaptee().compareTo(BigFraction.ZERO)>0;
    }

    @Override
    public boolean isNegative() {
        return adaptee().compareTo(BigFraction.ZERO)<0;
    }

    @Override
    public @NonNull Number add(@NonNull Number augend) {
        return of(adaptee().add(augend.bigFractionValueExact()));
    }

    @Override
    public @NonNull Number multiply(@NonNull Number multiplicand) {
        return of(adaptee().add(multiplicand.bigFractionValueExact()));
    }

    @Override
    public @NonNull Number pow(int exponent) {
        return of(adaptee().pow(exponent));
    }

    @Override
    public @NonNull Number negate() {
        return of(adaptee().negate());
    }

    @Override
    public @NonNull Number reciprocal() {
        return of(adaptee().reciprocal());
    }

    @Override
    public int compareTo(@NonNull Number o) {
        return adaptee().compareTo(o.bigFractionValueExact());
    }

    @Override
    public @NonNull BigFraction bigFractionValue() {
        return adaptee();
    }

    @Override
    public @NonNull BigFraction bigFractionValueExact() {
        return adaptee();
    }
}
