package org.caotc.unit4j.core.unit.type;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.unit.Alias;
import org.caotc.unit4j.core.util.Util;

/**
 * 组合单位类型,由基本到位和其他组合单位类型组合而成
 *
 * @author caotc
 * @date 2018-04-14
 * @since 1.0.0
 **/
@Value
@Builder(toBuilder = true)
public final class CompositeUnitType extends UnitType {

  /**
   * 无量纲单位类型
   */
  public static final CompositeUnitType NON = builder().build();

  /**
   * 频率
   */
  public static final CompositeUnitType FREQUENCY = builder()
      .unitTypeComponentToExponent(BaseUnitType.TIME, -1).build();

  /**
   * 力/重量
   */
  public static final CompositeUnitType FORCE_WEIGHT = builder()
      .unitTypeComponentToExponent(BaseUnitType.MASS, 1)
      .unitTypeComponentToExponent(BaseUnitType.LENGTH, 1)
      .unitTypeComponentToExponent(BaseUnitType.TIME, -2).build();

  /**
   * 压强/应力
   */
  public static final CompositeUnitType PRESSURE_STRESS = builder()
      .unitTypeComponentToExponent(BaseUnitType.MASS, 1)
      .unitTypeComponentToExponent(BaseUnitType.LENGTH, -1).unitTypeComponentToExponent(
          BaseUnitType.TIME, -2).build();

  /**
   * 能量/功/热量
   */
  public static final CompositeUnitType ENERGY_WORK_HEAT_QUANTITY = builder()
      .unitTypeComponentToExponent(BaseUnitType.MASS, 1)
      .unitTypeComponentToExponent(BaseUnitType.LENGTH, 2)
      .unitTypeComponentToExponent(BaseUnitType.TIME, -2).build();

  /**
   * 功率/辐射通量
   */
  public static final CompositeUnitType POWER_RADIANT_FLUX = builder()
      .unitTypeComponentToExponent(BaseUnitType.MASS, 1)
      .unitTypeComponentToExponent(BaseUnitType.LENGTH, 2)
      .unitTypeComponentToExponent(BaseUnitType.TIME, -3).build();

  /**
   * 电荷
   */
  public static final CompositeUnitType ELECTRIC_CHARGE = builder()
      .unitTypeComponentToExponent(BaseUnitType.TIME, 1)
      .unitTypeComponentToExponent(BaseUnitType.ELECTRIC_CURRENT, 1).build();

  /**
   * 电压(电势差)/电动势
   */
  public static final CompositeUnitType VOLTAGE_ELECTROMOTIVE_FORCE = builder()
      .unitTypeComponentToExponent(BaseUnitType.MASS, 1)
      .unitTypeComponentToExponent(BaseUnitType.LENGTH, 2)
      .unitTypeComponentToExponent(BaseUnitType.TIME, -3)
      .unitTypeComponentToExponent(BaseUnitType.ELECTRIC_CURRENT, -1).build();

  /**
   * 电容
   */
  public static final CompositeUnitType CAPACITANCE = builder()
      .unitTypeComponentToExponent(BaseUnitType.MASS, -1)
      .unitTypeComponentToExponent(BaseUnitType.LENGTH, -2).unitTypeComponentToExponent(
          BaseUnitType.TIME, 4)
      .unitTypeComponentToExponent(BaseUnitType.ELECTRIC_CURRENT, 2).build();

  /**
   * 电阻/阻抗/电抗
   */
  public static final CompositeUnitType RESISTANCE_ELECTRICAL_IMPEDANCE_REACTANCE = builder()
      .unitTypeComponentToExponent(BaseUnitType.MASS, 1)
      .unitTypeComponentToExponent(BaseUnitType.LENGTH, 2)
      .unitTypeComponentToExponent(BaseUnitType.TIME, -3)
      .unitTypeComponentToExponent(BaseUnitType.ELECTRIC_CURRENT, -2).build();

