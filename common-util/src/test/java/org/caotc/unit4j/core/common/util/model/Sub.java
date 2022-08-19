/*
 * Copyright (C) 2021 the original author or authors.
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

package org.caotc.unit4j.core.common.util.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.XSlf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(makeFinal = false)
@ToString(callSuper = true)
@FieldNameConstants
@XSlf4j
public class Sub extends Super implements StringFieldGetter {
    String stringField = "sub";
    Number numberField = 88;

    public void setNumberField(Number numberField) {
        this.numberField = numberField;
        log.debug("setNumberField Number");
    }

    public void setNumberField(int numberField) {
        this.numberField = numberField;
    log.debug("setNumberField int");
  }

  public void setNumberField(long numberField) {
    this.numberField = numberField;
    log.debug("setNumberField long");
  }

  public void setStringField(String stringField) {
    this.stringField = stringField;
  }

  public String getStringField() {
    return stringField;
  }

  public Number getNumberField() {
    return numberField;
  }

  public Integer getReadNumberField() {
    return numberField.intValue() + 22;
  }

  public int getIntField() {
    return intField;
  }

  public void setIntField(int intField) {
    this.intField = intField;
  }

  public String stringField() {
    return stringField;
  }

  public Number numberField() {
    return numberField;
  }

  public Integer readNumberField() {
    return numberField.intValue() + 22;
  }

  public int intField() {
    return intField;
  }
}
