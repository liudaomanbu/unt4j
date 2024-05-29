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

package org.caotc.unit4j.support.fastjson;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerialContext;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.QuantityCodecStrategy;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.common.base.CaseFormat;
import org.caotc.unit4j.support.QuantityCodecConfig;
import org.caotc.unit4j.support.Unit4jProperties;
import org.caotc.unit4j.support.common.util.QuantityUtil;
import org.caotc.unit4j.support.fastjson.util.CodecUtil;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 单独{@link Quantity}对象在fastjson中的序列化器
 * //TODO 考虑Spring环境时配置刷新问题
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
@Value(staticConstructor = "of")
@Slf4j
public class QuantitySerializer implements ObjectSerializer {
    //todo 确认到底Serializer应该依赖Unit4jProperties还是Unit4jProperties产生的CodecConfig
    @NonNull
    Unit4jProperties unit4jProperties;
    /**
     * 序列化反序列化配置
     */
    @NonNull
    @Getter(lazy = true)
    QuantityCodecConfig codecConfig = unit4jProperties().createQuantityCodecConfig();
    @NonNull
    @Getter(lazy = true)
    QuantityCodecConfig propertyCodecConfig = unit4jProperties().createQuantityCodecConfig().withStrategy(unit4jProperties().getDefaultPropertyStrategy());

    private QuantitySerializer(@NonNull Unit4jProperties unit4jProperties) {
        this.unit4jProperties = unit4jProperties;
        validate();
    }

    private void validate() {
        if (codecConfig().strategy() == QuantityCodecStrategy.FLAT) {
            throw new IllegalArgumentException(String.format("strategy %s only support property", QuantityCodecStrategy.FLAT));
        }
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) {
        log.debug("object:{},fieldName:{},fieldType:{},features:{}", object, fieldName, fieldType, features);

        Quantity quantity = (Quantity) object;
        QuantityCodecConfig codecConfig = codecConfig();
        SerialContext context = serializer.getContext();
        SerializeWriter writer = serializer.getWriter();

        //有context时,从逻辑上说一定有属性名称.但是fastjson在某些情况下比如bean使用了NameFilter时会不传递fieldName
        final String propertyName = Objects.nonNull(context) && Objects.isNull(fieldName) ? getPropertyName(writer) : (String) fieldName;
        CaseFormat caseFormat = null;
        //is property
        if (Objects.nonNull(context)) {
            codecConfig = QuantityUtil.readableQuantityProperty(context.object, propertyName)
                    .map(unit4jProperties::createPropertyQuantityCodecConfig)
                    .orElseGet(this::propertyCodecConfig);


            if (!(context.object instanceof Map)) {
                caseFormat = Optional.ofNullable(context.object.getClass())
                        .map(clazz -> clazz.getAnnotation(JSONType.class))
                        .map(JSONType::naming)
                        .or(() -> Optional.ofNullable(serializer.getMapping().propertyNamingStrategy))
                        .map(CodecUtil::mapping)
                        .orElse(null);
            }
        }
        if (Objects.isNull(caseFormat)) {
            caseFormat = Optional.ofNullable(propertyName)
                    .flatMap(name -> Arrays.stream(CaseFormat.values())
                            .filter(format -> format.matches(name))
                            .findFirst())
                    .orElse(CaseFormat.LOWER_CAMEL);
        }

        //todo convert to targetUnit

        NumberSerializer valueSerializer = NumberSerializer.of(codecConfig.valueCodecConfig());

        switch (codecConfig.strategy()) {
            case FLAT:
                List<String> propertyNameWords = caseFormat.split(propertyName);

                /*
                  fastjson中想要实现直接不输出该属性名,只能使用Filter机制.但是却没提供全局的Filter机制,必须按序列化目标Class注册.
                  所以在FLAT策略时只能输出null值和逗号来代替不输出属性名.
                 */
                serializer.writeNull();
                writer.write(",");
                List<String> unitPropertyNameWords = ImmutableList.<String>builder().addAll(propertyNameWords).addAll(codecConfig.outputUnitNameWords()).build();
                String unitPropertyName = caseFormat.join(unitPropertyNameWords);
                writer.writeFieldName(unitPropertyName);
                UnitSerializer.of(codecConfig.unitCodecConfig()).write(serializer, quantity.unit(), unitPropertyName, quantity.unit().getClass(), features);
                writer.write(",");
                List<String> valuePropertyNameWords = ImmutableList.<String>builder().addAll(propertyNameWords).addAll(codecConfig.outputValueNameWords()).build();
                String valuePropertyName = caseFormat.join(valuePropertyNameWords);
                writer.writeFieldName(valuePropertyName);
                valueSerializer.write(serializer, quantity.value(), valuePropertyName, quantity.value().getClass(), features);
                break;
            case OBJECT:
                writer.write("{");
                unitPropertyName = caseFormat.join(codecConfig.outputUnitNameWords());
                writer.writeFieldName(unitPropertyName);
                UnitSerializer.of(codecConfig.unitCodecConfig()).write(serializer, quantity.unit(), unitPropertyName, quantity.unit().getClass(), features);
                writer.write(",");
                valuePropertyName = caseFormat.join(codecConfig.outputValueNameWords());
                writer.writeFieldName(valuePropertyName);
                valueSerializer.write(serializer, quantity.value(), valuePropertyName, quantity.value().getClass(), features);
                writer.write("}");
                break;
            case AS_VALUE:
            default:
                valueSerializer.write(serializer, quantity.value(), null, null, features);
        }
    }

    private String getPropertyName(@NonNull SerializeWriter writer) {
        //"[propertyName]":
        char[] chars = writer.toCharArray();
        //[propertyNameStartIndex,propertyNameEndIndex)
        int propertyNameEndIndex = chars.length - 2;
        int propertyNameStartIndex = 0;
        for (int i = propertyNameEndIndex - 1; i >= 0; i--) {
            //属性名称符号可能是双引号或者单引号,以结尾符号对应匹配
            if (chars[i] == chars[propertyNameEndIndex]) {
                propertyNameStartIndex = i + 1;
                break;
            }
        }
        return String.valueOf(chars, propertyNameStartIndex, propertyNameEndIndex - propertyNameStartIndex);
    }
}