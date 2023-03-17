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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import lombok.NonNull;
import lombok.Value;

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
@Value
public class CompositeProperty<O, P, T> implements AccessibleProperty<O, P> {

    @NonNull
    String name;
    @NonNull
    ReadableProperty<O, T> transferProperty;
    @NonNull
    Property<T, P> delegate;

    CompositeProperty(@NonNull ReadableProperty<O, T> transferProperty,
                      @NonNull Property<? extends T, P> delegate) {
        this.name = transferProperty.name() + "." + delegate.name();
        this.transferProperty = transferProperty;
        this.delegate = delegate.ownerType(transferProperty.type());
    }

    @NonNull
    @Override
    public TypeToken<P> type() {
        return delegate().type();
    }

    @Override
    public @NonNull TypeToken<O> ownerType() {
        return transferProperty().ownerType();
    }

    @Override
    public boolean checkOwnerType(@NonNull TypeToken<?> newOwnerType) {
        return transferProperty.checkOwnerType(newOwnerType);
    }

    @Override
    public @NonNull <O1> AccessibleProperty<O1, P> ownerType(@NonNull TypeToken<O1> ownerType) {
        Preconditions.checkArgument(checkOwnerType(ownerType), "%s can not owner by %s", ownerType);
        return new CompositeProperty<>(transferProperty().ownerType(ownerType), delegate());
    }

    @Override
    public boolean readable() {
        return delegate().readable();
    }

    @Override
    public boolean writable() {
        return delegate().writable();
    }

    @Override
    public @NonNull Optional<P> read(@NonNull O target) {
        return transferProperty().read(target).flatMap(delegate().toReadable()::read);
    }

    @Override
    public @NonNull O write(@NonNull O target, @NonNull P value) {
        return transferProperty().read(target)
                .map(actualTarget -> delegate().toWritable().write(actualTarget, value))
                .filter(writeResult -> transferProperty().writable())
                .map(writeResult -> transferProperty().toWritable().write(target, writeResult))
                .orElse(target);
    }

    @Override
    public @NonNull <X extends Annotation> Optional<X> annotation(
            @NonNull Class<X> annotationClass) {
        return delegate().annotation(annotationClass);
    }

    @Override
    public @NonNull <X extends Annotation> ImmutableList<X> annotations(
            @NonNull Class<X> annotationClass) {
        return delegate().annotations(annotationClass);
    }
}
