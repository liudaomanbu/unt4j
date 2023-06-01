package org.caotc.unit4j.core.unit;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class CompositePrefixUnitTest {

  Configuration configuration = Configuration.defaultInstance();

  @Test
  void multiplyUnitComponent() {
    Unit result = UnitConstant.NEWTON
        .multiply(
            CompositeStandardUnit.builder().componentToExponent(UnitConstant.METER, -2)
                .build());
    log.debug("expected:{},actual:{}", UnitConstant.PASCAL, result);
    Assertions.assertEquals(UnitConstant.PASCAL, result);
  }

  @Test
  void rebase() {
      Unit actual = CompositeStandardUnit
              .builder().componentToExponent(UnitConstant.NEWTON, 1)
              .componentToExponent(UnitConstant.METER, -2).build();
    log.debug("PASCAL_ENGLISH_NAME:{},rebase:{}", UnitConstant.PASCAL.rebase(), actual.rebase());
    Assertions.assertEquals(UnitConstant.PASCAL.rebase(), actual.rebase());
  }

  @Test
  void divide() {
      Unit actual = UnitConstant.PASCAL.divide(UnitConstant.NEWTON);
      Unit expected = CompositeStandardUnit.builder()
              .componentToExponent(UnitConstant.METER, -2).build();
    Assertions.assertEquals(expected, actual);

      Unit pascalOther = CompositeStandardUnit
              .builder().componentToExponent(UnitConstant.NEWTON, 1)
              .componentToExponent(UnitConstant.METER, -2).build();
      Unit result = pascalOther.divide(UnitConstant.NEWTON);
    log.info("pascalOther:{},result:{}", pascalOther, result);
  }

  @Test
  void inverse() {
      Unit actual = UnitConstant.NEWTON.reciprocal();
      Unit expected = CompositeStandardUnit.builder()
              .componentToExponent(UnitConstant.KILOGRAM, -1)
              .componentToExponent(UnitConstant.METER, -1)
              .componentToExponent(UnitConstant.SECOND, 2)
              .build();
      Assertions.assertEquals(expected, actual);
  }

  @Test
  void compareTo() {
    Assertions.assertTrue(configuration.compare(UnitConstant.KILOGRAM, UnitConstant.GRAM) > 0);
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> {
          int i = configuration.compare(UnitConstant.KILOGRAM, UnitConstant.SECOND);
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
              .builder().componentToExponent(UnitConstant.SECOND, 1)
              .componentToExponent(UnitConstant.MINUTE, 1)
              .build();
    log.info("unit:{}", compositePrefixUnit);
    log.info("type:{}", compositePrefixUnit.type().id());
  }
}