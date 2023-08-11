package org.caotc.unit4j.core.common.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.fraction.BigFraction;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author caotc
 * @date 2023-08-11
 * @since 1.0.0
 */
@UtilityClass
public class MathUtil {
    @NonNull
    public static BigFraction toBigFraction(BigDecimal decimal) {
        int scale = decimal.scale();
        if (scale <= 0) {
            return new BigFraction(decimal.toBigInteger(), BigInteger.ONE);
        } else {
            BigInteger denominator = BigInteger.TEN.pow(scale);
            BigInteger numerator = decimal.unscaledValue();
            return new BigFraction(numerator, denominator);
        }
    }
}
