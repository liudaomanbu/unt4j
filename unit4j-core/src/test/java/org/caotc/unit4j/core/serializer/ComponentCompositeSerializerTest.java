package org.caotc.unit4j.core.serializer;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.caotc.unit4j.core.unit.type.UnitType;
import org.junit.jupiter.api.Test;

@Slf4j
class ComponentCompositeSerializerTest {
    @Test
    void test() {
        PowerSerializer<UnitType> powerSerializer = PowerSerializer.<UnitType>builder()
                .elementSerializer(UnitType::id)
                .build();
        ComponentCompositeSerializer<UnitType> serializer = ComponentCompositeSerializer.<UnitType>builder()
                .powerSerializer(powerSerializer)
                .build();
        String serialize = serializer.serialize(UnitTypes.LENGTH);
        log.info("unitType:{},serialize:{}", UnitTypes.LENGTH, serialize);
        serialize = serializer.serialize(UnitTypes.NON);
        log.info("unitType:{},serialize:{}", UnitTypes.NON, serialize);
        serialize = serializer.serialize(UnitTypes.FORCE_WEIGHT);
        log.info("unitType:{},serialize:{}", UnitTypes.FORCE_WEIGHT, serialize);
    }
}