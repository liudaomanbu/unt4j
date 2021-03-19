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

import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = false)
@ToString(callSuper = true)
public class Sub extends Super {
  String stringField = "sub";
  Number numberField = 88;

  public void setNumberField(Number numberField) {
    System.out.println("setNumberField Number");
    this.numberField = numberField;
  }

  public void setNumberField(int numberField) {
    System.out.println("setNumberField int");
    this.numberField = numberField;
  }

  public void setNumberField(long numberField) {
    System.out.println("setNumberField long");
    this.numberField = numberField;
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
