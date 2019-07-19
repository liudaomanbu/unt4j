package org.caotc.unit4j.support.spring;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author caotc
 * @date 2019-07-19
 * @since 1.0.0
 */
@Configuration
//@ConditionalOnClass(GlobalRestControllerExceptionHandler.class)
//@ConditionalOnWebApplication
//@ConditionalOnProperty(prefix = "guahao.convention.web",
//    name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(Unit4jSpringProperties.class)
//@ComponentScan(basePackages = {"com.alibaba.fastjson.support.spring"})
@FieldDefaults(makeFinal = false, level = AccessLevel.PRIVATE)
public class Unit4jSpringAutoConfiguration {

  @Autowired
  Unit4jSpringProperties unit4jSpringProperties;
}
