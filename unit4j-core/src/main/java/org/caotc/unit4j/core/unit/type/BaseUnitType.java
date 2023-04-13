package org.caotc.unit4j.core.unit.type;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.NonNull;
import lombok.Value;

/**
 * 基本单位类型 由于基本单位类型没有任何有意义的关联属性,纯粹由定义产生,因此仅拥有id属性,用以定义和区分基本单位类型
 *
 * @author caotc
 * @date 2018-04-14
 * @since 1.0.0
 **/
@Value(staticConstructor = "create")
public class BaseUnitType implements UnitType {

  /**
   * 长度
   */
  public static final BaseUnitType LENGTH = create("LENGTH");
  /**
   * 质量
   */
  public static final BaseUnitType MASS = create("MASS");

  /**
   * 时间
   */
  public static final BaseUnitType TIME = create("TIME");

  /**
   * 电流
   */
  public static final BaseUnitType ELECTRIC_CURRENT = create("ELECTRIC_CURRENT");

  /**
   * 热力学温度
   */
  public static final BaseUnitType TEMPERATURE = create("TEMPERATURE");
  /**
   * 物质的量
   */
  public static final BaseUnitType SUBSTANCE_AMOUNT = create("SUBSTANCE_AMOUNT");
  /**
   * 发光强度
   */
  public static final BaseUnitType LUMINOUS_INTENSITY = create("LUMINOUS_INTENSITY");
  /**
   * 国际单位制中的7个基本单位类型不可变集合
   */
  private static final ImmutableSet<BaseUnitType> SI_BASE_UNIT_TYPES = ImmutableSet
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
  public static ImmutableSet<BaseUnitType> siBaseUnitTypes() {
    return SI_BASE_UNIT_TYPES;
  }

  @NonNull
  String id;

  @NonNull
  @Override
  public ImmutableMap<UnitType, Integer> unitTypeComponentToExponents() {
    return ImmutableMap.of(this, 1);
  }

  @NonNull
  @Override
  public UnitType rebase() {
    return this;
  }
}
