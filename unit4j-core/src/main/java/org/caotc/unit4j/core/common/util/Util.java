package org.caotc.unit4j.core.common.util;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Streams;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.caotc.unit4j.core.constant.StringConstant;

/**
 * 工具类
 *
 * @author caotc
 * @date 2018-10-15
 * @implSpec
 * @implNote
 * @since 1.0.0
 **/
@UtilityClass
public class Util {

  /**
   * 个位数与对应的上标的map
   */
  private static final ImmutableBiMap<Integer, String> SINGLE_DIGIT_TO_SUPERSCRIPTS = ImmutableBiMap.<Integer, String>builder()
      .put(0, StringConstant.ZERO_SUPERSCRIPTS).put(1, StringConstant.ONE_SUPERSCRIPTS)
      .put(2, StringConstant.TWO_SUPERSCRIPTS).put(3, StringConstant.THREE_SUPERSCRIPTS)
      .put(4, StringConstant.FOUR_SUPERSCRIPTS).put(5, StringConstant.FIVE_SUPERSCRIPTS)
      .put(6, StringConstant.SIX_SUPERSCRIPTS).put(7, StringConstant.SEVEN_SUPERSCRIPTS)
      .put(8, StringConstant.EIGHT_SUPERSCRIPTS).put(9, StringConstant.NINE_SUPERSCRIPTS).build();

  /**
   * 上标与个位数对应的的map
   */
  private static final ImmutableBiMap<String, Integer> SUPERSCRIPT_TO_SINGLE_DIGITS = SINGLE_DIGIT_TO_SUPERSCRIPTS
      .inverse();

  /**
   * 根据整数获取对应的上标
   *
   * @param number 需要获得上标的整数
   * @return 整数对应的上标
   * @author caotc
   * @date 2018-10-15
   * @since 1.0.0
   */
  @NonNull
  public static String getSuperscript(int number) {
    String superscriptAbs = Streams
        .stream(StringConstant.ONE_LENGTH_SPLITTER.split(String.valueOf(Math.abs(number))))
        .mapToInt(Integer::valueOf)
        .mapToObj(SINGLE_DIGIT_TO_SUPERSCRIPTS::get)
        .collect(Collectors.joining());
    return number < 0 ? StringConstant.EMPTY_JOINER
        .join(StringConstant.MINUS_SUPERSCRIPTS, superscriptAbs)
        : superscriptAbs;
  }

  /**
   * 生成组合的id或别名
   *
   * @param stringSupplierToExponents id或别名与对应指数Map
   * @return 生成的默认的组合id
   * @author caotc
   * @date 2018-10-15
   * @since 1.0.0
   */
  @NonNull
  public static String createCompositeIdOrAlias(
      @NonNull Map<? extends Supplier<String>, Integer> stringSupplierToExponents) {
    return stringSupplierToExponents.entrySet().stream()
        .map(entry -> StringConstant.EMPTY_JOINER
            .join(StringConstant.HALF_WIDTH_LEFT_PARENTHESIS, entry.getKey().get(),
                StringConstant.HALF_WIDTH_RIGHT_PARENTHESIS, getSuperscript(entry.getValue())))
        .collect(Collectors.joining());
  }
}
