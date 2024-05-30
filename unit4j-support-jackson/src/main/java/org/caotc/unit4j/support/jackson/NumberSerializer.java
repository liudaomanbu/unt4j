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

package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.math.number.Number;
import org.caotc.unit4j.support.NumberCodecConfig;

import java.io.IOException;

/**
 * {@link Quantity#value()}在jackson的序列化器
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
@Value
public class NumberSerializer extends StdSerializer<Number> {

    @NonNull
    public static NumberSerializer of(@NonNull NumberCodecConfig codecConfig) {
        return new NumberSerializer(codecConfig);
    }

    /**
     * {@link Quantity#value()}的序列化反序列化配置
     */
    @NonNull
    NumberCodecConfig codecConfig;

    public NumberSerializer(@NonNull NumberCodecConfig codecConfig) {
        super(Number.class);
        this.codecConfig = codecConfig;
    }

  @Override
  public void serialize(Number value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
      gen.writeObject(value.value(codecConfig().valueType(), codecConfig().mathContext()));
  }
}
