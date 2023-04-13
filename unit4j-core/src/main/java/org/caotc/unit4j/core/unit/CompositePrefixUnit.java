package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.unit.type.CompositeUnitType;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 有词头的组合单位
 *
 * @author caotc
 * @date 2018-04-13
 * @since 1.0.0
 **/
@Value
@Builder(toBuilder = true)
public class CompositePrefixUnit implements CompositeUnit, PrefixUnit {

  /**
   * 工厂方法
   *
   * @param prefix 词头
   * @param compositeStandardUnit 组合标准单位
   * @return 有词头的组合单位
   * @author caotc
   * @date 2019-05-26
   * @since 1.0.0
   */
  @NonNull
  public static CompositePrefixUnit create(@NonNull Prefix prefix,
      @NonNull CompositeStandardUnit compositeStandardUnit) {
    return builder().prefix(prefix).standardUnit(compositeStandardUnit).build();
  }

  /**
   * 词头
   */
  @NonNull
  Prefix prefix;

  /**
   * 组合标准单位
   */
  @NonNull
  CompositeStandardUnit standardUnit;

  @NonNull
  @Override
  public CompositeUnitType type() {
    return standardUnit().type();
  }

  @NonNull
  @Override
  public CompositePrefixUnit rebase() {
    return builder().prefix(prefix()).standardUnit(standardUnit().rebase()).build();
  }

  /**
   * 幂函数,返回<tt>(this<sup>n</sup>)</tt>
   *
   * @param exponent 指数
   * @return <tt>this<sup>n</sup></tt>
   * @author caotc
   * @date 2019-01-11
   * @since 1.0.0
   */
  @Override
  @NonNull
  public CompositePrefixUnit power(int exponent) {
    return CompositePrefixUnit.builder()
        .prefix(this.prefix().power(exponent))
        .standardUnit(standardUnit().power(exponent))
        .build();
  }

  /**
   * 除法,{@code (this / divisor)}
   *
   * @param divisor 除数
   * @return {@code this / divisor}
   * @author caotc
   * @date 2019-01-11
   * @since 1.0.0
   */
  @NonNull
  public Unit divide(@NonNull CompositePrefixUnit divisor) {
    return multiply(divisor.inverse());
  }

  @Override
  @NonNull
  public CompositePrefixUnit inverse() {
    return builder().prefix(this.prefix().reciprocal())
        .standardUnit(standardUnit().inverse()).build();
  }

  @Override
  public @NonNull CompositePrefixUnit multiply(@NonNull BaseStandardUnit multiplicand) {
    return multiplicand.multiply(this);
  }

  @Override
  public @NonNull CompositeStandardUnit multiply(@NonNull BasePrefixUnit multiplicand) {
    return multiplicand.multiply(this);
  }

  @Override
  public @NonNull Unit multiply(@NonNull CompositeStandardUnit multiplicand) {
    return multiplicand.multiply(this);
  }

  @Override
  public @NonNull Unit multiply(@NonNull CompositePrefixUnit multiplicand) {
    return CompositeStandardUnit.builder().unitComponentToExponents(Stream.of(this, multiplicand)
        .collect(ImmutableMap.toImmutableMap(Function.identity(), (u) -> 1, Integer::sum))).build();
  }
}
