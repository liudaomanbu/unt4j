package org.caotc.unit4j.support.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.math.BigDecimal;
import java.math.MathContext;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.caotc.unit4j.support.AmountCodecConfig;
import org.caotc.unit4j.support.AmountValueCodecConfig;
import org.caotc.unit4j.support.Unit4jProperties;
import org.junit.jupiter.api.Test;

@Slf4j
class AmountValueSerializerTest {

  Unit4jProperties unit4jProperties = new Unit4jProperties();
  AmountCodecConfig amountCodecConfig = unit4jProperties.createAmountCodecConfig();
  Amount amount = Amount.create("123.56", UnitConstant.SECOND);
  AmountValueSerializer amountValueSerializer = new AmountValueSerializer(
      new AmountValueCodecConfig(
          BigDecimal.class, MathContext.UNLIMITED));
  SimpleModule module = new SimpleModule("myModule").addSerializer(amountValueSerializer)
      .addSerializer(new SerializeCommandsSerializer());
  ObjectMapper mapper = new ObjectMapper().registerModule(module);

  @Test
  void serialize() throws Exception {
    log.info("value:{}", mapper.writeValueAsString(amount.value()));
  }
}