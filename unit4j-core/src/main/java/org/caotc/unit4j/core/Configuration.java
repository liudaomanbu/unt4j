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

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Range;
import com.google.common.collect.Streams;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.fraction.BigFraction;
import org.caotc.unit4j.core.convert.AutoConverter;
import org.caotc.unit4j.core.convert.DefaultAutoConverter;
import org.caotc.unit4j.core.convert.QuantityChooser;
import org.caotc.unit4j.core.convert.UnitConvertConfig;
import org.caotc.unit4j.core.convert.ValueTargetRangeSingletonAutoConverter;
import org.caotc.unit4j.core.exception.ConfigurationNotFoundException;
import org.caotc.unit4j.core.exception.UnitNotFoundException;
import org.caotc.unit4j.core.math.number.Numbers;
import org.caotc.unit4j.core.unit.BaseStandardUnit;
import org.caotc.unit4j.core.unit.CompositePrefixUnit;
import org.caotc.unit4j.core.unit.CompositeStandardUnit;
import org.caotc.unit4j.core.unit.Prefix;
import org.caotc.unit4j.core.unit.PrefixUnit;
import org.caotc.unit4j.core.unit.Prefixes;
import org.caotc.unit4j.core.unit.StandardUnit;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.UnitGroup;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.caotc.unit4j.core.unit.Units;
import org.caotc.unit4j.core.unit.type.UnitType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 配置类 //TODO 两个单位之间存在不等值的转换config检查
 *
 * @author caotc
 * @date 2018-04-09
 * @implSpec
 * @implNote
 * @since 1.0.0
 **/
//todo toString方法处理和判断
//@Data(staticConstructor = "of")
@Getter
@FieldDefaults(makeFinal = false, level = AccessLevel.PRIVATE)
@Slf4j
public final class Configuration {
    /**
     * 默认实例的id
     */
    public static final String DEFAULT_ID = "_DEFAULT";
    /**
     * 默认的数学计算上下文对象
     */
    private static final MathContext DEFAULT_MATH_CONTEXT = MathContext.UNLIMITED;
    /**
     * 默认的单位自动转换器
     */
    private static final AutoConverter DEFAULT_AUTO_CONVERTER = DefaultAutoConverter.of(ValueTargetRangeSingletonAutoConverter.of(Range.closedOpen(Numbers.ONE, Numbers.valueOf(1000))), QuantityChooser.minQuantityChooser());
    //todo 应该是static还是对象?
    private static final Map<String, Unit> ID_TO_UNITS = Maps.newConcurrentMap();
    /**
     * 保存全局配置与id的map
     */
    private static final Map<String, Configuration> ID_TO_CONFIGURATIONS = Maps.newConcurrentMap();
    /**
     * 默认实例
     */
    private static final Configuration DEFAULT = new Configuration();

    @NonNull
    public static Configuration of(){
        return new Configuration();
    }
    static {
        Units.VALUES.forEach(Configuration::register);
        register(DEFAULT_ID, DEFAULT);
    }

    /**
     * 源对象与目标对象的对应转换器配置Table
     */
    @NonNull
    final Table<Unit, Unit, UnitConvertConfig> SOURCE_TO_TARGET_TO_CONFIG_TABLE = Tables
            .synchronizedTable(HashBasedTable.create());
    /**
     * 单位类型与目标单位的Map //TODO 用处确认
     */
    @NonNull
    final Map<UnitType, Unit> typeToTargetUnits = Maps.newConcurrentMap();
    /**
     * 单位和该单位所属的单位组
     */
    @NonNull
    final Map<Unit, UnitGroup> unitToGroups = Maps.newConcurrentMap();
    /**
     * 可注册别名的对象与别名类型和对应的别名Table 数据与{@link #aliasToTypeToUnitTypeTable}保持对应
     */
    @NonNull
    final Multimap<UnitType, Alias> unitTypeToAliases = Multimaps.synchronizedMultimap(HashMultimap.create());
    /**
     * 别名值与别名类型和对应的可注册别名的对象 数据与{@link #unitTypeToAliases}保持对应
     */
    @NonNull
    final Table<String, Alias.Type, UnitType> aliasToTypeToUnitTypeTable = Tables
            .synchronizedTable(HashBasedTable.create());
    /**
     * 可注册别名的对象与别名类型和对应的别名Table 数据与{@link #aliasToTypeToPrefixTable}保持对应
     */
    @NonNull
    final Multimap<Prefix, Alias> prefixToAliases = Multimaps.synchronizedMultimap(HashMultimap.create());
    /**
     * 别名值与别名类型和对应的可注册别名的对象 数据与{@link #prefixToAliases}保持对应
     */
    @NonNull
    final Table<String, Alias.Type, Prefix> aliasToTypeToPrefixTable = Tables
            .synchronizedTable(HashBasedTable.create());
    /**
     * 可注册别名的对象与别名类型和对应的别名Table 数据与{@link #aliasToTypeToStandardUnitTable}保持对应
     */
    @NonNull
    final Multimap<StandardUnit, Alias> standardUnitToAliases = Multimaps.synchronizedMultimap(HashMultimap.create());
    /**
     * 别名值与别名类型和对应的可注册别名的对象 数据与{@link #standardUnitToAliases}保持对应
     */
    @NonNull
    final Table<String, Alias.Type, StandardUnit> aliasToTypeToStandardUnitTable = Tables
            .synchronizedTable(HashBasedTable.create());

