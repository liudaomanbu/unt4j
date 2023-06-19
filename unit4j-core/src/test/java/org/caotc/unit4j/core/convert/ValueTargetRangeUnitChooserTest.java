package org.caotc.unit4j.core.convert;

import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.math.number.BigInteger;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.UnitConstant;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;

@Slf4j
class ValueTargetRangeUnitChooserTest {


    @Test
    void test() {
        ValueTargetRangeUnitChooser unitChooser = ValueTargetRangeUnitChooser.of(Range.closedOpen(BigInteger.valueOf(1), BigInteger.valueOf(1000)));

        Configuration configuration = Configuration.defaultInstance();
        Quantity quantity = Quantity.create(8000, UnitConstant.METER);
        log.info("units:{}", configuration.getUnitGroup(quantity.unit()).units());
        Unit targetUnit = unitChooser.choose(quantity, configuration);
        log.info("targetUnit:{}", targetUnit.id());
        log.info("targetValue:{}", configuration.convertTo(quantity, targetUnit).value().value(BigDecimal.class, MathContext.DECIMAL128));
    }
}