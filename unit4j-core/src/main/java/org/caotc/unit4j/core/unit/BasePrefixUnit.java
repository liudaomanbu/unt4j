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

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

/**
 * 有词头的基本单位
 *
 * @author caotc
 * @date 2019-05-26
 * @since 1.0.0
 */
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class BasePrefixUnit extends PrefixUnit {
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
  public @NonNull Unit simplify(boolean recursive) {
    return this;
  }
}
