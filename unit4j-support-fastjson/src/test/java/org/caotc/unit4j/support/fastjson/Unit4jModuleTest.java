package org.caotc.unit4j.support.fastjson;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.caotc.unit4j.support.Unit4jProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class Unit4jModuleTest {

    Unit4jProperties unit4jProperties = new Unit4jProperties();
    Unit4jModule unit4jModule = Unit4jModule.create(unit4jProperties);
    SerializeConfig globalInstance = SerializeConfig.getGlobalInstance();
    Quantity quantity = Quantity.create("123.00", UnitConstant.SECOND);
    QuantityField quantityFieldTest = QuantityField.create(quantity);

    @BeforeEach
    void init() {
        unit4jModule.registerTo(globalInstance, QuantityField.class);
    }

    @Test
    void write() {
        log.info("amount:{}", JSONObject.toJSONString(quantity));
        log.info("amountFieldTest:{}", JSONObject.toJSONString(quantityFieldTest));
    }
}