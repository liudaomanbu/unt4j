package org.caotc.unit4j.core;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.CompositeStandardUnit;
import org.caotc.unit4j.core.unit.UnitConstant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@Slf4j
class QuantityTest {

  private Configuration configuration = Configuration.defaultInstance();

  @Test
  void convertTo() {
    Quantity quantity = Quantity.create(BigDecimal.TEN, UnitConstant.KILOGRAM);
    Quantity expected = Quantity.create(BigDecimal.valueOf(10000), UnitConstant.GRAM);
    Assertions
            .assertEquals(0, configuration.compare(quantity.convertTo(UnitConstant.GRAM), expected));
  }

  @Test
  void add() {
    Quantity quantity = Quantity.create(BigDecimal.TEN, UnitConstant.KILOGRAM);
    Quantity augend = Quantity.create(BigDecimal.TEN, UnitConstant.GRAM);
    Quantity result = quantity.add(augend);
    log.debug("{}", result);
    Quantity expected = Quantity.create(new BigDecimal("10.010"), UnitConstant.KILOGRAM);
    Assertions.assertEquals(0, configuration.compare(expected, result));

    expected = Quantity.create(new BigDecimal("10.01"), UnitConstant.KILOGRAM);
    Assertions.assertEquals(0, configuration.compare(expected, result));
  }

  @Test
  void subtract() {
    Quantity quantity = Quantity.create(BigDecimal.TEN, UnitConstant.KILOGRAM);
    Quantity subtrahend = Quantity.create(BigDecimal.TEN, UnitConstant.GRAM);
    Quantity result = quantity.subtract(subtrahend);
    log.debug("{}", result);
    Quantity expected = Quantity.create(new BigDecimal("9.990"), UnitConstant.KILOGRAM);
    Assertions.assertEquals(0, configuration.compare(expected, result));

    expected = Quantity.create(new BigDecimal("9.99"), UnitConstant.KILOGRAM);
    Assertions.assertEquals(0, configuration.compare(expected, result));
  }

  @Test
  void multiply() {
    Quantity quantity = Quantity.create(BigDecimal.TEN, UnitConstant.KILOGRAM);
    Quantity multiplicand = Quantity.create(BigDecimal.TEN, UnitConstant.GRAM);
    Quantity result = quantity.multiply(multiplicand);
    log.debug("{}", result);
    Quantity expected = Quantity
            .create(BigDecimal.valueOf(100), CompositeStandardUnit.builder()
                    .componentToExponent(UnitConstant.GRAM, 2).build());
    Assertions.assertEquals(0, configuration.compare(expected, result));
  }

  @Test
  void divide() {
    Quantity quantity = Quantity.create(BigDecimal.ONE, UnitConstant.KILOGRAM);
    Quantity divisor = Quantity.create(BigDecimal.TEN, UnitConstant.GRAM);
    Quantity result = quantity.divide(divisor);
    log.debug("{}", result);
  }

}