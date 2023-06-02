package org.caotc.unit4j.core.unit;

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.unit.type.UnitType;

/**
 * 有词头的组合单位
 *
 * @author caotc
 * @date 2018-04-13
 * @since 1.0.0
 **/
@Value
public class CompositePrefixUnit extends PrefixUnit {
//  /**
//   * 词头
//   */
//  @NonNull
//  Prefix prefix;

    /**
     * 组合标准单位
     */
    @NonNull
    CompositeStandardUnit standardUnit;

    CompositePrefixUnit(@NonNull Prefix prefix, @NonNull CompositeStandardUnit standardUnit) {
        super(prefix);
        this.standardUnit = standardUnit;
    }

    @NonNull
    @Override
    public UnitType type() {
        return standardUnit().type();
    }

    @NonNull
    @Override
    public CompositePrefixUnit rebase() {
        //todo cast remove
        return (CompositePrefixUnit) builder().prefix(prefix()).standardUnit(standardUnit().rebase()).build();
    }

    @Override
    public @NonNull Unit simplify(@NonNull SimplifyConfig config) {
        //todo 是否根据配置进行this.prefix和standardUnit的组件中的prefix合并
        return this;
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
      return multiply(divisor.reciprocal());
  }

}
