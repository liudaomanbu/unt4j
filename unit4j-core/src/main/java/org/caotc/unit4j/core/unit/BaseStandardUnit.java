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
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import org.caotc.unit4j.core.unit.type.BaseUnitType;

/**
 * 基本标准单位
 *
 * @author caotc
 * @date 2018-03-27
 * @since 1.0.0
 **/
@Value
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
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

    @Override
    public @NonNull Unit simplify(@NonNull SimplifyConfig config) {
        return this;
    }

    @NonNull
    @Override
    public ImmutableMap<Unit, Integer> componentToExponents() {
        return ImmutableMap.of(this, 1);
    }


  public @NonNull BasePrefixUnit addPrefix(@NonNull Prefix prefix) {
      return new BasePrefixUnit(prefix, this);
  }
}
