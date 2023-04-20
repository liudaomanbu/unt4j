package org.caotc.unit4j.core.unit;

import org.caotc.unit4j.core.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class CompositePrefixUnitGroupTest {

  private UnitGroup timeUnitGroup = UnitGroup
          .create(UnitConstant.DEFAULT_TIME_STANDARD_UNITS,
                  Configuration.defaultInstance()::compare);

  @Test
  void previous() {
    Optional<Unit> previous = timeUnitGroup.lower(UnitConstant.ERA);
    Assertions.assertTrue(previous.isPresent());
    Assertions.assertEquals(UnitConstant.MILLENNIUM, previous.get());

    Assertions.assertTrue(!timeUnitGroup.lower(UnitConstant.SECOND).isPresent());
  }

  @Test
  void next() {
    Optional<Unit> next = timeUnitGroup.higher(UnitConstant.SECOND);
    Assertions.assertTrue(next.isPresent());
    Assertions.assertEquals(UnitConstant.MINUTE, next.get());

    Assertions.assertTrue(!timeUnitGroup.higher(UnitConstant.ERA).isPresent());
  }

  @Test
  void first() {
    Unit first = timeUnitGroup.first();
    Assertions.assertEquals(UnitConstant.SECOND, first);
  }

  @Test
  void last() {
    Unit last = timeUnitGroup.last();
    Assertions.assertEquals(UnitConstant.ERA, last);
  }

  @Test
  void valid() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> UnitGroup.builder(Configuration.defaultInstance()::compare).unit(UnitConstant.GRAM)
            .unit(UnitConstant.SECOND)
            .build());
  }
}