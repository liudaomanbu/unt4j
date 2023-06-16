package org.caotc.unit4j.core.convert;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Quantity;
import org.junit.jupiter.api.Test;

@Slf4j
class TargetUnitChooserTest {
    @Test
    void test() {
        Quantity.UNKNOWN.autoConvert();
    }
}