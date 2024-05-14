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

package org.caotc.unit4j.support.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerialContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.support.QuantityCodecConfig;
import org.caotc.unit4j.support.Unit4jProperties;
import org.caotc.unit4j.support.common.util.QuantityUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 单独{@link Quantity}对象在fastjson中的序列化器
 * //TODO 考虑Spring环境时配置刷新问题
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
@Value(staticConstructor = "of")
@Slf4j
public class QuantitySerializer implements ObjectSerializer {
    @NonNull
    Unit4jProperties unit4jProperties;
    /**
     * 序列化反序列化配置
     */
    @NonNull
    @Getter(lazy = true)
    QuantityCodecConfig codecConfig = unit4jProperties().createQuantityCodecConfig();
    @NonNull
    @Getter(lazy = true)
    QuantityCodecConfig propertyCodecConfig = unit4jProperties().createQuantityCodecConfig();
    /**
     * 数值序列化器
     */
    @NonNull
    @Getter(lazy = true)
    NumberSerializer numberSerializer = NumberSerializer.of(codecConfig().valueCodecConfig());
    /**
     * 单位序列化器
     */
    @NonNull
    @Getter(lazy = true)
    UnitSerializer unitSerializer = UnitSerializer.of(codecConfig().unitCodecConfig());

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) {
        log.debug("object:{},fieldName:{},fieldType:{},features:{}", object, fieldName, fieldType, features);

        Quantity quantity = (Quantity) object;
        QuantityCodecConfig codecConfig = codecConfig();
        SerialContext context = serializer.getContext();
        //todo
        if (Objects.nonNull(context)) {
            codecConfig = Optional.ofNullable(fieldName)
                    .flatMap(name -> QuantityUtil.readableQuantityProperty(context.object, (String) name))
                    .map(unit4jProperties::createPropertyQuantityCodecConfig)
                    .orElseGet(this::propertyCodecConfig);
        }

        //todo convert to targetUnit

        NumberSerializer valueSerializer = NumberSerializer.of(codecConfig.valueCodecConfig());
        switch (codecConfig.strategy()) {
            case FLAT:
                serializer.writeNull();
                serializer.getWriter().write(",");
                List<String> unitPropertyNameWords = codecConfig.outputUnitName();
                String unitPropertyName = codecConfig.nameCaseFormat().join();
                UnitSerializer.of(codecConfig.unitCodecConfig()).write(serializer, quantity.unit(), "unit", quantity.unit().getClass(), features);
                serializer.getWriter().write(",");
                valueSerializer.write(serializer, quantity.value(), "value", quantity.value().getClass(), features);
                break;
            case OBJECT:
                serializer.getWriter().write("{");

                serializer.getWriter().writeFieldName("unit");
                UnitSerializer.of(codecConfig.unitCodecConfig()).write(serializer, quantity.unit(), "unit", quantity.unit().getClass(), features);
                serializer.getWriter().write(",");
                serializer.getWriter().writeFieldName("value");
                valueSerializer.write(serializer, quantity.value(), "value", quantity.value().getClass(), features);
                serializer.getWriter().write("}");
                break;
            case AS_VALUE:
            default:
                valueSerializer.write(serializer, quantity.value(), null, null, features);
        }
    }

}