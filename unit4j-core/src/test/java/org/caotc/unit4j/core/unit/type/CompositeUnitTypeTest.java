package org.caotc.unit4j.core.unit.type;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.caotc.unit4j.core.unit.CompositeStandardUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class CompositeUnitTypeTest {

  @Test
  void id() {
    String id = CompositeUnitType.RESISTANCE_ELECTRICAL_IMPEDANCE_REACTANCE.id();
    log.debug("id:{}", id);
    Assertions.assertEquals("(MASS)¹(LENGTH)²(TIME)⁻³(ELECTRIC_CURRENT)⁻²", id);
  }

  @Test
  void rebaseEquals() {
    CompositeUnitType actual = CompositeStandardUnit
        .builder().unitComponentToExponent(UnitConstant.NEWTON, 1)
        .unitComponentToExponent(UnitConstant.METER, -2).build().type();
    Assertions.assertTrue(UnitConstant.PASCAL.type().rebaseEquals(actual));
  }

  @Test
  void rebase() {
    CompositeUnitType actual = CompositeStandardUnit
        .builder().unitComponentToExponent(UnitConstant.NEWTON, 1)
        .unitComponentToExponent(UnitConstant.METER, -2).build().type();
    Assertions.assertEquals(UnitConstant.PASCAL.type().rebase(), actual.rebase());
  }

  @Test
  void multiply() {
    CompositeUnitType multiply = CompositeUnitType.FORCE_WEIGHT
        .multiply(CompositeUnitType.ILLUMINANCE);
    CompositeUnitType expected = CompositeUnitType.builder()
        .unitTypeComponentToExponent(BaseUnitType.MASS, 1)
        .unitTypeComponentToExponent(BaseUnitType.LENGTH, -1)
        .unitTypeComponentToExponent(BaseUnitType.TIME, -2)
        .unitTypeComponentToExponent(BaseUnitType.LUMINOUS_INTENSITY, 1)
        .build();
    Assertions.assertEquals(expected, multiply);
  }

  @Test
  void divide() {
    CompositeUnitType divide = CompositeUnitType.FORCE_WEIGHT.divide(CompositeUnitType.ILLUMINANCE);
    CompositeUnitType expected = CompositeUnitType.builder()
        .unitTypeComponentToExponent(BaseUnitType.MASS, 1)
        .unitTypeComponentToExponent(BaseUnitType.LENGTH, 3)
        .unitTypeComponentToExponent(BaseUnitType.TIME, -2)
        .unitTypeComponentToExponent(BaseUnitType.LUMINOUS_INTENSITY, -1)
        .build();
    Assertions.assertEquals(expected, divide);
  }

  @Test
  void inverse() {
    CompositeUnitType inverse = CompositeUnitType.FORCE_WEIGHT.inverse();
    CompositeUnitType expected = CompositeUnitType
        .builder().unitTypeComponentToExponent(BaseUnitType.MASS, -1)
        .unitTypeComponentToExponent(BaseUnitType.LENGTH, -1)
        .unitTypeComponentToExponent(BaseUnitType.TIME, 2).build();
    Assertions.assertEquals(expected, inverse);
  }
}