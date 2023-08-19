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
    public static final Number ZERO = valueOf(BigFraction.ZERO);
    @NonNull
    public static final Number ONE = valueOf(BigFraction.ONE);

    @NonNull
    public static Number valueOf(long value) {
        return valueOf(new BigFraction(value));
    }

    @NonNull
    public static Number valueOf(double value) {
        return valueOf(new BigFraction(value));
    }

    @NonNull
    public static Number valueOf(@NonNull BigFraction value) {
        return BigFractionAdapter.of(value);
    }

    @NonNull
    public static Number valueOf(@NonNull BigInteger value) {
        return valueOf(new BigFraction(value));
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
