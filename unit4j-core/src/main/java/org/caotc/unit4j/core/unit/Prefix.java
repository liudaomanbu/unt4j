package org.caotc.unit4j.core.unit;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedSet;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Identifiable;
import org.caotc.unit4j.core.common.util.Util;
import org.caotc.unit4j.core.constant.StringConstant;
import org.caotc.unit4j.core.convert.UnitConvertConfig;
import org.caotc.unit4j.core.math.number.AbstractNumber;
import org.caotc.unit4j.core.math.number.BigInteger;

/**
 * 词头 //TODO 考虑将词头类改为类似基本单位类型
 *
 * @author caotc
 * @date 2018-04-13
 * @since 1.0.0
 **/
@Value(staticConstructor = "create")
public class Prefix implements Comparable<Prefix>, Identifiable {

    /**
     * 空对象
     */
    public static final Prefix EMPTY = create(1, 0);

    /**
     * 符号:y 中国大陆:幺(科托) 台湾:攸 10⁻²⁴
     */
    public static final Prefix YOCTO = create(10, -24);
  /**
   * 符号:z 中国大陆:仄(普托) 台湾:介 10⁻²¹
   */
  public static final Prefix ZEPTO = create(10, -21);
  /**
   * 符号:a 中国大陆:阿(托) 台湾:阿 10⁻¹⁸
   */
  public static final Prefix ATTO = create(10, -18);
  /**
   * 符号:f 中国大陆:飞(母托) 台湾:飛 10⁻¹⁵
   */
  public static final Prefix FEMTO = create(10, -15);
  /**
   * 符号:p 中国大陆:皮(可) 台湾:皮 10⁻¹²
   */
  public static final Prefix PICO = create(10, -12);
  /**
   * 符号:n 中国大陆:纳(诺) 台湾:奈 10⁻⁹
   */
  public static final Prefix NANO = create(10, -9);
  /**
   * 符号:μ 中国大陆:微 台湾:微 10⁻⁶
   */
  public static final Prefix MICRO = create(10, -6);
  /**
   * 符号:m 中国大陆:毫 台湾:毫 10⁻³
   */
  public static final Prefix MILLI = create(10, -3);
  /**
   * 符号:c 中国大陆:厘 台湾:厘 10⁻²
   */
  public static final Prefix CENTI = create(10, -2);
  /**
   * 符号:d 中国大陆:分 台湾:分 10⁻¹
   */
  public static final Prefix DECI = create(10, -1);
  /**
   * 符号:da 中国大陆:十 台湾:十 10¹
   */
  public static final Prefix DECA = create(10, 1);
  /**
   * 符号:h 中国大陆:百 台湾:百 10²
   */
  public static final Prefix HECTO = create(10, 2);
  /**
   * 符号:k 中国大陆:千 台湾:千 10³
   */
  public static final Prefix KILO = create(10, 3);
  /**
   * 符号:M 中国大陆:兆 台湾:百萬 10⁶
   */
  public static final Prefix MEGA = create(10, 6);
  /**
   * 符号:G 中国大陆:吉(咖) 台湾:吉 10⁹
   */
  public static final Prefix GIGA = create(10, 9);
  /**
   * 符号:T 中国大陆:太(拉) 台湾:兆 10¹²
   */
  public static final Prefix TERA = create(10, 12);
  /**
   * 符号:P 中国大陆:拍(它) 台湾:拍 10¹⁵
   */
  public static final Prefix PETA = create(10, 15);
  /**
   * 符号:E 中国大陆:艾(可萨) 台湾:艾 10¹⁸
   */
  public static final Prefix EXA = create(10, 18);
  /**
   * 符号:Z 中国大陆:泽(它) 台湾:皆 10²¹
   */
  public static final Prefix ZETTA = create(10, 21);
  /**
   * 符号:Y 中国大陆:尧(它) 台湾:佑 10²⁴
   */
  public static final Prefix YOTTA = create(10, 24);

  //非国际单位制词头
  /**
   * 符号:Å 中国大陆:埃
   */
  public static final Prefix ANGSTROM = create(10, -8);

  /**
   * 符号:cmm 中国大陆:忽
   */
  public static final Prefix CENTIMILLI = create(10, -5);

  /**
   * 国际单位制SI的所有标准词头
   */
  public static final ImmutableSortedSet<Prefix> SI_UNIT_PREFIXES = ImmutableSortedSet
      .of(YOCTO, ZEPTO, ATTO, FEMTO, PICO, NANO, MICRO, MILLI, CENTI, DECI, DECA, HECTO, KILO,
          MEGA, GIGA, TERA, PETA, EXA, ZETTA, YOTTA);

  //TODO 封装指数类或改成直接的number
  /**
   * 底数，必须为正数
   */
  int radix;
  /**
   * 指数
   */
  int exponent;

  /**
   * 是否是空词头(即值是否为1)
   *
   * @return 是否是空词头
   * @author caotc
   * @date 2019-04-25
   * @since 1.0.0
   */
  public boolean isEmpty() {
    return radix == 1;
  }

  /**
   * 乘法,{@code this * multiplicand}
   *
   * @param multiplicand 被乘词头
   * @return {@code this * multiplicand}
   * @throws IllegalArgumentException 如果该单位词头和乘数词头的底数不同
   * @author caotc
   * @date 2019-04-25
   * @since 1.0.0
   */
  @NonNull
  public Prefix multiply(@NonNull Prefix multiplicand) {
    Preconditions.checkArgument(radix() == multiplicand.radix(),
        "Prefix multiply must have same radix,this radix is %s,other radix is %s", this.radix(),
        multiplicand.radix());
    return create(radix(), exponent() + multiplicand.exponent());
  }

  /**
   * 幂函数,<tt>(this<sup>n</sup>)</tt>
   * todo name
   *
   * @param exponent 指数
   * @return <tt>this<sup>n</sup></tt>
   * @author caotc
   * @date 2019-04-25
   * @since 1.0.0
   */
  @NonNull
  public Prefix pow(int exponent) {
      return create(radix(), exponent() * exponent);
  }

  /**
   * 倒数
   *
   * @author caotc
   * @date 2019-03-16
   * @since 1.0.0
   */
  @NonNull
  public Prefix reciprocal() {
    return create(radix(), -exponent());
  }

  /**
   * 获得该词头的单位转换到对应的标准单位的转换配置对象
   *
   * @return 该词头的单位转换到对应的标准单位的转换配置对象
   * @author caotc
   * @date 2019-05-26
   * @since 1.0.0
   */
  @NonNull
  public UnitConvertConfig convertToStandardUnitConfig() {
    return UnitConvertConfig.create(value());
  }

  /**
   * 获得从对应的标准单位到该词头的单位的转换配置对象
   *
   * @return 对应的标准单位到该词头的单位的转换配置对象
   * @author caotc
   * @date 2019-05-26
   * @since 1.0.0
   */
  @NonNull
  public UnitConvertConfig convertFromStandardUnitConfig() {
    return UnitConvertConfig.create(reciprocal().value());
  }

    /**
     * 词头的值
     *
     * @return 词头的值
     * @author caotc
     * @date 2019-05-26
     * @since 1.0.0
     */
    private AbstractNumber value() {
        return BigInteger.valueOf(radix()).pow(exponent());
  }

  @Override
  public int compareTo(@NonNull Prefix val) {
    return value().compareTo(val.value());
  }

  @Override
  public @NonNull String id() {
      return StringConstant.EMPTY_JOINER.join(radix(), Util.getSuperscript(exponent()));//todo
  }
}
