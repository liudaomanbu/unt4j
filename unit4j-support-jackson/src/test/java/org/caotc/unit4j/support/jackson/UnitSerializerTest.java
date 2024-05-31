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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.UnitCodecStrategy;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Aliases;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Identifiable;
import org.caotc.unit4j.core.serializer.AliasSerializer;
import org.caotc.unit4j.core.serializer.FirstAliasFinder;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.Units;
import org.caotc.unit4j.support.UnitCodecConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
class UnitSerializerTest {

    @Test
    void writeNonId() throws JsonProcessingException {
        UnitSerializer unitSerializer = UnitSerializer.of(UnitCodecConfig.builder()
                .strategy(UnitCodecStrategy.AS_ID)
                .build());
        ObjectMapper mapper = init(unitSerializer);
        String jsonString = mapper.writeValueAsString(Units.NON);
        Assertions.assertEquals("\"\"", jsonString);
    }

    @Test
    void writeNonAlias() throws JsonProcessingException {
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
        ObjectMapper mapper = init(unitSerializer);
        String jsonString = mapper.writeValueAsString(Units.NON);
        log.info("Non:{}", jsonString);
        Assertions.assertEquals("\"\"", jsonString);

        String alias = "Non";
        configuration.registerAlias(Units.NON, Alias.create(type, alias));
        jsonString = mapper.writeValueAsString(Units.NON);
        log.info("Non:{}", jsonString);
        Assertions.assertEquals(String.format("\"%s\"", alias), jsonString);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.support.jackson.provider.UnitSerializerProvider#units")
    void writeUnitId(Unit unit) throws JsonProcessingException {
        UnitSerializer unitSerializer = UnitSerializer.of(
                UnitCodecConfig.builder()
                        .strategy(UnitCodecStrategy.AS_ID)
                        .build());
        ObjectMapper mapper = init(unitSerializer);
        String jsonString = mapper.writeValueAsString(unit);
        log.info("{}:{}", unit, jsonString);
        Assertions.assertEquals(String.format("\"%s\"", unit.id()), jsonString);
    }

    @Test
    void writeUnitAlias() throws JsonProcessingException {
        Alias.Type type = Aliases.Types.CHINESE_NAME;
        UnitSerializer unitSerializer = UnitSerializer.of(UnitCodecConfig.builder()
                .strategy(UnitCodecStrategy.AS_ALIAS)
                .aliasSerializer(AliasSerializer.<Unit>builder()
                        .configuration(Configuration.defaultInstance())
                        .aliasFinder(FirstAliasFinder.of(type))
                        .aliasUndefinedSerializer(Identifiable::id)
                        .build())
                .build());
        ObjectMapper mapper = init(unitSerializer);
        String jsonString = mapper.writeValueAsString(Units.METER);
        log.info("METER:{}", jsonString);
        Assertions.assertEquals(String.format("\"%s\"", "米"), jsonString);
    }

    @Test
    void writeUnitArray() throws JsonProcessingException {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        ObjectMapper mapper = init(unitSerializer);
        Unit[] array = new Unit[]{Units.METER};
        String jsonString = mapper.writeValueAsString(array);
        log.info("{}:{}", array, jsonString);
        Assertions.assertEquals(String.format("[\"%s\"]", Units.METER.id()), jsonString);
    }

    @Test
    void writeUnitList() throws JsonProcessingException {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        ObjectMapper mapper = init(unitSerializer);
        List<Unit> list = List.of(Units.METER);
        String jsonString = mapper.writeValueAsString(list);
        log.info("{}:{}", list, jsonString);
        Assertions.assertEquals(String.format("[\"%s\"]", Units.METER.id()), jsonString);
    }

    @Test
    void writeUnitSet() throws JsonProcessingException {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        ObjectMapper mapper = init(unitSerializer);
        Set<Unit> set = Set.of(Units.METER);
        String jsonString = mapper.writeValueAsString(set);
        log.info("{}:{}", set, jsonString);
        Assertions.assertEquals(String.format("[\"%s\"]", Units.METER.id()), jsonString);
    }

    @Test
    void writeMapUnit() throws JsonProcessingException {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        ObjectMapper mapper = init(unitSerializer);
        String unitKey = "unitKey";
        Map<String, Object> map = Map.of(unitKey, Units.METER);
        String jsonString = mapper.writeValueAsString(map);
        log.info("{}:{}", map, jsonString);
        Assertions.assertEquals(String.format("{\"%s\":\"米\"}", unitKey), jsonString);
    }

    @Test
    void writeMapUnitCollection() throws JsonProcessingException {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        ObjectMapper mapper = init(unitSerializer);
        String unitKey = "unitKey";
        Map<String, Object> map = Map.of(unitKey, List.of(Units.METER));
        String jsonString = mapper.writeValueAsString(map);
        log.info("{}:{}", map, jsonString);
        Assertions.assertEquals(String.format("{\"%s\":[\"%s\"]}", unitKey, Units.METER.id()), jsonString);
    }

    @Test
    void writeFieldUnit() throws JsonProcessingException {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        ObjectMapper mapper = init(unitSerializer);
        UnitFiledObject object = new UnitFiledObject(Units.METER);
        String jsonString = mapper.writeValueAsString(object);
        log.info("{}:{}", object, jsonString);
        Assertions.assertEquals(String.format("{\"%s\":\"米\"}", UnitFiledObject.Fields.UNIT), jsonString);
    }

    @Test
    void writeFieldUnitCollection() throws JsonProcessingException {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        ObjectMapper mapper = init(unitSerializer);
        UnitCollectionFiledObject object = new UnitCollectionFiledObject(List.of(Units.METER));
        String jsonString = mapper.writeValueAsString(object);
        log.info("{}:{}", object, jsonString);
        Assertions.assertEquals(String.format("{\"%s\":[\"%s\"]}", UnitCollectionFiledObject.Fields.UNITS, Units.METER.id()), jsonString);
    }

    @Test
    void writeFieldUnitUnwrapped() throws JsonProcessingException {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        ObjectMapper mapper = init(unitSerializer);
        UnwrappedUnitFiledObject object = new UnwrappedUnitFiledObject(Units.METER);
        String jsonString = mapper.writeValueAsString(object);
        log.info("{}:{}", object, jsonString);
        /*
        对于序列化结果为非object的情况,unwrapped不应该生效
        因为如果UnwrappedUnitFiledObject存在其他属性会产生类似于{"Name":"test","米"}之类的非法输出
         */
        Assertions.assertEquals("{\"unit\":\"米\"}", jsonString);
    }

    @Test
    void writeJSONFieldNameFieldUnit() throws JsonProcessingException {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        ObjectMapper mapper = init(unitSerializer);
        JsonPropertyNameUnitFiledObject object = new JsonPropertyNameUnitFiledObject(Units.METER);
        String jsonString = mapper.writeValueAsString(object);
        log.info("{}:{}", object, jsonString);
        Assertions.assertEquals(String.format("{\"%s\":\"米\"}", "unitField"), jsonString);
    }

    @Test
    void writePropertyNamingStrategyFieldUnit() throws JsonProcessingException {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        ObjectMapper mapper = init(unitSerializer);
        mapper.setPropertyNamingStrategy(new PropertyNamingStrategy.PropertyNamingStrategyBase() {
            @Override
            public String translate(String propertyName) {
                return propertyName.toUpperCase();
            }
        });
        UnitFiledObject object = new UnitFiledObject(Units.METER);
        String jsonString = mapper.writeValueAsString(object);
        log.info("{}:{}", object, jsonString);
        Assertions.assertEquals(String.format("{\"%s\":\"米\"}", UnitFiledObject.Fields.UNIT.toUpperCase()), jsonString);
    }

    @Test
    void writePropertyNamingStrategyFieldUnitUnwrapped() throws JsonProcessingException {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        ObjectMapper mapper = init(unitSerializer);
        mapper.setPropertyNamingStrategy(new PropertyNamingStrategy.PropertyNamingStrategyBase() {
            @Override
            public String translate(String propertyName) {
                return propertyName.toUpperCase();
            }
        });
        UnwrappedUnitFiledObject object = new UnwrappedUnitFiledObject(Units.METER);
        String jsonString = mapper.writeValueAsString(object);
        log.info("{}:{}", object, jsonString);
        /*
        对于序列化结果为非object的情况,unwrapped不应该生效
        因为如果UnwrappedUnitFiledObject存在其他属性会产生类似于{"Name":"test","米"}之类的非法输出
         */
        Assertions.assertEquals("{\"UNIT\":\"米\"}", jsonString);
    }

    @Test
    void writeNameFilterJSONFieldNameFieldUnit() throws JsonProcessingException {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        ObjectMapper mapper = init(unitSerializer);
        mapper.setPropertyNamingStrategy(new PropertyNamingStrategy.PropertyNamingStrategyBase() {
            @Override
            public String translate(String propertyName) {
                return propertyName.toUpperCase();
            }
        });
        JsonPropertyNameUnitFiledObject object = new JsonPropertyNameUnitFiledObject(Units.METER);
        String jsonString = mapper.writeValueAsString(object);
        log.info("{}:{}", object, jsonString);
        //按照正确逻辑,JsonProperty name优先级高于PropertyNamingStrategy,jackson逻辑,仅做了解
//        Assertions.assertEquals(String.format("{\"%s\":\"米\"}","unitField"), jsonString);
    }

    UnitSerializer propertyChineseNameUnitSerializer() {
        Alias.Type type = Aliases.Types.CHINESE_NAME;
        return UnitSerializer.of(UnitCodecConfig.builder()
                        .strategy(UnitCodecStrategy.AS_ID)
                        .build(),
                UnitCodecConfig.builder()
                        .strategy(UnitCodecStrategy.AS_ALIAS)
                        .aliasSerializer(AliasSerializer.<Unit>builder()
                                .configuration(Configuration.defaultInstance())
                                .aliasFinder(FirstAliasFinder.of(type))
                                .aliasUndefinedSerializer(Identifiable::id)
                                .build())
                        .build());
    }

    ObjectMapper init(UnitSerializer serializer) {
        SimpleModule module = new SimpleModule("unitModule").addSerializer(serializer);
        return new ObjectMapper().registerModule(module);
    }
}

@Value
@FieldNameConstants
class UnitFiledObject {
    public Unit unit;
}

@Value
@FieldNameConstants
class UnitCollectionFiledObject {
    public Collection<Unit> units;
}

@Value
class UnwrappedUnitFiledObject {
    @JsonUnwrapped
    public Unit unit;
}

@Value
class JsonPropertyNameUnitFiledObject {
    @JsonProperty("unitField")
    public Unit unit;
}