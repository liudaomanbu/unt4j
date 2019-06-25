package org.caotc.unit4j.core.util;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.type.CompositeUnitType;
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
        .createCompositeIdOrAlias(CompositeUnitType.PRESSURE_STRESS.unitTypeComponentToExponents());
    log.info(compositeId);
    Assertions.assertEquals("(MASS)¹(LENGTH)⁻¹(TIME)⁻²", compositeId);
    CompositeUnitType compositeUnitType = CompositeUnitType
        .builder().unitTypeComponentToExponent(CompositeUnitType.PRESSURE_STRESS, 2)
        .unitTypeComponentToExponent(CompositeUnitType.RESISTANCE_ELECTRICAL_IMPEDANCE_REACTANCE, 3)
        .build();
    compositeId = Util
        .createCompositeIdOrAlias(compositeUnitType.unitTypeComponentToExponents());
    log.info(compositeId);
    Assertions
        .assertEquals("((MASS)¹(LENGTH)⁻¹(TIME)⁻²)²((MASS)¹(LENGTH)²(TIME)⁻³(ELECTRIC_CURRENT)⁻²)³",
            compositeId);
  }

  @Test
  void createCompositeAlias() {
//    log.debug("NEWTON_ENGLISH_NAME ENGLISH_NAME:{}",
//        Util.createCompositeAlias(UnitConstant.NEWTON_ENGLISH_NAME.unitComponentToExponents(),
//            Configuration.defaultInstance(), Type.ENGLISH_NAME));

//    Unit unit = CompositeStandardUnit.builder()
//        .unitComponentToExponent(UnitConstant.WATT_ENGLISH_NAME.addPrefix(Prefix.CENTI_ENGLISH_NAME), 2)
//        .unitComponentToExponent(UnitConstant.NEWTON_ENGLISH_NAME, 3).build()
//        .addPrefix(Prefix.HECTO_ENGLISH_NAME);;
//    Util.createCompositeAlias(unit.unitComponentToExponents(), Configuration.defaultInstance(),
//        Type.ENGLISH_NAME);
  }

}