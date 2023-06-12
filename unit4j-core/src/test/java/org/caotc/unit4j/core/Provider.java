package org.caotc.unit4j.core;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.Prefix;
import org.caotc.unit4j.core.unit.UnitConstant;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.junit.jupiter.params.provider.Arguments;

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
                Arguments.of("", UnitConstant.NON),
                Arguments.of("10³_", UnitConstant.NON.addPrefix(Prefix.KILO)),
                Arguments.of("METER", UnitConstant.METER),
                Arguments.of("10³_METER", UnitConstant.METER.addPrefix(Prefix.KILO)),
                Arguments.of("GRAM", UnitConstant.GRAM),
                Arguments.of("10³_GRAM", UnitConstant.KILOGRAM),
                Arguments.of("(10³_GRAM)¹(METER)¹(SECOND)⁻²", UnitConstant.NEWTON),
                Arguments.of("10³_(10³_GRAM)¹(METER)¹(SECOND)⁻²", UnitConstant.NEWTON.addPrefix(Prefix.KILO)),
                Arguments.of("((SECOND)¹(AMPERE)¹)¹(((((10³_GRAM)¹(METER)¹(SECOND)⁻²)¹(METER)¹)¹(SECOND)⁻¹)¹(AMPERE)⁻¹)⁻¹", UnitConstant.FARAD),
                Arguments.of("10³_((SECOND)¹(AMPERE)¹)¹(((((10³_GRAM)¹(METER)¹(SECOND)⁻²)¹(METER)¹)¹(SECOND)⁻¹)¹(AMPERE)⁻¹)⁻¹", UnitConstant.FARAD.addPrefix(Prefix.KILO))
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
                Arguments.of(UnitConstant.GRAM, ImmutableSet.of(Alias.create(Aliases.Types.ENGLISH_NAME, "GRAM"), Alias.create(Aliases.Types.CHINESE_NAME, "克"), Alias.create(Aliases.Types.SYMBOL, "g"))),
                Arguments.of(UnitConstant.KILOGRAM, ImmutableSet.of(Alias.create(Aliases.Types.ENGLISH_NAME, "KILO_GRAM"))),
                Arguments.of(UnitConstant.NEWTON, ImmutableSet.of(Alias.create(Aliases.Types.ENGLISH_NAME, "NEWTON"), Alias.create(Aliases.Types.SYMBOL, "N"))),
                Arguments.of(UnitConstant.NEWTON.addPrefix(Prefix.KILO), ImmutableSet.of(Alias.create(Aliases.Types.ENGLISH_NAME, "KILO_NEWTON")))
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
}