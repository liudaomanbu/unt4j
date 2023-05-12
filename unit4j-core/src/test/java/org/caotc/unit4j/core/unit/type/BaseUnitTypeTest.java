package org.caotc.unit4j.core.unit.type;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class BaseUnitTypeTest {
  @Test
  void id() {
    Assertions.assertEquals("LENGTH", UnitTypes.LENGTH.id());
  }

  @Test
  void unitTypeComponentToExponentMap() {
    Assertions.assertEquals(ImmutableMap.of(UnitTypes.LENGTH, 1), UnitTypes.LENGTH.componentToExponents());
  }

  @Test
  void rebase() {
    Assertions.assertEquals(UnitTypes.LENGTH, UnitTypes.LENGTH.rebase());
  }

}