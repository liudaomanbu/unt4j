/*
 * Copyright (C) 2021 the original author or authors.
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
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.caotc.unit4j.api.annotation.WithUnit;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.common.reflect.property.AccessibleProperty;
import org.caotc.unit4j.core.common.reflect.property.Property;
import org.caotc.unit4j.core.common.reflect.property.ReadableProperty;
import org.caotc.unit4j.core.common.reflect.property.WritableProperty;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.support.common.util.QuantityUtil;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * @author caotc
 * @date 2020-07-02
 * @since 1.0.0
 */
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public abstract class BaseAmountProperty<O, P, D extends Property<O, P>> implements Property<O, Quantity> {
    //    @Delegate(types=Property.class)
    @NonNull
    protected D delegate;
    @NonNull
    @Getter(lazy = true)
    WithUnit withUnit = delegate.annotationExact(WithUnit.class);

    @Override
    @NonNull
    public String name() {
        return delegate.name();
    }

    @Override
    @NonNull
    public TypeToken<Quantity> type() {
        return TypeToken.of(Quantity.class);
    }

    @Override
    public @NonNull TypeToken<O> ownerType() {
        return delegate.ownerType();
    }

    @Override
    @NonNull
    public <P1 extends Quantity> Property<O, P1> type(@NonNull Class<P1> propertyType) {
        return type(TypeToken.of(propertyType));
    }

    @Override
    @NonNull
    public <P1 extends Quantity> Property<O, P1> type(@NonNull TypeToken<P1> propertyType) {
        Preconditions.checkArgument(propertyType.isSupertypeOf(type())
                , "Property is known type %s,not %s ", type(), propertyType);
        //noinspection unchecked
        return (Property<O, P1>) this;
    }

    @Override
    public boolean readable() {
        return delegate.readable();
    }

    @Override
    public boolean writable() {
        return delegate.writable();
    }

    @Override
    public boolean accessible() {
        return delegate.accessible();
    }

    @Override
    public ReadableProperty<O, Quantity> toReadable() {
        return new ReadableAmountProperty<>(delegate.toReadable());
    }

    @Override
    public WritableProperty<O, Quantity> toWritable() {
        return new WritableAmountProperty<>(delegate.toWritable());
    }

    @Override
    public AccessibleProperty<O, Quantity> toAccessible() {
        return (AccessibleProperty<O, Quantity>) delegate.toAccessible();
    }

    @Override
    @NonNull
    public <X extends Annotation> Optional<X> annotation(@NonNull Class<X> annotationClass) {
        return delegate.annotation(annotationClass);
    }

    @Override
    @NonNull
    public <X extends Annotation> ImmutableList<X> annotations(@NonNull Class<X> annotationClass) {
        return delegate.annotations(annotationClass);
    }

    @NonNull
    protected Unit unit() {
        return QuantityUtil.readUnit(withUnit());
    }
}
