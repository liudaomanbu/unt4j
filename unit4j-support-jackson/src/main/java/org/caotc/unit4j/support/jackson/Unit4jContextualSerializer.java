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

package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.QuantitySerialize;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.common.util.ReflectionUtil;
import org.caotc.unit4j.support.Unit4jProperties;

import java.io.IOException;
import java.util.Objects;


/**
 * {@link Quantity}在jackson中的上下文序列化器. 为了实现不同类中的{@link Quantity}属性通过注解实现不同策略序列化 ,在jackson中需要通过{@link
 * ContextualSerializer}实现给每个属性返回不同的{@link QuantitySerializer}
 *
 * @author caotc
 * @date 2019-05-11
 * @since 1.0.0
 */
@Value
@Slf4j
public class Unit4jContextualSerializer extends StdSerializer<Quantity> implements
        ContextualSerializer {

    /**
     * 属性
     */
    @NonNull
    Unit4jProperties unit4jProperties;
    @NonNull
    QuantitySerializer quantitySerializer;

  public Unit4jContextualSerializer(@NonNull Unit4jProperties unit4jProperties) {
      super(Quantity.class);
      this.unit4jProperties = unit4jProperties;
      quantitySerializer = new QuantitySerializer(unit4jProperties.createAmountCodecConfig());
  }

    @Override
    public void serialize(Quantity value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        quantitySerializer.serialize(value, gen, provider);
  }

  @Override
  public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
      throws JsonMappingException {
    log.debug("ContextualSerializer");
    log.debug("prov.getActiveView():{}", prov.getActiveView());
    log.debug("property:{}", property);
    //TODO 待确认
    if (property != null) {
        if (Objects.equals(property.getType().getRawClass(), Quantity.class)) {
            QuantitySerialize quantitySerialize = property.getAnnotation(QuantitySerialize.class);
            //TODO 整个类生效
//        if (amountSerialize == null) {
//          amountSerialize = property.getContextAnnotation(AmountSerialize.class);
//        }
            if (quantitySerialize != null) {
                return new QuantitySerializer(
                        unit4jProperties.createPropertyAmountCodecConfig(ReflectionUtil
                                .readablePropertyExact(prov.getActiveView(), property.getName())));
            } else {
                return new QuantitySerializer(unit4jProperties.createAmountCodecConfig());
            }
        }
      return prov.findValueSerializer(property.getType(), property);
    }
      return quantitySerializer;
  }
}
