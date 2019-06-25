package org.caotc.unit4j.support;

import java.math.MathContext;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.math.number.AbstractNumber;
import org.caotc.unit4j.support.SerializeCommand.Type;

/**
 * {@link Amount#value()}序列化反序列化配置
 *
 * @author caotc
 * @date 2019-04-21
 * @since 1.0.0
 */
@Value
public class AmountValueCodecConfig {

  /**
   * 数值转换目标类
   */
  @NonNull
  Class<?> valueType;
  /**
   * 数学计算上下文对象
   */
  @NonNull
  MathContext mathContext;

  /**
   * 获取数值的序列化指令
   *
   * @param number 数值
   * @return 序列化指令
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  @NonNull
  public SerializeCommands createSerializeCommands(@NonNull AbstractNumber number) {
    return SerializeCommands.builder()
        .command(SerializeCommand
            .create(Type.WRITE_VALUE, null, number.value(valueType(), mathContext())))
        .build();
  }
}
