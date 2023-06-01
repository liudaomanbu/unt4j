package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.unit.type.UnitType;

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
        throw new UnsupportedOperationException();
//        return standardUnit().simplify(config);
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

    @Override
    @NonNull
    public CompositePrefixUnit reciprocal() {
        //todo cast remove
        return (CompositePrefixUnit) builder().prefix(this.prefix().reciprocal())
                .standardUnit(standardUnit().reciprocal()).build();
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
    return CompositeStandardUnit.builder().componentToExponents(Stream.of(this, multiplicand)
            .collect(ImmutableMap.toImmutableMap(Function.identity(), (u) -> 1, Integer::sum))).build();
  }
}