    /**
     * 可注册别名的对象与别名类型和对应的别名Table 数据与{@link #aliasToTypeToPrefixUnitTable}保持对应
     */
    @NonNull
    final Multimap<PrefixUnit, Alias> prefixUnitToAliases = Multimaps.synchronizedMultimap(HashMultimap.create());
    /**
     * 别名值与别名类型和对应的可注册别名的对象 数据与{@link #prefixUnitToAliases}保持对应
     */
    @NonNull
    final Table<String, Alias.Type, PrefixUnit> aliasToTypeToPrefixUnitTable = Tables
            .synchronizedTable(HashBasedTable.create());

    /**
     * 可注册别名的对象与别名类型和对应的别名Table 数据与{@link #aliasToTypeToAliasRegistrableTable}保持对应
     */
    @NonNull
    final Multimap<Object, Alias> aliasRegistrableToAliases = Multimaps.synchronizedMultimap(HashMultimap.create());
    /**
     * 别名值与别名类型和对应的可注册别名的对象 数据与{@link #aliasRegistrableToAliases}保持对应
     */
    @NonNull
    final Table<String, Alias.Type, Object> aliasToTypeToAliasRegistrableTable = Tables
            .synchronizedTable(HashBasedTable.create());
    @NonNull
    final Map<Prefix, BigFraction> prefixToValues =Maps.newConcurrentMap();
    /**
     * 单位自动转换器
     */
    @NonNull
    @NonFinal
    AutoConverter autoConverter = DEFAULT_AUTO_CONVERTER;
    /**
     * 数学计算时使用的上下文
     */
    @NonNull
    volatile MathContext mathContext = DEFAULT_MATH_CONTEXT;

