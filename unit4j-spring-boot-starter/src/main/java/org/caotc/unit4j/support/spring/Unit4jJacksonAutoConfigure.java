package org.caotc.unit4j.support.spring;

import lombok.AllArgsConstructor;
import org.caotc.unit4j.support.jackson.Unit4jModule;
import org.springframework.context.annotation.Bean;

/**
 * 单位库在springboot中关于jackson的自动配置
 *
 * @author caotc
 * @date 2019-04-21
 * @since 1.0.0
 */
//@Configuration
//@ConditionalOnClass(ObjectMapper.class)
//@EnableConfigurationProperties(Unit4jSpringProperties.class)
@AllArgsConstructor
public class Unit4jJacksonAutoConfigure {

  Unit4jSpringProperties unit4jSpringProperties;

  @Bean
  public Unit4jModule unit4jModule() {
    return Unit4jModule.create(unit4jSpringProperties.getJson());
  }
}
