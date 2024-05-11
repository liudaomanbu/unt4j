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

package org.caotc.unit4j.support.fastjson;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.fraction.BigFraction;
import org.caotc.unit4j.core.math.number.BigFractionAdapter;
import org.caotc.unit4j.core.math.number.Number;
import org.caotc.unit4j.core.math.number.Numbers;
import org.caotc.unit4j.support.NumberCodecConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Random;

@Slf4j
class NumberSerializerTest {
    @RepeatedTest(5000)
    void serialize() {
        NumberSerializer numberSerializer = NumberSerializer.of(
                NumberCodecConfig.builder()
                        .valueType(BigDecimal.class)
                        .mathContext(MathContext.UNLIMITED)
                        .build());
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(Number.class, numberSerializer);
        serializeConfig.put(BigFractionAdapter.class, numberSerializer);

        Random random = new Random();
        BigDecimal value = BigDecimal.valueOf(random.nextDouble());
        String jsonString = JSONObject.toJSONString(value, serializeConfig);
        Number number = Numbers.valueOf(value);
        String result = JSONObject.toJSONString(number, serializeConfig);
        log.debug("number:{},jsonString:{},result:{}", number, jsonString, result);
        Assertions.assertEquals(jsonString, result);
    }

    @Test
    void serializeArithmeticException() {
        NumberSerializer numberSerializer = NumberSerializer.of(
                NumberCodecConfig.builder()
                        .valueType(BigDecimal.class)
                        .mathContext(MathContext.UNLIMITED)
                        .build());
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(Number.class, numberSerializer);
        serializeConfig.put(BigFractionAdapter.class, numberSerializer);

        Number number = Numbers.valueOf(BigFraction.ONE_THIRD);
        Assertions.assertThrows(ArithmeticException.class, () -> JSONObject.toJSONString(number, serializeConfig));
    }
}