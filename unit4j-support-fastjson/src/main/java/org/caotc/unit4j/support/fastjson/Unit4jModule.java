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

import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.math.number.BigFractionAdapter;
import org.caotc.unit4j.core.math.number.Number;
import org.caotc.unit4j.core.unit.BasePrefixUnit;
import org.caotc.unit4j.core.unit.BaseStandardUnit;
import org.caotc.unit4j.core.unit.CompositePrefixUnit;
import org.caotc.unit4j.core.unit.CompositeStandardUnit;
import org.caotc.unit4j.support.Unit4jProperties;

/**
 * unit4j库的fastjson的序列化与反序列化模块. 所有fastjson的序列化与反序列化需要的对象都包装在该类中使用.
 *
 * @author caotc
 * @date 2019-05-12
 * @since 1.0.0
 */
@Value(staticConstructor = "of")
public class Unit4jModule {

    @NonNull
    Unit4jProperties unit4jProperties;
    /**
     * 单独{@link Quantity}对象的序列化器
     */
    @NonNull
    @Getter(lazy = true)
    QuantitySerializer quantitySerializer = QuantitySerializer.of(unit4jProperties());
    @NonNull
    @Getter(lazy = true)
    NumberSerializer numberSerializer = NumberSerializer.of(unit4jProperties().createQuantityCodecConfig().valueCodecConfig());
    @NonNull
    @Getter(lazy = true)
    UnitSerializer unitSerializer = UnitSerializer.of(unit4jProperties().createQuantityCodecConfig().unitCodecConfig());

    /**
     * 注册到fastjson配置
     *
     * @param serializeConfig fastjson序列化配置
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    public void registerTo(@NonNull SerializeConfig serializeConfig) {
        //todo @WithUnit问题没有解决
        serializeConfig.put(Quantity.class, quantitySerializer());
        serializeConfig.put(Number.class, numberSerializer());
        serializeConfig.put(BigFractionAdapter.class, numberSerializer());
        serializeConfig.put(BaseStandardUnit.class, unitSerializer());
        serializeConfig.put(BasePrefixUnit.class, unitSerializer());
        serializeConfig.put(CompositeStandardUnit.class, unitSerializer());
        serializeConfig.put(CompositePrefixUnit.class, unitSerializer());
    }
}
