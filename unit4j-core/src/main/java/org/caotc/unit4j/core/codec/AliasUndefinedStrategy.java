package org.caotc.unit4j.core.codec;

import lombok.NonNull;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.exception.AliasUndefinedException;
import org.caotc.unit4j.core.unit.Prefix;
import org.caotc.unit4j.core.unit.Unit;

/**
 * 有别名的对象的{@link Alias}未注册时的策略枚举
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

    @Override
    public @NonNull String execute(@NonNull Prefix prefix, @NonNull Configuration configuration, Alias.@NonNull Type aliasType) {
      return null;
    }
  },
  /**
   * 自动组合
   */
  AUTO_COMPOSITE {
    @Override
    public @NonNull String execute(@NonNull Unit unit, @NonNull Configuration configuration,
                                   @NonNull Alias.Type aliasType) {
//      return unit.compositeAliasFromConfiguration(configuration, aliasType).map(Alias::value)
//          .orElseThrow(() -> AliasUndefinedException.create(unit, configuration, aliasType));
      return "";//todo
    }

    @Override
    public @NonNull String execute(@NonNull Prefix prefix, @NonNull Configuration configuration, Alias.@NonNull Type aliasType) {
      return null;
    }
  },
  ID {
    @Override
    public @NonNull String execute(@NonNull Unit unit, @NonNull Configuration configuration,
                                   @NonNull Alias.Type aliasType) {
      return "";//todo
    }

    @Override
    public @NonNull String execute(@NonNull Prefix prefix, @NonNull Configuration configuration, Alias.@NonNull Type aliasType) {
      return prefix.id();
    }
  };

  /**
   * 执行策略
   *
   * @param unit          单位
   * @param configuration 配置
   * @param aliasType     别名类型
   * @return 策略执行后的别名
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  @NonNull
  public abstract String execute(@NonNull Unit unit, @NonNull Configuration configuration,
                                 @NonNull Alias.Type aliasType);

  @NonNull
  public abstract String execute(@NonNull Prefix prefix, @NonNull Configuration configuration,
                                 @NonNull Alias.Type aliasType);
}
