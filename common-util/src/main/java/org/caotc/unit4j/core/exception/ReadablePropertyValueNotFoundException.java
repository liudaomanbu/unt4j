/*
 * Copyright (C) 2020 the original author or authors.
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

package org.caotc.unit4j.core.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.caotc.unit4j.core.common.reflect.property.ReadableProperty;

/**
 * {@link ReadableProperty}不存在异常
 *
 * @author caotc
 * @date 2019-11-01
 * @see ReadableProperty
 * @since 1.0.0
 */
@RequiredArgsConstructor(staticName = "create")
@Getter
@EqualsAndHashCode
public class ReadablePropertyValueNotFoundException extends IllegalStateException {

    @NonNull
    ReadableProperty<?, ?> readableProperty;
    @NonNull
    Object target;
    @Getter(lazy = true)
    String message = String
            .format("property %s for %s value not found", readableProperty.name(), target);

    @Override
    public String getMessage() {
        return message();
    }
}