    private Configuration() {
        register(Units.CHINESE_MILE,
                Units.METER, UnitConvertConfig.of(BigDecimal.valueOf(500)));
        register(Units.TANG,Units.CHINESE_MILE,
                UnitConvertConfig.of(BigDecimal.valueOf(10)));
        register(Units.CHINESE_MILE, Units.ZHANG,
                UnitConvertConfig.of(BigDecimal.valueOf(150)));
        register(Units.YIN,Units.ZHANG,
                UnitConvertConfig.of(BigDecimal.valueOf(10)));
        register(Units.ZHANG,Units.CHI,
                UnitConvertConfig.of(BigDecimal.valueOf(10)));
        register(Units.CHI,Units.CUN,
                UnitConvertConfig.of(BigDecimal.valueOf(10)));
        register(Units.CUN,Units.FEN,
                UnitConvertConfig.of(BigDecimal.valueOf(10)));
        register(Units.FEN,Units.LI,  UnitConvertConfig.of(
                BigDecimal.valueOf(10)));

        register(Units.INCH, Units.METER,
                UnitConvertConfig.of(BigFraction.getReducedFraction(254,10000)));
        register(Units.FOOT, Units.INCH,
                UnitConvertConfig.of(BigDecimal.valueOf(12)));
        register(Units.YARD, Units.FOOT,
                UnitConvertConfig.of(BigDecimal.valueOf(3)));
        register(Units.FATHOM, Units.INCH,
                UnitConvertConfig.of(BigDecimal.valueOf(72)));
        register(Units.CHAIN, Units.YARD,
                UnitConvertConfig.of(BigDecimal.valueOf(22)));
        register(Units.FURLONG, Units.YARD,
                UnitConvertConfig.of(BigDecimal.valueOf(220)));
        register(Units.MILE, Units.YARD,
                UnitConvertConfig.of(BigDecimal.valueOf(1760)));

        register(
                Units.TONNE, Units.GRAM,
                UnitConvertConfig.of(BigDecimal.valueOf(1000000)));

        register(Units.MINUTE, Units.SECOND,
                UnitConvertConfig.of(BigDecimal.valueOf(60)));
        register(Units.HOUR, Units.MINUTE,
                UnitConvertConfig.of(BigDecimal.valueOf(60)));
        register(Units.DAY, Units.HOUR,
                UnitConvertConfig.of(BigDecimal.valueOf(24)));
        register(Units.HALF_DAY, Units.DAY,
                UnitConvertConfig.of(BigFraction.ONE_HALF));
        register(Units.WEEK, Units.DAY,
                UnitConvertConfig.of(BigDecimal.valueOf(7)));
        register(Units.YEAR, Units.SECOND,
                UnitConvertConfig.of(BigDecimal.valueOf(31556952L)));
        register(Units.MONTH, Units.YEAR,
                UnitConvertConfig
                        .of(new BigFraction(1, 12)));
        register(Units.DECADE, Units.YEAR,
                UnitConvertConfig.of(BigDecimal.TEN));
        register(Units.CENTURY, Units.YEAR,
                UnitConvertConfig.of(BigDecimal.valueOf(100)));
        register(
                Units.MILLENNIUM, Units.YEAR,
                UnitConvertConfig.of(BigDecimal.valueOf(1000)));
        register(Units.ERA, Units.YEAR,
                UnitConvertConfig.of(BigDecimal.valueOf(1000000000)));


        register(Units.FAHRENHEIT_DEGREE, Units.CELSIUS_DEGREE,
                UnitConvertConfig.builder().ratio("1.8").constantDifference(32).build());

        registerAlias(UnitTypes.LENGTH, Aliases.lengthAliases());
        registerAlias(UnitTypes.MASS, Aliases.massAliases());
        registerAlias(UnitTypes.TIME, Aliases.timeAliases());
        registerAlias(UnitTypes.ELECTRIC_CURRENT, Aliases.electricCurrentAliases());
        registerAlias(UnitTypes.TEMPERATURE, Aliases.temperatureAliases());
        registerAlias(UnitTypes.SUBSTANCE_AMOUNT, Aliases.substanceAmountAliases());
        registerAlias(UnitTypes.LUMINOUS_INTENSITY, Aliases.luminousIntensityAliases());

        //注册默认词头别名
        registerAlias(Prefixes.YOCTO, Aliases.yoctoAliases());
        registerAlias(Prefixes.ZEPTO, Aliases.zeptoAliases());
        registerAlias(Prefixes.ATTO, Aliases.attoAliases());
        registerAlias(Prefixes.FEMTO, Aliases.femtoAliases());
        registerAlias(Prefixes.PICO, Aliases.picoAliases());
        registerAlias(Prefixes.NANO, Aliases.nanoAliases());
        registerAlias(Prefixes.MICRO, Aliases.microAliases());
        registerAlias(Prefixes.MILLI, Aliases.milliAliases());
        registerAlias(Prefixes.CENTI, Aliases.centiAliases());
        registerAlias(Prefixes.DECI, Aliases.deciAliases());
        registerAlias(Prefixes.DECA, Aliases.decaAliases());
        registerAlias(Prefixes.HECTO, Aliases.hectoAliases());
        registerAlias(Prefixes.KILO, Aliases.kiloAliases());
        registerAlias(Prefixes.MEGA, Aliases.megaAliases());
        registerAlias(Prefixes.GIGA, Aliases.gigaAliases());
        registerAlias(Prefixes.TERA, Aliases.teraAliases());
        registerAlias(Prefixes.EXA, Aliases.exaAliases());
        registerAlias(Prefixes.ZETTA, Aliases.zettaAliases());
        registerAlias(Prefixes.YOTTA, Aliases.yottaAliases());

        registerAlias(Prefixes.ANGSTROM, Aliases.angstromAliases());
        registerAlias(Prefixes.CENTIMILLI, Aliases.centimilliAliases());

        registerValue(Prefixes.YOCTO, new BigFraction(BigInteger.ONE,BigInteger.valueOf(10).pow(24)));
        registerValue(Prefixes.ZEPTO,new BigFraction(BigInteger.ONE,BigInteger.valueOf(10).pow(21)));
        registerValue(Prefixes.ATTO,new BigFraction(BigInteger.ONE,BigInteger.valueOf(10).pow(18)));
        registerValue(Prefixes.FEMTO,new BigFraction(BigInteger.ONE,BigInteger.valueOf(10).pow(15)));
        registerValue(Prefixes.PICO,new BigFraction(BigInteger.ONE,BigInteger.valueOf(10).pow(12)));
        registerValue(Prefixes.NANO,new BigFraction(BigInteger.ONE,BigInteger.valueOf(10).pow(9)));
        registerValue(Prefixes.MICRO,new BigFraction(BigInteger.ONE,BigInteger.valueOf(10).pow(6)));
        registerValue(Prefixes.MILLI,new BigFraction(BigInteger.ONE,BigInteger.valueOf(10).pow(3)));
        registerValue(Prefixes.CENTI,new BigFraction(BigInteger.ONE,BigInteger.valueOf(10).pow(2)));
        registerValue(Prefixes.DECI,new BigFraction(BigInteger.ONE,BigInteger.valueOf(10).pow(1)));
        registerValue(Prefixes.DECA,BigInteger.valueOf(10).pow(1));
        registerValue(Prefixes.HECTO,BigInteger.valueOf(10).pow(2));
        registerValue(Prefixes.KILO,BigInteger.valueOf(10).pow(3));
        registerValue(Prefixes.MEGA,BigInteger.valueOf(10).pow(6));
        registerValue(Prefixes.GIGA,BigInteger.valueOf(10).pow(9));
        registerValue(Prefixes.TERA,BigInteger.valueOf(10).pow(12));
        registerValue(Prefixes.PETA,BigInteger.valueOf(10).pow(15));
        registerValue(Prefixes.EXA,BigInteger.valueOf(10).pow(18));
        registerValue(Prefixes.ZETTA,BigInteger.valueOf(10).pow(21));
        registerValue(Prefixes.YOTTA,BigInteger.valueOf(10).pow(24));

        registerValue(Prefixes.ANGSTROM,new BigFraction(BigInteger.ONE,BigInteger.valueOf(10).pow(8)));
        registerValue(Prefixes.CENTIMILLI,new BigFraction(BigInteger.ONE,BigInteger.valueOf(10).pow(5)));

        //注册Unit别名
        registerAlias(Units.METER, Aliases.meterAliases());
        registerAlias(Units.GRAM, Aliases.gramAliases());
        registerAlias(Units.SECOND, Aliases.secondAliases());
        registerAlias(Units.AMPERE, Aliases.ampereAliases());
        registerAlias(Units.KELVIN, Aliases.kelvinAliases());
        registerAlias(Units.MOLE, Aliases.moleAliases());
        registerAlias(Units.CANDELA, Aliases.candelaAliases());
        registerAlias(Units.CELSIUS_DEGREE, Aliases.celsiusDegreeAliases());
        registerAlias(Units.FAHRENHEIT_DEGREE, Aliases.fahrenheitDegreeAliases());
        registerAlias(Units.CHINESE_MILE, Aliases.chineseMileAliases());
        registerAlias(Units.TANG, Aliases.tangAliases());
        registerAlias(Units.ZHANG, Aliases.zhangAliases());
        registerAlias(Units.YIN, Aliases.yinAliases());
        registerAlias(Units.CHI, Aliases.chiAliases());
        registerAlias(Units.CUN, Aliases.cunAliases());
        registerAlias(Units.FEN, Aliases.fenAliases());
        registerAlias(Units.LI, Aliases.liAliases());
        registerAlias(Units.INCH, Aliases.inchAliases());
        registerAlias(Units.FOOT, Aliases.footAliases());
        registerAlias(Units.YARD, Aliases.yardAliases());
        registerAlias(Units.FATHOM, Aliases.fathomAliases());
        registerAlias(Units.CHAIN, Aliases.chainAliases());
        registerAlias(Units.FURLONG, Aliases.furlongAliases());
        registerAlias(Units.MILE, Aliases.mileAliases());
        registerAlias(Units.TONNE, Aliases.tonneAliases());
        registerAlias(Units.MINUTE, Aliases.minuteAliases());
        registerAlias(Units.HOUR, Aliases.hourAliases());
        registerAlias(Units.DAY, Aliases.dayAliases());
        registerAlias(Units.HALF_DAY, Aliases.halfDayAliases());
        registerAlias(Units.WEEK, Aliases.weekAliases());
        registerAlias(Units.YEAR, Aliases.yearAliases());
        registerAlias(Units.MONTH, Aliases.monthAliases());
        registerAlias(Units.DECADE, Aliases.decadeAliases());
        registerAlias(Units.CENTURY, Aliases.centuryAliases());
        registerAlias(Units.MILLENNIUM, Aliases.millenniumAliases());
        registerAlias(Units.ERA, Aliases.eraAliases());
        //todo 确认有独立的弧度 m·m−1 平面角 球面度 m2·m−2 立体角 单位
        registerAlias(Units.NON, Aliases.RADIAN_PLANE_ANGLE_ENGLISH_NAME, Aliases.RADIAN_PLANE_ANGLE_SYMBOL);
        registerAlias(Units.NON, Aliases.STERADIAN_SOLID_ANGLE_ENGLISH_NAME, Aliases.STERADIAN_SOLID_ANGLE_SYMBOL);
        registerAlias(Units.HERTZ, Aliases.hertzAliases());
        registerAlias(Units.NEWTON, Aliases.newtonAliases());
        registerAlias(Units.PASCAL, Aliases.pascalAliases());
        registerAlias(Units.JOULE, Aliases.jouleAliases());
        registerAlias(Units.WATT, Aliases.wattAliases());
        registerAlias(Units.COULOMB, Aliases.coulombAliases());
        registerAlias(Units.VOLT, Aliases.voltAliases());
        registerAlias(Units.FARAD, Aliases.faradAliases());
        registerAlias(Units.OHM, Aliases.ohmAliases());
        registerAlias(Units.SIEMENS, Aliases.siemensAliases());
        registerAlias(Units.WEBER, Aliases.weberAliases());
        registerAlias(Units.TESLA, Aliases.teslaAliases());
        registerAlias(Units.HENRY, Aliases.henryAliases());
        registerAlias(Units.LUMEN, Aliases.lumenAliases());
        registerAlias(Units.LUX, Aliases.luxAliases());
        registerAlias(Units.BECQUEREL, Aliases.becquerelAliases());
        registerAlias(Units.GRAY, Aliases.grayAliases());
        registerAlias(Units.SIEVERT, Aliases.sievertAliases());
        registerAlias(Units.KATAL, Aliases.katalAliases());

        register(UnitGroup.builder().units(Units.DEFAULT_TIME_STANDARD_UNITS).configuration(this).build())
                .register(UnitGroup.createSiUnitGroup(Units.METER, this))
                .register(UnitGroup.createSiUnitGroup(Units.GRAM, this))
                .register(UnitGroup.createSiUnitGroup(Units.AMPERE, this))
                .register(UnitGroup.createSiUnitGroup(Units.CANDELA, this))
                .register(UnitGroup.createSiUnitGroup(Units.MOLE, this))
                .register(UnitGroup.createSiUnitGroup(Units.CELSIUS_DEGREE, this));
    }

