package org.caotc.unit4j.core.unit;

import lombok.NonNull;
import org.caotc.unit4j.core.AliasRegistrable;

/**
 * 标准单位，即无词头的单位
 *
 * @author caotc
 * @date 2019-04-26
 * @since 1.0.0
 */
public interface StandardUnit extends Unit, AliasRegistrable {

  @Override
  @NonNull
  default Prefix prefix() {
    return Prefix.EMPTY;
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
  PrefixUnit addPrefix(@NonNull Prefix prefix);
}
