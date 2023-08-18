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
import org.apache.commons.math3.fraction.BigFraction;

/**
 * @author caotc
 * @date 2020-06-25
 * @since 1.0.0
 */
@Value
public class UnkownNumber implements Number {
    public static final UnkownNumber INSTANCE = new UnkownNumber();

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
    public @NonNull Number add(@NonNull Number augend) {
        return null;
    }

    @Override
    public @NonNull Number multiply(@NonNull Number multiplicand) {
        return null;
    }


    @Override
    public @NonNull Number pow(int exponent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Number negate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Number reciprocal() {
        return null;
    }

    @Override
    public int compareTo(@NonNull Number o) {
        return 0;
    }

    @Override
    public @NonNull BigFraction bigFractionValue() {
        return null;
    }

    @Override
    public @NonNull BigFraction bigFractionValueExact() {
        return null;
    }


}
