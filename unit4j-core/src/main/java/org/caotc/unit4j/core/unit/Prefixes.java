package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableSet;
import lombok.experimental.UtilityClass;

import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2023-08-09
 * @since 1.0.0
 */
@UtilityClass
public class Prefixes {

    /**
     * 空对象
     */
    public static final Prefix EMPTY = Prefix.of("");//todo id

    /**
     * 符号:y 中国大陆:幺(科托) 台湾:攸 10⁻²⁴
     */
    public static final Prefix YOCTO = Prefix.of("YOCTO");
    /**
     * 符号:z 中国大陆:仄(普托) 台湾:介 10⁻²¹
     */
    public static final Prefix ZEPTO = Prefix.of("ZEPTO");
    /**
     * 符号:a 中国大陆:阿(托) 台湾:阿 10⁻¹⁸
     */
    public static final Prefix ATTO = Prefix.of("ATTO");
    /**
     * 符号:f 中国大陆:飞(母托) 台湾:飛 10⁻¹⁵
     */
    public static final Prefix FEMTO = Prefix.of("FEMTO");
    /**
     * 符号:p 中国大陆:皮(可) 台湾:皮 10⁻¹²
     */
    public static final Prefix PICO = Prefix.of("PICO");
    /**
     * 符号:n 中国大陆:纳(诺) 台湾:奈 10⁻⁹
     */
    public static final Prefix NANO = Prefix.of("NANO");
    /**
     * 符号:μ 中国大陆:微 台湾:微 10⁻⁶
     */
    public static final Prefix MICRO = Prefix.of("MICRO");
    /**
     * 符号:m 中国大陆:毫 台湾:毫 10⁻³
     */
    public static final Prefix MILLI = Prefix.of("MILLI");
    /**
     * 符号:c 中国大陆:厘 台湾:厘 10⁻²
     */
    public static final Prefix CENTI = Prefix.of("CENTI");
    /**
     * 符号:d 中国大陆:分 台湾:分 10⁻¹
     */
    public static final Prefix DECI = Prefix.of("DECI");
    /**
     * 符号:da 中国大陆:十 台湾:十 10¹
     */
    public static final Prefix DECA = Prefix.of("DECA");
    /**
     * 符号:h 中国大陆:百 台湾:百 10²
     */
    public static final Prefix HECTO = Prefix.of("HECTO");
    /**
     * 符号:k 中国大陆:千 台湾:千 10³
     */
    public static final Prefix KILO = Prefix.of("KILO");
    /**
     * 符号:M 中国大陆:兆 台湾:百萬 10⁶
     */
    public static final Prefix MEGA = Prefix.of("MEGA");
    /**
     * 符号:G 中国大陆:吉(咖) 台湾:吉 10⁹
     */
    public static final Prefix GIGA = Prefix.of("GIGA");
    /**
     * 符号:T 中国大陆:太(拉) 台湾:兆 10¹²
     */
    public static final Prefix TERA = Prefix.of("TERA");
    /**
     * 符号:P 中国大陆:拍(它) 台湾:拍 10¹⁵
     */
    public static final Prefix PETA = Prefix.of("PETA");
    /**
     * 符号:E 中国大陆:艾(可萨) 台湾:艾 10¹⁸
     */
    public static final Prefix EXA = Prefix.of("EXA");
    /**
     * 符号:Z 中国大陆:泽(它) 台湾:皆 10²¹
     */
    public static final Prefix ZETTA = Prefix.of("ZETTA");
    /**
     * 符号:Y 中国大陆:尧(它) 台湾:佑 10²⁴
     */
    public static final Prefix YOTTA = Prefix.of("YOTTA");

    //非国际单位制词头
    /**
     * 符号:Å 中国大陆:埃
     */
    public static final Prefix ANGSTROM = Prefix.of("ANGSTROM");

    /**
     * 符号:cmm 中国大陆:忽
     */
    public static final Prefix CENTIMILLI = Prefix.of("CENTIMILLI");

    /**
     * 国际单位制SI的所有标准词头
     */
    public static final ImmutableSet<Prefix> SI_UNIT_PREFIXES = ImmutableSet
            .of(YOCTO, ZEPTO, ATTO, FEMTO, PICO, NANO, MICRO, MILLI, CENTI, DECI, DECA, HECTO, KILO,
                    MEGA, GIGA, TERA, PETA, EXA, ZETTA, YOTTA);

    /**
     * 国际单位制SI的所有标准词头
     */
    public static final ImmutableSet<Prefix> VALUES = Stream.concat(SI_UNIT_PREFIXES.stream(), Stream.of(ANGSTROM, CENTIMILLI))
            .collect(ImmutableSet.toImmutableSet());
}
