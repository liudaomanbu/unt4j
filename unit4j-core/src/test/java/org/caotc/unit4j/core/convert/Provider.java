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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.math.number.AbstractNumber;
import org.caotc.unit4j.core.math.number.BigInteger;
import org.caotc.unit4j.core.unit.Prefix;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.UnitGroup;
import org.caotc.unit4j.core.unit.Units;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Collection;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2023-06-15
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class Provider {

    static Stream<Arguments> unitAutoConverterAndQuantityAndAutoConverted() {
        return Stream.of(
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.closed(BigInteger.ONE, BigInteger.valueOf(1000))), Quantity.create(8000, Units.METER), Quantity.create(80, Units.METER.addPrefix(Prefix.HECTO))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.closed(BigInteger.ONE, BigInteger.valueOf(1000))), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO)), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.closed(BigInteger.ONE, BigInteger.valueOf(1000))), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA)), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.closed(BigInteger.ONE, BigInteger.valueOf(1000))), Quantity.create(1, Units.METER), Quantity.create(1, Units.METER)),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.closed(BigInteger.ONE, BigInteger.valueOf(1000))), Quantity.create(1000, Units.METER), Quantity.create(1000, Units.METER)),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.closedOpen(BigInteger.ONE, BigInteger.valueOf(1000))), Quantity.create(8000, Units.METER), Quantity.create(80, Units.METER.addPrefix(Prefix.HECTO))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.closedOpen(BigInteger.ONE, BigInteger.valueOf(1000))), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO)), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.closedOpen(BigInteger.ONE, BigInteger.valueOf(1000))), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA)), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.closedOpen(BigInteger.ONE, BigInteger.valueOf(1000))), Quantity.create(1, Units.METER), Quantity.create(1, Units.METER)),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.closedOpen(BigInteger.ONE, BigInteger.valueOf(1000))), Quantity.create(1000, Units.METER), Quantity.create(10, Units.METER.addPrefix(Prefix.HECTO))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.openClosed(BigInteger.ONE, BigInteger.valueOf(1000))), Quantity.create(8000, Units.METER), Quantity.create(80, Units.METER.addPrefix(Prefix.HECTO))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.openClosed(BigInteger.ONE, BigInteger.valueOf(1000))), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO)), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.openClosed(BigInteger.ONE, BigInteger.valueOf(1000))), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA)), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.openClosed(BigInteger.ONE, BigInteger.valueOf(1000))), Quantity.create(1, Units.METER), Quantity.create(1000, Units.METER.addPrefix(Prefix.MILLI))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.openClosed(BigInteger.ONE, BigInteger.valueOf(1000))), Quantity.create(1000, Units.METER), Quantity.create(1000, Units.METER)),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.atMost(BigInteger.valueOf(1000))), Quantity.create(8000, Units.METER), Quantity.create("0.000008", Units.METER.addPrefix(Prefix.GIGA))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.atMost(BigInteger.valueOf(1000))), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO)), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.atMost(BigInteger.valueOf(1000))), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA)), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.atMost(BigInteger.valueOf(1000))), Quantity.create(1, Units.METER), Quantity.create(1, Units.METER)),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.atMost(BigInteger.valueOf(1000))), Quantity.create(1000, Units.METER), Quantity.create(1000, Units.METER)),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.lessThan(BigInteger.valueOf(1000))), Quantity.create(8000, Units.METER), Quantity.create("0.000008", Units.METER.addPrefix(Prefix.GIGA))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.lessThan(BigInteger.valueOf(1000))), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO)), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.lessThan(BigInteger.valueOf(1000))), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA)), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.lessThan(BigInteger.valueOf(1000))), Quantity.create(1, Units.METER), Quantity.create(1, Units.METER)),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.lessThan(BigInteger.valueOf(1000))), Quantity.create(1000, Units.METER), Quantity.create("0.00001", Units.METER.addPrefix(Prefix.GIGA))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.atLeast(BigInteger.valueOf(1))), Quantity.create(8000, Units.METER), Quantity.create(8000, Units.METER)),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.atLeast(BigInteger.valueOf(1))), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO)), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.atLeast(BigInteger.valueOf(1))), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA)), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.atLeast(BigInteger.valueOf(1))), Quantity.create(1, Units.METER), Quantity.create(1, Units.METER)),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.atLeast(BigInteger.valueOf(1))), Quantity.create(1000, Units.METER), Quantity.create(1000, Units.METER)),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.greaterThan(BigInteger.valueOf(1))), Quantity.create(8000, Units.METER), Quantity.create(8000, Units.METER)),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.greaterThan(BigInteger.valueOf(1))), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO)), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.greaterThan(BigInteger.valueOf(1))), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA)), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.greaterThan(BigInteger.valueOf(1))), Quantity.create(1, Units.METER), Quantity.create(BigInteger.TEN.pow(12), Units.METER.addPrefix(Prefix.PICO))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.greaterThan(BigInteger.valueOf(1))), Quantity.create(1000, Units.METER), Quantity.create(1000, Units.METER)),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.singleton(BigInteger.ONE)), Quantity.create(8000, Units.METER), Quantity.create(8, Units.METER.addPrefix(Prefix.KILO))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.singleton(BigInteger.ONE)), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO)), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.singleton(BigInteger.ONE)), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA)), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.singleton(BigInteger.ONE)), Quantity.create(1, Units.METER), Quantity.create(1, Units.METER)),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.singleton(BigInteger.ONE)), Quantity.create(1000, Units.METER), Quantity.create(1, Units.METER.addPrefix(Prefix.KILO))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.singleton(BigInteger.ONE), false), Quantity.create(8000, Units.METER), Quantity.create("0.008", Units.METER.addPrefix(Prefix.MEGA))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.singleton(BigInteger.ONE), false), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO)), Quantity.create("0.1", Units.METER.addPrefix(Prefix.YOCTO))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.singleton(BigInteger.ONE), false), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA)), Quantity.create("1000.1", Units.METER.addPrefix(Prefix.YOTTA))),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.singleton(BigInteger.ONE), false), Quantity.create(1, Units.METER), Quantity.create(1, Units.METER)),
                Arguments.of(ValueTargetRangeSingletonAutoConverter.of(Range.singleton(BigInteger.ONE), false), Quantity.create(1000, Units.METER), Quantity.create(1, Units.METER.addPrefix(Prefix.KILO)))
        );
    }


    static QuantityGroup randomQuantityGroup(@NonNull UnitGroup unitGroup, int size) {
        ImmutableList<Unit> units = unitGroup.stream().collect(ImmutableList.toImmutableList());
        Random random = new Random();
        QuantityGroup.Builder builder = QuantityGroup.builder();
        IntStream.generate(random::nextInt)
                .mapToObj(BigInteger::valueOf)
                .map(num -> Quantity.create(num, units.get(random.nextInt(units.size()))))
                .limit(size)
                .forEach(builder::quantity);
        return builder.build();
    }

    static Stream<Arguments> quantityCollectionAndConfigurationAndTarget() {
        return Stream.of(
                Arguments.of()
        );
    }

    @Builder
    @Getter
    static class QuantityGroup {
        @Singular
        Collection<Quantity> quantities;

        public Quantity max() {
            return quantities.stream().max(Configuration.defaultInstance()::compare).orElseThrow(AssertionError::new);
        }

        public Quantity min() {
            return quantities.stream().min(Configuration.defaultInstance()::compare).orElseThrow(AssertionError::new);
        }

        public Quantity median() {
            ImmutableList<Quantity> sortedQuantities = quantities.stream().sorted(Configuration.defaultInstance()::compare)
                    .collect(ImmutableList.toImmutableList());
            int medianIndex = sortedQuantities.size() / 2;

            //集合总数是否是偶数
            boolean sizeIsEven = (sortedQuantities.size() % 2) == 0;
            if (sizeIsEven) {
                //偶数时中位数为中位的两个数值的平均数
                Quantity quantity1 = sortedQuantities.get(medianIndex - 1);
                Quantity quantity2 = sortedQuantities.get(medianIndex);
                return Quantity.create(quantity1.value().add(quantity2.convertTo(quantity1.unit()).value()).divide(BigInteger.valueOf(2)), quantity1.unit());
            }
            return sortedQuantities.get(medianIndex);
        }

        public Quantity average() {
            Unit unit = quantities.stream().findAny().map(Quantity::unit).orElseThrow(AssertionError::new);
            return quantities.stream()
                    .map(quantity -> quantity.convertTo(unit))
                    .map(Quantity::value)
                    .reduce(AbstractNumber::add)
                    .map(num -> num.divide(BigInteger.valueOf(quantities.size())))
                    .map(num -> Quantity.create(num, unit))
                    .orElseThrow(AssertionError::new);
        }
    }
}
