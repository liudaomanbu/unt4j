/*
 * Copyright (C) 2020 the original author or authors.
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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.convert.QuantityChooser;
import org.caotc.unit4j.core.convert.TargetUnitChooser;
import org.caotc.unit4j.core.convert.UnitConvertConfig;
import org.caotc.unit4j.core.exception.ConfigurationNotFoundException;
import org.caotc.unit4j.core.exception.UnitNotFoundException;
import org.caotc.unit4j.core.math.number.AbstractNumber;
import org.caotc.unit4j.core.math.number.BigDecimal;
import org.caotc.unit4j.core.math.number.Fraction;
import org.caotc.unit4j.core.unit.BaseStandardUnit;
import org.caotc.unit4j.core.unit.CompositePrefixUnit;
import org.caotc.unit4j.core.unit.CompositeStandardUnit;
import org.caotc.unit4j.core.unit.Dimension;
import org.caotc.unit4j.core.unit.Prefix;
import org.caotc.unit4j.core.unit.PrefixUnit;
import org.caotc.unit4j.core.unit.StandardUnit;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.UnitConstant;
import org.caotc.unit4j.core.unit.UnitGroup;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.caotc.unit4j.core.unit.type.UnitType;

import java.math.BigInteger;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * 配置类 //TODO 两个单位之间存在不等值的转换config检查
 *
 * @author caotc
 * @date 2018-04-09
 * @implSpec
 * @implNote
 * @since 1.0.0
 **/
@Data
@FieldDefaults(makeFinal = false, level = AccessLevel.PRIVATE)
@Slf4j
public final class Configuration implements Identifiable {

    /**
     * 默认实例的id
     */
    public static final String DEFAULT_ID = "DEFAULT";

    /**
     * 默认的数学计算上下文对象
     */
    private static final MathContext DEFAULT_MATH_CONTEXT = MathContext.UNLIMITED;

    /**
     * 默认的自动转换时的目标单位选择器
     */
    private static final TargetUnitChooser DEFAULT_TARGET_UNIT_CHOOSER = TargetUnitChooser
            .create(QuantityChooser.targetValueQuantityChooser(BigInteger.ONE),
                    QuantityChooser.minQuantityChooser());

    /**
     * 保存全局配置与id的map
     */
    private static final Map<String, Configuration> ID_TO_CONFIGURATIONS = Maps.newConcurrentMap();

    private static final Map<String, Unit> ID_TO_UNITS = Maps.newConcurrentMap();

    /**
     * 默认实例
     */
    private static final Configuration DEFAULT = new Configuration(DEFAULT_ID);
    /**
     * 主键,全局的该类对象不能重复
     */
    @NonNull
    final String id;
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
    /**
     * 自动转换时的目标单位选择器
     */
    @NonNull
    volatile TargetUnitChooser targetUnitChooser = DEFAULT_TARGET_UNIT_CHOOSER;
    /**
     * 数学计算时使用的上下文
     */
    @NonNull
    volatile MathContext mathContext = DEFAULT_MATH_CONTEXT;

