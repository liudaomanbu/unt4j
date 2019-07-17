package org.caotc.unit4j.core.exception;

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Alias.Type;
import org.caotc.unit4j.core.Aliased;
import org.caotc.unit4j.core.Configuration;

/**
 * 别名未定义异常 获取{@link #undefinedAliased}对象在{@link #configuration}中{@link
 * #aliasType}的别名时,发现符合要求的别名未定义时抛出
 *
 * @author caotc
 * @date 2019-05-17
 * @see Aliased
 * @see org.caotc.unit4j.core.AliasRegistrable
 * @since 1.0.0
 */
@Value
public class AliasUndefinedException extends IllegalArgumentException {

  /**
   * 工厂方法
   *
   * @param undefinedAliased 未定义该类型别名的有别名对象
   * @param configuration 存储别名的配置环境
   * @param aliasType 别名类型
   * @return 别名未定义异常
   * @author caotc
   * @date 2019-05-25
   * @since 1.0.0
   */
  @NonNull
  public static AliasUndefinedException create(@NonNull Aliased undefinedAliased,
      @NonNull Configuration configuration, @NonNull Alias.Type aliasType) {
    return new AliasUndefinedException(undefinedAliased, configuration, aliasType);
  }

  /**
   * 未定义该类型别名的有别名对象
   */
  @NonNull
  Aliased undefinedAliased;

  /**
   * 存储别名的配置环境
   */
  @NonNull
  Configuration configuration;

  /**
   * 别名类型
   */
  @NonNull
  Alias.Type aliasType;

  private AliasUndefinedException(@NonNull Aliased undefinedAliased,
      @NonNull Configuration configuration,
      @NonNull Type aliasType) {
    super(String
        .format("%s dont't have alias that type is %s in configuration that id is %s",
            undefinedAliased,
            aliasType.name(), configuration.id()));
    this.undefinedAliased = undefinedAliased;
    this.configuration = configuration;
    this.aliasType = aliasType;
  }
}
