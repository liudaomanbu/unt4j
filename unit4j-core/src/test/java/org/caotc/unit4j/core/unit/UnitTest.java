package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.type.UnitType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;

@Slf4j
class UnitTest {
    @Test
    void id() {
        log.info("id:{}", Units.JOULE.id());
    }

    @Test
    void unitComponentToExponents() {
        ImmutableMap<Unit, Integer> unitComponentIntegerImmutableMap = Units.GRAM
                .componentToExponents();
        Assertions
                .assertEquals(ImmutableMap.of(Units.GRAM, 1), unitComponentIntegerImmutableMap);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.unit.Provider#unitAndTypes")
    void type(Unit unit, UnitType unitType) {
        UnitType result = unit.type();
        log.debug("unit:{},result:{}", unit.id(), result.id());
        Assertions.assertEquals(unitType, result, String.format("%s!=%s", unitType.id(), result.id()));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.unit.Provider#unitAndTypeToDimensionElementMaps")
    void typeToDimensionElementMap(Unit unit, ImmutableMap<UnitType, Dimension> typeToDimensionElementMap) {
        ImmutableMap<UnitType, Dimension> result = unit.typeToDimensionElementMap();
        log.debug("unit:{},result:{}", unit.id(), result);
        Assertions.assertEquals(typeToDimensionElementMap, result, String.format("%s!=%s", typeToDimensionElementMap, result));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.unit.Provider#originalAndInverseds")
    void reciprocal(Unit original, Unit inversed) {
        Unit result = original.reciprocal();
        log.debug("original:{},result:{}", original, result);
        Assertions.assertEquals(inversed, result);
        Assertions.assertEquals(original, result.reciprocal());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.unit.Provider#originalAndExponentAndPoweds")
    void pow(Unit original, int exponent, Unit powed) {
        Unit result = original.pow(exponent);
        log.debug("original:{},exponent:{},result:{}", original.id(), exponent, result.id());
        Assertions.assertEquals(powed, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.unit.Provider#multiplyArguments")
    void multiply(Unit multiplier, Unit multiplicand, Unit product) {
        Unit result = multiplier.multiply(multiplicand);
        log.debug("multiplier:{},multiplicand:{},result:{}", multiplier.id(), multiplicand.id(), result.id());
        Assertions.assertEquals(product, result, String.format("%s!=%s", product.id(), result.id()));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.unit.Provider#divideArguments")
    void divide(Unit dividend, Unit divisor, Unit quotient) {
        Unit result = dividend.divide(divisor);
        log.debug("dividend:{},divisor:{},result:{}", dividend.id(), divisor.id(), result.id());
        Assertions.assertEquals(quotient, result, String.format("%s!=%s", quotient.id(), result.id()));
    }

    @Slf4j
    static class BuilderTest {
        @ParameterizedTest
        @MethodSource("org.caotc.unit4j.core.unit.Provider#componentAndExponentAndUnits")
        void componentToExponent(Unit component, int exponent, Unit unit) {
            Unit result = Unit.builder().componentToExponent(component, exponent).build();
            log.debug("component:{},exponent:{},result:{}", component.id(), exponent, result.id());
            Assertions.assertEquals(unit, result, String.format("%s!=%s", unit.id(), result.id()));
        }

        @ParameterizedTest
        @MethodSource("org.caotc.unit4j.core.unit.Provider#componentMapAndUnits")
        void build(Map<Unit, Integer> componentMap, Unit unit) {
            Unit result = Unit.builder().componentToExponents(componentMap).build();
            log.debug("component:{},result:{}", componentMap, result.id());
            Assertions.assertEquals(unit, result, String.format("%s!=%s", unit.id(), result.id()));
        }
    }
}