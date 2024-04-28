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
import org.caotc.unit4j.core.convert.FixedUnitFinder;
import org.caotc.unit4j.core.convert.SingletonUnitFinder;
import org.caotc.unit4j.core.convert.UnitFinder;
import org.caotc.unit4j.core.serializer.AliasUndefinedStrategy;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
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
@SuppressWarnings("rawtypes")
@Data
@FieldDefaults(makeFinal = false, level = AccessLevel.PRIVATE)
@Accessors(fluent = false, chain = true)
public class Unit4jProperties {
    /**
     * 默认的编码解码策略
     */
    private static final CodecStrategy DEFAULT_STRATEGY = CodecStrategy.VALUE;
    /**
     * 作为其他类属性的{@link Quantity}对象的默认的编码解码策略
     */
    private static final CodecStrategy DEFAULT_PROPERTY_STRATEGY = CodecStrategy.VALUE;
    /**
     * 默认的单位转换配置
     */
    private static final Configuration DEFAULT_CONFIGURATION = Configuration.defaultInstance();
    private static final CaseFormat DEFAULT_NAME_CASE_FORMAT = CaseFormat.LOWER_CAMEL;
    /**
     * 默认的名称拆分器
     */
    private static final Function<@NonNull String, @NonNull List<String>> DEFAULT_NAME_SPLITTER = CaseFormat.LOWER_CAMEL::split;
    /**
     * 默认的名称拼接器
     */
    private static final Function<@NonNull List<String>, @NonNull String> DEFAULT_NAME_JOINER = CaseFormat.LOWER_CAMEL::join;
    /**
     * 默认的名称转换器
     */
    private static final Function<@NonNull String, @NonNull String> DEFAULT_NAME_CONVERTER = DEFAULT_NAME_SPLITTER
            .andThen(DEFAULT_NAME_JOINER);
    /**
     * 默认的属性名称拼接器
     */
    private static final BiFunction<@NonNull List<String>, @NonNull List<String>, @NonNull String> DEFAULT_FIELD_NAME_JOINER = (objectFieldNameWords, valueFieldNameWords) -> CaseFormat.LOWER_CAMEL
            .join(Stream.concat(objectFieldNameWords.stream(), valueFieldNameWords.stream()).collect(Collectors.toList()));
    private static final ImmutableList<String> DEFAULT_QUANTITY_UNIT_FIELD_NAME = ImmutableList.of("unit");
    private static final UnitFinder DEFAULT_TARGET_UNIT_FINDER = DEFAULT_CONFIGURATION.targetUnitFinder();
    private static final ImmutableList<String> DEFAULT_QUANTITY_VALUE_FIELD_NAME = ImmutableList.of("value");
    /**
     * 默认的值序列化时的类型
     */
    private static final Class<BigDecimal> DEFAULT_VALUE_TYPE = BigDecimal.class;
    /**
     * 默认的数学运算配置
     */
    private static final MathContext DEFAULT_VALUE_MATH_CONTEXT = MathContext.UNLIMITED;


    /**
     * 默认单位的输出别名类型
     */
    private static final Type DEFAULT_UNIT_ALIAS_TYPE = Aliases.Types.ENGLISH_NAME;
    /**
     * 默认单位别名未定义策略
     */
    private static final AliasUndefinedStrategy DEFAULT_UNIT_UNDEFINED_STRATEGY = AliasUndefinedStrategy.THROW_EXCEPTION;

