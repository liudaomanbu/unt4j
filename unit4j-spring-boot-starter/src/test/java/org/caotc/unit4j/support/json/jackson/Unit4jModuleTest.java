package org.caotc.unit4j.support.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.caotc.unit4j.support.Unit4jProperties;
import org.caotc.unit4j.support.json.AmountField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class Unit4jModuleTest {

  Unit4jProperties unit4jProperties = new Unit4jProperties();
  Amount amount = Amount.create("123.56", UnitConstant.SECOND);
  AmountField amountFieldTest = AmountField.create(amount);
  Unit4jModule module = Unit4jModule.create(unit4jProperties);
  ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  void init() {
    module.registerTo(mapper);
  }

  @Test
  void serialize() throws Exception {
    log.info("amount:{}", mapper.writeValueAsString(amount));
    log.info("amountFieldTest:{}", mapper.writeValueAsString(amountFieldTest));
  }
}