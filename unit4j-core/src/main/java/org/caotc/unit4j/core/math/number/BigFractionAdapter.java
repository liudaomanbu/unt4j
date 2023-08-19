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
        return of(adaptee().multiply(multiplicand.bigFractionValueExact()));
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

    @Override
    public String toString() {
        return adaptee.toString();
    }
}
