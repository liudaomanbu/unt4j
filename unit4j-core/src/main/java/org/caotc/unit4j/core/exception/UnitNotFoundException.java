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

package org.caotc.unit4j.core.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.caotc.unit4j.core.unit.Unit;

/**
 * {@link Unit}不存在异常
 *
 * @author caotc
 * @date 2019-11-01
 * @see Unit
 * @since 1.0.0
 */
@Getter
@EqualsAndHashCode
public class UnitNotFoundException extends IllegalArgumentException {

  /**
   * 工厂方法
   *
   * @param id 主键
   * @return {@link UnitNotFoundException}
   * @author caotc
   * @date 2019-05-25
   * @since 1.0.0
   */
  @NonNull
  public static UnitNotFoundException create(@NonNull String id) {
    return new UnitNotFoundException(id);
  }

  /**
   * 查找单位的id
   */
  @NonNull
  String id;

  private UnitNotFoundException(@NonNull String id) {
    super(String.format("the unit with an id of %s not found", id));
    this.id = id;
  }
}
