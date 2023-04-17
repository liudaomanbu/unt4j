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

package org.caotc.unit4j.support.fastjson;

import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.api.annotation.SerializeCommands;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.unit.BasePrefixUnit;
import org.caotc.unit4j.core.unit.BaseStandardUnit;
import org.caotc.unit4j.core.unit.CompositePrefixUnit;
import org.caotc.unit4j.core.unit.CompositeStandardUnit;
import org.caotc.unit4j.support.Unit4jProperties;

import java.util.Arrays;

/**
 * unit4j库的fastjson的序列化与反序列化模块. 所有fastjson的序列化与反序列化需要的对象都包装在该类中使用.
 *
 * @author caotc
 * @date 2019-05-12
 * @since 1.0.0
 */
@Value
public class Unit4jModule {

  /**
   * 工厂方法
   *
   * @param unit4jProperties 属性
   * @return unit4j库的fastjson的序列化与反序列化模块
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  public static Unit4jModule create(@NonNull Unit4jProperties unit4jProperties) {
    return new Unit4jModule(unit4jProperties);
  }

    /**
     * 属性过滤器
     */
    @NonNull
    Unit4jFilter unit4jFilter;
    /**
     * 单独{@link Quantity}对象的序列化器
     */
    @NonNull
    QuantitySerializer quantitySerializer;
    /**
     * {@link SerializeCommands}序列化器
     */
    @NonNull
    SerializeCommandsSerializer serializeCommandsSerializer;


  private Unit4jModule(@NonNull Unit4jProperties unit4jProperties) {
      unit4jFilter = new Unit4jFilter(unit4jProperties);
      quantitySerializer = new QuantitySerializer(unit4jProperties.createAmountCodecConfig());
      serializeCommandsSerializer = new SerializeCommandsSerializer();
  }

  /**
   * 注册到fastjson配置
   *
   * @param serializeConfig fastjson序列化配置
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  public void registerTo(@NonNull SerializeConfig serializeConfig, @NonNull Class<?>... classes) {
      serializeConfig.put(Quantity.class, quantitySerializer());
      serializeConfig.put(org.caotc.unit4j.core.math.number.BigDecimal.class,
              quantitySerializer().quantityValueSerializer());
      serializeConfig.put(org.caotc.unit4j.core.math.number.BigInteger.class,
              quantitySerializer().quantityValueSerializer());
      serializeConfig.put(org.caotc.unit4j.core.math.number.Fraction.class,
              quantitySerializer().quantityValueSerializer());
      serializeConfig.put(BaseStandardUnit.class, quantitySerializer().unitSerializer());
      serializeConfig.put(BasePrefixUnit.class, quantitySerializer().unitSerializer());
      serializeConfig.put(CompositeStandardUnit.class, quantitySerializer().unitSerializer());
      serializeConfig.put(CompositePrefixUnit.class, quantitySerializer().unitSerializer());
      serializeConfig.put(SerializeCommands.class, serializeCommandsSerializer());
      Arrays.stream(classes).forEach(clazz -> serializeConfig.addFilter(clazz, unit4jFilter()));
  }
}
