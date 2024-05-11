package org.caotc.unit4j.core.serializer;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Aliases;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.caotc.unit4j.core.unit.type.UnitType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class ComponentCompositeSerializerTest {
    @Test
    void test() {
        Configuration configuration = Configuration.defaultInstance();
        configuration.registerAlias(UnitTypes.FORCE_WEIGHT, Alias.create(Aliases.Types.CHINESE_NAME, "重量"));

        AliasSerializer<UnitType> base = AliasSerializer.<UnitType>builder()
                .aliasFinder(FirstAliasFinder.of(Aliases.Types.CHINESE_NAME))
                .configuration(configuration)
                .aliasUndefinedSerializer(UnitType::id)
                .build();
        PowerSerializer<UnitType> powerSerializer = PowerSerializer.<UnitType>builder()
                .elementSerializer(base)
                .build();
        ComponentCompositeSerializer<UnitType> componentCompositeSerializer = ComponentCompositeSerializer.<UnitType>builder()
                .powerSerializer(powerSerializer)
                .build();
        Serializer<UnitType> serializer = AliasSerializer.<UnitType>builder()
                .aliasFinder(FirstAliasFinder.of(Aliases.Types.CHINESE_NAME))
                .configuration(configuration)
                .aliasUndefinedSerializer(componentCompositeSerializer)
                .build();
        String serialize = serializer.serialize(UnitTypes.LENGTH);
        log.info("unitType:{},serialize:{}", UnitTypes.LENGTH, serialize);
        Assertions.assertEquals("LENGTH", serialize);
        serialize = serializer.serialize(UnitTypes.NON);
        log.info("unitType:{},serialize:{}", UnitTypes.NON, serialize);
        Assertions.assertEquals("", serialize);
        serialize = serializer.serialize(UnitTypes.FORCE_WEIGHT);
        log.info("unitType:{},serialize:{}", UnitTypes.FORCE_WEIGHT, serialize);
        Assertions.assertEquals("重量", serialize);
        UnitType unitType = UnitType.builder()
                .componentToExponent(UnitTypes.FORCE_WEIGHT, 3)
                .componentToExponent(UnitTypes.LENGTH, -1)
                .build();
        serialize = serializer.serialize(unitType);
        log.info("unitType:{},serialize:{}", unitType, serialize);
        Assertions.assertEquals("(重量)³(LENGTH)⁻¹", serialize);
    }
}