package org.caotc.unit4j.core.common.util;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.Prefix;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.caotc.unit4j.core.unit.Units;
import org.caotc.unit4j.core.unit.type.UnitType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author caotc
 * @date 2018-10-15
 * @implSpec
 * @implNote
 * @since 1.0.0
 **/
@Slf4j
class UtilTest {

    @Test
    void getSuperscript() {

        String superscript = Util.getSuperscript(-1234567890);
        log.info(superscript);
        Assertions.assertEquals("⁻¹²³⁴⁵⁶⁷⁸⁹⁰", superscript);
    }

    @Test
    void createCompositeId() {
        String compositeId = Util
                .createCompositeIdOrAlias(UnitTypes.PRESSURE_STRESS.componentToExponents());
        log.info(compositeId);
        Assertions.assertEquals("(MASS)¹(LENGTH)⁻¹(TIME)⁻²", compositeId);
        UnitType compositeUnitType = UnitType
                .builder().componentToExponent(UnitTypes.PRESSURE_STRESS, 2)
                .componentToExponent(UnitTypes.RESISTANCE_ELECTRICAL_IMPEDANCE_REACTANCE, 3)
                .build();
        compositeId = Util
                .createCompositeIdOrAlias(compositeUnitType.componentToExponents());
        log.info(compositeId);
        Assertions
                .assertEquals("((MASS)¹(LENGTH)⁻¹(TIME)⁻²)²((MASS)¹(LENGTH)²(TIME)⁻³(ELECTRIC_CURRENT)⁻²)³",
                        compositeId);
    }

    @Test
    void createCompositeAlias() {
        log.debug("NEWTON_ENGLISH_NAME ENGLISH_NAME:{}",
                Util.createCompositeIdOrAlias(Units.NEWTON.componentToExponents()));

        Unit unit = Unit.builder()
                .componentToExponent(Units.WATT.addPrefix(Prefix.CENTI), 2)
                .componentToExponent(Units.NEWTON, 3)
                .prefix(Prefix.HECTO)
                .build();

        Util.createCompositeIdOrAlias(unit.componentToExponents());
    }

}