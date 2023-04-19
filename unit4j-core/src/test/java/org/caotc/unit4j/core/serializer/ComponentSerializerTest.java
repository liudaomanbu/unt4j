package org.caotc.unit4j.core.serializer;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.common.util.Util;
import org.caotc.unit4j.core.constant.UnitTypes;
import org.caotc.unit4j.core.unit.type.UnitType;
import org.junit.jupiter.api.Test;

@Slf4j
class ComponentSerializerTest {
    @Test
    void test() {
        AliasFinder<UnitType> aliasFinder = (config, unitType) -> config.aliases(unitType, Alias.Type.SYMBOL).stream().findFirst();
        Serializer<? super UnitType> aliasUndefinedSerializer = new IdSerializer();
        Serializer<UnitType> unitTypeSerializer = new ComponentSerializer<>(
                new PowerSerializer<>(new AliasSerializer<>(Configuration.defaultInstance(), aliasFinder
                        , aliasUndefinedSerializer), "(", ")", "", Util::getSuperscript));

        log.info("unitType:{}", unitTypeSerializer.serialize(UnitTypes.ENERGY_WORK_HEAT_QUANTITY));
    }
}