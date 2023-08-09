package org.caotc.unit4j.core.unit;

import lombok.NonNull;

/**
 * 标准单位，即无词头的单位
 *
 * @author caotc
 * @date 2019-04-26
 * @since 1.0.0
 */
public abstract class StandardUnit extends Unit {

  @Override
  @NonNull
  public final Prefix prefix() {
    return Prefixes.EMPTY;
  }

  /**
   * 给该单位增加词头
   *
   * @param prefix 词头
   * @return 增加词头后的单位
   * @author caotc
   * @date 2019-04-26
   * @since 1.0.0
   */
  @NonNull
  public abstract PrefixUnit addPrefix(@NonNull Prefix prefix);
}
