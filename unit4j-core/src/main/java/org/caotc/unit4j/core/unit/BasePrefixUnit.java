package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.unit.type.BaseUnitType;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 有词头的基本单位
 *
 * @author caotc
 * @date 2019-05-26
 * @since 1.0.0
 */
@Value
public class BasePrefixUnit extends PrefixUnit {
//  /**
//   * 词头
//   */
//  @NonNull
//  Prefix prefix;
//
  /**
   * 基本标准单位
   */
  @NonNull
  BaseStandardUnit standardUnit;

  BasePrefixUnit(@NonNull Prefix prefix, @NonNull BaseStandardUnit standardUnit) {
    super(prefix);
    this.standardUnit = standardUnit;
  }

  @Override
  public @NonNull BaseUnitType type() {
    return standardUnit().type();
  }

  @Override
  public @NonNull BasePrefixUnit rebase() {
    return this;
  }

  @Override
  public @NonNull ImmutableMap<Unit, Integer> componentToExponents() {
      return ImmutableMap.of(standardUnit(), 1);
  }

  @Override
  public @NonNull PrefixUnit power(int exponent) {
    return standardUnit().power(exponent).addPrefix(prefix());
  }

  @Override
  public @NonNull Unit inverse() {
    return CompositePrefixUnit.builder().prefix(prefix().reciprocal())
            .standardUnit(standardUnit().inverse()).build();
  }

  @Override
  public @NonNull Unit multiply(@NonNull BaseStandardUnit multiplicand) {
    return multiplicand.multiply(this);
  }

  @Override
  public @NonNull Unit multiply(@NonNull BasePrefixUnit multiplicand) {
    return Unit.builder().componentToExponents(Stream.of(this, multiplicand)
            .collect(ImmutableMap.toImmutableMap(Function.identity(), (u) -> 1, Integer::sum))).build();
  }

  @Override
  public @NonNull CompositePrefixUnit multiply(@NonNull CompositeStandardUnit multiplicand) {
    //todo cast remove
    return (CompositePrefixUnit) CompositePrefixUnit.builder().prefix(prefix())
            .standardUnit(standardUnit().multiply(multiplicand)).build();
  }

  @Override
  public @NonNull CompositeStandardUnit multiply(@NonNull CompositePrefixUnit multiplicand) {
    //todo cast remove
    return (CompositeStandardUnit) CompositeStandardUnit.builder().componentToExponents(Stream.of(this, multiplicand)
            .collect(ImmutableMap.toImmutableMap(Function.identity(), (u) -> 1, Integer::sum))).build();
  }
}
