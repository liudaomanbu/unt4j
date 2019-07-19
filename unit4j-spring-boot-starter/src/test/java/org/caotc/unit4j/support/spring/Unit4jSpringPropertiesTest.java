package org.caotc.unit4j.support.spring;

import javax.annotation.Resource;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@FieldDefaults(makeFinal = false)
@SpringBootTest
@ExtendWith(SpringExtension.class)
class Unit4jSpringPropertiesTest {

  @Resource
  Unit4jSpringProperties unit4jSpringProperties;

  @Test
  void test() {
    System.out.println();
  }
}