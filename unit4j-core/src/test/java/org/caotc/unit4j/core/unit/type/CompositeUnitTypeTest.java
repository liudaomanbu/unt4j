package org.caotc.unit4j.core.unit.type;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.CompositeStandardUnit;
import org.caotc.unit4j.core.unit.UnitConstant;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class CompositeUnitTypeTest {

  @Test
  void id() {
      String id = UnitTypes.RESISTANCE_ELECTRICAL_IMPEDANCE_REACTANCE.id();
    log.debug("id:{}", id);
    Assertions.assertEquals("(MASS)¹(LENGTH)²(TIME)⁻³(ELECTRIC_CURRENT)⁻²", id);
  }

  @Test
  void rebaseEquals() {
      UnitType actual = CompositeStandardUnit
              .builder().componentToExponent(UnitConstant.NEWTON, 1)
              .componentToExponent(UnitConstant.METER, -2).build().type();
    Assertions.assertTrue(UnitConstant.PASCAL.type().rebaseEquals(actual));
  }

  @Test
  void rebase() {
      UnitType actual = CompositeStandardUnit
              .builder().componentToExponent(UnitConstant.NEWTON, 1)
              .componentToExponent(UnitConstant.METER, -2).build().type();
    Assertions.assertEquals(UnitConstant.PASCAL.type().rebase(), actual.rebase());
  }

  @Test
  void multiply() {
      UnitType multiply = UnitTypes.FORCE_WEIGHT
              .multiply(UnitTypes.ILLUMINANCE);
      UnitType expected = CompositeUnitType.builder()
              .componentToExponent(UnitTypes.MASS, 1)
              .componentToExponent(UnitTypes.LENGTH, -1)
              .componentToExponent(UnitTypes.TIME, -2)
              .componentToExponent(UnitTypes.LUMINOUS_INTENSITY, 1)
              .build();
      Assertions.assertEquals(expected, multiply);
  }

  @Test
  void divide() {
      UnitType divide = UnitTypes.FORCE_WEIGHT.divide(UnitTypes.ILLUMINANCE);
      UnitType expected = UnitType.builder()
              .componentToExponent(UnitTypes.MASS, 1)
              .componentToExponent(UnitTypes.LENGTH, 3)
              .componentToExponent(UnitTypes.TIME, -2)
              .componentToExponent(UnitTypes.LUMINOUS_INTENSITY, -1)
              .build();
      Assertions.assertEquals(expected, divide);
  }

  @Test
  void inverse() {
      UnitType inverse = UnitTypes.FORCE_WEIGHT.inverse();
      UnitType expected = UnitType
              .builder().componentToExponent(UnitTypes.MASS, -1)
              .componentToExponent(UnitTypes.LENGTH, -1)
              .componentToExponent(UnitTypes.TIME, 2).build();
      Assertions.assertEquals(expected, inverse);
  }
}