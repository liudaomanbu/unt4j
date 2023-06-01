package org.caotc.unit4j.core.unit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Slf4j
class CompositeStandardUnitTest {

    @Test
    void type() {
    }

    @Test
    void rebase() {
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.unit.Provider#originalAndSimplifyConfigAndSimplifieds")
    void simplify(Unit original, SimplifyConfig simplifyConfig, Unit simplified) {
        Unit result = original.simplify(simplifyConfig);
        log.debug("original:{},simplifyConfig:{},result:{}", original.id(), simplifyConfig, result.id());
        Assertions.assertEquals(simplified, result, String.format("%s!=%s", simplified.id(), result.id()));
    }

    @Test
    void pow() {
    }

    @Test
    void reciprocal() {
    }

    @Test
    void multiply() {
    }

    @Test
    void testMultiply() {
    }

    @Test
    void testMultiply1() {
    }

    @Test
    void testMultiply2() {
    }

    @Test
    void addPrefix() {
    }

    @Test
    void id() {
    }
}