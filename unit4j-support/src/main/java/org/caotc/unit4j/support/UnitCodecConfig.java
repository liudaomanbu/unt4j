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

package org.caotc.unit4j.support;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.serializer.AliasUndefinedStrategy;
import org.caotc.unit4j.core.unit.Unit;

/**
 * {@link Unit}对象序列化和反序列化配置
 *
 * @author caotc
 * @date 2019-05-17
 * @since 1.0.0
 */
@Value
@Builder(toBuilder = true)
@With
public class UnitCodecConfig {

  /**
   * 别名类型
   */
  @NonNull
  Alias.Type type;
  /**
   * 配置
   */
  @NonNull
  Configuration configuration;
  /**
   * 别名未定义
   */
  @NonNull
  AliasUndefinedStrategy aliasUndefinedStrategy;

  /**
   * 获取单位的序列化指令
   *
   * @param unit 单位
   * @return 序列化指令
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  @NonNull
  public String serialize(@NonNull Unit unit) {
    //TODO 增加UnitSerializeStrategy
//    return configuration().aliases(unit, type()).stream().map(Alias::value).findFirst()
//            .orElseGet(() -> aliasUndefinedStrategy().execute(unit, configuration(), type()));
    return "";
  }
}
