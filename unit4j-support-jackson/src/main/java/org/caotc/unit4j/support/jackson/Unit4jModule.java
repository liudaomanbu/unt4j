package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.support.Unit4jProperties;

/**
 * @author caotc
 * @date 2019-05-12
 * @since 1.0.0
 */
@Value
public class Unit4jModule extends SimpleModule {

  public static Unit4jModule create(@NonNull Unit4jProperties unit4jProperties) {
      Unit4jModule unit4jModule = new Unit4jModule();
      Unit4jContextualSerializer unit4jContextualSerializer = new Unit4jContextualSerializer(
              unit4jProperties);
      unit4jModule.addSerializer(unit4jContextualSerializer);
      unit4jModule.addSerializer(unit4jContextualSerializer.quantitySerializer());
      unit4jModule
              .addSerializer(unit4jContextualSerializer.quantitySerializer().quantityValueSerializer());
      unit4jModule.addSerializer(unit4jContextualSerializer.quantitySerializer().unitSerializer());
      SerializeCommandsSerializer serializeCommandsSerializer = new SerializeCommandsSerializer();
      unit4jModule.addSerializer(serializeCommandsSerializer);
      return unit4jModule;
  }

  private Unit4jModule() {
    super("unit4jModule");
  }

  /**
   * 注册到jackson配置
   *
   * @param mapper jackson序列化配置
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  public void registerTo(@NonNull ObjectMapper mapper) {
    mapper.registerModule(this);
  }
}
