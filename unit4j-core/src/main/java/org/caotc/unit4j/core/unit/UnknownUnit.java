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

package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.unit.type.UnitType;

/**
 * @author caotc
 * @date 2020-06-25
 * @since 1.0.0
 */
@Value
public class UnknownUnit extends Unit {
    public static final UnknownUnit INSTANCE = new UnknownUnit();
    private static final UnitType TYPE = UnitType.of("unknown");

    @Override
    public @NonNull Prefix prefix() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull UnitType type() {
        return TYPE;
    }

    @Override
    public @NonNull Unit rebase() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Unit simplify(@NonNull SimplifyConfig config) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull ImmutableMap<Unit, Integer> componentToExponents() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Unit pow(int exponent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Unit reciprocal() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Unit multiply(@NonNull BaseStandardUnit multiplicand) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Unit multiply(@NonNull BasePrefixUnit multiplicand) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Unit multiply(@NonNull CompositeStandardUnit multiplicand) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Unit multiply(@NonNull CompositePrefixUnit multiplicand) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull String id() {
        return "unknown";
    }

}
