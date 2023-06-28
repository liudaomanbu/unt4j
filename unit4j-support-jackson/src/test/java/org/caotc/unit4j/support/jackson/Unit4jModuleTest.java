package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.unit.Units;
import org.caotc.unit4j.support.Unit4jProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class Unit4jModuleTest {

    Unit4jProperties unit4jProperties = new Unit4jProperties();
    Quantity quantity = Quantity.create("123.56", Units.SECOND);
    QuantityField quantityFieldTest = QuantityField.create(quantity);
    Unit4jModule module = Unit4jModule.create(unit4jProperties);
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void init() {
        module.registerTo(mapper);
    }

    @Test
    void serialize() throws Exception {
        log.info("amount:{}", mapper.writeValueAsString(quantity));
        log.info("amountFieldTest:{}", mapper.writeValueAsString(quantityFieldTest));
    }
}