    /**
     * 单独的{@link Quantity}对象的序列化反序列化策略
     */
    @NonNull
    CodecStrategy defaultStrategy = DEFAULT_STRATEGY;
    /**
     * 作为其他类属性的{@link Quantity}对象的序列化反序列化策略
     */
    @NonNull
    CodecStrategy defaultPropertyStrategy = DEFAULT_STRATEGY;
    /**
     * 配置
     */
    @NonNull
    Configuration defaultConfiguration = DEFAULT_CONFIGURATION;
    @NonNull
    CaseFormat defaultNameCaseFormat = DEFAULT_NAME_CASE_FORMAT;
    /**
     * 名称转换器
     * todo 确认序列化和反序列化时是否使用同一个,使用接口应该定义为NameConverter还是CaseFormat或其他
     */
    Function<@NonNull String, @NonNull String> defaultNameConverter = DEFAULT_NAME_CONVERTER;
    /**
     * 属性名称拼接器,{@link CodecStrategy#FLAT}时拼接使用
     */
    BiFunction<@NonNull List<String>, @NonNull List<String>, @NonNull String> defaultFieldNameJoiner = DEFAULT_FIELD_NAME_JOINER;

    ImmutableList<String> defaultQuantityUnitFieldName = DEFAULT_QUANTITY_UNIT_FIELD_NAME;
    SingletonUnitFinder targetUnitFinder = DEFAULT_TARGET_UNIT_FINDER;
    ImmutableList<String> defaultQuantityValueFieldName = DEFAULT_QUANTITY_VALUE_FIELD_NAME;
    /**
     * 数值转换类
     */
    @NonNull
    Class<?> defaultValueType = DEFAULT_VALUE_TYPE;
    /**
     * 数学运算的上下文
     */
    @NonNull
    MathContext defaultValueMathContext = DEFAULT_VALUE_MATH_CONTEXT;
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
    public QuantityCodecConfig createQuantityCodecConfig() {
        return QuantityCodecConfig.builder().configuration(getDefaultConfiguration()).strategy(getDefaultStrategy())
                .outputValueName(defaultQuantityValueFieldName)
                .outputUnitName(defaultQuantityUnitFieldName)
                .valueCodecConfig(new NumberCodecConfig(getDefaultValueType(), getDefaultValueMathContext()))
                .unitCodecConfig(new UnitCodecConfig(getUnitAliasType(), getDefaultConfiguration(),
                        getUnitAliasUndefinedStrategy())).build();
    }

