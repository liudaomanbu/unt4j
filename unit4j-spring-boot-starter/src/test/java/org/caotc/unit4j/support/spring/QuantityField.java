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

package org.caotc.unit4j.support.spring;

import lombok.Value;
import lombok.experimental.Accessors;
import org.caotc.unit4j.api.annotation.CodecStrategy;
import org.caotc.unit4j.api.annotation.DataType;
import org.caotc.unit4j.api.annotation.QuantityDeserialize;
import org.caotc.unit4j.api.annotation.QuantitySerialize;
import org.caotc.unit4j.core.Quantity;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Value(staticConstructor = "create")
@Accessors(fluent = false, chain = true)
public class QuantityField {

    Quantity noAnnotationQuantity;
    @QuantitySerialize(strategy = CodecStrategy.VALUE, valueType = BigDecimal.class)
    Quantity annotationValueQuantity;
    @QuantitySerialize(strategy = CodecStrategy.OBJECT, valueType = byte.class, roundingMode = RoundingMode.HALF_UP)
    Quantity annotationObjectQuantity;
    @QuantitySerialize(strategy = CodecStrategy.FLAT, valueType = String.class)
    Quantity annotationFlatQuantity;
    @QuantitySerialize(strategy = CodecStrategy.VALUE, targetUnitId = "SECOND")
    @QuantityDeserialize(strategy = CodecStrategy.VALUE)
    Integer dbValue;
    @QuantitySerialize(strategy = CodecStrategy.FLAT, targetUnitId = "MINUTE")
    @QuantityDeserialize(strategy = CodecStrategy.VALUE)
    Integer jsonValue;

    public static QuantityField create(Quantity quantity) {
        return create(quantity, quantity, quantity, quantity, quantity.intValueExact(), quantity.intValueExact(),
                quantity.intValueExact());
    }

    @DataType("createTime")
    Integer dataTypeValue;
}
