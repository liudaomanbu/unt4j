package org.caotc.unit4j.core.unit.type;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;

@Slf4j
class UnitTypeTest {

    @Test
    void rebaseEquals() {
        Assertions.assertTrue(UnitTypes.LENGTH.rebaseEquals(UnitTypes.LENGTH));
        Assertions.assertTrue(UnitTypes.ENERGY_WORK_HEAT_QUANTITY.rebaseEquals(UnitTypes.FORCE_WEIGHT.multiply(UnitTypes.LENGTH)));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.unit.type.Provider#originalAndInverseds")
    void reciprocal(UnitType original, UnitType inversed) {
        UnitType result = original.reciprocal();
        log.debug("original:{},result:{}", original, result);
        Assertions.assertEquals(inversed, result);
        Assertions.assertEquals(original, result.reciprocal());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.unit.type.Provider#originalAndExponentAndPoweds")
    void pow(UnitType original, int exponent, UnitType powed) {
        UnitType result = original.pow(exponent);
        log.debug("original:{},exponent:{},result:{}", original, exponent, result);
        Assertions.assertEquals(powed, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.unit.type.Provider#multiplyArguments")
    void multiply(UnitType multiplier, UnitType multiplicand, UnitType product) {
        UnitType result = multiplier.multiply(multiplicand);
        log.debug("multiplier:{},multiplicand:{},result:{}", multiplier, multiplicand, result);
        Assertions.assertEquals(product, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.unit.type.Provider#divideArguments")
    void divide(UnitType dividend, UnitType divisor, UnitType quotient) {
        UnitType result = dividend.divide(divisor);
        log.debug("dividend:{},divisor:{},result:{}", dividend, divisor, result);
        Assertions.assertEquals(quotient, result, String.format("%s!=%s", quotient.id(), result.id()));
    }

    @Slf4j
    static class BuilderTest {
        @ParameterizedTest
        @MethodSource("org.caotc.unit4j.core.unit.type.Provider#componentAndExponentAndUnitTypes")
        void componentToExponent(UnitType component, int exponent, UnitType unitType) {
            UnitType result = UnitType.builder().componentToExponent(component, exponent).build();
            log.debug("component:{},exponent:{},result:{}", component, exponent, result);
            Assertions.assertEquals(unitType, result, String.format("%s!=%s", unitType.id(), result.id()));
        }

        @ParameterizedTest
        @MethodSource("org.caotc.unit4j.core.unit.type.Provider#componentMapAndUnitTypes")
        void build(Map<UnitType, Integer> componentMap, UnitType unitType) {
            UnitType result = UnitType.builder().componentToExponents(componentMap).build();
            log.debug("component:{},result:{}", componentMap, result);
            Assertions.assertEquals(unitType, result, String.format("%s!=%s", unitType.id(), result.id()));
        }
    }
}