package org.caotc.unit4j.support.json.fastjson;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import java.math.BigDecimal;
import java.math.MathContext;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.caotc.unit4j.support.AmountCodecConfig;
import org.caotc.unit4j.support.AmountValueCodecConfig;
import org.caotc.unit4j.support.SerializeCommands;
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