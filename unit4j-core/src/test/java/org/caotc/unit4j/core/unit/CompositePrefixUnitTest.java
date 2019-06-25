package org.caotc.unit4j.core.unit;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class CompositePrefixUnitTest {

  Configuration configuration = Configuration.defaultInstance();

  @Test
  void multiplyUnitComponent() {
    Unit result = UnitConstant.NEWTON
        .multiply(
            CompositeStandardUnit.builder().unitComponentToExponent(UnitConstant.METER, -2)
                .build());
    log.debug("expected:{},actual:{}", UnitConstant.PASCAL, result);
    Assertions.assertEquals(UnitConstant.PASCAL, result);
  }

  @Test
  void rebase() {
    Unit actual = CompositeStandardUnit
        .builder().unitComponentToExponent(UnitConstant.NEWTON, 1)
        .unitComponentToExponent(UnitConstant.METER, -2).build();
    log.debug("PASCAL_ENGLISH_NAME:{},rebase:{}", UnitConstant.PASCAL.rebase(), actual.rebase());
    Assertions.assertEquals(UnitConstant.PASCAL.rebase(), actual.rebase());
  }

  @Test
  void pow() {
    CompositeStandardUnit actual = UnitConstant.NEWTON.power(3);
    CompositeStandardUnit expected = CompositeStandardUnit.builder()
        .unitComponentToExponent(UnitConstant.KILOGRAM, 3)
        .unitComponentToExponent(UnitConstant.METER, 3)
        .unitComponentToExponent(UnitConstant.SECOND, -6)
        .build();
    Assertions.assertEquals(expected, actual);

    actual = UnitConstant.NEWTON.power(-3);
    expected = CompositeStandardUnit.builder()
        .unitComponentToExponent(UnitConstant.KILOGRAM, -3)
        .unitComponentToExponent(UnitConstant.METER, -3)
        .unitComponentToExponent(UnitConstant.SECOND, 6)
        .build();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void divide() {
    Unit actual = UnitConstant.PASCAL.divide(UnitConstant.NEWTON);
    CompositeStandardUnit expected = CompositeStandardUnit.builder()
        .unitComponentToExponent(UnitConstant.METER, -2).build();
    Assertions.assertEquals(expected, actual);

    CompositeStandardUnit pascalOther = CompositeStandardUnit
        .builder().unitComponentToExponent(UnitConstant.NEWTON, 1)
        .unitComponentToExponent(UnitConstant.METER, -2).build();
    Unit result = pascalOther.divide(UnitConstant.NEWTON);
    log.info("pascalOther:{},result:{}", pascalOther, result);
  }

  @Test
  void inverse() {
    CompositeStandardUnit actual = UnitConstant.NEWTON.inverse();
    CompositeStandardUnit expected = CompositeStandardUnit.builder()
        .unitComponentToExponent(UnitConstant.KILOGRAM, -1)
        .unitComponentToExponent(UnitConstant.METER, -1)
        .unitComponentToExponent(UnitConstant.SECOND, 2)
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
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> CompositeStandardUnit
            .builder().unitComponentToExponent(UnitConstant.SECOND, 1)
            .build());
  }

  @Test
  void sameTypeCompent() {
    CompositeStandardUnit compositePrefixUnit = CompositeStandardUnit
        .builder().unitComponentToExponent(UnitConstant.SECOND, 1)
        .unitComponentToExponent(UnitConstant.MINUTE, 1)
        .build();
    log.info("unit:{}", compositePrefixUnit);
    log.info("type:{}", compositePrefixUnit.type().id());
  }
}