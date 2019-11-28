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

package org.caotc.unit4j.support.mybatis.model;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.caotc.unit4j.support.annotation.AmountSerialize;
import org.caotc.unit4j.support.annotation.WithUnit;
import org.caotc.unit4j.support.annotation.WithUnit.ValueType;

/**
 * `
 *
 * @author caotc
 * @date 2019-09-27
 * @since 1.0.0
 */
@Data
@FieldDefaults(makeFinal = false, level = AccessLevel.PRIVATE)
public class TestAmount {

  Long id;

  @AmountSerialize(targetUnitId = "SECOND")
  @WithUnit("MINUTE")
  BigDecimal withUnitValue;

  @WithUnit(value = "unit", valueType = ValueType.PROPERTY_NAME)
  BigDecimal withUnitProperty;

  String unit;

  Object object;
}