    /**
     * 获取默认配置对象实例
     *
     * @return default global Configuration
     * @author caotc
     * @date 2018-11-30
     * @since 1.0.0
     */
    @NonNull
    public static Configuration defaultInstance() {
        return DEFAULT;
    }

    public static Configuration register(@NonNull String id, @NonNull Configuration configuration) {
        return ID_TO_CONFIGURATIONS.put(id, configuration);
    }

    /**
     * 根据id获取配置对象
     *
     * @param id id
     * @return 配置对象
     * @author caotc
     * @date 2019-04-23
     * @since 1.0.0
     */
    @NonNull
    public static Optional<Configuration> find(@NonNull String id) {
        return Optional.ofNullable(ID_TO_CONFIGURATIONS.get(id));
    }

    /**
     * 根据id获取{@link Configuration}
     *
     * @param id id
     * @return 配置对象
     * @throws ConfigurationNotFoundException 如果不存在该id的{@link Configuration}
     * @author caotc
     * @date 2019-11-01
     * @since 1.0.0
     */
    @NonNull
    public static Configuration findExact(@NonNull String id) {
        if (ID_TO_CONFIGURATIONS.containsKey(id)) {
            return ID_TO_CONFIGURATIONS.get(id);
        }
        throw ConfigurationNotFoundException.create(id);
    }

    public static void register(@NonNull Unit unit) {
        ID_TO_UNITS.putIfAbsent(unit.id(), unit);
    }

