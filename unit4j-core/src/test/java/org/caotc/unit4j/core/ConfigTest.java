package org.caotc.unit4j.core;

import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.convert.UnitConvertConfig;
import org.caotc.unit4j.core.math.number.BigDecimal;
import org.caotc.unit4j.core.unit.BaseStandardUnit;
import org.caotc.unit4j.core.unit.CompositePrefixUnit;
import org.caotc.unit4j.core.unit.Prefix;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.UnitConstant;
import org.caotc.unit4j.core.unit.UnitGroup;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class ConfigTest {
  Configuration configuration = Configuration.defaultInstance();

  @Test
  void getConvertConfig() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> configuration.getConvertConfig(UnitConstant.GRAM,
            UnitConstant.SECOND));

    UnitConvertConfig config = this.configuration
        .getConvertConfig(UnitConstant.GRAM, UnitConstant.KILOGRAM);
    log.debug("{}", config);
    Assertions.assertEquals(0, config.ratio().compareTo(BigDecimal.valueOf("0.001")));

    config = this.configuration.getConvertConfig(UnitConstant.TONNE, UnitConstant.KILOGRAM);
    log.debug("{}", config);
    Assertions.assertEquals(0, config.ratio().compareTo(BigDecimal.valueOf(1000)));

    config = this.configuration
        .getConvertConfig(UnitConstant.WATT,
            CompositePrefixUnit.builder().prefix(
                Prefix.DECA).standardUnit(UnitConstant.WATT)
                .build());
    log.debug("{}", config);
    Assertions.assertEquals(0,
        Prefix.DECA.convertFromStandardUnitConfig().ratio().compareTo(config.ratio()));

    Unit compositePrefixUnit1 = Unit
            .builder().componentToExponent(UnitConstant.KILOGRAM, 1)
            .componentToExponent(UnitConstant.METER.addPrefix(Prefix.HECTO), 2)
            .componentToExponent(UnitConstant.SECOND, -3).build();
    Unit compositePrefixUnit2 = Unit
            .builder().componentToExponent(UnitConstant.GRAM, 1)
            .componentToExponent(UnitConstant.METER, 2)
            .componentToExponent(UnitConstant.SECOND.addPrefix(Prefix.DECA), -3).build();
    config = this.configuration.getConvertConfig(compositePrefixUnit1, compositePrefixUnit2);
    log.debug("{}", config);
    Assertions
        .assertEquals(0, config.ratio().compareTo(BigDecimal.valueOf("10000000000")));
    Assertions.assertEquals(0, config.zeroDifference().compareTo(BigDecimal.valueOf("0")));
  }

  @Test
  void registerConvertConfig() {
    BaseStandardUnit testLength = BaseStandardUnit
            .create("testLength", UnitTypes.LENGTH);
    UnitConvertConfig expected = UnitConvertConfig.create(
            BigDecimal.valueOf("3.14"));
    configuration
        .register(testLength, UnitConstant.METER, expected);
    UnitConvertConfig actual = configuration
        .getConvertConfig(testLength, UnitConstant.METER);
    log.debug("{}", actual);
    Assertions.assertEquals(expected, actual);
    actual = configuration
        .getConvertConfig(testLength, UnitConstant.METER);
    log.debug("{}", actual);
    Assertions.assertEquals(0, expected.ratio().compareTo(actual.ratio()));

    Assertions.assertNotNull(configuration.getConvertConfig(UnitConstant.METER, testLength));
    Assertions
        .assertNotNull(configuration.getConvertConfig(testLength, UnitConstant.METER));
  }


  @Test
  void getTargetUnit() {
    Assertions.assertThrows(IllegalArgumentException.class, () ->
            configuration.register(
                    UnitGroup.builder(Configuration.defaultInstance()::compare).unit(UnitConstant.MINUTE)
                            .unit(
                                    UnitConstant.SECOND).build()));
    Quantity quantity = Quantity.create(BigDecimal.valueOf("0.26"), UnitConstant.MINUTE);
    Unit targetCompositePrefixUnit = configuration.getTargetUnit(quantity);
    Assertions.assertEquals(UnitConstant.SECOND, targetCompositePrefixUnit);

    targetCompositePrefixUnit = configuration.getTargetUnit(ImmutableSet.of(quantity));
    Assertions.assertEquals(UnitConstant.SECOND, targetCompositePrefixUnit);
  }

  @Test
  void getUnitGroup() {
    Assertions.assertNotNull(configuration.getUnitGroup(UnitConstant.HOUR));
  }

  @Test
  void setAlias() {

  }

  @Test
  void setAbbreviation() {

  }

  @Test
  void setSymbol() {

  }

  @Test
  void setName() {

  }

  @Test
  void register() {

  }

  @Test
  void compareTo() {
    Assertions.assertTrue(
            configuration.compare(Quantity.create(java.math.BigDecimal.TEN, UnitConstant.KILOGRAM),
                    Quantity.create(
                            java.math.BigDecimal.ONE, UnitConstant.KILOGRAM)) > 0);

    Assertions.assertTrue(
            configuration.compare(Quantity.create(java.math.BigDecimal.ONE, UnitConstant.KILOGRAM),
                    Quantity.create(
                            java.math.BigDecimal.TEN, UnitConstant.GRAM)) > 0);

    Assertions.assertEquals(0,
            configuration.compare(Quantity.create(java.math.BigDecimal.ONE, UnitConstant.KILOGRAM),
                    Quantity.create(
                            java.math.BigDecimal.valueOf(1000), UnitConstant.GRAM)));

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> {
          int i = configuration
                  .compare(Quantity.create(java.math.BigDecimal.ONE, UnitConstant.KILOGRAM),
                          Quantity.create(
                                  java.math.BigDecimal.valueOf(1000), UnitConstant.NEWTON));
        });
  }
}
