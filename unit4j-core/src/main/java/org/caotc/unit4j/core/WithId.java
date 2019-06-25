package org.caotc.unit4j.core;

import java.util.function.Supplier;
import lombok.NonNull;

/**
 * 有主键的
 *
 * @author caotc
 * @date 2018-09-28
 * @since 1.0.0
 **/
public interface WithId extends Supplier<String> {

  /**
   * 获取{@code this}主键
   *
   * @return 主键
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  @NonNull
  String id();

  /**
   * 获取{@code this}主键
   *
   * @return 主键
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  @Override
  default String get() {
    return id();
  }
}
