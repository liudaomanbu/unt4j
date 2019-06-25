package org.caotc.unit4j.core.unit;

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.unit.type.UnitType;

/**
 * 量纲(借用量纲分析法的概念) 一个类型的单位+指数={@link Dimension}
 *
 * @author caotc
 * @date 2019-02-03
 * @since 1.0.0
 */
@Value(staticConstructor = "create")
public class Dimension {

  /**
   * 单位
   */
  @NonNull
  Unit unit;

  /**
   * 指数
   */
  int exponent;

  /**
   * 维度单位的类型
   *
   * @return 维度单位的类型
   * @author caotc
   * @date 2019-05-26
   * @since 1.0.0
   */
  public UnitType type() {
    return unit.type();
  }
}
