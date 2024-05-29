package org.caotc.unit4j.support.fastjson;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.QuantityCodecStrategy;
import org.caotc.unit4j.api.annotation.UnitCodecStrategy;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.unit.Units;
import org.caotc.unit4j.support.Unit4jProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Slf4j
class QuantitySerializerTest {
    @Test
    void writeValue() {
        Unit4jProperties properties = new Unit4jProperties();
        properties.setDefaultStrategy(QuantityCodecStrategy.AS_VALUE);

        QuantitySerializer quantitySerializer = QuantitySerializer.of(properties);
        SerializeConfig serializeConfig = init(quantitySerializer);
        Quantity quantity = Quantity.create(999, Units.METER);
        String jsonString = JSONObject.toJSONString(quantity, serializeConfig);
        log.info("quantity:{}", jsonString);
        Assertions.assertEquals("999", jsonString);
    }

    @Test
    void writeObject() {
        Unit4jProperties properties = new Unit4jProperties();
        properties.setDefaultStrategy(QuantityCodecStrategy.OBJECT);
        properties.setDefaultUnitCodecStrategy(UnitCodecStrategy.AS_ID);

        QuantitySerializer quantitySerializer = QuantitySerializer.of(properties);
        SerializeConfig serializeConfig = init(quantitySerializer);
        Quantity quantity = Quantity.create(999, Units.METER);
        String jsonString = JSONObject.toJSONString(quantity, serializeConfig);
        log.info("quantity:{}", jsonString);
        Assertions.assertEquals("{\"unit\":\"METER\",\"value\":999}", jsonString);
    }

    @Test
    void writeMapQuantityFlat() {
        Unit4jProperties properties = new Unit4jProperties();
        properties.setDefaultPropertyStrategy(QuantityCodecStrategy.FLAT);
        properties.setDefaultUnitCodecStrategy(UnitCodecStrategy.AS_ID);

        QuantitySerializer quantitySerializer = QuantitySerializer.of(properties);
        SerializeConfig serializeConfig = init(quantitySerializer);
        Quantity quantity = Quantity.create(999, Units.METER);
        String jsonString = JSONObject.toJSONString(Map.of("quantity", quantity), serializeConfig);
        log.info("map:{}", jsonString);
        Assertions.assertEquals("{\"quantity\":null,\"quantityUnit\":\"METER\",\"quantityValue\":999}", jsonString);
    }

    @Test
    void writeMapQuantityLowerUnderscoreFlat() {
        Unit4jProperties properties = new Unit4jProperties();
        properties.setDefaultPropertyStrategy(QuantityCodecStrategy.FLAT);
        properties.setDefaultUnitCodecStrategy(UnitCodecStrategy.AS_ID);

        QuantitySerializer quantitySerializer = QuantitySerializer.of(properties);
        SerializeConfig serializeConfig = init(quantitySerializer);
        Quantity quantity = Quantity.create(999, Units.METER);
        String jsonString = JSONObject.toJSONString(Map.of("quantity_field", quantity), serializeConfig);
        log.info("map:{}", jsonString);
        Assertions.assertEquals("{\"quantity_field\":null,\"quantity_field_unit\":\"METER\",\"quantity_field_value\":999}", jsonString);
    }

    @Test
    void writeMapQuantityFlatWithNameFilter() {
        Unit4jProperties properties = new Unit4jProperties();
        properties.setDefaultPropertyStrategy(QuantityCodecStrategy.FLAT);
        properties.setDefaultUnitCodecStrategy(UnitCodecStrategy.AS_ID);

        QuantitySerializer quantitySerializer = QuantitySerializer.of(properties);
        SerializeConfig serializeConfig = init(quantitySerializer);
        Quantity quantity = Quantity.create(999, Units.METER);
        String jsonString = JSONObject.toJSONString(Map.of("quantity_field", quantity), serializeConfig, (NameFilter) (object, name, value) -> name.toUpperCase());
        log.info("map:{}", jsonString);
        Assertions.assertEquals("{\"QUANTITY_FIELD\":null,\"QUANTITY_FIELD_UNIT\":\"METER\",\"QUANTITY_FIELD_VALUE\":999}", jsonString);
    }

    @Test
    void writeFieldQuantityFlat() {
        Unit4jProperties properties = new Unit4jProperties();
        properties.setDefaultPropertyStrategy(QuantityCodecStrategy.FLAT);
        properties.setDefaultUnitCodecStrategy(UnitCodecStrategy.AS_ID);

        QuantitySerializer quantitySerializer = QuantitySerializer.of(properties);
        SerializeConfig serializeConfig = init(quantitySerializer);
        Quantity quantity = Quantity.create(999, Units.METER);
        String jsonString = JSONObject.toJSONString(new QuantityFiledObject(quantity), serializeConfig);
        log.info("QuantityFiledObject:{}", jsonString);
        Assertions.assertEquals("{\"quantity\":null,\"quantityUnit\":\"METER\",\"quantityValue\":999}", jsonString);
    }

    @Test
    void writeFieldQuantityFlatDefaultPropertyNamingStrategy() {
        Unit4jProperties properties = new Unit4jProperties();
        properties.setDefaultPropertyStrategy(QuantityCodecStrategy.FLAT);
        properties.setDefaultUnitCodecStrategy(UnitCodecStrategy.AS_ID);

        QuantitySerializer quantitySerializer = QuantitySerializer.of(properties);
        SerializeConfig serializeConfig = init(quantitySerializer);
        Quantity quantity = Quantity.create(999, Units.METER);
        String jsonString = JSONObject.toJSONString(new QuantityFiledDefaultPropertyNamingStrategyObject(quantity), serializeConfig);
        log.info("QuantityFiledDefaultPropertyNamingStrategyObject:{}", jsonString);
        Assertions.assertEquals("{\"quantityField\":null,\"quantityFieldUnit\":\"METER\",\"quantityFieldValue\":999}", jsonString);
    }

