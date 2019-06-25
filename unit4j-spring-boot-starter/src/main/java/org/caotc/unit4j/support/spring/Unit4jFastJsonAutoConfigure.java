package org.caotc.unit4j.support.spring;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.AllArgsConstructor;
import org.caotc.unit4j.support.json.fastjson.Unit4jModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 单位库在springboot中关于fastjson的自动配置
 *
 * @author caotc
 * @date 2019-04-21
 * @since 1.0.0
 */
@Configuration
@ConditionalOnBean(FastJsonHttpMessageConverter.class)
@EnableConfigurationProperties(Unit4jSpringProperties.class)
@AllArgsConstructor
public class Unit4jFastJsonAutoConfigure {

  FastJsonHttpMessageConverter fastJsonHttpMessageConverter;
  Unit4jSpringProperties unit4jSpringProperties;

  @Bean
  public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
    Unit4jModule unit4jModule = Unit4jModule.create(unit4jSpringProperties.getJson());
    unit4jModule.registerTo(fastJsonHttpMessageConverter.getFastJsonConfig().getSerializeConfig());
    return fastJsonHttpMessageConverter;
  }
}