    /**
     * 获取作为其他类属性的{@link Quantity}对象的序列化反序列化配置
     *
     * @param quantityReadableProperty 属性名称
     * @return 序列化反序列化配置
     * @author caotc
     * @date 2019-11-06
     * @since 1.0.0
     */
    @NonNull
    @SuppressWarnings({"unchecked", "rawtypes"})
    public QuantityCodecConfig createPropertyQuantityCodecConfig(
            @NonNull Property<?, ?> quantityReadableProperty) {
        Optional<QuantitySerialize> quantitySerialize = quantityReadableProperty.annotation(QuantitySerialize.class);
        Configuration configuration = quantitySerialize.map(QuantitySerialize::configId).map(Configuration::findExact)
                .orElseGet(this::getDefaultConfiguration);
        return QuantityCodecConfig.builder()
                .strategy(quantitySerialize.map(QuantitySerialize::strategy)
                        .orElseGet(this::getDefaultPropertyStrategy))
                .configuration(configuration)
                .nameCaseFormat(quantitySerialize.map(QuantitySerialize::nameCaseFormat)
                        .orElseGet(this::getDefaultNameCaseFormat))
                .outputUnitName(quantitySerialize.map(QuantitySerialize::unitName)
                        .filter(name -> name.length != 0)
                        .map(Arrays::asList)
                        .orElseGet(this::getDefaultQuantityUnitFieldName))
                .targetUnitFinder(quantitySerialize.map(QuantitySerialize::targetUnitId)
                        .filter(targetUnitId -> !targetUnitId.isEmpty())
                        .map(Configuration::findUnitExact)
                        .map(FixedUnitFinder::of)
                        .orElseGet(this::getTargetUnitFinder))
                .unitCodecConfig(new UnitCodecConfig(getUnitAliasType(), getDefaultConfiguration(),
                        getUnitAliasUndefinedStrategy()))
                .outputValueName(quantitySerialize.map(QuantitySerialize::valueName)
                        .filter(name -> name.length != 0)
                        .map(Arrays::asList)
                        .orElseGet(this::getDefaultQuantityValueFieldName))
                .valueCodecConfig(new NumberCodecConfig(
                        quantitySerialize.map(QuantitySerialize::valueType).orElseGet(() -> (Class) getDefaultValueType()),
                        quantitySerialize.map(a -> new MathContext(a.valuePrecision(), a.valueRoundingMode())).
                                orElseGet(this::getDefaultValueMathContext)))
                .build();
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public QuantityCodecConfig createPropertyQuantityCodecConfig(
            @NonNull WritableProperty<?, ?> amountWritableProperty) {
        Optional<QuantityDeserialize> quantityDeserialize = amountWritableProperty.annotation(QuantityDeserialize.class);
        return QuantityCodecConfig.builder()
                .strategy(quantityDeserialize.map(QuantityDeserialize::strategy)
                        .orElseGet(this::getDefaultPropertyStrategy))
                .configuration(quantityDeserialize.map(QuantityDeserialize::configId)
                        .map(Configuration::findExact).orElseGet(this::getDefaultConfiguration))
                .nameCaseFormat(quantityDeserialize.map(QuantityDeserialize::nameCaseFormat)
                        .orElseGet(this::getDefaultNameCaseFormat))
                .outputUnitName(quantityDeserialize.map(QuantityDeserialize::unitName)
                        .filter(name -> name.length != 0)
                        .map(Arrays::asList)
                        .orElseGet(this::getDefaultQuantityUnitFieldName))
                .targetUnitFinder(quantityDeserialize.map(QuantityDeserialize::sourceUnitId)
                        .filter(targetUnitId -> !targetUnitId.isEmpty())
                        .map(Configuration::findUnitExact)
                        .map(FixedUnitFinder::of)
                        .orElseGet(this::getTargetUnitFinder))
                .unitCodecConfig(new UnitCodecConfig(getUnitAliasType(), getDefaultConfiguration(),
                        getUnitAliasUndefinedStrategy()))
                .outputValueName(quantityDeserialize.map(QuantityDeserialize::valueName)
                        .filter(name -> name.length != 0)
                        .map(Arrays::asList)
                        .orElseGet(this::getDefaultQuantityValueFieldName))
                .valueCodecConfig(new NumberCodecConfig(
                        quantityDeserialize.map(QuantityDeserialize::valueType)
                                .orElseGet(() -> (Class) getDefaultValueType()),
                        quantityDeserialize
                                .map(a -> new MathContext(a.valuePrecision(), a.valueRoundingMode()))
                                .orElseGet(this::getDefaultValueMathContext)))
                .build();
    }

    /**
     * 舍入模式set方法
     *
     * @param valueRoundingMode 舍入模式
     * @return {@code this}
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    public Unit4jProperties setValueRoundingMode(@NonNull RoundingMode valueRoundingMode) {
        setDefaultValueMathContext(new MathContext(getDefaultValueMathContext().getPrecision(), valueRoundingMode));
        return this;
    }

    /**
     * 精度set方法
     *
     * @param valuePrecision 精度
     * @return {@code this}
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    public Unit4jProperties setValuePrecision(int valuePrecision) {
        setDefaultValueMathContext(new MathContext(valuePrecision, getDefaultValueMathContext().getRoundingMode()));
        return this;
    }

    /**
     * 数学上下文set方法
     *
     * @param defaultValueMathContext 数学上下文
     * @return {@code this}
     * @author caotc
     * @date 2019-05-29
     * @since 1.0.0
     */
    public Unit4jProperties setDefaultValueMathContext(@NonNull MathContext defaultValueMathContext) {
        this.defaultValueMathContext = defaultValueMathContext;
        return this;
    }
}
