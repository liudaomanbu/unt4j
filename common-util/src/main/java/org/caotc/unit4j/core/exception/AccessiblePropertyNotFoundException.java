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

import com.google.common.reflect.TypeToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.caotc.unit4j.core.common.reflect.property.AccessibleProperty;

import java.lang.reflect.Type;

/**
 * {@link AccessibleProperty}不存在异常
 *
 * @author caotc
 * @date 2019-11-01
 * @see AccessibleProperty
 * @since 1.0.0
 */
@Getter
@EqualsAndHashCode
public class AccessiblePropertyNotFoundException extends PropertyNotFoundException {

    private AccessiblePropertyNotFoundException(@NonNull TypeToken<?> typeToken,
                                                @NonNull String propertyName) {
        super(typeToken, propertyName);
    }

    @NonNull
    public static AccessiblePropertyNotFoundException create(@NonNull Type type,
                                                             @NonNull String propertyName) {
        return new AccessiblePropertyNotFoundException(TypeToken.of(type), propertyName);
    }

    @NonNull
    public static AccessiblePropertyNotFoundException create(@NonNull Class<?> clazz,
                                                             @NonNull String propertyName) {
        return new AccessiblePropertyNotFoundException(TypeToken.of(clazz), propertyName);
    }

    /**
     * 工厂方法
     *
     * @param propertyName 属性名称
     * @param typeToken    类
     * @return {@link AccessiblePropertyNotFoundException}
     * @author caotc
     * @date 2019-05-25
     * @since 1.0.0
     */
    @NonNull
    public static AccessiblePropertyNotFoundException create(@NonNull TypeToken<?> typeToken,
                                                             @NonNull String propertyName) {
        return new AccessiblePropertyNotFoundException(typeToken, propertyName);
    }

    @Override
    protected @NonNull String messageInternal() {
        return String
                .format("%s not found the AccessibleProperty named %s", typeToken(), propertyName());
    }
}