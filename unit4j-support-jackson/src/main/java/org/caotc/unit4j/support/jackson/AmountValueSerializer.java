package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.math.number.AbstractNumber;
import org.caotc.unit4j.support.AmountValueCodecConfig;

/**
 * {@link Amount#value()}在jackson的序列化器
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
@Value
public class AmountValueSerializer extends StdSerializer<AbstractNumber> {

  /**
   * {@link Amount#value()}的序列化反序列化配置
   */
  @NonNull
  AmountValueCodecConfig amountValueCodecConfig;

  public AmountValueSerializer(@NonNull AmountValueCodecConfig amountValueCodecConfig) {
    super(AbstractNumber.class);
    this.amountValueCodecConfig = amountValueCodecConfig;
  }

  @Override
  public void serialize(AbstractNumber value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    gen.writeObject(amountValueCodecConfig.createSerializeCommands(value));
  }

}
