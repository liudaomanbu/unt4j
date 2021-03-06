package org.caotc.unit4j.support.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.common.base.CaseFormat;
import org.caotc.unit4j.support.CodecStrategy;

/**
 * 数量反序列化配置注解
 *
 * @author caotc
 * @date 2019-04-23
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {METHOD, FIELD})
public @interface AmountDeserialize {

  /**
   * @return 反序列化策略
   */
  CodecStrategy strategy() default CodecStrategy.VALUE;

  /**
   * @return 数学计算中的舍入模式
   */
  RoundingMode roundingMode() default RoundingMode.UNNECESSARY;

  /**
   * @return 数学计算精度
   */
  int precision() default 0;

  /**
   * @return 数值序列化时使用的类
   */
  Class<?> valueType() default BigDecimal.class;

  /**
   * @return 配置对象主键
   * @see Configuration#id()
   */
  String configId() default Configuration.DEFAULT_ID;

  /**
   * @return 当前属性名称格式
   */
  CaseFormat caseFormat() default CaseFormat.LOWER_CAMEL;

  /**
   * @return 目标单位id
   */
  String targetUnitId() default "";

  /**
   * @return 来源单位id
   */
  String sourceUnitId() default "";

  //TODO 目标属性名称的格式以外的内容
}
