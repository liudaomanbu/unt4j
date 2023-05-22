package org.caotc.unit4j.core.unit.type;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Slf4j
class UnitTypeTest {

    @Test
    void rebaseEquals() {
        Assertions.assertTrue(UnitTypes.LENGTH.rebaseEquals(UnitTypes.LENGTH));
        Assertions.assertTrue(UnitTypes.ENERGY_WORK_HEAT_QUANTITY.rebaseEquals(UnitTypes.FORCE_WEIGHT.multiply(UnitTypes.LENGTH)));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.unit.type.provider.Provider#originalAndInverseds")
    void inverse(UnitType original, UnitType inversed) {
        UnitType result = original.inverse();
        log.debug("original:{},result:{}", original, result);
        Assertions.assertEquals(inversed, result);
        Assertions.assertEquals(original, result.inverse());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.unit.type.provider.Provider#multiplyArguments")
    void multiply(UnitType multiplier, UnitType multiplicand, UnitType product) {
        UnitType result = multiplier.multiply(multiplicand);
        log.debug("multiplier:{},multiplicand:{},result:{}", multiplier, multiplicand, result);
        Assertions.assertEquals(product, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.unit.type.provider.Provider#divideArguments")
    void divide(UnitType dividend, UnitType divisor, UnitType quotient) {
        UnitType result = dividend.divide(divisor);
        log.debug("dividend:{},divisor:{},result:{}", dividend, divisor, result);
        Assertions.assertEquals(quotient, result);
    }

}