package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
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
    static Stream<Arguments> unitAndTypes() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(Units.NON, UnitTypes.NON),
                Arguments.of(Units.METER, UnitTypes.LENGTH),
                Arguments.of(Unit.builder().componentToExponent(Units.METER, -1).build(), UnitType.builder().componentToExponent(UnitTypes.LENGTH, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(Units.METER, exponent).build(), UnitType.builder().componentToExponent(UnitTypes.LENGTH, exponent).build()),
                Arguments.of(Units.NEWTON, UnitTypes.FORCE_WEIGHT),
                Arguments.of(Unit.builder().componentToExponent(Units.NEWTON, -1).build(), UnitType.builder().componentToExponent(UnitTypes.FORCE_WEIGHT, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(Units.NEWTON, exponent).build(), UnitType.builder().componentToExponent(UnitTypes.FORCE_WEIGHT, exponent).build()),
                Arguments.of(Units.FARAD, UnitTypes.CAPACITANCE),
                Arguments.of(Unit.builder().componentToExponent(Units.FARAD, -1).build(), UnitType.builder().componentToExponent(UnitTypes.CAPACITANCE, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(Units.FARAD, exponent).build(), UnitType.builder().componentToExponent(UnitTypes.CAPACITANCE, exponent).build())
        );
    }

    static Stream<Arguments> unitAndTypeToDimensionElementMaps() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(Units.NON, ImmutableMap.of()),
                Arguments.of(Units.KILOGRAM, ImmutableMap.of(UnitTypes.MASS, Dimension.create(Units.KILOGRAM, 1))),
                Arguments.of(Unit.builder().componentToExponent(Units.KILOGRAM, -1).build(), ImmutableMap.of(UnitTypes.MASS, Dimension.create(Units.KILOGRAM, -1))),
                Arguments.of(Unit.builder().componentToExponent(Units.KILOGRAM, exponent).build(), ImmutableMap.of(UnitTypes.MASS, Dimension.create(Units.KILOGRAM, exponent))),
                Arguments.of(Units.NEWTON, ImmutableMap.of(UnitTypes.MASS, Dimension.create(Units.KILOGRAM, 1), UnitTypes.LENGTH, Dimension.create(Units.METER, 1), UnitTypes.TIME, Dimension.create(Units.SECOND, -2))),
                Arguments.of(Unit.builder().componentToExponent(Units.NEWTON, -1).build(), ImmutableMap.of(UnitTypes.FORCE_WEIGHT, Dimension.create(Units.NEWTON, -1))),
                Arguments.of(Unit.builder().componentToExponent(Units.NEWTON, exponent).build(), ImmutableMap.of(UnitTypes.FORCE_WEIGHT, Dimension.create(Units.NEWTON, exponent))),
                Arguments.of(Units.FARAD, ImmutableMap.of(UnitTypes.ELECTRIC_CHARGE, Dimension.create(Units.COULOMB, 1), UnitTypes.VOLTAGE_ELECTROMOTIVE_FORCE, Dimension.create(Units.VOLT, -1))),
                Arguments.of(Unit.builder().componentToExponent(Units.FARAD, -1).build(), ImmutableMap.of(UnitTypes.CAPACITANCE, Dimension.create(Units.FARAD, -1))),
                Arguments.of(Unit.builder().componentToExponent(Units.FARAD, exponent).build(), ImmutableMap.of(UnitTypes.CAPACITANCE, Dimension.create(Units.FARAD, exponent)))
        );
    }

    static Stream<Arguments> originalAndInverseds() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(Units.NON, Units.NON),
                Arguments.of(Units.METER, Unit.builder().componentToExponent(Units.METER, -1).build()),
                Arguments.of(Units.KILOGRAM, Unit.builder().componentToExponent(Units.KILOGRAM, -1).build()),
                Arguments.of(Units.FARAD, Unit.builder().componentToExponent(Units.FARAD, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(Units.METER, -1).build(), Units.METER),
                Arguments.of(Unit.builder().componentToExponent(Units.KILOGRAM, -1).build(), Units.KILOGRAM),
                Arguments.of(Unit.builder().componentToExponent(Units.FARAD, -1).build(), Units.FARAD),
                Arguments.of(Unit.builder().componentToExponent(Units.METER, exponent).build(), Unit.builder().componentToExponent(Units.METER, -exponent).build()),
                Arguments.of(Unit.builder().componentToExponent(Units.KILOGRAM, exponent).build(), Unit.builder().componentToExponent(Units.KILOGRAM, -exponent).build()),
                Arguments.of(Unit.builder().componentToExponent(Units.FARAD, exponent).build(), Unit.builder().componentToExponent(Units.FARAD, -exponent).build())
        );
    }

    static Stream<Arguments> originalAndExponentAndPoweds() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(Units.NON, 0, Units.NON),
                Arguments.of(Units.METER, 0, Units.NON),
                Arguments.of(Units.KILOGRAM, 0, Units.NON),
                Arguments.of(Units.FARAD, 0, Units.NON),
                Arguments.of(Units.NON, 1, Units.NON),
                Arguments.of(Units.METER, 1, Units.METER),
                Arguments.of(Units.KILOGRAM, 1, Units.KILOGRAM),
                Arguments.of(Units.FARAD, 1, Units.FARAD),
                Arguments.of(Units.NON, -1, Unit.builder().componentToExponent(Units.NON, -1).build()),
                Arguments.of(Units.METER, -1, Unit.builder().componentToExponent(Units.METER, -1).build()),
                Arguments.of(Units.KILOGRAM, -1, Unit.builder().componentToExponent(Units.KILOGRAM, -1).build()),
                Arguments.of(Units.FARAD, -1, Unit.builder().componentToExponent(Units.FARAD, -1).build()),
                Arguments.of(Units.NON, exponent, Unit.builder().componentToExponent(Units.NON, exponent).build()),
                Arguments.of(Units.METER, exponent, Unit.builder().componentToExponent(Units.METER, exponent).build()),
                Arguments.of(Units.KILOGRAM, exponent, Unit.builder().componentToExponent(Units.KILOGRAM, exponent).build()),
                Arguments.of(Units.FARAD, exponent, Unit.builder().componentToExponent(Units.FARAD, exponent).build())
        );
    }

    static Stream<Arguments> allEqualedOriginalAndSimplifieds() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(Units.NON, Units.NON),
                Arguments.of(Units.METER, Units.METER),
                Arguments.of(Units.KILOGRAM, Units.KILOGRAM),
                Arguments.of(Unit.builder().componentToExponent(Units.NON, -1).build(), Units.NON),
                Arguments.of(Unit.builder().componentToExponent(Units.METER, -1).build(), Unit.builder().componentToExponent(Units.METER, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(Units.NON, exponent).build(), Units.NON),
                Arguments.of(Unit.builder().componentToExponent(Units.METER, exponent).build(), Unit.builder().componentToExponent(Units.METER, exponent).build()),
                Arguments.of(Unit.builder().componentToExponent(Units.KILOGRAM, -1).build(), Unit.builder().componentToExponent(Units.KILOGRAM, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(Units.KILOGRAM, exponent).build(), Unit.builder().componentToExponent(Units.KILOGRAM, exponent).build())
        );
    }


    static Stream<Arguments> originalAndRecursiveAndSimplifieds() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Streams.concat(
                allEqualedOriginalAndSimplifieds()
                        .flatMap(arguments -> Stream.of(Arguments.of(arguments.get()[0], true, arguments.get()[1]),
                                Arguments.of(arguments.get()[0], false, arguments.get()[1])
                        )),
                Stream.of(
                        Arguments.of(Unit.builder().componentToExponent(Units.FARAD, -1).build(), true, Unit.builder().componentToExponent(Units.KILOGRAM, 1).componentToExponent(Units.METER, 2).componentToExponent(Units.SECOND, -4).componentToExponent(Units.AMPERE, -2).build()),
                        Arguments.of(Unit.builder().componentToExponent(Units.FARAD, -1).build(), false, Unit.builder().componentToExponent(Units.COULOMB, -1).componentToExponent(Units.VOLT, 1).build()),
                        Arguments.of(Units.FARAD, true, Unit.builder().componentToExponent(Units.KILOGRAM, -1).componentToExponent(Units.METER, -2).componentToExponent(Units.SECOND, 4).componentToExponent(Units.AMPERE, 2).build()),
                        Arguments.of(Units.FARAD, false, Unit.builder().componentToExponent(Units.WATT, -1).componentToExponent(Units.SECOND, 1).componentToExponent(Units.AMPERE, 2).build()),
                        Arguments.of(Unit.builder().componentToExponent(Units.FARAD, exponent).build(), true, Unit.builder().componentToExponent(Units.KILOGRAM, -exponent).componentToExponent(Units.METER, -2 * exponent).componentToExponent(Units.SECOND, 4 * exponent).componentToExponent(Units.AMPERE, 2 * exponent).build()),
                        Arguments.of(Unit.builder().componentToExponent(Units.FARAD, exponent).build(), false, Unit.builder().componentToExponent(Units.COULOMB, exponent).componentToExponent(Units.VOLT, -exponent).build())
                ));
    }

    static Stream<Arguments> multiplierAndMultiplicandAndResults() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(Units.NON, Units.NON, Units.NON),
                Arguments.of(Units.NON, Units.METER, Units.METER),
                Arguments.of(Units.NON, Units.KILOGRAM, Units.KILOGRAM),
                Arguments.of(Units.NON, Units.FARAD, Units.FARAD),
                Arguments.of(Units.METER, Units.METER, Unit.builder().componentToExponent(Units.METER, 2).build()),
                Arguments.of(Units.KILOGRAM, Units.KILOGRAM, Unit.builder().componentToExponent(Units.KILOGRAM, 2).build()),
                Arguments.of(Units.FARAD, Units.FARAD, Unit.builder().componentToExponent(Units.FARAD, 2).build()),
                Arguments.of(Units.METER, Unit.builder().componentToExponent(Units.METER, -1).build(), Units.NON),
                Arguments.of(Units.KILOGRAM, Unit.builder().componentToExponent(Units.KILOGRAM, -1).build(), Units.NON),
                Arguments.of(Units.FARAD, Unit.builder().componentToExponent(Units.FARAD, -1).build(), Units.NON),
                Arguments.of(Unit.builder().componentToExponent(Units.METER, exponent).build(), Unit.builder().componentToExponent(Units.METER, -exponent).build(), Units.NON),
                Arguments.of(Unit.builder().componentToExponent(Units.KILOGRAM, exponent).build(), Unit.builder().componentToExponent(Units.KILOGRAM, -exponent).build(), Units.NON),
                Arguments.of(Unit.builder().componentToExponent(Units.FARAD, exponent).build(), Unit.builder().componentToExponent(Units.FARAD, -exponent).build(), Units.NON),
                Arguments.of(Unit.builder().componentToExponent(Units.METER, Math.abs(exponent)).build(), Unit.builder().componentToExponent(Units.METER, -Math.abs(exponent) + 1).build(), Units.METER),
                Arguments.of(Unit.builder().componentToExponent(Units.KILOGRAM, Math.abs(exponent)).build(), Unit.builder().componentToExponent(Units.KILOGRAM, -Math.abs(exponent) + 1).build(), Units.KILOGRAM),
                Arguments.of(Unit.builder().componentToExponent(Units.FARAD, Math.abs(exponent)).build(), Unit.builder().componentToExponent(Units.FARAD, -Math.abs(exponent) + 1).build(), Units.FARAD),
                Arguments.of(Units.METER, Unit.builder().componentToExponent(Units.METER, Math.abs(exponent)).build(), Unit.builder().componentToExponent(Units.METER, Math.abs(exponent) + 1).build()),
                Arguments.of(Units.KILOGRAM, Unit.builder().componentToExponent(Units.KILOGRAM, Math.abs(exponent)).build(), Unit.builder().componentToExponent(Units.KILOGRAM, Math.abs(exponent) + 1).build()),
                Arguments.of(Units.FARAD, Unit.builder().componentToExponent(Units.FARAD, Math.abs(exponent)).build(), Unit.builder().componentToExponent(Units.FARAD, Math.abs(exponent) + 1).build()),
                Arguments.of(Units.METER, Units.NEWTON, Unit.builder().componentToExponent(Units.METER, 1).componentToExponent(Units.NEWTON, 1).build())
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
                Arguments.of(Units.NON, Units.NON, Units.NON),
                Arguments.of(Units.METER, Units.METER, Units.NON),
                Arguments.of(Units.KILOGRAM, Units.KILOGRAM, Units.NON),
                Arguments.of(Units.FARAD, Units.FARAD, Units.NON),
                Arguments.of(Unit.builder().componentToExponent(Units.METER, 2).build(), Units.METER, Units.METER),
                Arguments.of(Unit.builder().componentToExponent(Units.KILOGRAM, 2).build(), Units.KILOGRAM, Units.KILOGRAM),
                Arguments.of(Unit.builder().componentToExponent(Units.FARAD, 2).build(), Units.FARAD, Units.FARAD),
                Arguments.of(Units.NON, Unit.builder().componentToExponent(Units.METER, -1).build(), Units.METER),
                Arguments.of(Units.NON, Unit.builder().componentToExponent(Units.KILOGRAM, -1).build(), Units.KILOGRAM),
                Arguments.of(Units.NON, Unit.builder().componentToExponent(Units.FARAD, -1).build(), Units.FARAD),
                Arguments.of(Units.NON, Unit.builder().componentToExponent(Units.METER, -exponent).build(), Unit.builder().componentToExponent(Units.METER, exponent).build()),
                Arguments.of(Units.NON, Unit.builder().componentToExponent(Units.KILOGRAM, -exponent).build(), Unit.builder().componentToExponent(Units.KILOGRAM, exponent).build()),
                Arguments.of(Units.NON, Unit.builder().componentToExponent(Units.FARAD, -exponent).build(), Unit.builder().componentToExponent(Units.FARAD, exponent).build()),
                Arguments.of(Units.METER, Unit.builder().componentToExponent(Units.METER, -Math.abs(exponent) + 1).build(), Unit.builder().componentToExponent(Units.METER, Math.abs(exponent)).build()),
                Arguments.of(Units.KILOGRAM, Unit.builder().componentToExponent(Units.KILOGRAM, -Math.abs(exponent) + 1).build(), Unit.builder().componentToExponent(Units.KILOGRAM, Math.abs(exponent)).build()),
                Arguments.of(Units.FARAD, Unit.builder().componentToExponent(Units.FARAD, -Math.abs(exponent) + 1).build(), Unit.builder().componentToExponent(Units.FARAD, Math.abs(exponent)).build()),
                Arguments.of(Unit.builder().componentToExponent(Units.METER, Math.abs(exponent) + 1).build(), Unit.builder().componentToExponent(Units.METER, Math.abs(exponent)).build(), Units.METER),
                Arguments.of(Unit.builder().componentToExponent(Units.KILOGRAM, Math.abs(exponent) + 1).build(), Unit.builder().componentToExponent(Units.KILOGRAM, Math.abs(exponent)).build(), Units.KILOGRAM),
                Arguments.of(Unit.builder().componentToExponent(Units.FARAD, Math.abs(exponent) + 1).build(), Unit.builder().componentToExponent(Units.FARAD, Math.abs(exponent)).build(), Units.FARAD)
        );
    }

    static Stream<Arguments> divideArguments() {
        return Streams.concat(reversibleDividendAndDivisorAndQuotients()
                , reversibleDividendAndDivisorAndQuotients()
                        .filter(arguments -> !arguments.get()[1].equals(arguments.get()[2]))
                        .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[2], arguments.get()[1])),
                Stream.of(
                        Arguments.of(Unit.builder().componentToExponent(Units.METER, 1).componentToExponent(Units.NEWTON, 1).build(), Units.NEWTON, Unit.builder().componentToExponent(Unit.builder().componentToExponent(Units.METER, 1).componentToExponent(Units.NEWTON, 1).build(), 1).componentToExponent(Units.NEWTON, -1).build()),
                        Arguments.of(Unit.builder().componentToExponent(Units.METER, 1).componentToExponent(Units.NEWTON, 1).build(), Units.METER, Unit.builder().componentToExponent(Unit.builder().componentToExponent(Units.METER, 1).componentToExponent(Units.NEWTON, 1).build(), 1).componentToExponent(Units.METER, -1).build())));
    }

    static Stream<Arguments> componentAndExponentAndUnits() {
        Random random = new Random();
        int exponent = random.nextInt();
        if (exponent == 0 || exponent == 1) {
            exponent += 2;
        }
        return Stream.of(
                Arguments.of(Units.NON, 0, Units.NON),
                Arguments.of(Units.NON, 1, Units.NON),
                Arguments.of(Units.NON, -1, new CompositeStandardUnit(ImmutableMap.of(Units.NON, -1))),
                Arguments.of(Units.NON, exponent, new CompositeStandardUnit(ImmutableMap.of(Units.NON, exponent))),
                Arguments.of(Units.METER, 0, Units.NON),
                Arguments.of(Units.METER, -1, new CompositeStandardUnit(ImmutableMap.of(Units.METER, -1))),
                Arguments.of(Units.METER, exponent, new CompositeStandardUnit(ImmutableMap.of(Units.METER, exponent))),
                Arguments.of(Units.NEWTON, 0, Units.NON),
                Arguments.of(Units.NEWTON, -1, new CompositeStandardUnit(ImmutableMap.of(Units.NEWTON, -1))),
                Arguments.of(Units.NEWTON, exponent, new CompositeStandardUnit(ImmutableMap.of(Units.NEWTON, exponent)))
        );
    }

    static Stream<Arguments> componentMapAndUnits() {
        Random random = new Random();
        int exponent = random.nextInt();
        if (exponent == -1 || exponent == 0 || exponent == 1) {
            exponent += 3;
        }
        return Stream.of(
                Arguments.of(ImmutableMap.of(), Units.NON),
                Arguments.of(ImmutableMap.of(Units.METER, 1), Units.METER),
                Arguments.of(ImmutableMap.of(Units.NEWTON, 1), Units.NEWTON),
                Arguments.of(ImmutableMap.of(Units.METER, 1, new CompositeStandardUnit(ImmutableMap.of(Units.METER, -1)), 1), Units.NON),
                Arguments.of(ImmutableMap.of(Units.NEWTON, 1, new CompositeStandardUnit(ImmutableMap.of(Units.NEWTON, -1)), 1), Units.NON),
                Arguments.of(ImmutableMap.of(Units.METER, 1, new CompositeStandardUnit(ImmutableMap.of(Units.METER, exponent)), 1), new CompositeStandardUnit(ImmutableMap.of(Units.METER, exponent + 1))),
                Arguments.of(ImmutableMap.of(Units.NEWTON, 1, new CompositeStandardUnit(ImmutableMap.of(Units.NEWTON, exponent)), 1), new CompositeStandardUnit(ImmutableMap.of(Units.NEWTON, exponent + 1))),
                Arguments.of(ImmutableMap.of(Units.METER, 1, Units.NEWTON, 1), new CompositeStandardUnit(ImmutableMap.of(Units.METER, 1, Units.NEWTON, 1))),
                Arguments.of(ImmutableMap.of(Units.METER, 1, new CompositeStandardUnit(ImmutableMap.of(Units.NEWTON, -1)), 1), new CompositeStandardUnit(ImmutableMap.of(Units.METER, 1, Units.NEWTON, -1))),
                Arguments.of(ImmutableMap.of(Units.METER, 1, new CompositeStandardUnit(ImmutableMap.of(Units.NEWTON, exponent)), 1), new CompositeStandardUnit(ImmutableMap.of(Units.METER, 1, Units.NEWTON, exponent))),
                Arguments.of(ImmutableMap.of(new CompositeStandardUnit(ImmutableMap.of(Units.METER, -1)), 1, Units.NEWTON, 1), new CompositeStandardUnit(ImmutableMap.of(Units.METER, -1, Units.NEWTON, 1))),
                Arguments.of(ImmutableMap.of(new CompositeStandardUnit(ImmutableMap.of(Units.METER, exponent)), 1, Units.NEWTON, 1), new CompositeStandardUnit(ImmutableMap.of(Units.METER, exponent, Units.NEWTON, 1))),
                Arguments.of(ImmutableMap.of(new CompositeStandardUnit(ImmutableMap.of(Units.METER, -1)), 1, new CompositeStandardUnit(ImmutableMap.of(Units.NEWTON, -1)), 1), new CompositeStandardUnit(ImmutableMap.of(Units.METER, -1, Units.NEWTON, -1))),
                Arguments.of(ImmutableMap.of(new CompositeStandardUnit(ImmutableMap.of(Units.METER, exponent)), 1, new CompositeStandardUnit(ImmutableMap.of(Units.NEWTON, exponent)), 1), new CompositeStandardUnit(ImmutableMap.of(Units.METER, exponent, Units.NEWTON, exponent)))
        );
    }
}