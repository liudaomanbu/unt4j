package org.caotc.unit4j.support;

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.unit.Alias;
import org.caotc.unit4j.core.unit.Unit;

/**
 * {@link Unit}对象序列化和反序列化配置
 *
 * @author caotc
 * @date 2019-05-17
 * @since 1.0.0
 */
@Value
public class UnitCodecConfig {

  /**
   * 别名类型
   */
  @NonNull
  Alias.Type type;
  /**
   * 配置
   */
  @NonNull
  Configuration configuration;
  /**
   * 别名未定义
   */
  @NonNull
  AliasUndefinedStrategy aliasUndefinedStrategy;

  /**
   * 获取单位的序列化指令
   *
   * @param unit 单位
   * @return 序列化指令
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  @NonNull
  public String serialize(@NonNull Unit unit) {
    //TODO 增加UnitserializeStrategy
    return unit.aliasFromConfiguration(configuration(), type()).map(Alias::value)
        .orElseGet(() -> aliasUndefinedStrategy().execute(unit, configuration(), type()));
  }
}
