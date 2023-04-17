package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.caotc.unit4j.support.QuantityCodecConfig;
import org.caotc.unit4j.support.QuantityValueCodecConfig;
import org.caotc.unit4j.support.Unit4jProperties;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;

@Slf4j
class QuantityValueSerializerTest {

    Unit4jProperties unit4jProperties = new Unit4jProperties();
    QuantityCodecConfig quantityCodecConfig = unit4jProperties.createAmountCodecConfig();
    Quantity quantity = Quantity.create("123.56", UnitConstant.SECOND);
    QuantityValueSerializer quantityValueSerializer = new QuantityValueSerializer(
            new QuantityValueCodecConfig(
                    BigDecimal.class, MathContext.UNLIMITED));
    SimpleModule module = new SimpleModule("myModule").addSerializer(quantityValueSerializer)
            .addSerializer(new SerializeCommandsSerializer());
    ObjectMapper mapper = new ObjectMapper().registerModule(module);

    @Test
    void serialize() throws Exception {
        log.info("value:{}", mapper.writeValueAsString(quantity.value()));
    }

  @Test
  void test() throws Exception {
    A a = new A();
    a.getB().setName("sdhfgjtyjtuy");
    log.info("toJson:{}", mapper.writeValueAsString(a));
    log.info("parseJson:{}", mapper.readValue("{\"name\":\"shsfghsfgh\"}", A.class));
  }
}

@Data
@FieldDefaults(makeFinal = false)
@Accessors(fluent = false)
class A {

  @JsonUnwrapped
  B b = new B();
}

@Data
@FieldDefaults(makeFinal = false)
@Accessors(fluent = false)
class B {

  String name;
}