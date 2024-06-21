package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.QuantityCodecStrategy;
import org.caotc.unit4j.api.annotation.QuantitySerialize;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.unit.Units;
import org.caotc.unit4j.support.Unit4jProperties;
import org.junit.jupiter.api.Test;

@Slf4j
class QuantitySerializerTest {
    @Test
    void writeValue() throws JsonProcessingException {
        Unit4jProperties properties = new Unit4jProperties();
        properties.setDefaultStrategy(QuantityCodecStrategy.FLAT);

        QuantitySerializer quantitySerializer = QuantitySerializer.of(properties.createQuantityCodecConfig());
        ObjectMapper mapper = init(quantitySerializer);
        Quantity quantity = Quantity.create(999, Units.METER);
//        String jsonString = mapper.writeValueAsString(quantity);
//        log.info("quantity:{}", jsonString);
        log.info("QuantityFiledObject:{}", mapper.writeValueAsString(new QuantityFiledObject(quantity)));
//        log.info("QuantityFiledMap:{}", mapper.writeValueAsString(Map.of("quantityKey", quantity)));
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
        ObjectMapper mapper = new ObjectMapper().registerModule(module);
        mapper.setAnnotationIntrospector(AnnotationIntrospector.pair(mapper.getSerializationConfig().getAnnotationIntrospector(), AnnotationIntrospector.of()));
        return mapper;
    }
}

@Value
@FieldNameConstants
//@JsonNaming(TestPropertyNamingStrategy.class)
class QuantityFiledObject {
    @QuantitySerialize(strategy = QuantityCodecStrategy.FLAT)
    public Quantity quantity;

//    @QuantitySerialize(strategy = QuantityCodecStrategy.FLAT)
//    public Quantity getQuantity(){
//        return quantity;
//    }
}

@Slf4j
class TestPropertyNamingStrategy extends PropertyNamingStrategy.PropertyNamingStrategyBase {

    @Override
    public String translate(String propertyName) {
        log.error("translate propertyName:{}", propertyName);
        return propertyName.toUpperCase();
    }
}