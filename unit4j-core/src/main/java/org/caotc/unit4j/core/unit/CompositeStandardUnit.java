package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Singular;
import lombok.ToString;
import lombok.Value;
import org.caotc.unit4j.core.common.util.Util;
import org.caotc.unit4j.core.unit.type.UnitType;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 组合标准单位
 *
 * @author caotc
 * @date 2019-05-26
 * @since 1.0.0
 */
@Value
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
@lombok.Builder(builderMethodName = "builderInternal", access = AccessLevel.MODULE)
public class CompositeStandardUnit extends StandardUnit {

  /**
   * 单位组件与对应指数
   */
  @NonNull
  @Singular
  ImmutableMap<@NonNull Unit, @NonNull Integer> componentToExponents;

  CompositeStandardUnit(@NonNull ImmutableMap<@NonNull Unit, @NonNull Integer> componentToExponents) {
    this.componentToExponents = componentToExponents;
    validate();
  }

  @Override
  public @NonNull UnitType type() {
    return UnitType.builder().componentToExponents(
                    componentToExponents().entrySet().stream()
                            .collect(ImmutableMap.toImmutableMap(entry -> entry.getKey().type(), Entry::getValue, Integer::sum)))
            .build();
  }

  @Override
  public @NonNull Unit simplify(@NonNull SimplifyConfig config) {
    return builder().componentToExponents(
                    componentToExponents().entrySet().stream()
                            .map(entry -> {
                                Unit unit = entry.getKey();
                                Unit.Builder builder = builder();
                                //todo merge
                                if (config.prefixUnit() && !unit.prefix().isEmpty()) {
                                    builder.prefix(unit.prefix().pow(entry.getValue()));
                                    unit = ((PrefixUnit) unit).standardUnit();
                                }
                                unit = builder
                                        .componentToExponents(Maps.transformValues(unit.componentToExponents(), i -> i * entry.getValue()))
                                        .build();
                                if (config.recursive() && unit.componentToExponents().size() > 1) {//todo
                                    unit = unit.simplify(config);
                                }
                                return unit;
                            })
                            .map(Unit::componentToExponents)
                            .map(Map::entrySet)
                            .flatMap(Collection::stream)
                            .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue, Integer::sum)))
            .build();
  }

  @Override
  public @NonNull CompositePrefixUnit addPrefix(@NonNull Prefix prefix) {
      return new CompositePrefixUnit(prefix, this);
  }

  @Override
  public @NonNull String id() {
    return Util.createCompositeIdOrAlias(componentToExponents());
  }

  private void validate() {
    if (componentToExponents().size() == 1 && Iterables.getOnlyElement(componentToExponents().values()) == 1) {
      throw new IllegalArgumentException(String.format("Unit only and Exponent is 1.%s is not a CompositeStandardUnit", componentToExponents()));
    }
    if (componentToExponents().containsValue(0)) {
      throw new IllegalArgumentException(String.format("Exponent can not is 0.%s", componentToExponents()));
    }
  }
}
