package org.caotc.unit4j.core.math.number;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.fraction.BigFraction;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author caotc
 * @date 2023-08-15
 * @since 1.0.0
 */
@UtilityClass
public class Numbers {
    @NonNull
    public static final Number ZERO=valueOf(BigFraction.ZERO);
    @NonNull
    public static final Number ONE=valueOf(BigFraction.ONE);

    @NonNull
    public static Number valueOf(byte value){
        return valueOf(new BigFraction(value));
    }

    @NonNull
    public static Number valueOf(@NonNull BigFraction value){
        return BigFractionAdapter.of(value);
    }

    @NonNull
    public static Number valueOf(BigDecimal decimal) {
        int scale = decimal.scale();
        if (scale <= 0) {
            return valueOf(new BigFraction(decimal.toBigInteger(), java.math.BigInteger.ONE));
        } else {
            java.math.BigInteger denominator = java.math.BigInteger.TEN.pow(scale);
            BigInteger numerator = decimal.unscaledValue();
            return valueOf(new BigFraction(numerator, denominator));
        }
    }
}
