package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import org.caotc.unit4j.core.Identifiable;
import org.caotc.unit4j.core.unit.type.UnitType;

/**
 * 单位
 *
 * @author caotc
 * @date 2018-03-27
 * @since 1.0.0
 **/
public interface Unit extends Identifiable {

    //TODO 尝试删除
    @NonNull Prefix prefix();

    /**
     * 单位类型
     *
     * @return 单位类型
     * @author caotc
     * @date 2019-01-11
   * @since 1.0.0
   */
  @NonNull UnitType type();

  /**
   * 重定基准,将所有非基本类型拆解合并,返回结果的类型Map中不存在非基本类型Key //TODO 组件只有BaseStandardUnit还是包括BasePrefixUnit？
   *
   * @return 等价于原对象但是组件仅为基本单位的单位
   * @author caotc
   * @date 2018-08-17
   * @since 1.0.0
   */
  @NonNull Unit rebase();

  /**
   * 获取组成该对象的单位组件与对应指数
   *
   * @return 组成该对象的单位组件与对应指数的Map
   * @author caotc
   * @date 2019-01-11
   * @since 1.0.0
   */
  @NonNull ImmutableMap<Unit, Integer> unitComponentToExponents();

  @NonNull
  default ImmutableMap<UnitType, Dimension> typeToDimensionElementMap() {
    return unitComponentToExponents().entrySet().stream().collect(ImmutableMap
        .toImmutableMap(entry -> entry.getKey().type(),
            entry -> Dimension.create(entry.getKey(), entry.getValue())));
  }

  /**
   * 幂函数,<tt>(this<sup>n</sup>)</tt>
   *
   * @param exponent 指数
   * @return <tt>this<sup>n</sup></tt>
   * @author caotc
   * @date 2019-01-11
   * @since 1.0.0
   */
  @NonNull Unit power(int exponent);

  /**
   * 倒转
   *
   * @return 倒转所有单位组件的指数后的单位对象
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  //TODO 考虑方法名
  @NonNull Unit inverse();

  /**
   * 乘法{@code this * multiplicand}
   *
   * @param multiplicand 被乘数
   * @return {@code this * multiplicand}
   * @author caotc
   * @date 2019-01-11
   * @since 1.0.0
   */
  @NonNull
  default Unit multiply(@NonNull Unit multiplicand) {
    //TODO 配置策略化，目前使用为最保守逻辑，单位不同就作为不同组件(考虑将同一类型单位转换为同一单位的逻辑)
    if (multiplicand instanceof BaseStandardUnit) {
      return multiply((BaseStandardUnit) multiplicand);
    }
    if (multiplicand instanceof BasePrefixUnit) {
      return multiply((BasePrefixUnit) multiplicand);
    }
    if (multiplicand instanceof CompositeStandardUnit) {
      return multiply((CompositeStandardUnit) multiplicand);
    }
    if (multiplicand instanceof CompositePrefixUnit) {
      return multiply((CompositePrefixUnit) multiplicand);
    }
    throw new IllegalArgumentException("never come");
  }

  /**
   * 乘法{@code this * multiplicand}
   *
   * @param multiplicand 被乘数
   * @return {@code this * multiplicand}
   * @author caotc
   * @date 2019-01-11
   * @since 1.0.0
   */
  @NonNull
  Unit multiply(@NonNull BaseStandardUnit multiplicand);

  /**
   * 乘法{@code this * multiplicand}
   *
   * @param multiplicand 被乘数
   * @return {@code this * multiplicand}
   * @author caotc
   * @date 2019-01-11
   * @since 1.0.0
   */
  @NonNull
  Unit multiply(@NonNull BasePrefixUnit multiplicand);

  /**
   * 乘法{@code this * multiplicand}
   *
   * @param multiplicand 被乘数
   * @return {@code this * multiplicand}
   * @author caotc
   * @date 2019-01-11
   * @since 1.0.0
   */
  @NonNull
  Unit multiply(@NonNull CompositeStandardUnit multiplicand);

  /**
   * 乘法{@code this * multiplicand}
   *
   * @param multiplicand 被乘数
   * @return {@code this * multiplicand}
   * @author caotc
   * @date 2019-01-11
   * @since 1.0.0
   */
  @NonNull
  Unit multiply(@NonNull CompositePrefixUnit multiplicand);

  /**
   * 除法{@code (this / divisor)}
   *
   * @param divisor 除数
   * @return {@code (this / divisor)}
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  default Unit divide(@NonNull Unit divisor) {
    return multiply(divisor.inverse());
  }
}
