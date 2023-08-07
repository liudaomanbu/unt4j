package org.caotc.unit4j.core.unit;

import lombok.NonNull;
import lombok.Value;

/**
 * 有词头的基本单位
 *
 * @author caotc
 * @date 2019-05-26
 * @since 1.0.0
 */
@Value
public class BasePrefixUnit extends PrefixUnit {
  /**
   * 基本标准单位
   */
  @NonNull
  BaseStandardUnit standardUnit;

  BasePrefixUnit(@NonNull Prefix prefix, @NonNull BaseStandardUnit standardUnit) {
    super(prefix);
    this.standardUnit = standardUnit;
  }

  @Override
  public @NonNull Unit simplify(boolean recursive) {
    return this;
  }
}
