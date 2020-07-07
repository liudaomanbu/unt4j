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

package org.caotc.unit4j.support.common.constant;

import lombok.experimental.UtilityClass;
import org.caotc.unit4j.core.constant.StringConstant;

/**
 * json相关常量
 *
 * @author caotc
 * @date 2019-05-09
 * @since 1.0.0
 */
@UtilityClass
public class JsonConstant {

  /**
   * json中对象开始符号
   */
  public static final String OBJECT_BEGIN = StringConstant.HALF_WIDTH_LEFT_CURLY_BRACKET;

  /**
   * json中对象结束符号
   */
  public static final String OBJECT_END = StringConstant.HALF_WIDTH_RIGHT_CURLY_BRACKET;

  /**
   * json中属性分隔符
   */
  public static final String FIELD_DELIMITER = StringConstant.HALF_WIDTH_COMMA;
}
