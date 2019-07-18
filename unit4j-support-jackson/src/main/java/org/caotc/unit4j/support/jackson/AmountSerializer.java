package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.support.AmountCodecConfig;

/**
 * {@link Amount}在jackson中的序列化器 //TODO 考虑Spring环境时配置刷新问题
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
@Value
@Slf4j
public class AmountSerializer extends StdSerializer<Amount> {

  /**
   * 序列化反序列化配置
   */
  @NonNull
  AmountCodecConfig amountCodecConfig;
  /**
   * 数值序列化器
   */
  @NonNull
  AmountValueSerializer amountValueSerializer;
  /**
   * 单位序列化器
   */
  @NonNull
  UnitSerializer unitSerializer;

  public AmountSerializer(@NonNull AmountCodecConfig amountCodecConfig) {
    super(Amount.class);
    this.amountCodecConfig = amountCodecConfig;
    this.amountValueSerializer = new AmountValueSerializer(amountCodecConfig.valueCodecConfig());
    unitSerializer = new UnitSerializer(amountCodecConfig.unitCodecConfig());
  }

  @Override
  public void serialize(Amount value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    log.debug("AmountSerializer");
    log.debug("value:{}", value);
    log.debug("gen:{}", gen);
    log.debug("provider:{}", provider);
    gen.writeObject(amountCodecConfig.serializeCommandsFromAmount(value));
  }

}
