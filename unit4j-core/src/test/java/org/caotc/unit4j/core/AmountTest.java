package org.caotc.unit4j.core;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.caotc.unit4j.core.unit.CompositeStandardUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class AmountTest {

  private Configuration configuration = Configuration.defaultInstance();

  @Test
  void convertTo() {
    Amount amount = Amount.create(BigDecimal.TEN, UnitConstant.KILOGRAM);
    Amount expected = Amount.create(BigDecimal.valueOf(10000), UnitConstant.GRAM);
    Assertions
        .assertEquals(0, configuration.compare(amount.convertTo(UnitConstant.GRAM), expected));
  }

  @Test
  void add() {
    Amount amount = Amount.create(BigDecimal.TEN, UnitConstant.KILOGRAM);
    Amount augend = Amount.create(BigDecimal.TEN, UnitConstant.GRAM);
    Amount result = amount.add(augend);
    log.debug("{}", result);
    Amount expected = Amount.create(new BigDecimal("10.010"), UnitConstant.KILOGRAM);
    Assertions.assertEquals(0, configuration.compare(expected, result));

    expected = Amount.create(new BigDecimal("10.01"), UnitConstant.KILOGRAM);
    Assertions.assertEquals(0, configuration.compare(expected, result));
  }

  @Test
  void subtract() {
    Amount amount = Amount.create(BigDecimal.TEN, UnitConstant.KILOGRAM);
    Amount subtrahend = Amount.create(BigDecimal.TEN, UnitConstant.GRAM);
    Amount result = amount.subtract(subtrahend);
    log.debug("{}", result);
    Amount expected = Amount.create(new BigDecimal("9.990"), UnitConstant.KILOGRAM);
    Assertions.assertEquals(0, configuration.compare(expected, result));

    expected = Amount.create(new BigDecimal("9.99"), UnitConstant.KILOGRAM);
    Assertions.assertEquals(0, configuration.compare(expected, result));
  }

  @Test
  void multiply() {
    Amount amount = Amount.create(BigDecimal.TEN, UnitConstant.KILOGRAM);
    Amount multiplicand = Amount.create(BigDecimal.TEN, UnitConstant.GRAM);
    Amount result = amount.multiply(multiplicand);
    log.debug("{}", result);
    Amount expected = Amount
        .create(BigDecimal.valueOf(100), CompositeStandardUnit.builder()
            .unitComponentToExponent(UnitConstant.GRAM, 2).build());
    Assertions.assertEquals(0, configuration.compare(expected, result));
  }

  @Test
  void divide() {
    Amount amount = Amount.create(BigDecimal.ONE, UnitConstant.KILOGRAM);
    Amount divisor = Amount.create(BigDecimal.TEN, UnitConstant.GRAM);
    Amount result = amount.divide(divisor);
    log.debug("{}", result);
  }

}