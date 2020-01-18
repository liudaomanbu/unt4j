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

package org.caotc.unit4j.support;

import java.math.MathContext;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.api.annotation.SerializeCommand;
import org.caotc.unit4j.api.annotation.SerializeCommand.Type;
import org.caotc.unit4j.api.annotation.SerializeCommands;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.math.number.AbstractNumber;

/**
 * {@link Amount#value()}序列化反序列化配置
 *
 * @author caotc
 * @date 2019-04-21
 * @since 1.0.0
 */
@Value
public class AmountValueCodecConfig {

  /**
   * 数值转换目标类
   */
  @NonNull
  Class<?> valueType;
  /**
   * 数学计算上下文对象
   */
  @NonNull
  MathContext mathContext;

  /**
   * 获取数值的序列化指令
   *
   * @param number 数值
   * @return 序列化指令
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  @NonNull
  public SerializeCommands createSerializeCommands(@NonNull AbstractNumber number) {
    return SerializeCommands.builder()
        .command(SerializeCommand
            .create(Type.WRITE_VALUE, null, number.value(valueType(), mathContext())))
        .build();
  }
}
