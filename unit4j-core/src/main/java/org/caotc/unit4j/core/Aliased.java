package org.caotc.unit4j.core;

import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import lombok.NonNull;

/**
 * 有别名的.实现该接口的类可以从{@link Configuration}中获取别名
 *
 * @author caotc
 * @date 2019-05-29
 * @since 1.0.0
 */
public interface Aliased {

  /**
   * 从指定配置中获取{@code this}的所有别名
   *
   * @param configuration 配置
   * @return {@code this}在{@code configuration}中的所有别名
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  @NonNull
  ImmutableSet<Alias> aliasesFromConfiguration(@NonNull Configuration configuration);

  /**
   * 从指定配置中获取{@code this}的指定类型别名
   *
   * @param configuration 配置
   * @param aliasType 别名类型
   * @return {@code this}在{@code configuration}中的指定类型别名
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  @NonNull
  Optional<Alias> aliasFromConfiguration(@NonNull Configuration configuration,
      @NonNull Alias.Type aliasType);

  /**
   * 从指定配置中获取{@code this}的指定类型别名. 如果没有该类型别名,则尝试使用组件的该类型别名自动组合生成别名.
   *
   * @param configuration 配置
   * @param aliasType 别名类型
   * @return {@code this}在{@code configuration}中的指定类型别名
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  @NonNull
  Optional<Alias> compositeAliasFromConfiguration(@NonNull Configuration configuration,
      @NonNull Alias.Type aliasType);
}
