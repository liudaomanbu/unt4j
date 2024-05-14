package org.caotc.unit4j.support.fastjson;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.QuantityCodecStrategy;
import org.caotc.unit4j.api.annotation.UnitCodecStrategy;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.common.base.CaseFormat;
import org.caotc.unit4j.core.unit.Units;
import org.caotc.unit4j.support.Unit4jProperties;
import org.junit.jupiter.api.Test;

@Slf4j
class QuantitySerializerTest {
    @Test
    void writeNonId() {
        Unit4jProperties properties = new Unit4jProperties();
        properties.setDefaultStrategy(QuantityCodecStrategy.OBJECT);
        properties.setDefaultConfiguration(Configuration.defaultInstance());
        properties.setDefaultNameCaseFormat(CaseFormat.LOWER_CAMEL);
        properties.setDefaultUnitCodecStrategy(UnitCodecStrategy.AS_ID);

        QuantitySerializer unitSerializer = QuantitySerializer.of(properties);
        SerializeConfig serializeConfig = init(unitSerializer);
        Quantity quantity = Quantity.create(999, Units.METER);
        String jsonString = JSONObject.toJSONString(quantity, serializeConfig);
        log.info("quantity:{}", jsonString);
//        Assertions.assertEquals("\"\"", jsonString);
    }

    SerializeConfig init(QuantitySerializer quantitySerializer) {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(Quantity.class, quantitySerializer);
        return serializeConfig;
    }
}