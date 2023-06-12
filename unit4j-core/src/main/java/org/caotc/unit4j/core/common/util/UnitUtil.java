package org.caotc.unit4j.core.common.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.constant.StringConstant;
import org.caotc.unit4j.core.math.number.AbstractNumber;
import org.caotc.unit4j.core.unit.CompositeStandardUnit;
import org.caotc.unit4j.core.unit.Unit;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * 单位工具类,封装一些基本使用
 *
 * @author caotc
 * @date 2019-05-15
 * @since 1.0.0
 */
@UtilityClass
public class UnitUtil {

  /**
   * 默认配置
   */
  private static final Configuration DEFAULT_CONFIGURATION = Configuration.defaultInstance();
  /**
   * unit1/unit2的正则,视为unit1*unit2⁻¹的特殊形态
   */
  private static final String UNIT_REGEX = "\\w+" + StringConstant.SLASH + "\\w+";

  /**
   * 根据别名获取标准单位
   *
   * @param alias 别名
   * @return 标准单位
   * @throws IllegalArgumentException 如果别名搜索到的单位不唯一
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static Optional<Unit> getUnitByAlias(@NonNull String alias) {
    ImmutableSet<Unit> units = DEFAULT_CONFIGURATION.units(alias);
    Preconditions.checkArgument(units.size() <= 1, "%s all have this alias:%s, it's not only"
            , units, alias);
    return units.stream().findAny();
  }

  /**
   * 从传入的字符串中尝试解析单位
   *
   * @param unitString 表示单位的字符串
   * @return 解析出来的单位
   * @throws IllegalArgumentException 如果传入的字符串无法解析为单位
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static Unit parseUnit(@NonNull String unitString) {
    Optional<Unit> unitById = getUnitByAlias(unitString);
    if (unitById.isPresent()) {
      return unitById.get();
    }

    if (unitString.matches(UNIT_REGEX)) {
      List<String> units = StringConstant.SLASH_SPLITTER.splitToList(unitString);
      if (units.size() == 2) {
          return CompositeStandardUnit.builder().componentToExponent(parseUnit(units.get(0)), 1)
                  .componentToExponent(parseUnit(units.get(1)), -1).build();
      }
    }
    throw new IllegalArgumentException(unitString + " can't parse a Unit");
  }

  /**
   * 进行单位转换
   *
   * @param value 数据的值
   * @param currentUnit 当前单位
   * @param targetUnit 目标单位
   * @return 单位转换后的数据值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  private static AbstractNumber convertUnit(@NonNull AbstractNumber value,
      @NonNull String currentUnit,
      @NonNull String targetUnit) {
    Unit current = parseUnit(currentUnit);
    Unit target = parseUnit(targetUnit);
      return Quantity.create(value, current).convertTo(target).value();
  }

  /**
   * 进行单位转换
   *
   * @param value 数据的值
   * @param currentUnit 当前单位
   * @param targetUnit 目标单位
   * @return 单位转换后的数据值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal convertUnit(@NonNull String value, @NonNull String currentUnit,
      @NonNull String targetUnit) {
    return convertUnit(org.caotc.unit4j.core.math.number.BigDecimal.valueOf(value), currentUnit,
        targetUnit).bigDecimalValue();
  }

  /**
   * 进行单位转换
   *
   * @param value 数据的值
   * @param currentUnit 当前单位
   * @param targetUnit 目标单位
   * @return 单位转换后的数据值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal convertUnit(@NonNull BigDecimal value, @NonNull String currentUnit,
      @NonNull String targetUnit) {
    return convertUnit(org.caotc.unit4j.core.math.number.BigDecimal.valueOf(value), currentUnit,
        targetUnit).bigDecimalValue();
  }

  /**
   * 进行单位转换
   *
   * @param value 数据的值
   * @param currentUnit 当前单位
   * @param targetUnit 目标单位
   * @return 单位转换后的数据值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal convertUnit(@NonNull BigInteger value, @NonNull String currentUnit,
      @NonNull String targetUnit) {
    return convertUnit(org.caotc.unit4j.core.math.number.BigInteger.valueOf(value), currentUnit,
        targetUnit).bigDecimalValue();
  }

  /**
   * 进行单位转换
   *
   * @param value 数据的值
   * @param currentUnit 当前单位
   * @param targetUnit 目标单位
   * @return 单位转换后的数据值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal convertUnit(byte value, @NonNull String currentUnit,
      @NonNull String targetUnit) {
    return convertUnit(org.caotc.unit4j.core.math.number.BigInteger.valueOf(value), currentUnit,
        targetUnit).bigDecimalValue();
  }

  /**
   * 进行单位转换
   *
   * @param value 数据的值
   * @param currentUnit 当前单位
   * @param targetUnit 目标单位
   * @return 单位转换后的数据值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal convertUnit(@NonNull Byte value, @NonNull String currentUnit,
      @NonNull String targetUnit) {
    return convertUnit(org.caotc.unit4j.core.math.number.BigInteger.valueOf(value), currentUnit,
        targetUnit).bigDecimalValue();
  }

  /**
   * 进行单位转换
   *
   * @param value 数据的值
   * @param currentUnit 当前单位
   * @param targetUnit 目标单位
   * @return 单位转换后的数据值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal convertUnit(short value, @NonNull String currentUnit,
      @NonNull String targetUnit) {
    return convertUnit(org.caotc.unit4j.core.math.number.BigInteger.valueOf(value), currentUnit,
        targetUnit).bigDecimalValue();
  }

  /**
   * 进行单位转换
   *
   * @param value 数据的值
   * @param currentUnit 当前单位
   * @param targetUnit 目标单位
   * @return 单位转换后的数据值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal convertUnit(@NonNull Short value, @NonNull String currentUnit,
      @NonNull String targetUnit) {
    return convertUnit(org.caotc.unit4j.core.math.number.BigInteger.valueOf(value), currentUnit,
        targetUnit).bigDecimalValue();
  }

  /**
   * 进行单位转换
   *
   * @param value 数据的值
   * @param currentUnit 当前单位
   * @param targetUnit 目标单位
   * @return 单位转换后的数据值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal convertUnit(int value, @NonNull String currentUnit,
      @NonNull String targetUnit) {
    return convertUnit(org.caotc.unit4j.core.math.number.BigInteger.valueOf(value), currentUnit,
        targetUnit).bigDecimalValue();
  }

  /**
   * 进行单位转换
   *
   * @param value 数据的值
   * @param currentUnit 当前单位
   * @param targetUnit 目标单位
   * @return 单位转换后的数据值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal convertUnit(@NonNull Integer value, @NonNull String currentUnit,
      @NonNull String targetUnit) {
    return convertUnit(org.caotc.unit4j.core.math.number.BigInteger.valueOf(value), currentUnit,
        targetUnit).bigDecimalValue();
  }

  /**
   * 进行单位转换
   *
   * @param value 数据的值
   * @param currentUnit 当前单位
   * @param targetUnit 目标单位
   * @return 单位转换后的数据值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal convertUnit(long value, @NonNull String currentUnit,
      @NonNull String targetUnit) {
    return convertUnit(org.caotc.unit4j.core.math.number.BigInteger.valueOf(value), currentUnit,
        targetUnit).bigDecimalValue();
  }

  /**
   * 进行单位转换
   *
   * @param value 数据的值
   * @param currentUnit 当前单位
   * @param targetUnit 目标单位
   * @return 单位转换后的数据值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal convertUnit(@NonNull Long value, @NonNull String currentUnit,
      @NonNull String targetUnit) {
    return convertUnit(org.caotc.unit4j.core.math.number.BigInteger.valueOf(value), currentUnit,
        targetUnit).bigDecimalValue();
  }

  /**
   * 进行单位转换
   *
   * @param value 数据的值
   * @param currentUnit 当前单位
   * @param targetUnit 目标单位
   * @return 单位转换后的数据值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal convertUnit(float value, @NonNull String currentUnit,
      @NonNull String targetUnit) {
    return convertUnit(org.caotc.unit4j.core.math.number.BigDecimal.valueOf(value), currentUnit,
        targetUnit).bigDecimalValue();
  }

  /**
   * 进行单位转换
   *
   * @param value 数据的值
   * @param currentUnit 当前单位
   * @param targetUnit 目标单位
   * @return 单位转换后的数据值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal convertUnit(@NonNull Float value, @NonNull String currentUnit,
      @NonNull String targetUnit) {
    return convertUnit(org.caotc.unit4j.core.math.number.BigDecimal.valueOf(value), currentUnit,
        targetUnit).bigDecimalValue();
  }

  /**
   * 进行单位转换
   *
   * @param value 数据的值
   * @param currentUnit 当前单位
   * @param targetUnit 目标单位
   * @return 单位转换后的数据值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal convertUnit(double value, @NonNull String currentUnit,
      @NonNull String targetUnit) {
    return convertUnit(org.caotc.unit4j.core.math.number.BigDecimal.valueOf(value), currentUnit,
        targetUnit).bigDecimalValue();
  }

  /**
   * 进行单位转换
   *
   * @param value 数据的值
   * @param currentUnit 当前单位
   * @param targetUnit 目标单位
   * @return 单位转换后的数据值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public static BigDecimal convertUnit(@NonNull Double value, @NonNull String currentUnit,
      @NonNull String targetUnit) {
    return convertUnit(org.caotc.unit4j.core.math.number.BigDecimal.valueOf(value), currentUnit,
        targetUnit).bigDecimalValue();
  }
}
