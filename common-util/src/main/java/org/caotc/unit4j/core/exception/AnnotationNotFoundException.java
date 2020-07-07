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
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;

/**
 * {@link Annotation}不存在异常
 *
 * @author caotc
 * @date 2019-11-01
 * @see Annotation
 * @since 1.0.0
 */
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "create")
public class AnnotationNotFoundException extends IllegalArgumentException {

    /**
     * 类
     */
    @NonNull
    TypeToken<? extends Annotation> annotationType;
    /**
     * 查找的可注解元素
     */
    @NonNull
    Object annotatedElement;

    @NonNull
    @Getter(lazy = true)
    String message = String.format("%s not found the Annotation type is %s", annotatedElement(),
            annotationType());

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance (which may be {@code
     * null}).
     */
    @Override
    public String getMessage() {
        return message();
    }

}