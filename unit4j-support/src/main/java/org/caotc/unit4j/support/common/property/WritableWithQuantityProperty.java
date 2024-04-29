/*
 * Copyright (C) 2023 the original author or authors.
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
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.common.reflect.property.WritableProperty;

import java.math.MathContext;

/**
 * @author caotc
 * @date 2020-07-02
 * @since 1.0.0
 */
@Value
public class WritableWithQuantityProperty<O, P> extends BaseWithQuantityProperty<O, P, WritableProperty<O, P>> implements WritableProperty<O, Quantity> {
    //todo
    @NonNull
    Configuration configuration = Configuration.defaultInstance();
    @NonNull
    MathContext mathContext = configuration.mathContext();

    public WritableWithQuantityProperty(@NonNull WritableProperty<O, P> delegate) {
        super(delegate);
    }

    @Override
    public @NonNull O write(@NonNull O target, @NonNull Quantity value) {
        Quantity result = configuration().convert(value, unit());
        return delegate().write(target, result.value().value(delegate().type(), mathContext()));
    }

    @Override
    @NonNull
    public <P1 extends Quantity> WritableProperty<O, P1> type(@NonNull Class<P1> propertyType) {
        return type(TypeToken.of(propertyType));
    }

    @Override
    @NonNull
    public <P1 extends Quantity> WritableProperty<O, P1> type(@NonNull TypeToken<P1> propertyType) {
        Preconditions.checkArgument(propertyType.isSupertypeOf(type())
                , "Property is known type %s,not %s ", type(), propertyType);
        //noinspection unchecked
        return (WritableProperty<O, P1>) this;
    }

    @Override
    public @NonNull <O1> WritableProperty<O1, Quantity> ownerType(@NonNull TypeToken<O1> ownerType) {
        return new WritableWithQuantityProperty<>(delegate().ownerType(ownerType));
    }
}
