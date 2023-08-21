/*
 * Copyright (C) 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.caotc.unit4j.support;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.caotc.unit4j.api.annotation.CodecStrategy;
import org.caotc.unit4j.api.annotation.QuantityDeserialize;
import org.caotc.unit4j.api.annotation.QuantitySerialize;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Alias.Type;
import org.caotc.unit4j.core.Aliases;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.common.base.CaseFormat;
import org.caotc.unit4j.core.common.reflect.property.Property;
import org.caotc.unit4j.core.common.reflect.property.WritableProperty;
import org.caotc.unit4j.core.serializer.AliasUndefinedStrategy;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 属性,生成{@link QuantityCodecConfig}对象使用 //TODO 可变性考虑
 *
 * @author caotc
 * @date 2019-04-21
 * @see QuantitySerialize
 * @see QuantityCodecConfig
 * @since 1.0.0
 */
@Data
@FieldDefaults(makeFinal = false, level = AccessLevel.PRIVATE)
@Accessors(fluent = false, chain = true)
public class Unit4jProperties {
  private static final String AMOUNT_VALUE_FIELD_NAME = "value";
  private static final String AMOUNT_UNIT_FIELD_NAME = "unit";

  /**
   * 默认的数学运算配置
   */
  public static final MathContext DEFAULT_MATH_CONTEXT = MathContext.UNLIMITED;
  /**
   * 默认的值序列化时的类型
   */
  public static final Class<BigDecimal> DEFAULT_VALUE_TYPE = BigDecimal.class;
  /**
   * 默认的单位转换配置
   */
  public static final Configuration DEFAULT_CONFIGURATION = Configuration.defaultInstance();
  /**
   * 默认的编码解码策略
   */
  public static final CodecStrategy DEFAULT_STRATEGY = CodecStrategy.VALUE;
  /**
   * 默认的名称拆分器
   */
  public static final Function<@NonNull String, @NonNull List<String>> DEFAULT_NAME_SPLITTER = CaseFormat.LOWER_CAMEL::split;
  /**
   * 默认的名称拼接器
   */
  public static final Function<@NonNull List<String>, @NonNull String> DEFAULT_NAME_JOINER = CaseFormat.LOWER_CAMEL::join;
  /**
   * 默认的名称转换器
   */
  public static final Function<@NonNull String, @NonNull String> DEFAULT_NAME_CONVERTER = DEFAULT_NAME_SPLITTER
          .andThen(DEFAULT_NAME_JOINER);
  /**
   * 默认的属性名称拼接器
   */
  public static final BiFunction<@NonNull List<String>, @NonNull List<String>, @NonNull String> DEFAULT_FIELD_NAME_JOINER = (valueFieldNameWords, objectFieldNameWords) -> CaseFormat.LOWER_CAMEL
          .join(Stream.concat(objectFieldNameWords.stream(), valueFieldNameWords.stream()).collect(Collectors.toList()));

  /**
   * 默认单位的输出别名类型
   */
  public static final Type DEFAULT_UNIT_ALIAS_TYPE = Aliases.Types.ENGLISH_NAME;
  /**
   * 默认单位别名未定义策略
   */
  public static final AliasUndefinedStrategy DEFAULT_UNIT_UNDEFINED_STRATEGY = AliasUndefinedStrategy.THROW_EXCEPTION;

  /**
   * 数学运算的舍入模式
   */
  @NonNull
  RoundingMode roundingMode = DEFAULT_MATH_CONTEXT.getRoundingMode();
  /**
   * 数学运算的精度
   */
  int precision = DEFAULT_MATH_CONTEXT.getPrecision();
  /**
   * 数值转换类
   */
  @NonNull
  Class<?> valueType = DEFAULT_VALUE_TYPE;
  /**
   * 配置
   */
  //TODO
  //  TargetUnitChooser targetUnitChooser;
  @NonNull
  Configuration configuration = DEFAULT_CONFIGURATION;
  /**
   * 数学运算的上下文
   */
  @NonNull
  MathContext mathContext = DEFAULT_MATH_CONTEXT;
    /**
     * 单独的{@link Quantity}对象的序列化反序列化策略
     */
  @NonNull
  CodecStrategy strategy = DEFAULT_STRATEGY;
    /**
     * 作为其他类属性的{@link Quantity}对象的序列化反序列化策略
     */
  @NonNull
  CodecStrategy propertyStrategy = DEFAULT_STRATEGY;
    /**
     * 单独的{@link Quantity}对象的名称拼接器
     */
  Function<@NonNull List<String>, @NonNull String> nameJoiner = DEFAULT_NAME_JOINER;
    /**
     * 作为其他类属性的{@link Quantity}对象的属性名称拆分器
     */
  Function<@NonNull String, @NonNull List<String>> fieldNameSplitter = DEFAULT_NAME_SPLITTER;
    /**
     * 作为其他类属性的{@link Quantity}对象的属性名称拼接器
     */
  BiFunction<@NonNull List<String>, @NonNull List<String>, @NonNull String> fieldNameJoiner = DEFAULT_FIELD_NAME_JOINER;

