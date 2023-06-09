package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Aliases;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.serializer.AliasUndefinedStrategy;
import org.caotc.unit4j.core.unit.UnitConstant;
import org.caotc.unit4j.support.UnitCodecConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class UnitSerializerTest {

    UnitSerializer unitSerializer = new UnitSerializer(new UnitCodecConfig(Aliases.Types.ENGLISH_NAME,
            Configuration.defaultInstance(), AliasUndefinedStrategy.THROW_EXCEPTION));
    SimpleModule module = new SimpleModule("myModule").addSerializer(unitSerializer);
  ObjectMapper mapper = new ObjectMapper().registerModule(module);

  @Test
  void serialize() throws Exception {
    String unitJson = mapper.writeValueAsString(UnitConstant.NEWTON);
    log.debug("unit:{}", unitJson);
    Assertions.assertEquals("\"NEWTON\"", unitJson);
  }
}