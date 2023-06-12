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
import org.caotc.unit4j.core.unit.type.UnitType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;

@Slf4j
class ConfigurationTest {
  Configuration configuration = Configuration.defaultInstance();

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#idAndUnits")
  void findUnit(String id, Unit unit) {
    Optional<Unit> result = Configuration.findUnit(id);
    log.debug("id:{},result:{}", id, result);
    Assertions.assertTrue(result.isPresent());
    Assertions.assertEquals(unit, result.get());
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#errorUnitIds")
  void findUnitError(String id) {
    Optional<Unit> result = Configuration.findUnit(id);
    log.debug("id:{},result:{}", id, result);
    Assertions.assertFalse(result.isPresent());
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#idAndUnits")
  void findUnitExact(String id, Unit unit) {
    Unit result = Configuration.findUnitExact(id);
    log.debug("id:{},result:{}", id, result.id());
    Assertions.assertEquals(unit, result);
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#errorUnitIds")
  void findUnitExactError(String id) {
    log.debug("id:{}", id);
    Assertions.assertThrows(IllegalArgumentException.class, () -> Configuration.findUnitExact(id));
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#unitTypeAndAliasSets")
  void aliases(UnitType unitType, ImmutableSet<Alias> aliases) {
    ImmutableSet<Alias> result = configuration.aliases(unitType);
    log.debug("unitType:{},result:{}", unitType.id(), result);
    Assertions.assertEquals(aliases, result);
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#prefixAndAliasSets")
  void aliases(Prefix prefix, ImmutableSet<Alias> aliases) {
    ImmutableSet<Alias> result = configuration.aliases(prefix);
    log.debug("prefix:{},result:{}", prefix, result);
    Assertions.assertEquals(aliases, result);
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#unitAndAliasSets")
  void aliases(Unit unit, ImmutableSet<Alias> aliases) {
    ImmutableSet<Alias> result = configuration.aliases(unit);
    log.debug("unit:{},result:{}", unit.id(), result);
    Assertions.assertEquals(aliases, result);
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#unitTypeAndAliasTypeAndAliasSets")
  void aliases(UnitType unitType, Alias.Type type, ImmutableSet<Alias> aliases) {
    ImmutableSet<Alias> result = configuration.aliases(unitType, type);
    log.debug("unitType:{},type:{},result:{}", unitType.id(), type, result);
    Assertions.assertEquals(aliases, result);
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#prefixAndAliasTypeAndAliasSets")
  void aliases(Prefix prefix, Alias.Type type, ImmutableSet<Alias> aliases) {
    ImmutableSet<Alias> result = configuration.aliases(prefix, type);
    log.debug("prefix:{},type:{},result:{}", prefix, type, result);
    Assertions.assertEquals(aliases, result);
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#unitAndAliasTypeAndAliasSets")
  void aliases(Unit unit, Alias.Type type, ImmutableSet<Alias> aliases) {
    ImmutableSet<Alias> result = configuration.aliases(unit, type);
    log.debug("unit:{},type:{},result:{}", unit.id(), type, result);
    Assertions.assertEquals(aliases, result);
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#aliasAndUnitTypes")
  void findUnitType(Alias alias, UnitType unitType) {
    Optional<UnitType> result = configuration.findUnitType(alias);
    log.debug("alias:{},result:{}", alias, result);
    Assertions.assertTrue(result.isPresent());
    Assertions.assertEquals(unitType, result.get());
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#errorUnitTypeAliases")
  void findUnitTypeError(Alias alias) {
    Optional<UnitType> result = configuration.findUnitType(alias);
    log.debug("alias:{},result:{}", alias, result);
    Assertions.assertFalse(result.isPresent());
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#aliasAndPrefixes")
  void findPrefix(Alias alias, Prefix prefix) {
    Optional<Prefix> result = configuration.findPrefix(alias);
    log.debug("alias:{},result:{}", alias, result);
    Assertions.assertTrue(result.isPresent());
    Assertions.assertEquals(prefix, result.get());
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#errorPrefixAliases")
  void findPrefixError(Alias alias) {
    Optional<Prefix> result = configuration.findPrefix(alias);
    log.debug("alias:{},result:{}", alias, result);
    Assertions.assertFalse(result.isPresent());
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#aliasAndUnits")
  void findUnit(Alias alias, Unit unit) {
    Optional<? extends Unit> result = configuration.findUnit(alias);
    log.debug("alias:{},result:{}", alias, result);
    Assertions.assertTrue(result.isPresent());
    Assertions.assertEquals(unit, result.get());
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#errorUnitAliases")
  void findUnitError(Alias alias) {
    Optional<? extends Unit> result = configuration.findUnit(alias);
    log.debug("alias:{},result:{}", alias, result);
    Assertions.assertFalse(result.isPresent());
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#aliasStringAndUnitTypes")
  void unitTypes(String alias, UnitType unitType) {
    ImmutableSet<UnitType> result = configuration.unitTypes(alias);
    log.debug("alias:{},result:{}", alias, result);
    Assertions.assertEquals(ImmutableSet.of(unitType), result);
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#aliasStringAndPrefixes")
  void prefixes(String alias, Prefix prefix) {
    ImmutableSet<Prefix> result = configuration.prefixes(alias);
    log.debug("alias:{},result:{}", alias, result);
    Assertions.assertEquals(ImmutableSet.of(prefix), result);
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#aliasStringAndUnits")
  void units(String alias, Unit unit) {
    ImmutableSet<Unit> result = configuration.units(alias);
    log.debug("alias:{},result:{}", alias, result);
    Assertions.assertEquals(ImmutableSet.of(unit), result);
  }

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