    @NonNull
    public static Optional<Unit> findUnit(@NonNull String id) {
        return Optional.ofNullable(ID_TO_UNITS.get(id));
    }

    @NonNull
    public static Unit findUnitExact(@NonNull String id) {
        return findUnit(id).orElseThrow(() -> UnitNotFoundException.create(id));
    }

    @NonNull
    public synchronized Configuration registerAlias(@NonNull UnitType unitType,
                                                    @NonNull Alias alias) {
        if (!unitTypeToAliases.containsEntry(unitType, alias)) {
            unitTypeToAliases.put(unitType, alias);
            aliasToTypeToUnitTypeTable.put(alias.value(), alias.type(), unitType);
        }
        return this;
    }

    @NonNull
    public synchronized Configuration registerAlias(@NonNull Prefix prefix,
                                                    @NonNull Alias alias) {
        if (!prefixToAliases.containsEntry(prefix, alias)) {
            prefixToAliases.put(prefix, alias);
            aliasToTypeToPrefixTable.put(alias.value(), alias.type(), prefix);
            standardUnitToAliases.entries().stream()
                    .filter(entry -> alias.type().equals(entry.getValue().type()))
                    .forEach(entry -> registerAlias(entry.getKey().addPrefix(prefix), Alias.create(alias.type(), PrefixUnit.composite(alias.value(), entry.getValue().value()))));
        }
        return this;
    }

    @NonNull
    public synchronized Configuration registerAlias(@NonNull StandardUnit standardUnit,
                                                    @NonNull Alias alias) {
        if (!standardUnitToAliases.containsEntry(standardUnit, alias)) {
            standardUnitToAliases.put(standardUnit, alias);
            aliasToTypeToStandardUnitTable.put(alias.value(), alias.type(), standardUnit);
            prefixToAliases.entries().stream()
                    .filter(entry -> alias.type().equals(entry.getValue().type()))
                    .forEach(entry -> registerAlias(standardUnit.addPrefix(entry.getKey()), Alias.create(alias.type(), PrefixUnit.composite(entry.getValue().value(), alias.value()))));
        }
        return this;
    }

    @NonNull
    public synchronized Configuration registerAlias(@NonNull PrefixUnit prefixUnit,
                                                    @NonNull Alias alias) {
        if (!prefixUnitToAliases.containsEntry(prefixUnit, alias)) {
            prefixUnitToAliases.put(prefixUnit, alias);
            aliasToTypeToPrefixUnitTable.put(alias.value(), alias.type(), prefixUnit);
        }
        return this;
    }

