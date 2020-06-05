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

package org.caotc.unit4j.core.common.reflect.property;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * @author caotc
 * @date 2019-11-28
 * @since 1.0.0
 */
@EqualsAndHashCode
@ToString
public abstract class AbstractCompositeProperty<O, P, T, D extends Property<T, P>> implements Property<O, P> {

    @NonNull
    String name;
    @NonNull
    TypeToken<? extends P> type;
    boolean fieldExist;
    @NonNull
    protected ReadableProperty<O, T> targetReadableProperty;
    @NonNull
    protected D delegate;

    protected AbstractCompositeProperty(@NonNull ReadableProperty<O, T> targetReadableProperty,
                                        @NonNull D delegate) {
        this.name = targetReadableProperty.name() + "." + delegate.name();
        this.type = delegate.type();
        this.fieldExist = delegate.fieldExist();
        this.targetReadableProperty = targetReadableProperty;
        this.delegate = delegate;
    }

    @NonNull
    @Override
    public final String name() {
        return name;
    }

    @NonNull
    @Override
    public final TypeToken<? extends P> type() {
        return type;
    }

    @Override
    public final boolean fieldExist() {
        return fieldExist;
    }

    @Override
    public final @NonNull <X extends Annotation> Optional<X> annotation(
            @NonNull Class<X> annotationClass) {
        return delegate.annotation(annotationClass);
    }

    @Override
    public final @NonNull <X extends Annotation> ImmutableList<X> annotations(
            @NonNull Class<X> annotationClass) {
        return delegate.annotations(annotationClass);
    }
}
