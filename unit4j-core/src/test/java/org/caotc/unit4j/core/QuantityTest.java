package org.caotc.unit4j.core;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.Units;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@Slf4j
class QuantityTest {

  private Configuration configuration = Configuration.defaultInstance();

  @Test
  void convertTo() {
    Quantity quantity = Quantity.create(BigDecimal.TEN, Units.KILOGRAM);
    Quantity expected = Quantity.create(BigDecimal.valueOf(10000), Units.GRAM);
    Assertions
            .assertEquals(0, configuration.compare(quantity.convertTo(Units.GRAM), expected));
  }

  @Test
  void add() {
    Quantity quantity = Quantity.create(BigDecimal.TEN, Units.KILOGRAM);
    Quantity augend = Quantity.create(BigDecimal.TEN, Units.GRAM);
    Quantity result = quantity.add(augend);
    log.debug("{}", result);
    Quantity expected = Quantity.create(new BigDecimal("10.010"), Units.KILOGRAM);
    Assertions.assertEquals(0, configuration.compare(expected, result));

    expected = Quantity.create(new BigDecimal("10.01"), Units.KILOGRAM);
    Assertions.assertEquals(0, configuration.compare(expected, result));
  }

  @Test
  void subtract() {
    Quantity quantity = Quantity.create(BigDecimal.TEN, Units.KILOGRAM);
    Quantity subtrahend = Quantity.create(BigDecimal.TEN, Units.GRAM);
    Quantity result = quantity.subtract(subtrahend);
    log.debug("{}", result);
    Quantity expected = Quantity.create(new BigDecimal("9.990"), Units.KILOGRAM);
    Assertions.assertEquals(0, configuration.compare(expected, result));

    expected = Quantity.create(new BigDecimal("9.99"), Units.KILOGRAM);
    Assertions.assertEquals(0, configuration.compare(expected, result));
  }

  @Test
  void multiply() {
    Quantity quantity = Quantity.create(BigDecimal.TEN, Units.KILOGRAM);
    Quantity multiplicand = Quantity.create(BigDecimal.TEN, Units.GRAM);
    Quantity result = quantity.multiply(multiplicand);
    log.debug("{}", result);
    Quantity expected = Quantity
            .create(BigDecimal.valueOf(100), Unit.builder()
                    .componentToExponent(Units.KILOGRAM, 1)
                    .componentToExponent(Units.GRAM, 1)
                    .build());
    Assertions.assertEquals(expected, result);
  }

  @Test
  void divide() {
    Quantity quantity = Quantity.create(BigDecimal.ONE, Units.KILOGRAM);
    Quantity divisor = Quantity.create(BigDecimal.TEN, Units.GRAM);
    Quantity result = quantity.divide(divisor);
    log.debug("{}", result);
  }

}