package org.caotc.unit4j.core.unit;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class CompositePrefixUnitTest {

  Configuration configuration = Configuration.defaultInstance();

  @Test
  void rebase() {
      Unit actual = CompositeStandardUnit
              .builder().componentToExponent(Units.NEWTON, 1)
              .componentToExponent(Units.METER, -2).build();
    log.debug("PASCAL_ENGLISH_NAME:{},rebase:{}", Units.PASCAL.rebase(), actual.rebase());
    Assertions.assertEquals(Units.PASCAL.rebase(), actual.rebase());
  }

  @Test
  void divide() {
      Unit pascalOther = CompositeStandardUnit
              .builder().componentToExponent(Units.NEWTON, 1)
              .componentToExponent(Units.METER, -2).build();
      Unit result = pascalOther.divide(Units.NEWTON);
    log.info("pascalOther:{},result:{}", pascalOther, result);
  }

  @Test
  void inverse() {
      Unit actual = Units.NEWTON.reciprocal();
      Unit expected = CompositeStandardUnit.builder()
              .componentToExponent(Units.NEWTON, -1)
              .build();
      Assertions.assertEquals(expected, actual);
  }

  @Test
  void compareTo() {
    Assertions.assertTrue(configuration.compare(Units.KILOGRAM, Units.GRAM) > 0);
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> {
          int i = configuration.compare(Units.KILOGRAM, Units.SECOND);
        });
  }

  @Test
  void valid() {
      //todo Unit build 自动判断返回UnitConstant.SECOND本身
//    Assertions.assertThrows(IllegalArgumentException.class,
//        () -> CompositeStandardUnit
//                .builder().componentToExponent(UnitConstant.SECOND, 1)
//            .build());
  }

  @Test
  void sameTypeCompent() {
      Unit compositePrefixUnit = CompositeStandardUnit
              .builder().componentToExponent(Units.SECOND, 1)
              .componentToExponent(Units.MINUTE, 1)
              .build();
    log.info("unit:{}", compositePrefixUnit);
    log.info("type:{}", compositePrefixUnit.type().id());
  }
}