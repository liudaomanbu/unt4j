package org.caotc.unit4j.core;

import lombok.NonNull;
import lombok.Value;

/**
 * 别名
 *
 * @author caotc
 * @date 2018-12-04
 * @since 1.0.0
 **/
@Value(staticConstructor = "create")
public class Alias {

  @Value(staticConstructor = "create")
  public static class Type {

    /**
     * 英文名称
     */
    public static final Type ENGLISH_NAME = Type.create("ENGLISH_NAME");
    /**
     * 英文名称
     */
    public static final Type CHINESE_NAME = Type.create("CHINESE_NAME");
    /**
     * 缩写
     */
    public static final Type ABBREVIATION = Type.create("ABBREVIATION");
    /**
     * 符号
     */
    public static final Type SYMBOL = Type.create("SYMBOL");

    @NonNull
    String name;
  }

  /**
   * 默认英文别名
   */
  public static final Alias LENGTH_ENGLISH_NAME = create(Type.ENGLISH_NAME, "LENGTH");

  public static final Alias MASS_ENGLISH_NAME = create(Type.ENGLISH_NAME, "MASS");

  public static final Alias TIME_ENGLISH_NAME = create(Type.ENGLISH_NAME, "TIME");

  public static final Alias ELECTRIC_CURRENT_ENGLISH_NAME = create(Type.ENGLISH_NAME,
      "ELECTRIC_CURRENT");

  public static final Alias TEMPERATURE_ENGLISH_NAME = create(Type.ENGLISH_NAME, "TEMPERATURE");

  public static final Alias SUBSTANCE_AMOUNT_ENGLISH_NAME = create(Type.ENGLISH_NAME,
      "SUBSTANCE_AMOUNT");

  public static final Alias LUMINOUS_INTENSITY_ENGLISH_NAME = create(Type.ENGLISH_NAME,
      "LUMINOUS_INTENSITY");

  public static final Alias METER_ENGLISH_NAME = create(Type.ENGLISH_NAME, "METER");

  public static final Alias GRAM_ENGLISH_NAME = create(Type.ENGLISH_NAME, "GRAM");

  public static final Alias SECOND_ENGLISH_NAME = create(Type.ENGLISH_NAME, "SECOND");

  public static final Alias AMPERE_ENGLISH_NAME = create(Type.ENGLISH_NAME, "AMPERE");

  public static final Alias KELVIN_ENGLISH_NAME = create(Type.ENGLISH_NAME, "KELVIN");

  public static final Alias MOLE_ENGLISH_NAME = create(Type.ENGLISH_NAME, "MOLE");

  public static final Alias CANDELA_ENGLISH_NAME = create(Type.ENGLISH_NAME, "CANDELA");

  public static final Alias CELSIUS_DEGREE_ENGLISH_NAME = create(Type.ENGLISH_NAME,
      "CELSIUS_DEGREE_SYMBOL");

  public static final Alias FAHRENHEIT_DEGREE_ENGLISH_NAME = create(Type.ENGLISH_NAME,
      "FAHRENHEIT_DEGREE_SYMBOL");

  public static final Alias CHINESE_MILE_ENGLISH_NAME = create(Type.ENGLISH_NAME, "CHINESE_MILE");

  public static final Alias TANG_ENGLISH_NAME = create(Type.ENGLISH_NAME, "TANG");

  public static final Alias ZHANG_ENGLISH_NAME = create(Type.ENGLISH_NAME, "ZHANG");

  public static final Alias YIN_ENGLISH_NAME = create(Type.ENGLISH_NAME, "YIN");

  public static final Alias CHI_ENGLISH_NAME = create(Type.ENGLISH_NAME, "CHI");

  public static final Alias CUN_ENGLISH_NAME = create(Type.ENGLISH_NAME, "CUN");

  public static final Alias FEN_ENGLISH_NAME = create(Type.ENGLISH_NAME, "FEN");

  public static final Alias LI_ENGLISH_NAME = create(Type.ENGLISH_NAME, "LI");