    @Test
    void writeFieldQuantityFlatCamelCase() {
        Unit4jProperties properties = new Unit4jProperties();
        properties.setDefaultPropertyStrategy(QuantityCodecStrategy.FLAT);
        properties.setDefaultUnitCodecStrategy(UnitCodecStrategy.AS_ID);

        QuantitySerializer quantitySerializer = QuantitySerializer.of(properties);
        SerializeConfig serializeConfig = init(quantitySerializer);
        Quantity quantity = Quantity.create(999, Units.METER);
        String jsonString = JSONObject.toJSONString(new QuantityFiledCamelCaseObject(quantity), serializeConfig);
        log.info("QuantityFiledCamelCaseObject:{}", jsonString);
        Assertions.assertEquals("{\"quantityField\":null,\"quantityFieldUnit\":\"METER\",\"quantityFieldValue\":999}", jsonString);
    }

    @Test
    void writeFieldQuantityFlatPascalCase() {
        Unit4jProperties properties = new Unit4jProperties();
        properties.setDefaultPropertyStrategy(QuantityCodecStrategy.FLAT);
        properties.setDefaultUnitCodecStrategy(UnitCodecStrategy.AS_ID);

        QuantitySerializer quantitySerializer = QuantitySerializer.of(properties);
        SerializeConfig serializeConfig = init(quantitySerializer);
        Quantity quantity = Quantity.create(999, Units.METER);
        String jsonString = JSONObject.toJSONString(new QuantityFiledPascalCaseObject(quantity), serializeConfig);
        log.info("QuantityFiledPascalCaseObject:{}", jsonString);
        Assertions.assertEquals("{\"QuantityField\":null,\"QuantityFieldUnit\":\"METER\",\"QuantityFieldValue\":999}", jsonString);
    }

    @Test
    void writeFieldQuantityFlatSnakeCase() {
        Unit4jProperties properties = new Unit4jProperties();
        properties.setDefaultPropertyStrategy(QuantityCodecStrategy.FLAT);
        properties.setDefaultUnitCodecStrategy(UnitCodecStrategy.AS_ID);

        QuantitySerializer quantitySerializer = QuantitySerializer.of(properties);
        SerializeConfig serializeConfig = init(quantitySerializer);
        Quantity quantity = Quantity.create(999, Units.METER);
        String jsonString = JSONObject.toJSONString(new QuantityFiledSnakeCaseObject(quantity), serializeConfig);
        log.info("QuantityFiledSnakeCaseObject:{}", jsonString);
        Assertions.assertEquals("{\"quantity_field\":null,\"quantity_field_unit\":\"METER\",\"quantity_field_value\":999}", jsonString);
    }

    @Test
    void writeFieldQuantityFlatKebabCase() {
        Unit4jProperties properties = new Unit4jProperties();
        properties.setDefaultPropertyStrategy(QuantityCodecStrategy.FLAT);
        properties.setDefaultUnitCodecStrategy(UnitCodecStrategy.AS_ID);

        QuantitySerializer quantitySerializer = QuantitySerializer.of(properties);
        SerializeConfig serializeConfig = init(quantitySerializer);
        Quantity quantity = Quantity.create(999, Units.METER);
        String jsonString = JSONObject.toJSONString(new QuantityFiledKebabCaseObject(quantity), serializeConfig);
        log.info("QuantityFiledKebabCaseObject:{}", jsonString);
        Assertions.assertEquals("{\"quantity-field\":null,\"quantity-field-unit\":\"METER\",\"quantity-field-value\":999}", jsonString);
    }

    @Test
    void writeFieldQuantityFlatWithNameFilter() {
        Unit4jProperties properties = new Unit4jProperties();
        properties.setDefaultPropertyStrategy(QuantityCodecStrategy.FLAT);
        properties.setDefaultUnitCodecStrategy(UnitCodecStrategy.AS_ID);

        QuantitySerializer quantitySerializer = QuantitySerializer.of(properties);
        SerializeConfig serializeConfig = init(quantitySerializer);
        serializeConfig.addFilter(QuantityFiledObject.class, (NameFilter) (object, name, value) -> name.toUpperCase());
        Quantity quantity = Quantity.create(999, Units.METER);
        String jsonString = JSONObject.toJSONString(new QuantityFiledObject(quantity), serializeConfig);
        log.info("QuantityFiledObject:{}", jsonString);
        Assertions.assertEquals("{\"QUANTITY\":null,\"QUANTITY_UNIT\":\"METER\",\"QUANTITY_VALUE\":999}", jsonString);
    }

    SerializeConfig init(QuantitySerializer quantitySerializer) {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(Quantity.class, quantitySerializer);
        return serializeConfig;
    }
}

@Value
@FieldNameConstants
class QuantityFiledObject {
    public Quantity quantity;
}

@Value
class QuantityFiledDefaultPropertyNamingStrategyObject {
    public Quantity quantityField;
}

@JSONType(naming = PropertyNamingStrategy.CamelCase)
@Value
class QuantityFiledCamelCaseObject {
    public Quantity quantityField;
}

@JSONType(naming = PropertyNamingStrategy.PascalCase)
@Value
class QuantityFiledPascalCaseObject {
    public Quantity quantityField;
}

@JSONType(naming = PropertyNamingStrategy.SnakeCase)
@Value
class QuantityFiledSnakeCaseObject {
    public Quantity quantityField;
}

@JSONType(naming = PropertyNamingStrategy.KebabCase)
@Value
class QuantityFiledKebabCaseObject {
    public Quantity quantityField;
}