    private Configuration(@NonNull String id) {
        log.debug("Configuration Constructor start");
        this.id = id;
        register(UnitConstant.CHINESE_MILE,
                UnitConstant.METER, UnitConvertConfig.create(BigDecimal.valueOf(500)));
        register(UnitConstant.CHINESE_MILE, UnitConstant.TANG,
                UnitConvertConfig.create(BigDecimal.valueOf("0.1")));
        register(UnitConstant.CHINESE_MILE, UnitConstant.ZHANG,
                UnitConvertConfig.create(BigDecimal.valueOf(150)));
        register(UnitConstant.ZHANG, UnitConstant.YIN,
                UnitConvertConfig.create(BigDecimal.valueOf("0.1")));
        register(UnitConstant.CHI, UnitConstant.ZHANG,
                UnitConvertConfig.create(BigDecimal.valueOf("0.1")));
        register(UnitConstant.CUN, UnitConstant.CHI,
                UnitConvertConfig.create(BigDecimal.valueOf("0.1")));
        register(UnitConstant.FEN, UnitConstant.CUN,
                UnitConvertConfig.create(BigDecimal.valueOf("0.1")));
        register(UnitConstant.LI, UnitConstant.FEN, UnitConvertConfig.create(
                BigDecimal.valueOf("0.1")));

        register(UnitConstant.INCH, UnitConstant.METER,
                UnitConvertConfig.create(BigDecimal.valueOf("0.0254")));
        register(UnitConstant.FOOT, UnitConstant.INCH,
                UnitConvertConfig.create(BigDecimal.valueOf(12)));
        register(UnitConstant.YARD, UnitConstant.FOOT,
                UnitConvertConfig.create(BigDecimal.valueOf(3)));
        register(UnitConstant.FATHOM, UnitConstant.INCH,
                UnitConvertConfig.create(BigDecimal.valueOf(72)));
        register(UnitConstant.CHAIN, UnitConstant.YARD,
                UnitConvertConfig.create(BigDecimal.valueOf(22)));
        register(UnitConstant.FURLONG, UnitConstant.YARD,
                UnitConvertConfig.create(BigDecimal.valueOf(220)));
        register(UnitConstant.MILE, UnitConstant.YARD,
                UnitConvertConfig.create(BigDecimal.valueOf(1760)));

        register(
                UnitConstant.TONNE, UnitConstant.GRAM,
                UnitConvertConfig.create(BigDecimal.valueOf("1000000")));

        register(UnitConstant.MINUTE, UnitConstant.SECOND,
                UnitConvertConfig.create(BigDecimal.valueOf(60)));
        register(UnitConstant.HOUR, UnitConstant.MINUTE,
                UnitConvertConfig.create(BigDecimal.valueOf(60)));
        register(UnitConstant.DAY, UnitConstant.HOUR,
                UnitConvertConfig.create(BigDecimal.valueOf(24)));
        register(UnitConstant.HALF_DAY, UnitConstant.DAY,
                UnitConvertConfig.create(BigDecimal.valueOf("0.5")));
        register(UnitConstant.WEEK, UnitConstant.DAY,
                UnitConvertConfig.create(BigDecimal.valueOf(7)));
        register(UnitConstant.YEAR, UnitConstant.SECOND,
                UnitConvertConfig.create(BigDecimal.valueOf(31556952L)));
        register(UnitConstant.MONTH, UnitConstant.YEAR,
                UnitConvertConfig
                        .create(Fraction.valueOf(1L, 12L)));
        register(UnitConstant.DECADE, UnitConstant.YEAR,
                UnitConvertConfig.create(BigDecimal.TEN));
        register(UnitConstant.CENTURY, UnitConstant.YEAR,
                UnitConvertConfig.create(BigDecimal.valueOf(100)));
        register(
                UnitConstant.MILLENNIUM, UnitConstant.YEAR,
                UnitConvertConfig.create(BigDecimal.valueOf(1000)));
        register(UnitConstant.ERA, UnitConstant.YEAR,
                UnitConvertConfig.create(BigDecimal.valueOf(1000000000)));

        registerAlias(UnitTypes.LENGTH, Alias.LENGTH_ENGLISH_NAME, Alias.LENGTH_CHINESE_NAME);
        registerAlias(UnitTypes.MASS, Alias.MASS_ENGLISH_NAME, Alias.MASS_CHINESE_NAME);
        registerAlias(UnitTypes.TIME, Alias.TIME_ENGLISH_NAME, Alias.TIME_CHINESE_NAME);
        registerAlias(UnitTypes.ELECTRIC_CURRENT, Alias.ELECTRIC_CURRENT_ENGLISH_NAME,
                Alias.ELECTRIC_CURRENT_CHINESE_NAME);
        registerAlias(UnitTypes.TEMPERATURE, Alias.TEMPERATURE_ENGLISH_NAME,
                Alias.TEMPERATURE_CHINESE_NAME);
        registerAlias(UnitTypes.SUBSTANCE_AMOUNT, Alias.SUBSTANCE_AMOUNT_ENGLISH_NAME,
                Alias.SUBSTANCE_AMOUNT_CHINESE_NAME);
        registerAlias(UnitTypes.LUMINOUS_INTENSITY, Alias.LUMINOUS_INTENSITY_ENGLISH_NAME,
                Alias.LUMINOUS_INTENSITY_CHINESE_NAME);

        //注册默认词头别名
        registerAlias(Prefix.YOCTO, Alias.YOCTO_ENGLISH_NAME);
        registerAlias(Prefix.ZEPTO, Alias.ZEPTO_ENGLISH_NAME);
        registerAlias(Prefix.ATTO, Alias.ATTO_ENGLISH_NAME);
        registerAlias(Prefix.FEMTO, Alias.FEMTO_ENGLISH_NAME);
        registerAlias(Prefix.PICO, Alias.PICO_ENGLISH_NAME);
        registerAlias(Prefix.NANO, Alias.NANO_ENGLISH_NAME);
        registerAlias(Prefix.MICRO, Alias.MICRO_ENGLISH_NAME);
        registerAlias(Prefix.MILLI, Alias.MILLI_ENGLISH_NAME);
        registerAlias(Prefix.CENTI, Alias.CENTI_ENGLISH_NAME);
        registerAlias(Prefix.DECI, Alias.DECI_ENGLISH_NAME);
        registerAlias(Prefix.DECA, Alias.DECA_ENGLISH_NAME);
        registerAlias(Prefix.HECTO, Alias.HECTO_ENGLISH_NAME);
        registerAlias(Prefix.KILO, Alias.KILO_ENGLISH_NAME);
        registerAlias(Prefix.MEGA, Alias.MEGA_ENGLISH_NAME);
        registerAlias(Prefix.GIGA, Alias.GIGA_ENGLISH_NAME);
        registerAlias(Prefix.TERA, Alias.TERA_ENGLISH_NAME);
        registerAlias(Prefix.EXA, Alias.EXA_ENGLISH_NAME);
        registerAlias(Prefix.ZETTA, Alias.ZETTA_ENGLISH_NAME);
        registerAlias(Prefix.YOTTA, Alias.YOTTA_ENGLISH_NAME);

        registerAlias(Prefix.ANGSTROM, Alias.ANGSTROM_ENGLISH_NAME);

        //注册Unit
        registerAlias(UnitConstant.METER, Alias.METER_ENGLISH_NAME, Alias.METER_CHINESE_NAME,
                Alias.METER_SYMBOL);
        registerAlias(UnitConstant.GRAM, Alias.GRAM_ENGLISH_NAME, Alias.GRAM_CHINESE_NAME,
                Alias.GRAM_SYMBOL);
        registerAlias(UnitConstant.SECOND, Alias.SECOND_ENGLISH_NAME, Alias.SECOND_CHINESE_NAME,
                Alias.SECOND_SYMBOL);
        registerAlias(UnitConstant.AMPERE, Alias.AMPERE_ENGLISH_NAME, Alias.AMPERE_CHINESE_NAME,
                Alias.AMPERE_SYMBOL);
        registerAlias(UnitConstant.KELVIN, Alias.KELVIN_ENGLISH_NAME, Alias.KELVIN_CHINESE_NAME,
                Alias.KELVIN_SYMBOL);
        registerAlias(UnitConstant.MOLE, Alias.MOLE_ENGLISH_NAME, Alias.MOLE_CHINESE_NAME,
                Alias.MOLE_SYMBOL);
        registerAlias(UnitConstant.CANDELA, Alias.CANDELA_ENGLISH_NAME, Alias.CANDELA_CHINESE_NAME,
                Alias.CANDELA_SYMBOL);
        registerAlias(UnitConstant.CELSIUS_DEGREE, Alias.CELSIUS_DEGREE_ENGLISH_NAME,
                Alias.CELSIUS_DEGREE_SYMBOL);
        registerAlias(UnitConstant.FAHRENHEIT_DEGREE, Alias.FAHRENHEIT_DEGREE_ENGLISH_NAME,
                Alias.FAHRENHEIT_DEGREE_SYMBOL);
        registerAlias(UnitConstant.CHINESE_MILE, Alias.CHINESE_MILE_ENGLISH_NAME);
        registerAlias(UnitConstant.TANG, Alias.TANG_ENGLISH_NAME);
        registerAlias(UnitConstant.ZHANG, Alias.ZHANG_ENGLISH_NAME);
        registerAlias(UnitConstant.YIN, Alias.YIN_ENGLISH_NAME);
        registerAlias(UnitConstant.CHI, Alias.CHI_ENGLISH_NAME);
        registerAlias(UnitConstant.CUN, Alias.CUN_ENGLISH_NAME);
        registerAlias(UnitConstant.FEN, Alias.FEN_ENGLISH_NAME);
        registerAlias(UnitConstant.LI, Alias.LI_ENGLISH_NAME);
        registerAlias(UnitConstant.INCH, Alias.INCH_ENGLISH_NAME);
        registerAlias(UnitConstant.FOOT, Alias.FOOT_ENGLISH_NAME);
        registerAlias(UnitConstant.YARD, Alias.YARD_ENGLISH_NAME);
        registerAlias(UnitConstant.FATHOM, Alias.FATHOM_ENGLISH_NAME);
        registerAlias(UnitConstant.CHAIN, Alias.CHAIN_ENGLISH_NAME);
        registerAlias(UnitConstant.FURLONG, Alias.FURLONG_ENGLISH_NAME);
        registerAlias(UnitConstant.MILE, Alias.MILE_ENGLISH_NAME);
        registerAlias(UnitConstant.TONNE, Alias.TONNE_ENGLISH_NAME);
        registerAlias(UnitConstant.MINUTE, Alias.MINUTE_ENGLISH_NAME);
        registerAlias(UnitConstant.HOUR, Alias.HOUR_ENGLISH_NAME);
        registerAlias(UnitConstant.DAY, Alias.DAY_ENGLISH_NAME);
        registerAlias(UnitConstant.HALF_DAY, Alias.HALF_DAY_ENGLISH_NAME);
        registerAlias(UnitConstant.WEEK, Alias.WEEK_ENGLISH_NAME);
        registerAlias(UnitConstant.YEAR, Alias.YEAR_ENGLISH_NAME);
        registerAlias(UnitConstant.MONTH, Alias.MONTH_ENGLISH_NAME);
        registerAlias(UnitConstant.DECADE, Alias.DECADE_ENGLISH_NAME);
        registerAlias(UnitConstant.CENTURY, Alias.CENTURY_ENGLISH_NAME);
        registerAlias(UnitConstant.MILLENNIUM, Alias.MILLENNIUM_ENGLISH_NAME);
        registerAlias(UnitConstant.ERA, Alias.ERA_ENGLISH_NAME);
        registerAlias(UnitConstant.NON, Alias.RADIAN_PLANE_ANGLE_ENGLISH_NAME, Alias.RADIAN_PLANE_ANGLE_SYMBOL);
        registerAlias(UnitConstant.NON, Alias.STERADIAN_SOLID_ANGLE_ENGLISH_NAME, Alias.STERADIAN_SOLID_ANGLE_SYMBOL);
        registerAlias(UnitConstant.HERTZ, Alias.HERTZ_ENGLISH_NAME, Alias.HERTZ_SYMBOL);
        registerAlias(UnitConstant.NEWTON, Alias.NEWTON_ENGLISH_NAME, Alias.NEWTON_SYMBOL);
        registerAlias(UnitConstant.PASCAL, Alias.PASCAL_ENGLISH_NAME, Alias.PASCAL_SYMBOL);
        registerAlias(UnitConstant.JOULE, Alias.JOULE_ENGLISH_NAME, Alias.JOULE_SYMBOL);
        registerAlias(UnitConstant.WATT, Alias.WATT_ENGLISH_NAME, Alias.WATT_SYMBOL);
        registerAlias(UnitConstant.COULOMB, Alias.COULOMB_ENGLISH_NAME, Alias.COULOMB_SYMBOL);
        registerAlias(UnitConstant.VOLT, Alias.VOLT_ENGLISH_NAME, Alias.VOLT_SYMBOL);
        registerAlias(UnitConstant.FARAD, Alias.FARAD_ENGLISH_NAME, Alias.FARAD_SYMBOL);
        registerAlias(UnitConstant.OHM, Alias.OHM_ENGLISH_NAME, Alias.OHM_SYMBOL);
        registerAlias(UnitConstant.SIEMENS, Alias.SIEMENS_ENGLISH_NAME, Alias.SIEMENS_SYMBOL);
        registerAlias(UnitConstant.WEBER, Alias.WEBER_ENGLISH_NAME, Alias.WEBER_SYMBOL);
        registerAlias(UnitConstant.TESLA, Alias.TESLA_ENGLISH_NAME, Alias.TESLA_SYMBOL);
        registerAlias(UnitConstant.HENRY, Alias.HENRY_ENGLISH_NAME, Alias.HENRY_SYMBOL);
        registerAlias(UnitConstant.LUMEN, Alias.LUMEN_ENGLISH_NAME, Alias.LUMEN_SYMBOL);
        registerAlias(UnitConstant.LUX, Alias.LUX_ENGLISH_NAME, Alias.LUX_SYMBOL);
        registerAlias(UnitConstant.BECQUEREL, Alias.BECQUEREL_ENGLISH_NAME, Alias.BECQUEREL_SYMBOL);
        registerAlias(UnitConstant.GRAY, Alias.GRAY_ENGLISH_NAME, Alias.GRAY_SYMBOL);
        registerAlias(UnitConstant.SIEVERT, Alias.SIEVERT_ENGLISH_NAME, Alias.SIEVERT_SYMBOL);
        registerAlias(UnitConstant.KATAL, Alias.KATAL_ENGLISH_NAME, Alias.KATAL_SYMBOL);

        register(UnitGroup.create(UnitConstant.DEFAULT_TIME_STANDARD_UNITS, this::compare))
                .register(UnitGroup.createSiUnitGroup(UnitConstant.METER, this::compare))
                .register(UnitGroup.createSiUnitGroup(UnitConstant.GRAM, this::compare))
                .register(UnitGroup.createSiUnitGroup(UnitConstant.AMPERE, this::compare))
                .register(UnitGroup.createSiUnitGroup(UnitConstant.CANDELA, this::compare))
                .register(UnitGroup.createSiUnitGroup(UnitConstant.MOLE, this::compare))
                .register(UnitGroup.createSiUnitGroup(UnitConstant.CELSIUS_DEGREE, this::compare));

        register(this);
        log.debug("Configuration Constructor end");
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
    public static Optional<Unit> getUnitById(@NonNull String id) {
        return Optional.ofNullable(ID_TO_UNITS.get(id));
    }

    @NonNull
    public static Unit getUnitByIdExact(@NonNull String id) {
        return getUnitById(id).orElseThrow(() -> UnitNotFoundException.create(id));
    }

    private static void register(@NonNull Configuration configuration) {
        //TODO 重复性检查
        ID_TO_CONFIGURATIONS.putIfAbsent(configuration.id(), configuration);
    }

    /**
     * 获取可注册别名对象注册的所有别名
     *
     * @param aliasRegistrable 可注册别名对象
     * @return 所有别名
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public ImmutableSet<Alias> aliases(@NonNull Object aliasRegistrable) {
        return ImmutableSet.copyOf(aliasRegistrableToAliases.get(aliasRegistrable));
    }

    /**
     * //todo 优化成非遍历模式
     * 获取可注册别名对象注册的指定类型别名
     *
     * @param aliasRegistrable 可注册别名对象
     * @param aliasType        别名类型
     * @return 指定类型别名
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public ImmutableSet<Alias> aliases(@NonNull Object aliasRegistrable,
                                       @NonNull Alias.Type aliasType) {
        return aliasRegistrableToAliases.get(aliasRegistrable)
                .stream()
                .filter(alias -> alias.type().equals(aliasType))
                .collect(ImmutableSet.toImmutableSet());
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
    public Optional<Prefix> prefixByAlias(@NonNull Alias alias) {
        return aliasRegistrableByAlias(alias).filter(Prefix.class::isInstance).map(Prefix.class::cast);
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
    public Optional<? extends UnitType> unitTypeByAlias(@NonNull Alias alias) {
        return aliasRegistrableByAlias(alias).filter(UnitType.class::isInstance)
                .map(UnitType.class::cast);
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
    public Optional<? extends StandardUnit> standardUnitByAlias(@NonNull Alias alias) {
        return aliasRegistrableByAlias(alias).filter(StandardUnit.class::isInstance)
                .map(StandardUnit.class::cast);
    }

    /**
     * 根据别名值获取对应的标准单位集合
     *
     * @param alias 别名
     * @return 对应的标准单位集合
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public ImmutableSet<StandardUnit> standardUnitsByAlias(@NonNull String alias) {
        return aliasRegistrablesByAlias(alias).stream().filter(StandardUnit.class::isInstance)
                .map(StandardUnit.class::cast).collect(ImmutableSet.toImmutableSet());
    }

    /**
     * 根据别名获取对应的可注册别名对象
     *
     * @param alias 别名
     * @return 对应的可注册别名对象
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public Optional<?> aliasRegistrableByAlias(@NonNull Alias alias) {
        return Optional.ofNullable(aliasToTypeToAliasRegistrableTable.get(alias.value(), alias.type()));
    }

    /**
     * 根据别名值获取对应的可注册别名对象集合
     *
     * @param alias 别名
     * @return 对应的可注册别名对象集合
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public ImmutableSet<?> aliasRegistrablesByAlias(@NonNull String alias) {
        return ImmutableSet.copyOf(aliasToTypeToAliasRegistrableTable.row(alias).values());
    }

    /**
     * 注册别名
     *
     * @param aliasRegistrable 可注册别名对象
     * @param aliases          别名集合
     * @return {@code this}
     * @throws IllegalArgumentException 如果要注册的别名已经存在
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public Configuration registerAlias(@NonNull Object aliasRegistrable,
                                       @NonNull Alias... aliases) {
        Arrays.stream(aliases).forEach(alias -> registerAlias(aliasRegistrable, alias));
        return this;
    }

    /**
     * 注册别名
     *
     * @param aliasRegistrable 可注册别名对象
     * @param alias            别名
     * @return {@code this}
     * @throws IllegalArgumentException 如果要注册的别名已经存在
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public synchronized Configuration registerAlias(@NonNull Object aliasRegistrable,
                                                    @NonNull Alias alias) {
        if (aliasRegistrableToAliases.containsEntry(aliasRegistrable, alias)) {
            throw new IllegalArgumentException("" + aliasRegistrable + alias + "重复注册");
        }
        aliasRegistrableToAliases.put(aliasRegistrable, alias);
        aliasToTypeToAliasRegistrableTable.put(alias.value(), alias.type(), aliasRegistrable);
        return this;
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
    //TODO Optional?
    @NonNull
    public UnitConvertConfig getConvertConfig(@NonNull Unit source,
                                              @NonNull Unit target) {
        Preconditions.checkArgument(source.type().equals(target.type()),
                "%s and %s can't convert,%s and %s are not type equals",
                source, target, source, target);

        addUnitConvertConfig(source, source, UnitConvertConfig.empty());
        addUnitConvertConfig(target, target, UnitConvertConfig.empty());

        if (source instanceof PrefixUnit) {
            addUnitConvertConfig(source, ((PrefixUnit) source).standardUnit(),
                    source.prefix().convertToStandardUnitConfig());
        }
        if (target instanceof PrefixUnit) {
            addUnitConvertConfig(target, ((PrefixUnit) target).standardUnit(),
                    target.prefix().convertToStandardUnitConfig());
        }

        //todo 把这个变成Unit的方法?
        if (source.componentToExponents().size() != 1 && target.componentToExponents().size() != 1) {
            CompositeStandardUnit sourceCompositeStandardUnit =
                    source instanceof CompositeStandardUnit ? (CompositeStandardUnit) source
                            : ((CompositePrefixUnit) source).standardUnit();
            CompositeStandardUnit targetCompositeStandardUnit =
                    target instanceof CompositeStandardUnit ? (CompositeStandardUnit) target
                            : ((CompositePrefixUnit) target).standardUnit();
            if (!SOURCE_TO_TARGET_TO_CONFIG_TABLE
                    .contains(sourceCompositeStandardUnit, targetCompositeStandardUnit)) {
                addUnitConvertConfig(sourceCompositeStandardUnit, targetCompositeStandardUnit,
                        create(sourceCompositeStandardUnit, targetCompositeStandardUnit));
            }
        }

        return Optional.ofNullable(SOURCE_TO_TARGET_TO_CONFIG_TABLE.get(source, target))
                .orElseThrow(IllegalArgumentException::new);
    }

    /**
     * 获取数量对象的自动转换目标单位
     *
     * @param quantity 数量对象
     * @return 自动转换目标单位
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public Unit getTargetUnit(@NonNull Quantity quantity) {
        return targetUnitChooser.targetUnit(quantity, this);
    }

    /**
     * 获取数量对象集合的自动转换目标单位
     *
     * @param quantities 数量对象集合
     * @return 自动转换目标单位
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public Unit getTargetUnit(@NonNull Collection<Quantity> quantities) {
        return targetUnitChooser.targetUnit(quantities, this);
    }

    /**
     * 获取单位的所属单位组
     *
     * @param unit 单位
     * @return 所属单位组
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public UnitGroup getUnitGroup(@NonNull Unit unit) {
        //todo 不存在?
        return unitToGroups.get(unit);
    }

    /**
     * 注册单位组
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

        UnitConvertConfig unitConvertConfig = getConvertConfig(unit1,
                unit2);
        AbstractNumber value = unitConvertConfig.apply(BigDecimal.ONE);
        return value.compareTo(BigDecimal.ONE);
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
                    .filter(indirectConvertUnit -> !contains(unit, indirectConvertUnit))
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
    private UnitConvertConfig create(@NonNull CompositeStandardUnit source,
                                     @NonNull CompositeStandardUnit target) {
        ImmutableMap<UnitType, Dimension> targetRebaseTypeToDimensionElementMap = target
                .typeToDimensionElementMap();
        return source.componentToExponents().entrySet().stream()
                .map(entry -> getConvertConfig(entry.getKey(),
                        targetRebaseTypeToDimensionElementMap.get(entry.getKey().type()).unit())
                        //TODO 确认负数逻辑
                        .power(entry.getValue()))
                .reduce(UnitConvertConfig::reduce).orElseGet(UnitConvertConfig::empty)
                .multiply(source.prefix().convertToStandardUnitConfig().ratio())
                .multiply(target.prefix().convertFromStandardUnitConfig().ratio());
    }
}
