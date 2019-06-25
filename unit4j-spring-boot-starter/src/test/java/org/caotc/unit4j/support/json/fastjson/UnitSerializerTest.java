package org.caotc.unit4j.support.json.fastjson;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.caotc.unit4j.core.unit.Alias.Type;
import org.caotc.unit4j.core.unit.BasePrefixUnit;
import org.caotc.unit4j.core.unit.BaseStandardUnit;
import org.caotc.unit4j.core.unit.CompositePrefixUnit;
import org.caotc.unit4j.core.unit.CompositeStandardUnit;
import org.caotc.unit4j.support.AliasUndefinedStrategy;
import org.caotc.unit4j.support.UnitCodecConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class UnitSerializerTest {

  SerializeConfig globalInstance = SerializeConfig.getGlobalInstance();
  UnitSerializer unitSerializer = new UnitSerializer(new UnitCodecConfig(Type.ENGLISH_NAME,
      Configuration.defaultInstance(), AliasUndefinedStrategy.THROW_EXCEPTION));

  @BeforeEach
  void init() {
    globalInstance.put(BaseStandardUnit.class, unitSerializer);
    globalInstance.put(BasePrefixUnit.class, unitSerializer);
    globalInstance.put(CompositeStandardUnit.class, unitSerializer);
    globalInstance.put(CompositePrefixUnit.class, unitSerializer);
  }

  @Test
  void write() {
    String unitJson = JSONObject.toJSONString(UnitConstant.NEWTON);
    log.debug("unit:{}", unitJson);
    Assertions.assertEquals("\"NEWTON\"", unitJson);
  }
}