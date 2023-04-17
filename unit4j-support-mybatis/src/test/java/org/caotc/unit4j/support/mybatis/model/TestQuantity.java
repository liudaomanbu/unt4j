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

package org.caotc.unit4j.support.mybatis.model;

import lombok.Data;
import org.caotc.unit4j.api.annotation.CodecStrategy;
import org.caotc.unit4j.api.annotation.QuantityDeserialize;
import org.caotc.unit4j.api.annotation.QuantitySerialize;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.common.reflect.property.ReadableProperty;
import org.caotc.unit4j.core.common.util.ReflectionUtil;


/**
 * `
 *
 * @author caotc
 * @date 2019-09-27
 * @since 1.0.0
 */
@Data
public class TestQuantity {

    Long id;

    @QuantitySerialize(strategy = CodecStrategy.FLAT)
    @QuantityDeserialize(strategy = CodecStrategy.FLAT)
    Quantity data;

    public static void main(String[] args) {
        ReadableProperty<TestQuantity, Object> property = ReflectionUtil
                .readablePropertyExact(TestQuantity.class, "data.value");
        System.out.println(property);
    }
}