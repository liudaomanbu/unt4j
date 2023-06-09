package org.caotc.unit4j.core;

import com.google.common.collect.ImmutableSet;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.caotc.unit4j.core.unit.UnitConstant;
import org.caotc.unit4j.core.unit.UnitTypes;

/**
 * @author caotc
 * @date 2023-06-09
 * @since 1.0.0
 */
@UtilityClass
public class Aliases {

    /**
     * 默认英文别名
     */
    public static final Alias LENGTH_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitTypes.LENGTH.id());
    public static final Alias MASS_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitTypes.MASS.id());
    public static final Alias TIME_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitTypes.TIME.id());
    public static final Alias ELECTRIC_CURRENT_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME,
            UnitTypes.ELECTRIC_CURRENT.id());
    public static final Alias TEMPERATURE_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitTypes.TEMPERATURE.id());
    public static final Alias SUBSTANCE_AMOUNT_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME,
            UnitTypes.SUBSTANCE_AMOUNT.id());
    public static final Alias LUMINOUS_INTENSITY_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME,
            UnitTypes.LUMINOUS_INTENSITY.id());
    public static final Alias METER_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.METER.id());
    public static final Alias GRAM_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.GRAM.id());
    public static final Alias SECOND_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.SECOND.id());
    public static final Alias AMPERE_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.AMPERE.id());
    public static final Alias KELVIN_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.KELVIN.id());
    public static final Alias MOLE_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.MOLE.id());
    public static final Alias CANDELA_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.CANDELA.id());
    public static final Alias CELSIUS_DEGREE_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME,
            UnitConstant.CELSIUS_DEGREE.id());
    public static final Alias FAHRENHEIT_DEGREE_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME,
            UnitConstant.FAHRENHEIT_DEGREE.id());
    public static final Alias CHINESE_MILE_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.CHINESE_MILE.id());
    public static final Alias TANG_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.TANG.id());
    public static final Alias ZHANG_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.ZHANG.id());
    public static final Alias YIN_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.YIN.id());
    public static final Alias CHI_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.CHI.id());
    public static final Alias CUN_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.CUN.id());
    public static final Alias FEN_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.FEN.id());
    public static final Alias LI_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.LI.id());
    public static final Alias INCH_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.INCH.id());
    public static final Alias FOOT_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.FOOT.id());
    public static final Alias YARD_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.YARD.id());
    public static final Alias FATHOM_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.FATHOM.id());
    public static final Alias CHAIN_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.CHAIN.id());
    public static final Alias FURLONG_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.FURLONG.id());
    public static final Alias MILE_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.MILE.id());
    public static final Alias TONNE_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.TONNE.id());
    public static final Alias MINUTE_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.MINUTE.id());
    public static final Alias HOUR_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.HOUR.id());
    public static final Alias DAY_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.DAY.id());
    public static final Alias HALF_DAY_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.HALF_DAY.id());
    public static final Alias WEEK_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.WEEK.id());
    public static final Alias YEAR_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.YEAR.id());
    public static final Alias MONTH_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.MONTH.id());
    public static final Alias DECADE_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.DECADE.id());
    public static final Alias CENTURY_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.CENTURY.id());
    public static final Alias MILLENNIUM_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.MILLENNIUM.id());
    public static final Alias ERA_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, UnitConstant.ERA.id());
    public static final Alias RADIAN_PLANE_ANGLE_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME,
            "RADIAN_PLANE_ANGLE");
    public static final Alias STERADIAN_SOLID_ANGLE_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME,
            "STERADIAN_SOLID_ANGLE");
    public static final Alias HERTZ_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "HERTZ");
    public static final Alias NEWTON_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "NEWTON");
    public static final Alias PASCAL_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "PASCAL");
    public static final Alias JOULE_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "JOULE");
    public static final Alias WATT_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "WATT");
    public static final Alias COULOMB_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "COULOMB");
    public static final Alias VOLT_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "VOLT");
    public static final Alias FARAD_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "FARAD");
    public static final Alias OHM_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "OHM");
    public static final Alias SIEMENS_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "SIEMENS");
    public static final Alias WEBER_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "WEBER");
    public static final Alias TESLA_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "TESLA");
    public static final Alias HENRY_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "HENRY");
    public static final Alias LUMEN_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "LUMEN");
    public static final Alias LUX_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "LUX");
    public static final Alias BECQUEREL_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "BECQUEREL");
    public static final Alias GRAY_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "GRAY");
    public static final Alias SIEVERT_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "SIEVERT");
    public static final Alias KATAL_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "KATAL");
    public static final Alias YOCTO_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "YOCTO");
    public static final Alias ZEPTO_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "ZEPTO");
    public static final Alias ATTO_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "ATTO");
    public static final Alias FEMTO_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "FEMTO");
    public static final Alias PICO_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "PICO");
    public static final Alias NANO_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "NANO");
    public static final Alias MICRO_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "MICRO");
    public static final Alias MILLI_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "MILLI");
    public static final Alias CENTI_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "CENTI");
    public static final Alias DECI_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "DECI");
    public static final Alias DECA_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "DECA");
    public static final Alias HECTO_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "HECTO");
    public static final Alias KILO_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "KILO");
    public static final Alias MEGA_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "MEGA");
    public static final Alias GIGA_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "GIGA");
    public static final Alias TERA_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "TERA");
    public static final Alias PETA_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "PETA");
    public static final Alias EXA_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "EXA");
    public static final Alias ZETTA_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "ZETTA");
    public static final Alias YOTTA_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "YOTTA");
    public static final Alias ANGSTROM_ENGLISH_NAME = Alias.create(Types.ENGLISH_NAME, "ANGSTROM");
    /**
     * 默认中文别名
     */
    public static final Alias LENGTH_CHINESE_NAME = Alias.create(Types.CHINESE_NAME, "长度");
    public static final Alias MASS_CHINESE_NAME = Alias.create(Types.CHINESE_NAME, "质量");
    public static final Alias TIME_CHINESE_NAME = Alias.create(Types.CHINESE_NAME, "时间");
    public static final Alias ELECTRIC_CURRENT_CHINESE_NAME = Alias.create(Types.CHINESE_NAME, "电流");
    public static final Alias TEMPERATURE_CHINESE_NAME = Alias.create(Types.CHINESE_NAME, "温度");
    public static final Alias SUBSTANCE_AMOUNT_CHINESE_NAME = Alias.create(Types.CHINESE_NAME, "物质的量");
    public static final Alias LUMINOUS_INTENSITY_CHINESE_NAME = Alias.create(Types.CHINESE_NAME, "发光强度");
    public static final Alias METER_CHINESE_NAME = Alias.create(Types.CHINESE_NAME, "米");
    public static final Alias GRAM_CHINESE_NAME = Alias.create(Types.CHINESE_NAME, "克");
    public static final Alias SECOND_CHINESE_NAME = Alias.create(Types.CHINESE_NAME, "秒");
    public static final Alias AMPERE_CHINESE_NAME = Alias.create(Types.CHINESE_NAME, "安培");
    public static final Alias KELVIN_CHINESE_NAME = Alias.create(Types.CHINESE_NAME, "开尔文");
    public static final Alias MOLE_CHINESE_NAME = Alias.create(Types.CHINESE_NAME, "摩尔");
    public static final Alias CANDELA_CHINESE_NAME = Alias.create(Types.CHINESE_NAME, "坎德拉");
    /**
     * 默认符号别名
     */
    public static final Alias CELSIUS_DEGREE_SYMBOL = Alias.create(Types.SYMBOL, "℃");
    public static final Alias FAHRENHEIT_DEGREE_SYMBOL = Alias.create(Types.SYMBOL, "℉");
    public static final Alias METER_SYMBOL = Alias.create(Types.SYMBOL, "m");
    public static final Alias GRAM_SYMBOL = Alias.create(Types.SYMBOL, "g");
    public static final Alias SECOND_SYMBOL = Alias.create(Types.SYMBOL, "s");
    public static final Alias AMPERE_SYMBOL = Alias.create(Types.SYMBOL, "A");
    public static final Alias KELVIN_SYMBOL = Alias.create(Types.SYMBOL, "K");
    public static final Alias MOLE_SYMBOL = Alias.create(Types.SYMBOL, "mol");
    public static final Alias CANDELA_SYMBOL = Alias.create(Types.SYMBOL, "cd");
    public static final Alias RADIAN_PLANE_ANGLE_SYMBOL = Alias.create(Types.SYMBOL, "rad");
    public static final Alias STERADIAN_SOLID_ANGLE_SYMBOL = Alias.create(Types.SYMBOL, "sr");
    public static final Alias HERTZ_SYMBOL = Alias.create(Types.SYMBOL, "Hz");
    public static final Alias NEWTON_SYMBOL = Alias.create(Types.SYMBOL, "N");
    public static final Alias PASCAL_SYMBOL = Alias.create(Types.SYMBOL, "Pa");
    public static final Alias JOULE_SYMBOL = Alias.create(Types.SYMBOL, "J");
    public static final Alias WATT_SYMBOL = Alias.create(Types.SYMBOL, "W");
    public static final Alias COULOMB_SYMBOL = Alias.create(Types.SYMBOL, "C");
    public static final Alias VOLT_SYMBOL = Alias.create(Types.SYMBOL, "V");
    public static final Alias FARAD_SYMBOL = Alias.create(Types.SYMBOL, "F");
    public static final Alias OHM_SYMBOL = Alias.create(Types.SYMBOL, "Ω");
    public static final Alias SIEMENS_SYMBOL = Alias.create(Types.SYMBOL, "S");
    public static final Alias WEBER_SYMBOL = Alias.create(Types.SYMBOL, "Wb");
    public static final Alias TESLA_SYMBOL = Alias.create(Types.SYMBOL, "T");
    public static final Alias HENRY_SYMBOL = Alias.create(Types.SYMBOL, "H");
    public static final Alias LUMEN_SYMBOL = Alias.create(Types.SYMBOL, "lm");
    public static final Alias LUX_SYMBOL = Alias.create(Types.SYMBOL, "lx");
    public static final Alias BECQUEREL_SYMBOL = Alias.create(Types.SYMBOL, "Bq");
    public static final Alias GRAY_SYMBOL = Alias.create(Types.SYMBOL, "Gy");
    public static final Alias SIEVERT_SYMBOL = Alias.create(Types.SYMBOL, "Sv");
    public static final Alias KATAL_SYMBOL = Alias.create(Types.SYMBOL, "kat");
    public static final ImmutableSet<Alias> LENGTH_ALIASES = ImmutableSet.of(LENGTH_ENGLISH_NAME, LENGTH_CHINESE_NAME);
    public static final ImmutableSet<Alias> MASS_ALIASES = ImmutableSet.of(MASS_ENGLISH_NAME, MASS_CHINESE_NAME);
    public static final ImmutableSet<Alias> TIME_ALIASES = ImmutableSet.of(TIME_ENGLISH_NAME, TIME_CHINESE_NAME);
    public static final ImmutableSet<Alias> ELECTRIC_CURRENT_ALIASES = ImmutableSet.of(ELECTRIC_CURRENT_ENGLISH_NAME,
            ELECTRIC_CURRENT_CHINESE_NAME);
    public static final ImmutableSet<Alias> TEMPERATURE_ALIASES = ImmutableSet.of(TEMPERATURE_ENGLISH_NAME,
            TEMPERATURE_CHINESE_NAME);
    public static final ImmutableSet<Alias> SUBSTANCE_AMOUNT_ALIASES = ImmutableSet.of(SUBSTANCE_AMOUNT_ENGLISH_NAME,
            SUBSTANCE_AMOUNT_CHINESE_NAME);
    public static final ImmutableSet<Alias> LUMINOUS_INTENSITY_ALIASES = ImmutableSet.of(LUMINOUS_INTENSITY_ENGLISH_NAME,
            LUMINOUS_INTENSITY_CHINESE_NAME);
    public static final ImmutableSet<Alias> YOCTO_ALIASES = ImmutableSet.of(YOCTO_ENGLISH_NAME);
    public static final ImmutableSet<Alias> ZEPTO_ALIASES = ImmutableSet.of(ZEPTO_ENGLISH_NAME);
    public static final ImmutableSet<Alias> ATTO_ALIASES = ImmutableSet.of(ATTO_ENGLISH_NAME);
    public static final ImmutableSet<Alias> FEMTO_ALIASES = ImmutableSet.of(FEMTO_ENGLISH_NAME);
    public static final ImmutableSet<Alias> PICO_ALIASES = ImmutableSet.of(PICO_ENGLISH_NAME);
    public static final ImmutableSet<Alias> NANO_ALIASES = ImmutableSet.of(NANO_ENGLISH_NAME);
    public static final ImmutableSet<Alias> MICRO_ALIASES = ImmutableSet.of(MICRO_ENGLISH_NAME);
    public static final ImmutableSet<Alias> MILLI_ALIASES = ImmutableSet.of(MILLI_ENGLISH_NAME);
    public static final ImmutableSet<Alias> CENTI_ALIASES = ImmutableSet.of(CENTI_ENGLISH_NAME);
    public static final ImmutableSet<Alias> DECI_ALIASES = ImmutableSet.of(DECI_ENGLISH_NAME);
    public static final ImmutableSet<Alias> DECA_ALIASES = ImmutableSet.of(DECA_ENGLISH_NAME);
    public static final ImmutableSet<Alias> HECTO_ALIASES = ImmutableSet.of(HECTO_ENGLISH_NAME);
    public static final ImmutableSet<Alias> KILO_ALIASES = ImmutableSet.of(KILO_ENGLISH_NAME);
    public static final ImmutableSet<Alias> MEGA_ALIASES = ImmutableSet.of(MEGA_ENGLISH_NAME);
    public static final ImmutableSet<Alias> GIGA_ALIASES = ImmutableSet.of(GIGA_ENGLISH_NAME);
    public static final ImmutableSet<Alias> TERA_ALIASES = ImmutableSet.of(TERA_ENGLISH_NAME);
    public static final ImmutableSet<Alias> EXA_ALIASES = ImmutableSet.of(EXA_ENGLISH_NAME);
    public static final ImmutableSet<Alias> ZETTA_ALIASES = ImmutableSet.of(ZETTA_ENGLISH_NAME);
    public static final ImmutableSet<Alias> YOTTA_ALIASES = ImmutableSet.of(YOTTA_ENGLISH_NAME);
    public static final ImmutableSet<Alias> ANGSTROM_ALIASES = ImmutableSet.of(ANGSTROM_ENGLISH_NAME);
    public static final ImmutableSet<Alias> METER_ALIASES = ImmutableSet.of(METER_ENGLISH_NAME, METER_CHINESE_NAME,
            METER_SYMBOL);
    public static final ImmutableSet<Alias> GRAM_ALIASES = ImmutableSet.of(GRAM_ENGLISH_NAME, GRAM_CHINESE_NAME,
            GRAM_SYMBOL);
    public static final ImmutableSet<Alias> SECOND_ALIASES = ImmutableSet.of(SECOND_ENGLISH_NAME, SECOND_CHINESE_NAME,
            SECOND_SYMBOL);
    public static final ImmutableSet<Alias> AMPERE_ALIASES = ImmutableSet.of(AMPERE_ENGLISH_NAME, AMPERE_CHINESE_NAME,
            AMPERE_SYMBOL);
    public static final ImmutableSet<Alias> KELVIN_ALIASES = ImmutableSet.of(KELVIN_ENGLISH_NAME, KELVIN_CHINESE_NAME,
            KELVIN_SYMBOL);
    public static final ImmutableSet<Alias> MOLE_ALIASES = ImmutableSet.of(MOLE_ENGLISH_NAME, MOLE_CHINESE_NAME,
            MOLE_SYMBOL);
    public static final ImmutableSet<Alias> CANDELA_ALIASES = ImmutableSet.of(CANDELA_ENGLISH_NAME, CANDELA_CHINESE_NAME,
            CANDELA_SYMBOL);
    public static final ImmutableSet<Alias> CELSIUS_DEGREE_ALIASES = ImmutableSet.of(CELSIUS_DEGREE_ENGLISH_NAME,
            CELSIUS_DEGREE_SYMBOL);
    public static final ImmutableSet<Alias> FAHRENHEIT_DEGREE_ALIASES = ImmutableSet.of(FAHRENHEIT_DEGREE_ENGLISH_NAME,
            FAHRENHEIT_DEGREE_SYMBOL);
    public static final ImmutableSet<Alias> CHINESE_MILE_ALIASES = ImmutableSet.of(CHINESE_MILE_ENGLISH_NAME);
    public static final ImmutableSet<Alias> TANG_ALIASES = ImmutableSet.of(TANG_ENGLISH_NAME);
    public static final ImmutableSet<Alias> ZHANG_ALIASES = ImmutableSet.of(ZHANG_ENGLISH_NAME);
    public static final ImmutableSet<Alias> YIN_ALIASES = ImmutableSet.of(YIN_ENGLISH_NAME);
    public static final ImmutableSet<Alias> CHI_ALIASES = ImmutableSet.of(CHI_ENGLISH_NAME);
    public static final ImmutableSet<Alias> CUN_ALIASES = ImmutableSet.of(CUN_ENGLISH_NAME);
    public static final ImmutableSet<Alias> FEN_ALIASES = ImmutableSet.of(FEN_ENGLISH_NAME);
    public static final ImmutableSet<Alias> LI_ALIASES = ImmutableSet.of(LI_ENGLISH_NAME);
    public static final ImmutableSet<Alias> INCH_ALIASES = ImmutableSet.of(INCH_ENGLISH_NAME);
    public static final ImmutableSet<Alias> FOOT_ALIASES = ImmutableSet.of(FOOT_ENGLISH_NAME);
    public static final ImmutableSet<Alias> YARD_ALIASES = ImmutableSet.of(YARD_ENGLISH_NAME);
    public static final ImmutableSet<Alias> FATHOM_ALIASES = ImmutableSet.of(FATHOM_ENGLISH_NAME);
    public static final ImmutableSet<Alias> CHAIN_ALIASES = ImmutableSet.of(CHAIN_ENGLISH_NAME);
    public static final ImmutableSet<Alias> FURLONG_ALIASES = ImmutableSet.of(FURLONG_ENGLISH_NAME);
    public static final ImmutableSet<Alias> MILE_ALIASES = ImmutableSet.of(MILE_ENGLISH_NAME);
    public static final ImmutableSet<Alias> TONNE_ALIASES = ImmutableSet.of(TONNE_ENGLISH_NAME);
    public static final ImmutableSet<Alias> MINUTE_ALIASES = ImmutableSet.of(MINUTE_ENGLISH_NAME);
    public static final ImmutableSet<Alias> HOUR_ALIASES = ImmutableSet.of(HOUR_ENGLISH_NAME);
    public static final ImmutableSet<Alias> DAY_ALIASES = ImmutableSet.of(DAY_ENGLISH_NAME);
    public static final ImmutableSet<Alias> HALF_DAY_ALIASES = ImmutableSet.of(HALF_DAY_ENGLISH_NAME);
    public static final ImmutableSet<Alias> WEEK_ALIASES = ImmutableSet.of(WEEK_ENGLISH_NAME);
    public static final ImmutableSet<Alias> YEAR_ALIASES = ImmutableSet.of(YEAR_ENGLISH_NAME);
    public static final ImmutableSet<Alias> MONTH_ALIASES = ImmutableSet.of(MONTH_ENGLISH_NAME);
    public static final ImmutableSet<Alias> DECADE_ALIASES = ImmutableSet.of(DECADE_ENGLISH_NAME);
    public static final ImmutableSet<Alias> CENTURY_ALIASES = ImmutableSet.of(CENTURY_ENGLISH_NAME);
    public static final ImmutableSet<Alias> MILLENNIUM_ALIASES = ImmutableSet.of(MILLENNIUM_ENGLISH_NAME);
    public static final ImmutableSet<Alias> ERA_ALIASES = ImmutableSet.of(ERA_ENGLISH_NAME);
    public static final ImmutableSet<Alias> HERTZ_ALIASES = ImmutableSet.of(HERTZ_ENGLISH_NAME, HERTZ_SYMBOL);
    public static final ImmutableSet<Alias> NEWTON_ALIASES = ImmutableSet.of(NEWTON_ENGLISH_NAME, NEWTON_SYMBOL);
    public static final ImmutableSet<Alias> PASCAL_ALIASES = ImmutableSet.of(PASCAL_ENGLISH_NAME, PASCAL_SYMBOL);
    public static final ImmutableSet<Alias> JOULE_ALIASES = ImmutableSet.of(JOULE_ENGLISH_NAME, JOULE_SYMBOL);
    public static final ImmutableSet<Alias> WATT_ALIASES = ImmutableSet.of(WATT_ENGLISH_NAME, WATT_SYMBOL);
    public static final ImmutableSet<Alias> COULOMB_ALIASES = ImmutableSet.of(COULOMB_ENGLISH_NAME, COULOMB_SYMBOL);
    public static final ImmutableSet<Alias> VOLT_ALIASES = ImmutableSet.of(VOLT_ENGLISH_NAME, VOLT_SYMBOL);
    public static final ImmutableSet<Alias> FARAD_ALIASES = ImmutableSet.of(FARAD_ENGLISH_NAME, FARAD_SYMBOL);
    public static final ImmutableSet<Alias> OHM_ALIASES = ImmutableSet.of(OHM_ENGLISH_NAME, OHM_SYMBOL);
    public static final ImmutableSet<Alias> SIEMENS_ALIASES = ImmutableSet.of(SIEMENS_ENGLISH_NAME, SIEMENS_SYMBOL);
    public static final ImmutableSet<Alias> WEBER_ALIASES = ImmutableSet.of(WEBER_ENGLISH_NAME, WEBER_SYMBOL);
    public static final ImmutableSet<Alias> TESLA_ALIASES = ImmutableSet.of(TESLA_ENGLISH_NAME, TESLA_SYMBOL);
    public static final ImmutableSet<Alias> HENRY_ALIASES = ImmutableSet.of(HENRY_ENGLISH_NAME, HENRY_SYMBOL);
    public static final ImmutableSet<Alias> LUMEN_ALIASES = ImmutableSet.of(LUMEN_ENGLISH_NAME, LUMEN_SYMBOL);
    public static final ImmutableSet<Alias> LUX_ALIASES = ImmutableSet.of(LUX_ENGLISH_NAME, LUX_SYMBOL);
    public static final ImmutableSet<Alias> BECQUEREL_ALIASES = ImmutableSet.of(BECQUEREL_ENGLISH_NAME, BECQUEREL_SYMBOL);
    public static final ImmutableSet<Alias> GRAY_ALIASES = ImmutableSet.of(GRAY_ENGLISH_NAME, GRAY_SYMBOL);
    public static final ImmutableSet<Alias> SIEVERT_ALIASES = ImmutableSet.of(SIEVERT_ENGLISH_NAME, SIEVERT_SYMBOL);
    public static final ImmutableSet<Alias> KATAL_ALIASES = ImmutableSet.of(KATAL_ENGLISH_NAME, KATAL_SYMBOL);

    @NonNull
    public static ImmutableSet<Alias> lengthAliases() {
        return LENGTH_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> massAliases() {
        return MASS_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> timeAliases() {
        return TIME_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> electricCurrentAliases() {
        return ELECTRIC_CURRENT_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> temperatureAliases() {
        return TEMPERATURE_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> substanceAmountAliases() {
        return SUBSTANCE_AMOUNT_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> luminousIntensityAliases() {
        return LUMINOUS_INTENSITY_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> yoctoAliases() {
        return YOCTO_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> zeptoAliases() {
        return ZEPTO_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> attoAliases() {
        return ATTO_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> femtoAliases() {
        return FEMTO_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> picoAliases() {
        return PICO_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> nanoAliases() {
        return NANO_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> microAliases() {
        return MICRO_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> milliAliases() {
        return MILLI_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> centiAliases() {
        return CENTI_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> deciAliases() {
        return DECI_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> decaAliases() {
        return DECA_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> hectoAliases() {
        return HECTO_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> kiloAliases() {
        return KILO_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> megaAliases() {
        return MEGA_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> gigaAliases() {
        return GIGA_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> teraAliases() {
        return TERA_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> exaAliases() {
        return EXA_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> zettaAliases() {
        return ZETTA_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> yottaAliases() {
        return YOTTA_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> angstromAliases() {
        return ANGSTROM_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> meterAliases() {
        return METER_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> gramAliases() {
        return GRAM_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> secondAliases() {
        return SECOND_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> ampereAliases() {
        return AMPERE_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> kelvinAliases() {
        return KELVIN_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> moleAliases() {
        return MOLE_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> candelaAliases() {
        return CANDELA_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> celsiusDegreeAliases() {
        return CELSIUS_DEGREE_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> fahrenheitDegreeAliases() {
        return FAHRENHEIT_DEGREE_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> chineseMileAliases() {
        return CHINESE_MILE_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> tangAliases() {
        return TANG_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> zhangAliases() {
        return ZHANG_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> yinAliases() {
        return YIN_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> chiAliases() {
        return CHI_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> cunAliases() {
        return CUN_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> fenAliases() {
        return FEN_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> liAliases() {
        return LI_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> inchAliases() {
        return INCH_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> footAliases() {
        return FOOT_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> yardAliases() {
        return YARD_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> fathomAliases() {
        return FATHOM_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> chainAliases() {
        return CHAIN_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> furlongAliases() {
        return FURLONG_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> mileAliases() {
        return MILE_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> tonneAliases() {
        return TONNE_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> minuteAliases() {
        return MINUTE_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> hourAliases() {
        return HOUR_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> dayAliases() {
        return DAY_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> halfDayAliases() {
        return HALF_DAY_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> weekAliases() {
        return WEEK_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> yearAliases() {
        return YEAR_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> monthAliases() {
        return MONTH_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> decadeAliases() {
        return DECADE_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> centuryAliases() {
        return CENTURY_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> millenniumAliases() {
        return MILLENNIUM_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> eraAliases() {
        return ERA_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> hertzAliases() {
        return HERTZ_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> newtonAliases() {
        return NEWTON_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> pascalAliases() {
        return PASCAL_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> jouleAliases() {
        return JOULE_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> wattAliases() {
        return WATT_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> coulombAliases() {
        return COULOMB_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> voltAliases() {
        return VOLT_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> faradAliases() {
        return FARAD_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> ohmAliases() {
        return OHM_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> siemensAliases() {
        return SIEMENS_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> weberAliases() {
        return WEBER_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> teslaAliases() {
        return TESLA_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> henryAliases() {
        return HENRY_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> lumenAliases() {
        return LUMEN_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> luxAliases() {
        return LUX_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> becquerelAliases() {
        return BECQUEREL_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> grayAliases() {
        return GRAY_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> sievertAliases() {
        return SIEVERT_ALIASES;
    }

    @NonNull
    public static ImmutableSet<Alias> katalAliases() {
        return KATAL_ALIASES;
    }

    @UtilityClass
    public static class Types {

        /**
         * 英文名称
         */
        public static final Alias.Type ENGLISH_NAME = Alias.Type.create("ENGLISH_NAME");
        /**
         * 英文名称
         */
        public static final Alias.Type CHINESE_NAME = Alias.Type.create("CHINESE_NAME");
        /**
         * 缩写
         */
        public static final Alias.Type ABBREVIATION = Alias.Type.create("ABBREVIATION");
        /**
         * 符号
         */
        public static final Alias.Type SYMBOL = Alias.Type.create("SYMBOL");
    }
}
