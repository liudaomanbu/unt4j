package org.caotc.unit4j.core.convert;

import com.google.common.collect.ImmutableList;
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
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.UnitGroup;
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
