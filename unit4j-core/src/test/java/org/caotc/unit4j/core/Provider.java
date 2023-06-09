package org.caotc.unit4j.core;

import com.google.common.collect.ImmutableSet;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.Prefix;
import org.caotc.unit4j.core.unit.UnitConstant;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.junit.jupiter.params.provider.Arguments;

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
}