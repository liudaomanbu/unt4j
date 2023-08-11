package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.math.number.Number;
import org.caotc.unit4j.support.QuantityValueCodecConfig;

import java.io.IOException;

/**
 * {@link Quantity#value()}在jackson的序列化器
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
@Value
public class QuantityValueSerializer extends StdSerializer<Number> {

    /**
     * {@link Quantity#value()}的序列化反序列化配置
     */
    @NonNull
    QuantityValueCodecConfig quantityValueCodecConfig;

    public QuantityValueSerializer(@NonNull QuantityValueCodecConfig quantityValueCodecConfig) {
        super(Number.class);
        this.quantityValueCodecConfig = quantityValueCodecConfig;
    }

  @Override
  public void serialize(Number value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
      gen.writeObject(quantityValueCodecConfig.createSerializeCommands(value));
  }

}