  public static final Alias INCH_ENGLISH_NAME = create(Type.ENGLISH_NAME, "INCH");

  public static final Alias FOOT_ENGLISH_NAME = create(Type.ENGLISH_NAME, "FOOT");

  public static final Alias YARD_ENGLISH_NAME = create(Type.ENGLISH_NAME, "YARD");

  public static final Alias FATHOM_ENGLISH_NAME = create(Type.ENGLISH_NAME, "FATHOM");

  public static final Alias CHAIN_ENGLISH_NAME = create(Type.ENGLISH_NAME, "CHAIN");

  public static final Alias FURLONG_ENGLISH_NAME = create(Type.ENGLISH_NAME, "FURLONG");

  public static final Alias MILE_ENGLISH_NAME = create(Type.ENGLISH_NAME, "MILE");

  public static final Alias TONNE_ENGLISH_NAME = create(Type.ENGLISH_NAME, "TONNE");

  public static final Alias MINUTE_ENGLISH_NAME = create(Type.ENGLISH_NAME, "MINUTE");

  public static final Alias HOUR_ENGLISH_NAME = create(Type.ENGLISH_NAME, "HOUR");

  public static final Alias DAY_ENGLISH_NAME = create(Type.ENGLISH_NAME, "DAY");

  public static final Alias HALF_DAY_ENGLISH_NAME = create(Type.ENGLISH_NAME, "HALF_DAY");

  public static final Alias WEEK_ENGLISH_NAME = create(Type.ENGLISH_NAME, "WEEK");

  public static final Alias YEAR_ENGLISH_NAME = create(Type.ENGLISH_NAME, "YEAR");

  public static final Alias MONTH_ENGLISH_NAME = create(Type.ENGLISH_NAME, "MONTH");

  public static final Alias DECADE_ENGLISH_NAME = create(Type.ENGLISH_NAME, "DECADE");

  public static final Alias CENTURY_ENGLISH_NAME = create(Type.ENGLISH_NAME, "CENTURY");

  public static final Alias MILLENNIUM_ENGLISH_NAME = create(Type.ENGLISH_NAME, "MILLENNIUM");

  public static final Alias ERA_ENGLISH_NAME = create(Type.ENGLISH_NAME, "ERA");

  public static final Alias RADIAN_PLANE_ANGLE_ENGLISH_NAME = create(Type.ENGLISH_NAME,
      "RADIAN_PLANE_ANGLE");

  public static final Alias STERADIAN_SOLID_ANGLE_ENGLISH_NAME = create(Type.ENGLISH_NAME,
      "STERADIAN_SOLID_ANGLE");

  public static final Alias HERTZ_ENGLISH_NAME = create(Type.ENGLISH_NAME, "HERTZ");

  public static final Alias NEWTON_ENGLISH_NAME = create(Type.ENGLISH_NAME, "NEWTON");

  public static final Alias PASCAL_ENGLISH_NAME = create(Type.ENGLISH_NAME, "PASCAL");

  public static final Alias JOULE_ENGLISH_NAME = create(Type.ENGLISH_NAME, "JOULE");

  public static final Alias WATT_ENGLISH_NAME = create(Type.ENGLISH_NAME, "WATT");

  public static final Alias COULOMB_ENGLISH_NAME = create(Type.ENGLISH_NAME, "COULOMB");

  public static final Alias VOLT_ENGLISH_NAME = create(Type.ENGLISH_NAME, "VOLT");

  public static final Alias FARAD_ENGLISH_NAME = create(Type.ENGLISH_NAME, "FARAD");

  public static final Alias OHM_ENGLISH_NAME = create(Type.ENGLISH_NAME, "OHM");

  public static final Alias SIEMENS_ENGLISH_NAME = create(Type.ENGLISH_NAME, "SIEMENS");

  public static final Alias WEBER_ENGLISH_NAME = create(Type.ENGLISH_NAME, "WEBER");

  public static final Alias TESLA_ENGLISH_NAME = create(Type.ENGLISH_NAME, "TESLA");

