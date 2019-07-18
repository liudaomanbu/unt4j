package org.caotc.unit4j.support.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import java.io.IOException;
import java.lang.reflect.Type;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.support.AmountCodecConfig;
import org.caotc.unit4j.support.SerializeCommands;

/**
 * 单独{@link Amount}对象在fastjson中的序列化器 //TODO 考虑Spring环境时配置刷新问题
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
@Value
@Slf4j
public class AmountSerializer implements ObjectSerializer {

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
    this.amountCodecConfig = amountCodecConfig;
    amountValueSerializer = new AmountValueSerializer(amountCodecConfig().valueCodecConfig());
    unitSerializer = new UnitSerializer(amountCodecConfig().unitCodecConfig());
  }

  @Override
  public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
      int features) throws IOException {
    Amount amount = (Amount) object;
    SerializeCommands serializeCommands = amountCodecConfig
        .serializeCommandsFromAmount(amount);
    ObjectSerializer objectWriter = serializer.getObjectWriter(SerializeCommands.class);
    objectWriter.write(serializer, serializeCommands, fieldName, fieldType, features);
  }

}
