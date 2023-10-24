/*
 * Copyright (C) 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.FieldDefaults;
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
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
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
  public @NonNull Unit simplify(boolean recursive) {
    return builder().componentToExponents(
                    componentToExponents().entrySet().stream()
                            .map(entry -> {
                                Unit unit = entry.getKey();
                                Unit.Builder builder = builder();
                                unit = builder
                                        .componentToExponents(Maps.transformValues(unit.componentToExponents(), i -> i * entry.getValue()))
                                        .build();
                                if (recursive && unit.componentToExponents().size() > 1) {//todo
                                    unit = unit.simplify(true);
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
    return Util.createCompositeId(componentToExponents());
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
