package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.support.UnitCodecConfig;

/**
 * {@link org.caotc.unit4j.core.unit.Unit}在jackson的序列化器
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
@Value
public class UnitSerializer extends StdSerializer<Unit> {

  /**
   * {@link org.caotc.unit4j.core.unit.Unit}的序列化反序列化配置
   */
  @NonNull
  UnitCodecConfig unitCodecConfig;

  public UnitSerializer(@NonNull UnitCodecConfig unitCodecConfig) {
    super(Unit.class);
    this.unitCodecConfig = unitCodecConfig;
  }

  @Override
  public void serialize(Unit value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    gen.writeString(unitCodecConfig.serialize(value));
  }
}
