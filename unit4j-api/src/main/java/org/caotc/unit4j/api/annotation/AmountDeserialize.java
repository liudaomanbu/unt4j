/*
 * Copyright (C) 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.caotc.unit4j.api.annotation;

import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.common.base.CaseFormat;
import org.caotc.unit4j.core.constant.StringConstant;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

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
  String targetUnitId() default StringConstant.EMPTY;

  /**
   * @return 来源单位id
   */
  String sourceUnitId() default StringConstant.EMPTY;

  //TODO 目标属性名称的格式以外的内容
}
