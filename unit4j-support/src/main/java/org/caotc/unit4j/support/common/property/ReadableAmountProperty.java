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

package org.caotc.unit4j.support.common.property;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.common.reflect.property.ReadableProperty;
import org.caotc.unit4j.core.exception.ReadablePropertyValueNotFoundException;

import java.util.Optional;

/**
 * @author caotc
 * @date 2020-07-02
 * @since 1.0.0
 */
@Value
public class ReadableAmountProperty<O, P> extends BaseAmountProperty<O, P, ReadableProperty<O, P>> implements ReadableProperty<O, Amount> {
    public ReadableAmountProperty(@NonNull ReadableProperty<O, P> delegate) {
        super(delegate);
    }

    @NonNull
    public Optional<Amount> read(@NonNull O target) {
        return delegate.read(target).map(value -> Amount.create(value, unit()));
    }

    @NonNull
    public Amount readExact(@NonNull O target) {
        return read(target)
                .orElseThrow(() -> ReadablePropertyValueNotFoundException.create(this, target));
    }

    @NonNull
    public <P1 extends Amount> ReadableProperty<O, P1> type(@NonNull Class<P1> propertyType) {
        return type(TypeToken.of(propertyType));
    }

    @NonNull
    public <P1 extends Amount> ReadableProperty<O, P1> type(@NonNull TypeToken<P1> propertyType) {
        Preconditions.checkArgument(propertyType.isSupertypeOf(type())
                , "Property is known type %s,not %s ", type(), propertyType);
        //noinspection unchecked
        return (ReadableProperty<O, P1>) this;
    }
}
