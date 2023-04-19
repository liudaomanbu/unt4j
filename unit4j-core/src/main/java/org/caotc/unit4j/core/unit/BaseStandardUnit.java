/*
 * Copyright (C) 2019 the original author or authors.
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
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.unit.type.BaseUnitType;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 基本标准单位
 *
 * @author caotc
 * @date 2018-03-27
 * @since 1.0.0
 **/
@Value
public class BaseStandardUnit extends StandardUnit {

    @NonNull
    public static BaseStandardUnit create(@NonNull String id,
                                          @NonNull BaseUnitType type) {
        return new BaseStandardUnit(id, type);
    }

    @NonNull
    String id;

  /**
   * 单位类型
   */
  @NonNull
  BaseUnitType type;

  private BaseStandardUnit(@NonNull String id,
      @NonNull BaseUnitType type) {
    this.id = id;
    this.type = type;
  }

  @NonNull
  @Override
  public Unit rebase() {
    return this;
  }

  @NonNull
  @Override
  public ImmutableMap<Unit, Integer> componentToExponents() {
      return ImmutableMap.of(this, 1);
  }

  @NonNull
  @Override
  public StandardUnit power(int exponent) {
      return exponent == 1 ? this
              //todo cast remove
              : (StandardUnit) CompositeStandardUnit.builder().componentToExponent(this, exponent).build();
  }

  @Override
  public @NonNull CompositeStandardUnit inverse() {
      //todo cast remove
      return (CompositeStandardUnit) CompositeStandardUnit.builder().componentToExponent(this, -1).build();
  }

  @Override
  public @NonNull CompositeStandardUnit multiply(@NonNull BaseStandardUnit multiplicand) {
      //todo cast remove
      return (CompositeStandardUnit) CompositeStandardUnit.builder().componentToExponents(Stream.of(this, multiplicand)
              .collect(ImmutableMap.toImmutableMap(Function.identity(), (u) -> 1, Integer::sum))).build();
//    return CompositeStandardUnit.builder().unitComponentToExponents(Stream.of(this,multiplicand)
//        .map(Unit::unitComponentToExponents)
//        .map(Map::entrySet)
//        .flatMap(Collection::stream)
//        .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue, Integer::sum))).build();
  }

  @Override
  public @NonNull CompositePrefixUnit multiply(@NonNull BasePrefixUnit multiplicand) {
      return (CompositePrefixUnit) CompositePrefixUnit.builder().prefix(prefix())
              .standardUnit(multiply(multiplicand.standardUnit())).build();
  }

  @Override
  public @NonNull CompositeStandardUnit multiply(@NonNull CompositeStandardUnit multiplicand) {
      //todo cast remove
      return (CompositeStandardUnit) CompositeStandardUnit.builder().componentToExponents(Stream.of(this, multiplicand)
                      .map(Unit::componentToExponents)
                      .map(Map::entrySet)
                      .flatMap(Collection::stream)
                      .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue, Integer::sum)))
              .build();
  }

  @Override
  public @NonNull CompositePrefixUnit multiply(@NonNull CompositePrefixUnit multiplicand) {
      //todo cast remove
      return (CompositePrefixUnit) CompositePrefixUnit.builder().prefix(prefix())
              .standardUnit(multiply(multiplicand.standardUnit())).build();
  }

  public @NonNull BasePrefixUnit addPrefix(@NonNull Prefix prefix) {
      //todo cast remove
      return (BasePrefixUnit) Unit.builder().prefix(prefix).standardUnit(this).build();
  }
}
