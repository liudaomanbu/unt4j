package org.caotc.unit4j.core.unit.type;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.NonNull;
import org.caotc.unit4j.core.AliasRegistrable;
import org.caotc.unit4j.core.Aliased;
import org.caotc.unit4j.core.WithId;

/**
 * 单位类型接口
 *
 * @author caotc
 * @date 2018-04-14
 * @since 1.0.0
 **/
@Data
public abstract class UnitType implements WithId, Aliased, AliasRegistrable {

  /**
   * 获取组成该对象的单位类型组件与对应指数
   *
   * @return 组成该对象的单位类型组件与对应指数的Map
   * @author caotc
   * @date 2019-01-11
   * @since 1.0.0
   */
  @NonNull
  public abstract ImmutableMap<UnitType, Integer> unitTypeComponentToExponents();

  /**
   * 重定基准,将所有非基本类型拆解合并,返回等价于原对象但是组件仅为基本单位类型的单位类型.
   *
   * @return 等价于原对象但是组件仅为基本单位类型的单位类型
   * @author caotc
   * @date 2018-08-17
   * @since 1.0.0
   */
  @NonNull
  public abstract UnitType rebase();

  /**
   * 两个单位类型{@link #rebase()}后是否相等
   *
   * @param other 比较的单位类型
   * @return 两个单位类型{@link #rebase()}后是否相等
   * @author caotc
   * @date 2018-10-16
   * @see #rebase()
   * @since 1.0.0
   */
  public final boolean rebaseEquals(@NonNull UnitType other) {
    return rebase().equals(other.rebase());
  }

}
