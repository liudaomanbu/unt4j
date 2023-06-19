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


import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.math.number.AbstractNumber;
import org.caotc.unit4j.core.math.number.BigInteger;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.UnitGroup;

import java.util.List;

/**
 * @author caotc
 * @date 2023-06-17
 * @since 1.0.0
 */
@Value(staticConstructor = "of")
@Slf4j
public class ValueTargetRangeUnitChooser implements UnitChooser {
    @NonNull
    Range<AbstractNumber> valueTargetRange;

    private static Unit indexedBinarySearch(List<Unit> list, Range<AbstractNumber> valueTargetRange, Quantity quantity, Configuration configuration) {
        int low = 0;
        int high = list.size() - 1;

        Range<AbstractNumber> lowerRange = valueTargetRange.hasLowerBound() ?
                Range.upTo(valueTargetRange.lowerEndpoint(), valueTargetRange.lowerBoundType() == BoundType.CLOSED ? BoundType.OPEN : BoundType.CLOSED) :
                Range.closedOpen((AbstractNumber) BigInteger.ZERO, BigInteger.ZERO);

        Quantity current = quantity;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            current = configuration.convertTo(quantity, list.get(mid));
            log.error("current value:{},unit:{}", current.bigDecimalValue(), current.unit().id());

            if (valueTargetRange.contains(current.value())) {
                return current.unit();
            }

            if (lowerRange.contains(current.value())) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return current.unit();  // key not found
    }

    @Override
    public @NonNull Unit choose(@NonNull Quantity quantity, @NonNull Configuration configuration) {
        if (valueTargetRange().contains(quantity.value())) {
            return quantity.unit();
        }
        UnitGroup unitGroup = configuration.getUnitGroup(quantity.unit());

        return indexedBinarySearch(unitGroup.units(), valueTargetRange(), quantity, configuration);
    }
}
