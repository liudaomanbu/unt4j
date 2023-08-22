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
import org.caotc.unit4j.core.Aliases;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.serializer.AliasUndefinedStrategy;
import org.caotc.unit4j.core.unit.BasePrefixUnit;
import org.caotc.unit4j.core.unit.BaseStandardUnit;
import org.caotc.unit4j.core.unit.CompositePrefixUnit;
import org.caotc.unit4j.core.unit.CompositeStandardUnit;
import org.caotc.unit4j.core.unit.Units;
import org.caotc.unit4j.support.UnitCodecConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class UnitSerializerTest {

  SerializeConfig globalInstance = SerializeConfig.getGlobalInstance();
  UnitSerializer unitSerializer = new UnitSerializer(UnitCodecConfig.builder().type(Aliases.Types.ENGLISH_NAME)
          .configuration(Configuration.defaultInstance())
          .aliasUndefinedStrategy(AliasUndefinedStrategy.THROW_EXCEPTION).build());

  @BeforeEach
  void init() {
    globalInstance.put(BaseStandardUnit.class, unitSerializer);
    globalInstance.put(BasePrefixUnit.class, unitSerializer);
    globalInstance.put(CompositeStandardUnit.class, unitSerializer);
    globalInstance.put(CompositePrefixUnit.class, unitSerializer);
  }

  @Test
  void write() {
    String unitJson = JSONObject.toJSONString(Units.NEWTON);
    log.debug("unit:{}", unitJson);
    Assertions.assertEquals("\"NEWTON\"", unitJson);
  }
}