  /**
   * 电导
   */
  public static final CompositeUnitType ELECTRICAL_CONDUCTANCE = builder()
      .unitTypeComponentToExponent(BaseUnitType.MASS, -1)
      .unitTypeComponentToExponent(BaseUnitType.LENGTH, -2).unitTypeComponentToExponent(
          BaseUnitType.TIME, 3)
      .unitTypeComponentToExponent(BaseUnitType.ELECTRIC_CURRENT, 2).build();

  /**
   * 磁通量
   */
  public static final CompositeUnitType MAGNETIC_FLUX = builder()
      .unitTypeComponentToExponent(BaseUnitType.MASS, 1)
      .unitTypeComponentToExponent(BaseUnitType.LENGTH, 2)
      .unitTypeComponentToExponent(BaseUnitType.TIME, -2)
      .unitTypeComponentToExponent(BaseUnitType.ELECTRIC_CURRENT, -1).build();

  /**
   * 磁通量密度(磁场)
   */
  public static final CompositeUnitType MAGNETIC_FLUX_DENSITY = builder()
      .unitTypeComponentToExponent(BaseUnitType.MASS, 1)
      .unitTypeComponentToExponent(BaseUnitType.TIME, -2)
      .unitTypeComponentToExponent(BaseUnitType.ELECTRIC_CURRENT, -1).build();

  /**
   * 电感
   */
  public static final CompositeUnitType INDUCTANCE = builder().unitTypeComponentToExponent(
      BaseUnitType.MASS, 1)
      .unitTypeComponentToExponent(BaseUnitType.LENGTH, 2)
      .unitTypeComponentToExponent(BaseUnitType.TIME, -2)
      .unitTypeComponentToExponent(BaseUnitType.ELECTRIC_CURRENT, -2).build();
//  /**
//   * TODO 不同单位类型属性完全相同待处理
//   * 光通量 Φ
//   */
//  public static final CompositeUnitType LUMINOUS_FLUX = builder().unitTypeComponentToExponent(BaseUnitType.LUMINOUS_INTENSITY, 1).build();

  /**
   * 照度
   */
  public static final CompositeUnitType ILLUMINANCE = builder()
      .unitTypeComponentToExponent(BaseUnitType.LENGTH, -2)
      .unitTypeComponentToExponent(BaseUnitType.LUMINOUS_INTENSITY, 1).build();

  /**
   * 放射性活度
   */
  public static final CompositeUnitType RADIOACTIVITY = builder()
      .unitTypeComponentToExponent(BaseUnitType.TIME, -1).build();

  /**
   * 致电离辐射的吸收剂量
   */
  public static final CompositeUnitType ABSORBED_DOSE = builder()
      .unitTypeComponentToExponent(BaseUnitType.LENGTH, 2)
      .unitTypeComponentToExponent(BaseUnitType.TIME, -2).build();

  /**
   * 致电离辐射等效剂量
   */
  public static final CompositeUnitType EQUIVALENT_DOSE = builder()
      .unitTypeComponentToExponent(BaseUnitType.LENGTH, 2)
      .unitTypeComponentToExponent(BaseUnitType.TIME, -2).build();

  /**
   * 催化活度
   */
  public static final CompositeUnitType CATALYTIC_ACTIVITY = builder()
      .unitTypeComponentToExponent(BaseUnitType.SUBSTANCE_AMOUNT, 1)
      .unitTypeComponentToExponent(BaseUnitType.TIME, -1).build();

  /**
   * 单位类型组件与对应指数
   */
  @NonNull
  @Singular
  ImmutableMap<UnitType, Integer> unitTypeComponentToExponents;

  @NonNull
  @Override
  public String id() {
    return Util.createCompositeIdOrAlias(this.unitTypeComponentToExponents());
  }

