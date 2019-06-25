package org.caotc.unit4j.core.util;

import com.google.common.base.Converter;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.caotc.unit4j.core.constant.StringConstant;

/**
 * 对guava包中的{@link com.google.common.base.CaseFormat}的装饰器
 *
 * @author caotc
 * @date 2019-06-05
 * @see com.google.common.base.CaseFormat
 * @since 1.0.0
 */
@AllArgsConstructor
public enum CaseFormat {
  /**
   * {@link com.google.common.base.CaseFormat#LOWER_HYPHEN}
   */
  LOWER_HYPHEN(com.google.common.base.CaseFormat.LOWER_HYPHEN, StringConstant.HYPHEN_SPLITTER) {
    @Override
    public @NonNull String join(@NonNull Stream<String> words) {
      return words.map(String::toLowerCase).collect(Collectors.joining(StringConstant.HYPHEN));
    }
  },
  /**
   * {@link com.google.common.base.CaseFormat#LOWER_UNDERSCORE}
   */
  LOWER_UNDERSCORE(com.google.common.base.CaseFormat.LOWER_UNDERSCORE,
      StringConstant.UNDERSCORE_SPLITTER) {
    @Override
    public @NonNull String join(@NonNull Stream<String> words) {
      return words.map(String::toLowerCase).collect(Collectors.joining(StringConstant.UNDERSCORE));
    }
  },
  /**
   * {@link com.google.common.base.CaseFormat#LOWER_CAMEL}
   */
  LOWER_CAMEL(com.google.common.base.CaseFormat.LOWER_CAMEL, StringConstant.UNDERSCORE_SPLITTER) {
    String upperCaseRegex = "[A-Z]";
    String replace = StringConstant.UNDERSCORE + "$0";

    @Override
    public @NonNull ImmutableList<String> split(@NonNull String name) {
      return super.split(name.replaceAll(upperCaseRegex, replace));
    }

    @Override
    public @NonNull String join(@NonNull Stream<String> words) {
      return LOWER_HYPHEN.decorated
          .to(com.google.common.base.CaseFormat.LOWER_CAMEL, LOWER_HYPHEN.join(words));
    }
  },
  /**
   * {@link com.google.common.base.CaseFormat#UPPER_CAMEL}
   */
  UPPER_CAMEL(com.google.common.base.CaseFormat.UPPER_CAMEL, StringConstant.UNDERSCORE_SPLITTER) {
    String upperCaseRegex = "[A-Z]";
    String replace = StringConstant.UNDERSCORE + "$0";
    Converter<String, String> lowerCamelToUpperCamel = com.google.common.base.CaseFormat.LOWER_CAMEL
        .converterTo(com.google.common.base.CaseFormat.UPPER_CAMEL);

    @Override
    public @NonNull ImmutableList<String> split(@NonNull String name) {
      return super.split(name.replaceAll(upperCaseRegex, replace));
    }

    @Override
    public @NonNull String join(@NonNull Stream<String> words) {
      return words.map(String::toLowerCase).map(lowerCamelToUpperCamel::convert)
          .collect(Collectors.joining());
    }
  },
  /**
   * {@link com.google.common.base.CaseFormat#UPPER_UNDERSCORE}
   */
  UPPER_UNDERSCORE(com.google.common.base.CaseFormat.UPPER_UNDERSCORE,
      StringConstant.UNDERSCORE_SPLITTER) {
    @Override
    public @NonNull String join(@NonNull Stream<String> words) {
      return words.map(String::toUpperCase).collect(Collectors.joining(StringConstant.UNDERSCORE));
    }
  };
  /**
   * 装饰对象
   */
  @NonNull
  com.google.common.base.CaseFormat decorated;
  /**
   * 拆分器,{@link #split(String)}默认实现即使用该对象拆分
   */
  @NonNull
  Splitter splitter;

  /**
   * 将名称从{@code this}格式转换为{@code targetFormat}格式
   *
   * @param targetFormat 目标格式
   * @param name 需要转换格式的名称
   * @return 转换格式后的名称
   * @author caotc
   * @date 2019-06-06
   * @see com.google.common.base.CaseFormat#to(com.google.common.base.CaseFormat, String)
   * @since 1.0.0
   */
  @NonNull
  public String to(@NonNull CaseFormat targetFormat, @NonNull String name) {
    return decorated.to(targetFormat.decorated, name);
  }

  /**
   * 获得当前格式与目标格式的转换器
   *
   * @param targetFormat 目标格式
   * @return 当前格式与目标格式的转换器
   * @author caotc
   * @date 2019-06-06
   * @see com.google.common.base.CaseFormat#converterTo(com.google.common.base.CaseFormat)
   * @since 1.0.0
   */
  @NonNull
  public Converter<String, String> converterTo(
      @NonNull CaseFormat targetFormat) {
    return decorated.converterTo(targetFormat.decorated);
  }

  /**
   * 将传入名称按照当前格式拆分为单词集合
   *
   * @param name 需要拆分的名称
   * @return 组成名称的单词集合
   * @author caotc
   * @date 2019-06-06
   * @since 1.0.0
   */
  @NonNull
  public ImmutableList<String> split(@NonNull String name) {
    return ImmutableList.copyOf(splitter.split(name));
  }

  /**
   * 将单词集合按照当前格式生成名称
   *
   * @param words 单词集合
   * @return 当前格式生成的名称
   * @throws IllegalStateException if stream has already been operated upon or closed
   * @author caotc
   * @date 2019-06-06
   * @apiNote {@code words}必须为普通流而不是并行流,否则可能无法得到预期结果
   * @since 1.0.0
   */
  @NonNull
  public abstract String join(@NonNull Stream<String> words);

  /**
   * 将单词集合按照当前格式生成名称
   *
   * @param words 单词集合
   * @return 当前格式生成的名称
   * @author caotc
   * @date 2019-06-06
   * @since 1.0.0
   */
  @NonNull
  public String join(@NonNull String[] words) {
    return join(Arrays.stream(words));
  }

  /**
   * 将单词集合按照当前格式生成名称
   *
   * @param words 单词集合
   * @return 当前格式生成的名称
   * @author caotc
   * @date 2019-06-06
   * @since 1.0.0
   */
  @NonNull
  public String join(@NonNull Iterable<String> words) {
    return join(Streams.stream(words));
  }

  /**
   * 将单词集合按照当前格式生成名称
   *
   * @param words 单词集合
   * @return 当前格式生成的名称
   * @author caotc
   * @date 2019-06-06
   * @since 1.0.0
   */
  @NonNull
  public String join(@NonNull Iterator<String> words) {
    return join(Streams.stream(words));
  }
}
