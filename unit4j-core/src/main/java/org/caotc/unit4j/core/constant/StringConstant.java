package org.caotc.unit4j.core.constant;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import lombok.experimental.UtilityClass;

/**
 * 字符串相关常量类
 *
 * @author caotc
 * @date 2018-10-15
 * @since 1.0.0
 **/
@UtilityClass
public class StringConstant {

  /**
   * 空字符串
   */
  public static final String EMPTY = "";

  /**
   * 斜杠
   */
  public static final String SLASH = "/";

  /**
   * 左括号
   */
  public static final String HALF_WIDTH_LEFT_PARENTHESIS = "(";

  /**
   * 右括号
   */
  public static final String HALF_WIDTH_RIGHT_PARENTHESIS = ")";

  /**
   * 减号上标
   */
  public static final String MINUS_SUPERSCRIPTS = "⁻";

  /**
   * 0的上标
   */
  public static final String ZERO_SUPERSCRIPTS = "⁰";

  /**
   * 1的上标
   */
  public static final String ONE_SUPERSCRIPTS = "¹";

  /**
   * 2的上标
   */
  public static final String TWO_SUPERSCRIPTS = "²";

  /**
   * 3的上标
   */
  public static final String THREE_SUPERSCRIPTS = "³";

  /**
   * 4的上标
   */
  public static final String FOUR_SUPERSCRIPTS = "⁴";

  /**
   * 5的上标
   */
  public static final String FIVE_SUPERSCRIPTS = "⁵";

  /**
   * 6的上标
   */
  public static final String SIX_SUPERSCRIPTS = "⁶";

  /**
   * 7的上标
   */
  public static final String SEVEN_SUPERSCRIPTS = "⁷";

  /**
   * 8的上标
   */
  public static final String EIGHT_SUPERSCRIPTS = "⁸";

  /**
   * 9的上标
   */
  public static final String NINE_SUPERSCRIPTS = "⁹";

  /**
   * 半角左花括号(大括号)
   */
  public static final String HALF_WIDTH_LEFT_CURLY_BRACKET = "{";

  /**
   * 半角右花括号(大括号)
   */
  public static final String HALF_WIDTH_RIGHT_CURLY_BRACKET = "}";

  /**
   * 半角逗号
   */
  public static final String HALF_WIDTH_COMMA = ",";

  /**
   * 下划线
   */
  public static final String UNDERSCORE = "_";

  /**
   * 连字符
   */
  public static final String HYPHEN = "-";

  /**
   * 点
   */
  public static final String DOT = ".";

  /**
   * 下划线字符串拼接器
   */
  public static final Joiner UNDERLINE_JOINER = Joiner.on(UNDERSCORE).skipNulls();

  /**
   * 空的字符串拼接器
   */
  public static final Joiner EMPTY_JOINER = Joiner.on("").skipNulls();

  /**
   * 点字符串拼接器
   */
  public static final Joiner DOT_JOINER = Joiner.on(DOT).skipNulls();

  /**
   * 连字符串拼接器
   */
  public static final Joiner HYPHEN_JOINER = Joiner.on(HYPHEN).skipNulls();

  /**
   * 固定一位的字符串拆分器
   */
  public static final Splitter ONE_LENGTH_SPLITTER = Splitter.fixedLength(1);

  /**
   * 斜杠字符串拆分器
   */
  public static final Splitter SLASH_SPLITTER = Splitter.on(SLASH).trimResults();

  /**
   * 点字符串拆分器
   */
  public static final Splitter DOT_SPLITTER = Splitter.on(DOT).trimResults();

  /**
   * 连字符串拆分器
   */
  public static final Splitter HYPHEN_SPLITTER = Splitter.on(HYPHEN).trimResults();

  /**
   * 下划线串拆分器
   */
  public static final Splitter UNDERSCORE_SPLITTER = Splitter.on(UNDERSCORE).trimResults();
}
