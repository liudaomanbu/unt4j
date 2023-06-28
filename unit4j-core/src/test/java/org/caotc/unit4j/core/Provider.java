package org.caotc.unit4j.core;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Streams;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.convert.UnitConvertConfig;
import org.caotc.unit4j.core.math.number.BigDecimal;
import org.caotc.unit4j.core.unit.Prefix;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.caotc.unit4j.core.unit.Units;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2022-08-18
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class Provider {
    static Stream<Arguments> idAndUnits() {
        return Stream.of(
                Arguments.of("", Units.NON),
                Arguments.of("10³_", Units.NON.addPrefix(Prefix.KILO)),
                Arguments.of("METER", Units.METER),
                Arguments.of("10³_METER", Units.METER.addPrefix(Prefix.KILO)),
                Arguments.of("GRAM", Units.GRAM),
                Arguments.of("10³_GRAM", Units.KILOGRAM),
                Arguments.of("(10³_GRAM)¹(METER)¹(SECOND)⁻²", Units.NEWTON),
                Arguments.of("10³_(10³_GRAM)¹(METER)¹(SECOND)⁻²", Units.NEWTON.addPrefix(Prefix.KILO)),
                Arguments.of("((SECOND)¹(AMPERE)¹)¹(((((10³_GRAM)¹(METER)¹(SECOND)⁻²)¹(METER)¹)¹(SECOND)⁻¹)¹(AMPERE)⁻¹)⁻¹", Units.FARAD),
                Arguments.of("10³_((SECOND)¹(AMPERE)¹)¹(((((10³_GRAM)¹(METER)¹(SECOND)⁻²)¹(METER)¹)¹(SECOND)⁻¹)¹(AMPERE)⁻¹)⁻¹", Units.FARAD.addPrefix(Prefix.KILO))
        );
    }

    static Stream<String> errorUnitIds() {
        return idAndUnits().map(arguments -> (String) arguments.get()[0])
                .map(id -> id + Math.random());
    }

    static Stream<Arguments> unitTypeAndAliasSets() {
        return Stream.of(
                Arguments.of(UnitTypes.LENGTH, ImmutableSet.of(Alias.create(Aliases.Types.ENGLISH_NAME, "LENGTH"), Alias.create(Aliases.Types.CHINESE_NAME, "长度"))),
                Arguments.of(UnitTypes.MASS, ImmutableSet.of(Alias.create(Aliases.Types.ENGLISH_NAME, "MASS"), Alias.create(Aliases.Types.CHINESE_NAME, "质量")))
        );
    }

    static Stream<Arguments> prefixAndAliasSets() {
        return Stream.of(
                Arguments.of(Prefix.KILO, ImmutableSet.of(Alias.create(Aliases.Types.ENGLISH_NAME, "KILO")))
        );
    }

    static Stream<Arguments> unitAndAliasSets() {
        return Stream.of(
                Arguments.of(Units.GRAM, ImmutableSet.of(Alias.create(Aliases.Types.ENGLISH_NAME, "GRAM"), Alias.create(Aliases.Types.CHINESE_NAME, "克"), Alias.create(Aliases.Types.SYMBOL, "g"))),
                Arguments.of(Units.KILOGRAM, ImmutableSet.of(Alias.create(Aliases.Types.ENGLISH_NAME, "KILO_GRAM"))),
                Arguments.of(Units.NEWTON, ImmutableSet.of(Alias.create(Aliases.Types.ENGLISH_NAME, "NEWTON"), Alias.create(Aliases.Types.SYMBOL, "N"))),
                Arguments.of(Units.NEWTON.addPrefix(Prefix.KILO), ImmutableSet.of(Alias.create(Aliases.Types.ENGLISH_NAME, "KILO_NEWTON")))
        );
    }

    @SuppressWarnings("unchecked")
    static Stream<Arguments> unitTypeAndAliasTypeAndAliasSets() {
        return unitTypeAndAliasSets().flatMap(arguments -> ((Set<Alias>) arguments.get()[1]).stream().collect(ImmutableSetMultimap.toImmutableSetMultimap(Alias::type, Function.identity()))
                .asMap().entrySet().stream()
                .map(entry -> Arguments.of(arguments.get()[0], entry.getKey(), entry.getValue())));
    }

    @SuppressWarnings("unchecked")
    static Stream<Arguments> prefixAndAliasTypeAndAliasSets() {
        return prefixAndAliasSets().flatMap(arguments -> ((Set<Alias>) arguments.get()[1]).stream().collect(ImmutableSetMultimap.toImmutableSetMultimap(Alias::type, Function.identity()))
                .asMap().entrySet().stream()
                .map(entry -> Arguments.of(arguments.get()[0], entry.getKey(), entry.getValue())));
    }

    @SuppressWarnings("unchecked")
    static Stream<Arguments> unitAndAliasTypeAndAliasSets() {
        return unitAndAliasSets().flatMap(arguments -> ((Set<Alias>) arguments.get()[1]).stream().collect(ImmutableSetMultimap.toImmutableSetMultimap(Alias::type, Function.identity()))
                .asMap().entrySet().stream()
                .map(entry -> Arguments.of(arguments.get()[0], entry.getKey(), entry.getValue())));
    }

    @SuppressWarnings("unchecked")
    static Stream<Arguments> aliasAndUnitTypes() {
        return unitTypeAndAliasSets().flatMap(arguments -> ((Set<Alias>) arguments.get()[1]).stream()
                .map(alias -> Arguments.of(alias, arguments.get()[0])));
    }

    static Stream<Alias> errorUnitTypeAliases() {
        return aliasAndUnitTypes().map(arguments -> (Alias) arguments.get()[0])
                .flatMap(alias -> Stream.of(alias.withValue(alias.value() + Math.random()),
                        alias.withType(Alias.Type.create(alias.type().name() + Math.random()))));
    }

    @SuppressWarnings("unchecked")
    static Stream<Arguments> aliasAndPrefixes() {
        return prefixAndAliasSets().flatMap(arguments -> ((Set<Alias>) arguments.get()[1]).stream()
                .map(alias -> Arguments.of(alias, arguments.get()[0])));
    }

    static Stream<Alias> errorPrefixAliases() {
        return aliasAndPrefixes().map(arguments -> (Alias) arguments.get()[0])
                .flatMap(alias -> Stream.of(alias.withValue(alias.value() + Math.random()),
                        alias.withType(Alias.Type.create(alias.type().name() + Math.random()))));
    }

    @SuppressWarnings("unchecked")
    static Stream<Arguments> aliasAndUnits() {
        return unitAndAliasSets().flatMap(arguments -> ((Set<Alias>) arguments.get()[1]).stream()
                .map(alias -> Arguments.of(alias, arguments.get()[0])));
    }

    static Stream<Alias> errorUnitAliases() {
        return aliasAndUnits().map(arguments -> (Alias) arguments.get()[0])
                .flatMap(alias -> Stream.of(alias.withValue(alias.value() + Math.random()),
                        alias.withType(Alias.Type.create(alias.type().name() + Math.random()))));
    }

    @SuppressWarnings("unchecked")
    static Stream<Arguments> aliasStringAndUnitTypes() {
        return unitTypeAndAliasSets().flatMap(arguments -> ((Set<Alias>) arguments.get()[1]).stream()
                .map(Alias::value)
                .distinct()
                .map(alias -> Arguments.of(alias, arguments.get()[0])));
    }

    @SuppressWarnings("unchecked")
    static Stream<Arguments> aliasStringAndPrefixes() {
        return prefixAndAliasSets().flatMap(arguments -> ((Set<Alias>) arguments.get()[1]).stream()
                .map(Alias::value)
                .distinct()
                .map(alias -> Arguments.of(alias, arguments.get()[0])));
    }

    @SuppressWarnings("unchecked")
    static Stream<Arguments> aliasStringAndUnits() {
        return unitAndAliasSets().flatMap(arguments -> ((Set<Alias>) arguments.get()[1]).stream()
                .map(Alias::value)
                .distinct()
                .map(alias -> Arguments.of(alias, arguments.get()[0])));
    }

    static Stream<Arguments> sourceUnitAndTargetUnitAndUnitConvertConfigs() {
        Random random = new Random();
        int exponent = random.nextInt(100) + 1;
        Unit tonneNewtonUnit = Unit.builder().componentToExponent(Units.TONNE, 1).componentToExponent(Units.METER, 1).componentToExponent(Units.SECOND, -2).build();
        Unit gramNewtonUnit = Unit.builder().componentToExponent(Units.GRAM, 1).componentToExponent(Units.METER, 1).componentToExponent(Units.SECOND, -2).build();
        Unit gramMinuteNewtonUnit = Unit.builder().componentToExponent(Units.GRAM, 1).componentToExponent(Units.METER, 1).componentToExponent(Units.MINUTE, -2).build();
        return Stream.of(
                Arguments.of(Units.KILOGRAM, Units.GRAM, UnitConvertConfig.create(BigDecimal.valueOf(1000))),
                Arguments.of(Units.KILOGRAM, Units.GRAM.addPrefix(Prefix.DECA), UnitConvertConfig.create(BigDecimal.valueOf(100))),
                Arguments.of(Units.TONNE, Units.GRAM, UnitConvertConfig.create(BigDecimal.valueOf(1000000))),
                Arguments.of(Units.TONNE, Units.KILOGRAM, UnitConvertConfig.create(BigDecimal.valueOf(1000))),
                Arguments.of(Units.NEWTON.addPrefix(Prefix.KILO), Units.NEWTON, UnitConvertConfig.create(BigDecimal.valueOf(1000))),
                Arguments.of(Units.NEWTON.addPrefix(Prefix.KILO), Units.NEWTON.addPrefix(Prefix.DECA), UnitConvertConfig.create(BigDecimal.valueOf(100))),
                Arguments.of(Units.NEWTON, gramNewtonUnit, UnitConvertConfig.create(BigDecimal.valueOf(1000))),
                Arguments.of(gramMinuteNewtonUnit, Units.NEWTON, UnitConvertConfig.create(BigDecimal.valueOf(0.001).multiply(BigDecimal.valueOf(60).pow(-2)))),
                Arguments.of(tonneNewtonUnit, Units.NEWTON, UnitConvertConfig.create(BigDecimal.valueOf(1000))),
                Arguments.of(Units.NON.addPrefix(Prefix.KILO), Units.NON, UnitConvertConfig.create(BigDecimal.valueOf(1000))),
                Arguments.of(Units.NON.addPrefix(Prefix.KILO), Units.NON.addPrefix(Prefix.DECA), UnitConvertConfig.create(BigDecimal.valueOf(100))),
                Arguments.of(Unit.builder().componentToExponent(Units.KILOGRAM, 2).build(), Unit.builder().componentToExponent(Units.GRAM, 2).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(2))),
                Arguments.of(Unit.builder().componentToExponent(Units.KILOGRAM, 2).build(), Unit.builder().componentToExponent(Units.GRAM.addPrefix(Prefix.DECA), 2).build(), UnitConvertConfig.create(BigDecimal.valueOf(100).pow(2))),
                Arguments.of(Unit.builder().componentToExponent(Units.TONNE, 2).build(), Unit.builder().componentToExponent(Units.GRAM, 2).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000000).pow(2))),
                Arguments.of(Unit.builder().componentToExponent(Units.TONNE, 2).build(), Unit.builder().componentToExponent(Units.KILOGRAM, 2).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(2))),
                Arguments.of(Unit.builder().componentToExponent(Units.NEWTON.addPrefix(Prefix.KILO), 2).build(), Unit.builder().componentToExponent(Units.NEWTON, 2).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(2))),
                Arguments.of(Unit.builder().componentToExponent(Units.NEWTON.addPrefix(Prefix.KILO), 2).build(), Unit.builder().componentToExponent(Units.NEWTON.addPrefix(Prefix.DECA), 2).build(), UnitConvertConfig.create(BigDecimal.valueOf(100).pow(2))),
                Arguments.of(Unit.builder().componentToExponent(Units.NEWTON, 2).build(), Unit.builder().componentToExponent(gramNewtonUnit, 2).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(2))),
                Arguments.of(Unit.builder().componentToExponent(gramMinuteNewtonUnit, 2).build(), Unit.builder().componentToExponent(Units.NEWTON, 2).build(), UnitConvertConfig.create(BigDecimal.valueOf(0.001).multiply(BigDecimal.valueOf(60).pow(-2)).pow(2))),
                Arguments.of(Unit.builder().componentToExponent(tonneNewtonUnit, 2).build(), Unit.builder().componentToExponent(Units.NEWTON, 2).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(2))),
                Arguments.of(Unit.builder().componentToExponent(Units.NON.addPrefix(Prefix.KILO), 2).build(), Unit.builder().componentToExponent(Units.NON, 2).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(2))),
                Arguments.of(Unit.builder().componentToExponent(Units.NON.addPrefix(Prefix.KILO), 2).build(), Unit.builder().componentToExponent(Units.NON.addPrefix(Prefix.DECA), 2).build(), UnitConvertConfig.create(BigDecimal.valueOf(100).pow(2))),
                Arguments.of(Unit.builder().componentToExponent(Units.KILOGRAM, -1).build(), Unit.builder().componentToExponent(Units.GRAM, -1).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(-1))),
                Arguments.of(Unit.builder().componentToExponent(Units.KILOGRAM, -1).build(), Unit.builder().componentToExponent(Units.GRAM.addPrefix(Prefix.DECA), -1).build(), UnitConvertConfig.create(BigDecimal.valueOf(100).pow(-1))),
                Arguments.of(Unit.builder().componentToExponent(Units.TONNE, -1).build(), Unit.builder().componentToExponent(Units.GRAM, -1).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000000).pow(-1))),
                Arguments.of(Unit.builder().componentToExponent(Units.TONNE, -1).build(), Unit.builder().componentToExponent(Units.KILOGRAM, -1).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(-1))),
                Arguments.of(Unit.builder().componentToExponent(Units.NEWTON.addPrefix(Prefix.KILO), -1).build(), Unit.builder().componentToExponent(Units.NEWTON, -1).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(-1))),
                Arguments.of(Unit.builder().componentToExponent(Units.NEWTON.addPrefix(Prefix.KILO), -1).build(), Unit.builder().componentToExponent(Units.NEWTON.addPrefix(Prefix.DECA), -1).build(), UnitConvertConfig.create(BigDecimal.valueOf(100).pow(-1))),
                Arguments.of(Unit.builder().componentToExponent(Units.NEWTON, -1).build(), Unit.builder().componentToExponent(gramNewtonUnit, -1).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(-1))),
                Arguments.of(Unit.builder().componentToExponent(gramMinuteNewtonUnit, -1).build(), Unit.builder().componentToExponent(Units.NEWTON, -1).build(), UnitConvertConfig.create(BigDecimal.valueOf(0.001).multiply(BigDecimal.valueOf(60).pow(-2)).pow(-1))),
                Arguments.of(Unit.builder().componentToExponent(tonneNewtonUnit, -1).build(), Unit.builder().componentToExponent(Units.NEWTON, -1).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(-1))),
                Arguments.of(Unit.builder().componentToExponent(Units.NON.addPrefix(Prefix.KILO), -1).build(), Unit.builder().componentToExponent(Units.NON, -1).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(-1))),
                Arguments.of(Unit.builder().componentToExponent(Units.NON.addPrefix(Prefix.KILO), -1).build(), Unit.builder().componentToExponent(Units.NON.addPrefix(Prefix.DECA), -1).build(), UnitConvertConfig.create(BigDecimal.valueOf(100).pow(-1))),
                Arguments.of(Unit.builder().componentToExponent(Units.KILOGRAM, exponent).build(), Unit.builder().componentToExponent(Units.GRAM, exponent).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(exponent))),
                Arguments.of(Unit.builder().componentToExponent(Units.KILOGRAM, exponent).build(), Unit.builder().componentToExponent(Units.GRAM.addPrefix(Prefix.DECA), exponent).build(), UnitConvertConfig.create(BigDecimal.valueOf(100).pow(exponent))),
                Arguments.of(Unit.builder().componentToExponent(Units.TONNE, exponent).build(), Unit.builder().componentToExponent(Units.GRAM, exponent).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000000).pow(exponent))),
                Arguments.of(Unit.builder().componentToExponent(Units.TONNE, exponent).build(), Unit.builder().componentToExponent(Units.KILOGRAM, exponent).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(exponent))),
                Arguments.of(Unit.builder().componentToExponent(Units.NEWTON.addPrefix(Prefix.KILO), exponent).build(), Unit.builder().componentToExponent(Units.NEWTON, exponent).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(exponent))),
                Arguments.of(Unit.builder().componentToExponent(Units.NEWTON.addPrefix(Prefix.KILO), exponent).build(), Unit.builder().componentToExponent(Units.NEWTON.addPrefix(Prefix.DECA), exponent).build(), UnitConvertConfig.create(BigDecimal.valueOf(100).pow(exponent))),
                Arguments.of(Unit.builder().componentToExponent(Units.NEWTON, exponent).build(), Unit.builder().componentToExponent(gramNewtonUnit, exponent).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(exponent))),
                Arguments.of(Unit.builder().componentToExponent(gramMinuteNewtonUnit, exponent).build(), Unit.builder().componentToExponent(Units.NEWTON, exponent).build(), UnitConvertConfig.create(BigDecimal.valueOf(0.001).multiply(BigDecimal.valueOf(60).pow(-2)).pow(exponent))),
                Arguments.of(Unit.builder().componentToExponent(tonneNewtonUnit, exponent).build(), Unit.builder().componentToExponent(Units.NEWTON, exponent).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(exponent))),
                Arguments.of(Unit.builder().componentToExponent(Units.NON.addPrefix(Prefix.KILO), exponent).build(), Unit.builder().componentToExponent(Units.NON, exponent).build(), UnitConvertConfig.create(BigDecimal.valueOf(1000).pow(exponent))),
                Arguments.of(Unit.builder().componentToExponent(Units.NON.addPrefix(Prefix.KILO), exponent).build(), Unit.builder().componentToExponent(Units.NON.addPrefix(Prefix.DECA), exponent).build(), UnitConvertConfig.create(BigDecimal.valueOf(100).pow(exponent)))
        );
    }

    static Stream<Arguments> getConvertConfigArguments() {
        return Streams.concat(sourceUnitAndTargetUnitAndUnitConvertConfigs().flatMap(arguments -> Stream.of(arguments.get()[0], arguments.get()[1]))
                        .distinct().map(unit -> Arguments.of(unit, unit, UnitConvertConfig.empty())),
                sourceUnitAndTargetUnitAndUnitConvertConfigs().map(arguments -> Arguments.of(arguments.get()[1], arguments.get()[0], ((UnitConvertConfig) arguments.get()[2]).reciprocal())),
                sourceUnitAndTargetUnitAndUnitConvertConfigs()
        );
    }

    static Stream<Unit> typeNotEqualedUnits() {
        return Stream.of(Units.METER, Units.GRAM, Units.NON, Units.NEWTON, Units.FARAD,
                Unit.builder().componentToExponent(Units.SECOND, 4).componentToExponent(Units.AMPERE, 2).componentToExponent(Units.KILOGRAM, -1).componentToExponent(Units.METER, -2).build(),
                Unit.builder().componentToExponent(Units.METER, 2).build(), Unit.builder().componentToExponent(Units.METER, -1).build());
    }

    static Stream<Arguments> noConvertConfigSourceUnitAndTargetUnits() {
        Unit massNewUnit = Unit.of(Math.random() + "", UnitTypes.MASS);
        Random random = new Random();
        int exponent = random.nextInt();
        if (exponent == 0 || exponent == 1) {
            exponent += 2;
        }
        Unit massNewUnitNewton = Unit.builder().componentToExponent(massNewUnit, 1).componentToExponent(Units.METER, 1).componentToExponent(Units.SECOND, -2).build();
        return Stream.of(
                Arguments.of(massNewUnit, Units.GRAM),
                Arguments.of(massNewUnit, Units.KILOGRAM),
                Arguments.of(massNewUnitNewton, Units.NEWTON),
                Arguments.of(Unit.builder().componentToExponent(massNewUnit, 2).build(), Unit.builder().componentToExponent(Units.GRAM, 2).build()),
                Arguments.of(Unit.builder().componentToExponent(massNewUnit, 2).build(), Unit.builder().componentToExponent(Units.KILOGRAM, 2).build()),
                Arguments.of(Unit.builder().componentToExponent(massNewUnitNewton, 2).build(), Unit.builder().componentToExponent(Units.KILOGRAM, 2).build()),
                Arguments.of(Unit.builder().componentToExponent(massNewUnit, -1).build(), Unit.builder().componentToExponent(Units.GRAM, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(massNewUnit, -1).build(), Unit.builder().componentToExponent(Units.KILOGRAM, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(massNewUnitNewton, -1).build(), Unit.builder().componentToExponent(Units.KILOGRAM, -1).build()),
                Arguments.of(Unit.builder().componentToExponent(massNewUnit, exponent).build(), Unit.builder().componentToExponent(Units.GRAM, exponent).build()),
                Arguments.of(Unit.builder().componentToExponent(massNewUnit, exponent).build(), Unit.builder().componentToExponent(Units.KILOGRAM, exponent).build()),
                Arguments.of(Unit.builder().componentToExponent(massNewUnitNewton, exponent).build(), Unit.builder().componentToExponent(Units.KILOGRAM, exponent).build())
        );
    }

    static Stream<Arguments> sourceUnitAndErrorTargetUnits() {
        return Stream.concat(typeNotEqualedUnits().flatMap(unit -> typeNotEqualedUnits()
                        .filter(other -> !unit.equals(other))
                        .map(other -> Arguments.of(unit, other))),
                noConvertConfigSourceUnitAndTargetUnits().flatMap(arguments -> Stream.of(arguments, Arguments.of(arguments.get()[1], arguments.get()[0]))));
    }
}