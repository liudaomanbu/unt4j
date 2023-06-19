/*
 * Copyright (C) 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.caotc.unit4j.core.convert;

import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.math.number.BigInteger;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.UnitConstant;
import org.caotc.unit4j.core.unit.UnitGroup;
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
        UnitGroup unitGroup = configuration.getUnitGroup(quantity.unit());
        log.info("units:{}", unitGroup.units());
        Unit targetUnit = unitChooser.choose(quantity, configuration);
        log.info("targetUnit:{}", targetUnit.id());
        log.info("targetValue:{}", configuration.convertTo(quantity, targetUnit).value().value(BigDecimal.class, MathContext.DECIMAL128));

        quantity = Quantity.create("0.1", unitGroup.first());
        targetUnit = unitChooser.choose(quantity, configuration);
        log.info("targetUnit:{}", targetUnit.id());
        log.info("targetValue:{}", configuration.convertTo(quantity, targetUnit).value().value(BigDecimal.class, MathContext.DECIMAL128));

        quantity = Quantity.create(1000, unitGroup.last());
        targetUnit = unitChooser.choose(quantity, configuration);
        log.info("targetUnit:{}", targetUnit.id());
        log.info("targetValue:{}", configuration.convertTo(quantity, targetUnit).value().value(BigDecimal.class, MathContext.DECIMAL128));

        unitChooser = ValueTargetRangeUnitChooser.of(Range.atMost(BigInteger.valueOf(1000)));

        quantity = Quantity.create(8000, UnitConstant.METER);
        targetUnit = unitChooser.choose(quantity, configuration);
        log.info("targetUnit:{}", targetUnit.id());
        log.info("targetValue:{}", configuration.convertTo(quantity, targetUnit).value().value(BigDecimal.class, MathContext.DECIMAL128));

        quantity = Quantity.create("0.1", unitGroup.first());
        targetUnit = unitChooser.choose(quantity, configuration);
        log.info("targetUnit:{}", targetUnit.id());
        log.info("targetValue:{}", configuration.convertTo(quantity, targetUnit).value().value(BigDecimal.class, MathContext.DECIMAL128));

        quantity = Quantity.create(1000, unitGroup.last());
        targetUnit = unitChooser.choose(quantity, configuration);
        log.info("targetUnit:{}", targetUnit.id());
        log.info("targetValue:{}", configuration.convertTo(quantity, targetUnit).value().value(BigDecimal.class, MathContext.DECIMAL128));


        unitChooser = ValueTargetRangeUnitChooser.of(Range.atLeast(BigInteger.valueOf(1)));

        quantity = Quantity.create(8000, UnitConstant.METER);
        targetUnit = unitChooser.choose(quantity, configuration);
        log.info("targetUnit:{}", targetUnit.id());
        log.info("targetValue:{}", configuration.convertTo(quantity, targetUnit).value().value(BigDecimal.class, MathContext.DECIMAL128));

        quantity = Quantity.create("0.1", unitGroup.first());
        targetUnit = unitChooser.choose(quantity, configuration);
        log.info("targetUnit:{}", targetUnit.id());
        log.info("targetValue:{}", configuration.convertTo(quantity, targetUnit).value().value(BigDecimal.class, MathContext.DECIMAL128));

        quantity = Quantity.create(1000, unitGroup.last());
        targetUnit = unitChooser.choose(quantity, configuration);
        log.info("targetUnit:{}", targetUnit.id());
        log.info("targetValue:{}", configuration.convertTo(quantity, targetUnit).value().value(BigDecimal.class, MathContext.DECIMAL128));

        unitChooser = ValueTargetRangeUnitChooser.of(Range.closedOpen(BigInteger.valueOf(1), BigInteger.valueOf(1)));

        quantity = Quantity.create(8000, UnitConstant.METER);
        targetUnit = unitChooser.choose(quantity, configuration);
        log.info("targetUnit:{}", targetUnit.id());
        log.info("targetValue:{}", configuration.convertTo(quantity, targetUnit).value().value(BigDecimal.class, MathContext.DECIMAL128));

        quantity = Quantity.create("0.1", unitGroup.first());
        targetUnit = unitChooser.choose(quantity, configuration);
        log.info("targetUnit:{}", targetUnit.id());
        log.info("targetValue:{}", configuration.convertTo(quantity, targetUnit).value().value(BigDecimal.class, MathContext.DECIMAL128));

        quantity = Quantity.create(1000, unitGroup.last());
        targetUnit = unitChooser.choose(quantity, configuration);
        log.info("targetUnit:{}", targetUnit.id());
        log.info("targetValue:{}", configuration.convertTo(quantity, targetUnit).value().value(BigDecimal.class, MathContext.DECIMAL128));
    }
}