package org.caotc.unit4j.core.unit;

import com.google.common.collect.Maps;
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
    static Stream<Arguments> recursiveEqualedOriginalAndSimplifieds() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.of(
                Arguments.of(UnitConstant.NON, UnitConstant.NON),
                Arguments.of(UnitConstant.METER, UnitConstant.METER),
                Arguments.of(UnitConstant.KILOGRAM, UnitConstant.KILOGRAM),
                Arguments.of(UnitConstant.JOULE, UnitConstant.JOULE),
                Arguments.of(UnitConstant.NEWTON.multiply(UnitConstant.METER), Unit.builder().componentToExponent(UnitConstant.KILOGRAM, 1).componentToExponent(UnitConstant.METER, 2).componentToExponent(UnitConstant.SECOND, -2).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.METER, -1).build(), Unit.builder().componentToExponent(UnitConstant.METER, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -1).build(), Unit.builder().prefix(Prefix.KILO.power(-1)).componentToExponent(UnitConstant.GRAM, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.JOULE, -1).build(), Unit.builder().componentToExponents(Maps.transformValues(UnitConstant.JOULE.componentToExponents(), value -> -value)).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.METER, exponent).build(), Unit.builder().componentToExponent(UnitConstant.METER, exponent).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.KILOGRAM, exponent).build(), Unit.builder().prefix(Prefix.KILO.power(exponent)).componentToExponent(UnitConstant.GRAM, exponent).build()),
                Arguments.of(Unit.builder().componentToExponent(UnitConstant.JOULE, exponent).build(), Unit.builder().componentToExponents(Maps.transformValues(UnitConstant.JOULE.componentToExponents(), value -> value * exponent)).build())
        );
    }

    static Stream<Arguments> originalAndRecursiveAndSimplifieds() {
        Random random = new Random();
        int exponent = random.nextInt();
        return Stream.concat(recursiveEqualedOriginalAndSimplifieds()
                        .flatMap(arguments -> Stream.of(Arguments.of(arguments.get()[0], true, arguments.get()[1]), Arguments.of(arguments.get()[0], false, arguments.get()[1]))),
                Stream.of(
                        Arguments.of(Unit.builder().componentToExponent(Unit.builder().componentToExponent(UnitConstant.NEWTON, 1).componentToExponent(UnitConstant.METER, 1).build(), -1).build(), true, Unit.builder().componentToExponent(UnitConstant.KILOGRAM, -1).componentToExponent(UnitConstant.METER, -2).componentToExponent(UnitConstant.SECOND, 2).build()),
                        Arguments.of(Unit.builder().componentToExponent(Unit.builder().componentToExponent(UnitConstant.NEWTON, 1).componentToExponent(UnitConstant.METER, 1).build(), -1).build(), false, Unit.builder().componentToExponent(UnitConstant.NEWTON, -1).componentToExponent(UnitConstant.METER, -1).build()),
                        Arguments.of(Unit.builder().componentToExponent(Unit.builder().componentToExponent(UnitConstant.NEWTON, 1).componentToExponent(UnitConstant.METER, 1).build(), exponent).build(), true, Unit.builder().componentToExponent(UnitConstant.KILOGRAM, exponent).componentToExponent(UnitConstant.METER, 2 * exponent).componentToExponent(UnitConstant.SECOND, -2 * exponent).build()),
                        Arguments.of(Unit.builder().componentToExponent(Unit.builder().componentToExponent(UnitConstant.NEWTON, 1).componentToExponent(UnitConstant.METER, 1).build(), exponent).build(), false, Unit.builder().componentToExponent(UnitConstant.NEWTON, exponent).componentToExponent(UnitConstant.METER, exponent).build())
                ));
    }
}