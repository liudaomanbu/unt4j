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

package org.caotc.unit4j.support;

import com.google.common.collect.ImmutableList;
import java.util.function.Function;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.unit.Unit;

/**
 * {@link Amount}对象序列化和反序列化配置
 *
 * @author caotc
 * @date 2019-04-21
 * @since 1.0.0
 */
@Value
@Builder(toBuilder = true)
public class AmountCodecConfig {

  private static final String AMOUNT_VALUE_FIELD_NAME = "value";
  private static final String AMOUNT_UNIT_FIELD_NAME = "unit";

  /**
   * 配置
   */
  @NonNull
  Configuration configuration;
  /**
   * 目标单位//TODO 待处理
   */
  Unit targetUnit;
  /**
   * 序列化和反序列化策略
   */
  @NonNull
  CodecStrategy strategy;
  /**
   * 属性名称转换器
   */
  @NonNull
  Function<ImmutableList<String>, String> fieldNameConverter;
//  /**
//   * 属性名称转换器
//   */
//  @NonNull
//  Function<String, String> nameTransformer;
  /**
   * 数值的序列化和反序列化配置
   */
  @NonNull
  AmountValueCodecConfig valueCodecConfig;
  /**
   * 单位的序列化和反序列化配置
   */
  @NonNull
  UnitCodecConfig unitCodecConfig;

  @NonNull
  public String outputName() {
    return fieldNameConverter.apply(ImmutableList.of());
  }

  @NonNull
  public String outputValueName() {
    return fieldNameConverter.apply(ImmutableList.of(AMOUNT_VALUE_FIELD_NAME));
  }

  @NonNull
  public String outputUnitName() {
    return fieldNameConverter.apply(ImmutableList.of(AMOUNT_UNIT_FIELD_NAME));
  }

  /**
   * 获取传入数量对象的序列化指令
   *
   * @param amount 要进行序列化的数量对象
   * @return 序列化指令
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  //TODO 待删除
  @NonNull
  public <T> SerializeCommands serializeCommandsFromAmount(@NonNull Amount amount) {
//    Function<Class<? extends T>, ? extends Set<FieldWrapper<T, ?>>> kvRemovalListener = type -> ReflectionUtil
//        .fieldWrappersFromClassWithFieldCheck(type, MethodNameStyle.FLUENT);
//    return strategy.createSerializeCommands(beforeCodec(amount), fieldNameConverter()
//        , kvRemovalListener
//        , CaseFormat.LOWER_CAMEL);
    return null;
  }
}
