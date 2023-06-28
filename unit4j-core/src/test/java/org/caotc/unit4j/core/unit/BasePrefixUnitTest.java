package org.caotc.unit4j.core.unit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class BasePrefixUnitTest {
    @Test
    void test() {
        String toString = Units.KILOGRAM.toString();
        log.debug("toString:{}", toString);
    }

}