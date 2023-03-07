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

import com.google.common.reflect.TypeToken;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;

import java.util.Optional;

/**
 * @param <O> owner type
 * @param <P> property type
 * @param <T> transfer type
 * @author caotc
 * @date 2019-05-27
 * @see PropertyReader
 * @since 1.0.0
 */
@Value
public class CompositeAccessibleProperty<O, P, T> extends
        AbstractCompositeProperty<O, P, T> implements
        AccessibleProperty<O, P> {
    @NonNull
    AccessibleProperty<T, P> delegate;


    CompositeAccessibleProperty(
            @NonNull ReadableProperty<O, T> targetReadableProperty,
            @NonNull AccessibleProperty<? extends T, P> delegate) {
        super(targetReadableProperty, delegate);
        this.delegate = delegate.ownerType(targetReadableProperty.type());
    }

    @NonNull
    static <O, P, T> CompositeAccessibleProperty<O, P, T> create(
            @NonNull ReadableProperty<O, T> targetReadableProperty,
            @NonNull AccessibleProperty<? extends T, P> delegate) {
        return new CompositeAccessibleProperty<>(targetReadableProperty, delegate);
    }

    @Override
    public @NonNull Optional<P> read(@NonNull O target) {
        return transferProperty().read(target).flatMap(delegate().toAccessible()::read);
    }

    @Override
    public @NonNull O write(@NonNull O target, @NonNull P value) {
        return transferProperty().read(target)
                .map(actualTarget -> delegate().toAccessible().write(actualTarget, value))
                .filter(writeResult -> transferProperty().writable())
                .map(writeResult -> transferProperty().toWritable().write(target, writeResult))
                .orElse(target);
    }

    @Override
    public @NonNull <O1> AccessibleProperty<O1, P> ownerType(@NonNull TypeToken<O1> ownerType) {
        return new CompositeAccessibleProperty<>(transferProperty().ownerType(ownerType), delegate());
    }

}
