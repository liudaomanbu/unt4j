package org.caotc.unit4j.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ReflectionUtilTest {

  @Test
  void propertyGettersFromClass() {
    ReflectionUtil.propertyGettersFromClass(Sub.class, false).forEach(System.out::println);
  }

  @Test
  void propertySettersFromClass() {
    ReflectionUtil.propertySettersFromClass(Sub.class, false).forEach(System.out::println);
  }

  @Test
  @SneakyThrows
  void jackson() {
    ObjectMapper objectMapper = new ObjectMapper();
    Sub sub = new Sub();
    String json = objectMapper.writeValueAsString(sub);
    System.out.println(json);
    sub = objectMapper.readValue(json, Sub.class);
    System.out.println(sub);
  }
}


@Data
@FieldDefaults(makeFinal = false)
abstract class Super {

  protected String stringField = "super";
}

@Data
@FieldDefaults(makeFinal = false)
@ToString(callSuper = true)
class Sub extends Super {

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

//  public void setNumberField(long numberField) {
//    System.out.println("setNumberField long");
//    this.numberField = numberField;
//  }

  public void setStringField(String stringField) {
    this.stringField = stringField;
  }

  public String getStringField() {
    return stringField;
  }

  public Number getNumberField() {
    return numberField;
  }

//  public Integer getReadNumberField() {
//    return numberField.intValue() + 22;
//  }
}