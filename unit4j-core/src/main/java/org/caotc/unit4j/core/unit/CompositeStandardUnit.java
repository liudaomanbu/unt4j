package org.caotc.unit4j.core.unit;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.common.util.Util;
import org.caotc.unit4j.core.unit.type.CompositeUnitType;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 组合标准单位
 *
 * @author caotc
 * @date 2019-05-26
 * @since 1.0.0
 */
@Value
@Builder(toBuilder = true)
public class CompositeStandardUnit implements CompositeUnit, StandardUnit {

  /**
   * 单位组件与对应指数
   */
  @NonNull
  @Singular
  ImmutableMap<Unit, Integer> unitComponentToExponents;

  @Override
  public @NonNull CompositeUnitType type() {
    return CompositeUnitType.builder().unitTypeComponentToExponents(
        unitComponentToExponents().entrySet().stream()
            .collect(ImmutableMap
                .toImmutableMap(entry -> entry.getKey().type(), Entry::getValue, Integer::sum)))
        .build();
  }

  @Override
  public @NonNull CompositeStandardUnit rebase() {
    return CompositeStandardUnit.builder().unitComponentToExponents(
        unitComponentToExponents().entrySet().stream()
            .map(entry -> entry.getKey().rebase().power(entry.getValue()))
            .map(Unit::unitComponentToExponents)
            .map(Map::entrySet)
            .flatMap(Collection::stream)
            .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue, Integer::sum)))
        .build();
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public @NonNull CompositeStandardUnit power(int exponent) {
    return CompositeStandardUnit.builder()
        .unitComponentToExponents(Maps.transformValues(unitComponentToExponents, i -> i * exponent))
        .build();
  }

  @Override
  @SuppressWarnings("ConstantConditions")
  @NonNull
  public CompositeStandardUnit inverse() {
    return builder().unitComponentToExponents(
        Maps.transformValues(unitComponentToExponents(), exponent -> -exponent))
        .build();
  }

  @Override
  public @NonNull CompositeStandardUnit multiply(@NonNull BaseStandardUnit multiplicand) {
    return multiplicand.multiply(this);
  }

  @Override
  public @NonNull CompositePrefixUnit multiply(@NonNull BasePrefixUnit multiplicand) {
    return multiplicand.multiply(this);
  }

  @Override
  public @NonNull Unit multiply(@NonNull CompositeStandardUnit multiplicand) {
    ImmutableMap<Unit, Integer> map = Stream.of(this, multiplicand)
        .map(Unit::unitComponentToExponents)
        .map(Map::entrySet)
        .flatMap(Collection::stream)
        .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue, Integer::sum));
    if (map.size() == 1 && Iterables.getOnlyElement(map.values()) == 1) {
      return Iterables.getOnlyElement(map.keySet());
    }
    return CompositeStandardUnit.builder().unitComponentToExponents(map).build();
  }

  @Override
  public @NonNull Unit multiply(@NonNull CompositePrefixUnit multiplicand) {
    Unit multiply = multiply(multiplicand.standardUnit());
    if (multiply instanceof StandardUnit) {
      return ((StandardUnit) multiply).addPrefix(prefix());
    }
    return CompositeStandardUnit.builder().unitComponentToExponent(multiplicand, 1)
        .unitComponentToExponent(this, 1).build();
  }

  @Override
  public @NonNull CompositePrefixUnit addPrefix(@NonNull Prefix prefix) {
    return CompositePrefixUnit.builder().prefix(prefix).standardUnit(this).build();
  }

  @Override
  public @NonNull String id() {
    return Util.createCompositeIdOrAlias(unitComponentToExponents());
  }

    @Override
    public @NonNull ImmutableSet<Alias> aliases(
            @NonNull Configuration configuration) {
        return configuration.aliases(this);
    }

    @Override
    public @NonNull Optional<Alias> alias(@NonNull Configuration configuration,
                                          @NonNull Alias.Type aliasType) {
        return configuration.alias(this, aliasType);
    }

  @NonNull
  @Override
  public Optional<Alias> compositeAliasFromConfiguration(@NonNull Configuration configuration,
      @NonNull Alias.Type aliasType) {
      Optional<Alias> alias = alias(configuration, aliasType);
    if (alias.isPresent()) {
      return alias;
    }
    boolean componentAliased = unitComponentToExponents().keySet().stream()
        .map(unit -> unit.compositeAliasFromConfiguration(configuration, aliasType))
        .allMatch(Optional::isPresent);
    if (componentAliased) {
      String compositeAlias = Util.createCompositeIdOrAlias(
          unitComponentToExponents().entrySet().stream()
              .collect(ImmutableMap.toImmutableMap(entry -> (() -> entry.getKey()
                      .compositeAliasFromConfiguration(configuration, aliasType).map(Alias::value)
                      .get())
                  , Entry::getValue)));
      return Optional.of(Alias.create(aliasType, compositeAlias));
    }
    return Optional.empty();
  }

  /**
   * 校验方法
   *
   * @author caotc
   * @date 2019-05-26
   * @since 1.0.0
   */
  private CompositeStandardUnit valid() {
    //检查不能有指数为0的组件
    Preconditions
        .checkArgument(unitComponentToExponents.values().stream().noneMatch(integer -> integer == 0)
            , "exponent of unit can't be 0,%s", this);
    //检查是否只存在一个单位组件,而且指数为1,这是无意义的单位
    Preconditions.checkArgument(!(unitComponentToExponents.size() == 1 && Iterables
            .getOnlyElement(unitComponentToExponents.values()) == 1)
        , "无意义的单位:%s", this);
    return this;
  }

  public static CompositeStandardUnitBuilder builder() {
    return new InternalBuilder();
  }

  public static class InternalBuilder extends CompositeStandardUnitBuilder {

    @Override
    public CompositeStandardUnitBuilder unitComponentToExponent(Unit key, Integer value) {
      return value == 0 ? this : super.unitComponentToExponent(key, value);
    }

    @Override
    public CompositeStandardUnitBuilder unitComponentToExponents(
        Map<? extends Unit, ? extends Integer> unitComponentToExponents) {
      return super
          .unitComponentToExponents(Maps.filterValues(unitComponentToExponents, i -> i != 0));
    }

    @Override
    public CompositeStandardUnit build() {
      return super.build().valid();
    }
  }
}
