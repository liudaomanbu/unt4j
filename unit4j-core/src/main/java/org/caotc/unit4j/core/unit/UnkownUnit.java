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
import com.google.common.collect.ImmutableSet;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.unit.type.BaseUnitType;
import org.caotc.unit4j.core.unit.type.UnitType;

import java.util.Optional;

/**
 * @author caotc
 * @date 2020-06-25
 * @since 1.0.0
 */
@Value
public class UnkownUnit implements Unit {
    public static final UnkownUnit INSTANCE = new UnkownUnit();
    private static final UnitType TYPE = BaseUnitType.create("unknow");

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
    public @NonNull ImmutableMap<Unit, Integer> unitComponentToExponents() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Unit power(int exponent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Unit inverse() {
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
    public @NonNull ImmutableSet<Alias> aliases(@NonNull Configuration configuration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Optional<Alias> alias(@NonNull Configuration configuration, Alias.@NonNull Type aliasType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Optional<Alias> compositeAliasFromConfiguration(@NonNull Configuration configuration, Alias.@NonNull Type aliasType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull String id() {
        return "unknow";
    }
}
