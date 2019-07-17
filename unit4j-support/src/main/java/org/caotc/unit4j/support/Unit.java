package org.caotc.unit4j.support;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单位注解,标注在属性和get方法上
 *
 * @author caotc
 * @date 2019-07-17
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {METHOD, FIELD})
public @interface Unit {

  /**
   * 单位值,为id或者别名
   */
  String value();

  /**
   * 别名类型,为空字符串时,{@link #value()}为id
   */
  String aliasType() default "";
}
