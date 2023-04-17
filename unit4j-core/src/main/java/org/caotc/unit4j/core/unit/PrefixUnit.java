/*
 * Copyright (C) 2020 the original author or authors.
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
import org.caotc.unit4j.core.constant.StringConstant;
import org.caotc.unit4j.core.unit.type.UnitType;

/**
 * 有词头的单位
 *
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
public interface PrefixUnit extends Unit {

    /**
     * 词头与单位的id分隔符
     */
    String DELIMITER = StringConstant.UNDERSCORE;

  /**
   * 该词头单位的标准单位
   *
   * @return 标准单位
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  StandardUnit standardUnit();

  @Override
  @NonNull
  default UnitType type() {
    return standardUnit().type();
  }

  @Override
  @NonNull
  default ImmutableMap<Unit, Integer> componentToExponents() {
      return standardUnit().componentToExponents();
  }

  @Override
  @NonNull
  default String id() {
    return composite(prefix().id(), standardUnit().id());
  }

  /**
   * 组合该对象的词头和标准单位的id和别名的方法
   *
   * @param prefix 词头id或别名
   * @param standardUnit 准单位id或别名
   * @return 组合后的id或别名
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  default String composite(@NonNull String prefix, @NonNull String standardUnit) {
    return StringConstant.UNDERLINE_JOINER.join(prefix, standardUnit);
  }
}
