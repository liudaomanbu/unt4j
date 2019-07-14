package org.caotc.unit4j.core.common.util;

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
}

@FieldDefaults(makeFinal = false)
@ToString
abstract class Super {

  protected String stringField = "super";
  protected int intField = 99;
}
