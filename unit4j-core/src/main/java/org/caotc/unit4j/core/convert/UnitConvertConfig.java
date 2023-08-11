package org.caotc.unit4j.core.convert;

import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.fraction.BigFraction;
import org.caotc.unit4j.core.common.util.MathUtil;

/**
 * 单位转换配置类
 *
 * @author caotc
 * @date 2018-03-29
 * @since 1.0.0
 **/
@Value
@Builder(toBuilder = true)
@Slf4j
public class UnitConvertConfig {

  /**
   * 空对象
   */
  private static final UnitConvertConfig EMPTY = builder().ratio(BigFraction.ONE)
          .constantDifference(BigFraction.ZERO).build();

  /**
   * 空对象
   *
   * @return 空对象
   * @author caotc
   * @date 2019-05-24
   * @since 1.0.0
   */
  @NonNull
  public static UnitConvertConfig empty() {
    return EMPTY;
  }
  /**
   * 单位转换比例
   */
  @NonNull
  BigFraction ratio;
  /**
   * 单位之间的常量差值(以源单位为标准,所以先加常数运算再比例运算)
   */
  @NonNull
  BigFraction constantDifference;

  /**
   * 工厂方法
   *
   * @param ratio 单位转换比例
   * @return 单位转换配置类
   * @author caotc
   * @date 2019-05-24
   * @since 1.0.0
   */
  @NonNull
  public static UnitConvertConfig create(@NonNull java.math.BigDecimal ratio) {
    return builder().ratio(MathUtil.toBigFraction(ratio)).constantDifference(BigFraction.ZERO).build();
  }

  /**
   * 工厂方法
   *
   * @param ratio 单位转换比例
   * @return 单位转换配置类
   * @author caotc
   * @date 2019-05-24
   * @since 1.0.0
   */
  @NonNull
  public static UnitConvertConfig create(@NonNull BigFraction ratio) {
    return builder().ratio(ratio).constantDifference(BigFraction.ZERO).build();
  }

  /**
   * 工厂方法
   *
   * @param ratio 单位转换比例
   * @param zeroDifference 单位之间的零点差值
   * @return 单位转换配置类
   * @author caotc
   * @date 2019-05-24
   * @apiNote {@code zeroDifference}的值以源单位为标准
   * @since 1.0.0
   */
  @NonNull
  public static UnitConvertConfig create(@NonNull java.math.BigDecimal ratio,
      @NonNull java.math.BigDecimal zeroDifference) {
    return builder().ratio(MathUtil.toBigFraction(ratio))
        .constantDifference(MathUtil.toBigFraction(zeroDifference)).build();
  }

  /**
   * 工厂方法
   *
   * @param ratio 单位转换比例
   * @param zeroDifference 单位之间的零点差值
   * @return 单位转换配置类
   * @author caotc
   * @date 2019-05-24
   * @apiNote {@code zeroDifference}的值以源单位为标准
   * @since 1.0.0
   */
  @NonNull
  public static UnitConvertConfig create(@NonNull BigFraction ratio,
      @NonNull BigFraction zeroDifference) {
    return builder().ratio(ratio).constantDifference(zeroDifference).build();
  }

  /**
   * 该类对象的合并方法
   *
   * @param other 需要合并的对象
   * @return 合并后的对象
   * @author caotc
   * @date 2019-05-24
   * @since 1.0.0
   */
  @NonNull
  public UnitConvertConfig reduce(@NonNull UnitConvertConfig other) {
    BigFraction newZeroDifference = constantDifference().add(other.constantDifference().divide(ratio()));
    BigFraction newRatio = ratio().multiply(other.ratio());
    return builder().ratio(newRatio).constantDifference(newZeroDifference).build();
  }

  /**
   * 倒数
   *
   * @return 当前对象的倒数, 即源单位和目标单位互换后的单位转换配置对象
   * @author caotc
   * @date 2019-05-24
   * @since 1.0.0
   */
  @NonNull
  public UnitConvertConfig reciprocal() {
    BigFraction newRatio = ratio().reciprocal();
    return builder().ratio(newRatio).constantDifference(constantDifference().negate().multiply(newRatio))
        .build();
  }

  /**
   * 与数值对象相乘的方法
   *
   * @param multiplicand 被乘数
   * @return 单位转换比例和单位之间的零点差值使用乘积的单位转换配置对象
   * @author caotc
   * @date 2019-05-24
   * @since 1.0.0
   */
  @NonNull
  public UnitConvertConfig multiply(@NonNull BigFraction multiplicand) {
    return create(ratio().multiply(multiplicand), constantDifference().multiply(multiplicand));
  }

  /**
   * 与数值对象相除的方法
   *
   * @param divisor 除数
   * @return 单位转换比例和单位之间的零点差值使用商的单位转换配置对象
   * @author caotc
   * @date 2019-05-24
   * @since 1.0.0
   */
  @NonNull
  public UnitConvertConfig divide(@NonNull BigFraction divisor) {
    return create(ratio().divide(divisor),
        constantDifference().divide(divisor));
  }

  /**
   * {@code exponent}次幂
   *
   * @param exponent 指数
   * @return {@code exponent}次幂后的单位转换配置对象
   * @author caotc
   * @date 2019-05-25
   * @since 1.0.0
   */
  @NonNull
  public UnitConvertConfig pow(int exponent) {
    if(exponent==1){
      return this;
    }
    //有常量差值时不能指数计算
    Preconditions.checkState(isConstantDifferenceZero(),"constant difference is not 0");
    return create(ratio().pow(exponent));
  }

  /**
   * 运算
   *
   * @param value 需要运算的值
   * @return 运算后的值
   * @author caotc
   * @date 2019-05-25
   * @since 1.0.0
   */
  @NonNull
  public BigFraction apply(@NonNull BigFraction value) {
    return value.add(constantDifference()).multiply(ratio());
  }

  /**
   * 单位之间零点是否相同
   * todo 方法命名
   *
   * @return 单位之间零点是否相同
   * @author caotc
   * @date 2019-05-25
   * @since 1.0.0
   */
  public boolean isConstantDifferenceZero() {
    return BigFraction.ZERO.compareTo(constantDifference()) == 0;
  }

  /**
   * 该对象是否为空对象
   *
   * @return 该对象是否为空对象
   * @author caotc
   * @date 2019-05-25
   * @since 1.0.0
   */
  public boolean isEmpty() {
    return BigFraction.ONE.compareTo(ratio()) == 0
        && BigFraction.ZERO.compareTo(constantDifference()) == 0;
  }

}
