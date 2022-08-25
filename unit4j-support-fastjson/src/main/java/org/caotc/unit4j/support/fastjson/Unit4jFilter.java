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

import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.BeforeFilter;
import com.alibaba.fastjson.serializer.ContextValueFilter;
import com.alibaba.fastjson.serializer.PropertyFilter;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.AmountSerialize;
import org.caotc.unit4j.api.annotation.SerializeCommand;
import org.caotc.unit4j.api.annotation.SerializeCommands;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.common.util.ReflectionUtil;
import org.caotc.unit4j.support.AmountCodecConfig;
import org.caotc.unit4j.support.Unit4jProperties;

import java.lang.reflect.Type;


/**
 * {@link Amount}在fastjson中的属性过滤器,为了实现不同类中的{@link Amount}属性通过注解实现不同策略序列化 ,在fastjson中需要通过Filter实现
 * //TODO 考虑Spring环境时配置刷新问题
 *
 * @author caotc
 * @date 2019-05-07
 * @since 1.0.0
 */
@Value
@Slf4j
public class Unit4jFilter extends BeforeFilter implements ContextValueFilter, PropertyFilter,
    ExtraProcessor, ExtraTypeProvider {

  /**
   * 属性
   */
  @NonNull
  Unit4jProperties unit4jProperties;

  @Override
  public Object process(BeanContext context, Object object, String name, Object value) {
    AmountCodecConfig amountCodecConfig = ReflectionUtil
            .readableProperty(object.getClass(), name)
            .map(unit4jProperties::createPropertyAmountCodecConfig)
        .orElseGet(unit4jProperties::createAmountCodecConfig);
    return amountCodecConfig.serializeCommandsFromAmount((Amount) value);
  }

  @Override
  public boolean apply(Object object, String name, Object value) {
    AmountCodecConfig amountCodecConfig = ReflectionUtil
            .readableProperty(object.getClass(), name)
            .map(unit4jProperties::createPropertyAmountCodecConfig)
        .orElseGet(unit4jProperties::createAmountCodecConfig);
    return amountCodecConfig.serializeCommandsFromAmount((Amount) value).commands().stream()
        .noneMatch(SerializeCommand.REMOVE_ORIGINAL_FIELD::equals);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void writeBefore(Object object) {
      ReflectionUtil.readableProperties((Class<Object>) object.getClass())
        .stream()
        .filter(fieldWrapper -> fieldWrapper.annotation(AmountSerialize.class).isPresent()
            || Amount.class.equals(fieldWrapper.type().getRawType()))
        .forEach(fieldWrapper -> {
          fieldWrapper.read(object).map(Amount.class::cast).ifPresent(amount -> {
            AmountCodecConfig amountCodecConfig = fieldWrapper.annotation(AmountSerialize.class)
                .map(amountSerialize -> unit4jProperties
                        .createPropertyAmountCodecConfig(fieldWrapper))
                .orElseGet(unit4jProperties::createAmountCodecConfig);
            SerializeCommands serializeCommands = amountCodecConfig
                .serializeCommandsFromAmount(amount);
            if (serializeCommands.commands().stream()
                .anyMatch(SerializeCommand.REMOVE_ORIGINAL_FIELD::equals)) {
              serializeCommands.commands().forEach(command -> {
                if (command.type() == SerializeCommand.Type.WRITE_FIELD) {
                  writeKeyValue(command.fieldName(), command.fieldValue());
                }
              });
            }
          });
        });
  }

  @Override
  public void processExtra(Object object, String key, Object value) {
    log.debug("ExtraProcessor start");
    log.debug("object:{}", object);
    log.debug("key:{}", key);
    log.debug("value:{}", value);
    log.debug("ExtraProcessor end");
  }

  @Override
  public Type getExtraType(Object object, String key) {
    log.debug("ExtraTypeProvider start");
    log.debug("object:{}", object);
    log.debug("key:{}", key);
    log.debug("ExtraTypeProvider end");
    return null;
  }
}