  public static final Alias HENRY_ENGLISH_NAME = create(Type.ENGLISH_NAME, "HENRY");

  public static final Alias LUMEN_ENGLISH_NAME = create(Type.ENGLISH_NAME, "LUMEN");

  public static final Alias LUX_ENGLISH_NAME = create(Type.ENGLISH_NAME, "LUX");

  public static final Alias BECQUEREL_ENGLISH_NAME = create(Type.ENGLISH_NAME, "BECQUEREL");

  public static final Alias GRAY_ENGLISH_NAME = create(Type.ENGLISH_NAME, "GRAY");

  public static final Alias SIEVERT_ENGLISH_NAME = create(Type.ENGLISH_NAME, "SIEVERT");

  public static final Alias KATAL_ENGLISH_NAME = create(Type.ENGLISH_NAME, "KATAL");

  public static final Alias YOCTO_ENGLISH_NAME = create(Type.ENGLISH_NAME, "YOCTO");

  public static final Alias ZEPTO_ENGLISH_NAME = create(Type.ENGLISH_NAME, "ZEPTO");

  public static final Alias ATTO_ENGLISH_NAME = create(Type.ENGLISH_NAME, "ATTO");

  public static final Alias FEMTO_ENGLISH_NAME = create(Type.ENGLISH_NAME, "FEMTO");

  public static final Alias PICO_ENGLISH_NAME = create(Type.ENGLISH_NAME, "PICO");

  public static final Alias NANO_ENGLISH_NAME = create(Type.ENGLISH_NAME, "NANO");

  public static final Alias MICRO_ENGLISH_NAME = create(Type.ENGLISH_NAME, "MICRO");

  public static final Alias MILLI_ENGLISH_NAME = create(Type.ENGLISH_NAME, "MILLI");

  public static final Alias CENTI_ENGLISH_NAME = create(Type.ENGLISH_NAME, "CENTI");

  public static final Alias DECI_ENGLISH_NAME = create(Type.ENGLISH_NAME, "DECI");

  public static final Alias DECA_ENGLISH_NAME = create(Type.ENGLISH_NAME, "DECA");

  public static final Alias HECTO_ENGLISH_NAME = create(Type.ENGLISH_NAME, "HECTO");

  public static final Alias KILO_ENGLISH_NAME = create(Type.ENGLISH_NAME, "KILO");

  public static final Alias MEGA_ENGLISH_NAME = create(Type.ENGLISH_NAME, "MEGA");

  public static final Alias GIGA_ENGLISH_NAME = create(Type.ENGLISH_NAME, "GIGA");

  public static final Alias TERA_ENGLISH_NAME = create(Type.ENGLISH_NAME, "TERA");

  public static final Alias PETA_ENGLISH_NAME = create(Type.ENGLISH_NAME, "PETA");

  public static final Alias EXA_ENGLISH_NAME = create(Type.ENGLISH_NAME, "EXA");

  public static final Alias ZETTA_ENGLISH_NAME = create(Type.ENGLISH_NAME, "ZETTA");

  public static final Alias YOTTA_ENGLISH_NAME = create(Type.ENGLISH_NAME, "YOTTA");

  public static final Alias ANGSTROM_ENGLISH_NAME = create(Type.ENGLISH_NAME, "ANGSTROM");

  /**
   * 默认中文别名
   */
  public static final Alias LENGTH_CHINESE_NAME = create(Type.CHINESE_NAME, "长度");

  public static final Alias MASS_CHINESE_NAME = create(Type.CHINESE_NAME, "质量");

  public static final Alias TIME_CHINESE_NAME = create(Type.CHINESE_NAME, "时间");

  public static final Alias ELECTRIC_CURRENT_CHINESE_NAME = create(Type.CHINESE_NAME, "电流");

  public static final Alias TEMPERATURE_CHINESE_NAME = create(Type.CHINESE_NAME, "温度");

