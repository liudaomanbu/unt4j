/*
 * Copyright (C) 2020 the original author or authors.
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
import lombok.Value;

/**
 * @author caotc
 * @date 2020-06-25
 * @since 1.0.0
 */
@Value
public class UnkownNumber extends AbstractNumber {
    public static final UnkownNumber INSTANCE = new UnkownNumber();

    @Override
    public @NonNull BigInteger toBigInteger() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull BigInteger toBigIntegerExact() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull BigDecimal toBigDecimal() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull BigDecimal toBigDecimalExact() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Fraction toFraction() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Fraction toFractionExact() {
        throw new UnsupportedOperationException();
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
    public @NonNull AbstractNumber add(@NonNull Fraction augend) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull AbstractNumber add(@NonNull BigDecimal augend) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull AbstractNumber add(@NonNull BigInteger augend) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull AbstractNumber multiply(@NonNull Fraction multiplicand) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull AbstractNumber multiply(@NonNull BigDecimal multiplicand) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull AbstractNumber multiply(@NonNull BigInteger multiplicand) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull AbstractNumber divide(@NonNull Fraction divisor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull AbstractNumber divide(@NonNull BigDecimal divisor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull AbstractNumber divide(@NonNull BigInteger divisor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull AbstractNumber pow(int exponent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull AbstractNumber negate() {
        throw new UnsupportedOperationException();
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
}
