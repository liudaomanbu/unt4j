package org.caotc.unit4j.core.exception;

import lombok.NonNull;

/**
 * 理论上不会发生的异常,如果被抛出,说明该库代码存在bug
 *
 * @author caotc
 * @date 2019-05-24
 * @since 1.0.0
 */
public class NeverHappenException extends IllegalStateException {

  /**
   * 实例对象
   */
  private static final NeverHappenException INSTANCE = new NeverHappenException();

  /**
   * 获取实例对象
   *
   * @return 实例对象
   * @author caotc
   * @date 2019-05-26
   * @since 1.0.0
   */
  @NonNull
  public static NeverHappenException instance() {
    return INSTANCE;
  }

  private NeverHappenException() {
    super("never happen");
  }
}
