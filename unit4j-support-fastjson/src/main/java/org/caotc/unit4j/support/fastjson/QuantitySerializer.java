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

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.SerializeCommands;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.support.QuantityCodecConfig;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 单独{@link Quantity}对象在fastjson中的序列化器 //TODO 考虑Spring环境时配置刷新问题
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
@Value
@Slf4j
public class QuantitySerializer implements ObjectSerializer {

    /**
     * 序列化反序列化配置
     */
    @NonNull
    QuantityCodecConfig quantityCodecConfig;
    /**
     * 数值序列化器
     */
    @NonNull
    QuantityValueSerializer quantityValueSerializer;
    /**
     * 单位序列化器
     */
    @NonNull
    UnitSerializer unitSerializer;

    public QuantitySerializer(@NonNull QuantityCodecConfig quantityCodecConfig) {
        this.quantityCodecConfig = quantityCodecConfig;
        quantityValueSerializer = new QuantityValueSerializer(quantityCodecConfig().valueCodecConfig());
        unitSerializer = new UnitSerializer(quantityCodecConfig().unitCodecConfig());
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        Quantity quantity = (Quantity) object;
        SerializeCommands serializeCommands = quantityCodecConfig
                .serializeCommandsFromAmount(quantity);
        ObjectSerializer objectWriter = serializer.getObjectWriter(SerializeCommands.class);
        objectWriter.write(serializer, serializeCommands, fieldName, fieldType, features);
    }

}
