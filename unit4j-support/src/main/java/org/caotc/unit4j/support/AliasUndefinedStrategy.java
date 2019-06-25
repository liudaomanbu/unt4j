package org.caotc.unit4j.support;

import lombok.NonNull;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.exception.AliasUndefinedException;
import org.caotc.unit4j.core.unit.Alias;
import org.caotc.unit4j.core.unit.Unit;

/**
 * {@link org.caotc.unit4j.core.Aliased}有别名的对象的{@link Alias}未注册时的策略枚举
 *
 * @author caotc
 * @date 2019-05-18
 * @since 1.0.0
 */
public enum AliasUndefinedStrategy {
  /**
   * 抛出异常
   */
  THROW_EXCEPTION {
    @Override
    public @NonNull String execute(@NonNull Unit unit, @NonNull Configuration configuration,
        @NonNull Alias.Type aliasType) {
      throw AliasUndefinedException.create(unit, configuration, aliasType);
    }
  },
  /**
   * 自动组合
   */
  AUTO_COMPOSITE {
    @Override
    public @NonNull String execute(@NonNull Unit unit, @NonNull Configuration configuration,
        @NonNull Alias.Type aliasType) {
      return unit.compositeAliasFromConfiguration(configuration, aliasType).map(Alias::value)
          .orElseThrow(() -> AliasUndefinedException.create(unit, configuration, aliasType));
    }
  };

  /**
   * 执行策略
   *
   * @param unit 单位
   * @param configuration 配置
   * @param aliasType 别名类型
   * @return 策略执行后的别名
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  @NonNull
  public abstract String execute(@NonNull Unit unit, @NonNull Configuration configuration,
      @NonNull Alias.Type aliasType);
}
