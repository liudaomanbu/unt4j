/*
 * Copyright (C) 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.caotc.unit4j.core;

import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.convert.UnitConvertConfig;
import org.caotc.unit4j.core.math.number.BigDecimal;
import org.caotc.unit4j.core.unit.BaseStandardUnit;
import org.caotc.unit4j.core.unit.Prefix;
import org.caotc.unit4j.core.unit.PrefixUnit;
import org.caotc.unit4j.core.unit.StandardUnit;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.caotc.unit4j.core.unit.Units;
import org.caotc.unit4j.core.unit.type.UnitType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.Random;

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
  void registerAlias() {
    Configuration configuration = Configuration.of();
    Random random = new Random();
    Prefix prefix = Prefix.create(10, random.nextInt());
    Alias prefixAlias = Alias.create(Alias.Type.create(random.nextInt() + ""), random.nextInt() + "");
    StandardUnit standardUnit = Unit.of(random.nextInt() + "", UnitTypes.LENGTH);
    Alias standardUnitAlias = prefixAlias.withValue(random.nextInt() + "");
    PrefixUnit unit = standardUnit.addPrefix(prefix);
    Alias compositeUnitAlias = prefixAlias.withValue(PrefixUnit.composite(prefixAlias.value(), standardUnitAlias.value()));

    Assertions.assertTrue(configuration.aliases(prefix).isEmpty());
    Assertions.assertFalse(configuration.findPrefix(prefixAlias).isPresent());
    Assertions.assertTrue(configuration.aliases(standardUnit).isEmpty());
    Assertions.assertFalse(configuration.findUnit(standardUnitAlias).isPresent());
    Assertions.assertTrue(configuration.aliases(unit).isEmpty());
    Assertions.assertFalse(configuration.findUnit(compositeUnitAlias).isPresent());

    configuration.registerAlias(standardUnit, standardUnitAlias);
    ImmutableSet<Alias> standardUnitAliases = configuration.aliases(standardUnit);
    log.debug("standardUnitAliases:{}", standardUnitAliases);
    Assertions.assertEquals(ImmutableSet.of(standardUnitAlias), standardUnitAliases);
    Optional<? extends Unit> standardUnitOptional = configuration.findUnit(standardUnitAlias);
    log.debug("standardUnitOptional:{}", standardUnitOptional);
    Assertions.assertTrue(standardUnitOptional.isPresent());
    Assertions.assertEquals(standardUnit, standardUnitOptional.get());

    configuration.registerAlias(prefix, prefixAlias);

    ImmutableSet<Alias> prefixAliases = configuration.aliases(prefix);
    log.debug("prefixAliases:{}", prefixAliases);
    Assertions.assertEquals(ImmutableSet.of(prefixAlias), prefixAliases);
    Optional<Prefix> prefixOptional = configuration.findPrefix(prefixAlias);
    log.debug("prefixOptional:{}", prefixOptional);
    Assertions.assertTrue(prefixOptional.isPresent());
    Assertions.assertEquals(prefix, prefixOptional.get());
    ImmutableSet<Alias> unitAliases = configuration.aliases(unit);
    log.debug("unitAliases:{}", unitAliases);
    Assertions.assertEquals(ImmutableSet.of(compositeUnitAlias), unitAliases);
    Optional<? extends Unit> unitOptional = configuration.findUnit(compositeUnitAlias);
    log.debug("unitOptional:{}", unitOptional);
    Assertions.assertTrue(unitOptional.isPresent());
    Assertions.assertEquals(unit, unitOptional.get());

    Alias unitAlias = Alias.create(Alias.Type.create(random.nextInt() + ""), random.nextInt() + "");
    configuration.registerAlias(unit, unitAlias);
    unitAliases = configuration.aliases(unit);
    log.debug("unitAliases:{}", unitAliases);
    Assertions.assertEquals(ImmutableSet.of(compositeUnitAlias, unitAlias), unitAliases);
    unitOptional = configuration.findUnit(unitAlias);
    log.debug("unitOptional:{}", unitOptional);
    Assertions.assertTrue(unitOptional.isPresent());
    Assertions.assertEquals(unit, unitOptional.get());
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#getConvertConfigArguments")
  void getConvertConfig(Unit sourceUnit, Unit targetUnit, UnitConvertConfig unitConvertConfig) {
    UnitConvertConfig result = configuration.getConvertConfig(sourceUnit, targetUnit);
    log.debug("sourceUnit:{},targetUnit:{},result:{}", sourceUnit.id(), targetUnit.id(), result);
    Assertions.assertEquals(0, result.ratio().compareTo(unitConvertConfig.ratio()));
  }

  @ParameterizedTest
  @MethodSource("org.caotc.unit4j.core.Provider#sourceUnitAndErrorTargetUnits")
  void getConvertConfigError(Unit sourceUnit, Unit targetUnit) {
    log.debug("sourceUnit:{},targetUnit:{}", sourceUnit.id(), targetUnit.id());
    Assertions.assertThrows(IllegalArgumentException.class,
            () -> configuration.getConvertConfig(sourceUnit, targetUnit));
  }

  @Test
  void registerConvertConfig() {
    BaseStandardUnit testLength = BaseStandardUnit
            .create("testLength", UnitTypes.LENGTH);
    UnitConvertConfig expected = UnitConvertConfig.create(
            BigDecimal.valueOf("3.14"));
    configuration
        .register(testLength, Units.METER, expected);
    UnitConvertConfig actual = configuration
        .getConvertConfig(testLength, Units.METER);
    log.debug("{}", actual);
    Assertions.assertEquals(expected, actual);
    actual = configuration
        .getConvertConfig(testLength, Units.METER);
    log.debug("{}", actual);
    Assertions.assertEquals(0, expected.ratio().compareTo(actual.ratio()));

    Assertions.assertNotNull(configuration.getConvertConfig(Units.METER, testLength));
    Assertions
        .assertNotNull(configuration.getConvertConfig(testLength, Units.METER));
  }

  @Test
  void getUnitGroup() {
    Assertions.assertNotNull(configuration.getUnitGroup(Units.HOUR));
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
            configuration.compare(Quantity.create(java.math.BigDecimal.TEN, Units.KILOGRAM),
                    Quantity.create(
                            java.math.BigDecimal.ONE, Units.KILOGRAM)) > 0);

    Assertions.assertTrue(
            configuration.compare(Quantity.create(java.math.BigDecimal.ONE, Units.KILOGRAM),
                    Quantity.create(
                            java.math.BigDecimal.TEN, Units.GRAM)) > 0);

    Assertions.assertEquals(0,
            configuration.compare(Quantity.create(java.math.BigDecimal.ONE, Units.KILOGRAM),
                    Quantity.create(
                            java.math.BigDecimal.valueOf(1000), Units.GRAM)));

    Assertions.assertThrows(IllegalArgumentException.class,
            () -> {
              int i = configuration
                      .compare(Quantity.create(java.math.BigDecimal.ONE, Units.KILOGRAM),
                              Quantity.create(
                                      java.math.BigDecimal.valueOf(1000), Units.NEWTON));
            });
  }

  @Test
  void test() {
//    Expression e = new ExpressionBuilder("3 * sin(y) - 2 / (x - 2)")
//            .variables("x", "y")
//            .build()
//            .setVariable("x", 2.3)
//            .setVariable("y", 3.14);
//    double result = e.evaluate();
//    log.info("result:{}",result);
  }

  @Test
  void test1() {
//    License.iConfirmCommercialUse("aaaaa");
//    Expression eh = new Expression("5^2 * 7^3 * 11^1 * 67^1 * 49201^1");
//    Expression ew = new Expression("71^1 * 218549^1 * 6195547^1");
//    String h = mXparser.numberToAsciiString(eh.calculate());
//    String w = mXparser.numberToAsciiString(ew.calculate());
//    mXparser.consolePrintln(h + " " + w);
  }

  @Test
  void test2() {
//    try {
//      ExprEvaluator util = new ExprEvaluator(false, (short) 100);
//
//      // Convert an expression to the internal Java form:
//      // Note: single character identifiers are case sensitive
//      // (the "D()" function identifier must be written as upper case
//      // character)
//      String javaForm = util.toJavaForm("D(sin(x)*cos(x),x)");
//      // prints: D(Times(Sin(x),Cos(x)),x)
//      System.out.println("Out[1]: " + javaForm.toString());
//
//      // Use the Java form to create an expression with F.* static
//      // methods:
//      ISymbol x = F.Dummy("x");
//      IAST function = F.D(F.Times(F.Sin(x), F.Cos(x)), x);
//      IExpr result = util.eval(function);
//      // print: Cos(x)^2-Sin(x)^2
//      System.out.println("Out[2]: " + result.toString());
//
//      // Note "diff" is an alias for the "D" function
//      result = util.eval("diff(sin(x)*cos(x),x)");
//      // print: Cos(x)^2-Sin(x)^2
//      System.out.println("Out[3]: " + result.toString());
//
//      // evaluate the last result (% contains "last answer")
//      result = util.eval("%+cos(x)^2");
//      // print: 2*Cos(x)^2-Sin(x)^2
//      System.out.println("Out[4]: " + result.toString());
//
//      // evaluate an Integrate[] expression
//      result = util.eval("integrate(sin(x)^5,x)");
//      // print: 2/3*Cos(x)^3-1/5*Cos(x)^5-Cos(x)
//      System.out.println("Out[5]: " + result.toString());
//
//      // set the value of a variable "a" to 10
//      result = util.eval("a=10");
//      // print: 10
//      System.out.println("Out[6]: " + result.toString());
//
//      // do a calculation with variable "a"
//      result = util.eval("a*3+b");
//      // print: 30+b
//      System.out.println("Out[7]: " + result.toString());
//
//      // Do a calculation in "numeric mode" with the N() function
//      // Note: single character identifiers are case sensistive
//      // (the "N()" function identifier must be written as upper case
//      // character)
//      result = util.eval("N(sinh(5))");
//      // print: 74.20321057778875
//      System.out.println("Out[8]: " + result.toString());
//
//      // define a function with a recursive factorial function definition.
//      // Note: fac(0) is the stop condition.
//      result = util.eval("fac(x_Integer):=x*fac(x-1);fac(0)=1");
//      // now calculate factorial of 10:
//      result = util.eval("fac(10)");
//      // print: 3628800
//      System.out.println("Out[9]: " + result.toString());
//
//      function = F.Function(F.Divide(F.Gamma(F.Plus(F.C1, F.Slot1)), F.Gamma(F.Plus(F.C1, F.Slot2))));
//      // eval function ( Gamma(1+#1)/Gamma(1+#2) ) & [23,20]
//      result = util.evalFunction(function, "23", "20");
//      // print: 10626
//      System.out.println("Out[10]: " + result.toString());
//    } catch (SyntaxError e) {
//      // catch Symja parser errors here
//      System.out.println(e.getMessage());
//    } catch (MathException me) {
//      // catch Symja math errors here
//      System.out.println(me.getMessage());
//    } catch (final Exception ex) {
//      System.out.println(ex.getMessage());
//    } catch (final StackOverflowError soe) {
//      System.out.println(soe.getMessage());
//    } catch (final OutOfMemoryError oome) {
//      System.out.println(oome.getMessage());
//    }
  }

//  @Test
//  void testEvalEx() throws EvaluationException, ParseException {
//    Expression expression = new Expression("(1.8 * C + 32)^2");
//
//    EvaluationValue result = expression.evaluate();
//
//    System.out.println(result.getNumberValue()); // prints 1.25
//  }
}
