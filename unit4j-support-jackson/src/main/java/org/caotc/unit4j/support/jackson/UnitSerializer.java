package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.support.UnitCodecConfig;

import java.io.IOException;

/**
 * {@link org.caotc.unit4j.core.unit.Unit}在jackson的序列化器
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
@Value
public class UnitSerializer extends StdSerializer<Unit> {

  @NonNull
  public static UnitSerializer of(@NonNull UnitCodecConfig codecConfig) {
    return new UnitSerializer(codecConfig, codecConfig);
  }

  @NonNull
  public static UnitSerializer of(@NonNull UnitCodecConfig codecConfig, @NonNull UnitCodecConfig propertyCodecConfig) {
    return new UnitSerializer(codecConfig, propertyCodecConfig);
  }

  /**
   * {@link org.caotc.unit4j.core.unit.Unit}的序列化反序列化配置
   */
  @NonNull
  UnitCodecConfig codecConfig;
  @NonNull
  UnitCodecConfig propertyCodecConfig;

  public UnitSerializer(@NonNull UnitCodecConfig codecConfig, @NonNull UnitCodecConfig propertyCodecConfig) {
    super(Unit.class);
    this.codecConfig = codecConfig;
    this.propertyCodecConfig = propertyCodecConfig;
  }

  @Override
  public void serialize(Unit value, JsonGenerator gen, SerializerProvider provider)
          throws IOException {
    JsonStreamContext sc = gen.getOutputContext();

    UnitCodecConfig unitCodecConfig;
    //是否作为属性
    if (sc.inRoot() || (sc.inArray() && sc.getParent().inRoot())) {
      unitCodecConfig = codecConfig();
    } else {
      unitCodecConfig = propertyCodecConfig();
    }

    String serialize = serialize(unitCodecConfig, value);
    gen.writeString(serialize);
  }

  static String serialize(@NonNull UnitCodecConfig unitCodecConfig, @NonNull Unit unit) {
    switch (unitCodecConfig.strategy()) {
      case AS_ALIAS:
        return unitCodecConfig.aliasSerializer().serialize(unit);
      case AS_ID:
      default:
        return unit.id();
    }
  }
}
