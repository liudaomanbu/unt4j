/*
 * Copyright (C) 2020 the original author or authors.
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

package org.caotc.unit4j.support.fastjson;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.SerializeCommands;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.unit.UnitConstant;
import org.caotc.unit4j.support.QuantityCodecConfig;
import org.caotc.unit4j.support.QuantityValueCodecConfig;
import org.caotc.unit4j.support.Unit4jProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;

@Slf4j
class QuantityValueSerializerTest {

    Unit4jProperties unit4jProperties = new Unit4jProperties();
    QuantityCodecConfig quantityCodecConfig = unit4jProperties.createAmountCodecConfig();
    SerializeConfig globalInstance = SerializeConfig.getGlobalInstance();
    Quantity quantity = Quantity.create("123.56", UnitConstant.SECOND);
    QuantityValueSerializer quantityValueSerializer = new QuantityValueSerializer(
            new QuantityValueCodecConfig(BigDecimal.class, MathContext.UNLIMITED));

    @BeforeEach
    void init() {
        globalInstance.put(org.caotc.unit4j.core.math.number.BigDecimal.class, quantityValueSerializer);
        globalInstance.put(org.caotc.unit4j.core.math.number.BigInteger.class, quantityValueSerializer);
        globalInstance.put(org.caotc.unit4j.core.math.number.Fraction.class, quantityValueSerializer);
        globalInstance.put(SerializeCommands.class, new SerializeCommandsSerializer());
    }

    @Test
  void serialize() throws Exception {
        log.info("value:{}", JSONObject.toJSONString(quantity.value()));
  }
}