    /**
     * 注册别名
     *
     * @param unitType 可注册别名对象
     * @param aliases  别名集合
     * @return {@code this}
     * @throws IllegalArgumentException 如果要注册的别名已经存在
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public Configuration registerAlias(@NonNull UnitType unitType,
                                       @NonNull Alias... aliases) {
        Arrays.stream(aliases).forEach(alias -> registerAlias(unitType, alias));
        return this;
    }

    @NonNull
    public Configuration registerAlias(@NonNull Prefix unitType,
                                       @NonNull Alias... aliases) {
        Arrays.stream(aliases).forEach(alias -> registerAlias(unitType, alias));
        return this;
    }

    @NonNull
    public Configuration registerAlias(@NonNull StandardUnit standardUnit,
                                       @NonNull Alias... aliases) {
        Arrays.stream(aliases).forEach(alias -> registerAlias(standardUnit, alias));
        return this;
    }

    @NonNull
    public Configuration registerAlias(@NonNull PrefixUnit prefixUnit,
                                       @NonNull Alias... aliases) {
        Arrays.stream(aliases).forEach(alias -> registerAlias(prefixUnit, alias));
        return this;
    }

    @NonNull
    public Configuration registerAlias(@NonNull UnitType unitType,
                                       @NonNull Iterable<Alias> aliases) {
        Streams.stream(aliases).forEach(alias -> registerAlias(unitType, alias));
        return this;
    }

    @NonNull
    public Configuration registerAlias(@NonNull Prefix prefix,
                                       @NonNull Iterable<Alias> aliases) {
        Streams.stream(aliases).forEach(alias -> registerAlias(prefix, alias));
        return this;
    }

    @NonNull
    public Configuration registerAlias(@NonNull StandardUnit standardUnit,
                                       @NonNull Iterable<Alias> aliases) {
        Streams.stream(aliases).forEach(alias -> registerAlias(standardUnit, alias));
        return this;
    }

    @NonNull
    public Configuration registerAlias(@NonNull PrefixUnit prefixUnit,
                                       @NonNull Iterable<Alias> aliases) {
        Streams.stream(aliases).forEach(alias -> registerAlias(prefixUnit, alias));
        return this;
    }

    @NonNull
    public ImmutableSet<Alias> aliases(@NonNull Object element) {
        //todo
        return ImmutableSet.of();
    }

    @NonNull
    public ImmutableSet<Alias> aliases(@NonNull UnitType unitType) {
        return ImmutableSet.copyOf(unitTypeToAliases.get(unitType));
    }

    @NonNull
    public ImmutableSet<Alias> aliases(@NonNull Prefix prefix) {
        return ImmutableSet.copyOf(prefixToAliases.get(prefix));
    }

    @NonNull
    public ImmutableSet<Alias> aliases(@NonNull StandardUnit standardUnit) {
        return ImmutableSet.copyOf(standardUnitToAliases.get(standardUnit));
    }

    @NonNull
    public ImmutableSet<Alias> aliases(@NonNull PrefixUnit prefixUnit) {
        return ImmutableSet.copyOf(prefixUnitToAliases.get(prefixUnit));
    }

    @NonNull
    public ImmutableSet<Alias> aliases(@NonNull Unit unit) {
        if (unit instanceof PrefixUnit) {
            return aliases((PrefixUnit) unit);
        }
        return aliases((StandardUnit) unit);
    }

    @NonNull
    public ImmutableSet<Alias> aliases(@NonNull UnitType unitType,
                                       @NonNull Alias.Type aliasType) {
        return aliases(unitType)
                .stream()
                .filter(alias -> alias.type().equals(aliasType))
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public ImmutableSet<Alias> aliases(@NonNull Prefix prefix,
                                       @NonNull Alias.Type aliasType) {
        return aliases(prefix)
                .stream()
                .filter(alias -> alias.type().equals(aliasType))
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public ImmutableSet<Alias> aliases(@NonNull StandardUnit standardUnit,
                                       @NonNull Alias.Type aliasType) {
        return aliases(standardUnit)
                .stream()
                .filter(alias -> alias.type().equals(aliasType))
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public ImmutableSet<Alias> aliases(@NonNull PrefixUnit prefixUnit,
                                       @NonNull Alias.Type aliasType) {
        return aliases(prefixUnit)
                .stream()
                .filter(alias -> alias.type().equals(aliasType))
                .collect(ImmutableSet.toImmutableSet());
    }

    //todo 优化成非遍历模式
    @NonNull
    public ImmutableSet<Alias> aliases(@NonNull Unit unit,
                                       @NonNull Alias.Type aliasType) {
        if (unit instanceof PrefixUnit) {
            return aliases((PrefixUnit) unit, aliasType);
        }
        return aliases((StandardUnit) unit, aliasType);
    }

    /**
     * 根据别名获取对应的词头
     *
     * @param alias 别名
     * @return 对应的词头
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public Optional<Prefix> findPrefix(@NonNull Alias alias) {
        return Optional.ofNullable(aliasToTypeToPrefixTable.get(alias.value(), alias.type()));
    }

    /**
     * 根据别名获取对应的单位类型
     *
     * @param alias 别名
     * @return 对应的单位类型
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public Optional<UnitType> findUnitType(@NonNull Alias alias) {
        return Optional.ofNullable(aliasToTypeToUnitTypeTable.get(alias.value(), alias.type()));
    }

    /**
     * 根据别名获取对应的标准单位
     *
     * @param alias 别名
     * @return 对应的标准单位
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public Optional<StandardUnit> findStandardUnit(@NonNull Alias alias) {
        return Optional.ofNullable(aliasToTypeToStandardUnitTable.get(alias.value(), alias.type()));
    }

    @NonNull
    public Optional<PrefixUnit> findPrefixUnit(@NonNull Alias alias) {
        return Optional.ofNullable(aliasToTypeToPrefixUnitTable.get(alias.value(), alias.type()));
    }

    @NonNull
    public Optional<? extends Unit> findUnit(@NonNull Alias alias) {
        //todo 都有时报错并且在register时检查? 允许同一个Alias对应多个对象?
        return Stream.of(findStandardUnit(alias), findPrefixUnit(alias))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny();
    }

    @NonNull
    public ImmutableSet<UnitType> unitTypes(@NonNull String alias) {
        return ImmutableSet.copyOf(aliasToTypeToUnitTypeTable.row(alias).values());
    }

    @NonNull
    public ImmutableSet<Prefix> prefixes(@NonNull String alias) {
        return ImmutableSet.copyOf(aliasToTypeToPrefixTable.row(alias).values());
    }

    /**
     * 根据别名值获取对应的单位集合
     *
     * @param alias 别名
     * @return 对应的标准单位集合
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public ImmutableSet<Unit> units(@NonNull String alias) {
        return Stream.concat(aliasToTypeToStandardUnitTable.row(alias).values().stream(),
                        aliasToTypeToPrefixUnitTable.row(alias).values().stream())
                .collect(ImmutableSet.toImmutableSet());
    }

    /**
     * 注册基本标准单位之间的转换关系
     *
     * @param source            源单位
     * @param target            目标单位
     * @param unitConvertConfig 转换关系
     * @author caotc
     * @date 2018-12-07
     * @since 1.0.0
     */
    @NonNull
    public Configuration register(@NonNull BaseStandardUnit source,
                                  @NonNull BaseStandardUnit target,
                                  @NonNull UnitConvertConfig unitConvertConfig) {
        register(source);
        register(target);
        addUnitConvertConfig(source, target, unitConvertConfig);
        return this;
    }

