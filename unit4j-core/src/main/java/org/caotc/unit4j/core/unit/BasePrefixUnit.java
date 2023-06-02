package org.caotc.unit4j.core.unit;

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.unit.type.BaseUnitType;

/**
 * 有词头的基本单位
 *
 * @author caotc
 * @date 2019-05-26
 * @since 1.0.0
 */
@Value
public class BasePrefixUnit extends PrefixUnit {
//  /**todo
//   * 词头
//   */
//  @NonNull
//  Prefix prefix;
//
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
  public @NonNull BaseUnitType type() {
    return standardUnit().type();
  }

  @Override
  public @NonNull BasePrefixUnit rebase() {
    return this;
  }

  @Override
  public @NonNull Unit simplify(@NonNull SimplifyConfig config) {
    return this;
  }
}
