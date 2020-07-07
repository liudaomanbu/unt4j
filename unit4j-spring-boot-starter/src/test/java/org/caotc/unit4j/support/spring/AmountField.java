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
import org.caotc.unit4j.api.annotation.AmountDeserialize;
import org.caotc.unit4j.api.annotation.AmountSerialize;
import org.caotc.unit4j.api.annotation.CodecStrategy;
import org.caotc.unit4j.api.annotation.DataType;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.math.number.BigDecimal;

import java.math.RoundingMode;


@Value(staticConstructor = "create")
@Accessors(fluent = false, chain = true)
public class AmountField {

    public static AmountField create(Amount amount) {
        return create(amount, amount, amount, amount, amount.intValueExact(), amount.intValueExact(),
                amount.intValueExact());
    }

  Amount noAnnotationAmount;

  @AmountSerialize(strategy = CodecStrategy.VALUE, valueType = BigDecimal.class)
  Amount annotationValueAmount;

  @AmountSerialize(strategy = CodecStrategy.OBJECT, valueType = byte.class, roundingMode = RoundingMode.HALF_UP)
  Amount annotationObjectAmount;

  @AmountSerialize(strategy = CodecStrategy.FLAT, valueType = String.class)
  Amount annotationFlatAmount;

    @AmountSerialize(strategy = CodecStrategy.VALUE, targetUnitId = "SECOND")
    @AmountDeserialize(strategy = CodecStrategy.VALUE)
  Integer dbValue;

    @AmountSerialize(strategy = CodecStrategy.FLAT, targetUnitId = "MINUTE")
    @AmountDeserialize(strategy = CodecStrategy.VALUE)
  Integer jsonValue;

  @DataType("createTime")
  Integer dataTypeValue;
}
