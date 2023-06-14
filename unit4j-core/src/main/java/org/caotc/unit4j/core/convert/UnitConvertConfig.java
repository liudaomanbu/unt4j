package org.caotc.unit4j.core.convert;

import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.math.number.AbstractNumber;
import org.caotc.unit4j.core.math.number.BigDecimal;

/**
 * 单位转换配置类 //TODO UnitConvert接口
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
  private static final UnitConvertConfig EMPTY = builder().ratio(BigDecimal.ONE)
          .zeroDifference(BigDecimal.ZERO).build();

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
    return builder().ratio(BigDecimal.valueOf(ratio)).zeroDifference(BigDecimal.ZERO).build();
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
  public static UnitConvertConfig create(@NonNull AbstractNumber ratio) {
    return builder().ratio(ratio).zeroDifference(BigDecimal.ZERO).build();
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
    return builder().ratio(BigDecimal.valueOf(ratio))
        .zeroDifference(BigDecimal.valueOf(zeroDifference)).build();
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
  public static UnitConvertConfig create(@NonNull AbstractNumber ratio,
      @NonNull AbstractNumber zeroDifference) {
    return builder().ratio(ratio).zeroDifference(zeroDifference).build();
  }

  /**
   * 单位转换比例
   */
  @NonNull
  AbstractNumber ratio;
  /**
   * 单位之间的零点差值(以源单位为标准)
   */
  @NonNull
  AbstractNumber zeroDifference;

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
    AbstractNumber newZeroDifference = zeroDifference().add(other.zeroDifference().divide(ratio()));
    AbstractNumber newRatio = ratio().multiply(other.ratio());
    return builder().ratio(newRatio).zeroDifference(newZeroDifference).build();
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
    AbstractNumber newRatio = ratio().reciprocal();
    return builder().ratio(newRatio).zeroDifference(zeroDifference().negate().multiply(newRatio))
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
  public UnitConvertConfig multiply(@NonNull AbstractNumber multiplicand) {
    return create(ratio().multiply(multiplicand), zeroDifference().multiply(multiplicand));
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
  public UnitConvertConfig divide(@NonNull AbstractNumber divisor) {
    return create(ratio().divide(divisor),
        zeroDifference().divide(divisor));
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
    //TODO 根据指数不同,零点影响不同,需要符号计算处理或者不进行合并
    Preconditions.checkArgument(isZeroPointSame());
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
  public AbstractNumber apply(@NonNull AbstractNumber value) {
    return value.add(zeroDifference()).multiply(ratio());
  }

  /**
   * 单位之间零点是否相同
   *
   * @return 单位之间零点是否相同
   * @author caotc
   * @date 2019-05-25
   * @since 1.0.0
   */
  public boolean isZeroPointSame() {
    return BigDecimal.ZERO.compareTo(zeroDifference()) == 0;
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
    return BigDecimal.ONE.compareTo(ratio()) == 0
        && BigDecimal.ZERO.compareTo(zeroDifference()) == 0;
  }

}
