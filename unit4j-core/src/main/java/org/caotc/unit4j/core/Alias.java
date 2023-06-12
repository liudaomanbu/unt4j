package org.caotc.unit4j.core;

import lombok.NonNull;
import lombok.Value;
import lombok.With;

/**
 * 别名
 *
 * @author caotc
 * @date 2018-12-04
 * @since 1.0.0
 **/
@Value(staticConstructor = "create")
@With
public class Alias {

  @Value(staticConstructor = "create")
  public static class Type {

    @NonNull
    String name;
  }

  /**
   * 类型
   */
  @NonNull
  Type type;
  /**
   * 具体别名
   */
  @NonNull
  String value;
}