  public static final Alias SUBSTANCE_AMOUNT_CHINESE_NAME = create(Type.CHINESE_NAME, "物质的量");

  public static final Alias LUMINOUS_INTENSITY_CHINESE_NAME = create(Type.CHINESE_NAME, "发光强度");

  public static final Alias METER_CHINESE_NAME = create(Type.CHINESE_NAME, "米");

  public static final Alias GRAM_CHINESE_NAME = create(Type.CHINESE_NAME, "克");

  public static final Alias SECOND_CHINESE_NAME = create(Type.CHINESE_NAME, "秒");

  public static final Alias AMPERE_CHINESE_NAME = create(Type.CHINESE_NAME, "安培");

  public static final Alias KELVIN_CHINESE_NAME = create(Type.CHINESE_NAME, "开尔文");

  public static final Alias MOLE_CHINESE_NAME = create(Type.CHINESE_NAME, "摩尔");

  public static final Alias CANDELA_CHINESE_NAME = create(Type.CHINESE_NAME, "坎德拉");

  /**
   * 默认符号别名
   */
  public static final Alias CELSIUS_DEGREE_SYMBOL = create(Type.SYMBOL, "℃");

  public static final Alias FAHRENHEIT_DEGREE_SYMBOL = create(Type.SYMBOL, "℉");

  public static final Alias METER_SYMBOL = create(Type.SYMBOL, "m");

  public static final Alias GRAM_SYMBOL = create(Type.SYMBOL, "g");

  public static final Alias SECOND_SYMBOL = create(Type.SYMBOL, "s");

  public static final Alias AMPERE_SYMBOL = create(Type.SYMBOL, "A");

  public static final Alias KELVIN_SYMBOL = create(Type.SYMBOL, "K");

  public static final Alias MOLE_SYMBOL = create(Type.SYMBOL, "mol");

  public static final Alias CANDELA_SYMBOL = create(Type.SYMBOL, "cd");

  public static final Alias RADIAN_PLANE_ANGLE_SYMBOL = create(Type.SYMBOL, "rad");

  public static final Alias STERADIAN_SOLID_ANGLE_SYMBOL = create(Type.SYMBOL, "sr");

  public static final Alias HERTZ_SYMBOL = create(Type.SYMBOL, "Hz");

  public static final Alias NEWTON_SYMBOL = create(Type.SYMBOL, "N");

  public static final Alias PASCAL_SYMBOL = create(Type.SYMBOL, "Pa");

  public static final Alias JOULE_SYMBOL = create(Type.SYMBOL, "J");

  public static final Alias WATT_SYMBOL = create(Type.SYMBOL, "W");

  public static final Alias COULOMB_SYMBOL = create(Type.SYMBOL, "C");

  public static final Alias VOLT_SYMBOL = create(Type.SYMBOL, "V");

  public static final Alias FARAD_SYMBOL = create(Type.SYMBOL, "F");

  public static final Alias OHM_SYMBOL = create(Type.SYMBOL, "Ω");

  public static final Alias SIEMENS_SYMBOL = create(Type.SYMBOL, "S");

  public static final Alias WEBER_SYMBOL = create(Type.SYMBOL, "Wb");

  public static final Alias TESLA_SYMBOL = create(Type.SYMBOL, "T");

  public static final Alias HENRY_SYMBOL = create(Type.SYMBOL, "H");

  public static final Alias LUMEN_SYMBOL = create(Type.SYMBOL, "lm");

  public static final Alias LUX_SYMBOL = create(Type.SYMBOL, "lx");

  public static final Alias BECQUEREL_SYMBOL = create(Type.SYMBOL, "Bq");

  public static final Alias GRAY_SYMBOL = create(Type.SYMBOL, "Gy");

  public static final Alias SIEVERT_SYMBOL = create(Type.SYMBOL, "Sv");

  public static final Alias KATAL_SYMBOL = create(Type.SYMBOL, "kat");

  /**
   * 类型
   */
  @NonNull
  Type type;
  /**
   * 具体别名
   */
  @NonNull
  String value;
}