  @NonNull
  @Override
  public UnitType rebase() {
    return builder().unitTypeComponentToExponents(unitTypeComponentToExponents().entrySet().stream()
        .map(entry -> entry.getKey().rebase().unitTypeComponentToExponents().entrySet().stream()
            .collect(
                ImmutableMap.toImmutableMap(Entry::getKey, e -> e.getValue() * entry.getValue())))
        .map(Map::entrySet)
        .flatMap(Collection::stream)
        .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue, Integer::sum)))
        .build();
  }

  /**
   * 乘法{@code this * multiplicand} //TODO 待移到父类
   *
   * @param multiplicand 被乘数
   * @return {@code this * multiplicand}
   * @author caotc
   * @date 2019-01-10
   * @since 1.0.0
   */
  @NonNull
  public CompositeUnitType multiply(@NonNull CompositeUnitType multiplicand) {
    ImmutableMap<UnitType, Integer> unitTypeToIndexMap = Stream
        .of(this.unitTypeComponentToExponents(), multiplicand.unitTypeComponentToExponents())
        .map(Map::entrySet)
        .flatMap(Collection::stream)
        .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue, Integer::sum));
    return builder().unitTypeComponentToExponents(unitTypeToIndexMap).build();
  }

  /**
   * 除法{@code (this / divisor)}
   *
   * @param divisor 除数
   * @return {@code this / divisor}
   * @author caotc
   * @date 2019-01-10
   * @since 1.0.0
   */
  @NonNull
  public CompositeUnitType divide(@NonNull CompositeUnitType divisor) {
    return multiply(divisor.inverse());
  }

  /**
   * 反转
   *
   * @return 组件不变, 指数取反的单位类型
   * @author caotc
   * @date 2019-01-11
   * @since 1.0.0
   */
  @SuppressWarnings("ConstantConditions")
  @NonNull
  public CompositeUnitType inverse() {
    return builder().unitTypeComponentToExponents(
        Maps.transformValues(this.unitTypeComponentToExponents(), exponent -> -exponent)).build();
  }

  @Override
  public @NonNull ImmutableSet<Alias> aliasesFromConfiguration(
      @NonNull Configuration configuration) {
    return configuration.aliases(this);
  }

  @Override
  public @NonNull Optional<Alias> aliasFromConfiguration(@NonNull Configuration configuration,
      @NonNull Alias.Type aliasType) {
    return configuration.alias(this, aliasType);
  }

  @Override
  public @NonNull Optional<Alias> compositeAliasFromConfiguration(
      @NonNull Configuration configuration, @NonNull Alias.Type aliasType) {
    Optional<Alias> alias = aliasFromConfiguration(configuration, aliasType);
    if (alias.isPresent()) {
      return alias;
    }
    boolean componentAliased = unitTypeComponentToExponents().keySet().stream()
        .map(unit -> unit.compositeAliasFromConfiguration(configuration, aliasType))
        .allMatch(Optional::isPresent);
    if (componentAliased) {
      String compositeAlias = Util.createCompositeIdOrAlias(
          unitTypeComponentToExponents().entrySet().stream()
              .collect(ImmutableMap.toImmutableMap(entry -> (() -> entry.getKey()
                      .compositeAliasFromConfiguration(configuration, aliasType).map(Alias::value)
                      .get())
                  , Entry::getValue)));
      return Optional.of(Alias.create(aliasType, compositeAlias));
    }
    return Optional.empty();
  }

  public static InternalBuilder builder() {
    return new InternalBuilder();
  }

  public static class InternalBuilder extends CompositeUnitTypeBuilder {

    @Override
    public CompositeUnitTypeBuilder unitTypeComponentToExponent(UnitType key, Integer value) {
      if (value == 0) {
        return this;
      }
      return super.unitTypeComponentToExponent(key, value);
    }

    @Override
    public CompositeUnitTypeBuilder unitTypeComponentToExponents(
        Map<? extends UnitType, ? extends Integer> unitTypeComponentToExponents) {
      return super.unitTypeComponentToExponents(
          Maps.filterValues(unitTypeComponentToExponents, exponent -> exponent != 0));
    }

    @Override
    public CompositeUnitType build() {
      return super.build();
    }
  }
}
