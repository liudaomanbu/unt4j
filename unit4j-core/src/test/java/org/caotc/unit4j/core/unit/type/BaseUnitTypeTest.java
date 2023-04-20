package org.caotc.unit4j.core.unit.type;

import org.caotc.unit4j.core.unit.UnitTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BaseUnitTypeTest {

  @Test
  void rebaseEquals() {
  }

  @Test
  void unitTypeComponentToExponentMap() {
  }

  @Test
  void rebase() {
      Assertions.assertEquals(UnitTypes.LENGTH, UnitTypes.LENGTH.rebase());
  }
}