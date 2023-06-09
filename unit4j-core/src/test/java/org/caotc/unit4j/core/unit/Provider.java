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
                Arguments.of(UnitConstant.NON, UnitTypes.NON),
                Arguments.of(UnitConstant.METER, UnitTypes.LENGTH),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.METER, -1).build(), UnitType.builder().componentToExponent(UnitTypes.LENGTH, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.METER, exponent).build(), UnitType.builder().componentToExponent(UnitTypes.LENGTH, exponent).build()),
                Arguments.of(UnitConstant.NEWTON, UnitTypes.FORCE_WEIGHT),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.NEWTON, -1).build(), UnitType.builder().componentToExponent(UnitTypes.FORCE_WEIGHT, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.NEWTON, exponent).build(), UnitType.builder().componentToExponent(UnitTypes.FORCE_WEIGHT, exponent).build()),
                Arguments.of(UnitConstant.FARAD, UnitTypes.CAPACITANCE),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.FARAD, -1).build(), UnitType.builder().componentToExponent(UnitTypes.CAPACITANCE, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.FARAD, exponent).build(), UnitType.builder().componentToExponent(UnitTypes.CAPACITANCE, exponent).build())
        );
    }

    static Stream<Arguments> unitAndTypeToDimensionElementMaps() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(UnitConstant.NON, ImmutableMap.of()),
                Arguments.of(UnitConstant.KILOGRAM, ImmutableMap.of(UnitTypes.MASS, Dimension.create(UnitConstant.KILOGRAM, 1))),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -1).build(), ImmutableMap.of(UnitTypes.MASS, Dimension.create(UnitConstant.KILOGRAM, -1))),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.KILOGRAM, exponent).build(), ImmutableMap.of(UnitTypes.MASS, Dimension.create(UnitConstant.KILOGRAM, exponent))),
                Arguments.of(UnitConstant.NEWTON, ImmutableMap.of(UnitTypes.MASS, Dimension.create(UnitConstant.KILOGRAM, 1), UnitTypes.LENGTH, Dimension.create(UnitConstant.METER, 1), UnitTypes.TIME, Dimension.create(UnitConstant.SECOND, -2))),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.NEWTON, -1).build(), ImmutableMap.of(UnitTypes.FORCE_WEIGHT, Dimension.create(UnitConstant.NEWTON, -1))),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.NEWTON, exponent).build(), ImmutableMap.of(UnitTypes.FORCE_WEIGHT, Dimension.create(UnitConstant.NEWTON, exponent))),
                Arguments.of(UnitConstant.FARAD, ImmutableMap.of(UnitTypes.ELECTRIC_CHARGE, Dimension.create(UnitConstant.COULOMB, 1), UnitTypes.VOLTAGE_ELECTROMOTIVE_FORCE, Dimension.create(UnitConstant.VOLT, -1))),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.FARAD, -1).build(), ImmutableMap.of(UnitTypes.CAPACITANCE, Dimension.create(UnitConstant.FARAD, -1))),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.FARAD, exponent).build(), ImmutableMap.of(UnitTypes.CAPACITANCE, Dimension.create(UnitConstant.FARAD, exponent)))
        );
    }

    static Stream<Arguments> originalAndInverseds() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(UnitConstant.NON, UnitConstant.NON),
                Arguments.of(UnitConstant.METER, Unit.builder().componentToExponent(UnitConstant.METER, -1).build()),
                Arguments.of(UnitConstant.KILOGRAM, Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -1).build()),
                Arguments.of(UnitConstant.FARAD, Unit.builder().componentToExponent(UnitConstant.FARAD, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.METER, -1).build(), UnitConstant.METER),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -1).build(), UnitConstant.KILOGRAM),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.FARAD, -1).build(), UnitConstant.FARAD),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.METER, exponent).build(), Unit.builder().componentToExponent(UnitConstant.METER, -exponent).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.KILOGRAM, exponent).build(), Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -exponent).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.FARAD, exponent).build(), Unit.builder().componentToExponent(UnitConstant.FARAD, -exponent).build())
        );
    }

    static Stream<Arguments> originalAndExponentAndPoweds() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(UnitConstant.NON, 0, UnitConstant.NON),
                Arguments.of(UnitConstant.METER, 0, UnitConstant.NON),
                Arguments.of(UnitConstant.KILOGRAM, 0, UnitConstant.NON),
                Arguments.of(UnitConstant.FARAD, 0, UnitConstant.NON),
                Arguments.of(UnitConstant.NON, 1, UnitConstant.NON),
                Arguments.of(UnitConstant.METER, 1, UnitConstant.METER),
                Arguments.of(UnitConstant.KILOGRAM, 1, UnitConstant.KILOGRAM),
                Arguments.of(UnitConstant.FARAD, 1, UnitConstant.FARAD),
                Arguments.of(UnitConstant.NON, -1, UnitConstant.NON),
                Arguments.of(UnitConstant.METER, -1, Unit.builder().componentToExponent(UnitConstant.METER, -1).build()),
                Arguments.of(UnitConstant.KILOGRAM, -1, Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -1).build()),
                Arguments.of(UnitConstant.FARAD, -1, Unit.builder().componentToExponent(UnitConstant.FARAD, -1).build()),
                Arguments.of(UnitConstant.NON, exponent, UnitConstant.NON),
                Arguments.of(UnitConstant.METER, exponent, Unit.builder().componentToExponent(UnitConstant.METER, exponent).build()),
                Arguments.of(UnitConstant.KILOGRAM, exponent, Unit.builder().componentToExponent(UnitConstant.KILOGRAM, exponent).build()),
                Arguments.of(UnitConstant.FARAD, exponent, Unit.builder().componentToExponent(UnitConstant.FARAD, exponent).build())
        );
    }

    static Stream<Arguments> allEqualedOriginalAndSimplifieds() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(UnitConstant.NON, UnitConstant.NON),
                Arguments.of(UnitConstant.METER, UnitConstant.METER),
                Arguments.of(UnitConstant.KILOGRAM, UnitConstant.KILOGRAM),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.NON, -1).build(), UnitConstant.NON),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.METER, -1).build(), Unit.builder().componentToExponent(UnitConstant.METER, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.NON, exponent).build(), UnitConstant.NON),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.METER, exponent).build(), Unit.builder().componentToExponent(UnitConstant.METER, exponent).build())
        );
    }

    static Stream<Arguments> recursiveEqualedOriginalAndPrefixUnitAndSimplifieds() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -1).build(), true, Unit.builder().componentToExponent(Unit.builder().prefix(Prefix.KILO.pow(-1)).componentToExponent(UnitConstant.GRAM, -1).build(), 1).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -1).build(), false, Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.KILOGRAM, exponent).build(), true, Unit.builder().prefix(Prefix.KILO.pow(exponent)).componentToExponent(UnitConstant.GRAM, exponent).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.KILOGRAM, exponent).build(), false, Unit.builder().componentToExponent(UnitConstant.KILOGRAM, exponent).build())
        );
    }

    static Stream<Arguments> prefixUnitEqualedOriginalAndRecursiveAndSimplifieds() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.FARAD, -1).build(), true, Unit.builder().componentToExponent(UnitConstant.KILOGRAM, 1).componentToExponent(UnitConstant.METER, 2).componentToExponent(UnitConstant.SECOND, -4).componentToExponent(UnitConstant.AMPERE, -2).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.FARAD, -1).build(), false, Unit.builder().componentToExponent(UnitConstant.COULOMB, -1).componentToExponent(UnitConstant.VOLT, 1).build())
        );
    }

    static Stream<Arguments> originalAndSimplifyConfigAndSimplifieds() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Streams.concat(
                allEqualedOriginalAndSimplifieds()
                        .flatMap(arguments -> Stream.of(Arguments.of(arguments.get()[0], new SimplifyConfig(true, true, true, true), arguments.get()[1]),
                                Arguments.of(arguments.get()[0], new SimplifyConfig(true, true, true, false), arguments.get()[1]),
                                Arguments.of(arguments.get()[0], new SimplifyConfig(true, true, false, true), arguments.get()[1]),
                                Arguments.of(arguments.get()[0], new SimplifyConfig(true, true, false, false), arguments.get()[1])
                        )),
                recursiveEqualedOriginalAndPrefixUnitAndSimplifieds()
                        .flatMap(arguments -> Stream.of(Arguments.of(arguments.get()[0], new SimplifyConfig(true, true, (Boolean) arguments.get()[1], true), arguments.get()[2]),
                                Arguments.of(arguments.get()[0], new SimplifyConfig(true, true, (Boolean) arguments.get()[1], false), arguments.get()[2])
                        )),
                prefixUnitEqualedOriginalAndRecursiveAndSimplifieds()
                        .flatMap(arguments -> Stream.of(Arguments.of(arguments.get()[0], new SimplifyConfig(true, true, true, (Boolean) arguments.get()[1]), arguments.get()[2]),
                                Arguments.of(arguments.get()[0], new SimplifyConfig(true, true, false, (Boolean) arguments.get()[1]), arguments.get()[2])
                        )),
                Stream.of(
                        Arguments.of(UnitConstant.FARAD, new SimplifyConfig(true, true, true, true), Unit.builder().componentToExponent(Unit.builder().prefix(Prefix.KILO.pow(-1)).componentToExponent(UnitConstant.GRAM, -1).build(), 1).componentToExponent(UnitConstant.METER, -2).componentToExponent(UnitConstant.SECOND, 4).componentToExponent(UnitConstant.AMPERE, 2).build()),
                        Arguments.of(UnitConstant.FARAD, new SimplifyConfig(true, true, true, false), Unit.builder().componentToExponent(UnitConstant.WATT, -1).componentToExponent(UnitConstant.SECOND, 1).componentToExponent(UnitConstant.AMPERE, 2).build()),
                        Arguments.of(UnitConstant.FARAD, new SimplifyConfig(true, true, false, true), Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -1).componentToExponent(UnitConstant.METER, -2).componentToExponent(UnitConstant.SECOND, 4).componentToExponent(UnitConstant.AMPERE, 2).build()),
                        Arguments.of(UnitConstant.FARAD, new SimplifyConfig(true, true, false, false), Unit.builder().componentToExponent(UnitConstant.WATT, -1).componentToExponent(UnitConstant.SECOND, 1).componentToExponent(UnitConstant.AMPERE, 2).build()),
                        Arguments.of(Unit.builder().componentToExponent(UnitConstant.FARAD, exponent).build(), new SimplifyConfig(true, true, true, true), Unit.builder().componentToExponent(Unit.builder().prefix(Prefix.KILO.pow(-exponent)).componentToExponent(UnitConstant.GRAM, -exponent).build(), 1).componentToExponent(UnitConstant.METER, -2 * exponent).componentToExponent(UnitConstant.SECOND, 4 * exponent).componentToExponent(UnitConstant.AMPERE, 2 * exponent).build()),
                        Arguments.of(Unit.builder().componentToExponent(UnitConstant.FARAD, exponent).build(), new SimplifyConfig(true, true, true, false), Unit.builder().componentToExponent(UnitConstant.COULOMB, exponent).componentToExponent(UnitConstant.VOLT, -exponent).build()),
                        Arguments.of(Unit.builder().componentToExponent(UnitConstant.FARAD, exponent).build(), new SimplifyConfig(true, true, false, true), Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -exponent).componentToExponent(UnitConstant.METER, -2 * exponent).componentToExponent(UnitConstant.SECOND, 4 * exponent).componentToExponent(UnitConstant.AMPERE, 2 * exponent).build()),
                        Arguments.of(Unit.builder().componentToExponent(UnitConstant.FARAD, exponent).build(), new SimplifyConfig(true, true, false, false), Unit.builder().componentToExponent(UnitConstant.COULOMB, exponent).componentToExponent(UnitConstant.VOLT, -exponent).build())
                ));
    }

    static Stream<Arguments> multiplierAndMultiplicandAndResults() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(UnitConstant.NON, UnitConstant.NON, UnitConstant.NON),
                Arguments.of(UnitConstant.NON, UnitConstant.METER, UnitConstant.METER),
                Arguments.of(UnitConstant.NON, UnitConstant.KILOGRAM, UnitConstant.KILOGRAM),
                Arguments.of(UnitConstant.NON, UnitConstant.FARAD, UnitConstant.FARAD),
                Arguments.of(UnitConstant.METER, UnitConstant.METER, Unit.builder().componentToExponent(UnitConstant.METER, 2).build()),
                Arguments.of(UnitConstant.KILOGRAM, UnitConstant.KILOGRAM, Unit.builder().componentToExponent(UnitConstant.KILOGRAM, 2).build()),
                Arguments.of(UnitConstant.FARAD, UnitConstant.FARAD, Unit.builder().componentToExponent(UnitConstant.FARAD, 2).build()),
                Arguments.of(UnitConstant.METER, Unit.builder().componentToExponent(UnitConstant.METER, -1).build(), UnitConstant.NON),
                Arguments.of(UnitConstant.KILOGRAM, Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -1).build(), UnitConstant.NON),
                Arguments.of(UnitConstant.FARAD, Unit.builder().componentToExponent(UnitConstant.FARAD, -1).build(), UnitConstant.NON),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.METER, exponent).build(), Unit.builder().componentToExponent(UnitConstant.METER, -exponent).build(), UnitConstant.NON),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.KILOGRAM, exponent).build(), Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -exponent).build(), UnitConstant.NON),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.FARAD, exponent).build(), Unit.builder().componentToExponent(UnitConstant.FARAD, -exponent).build(), UnitConstant.NON),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.METER, Math.abs(exponent)).build(), Unit.builder().componentToExponent(UnitConstant.METER, -Math.abs(exponent) + 1).build(), UnitConstant.METER),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.KILOGRAM, Math.abs(exponent)).build(), Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -Math.abs(exponent) + 1).build(), UnitConstant.KILOGRAM),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.FARAD, Math.abs(exponent)).build(), Unit.builder().componentToExponent(UnitConstant.FARAD, -Math.abs(exponent) + 1).build(), UnitConstant.FARAD),
                Arguments.of(UnitConstant.METER, Unit.builder().componentToExponent(UnitConstant.METER, Math.abs(exponent)).build(), Unit.builder().componentToExponent(UnitConstant.METER, Math.abs(exponent) + 1).build()),
                Arguments.of(UnitConstant.KILOGRAM, Unit.builder().componentToExponent(UnitConstant.KILOGRAM, Math.abs(exponent)).build(), Unit.builder().componentToExponent(UnitConstant.KILOGRAM, Math.abs(exponent) + 1).build()),
                Arguments.of(UnitConstant.FARAD, Unit.builder().componentToExponent(UnitConstant.FARAD, Math.abs(exponent)).build(), Unit.builder().componentToExponent(UnitConstant.FARAD, Math.abs(exponent) + 1).build()),
                Arguments.of(UnitConstant.METER, UnitConstant.NEWTON, Unit.builder().componentToExponent(UnitConstant.METER, 1).componentToExponent(UnitConstant.NEWTON, 1).build())
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
                Arguments.of(UnitConstant.NON, UnitConstant.NON, UnitConstant.NON),
                Arguments.of(UnitConstant.METER, UnitConstant.METER, UnitConstant.NON),
                Arguments.of(UnitConstant.KILOGRAM, UnitConstant.KILOGRAM, UnitConstant.NON),
                Arguments.of(UnitConstant.FARAD, UnitConstant.FARAD, UnitConstant.NON),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.METER, 2).build(), UnitConstant.METER, UnitConstant.METER),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.KILOGRAM, 2).build(), UnitConstant.KILOGRAM, UnitConstant.KILOGRAM),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.FARAD, 2).build(), UnitConstant.FARAD, UnitConstant.FARAD),
                Arguments.of(UnitConstant.NON, Unit.builder().componentToExponent(UnitConstant.METER, -1).build(), UnitConstant.METER),
                Arguments.of(UnitConstant.NON, Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -1).build(), UnitConstant.KILOGRAM),
                Arguments.of(UnitConstant.NON, Unit.builder().componentToExponent(UnitConstant.FARAD, -1).build(), UnitConstant.FARAD),
                Arguments.of(UnitConstant.NON, Unit.builder().componentToExponent(UnitConstant.METER, -exponent).build(), Unit.builder().componentToExponent(UnitConstant.METER, exponent).build()),
                Arguments.of(UnitConstant.NON, Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -exponent).build(), Unit.builder().componentToExponent(UnitConstant.KILOGRAM, exponent).build()),
                Arguments.of(UnitConstant.NON, Unit.builder().componentToExponent(UnitConstant.FARAD, -exponent).build(), Unit.builder().componentToExponent(UnitConstant.FARAD, exponent).build()),
                Arguments.of(UnitConstant.METER, Unit.builder().componentToExponent(UnitConstant.METER, -Math.abs(exponent) + 1).build(), Unit.builder().componentToExponent(UnitConstant.METER, Math.abs(exponent)).build()),
                Arguments.of(UnitConstant.KILOGRAM, Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -Math.abs(exponent) + 1).build(), Unit.builder().componentToExponent(UnitConstant.KILOGRAM, Math.abs(exponent)).build()),
                Arguments.of(UnitConstant.FARAD, Unit.builder().componentToExponent(UnitConstant.FARAD, -Math.abs(exponent) + 1).build(), Unit.builder().componentToExponent(UnitConstant.FARAD, Math.abs(exponent)).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.METER, Math.abs(exponent) + 1).build(), Unit.builder().componentToExponent(UnitConstant.METER, Math.abs(exponent)).build(), UnitConstant.METER),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.KILOGRAM, Math.abs(exponent) + 1).build(), Unit.builder().componentToExponent(UnitConstant.KILOGRAM, Math.abs(exponent)).build(), UnitConstant.KILOGRAM),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.FARAD, Math.abs(exponent) + 1).build(), Unit.builder().componentToExponent(UnitConstant.FARAD, Math.abs(exponent)).build(), UnitConstant.FARAD)
        );
    }

    static Stream<Arguments> divideArguments() {
        return Streams.concat(reversibleDividendAndDivisorAndQuotients()
                , reversibleDividendAndDivisorAndQuotients()
                        .filter(arguments -> !arguments.get()[1].equals(arguments.get()[2]))
                        .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[2], arguments.get()[1])),
                Stream.of(
                        Arguments.of(Unit.builder().componentToExponent(UnitConstant.METER, 1).componentToExponent(UnitConstant.NEWTON, 1).build(), UnitConstant.NEWTON, Unit.builder().componentToExponent(Unit.builder().componentToExponent(UnitConstant.METER, 1).componentToExponent(UnitConstant.NEWTON, 1).build(), 1).componentToExponent(UnitConstant.NEWTON, -1).build()),
                        Arguments.of(Unit.builder().componentToExponent(UnitConstant.METER, 1).componentToExponent(UnitConstant.NEWTON, 1).build(), UnitConstant.METER, Unit.builder().componentToExponent(Unit.builder().componentToExponent(UnitConstant.METER, 1).componentToExponent(UnitConstant.NEWTON, 1).build(), 1).componentToExponent(UnitConstant.METER, -1).build())));
    }

    static Stream<Arguments> componentAndExponentAndUnits() {
        Random random = new Random();
        int exponent = random.nextInt();
        if (exponent == 0 || exponent == 1) {
            exponent += 2;
        }
        return Stream.of(
                Arguments.of(UnitConstant.NON, 0, UnitConstant.NON),
                Arguments.of(UnitConstant.NON, 1, UnitConstant.NON),
                Arguments.of(UnitConstant.NON, -1, UnitConstant.NON),
                Arguments.of(UnitConstant.NON, random.nextInt(), UnitConstant.NON),
                Arguments.of(UnitConstant.METER, 0, UnitConstant.NON),
                Arguments.of(UnitConstant.METER, -1, new CompositeStandardUnit(ImmutableMap.of(UnitConstant.METER, -1))),
                Arguments.of(UnitConstant.METER, exponent, new CompositeStandardUnit(ImmutableMap.of(UnitConstant.METER, exponent))),
                Arguments.of(UnitConstant.NEWTON, 0, UnitConstant.NON),
                Arguments.of(UnitConstant.NEWTON, -1, new CompositeStandardUnit(ImmutableMap.of(UnitConstant.NEWTON, -1))),
                Arguments.of(UnitConstant.NEWTON, exponent, new CompositeStandardUnit(ImmutableMap.of(UnitConstant.NEWTON, exponent)))
        );
    }

    static Stream<Arguments> componentMapAndUnits() {
        Random random = new Random();
        int exponent = random.nextInt();
        if (exponent == -1 || exponent == 0 || exponent == 1) {
            exponent += 3;
        }
        return Stream.of(
                Arguments.of(ImmutableMap.of(), UnitConstant.NON),
                Arguments.of(ImmutableMap.of(UnitConstant.METER, 1), UnitConstant.METER),
                Arguments.of(ImmutableMap.of(UnitConstant.NEWTON, 1), UnitConstant.NEWTON),
                Arguments.of(ImmutableMap.of(UnitConstant.METER, 1, new CompositeStandardUnit(ImmutableMap.of(UnitConstant.METER, -1)), 1), UnitConstant.NON),
                Arguments.of(ImmutableMap.of(UnitConstant.NEWTON, 1, new CompositeStandardUnit(ImmutableMap.of(UnitConstant.NEWTON, -1)), 1), UnitConstant.NON),
                Arguments.of(ImmutableMap.of(UnitConstant.METER, 1, new CompositeStandardUnit(ImmutableMap.of(UnitConstant.METER, exponent)), 1), new CompositeStandardUnit(ImmutableMap.of(UnitConstant.METER, exponent + 1))),
                Arguments.of(ImmutableMap.of(UnitConstant.NEWTON, 1, new CompositeStandardUnit(ImmutableMap.of(UnitConstant.NEWTON, exponent)), 1), new CompositeStandardUnit(ImmutableMap.of(UnitConstant.NEWTON, exponent + 1))),
                Arguments.of(ImmutableMap.of(UnitConstant.METER, 1, UnitConstant.NEWTON, 1), new CompositeStandardUnit(ImmutableMap.of(UnitConstant.METER, 1, UnitConstant.NEWTON, 1))),
                Arguments.of(ImmutableMap.of(UnitConstant.METER, 1, new CompositeStandardUnit(ImmutableMap.of(UnitConstant.NEWTON, -1)), 1), new CompositeStandardUnit(ImmutableMap.of(UnitConstant.METER, 1, UnitConstant.NEWTON, -1))),
                Arguments.of(ImmutableMap.of(UnitConstant.METER, 1, new CompositeStandardUnit(ImmutableMap.of(UnitConstant.NEWTON, exponent)), 1), new CompositeStandardUnit(ImmutableMap.of(UnitConstant.METER, 1, UnitConstant.NEWTON, exponent))),
                Arguments.of(ImmutableMap.of(new CompositeStandardUnit(ImmutableMap.of(UnitConstant.METER, -1)), 1, UnitConstant.NEWTON, 1), new CompositeStandardUnit(ImmutableMap.of(UnitConstant.METER, -1, UnitConstant.NEWTON, 1))),
                Arguments.of(ImmutableMap.of(new CompositeStandardUnit(ImmutableMap.of(UnitConstant.METER, exponent)), 1, UnitConstant.NEWTON, 1), new CompositeStandardUnit(ImmutableMap.of(UnitConstant.METER, exponent, UnitConstant.NEWTON, 1))),
                Arguments.of(ImmutableMap.of(new CompositeStandardUnit(ImmutableMap.of(UnitConstant.METER, -1)), 1, new CompositeStandardUnit(ImmutableMap.of(UnitConstant.NEWTON, -1)), 1), new CompositeStandardUnit(ImmutableMap.of(UnitConstant.METER, -1, UnitConstant.NEWTON, -1))),
                Arguments.of(ImmutableMap.of(new CompositeStandardUnit(ImmutableMap.of(UnitConstant.METER, exponent)), 1, new CompositeStandardUnit(ImmutableMap.of(UnitConstant.NEWTON, exponent)), 1), new CompositeStandardUnit(ImmutableMap.of(UnitConstant.METER, exponent, UnitConstant.NEWTON, exponent)))
        );
    }
}