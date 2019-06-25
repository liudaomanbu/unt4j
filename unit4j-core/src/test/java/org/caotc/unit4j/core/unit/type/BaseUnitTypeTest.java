package org.caotc.unit4j.core.unit.type;

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
    Assertions.assertEquals(BaseUnitType.LENGTH, BaseUnitType.LENGTH.rebase());
  }
}