  /**
   * 单位的别名类型
   */
  @NonNull
  Alias.Type unitAliasType = DEFAULT_UNIT_ALIAS_TYPE;
  /**
   * 别名未定义策略
   */
  @NonNull
  AliasUndefinedStrategy unitAliasUndefinedStrategy = DEFAULT_UNIT_UNDEFINED_STRATEGY;

  /**
   * 单位转换关系额外注入//TODO
   */
  @NonNull
  Map<String, Map<String, String>> unitConvertConfigs = Maps.newHashMap();

    /**
     * 获取单独的{@link Quantity}对象的序列化反序列化配置
     *
     * @return 序列化反序列化配置
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    @NonNull
    public QuantityCodecConfig createAmountCodecConfig() {
        return QuantityCodecConfig.builder().configuration(getConfiguration()).strategy(getStrategy())
//        .nameTransformer()
                .outputName(getNameJoiner().apply(ImmutableList.of()))
                .outputValueName(getNameJoiner().apply(ImmutableList.of(AMOUNT_VALUE_FIELD_NAME)))
                .outputUnitName(getNameJoiner().apply(ImmutableList.of(AMOUNT_UNIT_FIELD_NAME)))
                .valueCodecConfig(new NumberCodecConfig(getValueType(), getMathContext()))
                .unitCodecConfig(new UnitCodecConfig(getUnitAliasType(), getConfiguration(),
                        getUnitAliasUndefinedStrategy())).build();
    }

    /**
     * 获取作为其他类属性的{@link Quantity}对象的序列化反序列化配置
     *
     * @param amountReadableProperty 属性名称
     * @return 序列化反序列化配置
     * @author caotc
     * @date 2019-11-06
     * @since 1.0.0
     */
    @NonNull
    @SuppressWarnings("unchecked")
    public QuantityCodecConfig createPropertyAmountCodecConfig(
            @NonNull Property<?, ?> amountReadableProperty) {
        QuantitySerialize quantitySerialize = amountReadableProperty.annotation(QuantitySerialize.class).orElse(null);
        Function<@NonNull List<String>, @NonNull String> fieldNameConverter = valueFieldNameWords -> getFieldNameJoiner()
                .apply(valueFieldNameWords,
                        Optional.ofNullable(quantitySerialize).map(QuantitySerialize::caseFormat)
                                .map(
                                        caseFormat -> (Function<@NonNull String, @NonNull List<String>>) caseFormat::split)
                                .orElseGet(this::getFieldNameSplitter)
                                .apply(amountReadableProperty.name()));
        return QuantityCodecConfig.builder()
                .configuration(Optional.ofNullable(quantitySerialize).map(QuantitySerialize::configId)
                        .map(Configuration::findExact).orElseGet(this::getConfiguration))
                .strategy(Optional.ofNullable(quantitySerialize).map(QuantitySerialize::strategy)
                        .orElseGet(this::getPropertyStrategy))
                .targetUnit(Optional.ofNullable(quantitySerialize).map(QuantitySerialize::targetUnitId)
                        .filter(targetUnitId -> !targetUnitId.isEmpty())
                        .map(Configuration::findUnitExact).orElse(null))
                .outputName(Optional.ofNullable(quantitySerialize).map(QuantitySerialize::name)
                        .filter(name -> !name.isEmpty())
                        .orElse(fieldNameConverter.apply(ImmutableList.of())))
                .outputValueName(Optional.ofNullable(quantitySerialize).map(QuantitySerialize::valueName)
                        .filter(name -> !name.isEmpty())
                        .orElse(fieldNameConverter.apply(ImmutableList.of(AMOUNT_VALUE_FIELD_NAME))))
                .outputUnitName(Optional.ofNullable(quantitySerialize).map(QuantitySerialize::unitName)
                        .filter(name -> !name.isEmpty())
                        .orElse(fieldNameConverter.apply(ImmutableList.of(AMOUNT_UNIT_FIELD_NAME))))
                .valueCodecConfig(new NumberCodecConfig(
                        Optional.ofNullable(quantitySerialize).map(QuantitySerialize::valueType)
                                .orElseGet(() -> (Class) getValueType()),
                        Optional.ofNullable(quantitySerialize)
                                .map(a -> new MathContext(a.precision(), a.roundingMode()))
                                .orElseGet(this::getMathContext)))
            .unitCodecConfig(new UnitCodecConfig(getUnitAliasType(), getConfiguration(),
                    getUnitAliasUndefinedStrategy())).build();
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public QuantityCodecConfig createPropertyAmountCodecConfig(
            @NonNull WritableProperty<?, ?> amountWritableProperty) {
        QuantityDeserialize quantityDeserialize = amountWritableProperty.annotation(QuantityDeserialize.class).orElse(null);
        Function<@NonNull List<String>, @NonNull String> fieldNameConverter = valueFieldNameWords -> getFieldNameJoiner()
                .apply(valueFieldNameWords,
                        Optional.ofNullable(quantityDeserialize).map(QuantityDeserialize::caseFormat)
                                .map(
                                        caseFormat -> (Function<@NonNull String, @NonNull List<String>>) caseFormat::split)
                                .orElseGet(this::getFieldNameSplitter)
                                .apply(amountWritableProperty.name()));
        return QuantityCodecConfig.builder()
                .configuration(Optional.ofNullable(quantityDeserialize).map(QuantityDeserialize::configId)
                        .map(Configuration::findExact).orElseGet(this::getConfiguration))
                .strategy(Optional.ofNullable(quantityDeserialize).map(QuantityDeserialize::strategy)
                        .orElseGet(this::getPropertyStrategy))
//            .targetUnit(Optional.ofNullable(amountDeserialize).map(AmountDeserialize::targetUnitId)
//                    .filter(targetUnitId->!targetUnitId.isEmpty())
//                    .map(Configuration::getUnitByIdExact).orElse(null))
                .outputName(Optional.ofNullable(quantityDeserialize).map(QuantityDeserialize::name)
                        .filter(name -> !name.isEmpty())
                        .orElse(fieldNameConverter.apply(ImmutableList.of())))
                .outputValueName(Optional.ofNullable(quantityDeserialize).map(QuantityDeserialize::valueName)
                        .filter(name -> !name.isEmpty())
                        .orElse(fieldNameConverter.apply(ImmutableList.of(AMOUNT_VALUE_FIELD_NAME))))
                .outputUnitName(Optional.ofNullable(quantityDeserialize).map(QuantityDeserialize::unitName)
                        .filter(name -> !name.isEmpty())
                        .orElse(fieldNameConverter.apply(ImmutableList.of(AMOUNT_UNIT_FIELD_NAME))))
                .valueCodecConfig(new NumberCodecConfig(
                        Optional.ofNullable(quantityDeserialize).map(QuantityDeserialize::valueType)
                                .orElseGet(() -> (Class) getValueType()),
                        Optional.ofNullable(quantityDeserialize)
                                .map(a -> new MathContext(a.precision(), a.roundingMode()))
                                .orElseGet(this::getMathContext)))
                .unitCodecConfig(new UnitCodecConfig(getUnitAliasType(), getConfiguration(),
                        getUnitAliasUndefinedStrategy())).build();
    }

    /**
     * 舍入模式set方法
     *
     * @param roundingMode 舍入模式
     * @return {@code this}
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
  public Unit4jProperties setRoundingMode(@NonNull RoundingMode roundingMode) {
    this.roundingMode = roundingMode;
    mathContext = new MathContext(precision, roundingMode);
    return this;
  }

  /**
   * 精度set方法
   *
   * @param precision 精度
   * @return {@code this}
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  public Unit4jProperties setPrecision(int precision) {
    this.precision = precision;
    mathContext = new MathContext(precision, roundingMode);
    return this;
  }

  /**
   * 数学上下文set方法
   *
   * @param mathContext 数学上下文
   * @return {@code this}
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  public Unit4jProperties setMathContext(@NonNull MathContext mathContext) {
    this.mathContext = mathContext;
    this.roundingMode = mathContext.getRoundingMode();
    this.precision = mathContext.getPrecision();
    return this;
  }
}
