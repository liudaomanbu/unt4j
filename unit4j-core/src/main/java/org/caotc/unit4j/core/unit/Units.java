package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import lombok.experimental.UtilityClass;

import java.util.stream.Stream;

/**
 * 单位常量类
 *
 * @author caotc
 * @date 2019-04-28
 * @since 1.0.0
 */
@UtilityClass
public class Units {

    /**
     * 米 最初(1793年)：从北极至赤道经过巴黎的子午线长度的一千万分之一。法 过渡(1960年)：氪-86原子在2p10和5d5量子能级之间跃迁所发出的电磁波在真空中的波长的1,650,763.73倍。
     * 目前(1983年)：光在1299,792,458秒内在真空中行进的距离。
     */
    public static final BaseStandardUnit METER = BaseStandardUnit
            .create("METER", UnitTypes.LENGTH);
    /**
     * 克,实际上国际单位制中应该为千克 最初(1793年)：最初法文名为grave，定义为在冰点下体积为一立方分米的纯水的重量(质量)/1000。
     * 目前(1889年)：国际千克原器的质量/1000。
     */
    public static final BaseStandardUnit GRAM = BaseStandardUnit
            .create("GRAM", UnitTypes.MASS);
    /**
     * 秒 最初(中世纪)：一天时长的86,400分之一。 过渡(1956年)：1900年1月0日历书时12时算起的回归年时长的131,556,925.9747。
     * 目前(1967年)：铯-133原子基态的两个超精细能级之间跃迁所对应的辐射周期时长的9,192,631,770倍。
     */
    public static final BaseStandardUnit SECOND = BaseStandardUnit
            .create("SECOND", UnitTypes.TIME);
    /**
     * 安培 最初(1881年)：CGS电磁单位制中电流单位的十分之一。CGS电流单位的定义是，在半径为1厘米、长度为1厘米的圆弧上流通，并在圆心产生1奥斯特电场的电流。
     * 目前(1946年)：在真空中相距1米的两根横截面为圆形、粗度可忽略不计的无限长平行直导线，各通上相等的恒定电流，当两根导线之间每米长度所受力为2×10−7牛顿时，各导线上的电流定义为1安培。
     */
    public static final BaseStandardUnit AMPERE = BaseStandardUnit
            .create("AMPERE", UnitTypes.ELECTRIC_CURRENT);
    /**
     * 开尔文 最初(1743年)：摄氏温标将0 °C和100 °C分别定义为水的熔点和沸点。 过渡(1954年)：273.16 K定义为水的三相点(0.01 °C)。
     * 目前(1967年)：水的三相点热力学温度的1/273.16。
     */
    public static final BaseStandardUnit KELVIN = BaseStandardUnit
            .create("KELVIN", UnitTypes.TEMPERATURE);
    /**
     * 摩尔 最初(1900年)：物质的克数等于其分子量时的数量。原 目前(1967年)：物质所含的粒子数量相等于0.012千克碳-12所含的原子数量。
     */
    public static final BaseStandardUnit MOLE = BaseStandardUnit
            .create("MOLE", UnitTypes.SUBSTANCE_AMOUNT);
    /**
     * 坎德拉 最初(1946年)：整个辐射体在铂凝固温度下的亮度，定义为60新坎德拉每平方厘米。 目前(1979年)：频率为5.4×1014赫兹的单色光源在特定方向辐射强度为1683W/sr时的发光强度。
     */
    public static final BaseStandardUnit CANDELA = BaseStandardUnit
            .create("CANDELA", UnitTypes.LUMINOUS_INTENSITY);

    /**
     * 摄氏度 K
     */
    public static final BaseStandardUnit CELSIUS_DEGREE = BaseStandardUnit
            .create("CELSIUS_DEGREE", UnitTypes.TEMPERATURE);

    /**
     * 华氏度 K
     */
    public static final BaseStandardUnit FAHRENHEIT_DEGREE = BaseStandardUnit
            .create("FAHRENHEIT_DEGREE", UnitTypes.TEMPERATURE);

    /**
     * 1里=500米
     */
    public static final BaseStandardUnit CHINESE_MILE = BaseStandardUnit
            .create("CHINESE_MILE", UnitTypes.LENGTH);

    /**
     * 一堂为10里
     */
    public static final BaseStandardUnit TANG = BaseStandardUnit
            .create("TANG", UnitTypes.LENGTH);

