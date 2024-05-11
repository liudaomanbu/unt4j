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
import org.caotc.unit4j.api.annotation.UnitCodecStrategy;
import org.caotc.unit4j.core.serializer.AliasSerializer;
import org.caotc.unit4j.core.unit.Unit;

import java.util.Objects;

/**
 * {@link Unit}对象序列化和反序列化配置
 *
 * @author caotc
 * @date 2019-05-17
 * @since 1.0.0
 */
@Value
@Builder(toBuilder = true)
@With
public class UnitCodecConfig {
    /**
     * 序列化和反序列化策略
     */
    @NonNull
    UnitCodecStrategy strategy;
    AliasSerializer<Unit> aliasSerializer;

    @Builder
    private UnitCodecConfig(@NonNull UnitCodecStrategy strategy, AliasSerializer<Unit> aliasSerializer) {
        this.strategy = strategy;
        this.aliasSerializer = aliasSerializer;
        validate();
    }

    private void validate() {
        if (UnitCodecStrategy.AS_ALIAS == strategy() && Objects.isNull(aliasSerializer())) {
            throw new IllegalArgumentException(String.format("strategy is %s,aliasSerializer can't be null", UnitCodecStrategy.AS_ALIAS));
        }
    }
}