    /**
     * 获取两个单位之间的转换关系
     *
     * @param source 源单位
     * @param target 目标单位
     * @return 转换关系
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public UnitConvertConfig convertConfig(@NonNull Unit source,
                                           @NonNull Unit target) {
        Preconditions.checkArgument(source.type().equals(target.type()),
                "%s and %s can't convert,%s and %s are not type equals",
                source, target, source, target);
        UnitConvertConfig unitConvertConfig = SOURCE_TO_TARGET_TO_CONFIG_TABLE.get(source, target);
        if (unitConvertConfig != null) {
            return unitConvertConfig;
        }

        Optional<UnitConvertConfig> result = tryCreateUnitConvertConfig(source, target);
        result.ifPresent(config -> addUnitConvertConfig(source, target, config));
        return result.orElseThrow(() -> new IllegalArgumentException(String.format("no convert config for %s to %s", source, target)));
    }

    void registerValue(@NonNull Prefix prefix,@NonNull BigInteger value){
        registerValue(prefix, new BigFraction(value));
    }
    //todo
    void registerValue(@NonNull Prefix prefix,@NonNull BigFraction value){
        prefixToValues.put(prefix,value);
    }

    private Optional<BigFraction> findPrefixValue(@NonNull Prefix prefix){
        return Optional.ofNullable(prefixToValues.get(prefix));
    }

    //todo try还是会报错
    private Optional<UnitConvertConfig> tryCreateUnitConvertConfig(@NonNull Unit source, @NonNull Unit target) {
        if (source.equals(target)) {
            return Optional.of(UnitConvertConfig.empty());
        }

        if (source instanceof PrefixUnit) {
            StandardUnit sourceStandardUnit = ((PrefixUnit) source).standardUnit();
            if (sourceStandardUnit.equals(target)) {
                return findPrefixValue(source.prefix())
                        .map(UnitConvertConfig::of);
            }
            return Optional.of(convertConfig(source, sourceStandardUnit))
                    .map(config -> config.reduce(convertConfig(sourceStandardUnit, target)));
        }
        if (target instanceof PrefixUnit) {
            StandardUnit targetStandardUnit = ((PrefixUnit) target).standardUnit();
            if (targetStandardUnit.equals(source)) {
                return findPrefixValue(target.prefix())
                        .map(BigFraction::reciprocal)
                        .map(UnitConvertConfig::of);
            }
            return Optional.of(convertConfig(source, targetStandardUnit))
                    .map(config -> config.reduce(convertConfig(targetStandardUnit, target)));
        }

        //todo 把这个变成Unit的方法?
        if (source instanceof CompositeStandardUnit && target instanceof CompositeStandardUnit) {
            return Optional.of(createUnitConvertConfig((CompositeStandardUnit) source, (CompositeStandardUnit) target));
        }

        return Optional.empty();
    }

    @NonNull
    public Quantity convert(@NonNull Quantity quantity, @NonNull Unit targetUnit) {
        if (targetUnit.equals(quantity.unit())) {
            return quantity;
        }

        UnitConvertConfig convertConfig = convertConfig(quantity.unit(), targetUnit);
        return Quantity.create(convertConfig.apply(quantity.value()), targetUnit);
    }

    @NonNull
    public Quantity autoConvert(@NonNull Quantity quantity) {
        return autoConverter().autoConvert(this, quantity);
    }

    @NonNull
    public Collection<Quantity> autoConvert(@NonNull Collection<Quantity> quantities, boolean unitConsistency) {
        return autoConverter().autoConvert(this, quantities, unitConsistency);
    }

    @NonNull
    public Collection<Quantity> autoConvert(@NonNull Collection<Quantity> quantities) {
        return autoConverter().autoConvert(this, quantities);
    }

    /**
     * 获取单位的所属单位组
     *
     * @param unit 单位
     * @return 所属单位组
     * @author caotc
     * @date 2019-05-29
     * @apiNote 返回的UnitGroup不一定包含参数unit
     * @since 1.0.0
     */
    @NonNull
    public UnitGroup getUnitGroup(@NonNull Unit unit) {
        //todo 不存在?
        return unitToGroups.get(unit);
    }

    /**
     * 注册单位组
     * //todo 注册unitType to unitGroup
     *
     * @return {@code this}
     * @author caotc
     * @date 2019-03-21
     * @since 1.0.0
     */
    @NonNull
    public Configuration register(@NonNull UnitGroup unitGroup) {
        unitGroup.units()
                .forEach(unit -> Preconditions.checkArgument(!unitToGroups.containsKey(unit)
                        , "%s already belongs to another group %s", unit, unitToGroups.get(unit)));
        unitGroup.units().forEach(unit -> unitToGroups.put(unit, unitGroup));
        return this;
    }

    /**
     * 注册自动转换策略为FIXED时的各单位类型的自动转换目标单位
     *
     * @param compositePrefixUnit 自动转换目标单位
     * @return this configuration object
     * @author caotc
     * @date 2019-04-23
     * @since 1.0.0
     */
    @NonNull
    //TODO 判断是否有用
    public Configuration registerFixedTargetUnit(@NonNull CompositePrefixUnit compositePrefixUnit) {
        typeToTargetUnits.put(compositePrefixUnit.type(), compositePrefixUnit);
        return this;
    }

    /**
     * 比较两个单位大小
     *
     * @param unit1 比较单位
     * @param unit2 比较单位
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
     * or greater than the specified object.
     * @throws IllegalArgumentException 如果两个比较单位类型不同
     * @author caotc
     * @date 2019-05-29
     * @see java.util.Comparator#compare(Object, Object)
     * @since 1.0.0
     */
    public int compare(@NonNull Unit unit1, @NonNull Unit unit2) {
        Preconditions.checkArgument(unit1.type().equals(unit2.type()),
                "%s and %s can't compare,%s and %s are not type equals",
                unit1, unit2, this, unit1);

        UnitConvertConfig unitConvertConfig = convertConfig(unit1, unit2);
        return unitConvertConfig.ratio().compareTo(Numbers.ONE);
    }