    /**
     * 1里=150丈
     */
    public static final BaseStandardUnit ZHANG = BaseStandardUnit
            .create("ZHANG", UnitTypes.LENGTH);

    /**
     * 1引=10丈
     */
    public static final BaseStandardUnit YIN = BaseStandardUnit
            .create("YIN", UnitTypes.LENGTH);

    /**
     * 1丈=10尺
     */
    public static final BaseStandardUnit CHI = BaseStandardUnit
            .create("CHI", UnitTypes.LENGTH);

    /**
     * 1尺=10寸
     */
    public static final BaseStandardUnit CUN = BaseStandardUnit
            .create("CUN", UnitTypes.LENGTH);

    /**
     * 1寸=10分
     */
    public static final BaseStandardUnit FEN = BaseStandardUnit
            .create("FEN", UnitTypes.LENGTH);

    /**
     * 1分=10厘
     */
    public static final BaseStandardUnit LI = BaseStandardUnit
            .create("LI", UnitTypes.LENGTH);

    /**
     * 英寸 in
     */
    public static final BaseStandardUnit INCH = BaseStandardUnit
            .create("INCH", UnitTypes.LENGTH);

    /**
     * 英尺 ft
     */
    public static final BaseStandardUnit FOOT = BaseStandardUnit
            .create("FOOT", UnitTypes.LENGTH);

    /**
     * 码 yd
     */
    public static final BaseStandardUnit YARD = BaseStandardUnit
            .create("YARD", UnitTypes.LENGTH);

    /**
     * 英寻 fm
     */
    public static final BaseStandardUnit FATHOM = BaseStandardUnit
            .create("FATHOM", UnitTypes.LENGTH);

    /**
     * 链,锁
     */
    public static final BaseStandardUnit CHAIN = BaseStandardUnit
            .create("CHAIN", UnitTypes.LENGTH);

    /**
     * 浪
     */
    public static final BaseStandardUnit FURLONG = BaseStandardUnit
            .create("FURLONG", UnitTypes.LENGTH);

    /**
     * 英里 mi
     */
    public static final BaseStandardUnit MILE = BaseStandardUnit
            .create("MILE", UnitTypes.LENGTH);

    /**
     * 吨
     */
    public static final BaseStandardUnit TONNE = BaseStandardUnit
            .create("TONNE", UnitTypes.MASS);

    /**
     * 分钟
     */
    public static final BaseStandardUnit MINUTE = BaseStandardUnit
            .create("MINUTE", UnitTypes.TIME);

    /**
     * 小时
     */
    public static final BaseStandardUnit HOUR = BaseStandardUnit
            .create("HOUR", UnitTypes.TIME);

    /**
     * 天,24小时
     */
    public static final BaseStandardUnit DAY = BaseStandardUnit
            .create("DAY", UnitTypes.TIME);

    /**
     * 半天,12小时
     */
    public static final BaseStandardUnit HALF_DAY = BaseStandardUnit
            .create("HALF_DAY", UnitTypes.TIME);

    /**
     * 周,星期,7天
     */
    public static final BaseStandardUnit WEEK = BaseStandardUnit
            .create("WEEK", UnitTypes.TIME);

    /**
     * 年,以31556952秒为标准年
     */
    public static final BaseStandardUnit YEAR = BaseStandardUnit
            .create("YEAR", UnitTypes.TIME);

    /**
     * 月,12分之一年
     */
    public static final BaseStandardUnit MONTH = BaseStandardUnit
            .create("MONTH", UnitTypes.TIME);

    /**
     * 10年
     */
    public static final BaseStandardUnit DECADE = BaseStandardUnit
            .create("DECADE", UnitTypes.TIME);

    /**
     * 100年
     */
    public static final BaseStandardUnit CENTURY = BaseStandardUnit
            .create("CENTURY", UnitTypes.TIME);

    /**
     * 1000年
     */
    public static final BaseStandardUnit MILLENNIUM = BaseStandardUnit
            .create("MILLENNIUM", UnitTypes.TIME);

    /**
     * 时代,1,000,000,000 Years
     */
    public static final BaseStandardUnit ERA = BaseStandardUnit
            .create("ERA", UnitTypes.TIME);

