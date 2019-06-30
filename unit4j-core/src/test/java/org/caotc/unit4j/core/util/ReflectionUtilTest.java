package org.caotc.unit4j.core.util;

import lombok.Data;
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
}


@Data
abstract class Super {

  protected String a = "super";

//  public String getA() {
//    return a;
//  }
}

@Data
class Sub extends Super {

//  String a = "sub";

  //  @Override
  public String getA() {
    return a;
  }
}