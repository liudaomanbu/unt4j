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

package org.caotc.unit4j.core.convert;


import com.google.common.collect.RangeSet;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.math.number.AbstractNumber;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.UnitGroup;

/**
 * @author caotc
 * @date 2023-06-17
 * @since 1.0.0
 */
@Value(staticConstructor = "of")
public class TargetValueUnitChooser implements UnitChooser {
    @NonNull
    AbstractNumber targetValue;
    boolean higher;
    @NonNull
    RangeSet<AbstractNumber> valueRange;

    @Override
    public @NonNull Unit choose(@NonNull Quantity quantity, @NonNull Configuration configuration) {
        if (valueRange().contains(quantity.value())) {
            return quantity.unit();
        }
        UnitGroup unitGroup = configuration.getUnitGroup(quantity.unit());

        Unit currentUnit = quantity.unit();
        while (quantity.value().compareTo(targetValue) > 0 && unitGroup.higher(currentUnit).isPresent()) {

        }
        return null;
    }
}
