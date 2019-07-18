package org.caotc.unit4j.support.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据类型,标注在属性和get方法上
 *
 * @author caotc
 * @date 2019-07-18
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {METHOD, FIELD})
public @interface DataType {

  /**
   * 数据类型
   */
  String value();
}
