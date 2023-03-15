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
import com.google.common.reflect.TypeToken;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyWriter;

/**
 * @param <O> owner type
 * @param <P> property type
 * @param <T> transfer type
 * @author caotc
 * @date 2019-05-27
 * @see WritableProperty
 * @see PropertyWriter
 * @since 1.0.0
 */
@Value
public class CompositeWritableProperty<O, P, T> extends BaseCompositeProperty<O, P, T> implements
        WritableProperty<O, P> {

    @NonNull
    WritableProperty<T, P> delegate;

    CompositeWritableProperty(
            @NonNull ReadableProperty<O, T> targetReadableProperty,
            @NonNull WritableProperty<? extends T, P> delegate) {
        super(targetReadableProperty, delegate);
        this.delegate = delegate.ownerType(targetReadableProperty.type());
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
    @SuppressWarnings("unchecked")
    public @NonNull <R1 extends P> CompositeWritableProperty<O, R1, T> type(
            @NonNull TypeToken<R1> propertyType) {
        Preconditions.checkArgument(propertyType.isSupertypeOf(type())
                , "PropertySetter is known propertyType %s,not %s ", type(), propertyType);
        return (CompositeWritableProperty<O, R1, T>) this;
    }

    @Override
    public @NonNull <O1> WritableProperty<O1, P> ownerType(@NonNull TypeToken<O1> ownerType) {
        return new CompositeWritableProperty<>(transferProperty().ownerType(ownerType), delegate());
    }

    @Override
    public boolean readable() {
        return false;
    }
}
