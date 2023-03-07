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

package org.caotc.unit4j.core.common.reflect.property;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.reflect.TypeToken;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyAccessor;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyWriter;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @param <O> owner type
 * @param <P> property type
 * @author caotc
 * @date 2019-11-28
 * @since 1.0.0
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class SimpleAccessibleProperty<O, P> extends AbstractSimpleProperty<O, P> implements AccessibleProperty<O, P> {

    public SimpleAccessibleProperty(@NonNull Iterable<? extends PropertyAccessor<O, P>> propertyAccessors) {
        super(propertyAccessors);
    }

    public SimpleAccessibleProperty(@NonNull Iterable<? extends PropertyReader<O, P>> propertyReaders,
                                    @NonNull Iterable<? extends PropertyWriter<O, P>> propertyWriters) {
        super(propertyReaders, propertyWriters);
    }

    public SimpleAccessibleProperty(@NonNull Iterator<PropertyReader<O, P>> propertyReaders,
                                    @NonNull Iterator<PropertyWriter<O, P>> propertyWriters) {
        super(propertyReaders, propertyWriters);
    }

    public SimpleAccessibleProperty(@NonNull Stream<PropertyReader<O, P>> propertyReaders,
                                    @NonNull Stream<PropertyWriter<O, P>> propertyWriters) {
        super(propertyReaders, propertyWriters);
    }

    public SimpleAccessibleProperty(
            @NonNull ImmutableSortedSet<PropertyReader<O, P>> propertyReaders,
            @NonNull ImmutableSortedSet<PropertyWriter<O, P>> propertyWriters) {
        super(propertyReaders, propertyWriters);
        Preconditions
                .checkArgument(!propertyReaders.isEmpty(), "propertyReaders can't be empty");
        Preconditions
                .checkArgument(!propertyWriters.isEmpty(), "propertyWriters can't be empty");
    }

    @Override
    public boolean writable() {
        return true;
    }

    @Override
    public boolean readable() {
        return true;
    }

    @Override
    public @NonNull O write(@NonNull O target, @NonNull P value) {
        return propertyWriters().first().write(target, value);
    }

    @Override
    public @NonNull <O1> AccessibleProperty<O1, P> ownerType(@NonNull TypeToken<O1> ownerType) {
        return new SimpleAccessibleProperty<>(propertyReaders().stream().map(propertyReader -> propertyReader.ownerType(ownerType))
                , propertyWriters().stream().map(propertyWriter -> propertyWriter.ownerType(ownerType)));
    }

    @Override
    public @NonNull Optional<P> read(@NonNull O target) {
        return propertyReaders().stream().map(propertyGetter -> propertyGetter.read(target))
                .filter(Optional::isPresent).map(Optional::get).findFirst();
    }

}
