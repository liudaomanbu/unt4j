package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.QuantityCodecStrategy;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.unit.Units;
import org.caotc.unit4j.support.Unit4jProperties;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Slf4j
class QuantitySerializerTest {
    @Test
    void writeValue() throws JsonProcessingException {
        Unit4jProperties properties = new Unit4jProperties();
        properties.setDefaultStrategy(QuantityCodecStrategy.AS_VALUE);

        QuantitySerializer quantitySerializer = QuantitySerializer.of(properties.createQuantityCodecConfig(), properties.createQuantityCodecConfig());
        ObjectMapper mapper = init(quantitySerializer);
//        log.info("PointObject:{}", mapper.writeValueAsString(new PointObject()));
        Quantity quantity = Quantity.create(999, Units.METER);
//        String jsonString = mapper.writeValueAsString(quantity);
//        log.info("quantity:{}", jsonString);
        log.info("QuantityFiledObject:{}", mapper.writeValueAsString(new QuantityFiledObject(quantity)));
        log.info("QuantityFiledMap:{}", mapper.writeValueAsString(Map.of("quantityKey", quantity)));
//        mapper.setPropertyNamingStrategy(new PropertyNamingStrategy.PropertyNamingStrategyBase() {
//            @Override
//            public String translate(String propertyName) {
//                return propertyName.toUpperCase();
//            }
//        });
//        Assertions.assertEquals("999", jsonString);
    }

    ObjectMapper init(QuantitySerializer serializer) {
        SimpleModule module = new SimpleModule("quantityModule")
                .addSerializer(serializer)
                .addSerializer(UnitSerializer.of(serializer.codecConfig().unitCodecConfig()))
                .addSerializer(NumberSerializer.of(serializer.codecConfig().valueCodecConfig()));
        return new ObjectMapper().registerModule(module);
    }
}

@Value
@FieldNameConstants
class PointObject {
    @JsonUnwrapped(prefix = "point")
    public Point point = new Point(99, 110);
}

@Value
class Point {
    public int x;
    public int y;
}

@Value
@FieldNameConstants
class QuantityFiledObject {
    public Quantity quantity;
}