package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.unit.type.BaseUnitType;

/**
 * 基本标准单位
 *
 * @author caotc
 * @date 2018-03-27
 * @since 1.0.0
 **/
@Value(staticConstructor = "create")
public class BaseStandardUnit implements StandardUnit, BaseUnit {

  @NonNull
  String id;

  /**
   * 单位类型
   */
  @NonNull
  BaseUnitType type;

  @NonNull
  @Override
  public Unit rebase() {
    return this;
  }

  @NonNull
  @Override
  public ImmutableMap<Unit, Integer> unitComponentToExponents() {
    return ImmutableMap.of(this, 1);
  }

  @NonNull
  @Override
  public StandardUnit power(int exponent) {
    return exponent == 1 ? this
        : CompositeStandardUnit.builder().unitComponentToExponent(this, exponent).build();
  }

  @Override
  public @NonNull CompositeStandardUnit inverse() {
    return CompositeStandardUnit.builder().unitComponentToExponent(this, -1).build();
  }

  @Override
  public @NonNull CompositeStandardUnit multiply(@NonNull BaseStandardUnit multiplicand) {
    return CompositeStandardUnit.builder().unitComponentToExponents(Stream.of(this, multiplicand)
        .collect(ImmutableMap.toImmutableMap(Function.identity(), (u) -> 1, Integer::sum))).build();
//    return CompositeStandardUnit.builder().unitComponentToExponents(Stream.of(this,multiplicand)
//        .map(Unit::unitComponentToExponents)
//        .map(Map::entrySet)
//        .flatMap(Collection::stream)
//        .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue, Integer::sum))).build();
  }

  @Override
  public @NonNull CompositePrefixUnit multiply(@NonNull BasePrefixUnit multiplicand) {
    return CompositePrefixUnit.builder().prefix(prefix())
        .standardUnit(multiply(multiplicand.standardUnit())).build();
  }

  @Override
  public @NonNull CompositeStandardUnit multiply(@NonNull CompositeStandardUnit multiplicand) {
    return CompositeStandardUnit.builder().unitComponentToExponents(Stream.of(this, multiplicand)
        .map(Unit::unitComponentToExponents)
        .map(Map::entrySet)
        .flatMap(Collection::stream)
        .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue, Integer::sum)))
        .build();
  }

  @Override
  public @NonNull CompositePrefixUnit multiply(@NonNull CompositePrefixUnit multiplicand) {
    return CompositePrefixUnit.builder().prefix(prefix())
        .standardUnit(multiply(multiplicand.standardUnit())).build();
  }

  @Override
  public @NonNull BasePrefixUnit addPrefix(@NonNull Prefix prefix) {
    return BasePrefixUnit.builder().prefix(prefix).standardUnit(this).build();
  }

  @Override
  public @NonNull ImmutableSet<Alias> aliasesFromConfiguration(
      @NonNull Configuration configuration) {
    return configuration.aliases(this);
  }

  @Override
  public @NonNull Optional<Alias> aliasFromConfiguration(@NonNull Configuration configuration,
      @NonNull Alias.Type aliasType) {
    return configuration.alias(this, aliasType);
  }

  @Override
  public @NonNull Optional<Alias> compositeAliasFromConfiguration(
      @NonNull Configuration configuration, @NonNull Alias.Type aliasType) {
    return aliasFromConfiguration(configuration, aliasType);
  }
}
