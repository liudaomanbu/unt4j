package org.caotc.unit4j.core.serializer;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Power;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.caotc.unit4j.core.unit.type.UnitType;
import org.junit.jupiter.api.Test;

@Slf4j
class PowerSerializerTest {
    @Test
    void test() {
        PowerSerializer<UnitType> powerSerializer = PowerSerializer.<UnitType>builder()
                .elementSerializer(UnitType::id)
                .build();
        Power<UnitType> power = new Power<>(UnitTypes.LENGTH, 2);
        String serialize = powerSerializer.serialize(power);
        log.info("power:{},serialize:{}", power, serialize);
        power = new Power<>(UnitTypes.NON, 2);
        serialize = powerSerializer.serialize(power);
        log.info("power:{},serialize:{}", power, serialize);
        power = new Power<>(UnitTypes.FORCE_WEIGHT, 2);
        serialize = powerSerializer.serialize(power);
        log.info("power:{},serialize:{}", power, serialize);
        power = new Power<>(UnitTypes.POWER_RADIANT_FLUX, 2);
        serialize = powerSerializer.serialize(power);
        log.info("power:{},serialize:{}", power, serialize);
    }
}