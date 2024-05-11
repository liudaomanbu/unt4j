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
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.UnitCodecStrategy;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Identifiable;
import org.caotc.unit4j.core.serializer.AliasSerializer;
import org.caotc.unit4j.core.serializer.FirstAliasFinder;
import org.caotc.unit4j.core.unit.BasePrefixUnit;
import org.caotc.unit4j.core.unit.BaseStandardUnit;
import org.caotc.unit4j.core.unit.CompositePrefixUnit;
import org.caotc.unit4j.core.unit.CompositeStandardUnit;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.Units;
import org.caotc.unit4j.support.UnitCodecConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Slf4j
class UnitSerializerTest {

    SerializeConfig init(UnitSerializer unitSerializer) {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(BaseStandardUnit.class, unitSerializer);
        serializeConfig.put(BasePrefixUnit.class, unitSerializer);
        serializeConfig.put(CompositeStandardUnit.class, unitSerializer);
        serializeConfig.put(CompositePrefixUnit.class, unitSerializer);
        return serializeConfig;
    }

    @Test
    void writeNonId() {
        UnitSerializer unitSerializer = UnitSerializer.of(UnitCodecConfig.builder()
                .strategy(UnitCodecStrategy.AS_ID)
                .build());
        SerializeConfig serializeConfig = init(unitSerializer);
        String jsonString = JSONObject.toJSONString(Units.NON, serializeConfig);
        Assertions.assertTrue(jsonString.isEmpty());
    }

    @Test
    void writeNonAlias() {
        Configuration configuration = Configuration.of();
        Alias.Type type = Alias.Type.of("writeNonAlias");
        UnitSerializer unitSerializer = UnitSerializer.of(UnitCodecConfig.builder()
                .strategy(UnitCodecStrategy.AS_ALIAS)
                .aliasSerializer(AliasSerializer.<Unit>builder()
                        .configuration(configuration)
                        .aliasFinder(FirstAliasFinder.of(type))
                        .aliasUndefinedSerializer(Identifiable::id)
                        .build())
                .build());
        SerializeConfig serializeConfig = init(unitSerializer);
        String jsonString = JSONObject.toJSONString(Units.NON, serializeConfig);
        log.info("Non:{}", jsonString);
        Assertions.assertTrue(jsonString.isEmpty());

        String alias = "Non";
        configuration.registerAlias(Units.NON, Alias.create(type, alias));
        jsonString = JSONObject.toJSONString(Units.NON, serializeConfig);
        log.info("Non:{}", jsonString);
        Assertions.assertEquals(alias, jsonString);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.support.fastjson.provider.UnitSerializerProvider#units")
    void writeUnitId(Unit unit) {
        UnitSerializer unitSerializer = UnitSerializer.of(UnitCodecConfig.builder()
                .strategy(UnitCodecStrategy.AS_ID)
                .build());
        SerializeConfig serializeConfig = init(unitSerializer);
        String jsonString = JSONObject.toJSONString(unit, serializeConfig);
        log.info("{}:{}", unit, jsonString);
        Assertions.assertEquals(unit.id(), jsonString);
    }
}

@Value
class UnitFiledObject {
    @JSONField(unwrapped = true)
    public Unit unit;
}

