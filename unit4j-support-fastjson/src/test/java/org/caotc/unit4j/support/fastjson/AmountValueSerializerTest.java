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
import java.math.BigDecimal;
import java.math.MathContext;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.SerializeCommands;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.caotc.unit4j.support.AmountCodecConfig;
import org.caotc.unit4j.support.AmountValueCodecConfig;
import org.caotc.unit4j.support.Unit4jProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class AmountValueSerializerTest {

  Unit4jProperties unit4jProperties = new Unit4jProperties();
  AmountCodecConfig amountCodecConfig = unit4jProperties.createAmountCodecConfig();
  SerializeConfig globalInstance = SerializeConfig.getGlobalInstance();
  Amount amount = Amount.create("123.56", UnitConstant.SECOND);
  AmountValueSerializer amountValueSerializer = new AmountValueSerializer(
      new AmountValueCodecConfig(BigDecimal.class, MathContext.UNLIMITED));

  @BeforeEach
  void init() {
    globalInstance.put(org.caotc.unit4j.core.math.number.BigDecimal.class, amountValueSerializer);
    globalInstance.put(org.caotc.unit4j.core.math.number.BigInteger.class, amountValueSerializer);
    globalInstance.put(org.caotc.unit4j.core.math.number.Fraction.class, amountValueSerializer);
    globalInstance.put(SerializeCommands.class, new SerializeCommandsSerializer());
  }

  @Test
  void serialize() throws Exception {
    log.info("value:{}", JSONObject.toJSONString(amount.value()));
  }
}