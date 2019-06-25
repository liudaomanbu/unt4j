package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.caotc.unit4j.core.unit.type.BaseUnitType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class UnitTest {

  @Test
  void typeToDimensionElementMap() {
    Assertions.assertEquals(
        ImmutableMap.of(BaseUnitType.MASS, Dimension.create(UnitConstant.GRAM, 1)),
        UnitConstant.GRAM.typeToDimensionElementMap());
  }

  @Test
  void multiplyUnitComponent() {
    CompositeStandardUnit gram = CompositeStandardUnit
        .builder().unitComponentToExponent(UnitConstant.GRAM, 2).build();
    Unit multiply = UnitConstant.GRAM.multiply(UnitConstant.GRAM);
    Assertions.assertEquals(gram.unitComponentToExponents(), multiply.unitComponentToExponents());
    Assertions.assertEquals(0, gram.prefix().compareTo(multiply.prefix()));
    Assertions.assertEquals(gram, multiply);
  }

  @Test
  void rebase() {
    Assertions.assertEquals(UnitConstant.GRAM, UnitConstant.GRAM.rebase());
  }

  @Test
  void unitComponentToExponents() {
    ImmutableMap<Unit, Integer> unitComponentIntegerImmutableMap = UnitConstant.GRAM
        .unitComponentToExponents();
    Assertions
        .assertEquals(ImmutableMap.of(UnitConstant.GRAM, 1), unitComponentIntegerImmutableMap);
  }

  @Test
  void pow() {
    Unit pow = UnitConstant.GRAM.power(3);
    Assertions
        .assertEquals(
            CompositeStandardUnit.builder().unitComponentToExponent(UnitConstant.GRAM, 3)
                .build(), pow);
  }

  @Test
  void id() {
    Unit unit = CompositeStandardUnit.builder()
        .unitComponentToExponent(UnitConstant.WATT.addPrefix(Prefix.CENTI), 2)
        .unitComponentToExponent(UnitConstant.NEWTON, 3).build()
        .addPrefix(Prefix.HECTO);
    log.info("id:{}", unit.id());
  }
}