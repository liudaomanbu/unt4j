package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableSet;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.caotc.unit4j.core.unit.type.BaseUnitType;
import org.caotc.unit4j.core.unit.type.UnitType;

/**
 * @author caotc
 * @date 2023-04-18
 * @since 1.0.0
 */
@UtilityClass
public class UnitTypes {
    /**
     * 长度
     */
    public static final BaseUnitType LENGTH = UnitType.of("LENGTH");
    /**
     * 质量
     */
    public static final BaseUnitType MASS = UnitType.of("MASS");

    /**
     * 时间
     */
    public static final BaseUnitType TIME = UnitType.of("TIME");

    /**
     * 电流
     */
    public static final BaseUnitType ELECTRIC_CURRENT = UnitType.of("ELECTRIC_CURRENT");

    /**
     * 热力学温度
     */
    public static final BaseUnitType TEMPERATURE = UnitType.of("TEMPERATURE");
    /**
     * 物质的量
     */
    public static final BaseUnitType SUBSTANCE_AMOUNT = UnitType.of("SUBSTANCE_AMOUNT");
    /**
     * 发光强度
     */
    public static final BaseUnitType LUMINOUS_INTENSITY = UnitType.of("LUMINOUS_INTENSITY");
    /**
     * 无量纲单位类型
     */
    public static final UnitType NON = UnitType.builder().build();
    /**
     * 频率
     */
    public static final UnitType FREQUENCY = UnitType.builder()
            .componentToExponent(TIME, -1).build();
    /**
     * 力/重量
     */
    public static final UnitType FORCE_WEIGHT = UnitType.builder()
            .componentToExponent(MASS, 1)
            .componentToExponent(LENGTH, 1)
            .componentToExponent(TIME, -2).build();
    /**
     * 压强/应力
     */
    public static final UnitType PRESSURE_STRESS = UnitType.builder()
            .componentToExponent(MASS, 1)
            .componentToExponent(LENGTH, -1).componentToExponent(
                    TIME, -2).build();
    /**
     * 能量/功/热量
     */
    public static final UnitType ENERGY_WORK_HEAT_QUANTITY = UnitType.builder()
            .componentToExponent(MASS, 1)
            .componentToExponent(LENGTH, 2)
            .componentToExponent(TIME, -2).build();
    /**
     * 功率/辐射通量
     */
    public static final UnitType POWER_RADIANT_FLUX = UnitType.builder()
            .componentToExponent(MASS, 1)
            .componentToExponent(LENGTH, 2)
            .componentToExponent(TIME, -3).build();
    /**
     * 电荷
     */
    public static final UnitType ELECTRIC_CHARGE = UnitType.builder()
            .componentToExponent(TIME, 1)
            .componentToExponent(ELECTRIC_CURRENT, 1).build();
    /**
     * 电压(电势差)/电动势
     */
    public static final UnitType VOLTAGE_ELECTROMOTIVE_FORCE = UnitType.builder()
            .componentToExponent(MASS, 1)
            .componentToExponent(LENGTH, 2)
            .componentToExponent(TIME, -3)
            .componentToExponent(ELECTRIC_CURRENT, -1).build();
    /**
     * 电容
     */
    public static final UnitType CAPACITANCE = UnitType.builder()
            .componentToExponent(MASS, -1)
            .componentToExponent(LENGTH, -2).componentToExponent(
                    TIME, 4)
            .componentToExponent(ELECTRIC_CURRENT, 2).build();
    /**
     * 电阻/阻抗/电抗
     */
    public static final UnitType RESISTANCE_ELECTRICAL_IMPEDANCE_REACTANCE = UnitType.builder()
            .componentToExponent(MASS, 1)
            .componentToExponent(LENGTH, 2)
            .componentToExponent(TIME, -3)
            .componentToExponent(ELECTRIC_CURRENT, -2).build();
    /**
     * 电导
     */
    public static final UnitType ELECTRICAL_CONDUCTANCE = UnitType.builder()
            .componentToExponent(MASS, -1)
            .componentToExponent(LENGTH, -2).componentToExponent(
                    TIME, 3)
            .componentToExponent(ELECTRIC_CURRENT, 2).build();
    /**
     * 磁通量
     */
    public static final UnitType MAGNETIC_FLUX = UnitType.builder()
            .componentToExponent(MASS, 1)
            .componentToExponent(LENGTH, 2)
            .componentToExponent(TIME, -2)
            .componentToExponent(ELECTRIC_CURRENT, -1).build();
    /**
     * 磁通量密度(磁场)
     */
    public static final UnitType MAGNETIC_FLUX_DENSITY = UnitType.builder()
            .componentToExponent(MASS, 1)
            .componentToExponent(TIME, -2)
            .componentToExponent(ELECTRIC_CURRENT, -1).build();
    /**
     * 电感
     */
    public static final UnitType INDUCTANCE = UnitType.builder().componentToExponent(
                    MASS, 1)
            .componentToExponent(LENGTH, 2)
            .componentToExponent(TIME, -2)
            .componentToExponent(ELECTRIC_CURRENT, -2).build();
    /**
     * 光通量 Φ
     */
    public static final UnitType LUMINOUS_FLUX = LUMINOUS_INTENSITY;
    /**
     * 照度
     */
    public static final UnitType ILLUMINANCE = UnitType.builder()
            .componentToExponent(LENGTH, -2)
            .componentToExponent(LUMINOUS_INTENSITY, 1).build();
    /**
     * 放射性活度
     */
    public static final UnitType RADIOACTIVITY = UnitType.builder()
            .componentToExponent(TIME, -1).build();
    /**
     * 致电离辐射的吸收剂量
     */
    public static final UnitType ABSORBED_DOSE = UnitType.builder()
            .componentToExponent(LENGTH, 2)
            .componentToExponent(TIME, -2).build();
    /**
     * 致电离辐射等效剂量
     */
    public static final UnitType EQUIVALENT_DOSE = UnitType.builder()
            .componentToExponent(LENGTH, 2)
            .componentToExponent(TIME, -2).build();
    /**
     * 催化活度
     */
    public static final UnitType CATALYTIC_ACTIVITY = UnitType.builder()
            .componentToExponent(SUBSTANCE_AMOUNT, 1)
            .componentToExponent(TIME, -1).build();
    /**
     * 国际单位制中的7个基本单位类型不可变集合
     */
    private static final ImmutableSet<UnitType> SI_BASE_UNIT_TYPES = ImmutableSet
            .of(LENGTH, MASS, TIME, ELECTRIC_CURRENT, TEMPERATURE, SUBSTANCE_AMOUNT,
                    LUMINOUS_INTENSITY);

    /**
     * 国际单位制中的7个基本单位类型不可变集合
     *
     * @return 国际单位制中的7个基本单位类型不可变集合
     * @author caotc
     * @date 2019-05-26
     * @since 1.0.0
     */
    @NonNull
    public static ImmutableSet<UnitType> siUnitTypes() {
        return SI_BASE_UNIT_TYPES;
    }
}