    /**
     * 比较两个数量大小
     *
     * @param quantity1 比较数量
     * @param quantity2 比较数量
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
     * or greater than the specified object.
     * @throws IllegalArgumentException 如果两个比较数量的单位类型不同
     * @author caotc
     * @date 2019-05-29
     * @see java.util.Comparator#compare(Object, Object)
     * @see #compare(Unit, Unit)
     * @since 1.0.0
     */
    public int compare(@NonNull Quantity quantity1, @NonNull Quantity quantity2) {
        Preconditions.checkArgument(quantity1.unit().type().equals(quantity2.unit().type()),
                "%s and %s can't compare,%s and %s are not type equals",
                quantity1, quantity2, quantity1.unit(), quantity2.unit());
        return quantity1.value().compareTo(quantity2.convertTo(quantity1.unit()).value());
    }

    /**
     * `增加单位转换配置
     * todo 并发考虑
     *
     * @param source            源单位
     * @param target            目标单位
     * @param unitConvertConfig 单位转换配置
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    private void addUnitConvertConfig(@NonNull Unit source, @NonNull Unit target,
                                      @NonNull UnitConvertConfig unitConvertConfig) {
        log.debug("addUnitComponentConvertConfig. source:{},target:{}", source, target);
        putSelfAndNegateIfNotContains(source, target, unitConvertConfig);
        refreshIndirectConvertConfig(source);
        refreshIndirectConvertConfig(target);
    }

    /**
     * 刷新所有与该单位相关的转换配置,将该单位所有能做到的间接转换的配置合并保留为直接转换配置
     *
     * @param unit 刷新所有与该单位相关的转换配置
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    private void refreshIndirectConvertConfig(@NonNull Unit unit) {
        ImmutableSet<Unit> directConvertUnits = SOURCE_TO_TARGET_TO_CONFIG_TABLE
                .row(unit)
                .keySet().stream().collect(ImmutableSet.toImmutableSet());
        for (Unit directConvertUnit : directConvertUnits) {
            SOURCE_TO_TARGET_TO_CONFIG_TABLE
                    .row(directConvertUnit).keySet().stream()
                    .filter(indirectConvertUnit -> !contains(unit, indirectConvertUnit))//todo 似乎有逻辑问题，当unit->indirectConvertUnit原来就存在转换配置时，该配置不会被刷新
                    .forEach(
                            indirectConvertUnit -> {
                                UnitConvertConfig config = SOURCE_TO_TARGET_TO_CONFIG_TABLE
                                        .get(unit, directConvertUnit).reduce(
                                                SOURCE_TO_TARGET_TO_CONFIG_TABLE
                                                        .get(directConvertUnit, indirectConvertUnit));
                                putSelfAndNegateIfNotContains(unit,
                                        indirectConvertUnit, config);
                            });
        }
    }

    /**
     * //TODO 冲突检测 两个单位互相转换的配置添加
     *
     * @param source            源单位
     * @param target            目标单位
     * @param unitConvertConfig 单位转换配置
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    private void putSelfAndNegateIfNotContains(@NonNull Unit source,
                                               @NonNull Unit target,
                                               @NonNull UnitConvertConfig unitConvertConfig) {
        SOURCE_TO_TARGET_TO_CONFIG_TABLE.put(source, target, unitConvertConfig);
        SOURCE_TO_TARGET_TO_CONFIG_TABLE.put(target, source, unitConvertConfig.reciprocal());
    }

    private ImmutableCollection<Unit> next(@NonNull Unit original,
                                           @NonNull ImmutableCollection<Unit> canConvertUnits) {
        return canConvertUnits.stream()
                .map(SOURCE_TO_TARGET_TO_CONFIG_TABLE::row)
                .map(Map::entrySet).flatMap(Collection::stream)
                .filter(entry -> !SOURCE_TO_TARGET_TO_CONFIG_TABLE
                        .contains(original, entry.getKey()))
                .map(Entry::getKey)
                .collect(ImmutableSet.toImmutableSet());
    }

    /**
     * 源单位到目标单位是否已经存在转换配置
     *
     * @param source 源单位
     * @param target 目标单位
     * @return 是否已经存在转换配置
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    private boolean contains(@NonNull Unit source, @NonNull Unit target) {
        return SOURCE_TO_TARGET_TO_CONFIG_TABLE.contains(source, target);
    }

    /**
     * 根据基本单位的转换配置创建组合标准单位的转换配置
     *
     * @param source 源单位
     * @param target 目标单位
     * @return 组合标准单位的转换配置
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    private UnitConvertConfig createUnitConvertConfig(@NonNull CompositeStandardUnit source,
                                                      @NonNull CompositeStandardUnit target) {
        ImmutableList<UnitConvertConfig> unitConvertConfigs = source.componentToExponents().entrySet().stream()
                .map(entry -> {
                    UnitConvertConfig componentUnitConvertConfig = convertConfig(entry.getKey(), target.dimension(entry.getKey().type()).unit());
                    // 当转换关系不只是比例而是有常量差值时，其作为组合单位的一部分时无法正常推导组合单位的转换关系
                    if(!componentUnitConvertConfig.isConstantDifferenceZero() && (entry.getValue()!=1 || source.componentToExponents().size()>1)){//todo 封装为unit方法？
                        throw new IllegalArgumentException(String.format("%s for %s to %s.component UnitConvertConfig ConstantDifference must be 0.",componentUnitConvertConfig,entry.getKey(),target.dimension(entry.getKey().type())));
                    }
                    return componentUnitConvertConfig.pow(entry.getValue());
                })
                .collect(ImmutableList.toImmutableList());
        return unitConvertConfigs.stream()
                .reduce(UnitConvertConfig::reduce)
                .orElseGet(UnitConvertConfig::empty);
    }
}
