package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.unit.type.BaseUnitType;

/**
 * 有词头的基本单位
 *
 * @author caotc
 * @date 2019-05-26
 * @since 1.0.0
 */
@Value
@Builder(toBuilder = true)
public class BasePrefixUnit implements BaseUnit, PrefixUnit {

  /**
   * 工厂方法
   *
   * @param prefix 词头
   * @param baseStandardUnit 基本标准单位
   * @return 有词头的基本单位
   * @author caotc
   * @date 2019-05-26
   * @since 1.0.0
   */
  @NonNull
  public static BasePrefixUnit create(@NonNull Prefix prefix,
      @NonNull BaseStandardUnit baseStandardUnit) {
    return builder().prefix(prefix).standardUnit(baseStandardUnit).build();
  }

  /**
   * 词头
   */
  @NonNull
  Prefix prefix;

  /**
   * 基本标准单位
   */
  @NonNull
  BaseStandardUnit standardUnit;

  @Override
  public @NonNull BaseUnitType type() {
    return standardUnit().type();
  }

  @Override
  public @NonNull BasePrefixUnit rebase() {
    return this;
  }

  @Override
  public @NonNull ImmutableMap<Unit, Integer> unitComponentToExponents() {
    return ImmutableMap.of(standardUnit(), 1);
  }

  @Override
  public @NonNull PrefixUnit power(int exponent) {
    return standardUnit().power(exponent).addPrefix(prefix());
  }

  @Override
  public @NonNull Unit inverse() {
    return CompositePrefixUnit.builder().prefix(prefix.reciprocal())
        .standardUnit(standardUnit().inverse()).build();
  }

  @Override
  public @NonNull CompositePrefixUnit multiply(@NonNull BaseStandardUnit multiplicand) {
    return multiplicand.multiply(this);
  }

  @Override
  public @NonNull CompositeStandardUnit multiply(@NonNull BasePrefixUnit multiplicand) {
    return CompositeStandardUnit.builder().unitComponentToExponents(Stream.of(this, multiplicand)
        .collect(ImmutableMap.toImmutableMap(Function.identity(), (u) -> 1, Integer::sum))).build();
  }

  @Override
  public @NonNull CompositePrefixUnit multiply(@NonNull CompositeStandardUnit multiplicand) {
    return CompositePrefixUnit.builder().prefix(prefix())
        .standardUnit(standardUnit().multiply(multiplicand)).build();
  }

  @Override
  public @NonNull CompositeStandardUnit multiply(@NonNull CompositePrefixUnit multiplicand) {
    return CompositeStandardUnit.builder().unitComponentToExponents(Stream.of(this, multiplicand)
        .collect(ImmutableMap.toImmutableMap(Function.identity(), (u) -> 1, Integer::sum))).build();
  }

  @Override
  public @NonNull Optional<Alias> compositeAliasFromConfiguration(
      @NonNull Configuration configuration, @NonNull Alias.Type aliasType) {
    return aliasFromConfiguration(configuration, aliasType);
  }
}
