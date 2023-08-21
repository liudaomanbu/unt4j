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

package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.unit.Units;
import org.caotc.unit4j.support.NumberCodecConfig;
import org.caotc.unit4j.support.QuantityCodecConfig;
import org.caotc.unit4j.support.Unit4jProperties;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;

@Slf4j
class QuantityValueSerializerTest {

    Unit4jProperties unit4jProperties = new Unit4jProperties();
    QuantityCodecConfig quantityCodecConfig = unit4jProperties.createAmountCodecConfig();
    Quantity quantity = Quantity.create("123.56", Units.SECOND);
    QuantityValueSerializer quantityValueSerializer = new QuantityValueSerializer(
            new NumberCodecConfig(
                    BigDecimal.class, MathContext.UNLIMITED));
    SimpleModule module = new SimpleModule("myModule").addSerializer(quantityValueSerializer)
            .addSerializer(new SerializeCommandsSerializer());
    ObjectMapper mapper = new ObjectMapper().registerModule(module);

    @Test
    void serialize() throws Exception {
        log.info("value:{}", mapper.writeValueAsString(quantity.value()));
    }

  @Test
  void test() throws Exception {
    A a = new A();
    a.getB().setName("sdhfgjtyjtuy");
    log.info("toJson:{}", mapper.writeValueAsString(a));
    log.info("parseJson:{}", mapper.readValue("{\"name\":\"shsfghsfgh\"}", A.class));
  }
}

@Data
@FieldDefaults(makeFinal = false)
@Accessors(fluent = false)
class A {

  @JsonUnwrapped
  B b = new B();
}

@Data
@FieldDefaults(makeFinal = false)
@Accessors(fluent = false)
class B {

  String name;
}