    /**
     * 千克
     */
    public static final BasePrefixUnit KILOGRAM = GRAM.addPrefix(Prefix.KILO);
    /**
     * 埃米 10−8m 不是国际制单位 Å
     */
    public static final BasePrefixUnit ANGSTROM_METER = METER.addPrefix(Prefix.ANGSTROM);
    /**
     * 忽米 10−5m cmm
     */
    public static final BasePrefixUnit CENTIMILLI_METER = METER.addPrefix(Prefix.CENTIMILLI);

    /**
     * 弧度 m·m−1 平面角 球面度 m2·m−2 立体角
     */
    public static final CompositeStandardUnit NON = CompositeStandardUnit.builderInternal().build();

    /**
     * 牛顿 kg·m·s−2 力 重量
     */
    public static final CompositeStandardUnit NEWTON = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.KILOGRAM, 1)
            .componentToExponent(Units.METER, 1)
            .componentToExponent(Units.SECOND, -2)
            .build();
    /**
     * 帕斯卡 N/m2 kg·m−1·s−2 压强 应力
     */
    public static final CompositeStandardUnit PASCAL = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.KILOGRAM, 1)
            .componentToExponent(Units.METER, -1)
            .componentToExponent(Units.SECOND, -2)
            .build();
    /**
     * 焦耳 N·m kg·m2·s−2 能量 功 热量
     */
    public static final CompositeStandardUnit JOULE = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.NEWTON, 1)
            .componentToExponent(Units.METER, 1)
            .build();
    /**
     * 瓦特 J/s kg·m2·s−3 功率 辐射通量
     */
    public static final CompositeStandardUnit WATT = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.JOULE, 1)
            .componentToExponent(Units.SECOND, -1)
            .build();
    /**
     * 伏特 W/A kg·m2·s−3·A−1 电压(电势差) 电动势
     */
    public static final CompositeStandardUnit VOLT = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.WATT, 1)
            .componentToExponent(Units.AMPERE, -1)
            .build();

    /**
     * 欧姆 V/A	kg·m2·s−3·A−2 电阻、阻抗、电抗
     */
    public static final CompositeStandardUnit OHM = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.KILOGRAM, 1).componentToExponent(
                    Units.METER, 2)
            .componentToExponent(Units.SECOND, -3)
            .componentToExponent(Units.AMPERE, -2).build();
    /**
     * 西门子 A/V	kg−1·m−2·s3·A2 电导
     */
    public static final CompositeStandardUnit SIEMENS = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.KILOGRAM, -1).componentToExponent(
                    Units.METER, -2)
            .componentToExponent(Units.SECOND, 3)
            .componentToExponent(Units.AMPERE, 2)
            .build();
    /**
     * 韦伯 V·s	kg·m2·s−2·A−1 磁通量
     */
    public static final CompositeStandardUnit WEBER = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.KILOGRAM, 1).componentToExponent(
                    Units.METER, 2)
            .componentToExponent(Units.SECOND, -2)
            .componentToExponent(Units.AMPERE, -1).build();
    /**
     * 特斯拉 Wb/m2	kg·s−2·A−1 磁通量密度(磁场)
     */
    public static final CompositeStandardUnit TESLA = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.KILOGRAM, 1).componentToExponent(
                    Units.SECOND, -2)
            .componentToExponent(Units.AMPERE, -1).build();
    /**
     * 亨利 Wb/A	kg·m2·s−2·A−2 电感
     */
    public static final CompositeStandardUnit HENRY = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.KILOGRAM, 1).componentToExponent(
                    Units.METER, 2)
            .componentToExponent(Units.SECOND, -2)
            .componentToExponent(Units.AMPERE, -2).build();
    /**
     * 赫兹 s−1
     */
    public static final CompositeStandardUnit HERTZ = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.SECOND, -1).build();
    /**
     * 库仑 s·A 电荷
     */
    public static final CompositeStandardUnit COULOMB = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.SECOND, 1)
            .componentToExponent(Units.AMPERE, 1)
            .build();

    /**
     * 法拉 C/V	kg−1·m−2·s4·A2 电容
     */
    public static final CompositeStandardUnit FARAD = CompositeStandardUnit.builderInternal()
            .componentToExponent(COULOMB, 1)
            .componentToExponent(VOLT, -1)
            .build();

    /**
     * 流明 cd·sr	cd 光通量
     */
    public static final CompositeStandardUnit LUMEN = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.CANDELA, 1)
            .componentToExponent(NON, 1)
            .build();

    /**
     * 勒克斯 lm/m2	m−2·cd 照度
     */
    public static final CompositeStandardUnit LUX = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.METER, -2)
            .componentToExponent(Units.SECOND, -2).componentToExponent(
                    Units.CANDELA, 1).build();

    /**
     * 贝克勒尔 s−1 放射性活度
     */
    public static final CompositeStandardUnit BECQUEREL = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.SECOND, -1).build();

    /**
     * 戈瑞 J/kg	m2·s−2 致电离辐射的吸收剂量
     */
    public static final CompositeStandardUnit GRAY = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.METER, 2)
            .componentToExponent(Units.SECOND, -2)
            .build();

    /**
     * 希沃特 J/kg	m2·s−2 致电离辐射等效剂量
     */
    public static final CompositeStandardUnit SIEVERT = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.METER, 2)
            .componentToExponent(Units.SECOND, -2)
            .build();

    /**
     * 开特 mol·s−1 催化活度
     */
    public static final CompositeStandardUnit KATAL = CompositeStandardUnit.builderInternal()
            .componentToExponent(Units.MOLE, 1)
            .componentToExponent(Units.SECOND, -1)
            .build();

    /**
     * 国际单位制7个基本标准单位不可变集合
     */
    public static final ImmutableSet<BaseStandardUnit> SI_BASE_STANDARD_UNITS = ImmutableSet
            .of(Units.METER, Units.GRAM, Units.SECOND, Units.AMPERE,
                    Units.KELVIN, Units.MOLE, Units.CANDELA);

    /**
     * 古代东亚长度标准单位不可变集合
     */
    public static final ImmutableSet<BaseStandardUnit> EAST_ASIA_OLD_LENGTH_STANDARD_UNITS = ImmutableSet
            .of(Units.CHINESE_MILE, Units.TANG, Units.ZHANG, Units.YIN,
                    Units.CHI, Units.CUN, Units.FEN, Units.LI);

    /**
     * 英制长度标准单位不可变集合
     */
    public static final ImmutableSet<BaseStandardUnit> ENGLISH_LENGTH_STANDARD_UNITS = ImmutableSet
            .of(Units.INCH,
                    Units.FOOT, Units.YARD, Units.FATHOM, Units.CHAIN,
                    Units.FURLONG, Units.MILE);

    /**
     * 默认时间标准单位不可变集合
     */
    public static final ImmutableSet<BaseStandardUnit> DEFAULT_TIME_STANDARD_UNITS = ImmutableSet
            .of(Units.SECOND, Units.MINUTE,
                    Units.HOUR,
                    Units.DAY, Units.HALF_DAY,
                    Units.WEEK, Units.YEAR, Units.MONTH, Units.DECADE,
                    Units.CENTURY, Units.MILLENNIUM, Units.ERA);

    public static final ImmutableSet<CompositeStandardUnit> SI_COMPOSITE_STANDARD_UNITS = ImmutableSet.of(NON, NEWTON, PASCAL,
            JOULE, WATT, VOLT, OHM, SIEMENS, WEBER, TESLA, HENRY, HERTZ, COULOMB, FARAD, LUMEN, LUX, BECQUEREL, GRAY, SIEVERT, KATAL);

    public static final ImmutableSet<StandardUnit> SI_STANDARD_UNITS = Stream.concat(SI_BASE_STANDARD_UNITS.stream(),
                    SI_COMPOSITE_STANDARD_UNITS.stream())
            .collect(ImmutableSet.toImmutableSet());

    public static final ImmutableSet<Unit> SI_UNITS = Stream.concat(SI_STANDARD_UNITS.stream(),
                    SI_STANDARD_UNITS.stream().flatMap(unit -> Prefix.SI_UNIT_PREFIXES.stream().map(unit::addPrefix)))
            .collect(ImmutableSet.toImmutableSet());

    public static final ImmutableSet<Unit> VALUES = Streams.concat(SI_STANDARD_UNITS.stream(),
                    Stream.of(CELSIUS_DEGREE, FAHRENHEIT_DEGREE, TONNE), EAST_ASIA_OLD_LENGTH_STANDARD_UNITS.stream()
                    , ENGLISH_LENGTH_STANDARD_UNITS.stream(), DEFAULT_TIME_STANDARD_UNITS.stream())
            .flatMap(unit -> Stream.concat(Stream.of(unit), Prefix.VALUES.stream().map(unit::addPrefix)))
            .collect(ImmutableSet.toImmutableSet());
}