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
  public static final String FIELD_SEPARATOR = StringConstant.HALF_WIDTH_COMMA;
}
