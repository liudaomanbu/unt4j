package org.caotc.unit4j.core.unit;

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Identifiable;

/**
 * 词头
 *
 * @author caotc
 * @date 2018-04-13
 * @since 1.0.0
 **/
@Value(staticConstructor = "of")
public class Prefix implements Identifiable {

  @NonNull
  String id;//todo id check？
  /**
   * 是否是空词头 todo
   *
   * @return 是否是空词头
   * @author caotc
   * @date 2019-04-25
   * @since 1.0.0
   */
  public boolean isEmpty() {
    return id.isEmpty();
  }
}
