package org.caotc.unit4j.core.unit.type;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.CompositeStandardUnit;
import org.caotc.unit4j.core.unit.UnitConstant;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Slf4j
class CompositeUnitTypeTest {

    @Test
    void test() {
        String toString = UnitTypes.RESISTANCE_ELECTRICAL_IMPEDANCE_REACTANCE.toString();
        log.debug("toString:{}", toString);
    }


    @Test
    void id() {
        String id = UnitTypes.RESISTANCE_ELECTRICAL_IMPEDANCE_REACTANCE.id();
        log.debug("id:{}", id);
        Assertions.assertEquals("(MASS)¹(LENGTH)²(TIME)⁻³(ELECTRIC_CURRENT)⁻²", id);
        log.debug("id:{}", UnitType.builder().componentToExponent(UnitTypes.FORCE_WEIGHT.multiply(UnitTypes.LENGTH), -1).build().id());
    }

    @Test
    void rebase() {
        UnitType actual = CompositeStandardUnit
                .builder().componentToExponent(UnitConstant.NEWTON, 1)
                .componentToExponent(UnitConstant.METER, -2).build().type();
        Assertions.assertEquals(UnitConstant.PASCAL.type().rebase(), actual.rebase());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.unit.type.Provider#originalAndRecursiveAndSimplifieds")
    void simplify(UnitType original, boolean recursive, UnitType simplified) {
        UnitType result = original.simplify(recursive);
        log.debug("original:{},recursive:{},result:{}", original.id(), recursive, result.id());
        Assertions.assertEquals(simplified, result, String.format("%s!=%s", simplified.id(), result.id()));
    }

    @Test
    void validate() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CompositeUnitType(ImmutableMap.of(UnitTypes.LENGTH, 1)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CompositeUnitType(ImmutableMap.of(UnitTypes.FORCE_WEIGHT, 1)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CompositeUnitType(ImmutableMap.<UnitType, Integer>builder().putAll(UnitTypes.FORCE_WEIGHT.componentToExponents()).put(UnitTypes.TEMPERATURE, 0).build()));
    }
}