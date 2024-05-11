package org.caotc.unit4j.support.fastjson;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.QuantityCodecStrategy;
import org.caotc.unit4j.api.annotation.UnitCodecStrategy;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.common.base.CaseFormat;
import org.caotc.unit4j.core.unit.Units;
import org.caotc.unit4j.support.NumberCodecConfig;
import org.caotc.unit4j.support.QuantityCodecConfig;
import org.caotc.unit4j.support.UnitCodecConfig;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;

@Slf4j
class QuantitySerializerTest {
    @Test
    void writeNonId() {
        QuantitySerializer unitSerializer = QuantitySerializer.of(QuantityCodecConfig.builder()
                .strategy(QuantityCodecStrategy.AS_VALUE)
                .configuration(Configuration.defaultInstance())
                .nameCaseFormat(CaseFormat.LOWER_CAMEL)
                .outputUnitName(ImmutableList.of("unit"))
                .outputValueName(ImmutableList.of("value"))
                .valueCodecConfig(NumberCodecConfig.builder()
                        .valueType(BigDecimal.class)
                        .mathContext(MathContext.UNLIMITED)
                        .build())
                .unitCodecConfig(UnitCodecConfig.builder()
                        .strategy(UnitCodecStrategy.AS_ID)
                        .build())
                .build());
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