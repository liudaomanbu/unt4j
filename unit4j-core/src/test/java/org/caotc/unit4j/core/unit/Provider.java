package org.caotc.unit4j.core.unit;

import com.google.common.collect.Streams;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
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
}