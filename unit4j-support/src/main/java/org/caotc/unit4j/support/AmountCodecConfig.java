package org.caotc.unit4j.support;

import com.google.common.collect.ImmutableList;
import java.util.Objects;
import java.util.function.Function;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.unit.Unit;

/**
 * {@link Amount}对象序列化和反序列化配置
 *
 * @author caotc
 * @date 2019-04-21
 * @since 1.0.0
 */
@Value
@Builder(toBuilder = true)
public class AmountCodecConfig {

  private static final String AMOUNT_VALUE_FIELD_NAME = "value";
  private static final String AMOUNT_UNIT_FIELD_NAME = "unit";

  /**
   * 配置
   */
  @NonNull
  Configuration configuration;
  /**
   * 目标单位//TODO 待处理
   */
  Unit targetUnit;
  /**
   * 序列化和反序列化策略
   */
  @NonNull
  CodecStrategy strategy;
  /**
   * 属性名称转换器
   */
  @NonNull
  Function<ImmutableList<String>, String> fieldNameConverter;
//  /**
//   * 属性名称转换器
//   */
//  @NonNull
//  Function<String, String> nameTransformer;
  /**
   * 数值的序列化和反序列化配置
   */
  @NonNull
  AmountValueCodecConfig valueCodecConfig;
  /**
   * 单位的序列化和反序列化配置
   */
  @NonNull
  UnitCodecConfig unitCodecConfig;

  @NonNull
  public String outputName() {
    return fieldNameConverter.apply(ImmutableList.of());
  }

  @NonNull
  public String outputValueName() {
    return fieldNameConverter.apply(ImmutableList.of(AMOUNT_VALUE_FIELD_NAME));
  }

  @NonNull
  public String outputUnitName() {
    return fieldNameConverter.apply(ImmutableList.of(AMOUNT_UNIT_FIELD_NAME));
  }

  /**
   * 在序列化和反序列化之前执行的方法,转换到真正序列化和反序列化的{@link Amount}对象
   *
   * @param amount 数量
   * @return 真正序列化和反序列化的{@link Amount}对象
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  @NonNull
  protected Amount beforeCodec(@NonNull Amount amount) {
    if (Objects.isNull(targetUnit())) {
      return amount;
    }
    return amount.convertTo(targetUnit(), configuration());
  }

  /**
   * 获取传入数量对象的序列化指令
   *
   * @param amount 要进行序列化的数量对象
   * @return 序列化指令
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  //TODO 待删除
  @NonNull
  public <T> SerializeCommands serializeCommandsFromAmount(@NonNull Amount amount) {
//    Function<Class<? extends T>, ? extends Set<FieldWrapper<T, ?>>> kvRemovalListener = type -> ReflectionUtil
//        .fieldWrappersFromClassWithFieldCheck(type, MethodNameStyle.FLUENT);
//    return strategy.createSerializeCommands(beforeCodec(amount), fieldNameConverter()
//        , kvRemovalListener
//        , CaseFormat.LOWER_CAMEL);
    return null;
  }
}
