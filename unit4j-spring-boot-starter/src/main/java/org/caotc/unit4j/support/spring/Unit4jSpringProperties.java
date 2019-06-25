package org.caotc.unit4j.support.spring;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.caotc.unit4j.support.Unit4jProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 单位库在springboot中的配置对象
 *
 * @author caotc
 * @date 2019-05-30
 * @since 1.0.0
 */
@Component
@ConfigurationProperties("unit4j.default")
@Data
@FieldDefaults(makeFinal = false, level = AccessLevel.PRIVATE)
@Accessors(fluent = false, chain = true)
public class Unit4jSpringProperties {

  Unit4jProperties db;
  Unit4jProperties json;
}
