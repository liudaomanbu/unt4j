package org.caotc.unit4j.core.serializer;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Aliases;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.caotc.unit4j.core.unit.type.UnitType;
import org.junit.jupiter.api.Test;

@Slf4j
class ComponentSerializerTest {
    @Test
    void test() {
        AliasFinder<UnitType> aliasFinder = DefaultAliasFinder.of(Aliases.Types.SYMBOL);
        Serializer<UnitType> aliasUndefinedSerializer = new IdentifiableSerializer<>();
        Serializer<UnitType> unitTypeSerializer = ComponentSerializer.<UnitType>builder()
                .configuration(Configuration.defaultInstance())
                .aliasFinder(aliasFinder)
                .aliasUndefinedStrategy(AliasUndefinedStrategy.ID)
                .baseSerializer(AliasSerializer.<UnitType>builder()
                        .configuration(Configuration.defaultInstance())
                        .aliasFinder(aliasFinder)
                        .aliasUndefinedSerializer(aliasUndefinedSerializer)
                        .build())
                .build();

        log.info("unitType:{}", unitTypeSerializer.serialize(UnitTypes.ENERGY_WORK_HEAT_QUANTITY));
        log.info("unitType:{}", aliasUndefinedSerializer.serialize(UnitTypes.ENERGY_WORK_HEAT_QUANTITY));
    }
}