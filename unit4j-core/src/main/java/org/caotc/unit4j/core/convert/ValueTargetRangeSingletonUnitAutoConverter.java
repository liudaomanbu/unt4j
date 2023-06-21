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
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.math.number.AbstractNumber;
import org.caotc.unit4j.core.math.number.BigInteger;
import org.caotc.unit4j.core.unit.UnitGroup;

import java.util.stream.IntStream;

/**
 * @author caotc
 * @date 2023-06-17
 * @since 1.0.0
 */
@Value(staticConstructor = "of")
@Slf4j
public class ValueTargetRangeSingletonUnitAutoConverter implements SingletonUnitAutoConverter {
    @NonNull
    Range<AbstractNumber> valueTargetRange;
    @NonNull
    @Getter(lazy = true)
    Range<AbstractNumber> valueTargetLowerRange = valueTargetRange().hasLowerBound() ?
            Range.upTo(valueTargetRange().lowerEndpoint(), valueTargetRange().lowerBoundType() == BoundType.CLOSED ? BoundType.OPEN : BoundType.CLOSED) :
            Range.closedOpen((AbstractNumber) BigInteger.ZERO, BigInteger.ZERO);

    private Quantity indexedBinarySearch(Quantity quantity, Configuration configuration) {
        UnitGroup list = configuration.getUnitGroup(quantity.unit());

        int low = 0;
        int high = list.size() - 1;

        int mid = 0;
        Quantity current = quantity;
        while (low <= high) {
            mid = (low + high) >>> 1;
            current = configuration.convertTo(quantity, list.get(mid));

            if (valueTargetRange().contains(current.value())) {
                return current;
            }

            if (valueTargetLowerRange().contains(current.value())) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        // key not found
        if (mid != 0 && mid != (list.size() - 1)) {
            if (valueTargetLowerRange().contains(current.value())) {
                return IntStream.iterate(mid, i -> i - 1).mapToObj(list::get)
                        .map(unit -> configuration.convertTo(quantity, unit))
                        .filter(q -> !valueTargetLowerRange().contains(q.value()))
                        .findFirst()
                        .orElseThrow(AssertionError::new);
            } else {
                return list.tailSet(current.unit()).stream()
                        .map(unit -> configuration.convertTo(quantity, unit))
                        .filter(q -> valueTargetLowerRange().contains(q.value()))
                        .findFirst()
                        .orElseThrow(AssertionError::new);
            }
        } else {
            return current;
        }
    }

    @Override
    public @NonNull Quantity autoConvert(@NonNull Quantity quantity, @NonNull Configuration configuration) {
        if (valueTargetRange().contains(quantity.value())) {
            return quantity;
        }

        return indexedBinarySearch(quantity, configuration);
    }
}
