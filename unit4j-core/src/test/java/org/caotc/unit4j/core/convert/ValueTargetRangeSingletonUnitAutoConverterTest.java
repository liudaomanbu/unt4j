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

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Slf4j
class ValueTargetRangeSingletonUnitAutoConverterTest {


    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.convert.Provider#unitAutoConverterAndQuantityAndAutoConverted")
    void autoConvert(ValueTargetRangeSingletonUnitAutoConverter converter, Quantity quantity, Quantity autoConverted) {
        Quantity result = converter.autoConvert(quantity, Configuration.defaultInstance());
        log.debug("converter:{},quantity:{},result:{}", converter, quantity, result);
        Assertions.assertEquals(autoConverted.unit(), result.unit());
        Assertions.assertEquals(autoConverted.bigDecimalValue(), autoConverted.bigDecimalValue());
    }
}