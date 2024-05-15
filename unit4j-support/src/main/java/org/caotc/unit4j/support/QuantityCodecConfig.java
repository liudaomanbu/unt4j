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

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import org.caotc.unit4j.api.annotation.QuantityCodecStrategy;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.common.base.CaseFormat;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * {@link Quantity}对象序列化和反序列化配置
 *
 * @author caotc
 * @date 2019-04-21
 * @since 1.0.0
 */
@Value
@Builder(toBuilder = true)
@With
public class QuantityCodecConfig {
    /**
     * 序列化和反序列化策略
     */
    @NonNull
    QuantityCodecStrategy strategy;
    /**
     * 配置
     */
    @NonNull
    Configuration configuration;
    CaseFormat nameCaseFormat;
    List<String> outputUnitNameWords;
    List<String> outputValueNameWords;
    /**
     * 数值的序列化和反序列化配置
     */
    @NonNull
    NumberCodecConfig valueCodecConfig;
    /**
     * 单位的序列化和反序列化配置
     */
    UnitCodecConfig unitCodecConfig;

    @Builder
    private QuantityCodecConfig(@NonNull QuantityCodecStrategy strategy, @NonNull Configuration configuration, @NonNull CaseFormat nameCaseFormat, List<String> outputUnitNameWords, List<String> outputValueNameWords, @NonNull NumberCodecConfig valueCodecConfig, UnitCodecConfig unitCodecConfig) {
        this.strategy = strategy;
        this.configuration = configuration;
        this.nameCaseFormat = nameCaseFormat;
        this.outputUnitNameWords = outputUnitNameWords;
        this.outputValueNameWords = outputValueNameWords;
        this.valueCodecConfig = valueCodecConfig;
        this.unitCodecConfig = unitCodecConfig;
        validate();
    }

    private void validate() {
        if (QuantityCodecStrategy.AS_VALUE != strategy()) {
            if (Objects.isNull(nameCaseFormat())) {
                throw new IllegalArgumentException(String.format("strategy is %s,nameCaseFormat can't be null", strategy()));
            }
            if (CollectionUtils.isEmpty(outputUnitNameWords())) {
                throw new IllegalArgumentException(String.format("strategy is %s,outputValueNameWords can't be empty", strategy()));
            }
            if (CollectionUtils.isEmpty(outputValueNameWords())) {
                throw new IllegalArgumentException(String.format("strategy is %s,outputValueNameWords can't be empty", strategy()));
            }
            if (Objects.isNull(unitCodecConfig())) {
                throw new IllegalArgumentException(String.format("strategy is %s,unitCodecConfig can't be null", strategy()));
            }
        }
    }
}
