package org.caotc.unit4j.support.fastjson;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.unit.Units;
import org.caotc.unit4j.support.Unit4jProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class Unit4jModuleTest {

    @Test
    void write() {
        SerializeConfig serializeConfig = new SerializeConfig();

        Unit4jProperties unit4jProperties = new Unit4jProperties();
        Unit4jModule unit4jModule = Unit4jModule.of(unit4jProperties);
        unit4jModule.registerTo(serializeConfig);

        Quantity quantity = Quantity.create("123.00", Units.SECOND);
        QuantityField quantityFieldObject = QuantityField.create(quantity);
        String quantityJson = JSONObject.toJSONString(quantity, serializeConfig);
        String quantityFieldObjectJson = JSONObject.toJSONString(quantityFieldObject, serializeConfig);
        log.info("quantity:{}", quantityJson);
        log.info("quantityFieldObject:{}", quantityFieldObjectJson);
        Assertions.assertEquals("123", quantityJson);
        Assertions.assertEquals("{\"annotationFlatQuantity\":null,\"annotationFlatQuantityUnit\":\"SECOND\",\"annotationFlatQuantityValue\":\"123\",\"annotationObjectQuantity\":{\"unit\":\"SECOND\",\"value\":123},\"annotationValueQuantity\":123,\"noAnnotationQuantity\":123}", quantityFieldObjectJson);
    }
}