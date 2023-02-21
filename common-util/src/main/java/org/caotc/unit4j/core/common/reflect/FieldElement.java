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

package org.caotc.unit4j.core.common.reflect;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * @author caotc
 * @date 2019-11-29
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
public class FieldElement extends BaseElement {

    Field field;

    private FieldElement(
            @NonNull Field field) {
        super(field);
        this.field = field;
    }

    /**
     * @author caotc
     * @date 2019-12-08
     * @implNote
     * @implSpec
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static FieldElement of(@NonNull Field field) {
        return new FieldElement(field);
    }

    @NonNull
    public AnnotatedType annotatedType() {
        return field.getAnnotatedType();
    }

    @NonNull
    public Type genericType() {
        return field.getGenericType();
    }

    @SneakyThrows
    @NonNull
    public FieldElement set(Object obj, Object value) {
        field.set(obj, value);
        return this;
    }

    @SneakyThrows
    public Object get(Object obj) {
        return field.get(obj);
    }

    @Override
    public boolean accessible() {
        return field.isAccessible();
    }

    @Override
    public @NonNull FieldElement accessible(boolean accessible) {
        field.setAccessible(accessible);
        return this;
    }

    @Override
    public String toString() {
        return field.toString();
    }
}
