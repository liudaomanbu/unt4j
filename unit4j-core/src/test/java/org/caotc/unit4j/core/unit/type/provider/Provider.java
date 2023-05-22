package org.caotc.unit4j.core.unit.type.provider;

import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.caotc.unit4j.core.unit.type.UnitType;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Random;
import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2022-08-18
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class Provider {
    static Stream<Arguments> originalAndInverseds() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(UnitTypes.NON, UnitTypes.NON),
                Arguments.of(UnitTypes.LENGTH, UnitType.builder().componentToExponent(UnitTypes.LENGTH, -1).build()),
                Arguments.of(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, -1).build()),
                Arguments.of(UnitType.builder().componentToExponent(UnitTypes.LENGTH, -1).build(), UnitTypes.LENGTH),
                Arguments.of(UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, -1).build(), UnitTypes.ENERGY_WORK_HEAT_QUANTITY),
                Arguments.of(UnitType.builder().componentToExponent(UnitTypes.LENGTH, exponent).build(), UnitType.builder().componentToExponent(UnitTypes.LENGTH, -exponent).build()),
                Arguments.of(UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, exponent).build(), UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, -exponent).build())
        );
    }

    static Stream<Arguments> multiplierAndMultiplicandAndResults() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(UnitTypes.NON, UnitTypes.LENGTH, UnitTypes.LENGTH),
                Arguments.of(UnitTypes.NON, UnitTypes.ENERGY_WORK_HEAT_QUANTITY, UnitTypes.ENERGY_WORK_HEAT_QUANTITY),
                Arguments.of(UnitTypes.NON, UnitTypes.NON, UnitTypes.NON),
                Arguments.of(UnitTypes.LENGTH, UnitTypes.LENGTH, UnitType.builder().componentToExponent(UnitTypes.LENGTH, 2).build()),
                Arguments.of(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, UnitTypes.ENERGY_WORK_HEAT_QUANTITY, UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, 2).build()),
                Arguments.of(UnitTypes.LENGTH, UnitType.builder().componentToExponent(UnitTypes.LENGTH, -1).build(), UnitTypes.NON),
                Arguments.of(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, -1).build(), UnitTypes.NON),
                Arguments.of(UnitType.builder().componentToExponent(UnitTypes.LENGTH, exponent).build(), UnitType.builder().componentToExponent(UnitTypes.LENGTH, -exponent).build(), UnitTypes.NON),
                Arguments.of(UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, exponent).build(), UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, -exponent).build(), UnitTypes.NON),
                Arguments.of(UnitType.builder().componentToExponent(UnitTypes.LENGTH, Math.abs(exponent)).build(), UnitType.builder().componentToExponent(UnitTypes.LENGTH, -Math.abs(exponent) + 1).build(), UnitTypes.LENGTH),
                Arguments.of(UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, Math.abs(exponent)).build(), UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, -Math.abs(exponent) + 1).build(), UnitTypes.ENERGY_WORK_HEAT_QUANTITY),
                Arguments.of(UnitTypes.LENGTH, UnitType.builder().componentToExponent(UnitTypes.LENGTH, Math.abs(exponent)).build(), UnitType.builder().componentToExponent(UnitTypes.LENGTH, Math.abs(exponent) + 1).build()),
                Arguments.of(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, Math.abs(exponent)).build(), UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, Math.abs(exponent) + 1).build()),
                Arguments.of(UnitTypes.LENGTH, UnitTypes.FORCE_WEIGHT, UnitType.builder().componentToExponent(UnitTypes.LENGTH, 1).componentToExponent(UnitTypes.FORCE_WEIGHT, 1).build())
        );
    }

    static Stream<Arguments> multiplyArguments() {
        return Stream.concat(multiplierAndMultiplicandAndResults()
                , multiplierAndMultiplicandAndResults()
                        .filter(arguments -> !arguments.get()[0].equals(arguments.get()[1]))
                        .map(arguments -> Arguments.of(arguments.get()[1], arguments.get()[0], arguments.get()[2]))
        );
    }

    static Stream<Arguments> reversibleDividendAndDivisorAndQuotients() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(UnitTypes.LENGTH, UnitTypes.LENGTH, UnitTypes.NON),
                Arguments.of(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, UnitTypes.ENERGY_WORK_HEAT_QUANTITY, UnitTypes.NON),
                Arguments.of(UnitTypes.NON, UnitTypes.NON, UnitTypes.NON),
                Arguments.of(UnitType.builder().componentToExponent(UnitTypes.LENGTH, 2).build(), UnitTypes.LENGTH, UnitTypes.LENGTH),
                Arguments.of(UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, 2).build(), UnitTypes.ENERGY_WORK_HEAT_QUANTITY, UnitTypes.ENERGY_WORK_HEAT_QUANTITY),
                Arguments.of(UnitTypes.NON, UnitType.builder().componentToExponent(UnitTypes.LENGTH, -1).build(), UnitTypes.LENGTH),
                Arguments.of(UnitTypes.NON, UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, -1).build(), UnitTypes.ENERGY_WORK_HEAT_QUANTITY),
                Arguments.of(UnitTypes.NON, UnitType.builder().componentToExponent(UnitTypes.LENGTH, -exponent).build(), UnitType.builder().componentToExponent(UnitTypes.LENGTH, exponent).build()),
                Arguments.of(UnitTypes.NON, UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, -exponent).build(), UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, exponent).build()),
                Arguments.of(UnitTypes.LENGTH, UnitType.builder().componentToExponent(UnitTypes.LENGTH, -Math.abs(exponent) + 1).build(), UnitType.builder().componentToExponent(UnitTypes.LENGTH, Math.abs(exponent)).build()),
                Arguments.of(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, -Math.abs(exponent) + 1).build(), UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, Math.abs(exponent)).build()),
                Arguments.of(UnitType.builder().componentToExponent(UnitTypes.LENGTH, Math.abs(exponent) + 1).build(), UnitType.builder().componentToExponent(UnitTypes.LENGTH, Math.abs(exponent)).build(), UnitTypes.LENGTH),
                Arguments.of(UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, Math.abs(exponent) + 1).build(), UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, Math.abs(exponent)).build(), UnitTypes.ENERGY_WORK_HEAT_QUANTITY)
        );
    }

    static Stream<Arguments> divideArguments() {
        return Streams.concat(reversibleDividendAndDivisorAndQuotients()
                , reversibleDividendAndDivisorAndQuotients()
                        .filter(arguments -> !arguments.get()[1].equals(arguments.get()[2]))
                        .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[2], arguments.get()[1])),
                Stream.of(
                        Arguments.of(UnitType.builder().componentToExponent(UnitTypes.LENGTH, 1).componentToExponent(UnitTypes.FORCE_WEIGHT, 1).build(), UnitTypes.FORCE_WEIGHT, UnitType.builder().componentToExponent(UnitType.builder().componentToExponent(UnitTypes.LENGTH, 1).componentToExponent(UnitTypes.FORCE_WEIGHT, 1).build(), 1).componentToExponent(UnitTypes.FORCE_WEIGHT, -1).build()),
                        Arguments.of(UnitType.builder().componentToExponent(UnitTypes.LENGTH, 1).componentToExponent(UnitTypes.FORCE_WEIGHT, 1).build(), UnitTypes.LENGTH, UnitType.builder().componentToExponent(UnitType.builder().componentToExponent(UnitTypes.LENGTH, 1).componentToExponent(UnitTypes.FORCE_WEIGHT, 1).build(), 1).componentToExponent(UnitTypes.LENGTH, -1).build())));
    }

    static Stream<Arguments> recursiveEqualedOriginalAndSimplifieds() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(UnitTypes.NON, UnitTypes.NON),
                Arguments.of(UnitTypes.LENGTH, UnitTypes.LENGTH),
                Arguments.of(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, UnitTypes.ENERGY_WORK_HEAT_QUANTITY),
                Arguments.of(UnitTypes.FORCE_WEIGHT.multiply(UnitTypes.LENGTH), UnitType.builder().componentToExponent(UnitTypes.MASS, 1).componentToExponent(UnitTypes.LENGTH, 2).componentToExponent(UnitTypes.TIME, -2).build()),
                Arguments.of(UnitType.builder().componentToExponent(UnitTypes.LENGTH, -1).build(), UnitType.builder().componentToExponent(UnitTypes.LENGTH, -1).build()),
                Arguments.of(UnitType.builder().componentToExponent(UnitTypes.LENGTH, exponent).build(), UnitType.builder().componentToExponent(UnitTypes.LENGTH, exponent).build()),
                Arguments.of(UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, exponent).build(), UnitType.builder().componentToExponents(Maps.transformValues(UnitTypes.ENERGY_WORK_HEAT_QUANTITY.componentToExponents(), value -> value * exponent)).build())
        );
    }

    static Stream<Arguments> originalAndRecursiveAndSimplifieds() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.concat(recursiveEqualedOriginalAndSimplifieds()
                        .flatMap(arguments -> Stream.of(Arguments.of(arguments.get()[0], true, arguments.get()[1]), Arguments.of(arguments.get()[0], false, arguments.get()[1]))),
                Stream.of(
                        Arguments.of(UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, -1).build(), true, UnitType.builder().componentToExponents(Maps.transformValues(UnitTypes.ENERGY_WORK_HEAT_QUANTITY.componentToExponents(), value -> -value)).build()),
                        Arguments.of(UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, -1).build(), false, UnitType.builder().componentToExponent(UnitTypes.ENERGY_WORK_HEAT_QUANTITY, -1).build()),
                        Arguments.of(UnitType.builder().componentToExponent(UnitTypes.FORCE_WEIGHT.multiply(UnitTypes.LENGTH), -1).build(), true, UnitType.builder().componentToExponent(UnitTypes.MASS, -1).componentToExponent(UnitTypes.LENGTH, -2).componentToExponent(UnitTypes.TIME, 2).build()),
                        Arguments.of(UnitType.builder().componentToExponent(UnitTypes.FORCE_WEIGHT.multiply(UnitTypes.LENGTH), -1).build(), false, UnitType.builder().componentToExponent(UnitTypes.FORCE_WEIGHT.multiply(UnitTypes.LENGTH), -1).build()),
                        Arguments.of(UnitType.builder().componentToExponent(UnitTypes.FORCE_WEIGHT.multiply(UnitTypes.LENGTH), exponent).build(), true, UnitType.builder().componentToExponent(UnitTypes.MASS, exponent).componentToExponent(UnitTypes.LENGTH, 2 * exponent).componentToExponent(UnitTypes.TIME, -2 * exponent).build()),
                        Arguments.of(UnitType.builder().componentToExponent(UnitTypes.FORCE_WEIGHT.multiply(UnitTypes.LENGTH), exponent).build(), false, UnitType.builder().componentToExponent(UnitTypes.FORCE_WEIGHT, exponent).componentToExponent(UnitTypes.LENGTH, exponent).build())
                ));
    }
}