package org.caotc.unit4j.core.unit;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import org.caotc.unit4j.core.common.util.Util;
import org.caotc.unit4j.core.unit.type.CompositeUnitType;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
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
  ImmutableMap<Unit, Integer> componentToExponents;

  @Override
  public @NonNull CompositeUnitType type() {
      return CompositeUnitType.builder().componentToExponents(
                      componentToExponents().entrySet().stream()
                              .collect(ImmutableMap
                                      .toImmutableMap(entry -> entry.getKey().type(), Entry::getValue, Integer::sum)))
              .build();
  }

  @Override
  public @NonNull CompositeStandardUnit rebase() {
      return CompositeStandardUnit.builder().componentToExponents(
                      componentToExponents().entrySet().stream()
                              .map(entry -> entry.getKey().rebase().power(entry.getValue()))
                              .map(Unit::componentToExponents)
                              .map(Map::entrySet)
                              .flatMap(Collection::stream)
                              .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue, Integer::sum)))
              .build();
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public @NonNull CompositeStandardUnit power(int exponent) {
    return CompositeStandardUnit.builder()
            .componentToExponents(Maps.transformValues(componentToExponents, i -> i * exponent))
        .build();
  }

  @Override
  @SuppressWarnings("ConstantConditions")
  @NonNull
  public CompositeStandardUnit inverse() {
      return builder().componentToExponents(
                      Maps.transformValues(componentToExponents(), exponent -> -exponent))
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
            .map(Unit::componentToExponents)
        .map(Map::entrySet)
        .flatMap(Collection::stream)
        .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue, Integer::sum));
    if (map.size() == 1 && Iterables.getOnlyElement(map.values()) == 1) {
      return Iterables.getOnlyElement(map.keySet());
    }
      return CompositeStandardUnit.builder().componentToExponents(map).build();
  }

  @Override
  public @NonNull Unit multiply(@NonNull CompositePrefixUnit multiplicand) {
      Unit multiply = multiply(multiplicand.standardUnit());
      if (multiply instanceof StandardUnit) {
          return ((StandardUnit) multiply).addPrefix(prefix());
      }
      return CompositeStandardUnit.builder().componentToExponent(multiplicand, 1)
              .componentToExponent(this, 1).build();
  }

  @Override
  public @NonNull CompositePrefixUnit addPrefix(@NonNull Prefix prefix) {
    return CompositePrefixUnit.builder().prefix(prefix).standardUnit(this).build();
  }

  @Override
  public @NonNull String id() {
      return Util.createCompositeIdOrAlias(componentToExponents());
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
              .checkArgument(componentToExponents.values().stream().noneMatch(integer -> integer == 0)
                      , "exponent of unit can't be 0,%s", this);
      //检查是否只存在一个单位组件,而且指数为1,这是无意义的单位
      Preconditions.checkArgument(!(componentToExponents.size() == 1 && Iterables
                      .getOnlyElement(componentToExponents.values()) == 1)
              , "无意义的单位:%s", this);
      return this;
  }


    public static CompositeStandardUnitBuilder builder() {
    return new InternalBuilder();
  }

  public static class InternalBuilder extends CompositeStandardUnitBuilder {

      @Override
      public CompositeStandardUnitBuilder componentToExponent(Unit key, Integer value) {
          return value == 0 ? this : super.componentToExponent(key, value);
      }

      @Override
      public CompositeStandardUnitBuilder componentToExponents(
              Map<? extends Unit, ? extends Integer> unitComponentToExponents) {
          return super
                  .componentToExponents(Maps.filterValues(unitComponentToExponents, i -> i != 0));
      }

    @Override
    public CompositeStandardUnit build() {
      return super.build().valid();
    }
  }
}
