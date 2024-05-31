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
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
class UnitSerializerTest {

    @Test
    void writeNonId() {
        UnitSerializer unitSerializer = UnitSerializer.of(UnitCodecConfig.builder().strategy(UnitCodecStrategy.AS_ID).build());
        SerializeConfig serializeConfig = init(unitSerializer);
        String jsonString = JSONObject.toJSONString(Units.NON, serializeConfig);
        Assertions.assertEquals("\"\"", jsonString);
    }

    @Test
    void writeNonAlias() {
        Configuration configuration = Configuration.of();
        Alias.Type type = Alias.Type.of("writeNonAlias");
        UnitSerializer unitSerializer = UnitSerializer.of(UnitCodecConfig.builder().strategy(UnitCodecStrategy.AS_ALIAS).aliasSerializer(AliasSerializer.<Unit>builder().configuration(configuration).aliasFinder(FirstAliasFinder.of(type)).aliasUndefinedSerializer(Identifiable::id).build()).build());
        SerializeConfig serializeConfig = init(unitSerializer);
        String jsonString = JSONObject.toJSONString(Units.NON, serializeConfig);
        log.info("Non:{}", jsonString);
        Assertions.assertEquals("\"\"", jsonString);

        String alias = "Non";
        configuration.registerAlias(Units.NON, Alias.create(type, alias));
        jsonString = JSONObject.toJSONString(Units.NON, serializeConfig);
        log.info("Non:{}", jsonString);
        Assertions.assertEquals(String.format("\"%s\"", alias), jsonString);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.support.fastjson.provider.UnitSerializerProvider#units")
    void writeUnitId(Unit unit) {
        UnitSerializer unitSerializer = UnitSerializer.of(UnitCodecConfig.builder().strategy(UnitCodecStrategy.AS_ID).build());
        SerializeConfig serializeConfig = init(unitSerializer);
        String jsonString = JSONObject.toJSONString(unit, serializeConfig);
        log.info("{}:{}", unit, jsonString);
        Assertions.assertEquals(String.format("\"%s\"", unit.id()), jsonString);
    }

    @Test
    void writeUnitAlias() {
        Alias.Type type = Aliases.Types.CHINESE_NAME;
        UnitSerializer unitSerializer = UnitSerializer.of(UnitCodecConfig.builder().strategy(UnitCodecStrategy.AS_ALIAS).aliasSerializer(AliasSerializer.<Unit>builder().configuration(Configuration.defaultInstance()).aliasFinder(FirstAliasFinder.of(type)).aliasUndefinedSerializer(Identifiable::id).build()).build());
        SerializeConfig serializeConfig = init(unitSerializer);
        String jsonString = JSONObject.toJSONString(Units.METER, serializeConfig);
        log.info("METER:{}", jsonString);
        Assertions.assertEquals(String.format("\"%s\"", "米"), jsonString);
    }

    @Test
    void writeUnitArray() {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        SerializeConfig serializeConfig = init(unitSerializer);
        Unit[] array = new Unit[]{Units.METER};
        String jsonString = JSONObject.toJSONString(array, serializeConfig);
        log.info("{}:{}", array, jsonString);
        Assertions.assertEquals(String.format("[\"%s\"]", Units.METER.id()), jsonString);
    }

    @Test
    void writeUnitList() {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        SerializeConfig serializeConfig = init(unitSerializer);
        List<Unit> list = List.of(Units.METER);
        String jsonString = JSONObject.toJSONString(list, serializeConfig);
        log.info("{}:{}", list, jsonString);
        Assertions.assertEquals(String.format("[\"%s\"]", Units.METER.id()), jsonString);
    }

    @Test
    void writeUnitSet() {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        SerializeConfig serializeConfig = init(unitSerializer);
        Set<Unit> set = Set.of(Units.METER);
        String jsonString = JSONObject.toJSONString(set, serializeConfig);
        log.info("{}:{}", set, jsonString);
        Assertions.assertEquals(String.format("[\"%s\"]", Units.METER.id()), jsonString);
    }

    @Test
    void writeMapUnit() {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        SerializeConfig serializeConfig = init(unitSerializer);
        String unitKey = "unitKey";
        Map<String, Object> map = Map.of(unitKey, Units.METER);
        String jsonString = JSONObject.toJSONString(map, serializeConfig);
        log.info("{}:{}", map, jsonString);
        Assertions.assertEquals(String.format("{\"%s\":\"米\"}", unitKey), jsonString);
    }

    @Test
    void writeMapUnitCollection() {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        SerializeConfig serializeConfig = init(unitSerializer);
        String unitKey = "unitKey";
        Map<String, Object> map = Map.of(unitKey, List.of(Units.METER));
        String jsonString = JSONObject.toJSONString(map, serializeConfig);
        log.info("{}:{}", map, jsonString);
        Assertions.assertEquals(String.format("{\"%s\":[\"%s\"]}", unitKey, Units.METER.id()), jsonString);
    }

    @Test
    void writeFieldUnit() {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        SerializeConfig serializeConfig = init(unitSerializer);
        UnitFiledObject object = new UnitFiledObject(Units.METER);
        String jsonString = JSONObject.toJSONString(object, serializeConfig);
        log.info("{}:{}", object, jsonString);
        Assertions.assertEquals(String.format("{\"%s\":\"米\"}", UnitFiledObject.Fields.UNIT), jsonString);
    }

    @Test
    void writeFieldUnitCollection() {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        SerializeConfig serializeConfig = init(unitSerializer);
        UnitCollectionFiledObject object = new UnitCollectionFiledObject(List.of(Units.METER));
        String jsonString = JSONObject.toJSONString(object, serializeConfig);
        log.info("{}:{}", object, jsonString);
        Assertions.assertEquals(String.format("{\"%s\":[\"%s\"]}", UnitCollectionFiledObject.Fields.UNITS, Units.METER.id()), jsonString);
    }

    @Test
    void writeFieldUnitUnwrapped() {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        SerializeConfig serializeConfig = init(unitSerializer);
        UnwrappedUnitFiledObject object = new UnwrappedUnitFiledObject(Units.METER);
        String jsonString = JSONObject.toJSONString(object, serializeConfig);
        log.info("{}:{}", object, jsonString);
        /*
        对于序列化结果为非object的情况,unwrapped不应该生效
        因为如果UnwrappedUnitFiledObject存在其他属性会产生类似于{"Name":"test","米"}之类的非法输出
        fastjson bug,ignore.
         */
//        Assertions.assertEquals("{\"unit\":\"米\"}", jsonString);
    }

    @Test
    void writeJSONFieldNameFieldUnit() {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        SerializeConfig serializeConfig = init(unitSerializer);
        JSONFieldNameUnitFiledObject object = new JSONFieldNameUnitFiledObject(Units.METER);
        String jsonString = JSONObject.toJSONString(object, serializeConfig);
        log.info("{}:{}", object, jsonString);
        Assertions.assertEquals(String.format("{\"%s\":\"米\"}", "unitField"), jsonString);
    }

    @Test
    void writeNameFilterFieldUnit() {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        SerializeConfig serializeConfig = init(unitSerializer);
        serializeConfig.addFilter(UnitFiledObject.class, (NameFilter) (object, name, value) -> name.toUpperCase());
        UnitFiledObject object = new UnitFiledObject(Units.METER);
        String jsonString = JSONObject.toJSONString(object, serializeConfig);
        log.info("{}:{}", object, jsonString);
        Assertions.assertEquals(String.format("{\"%s\":\"米\"}", UnitFiledObject.Fields.UNIT.toUpperCase()), jsonString);
    }

    @Test
    void writeNameFilterFieldUnitUnwrapped() {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        SerializeConfig serializeConfig = init(unitSerializer);
        serializeConfig.addFilter(UnwrappedUnitFiledObject.class, (NameFilter) (object, name, value) -> name.toUpperCase());
        UnwrappedUnitFiledObject object = new UnwrappedUnitFiledObject(Units.METER);
        String jsonString = JSONObject.toJSONString(object, serializeConfig);
        log.info("{}:{}", object, jsonString);
        /*
        对于序列化结果为非object的情况,unwrapped不应该生效
        因为如果UnwrappedUnitFiledObject存在其他属性会产生类似于{"Name":"test","米"}之类的非法输出
         */
        Assertions.assertEquals("{\"UNIT\":\"米\"}", jsonString);
    }

    @Test
    void writeNameFilterJSONFieldNameFieldUnit() {
        UnitSerializer unitSerializer = propertyChineseNameUnitSerializer();
        SerializeConfig serializeConfig = init(unitSerializer);
        serializeConfig.addFilter(UnwrappedUnitFiledObject.class, (NameFilter) (object, name, value) -> name.toUpperCase());
        JSONFieldNameUnitFiledObject object = new JSONFieldNameUnitFiledObject(Units.METER);
        String jsonString = JSONObject.toJSONString(object, serializeConfig);
        log.info("{}:{}", object, jsonString);
        //按照正确逻辑,JSONField name优先级高于NameFilter,fastjson逻辑,仅做了解
//        Assertions.assertEquals(String.format("{\"%s\":\"米\"}","unitField"), jsonString);
    }

    SerializeConfig init(UnitSerializer unitSerializer) {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(BaseStandardUnit.class, unitSerializer);
        serializeConfig.put(BasePrefixUnit.class, unitSerializer);
        serializeConfig.put(CompositeStandardUnit.class, unitSerializer);
        serializeConfig.put(CompositePrefixUnit.class, unitSerializer);
        return serializeConfig;
    }

    UnitSerializer propertyChineseNameUnitSerializer() {
        Alias.Type type = Aliases.Types.CHINESE_NAME;
        return UnitSerializer.of(UnitCodecConfig.builder().strategy(UnitCodecStrategy.AS_ID).build(), UnitCodecConfig.builder().strategy(UnitCodecStrategy.AS_ALIAS).aliasSerializer(AliasSerializer.<Unit>builder().configuration(Configuration.defaultInstance()).aliasFinder(FirstAliasFinder.of(type)).aliasUndefinedSerializer(Identifiable::id).build()).build());
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
    @JSONField(unwrapped = true)
    public Unit unit;
}

@Value
class JSONFieldNameUnitFiledObject {
    @JSONField(name = "unitField")
    public Unit unit;
}

