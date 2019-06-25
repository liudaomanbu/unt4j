package org.caotc.unit4j.core.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ReflectionUtilTest {

  @Test
  void fieldWrappersFromClassWithFieldCheck() {
    ReflectionUtil.propertyGettersFromClassWithFieldCheck(Sub.class).forEach(System.out::println);
  }
}


@Data
abstract class Super {

  String a = "super";

  public String getA() {
    return a;
  }
}

@Data
class Sub extends Super {

  String a = "sub";

  @Override
  public String getA() {
    return a;
  }
}