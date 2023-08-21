package org.caotc.unit4j.core.unit;

import org.caotc.unit4j.core.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class CompositePrefixUnitGroupTest {

  private UnitGroup timeUnitGroup = UnitGroup.builder().units(Units.DEFAULT_TIME_STANDARD_UNITS).configuration(Configuration.defaultInstance()).build();

  @Test
  void previous() {
    Optional<Unit> previous = timeUnitGroup.lower(Units.ERA);
    Assertions.assertTrue(previous.isPresent());
    Assertions.assertEquals(Units.MILLENNIUM, previous.get());

    Assertions.assertTrue(!timeUnitGroup.lower(Units.SECOND).isPresent());
  }

  @Test
  void next() {
    Optional<Unit> next = timeUnitGroup.higher(Units.SECOND);
    Assertions.assertTrue(next.isPresent());
    Assertions.assertEquals(Units.MINUTE, next.get());

    Assertions.assertTrue(!timeUnitGroup.higher(Units.ERA).isPresent());
  }

  @Test
  void first() {
    Unit first = timeUnitGroup.first();
    Assertions.assertEquals(Units.SECOND, first);
  }

  @Test
  void last() {
    Unit last = timeUnitGroup.last();
    Assertions.assertEquals(Units.ERA, last);
  }

  @Test
  void valid() {
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> UnitGroup.builder().configuration(Configuration.defaultInstance()).unit(Units.GRAM)
                    .unit(Units.SECOND)
                    .build());
  }
}