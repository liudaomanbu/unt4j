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
public class CompositeReadableProperty<O, P, T> extends BaseCompositeProperty<O, P, T> implements
        ReadableProperty<O, P> {

    @NonNull
    ReadableProperty<T, P> delegate;

    CompositeReadableProperty(
            @NonNull ReadableProperty<O, T> targetReadableProperty,
            @NonNull ReadableProperty<? extends T, P> delegate) {
        super(targetReadableProperty, delegate);
        this.delegate = delegate.ownerType(targetReadableProperty.type());
    }

    @Override
    public @NonNull Optional<P> read(@NonNull O target) {
        return transferProperty().read(target).flatMap(delegate().toReadable()::read);
    }

    @Override
    public @NonNull <O1> ReadableProperty<O1, P> ownerType(@NonNull TypeToken<O1> ownerType) {
        return new CompositeReadableProperty<>(transferProperty().ownerType(ownerType), delegate());
    }

    @Override
    public boolean writable() {
        return false;
    }
}
