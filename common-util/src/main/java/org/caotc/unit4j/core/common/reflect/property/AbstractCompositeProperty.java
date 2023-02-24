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
import lombok.*;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * @param <O> owner type
 * @param <P> property type
 * @param <T> transfer type
 * @author caotc
 * @date 2019-11-28
 * @since 1.0.0
 */
@Getter(AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public abstract class AbstractCompositeProperty<O, P, T> implements Property<O, P> {

    @NonNull
    String name;
    @NonNull
    ReadableProperty<O, T> transferProperty;

    protected AbstractCompositeProperty(@NonNull ReadableProperty<O, T> transferProperty,
                                        @NonNull Property<T, P> delegate) {
        this.name = transferProperty.name() + "." + delegate.name();
        this.transferProperty = transferProperty;
    }

    @NonNull
    @Override
    public final String name() {
        return name;
    }

    @NonNull
    @Override
    public final TypeToken<P> type() {
        return delegate().type();
    }

    @Override
    public @NonNull TypeToken<O> ownerType() {
        return transferProperty().ownerType();
    }

    @Override
    public boolean canOwnBy(@NonNull TypeToken<?> newOwnerType) {
        return transferProperty.canOwnBy(newOwnerType);
    }

    @Override
    public final @NonNull <X extends Annotation> Optional<X> annotation(
            @NonNull Class<X> annotationClass) {
        return delegate().annotation(annotationClass);
    }

    @Override
    public final @NonNull <X extends Annotation> ImmutableList<X> annotations(
            @NonNull Class<X> annotationClass) {
        return delegate().annotations(annotationClass);
    }

    @NonNull
    protected abstract Property<T, P> delegate();
}
