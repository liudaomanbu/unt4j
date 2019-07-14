package org.caotc.unit4j.core.common.util;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.caotc.unit4j.core.unit.CompositeStandardUnit;
import org.caotc.unit4j.core.unit.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class UnitUtilTest {

  @Test
  void parseUnit() {
    Unit second = UnitUtil.parseUnit("SECOND");
    log.debug("parse SECOND:{}", second);
    Unit unit = UnitUtil.parseUnit("METER/DAY");
    log.debug("parse METER/DAY:{}", unit);
    Assertions.assertEquals(CompositeStandardUnit
        .builder().unitComponentToExponent(UnitConstant.METER, 1)
        .unitComponentToExponent(UnitConstant.DAY, -1).build(), unit);
  }

  @Test
  void convertUnit() {
  }

  @Test
  void convertUnit1() {
  }

  @Test
  void convertUnit2() {
  }

  @Test
  void convertUnit3() {
  }

  @Test
  void convertUnit4() {
  }

  @Test
  void convertUnit5() {
  }

  @Test
  void convertUnit6() {
  }

  @Test
  void convertUnit7() {
  }

  @Test
  void convertUnit8() {
  }

  @Test
  void convertUnit9() {
  }

  @Test
  void convertUnit10() {
  }

  @Test
  void convertUnit11() {
  }

  @Test
  void convertUnit12() {
  }

  @Test
  void convertUnit13() {
  }

  @Test
  void convertUnit14() {
  }
}