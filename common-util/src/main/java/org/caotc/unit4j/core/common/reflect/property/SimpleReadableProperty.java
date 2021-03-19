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

import com.google.common.collect.ImmutableSortedSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyWriter;

/**
 * 可读取属性
 *
 * @param <O> 拥有该属性的类
 * @param <P> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @see PropertyReader
 * @since 1.0.0
 */
@Value
public class SimpleReadableProperty<O, P> extends AbstractSimpleProperty<O, P> implements
        ReadableProperty<O, P> {

    protected SimpleReadableProperty(
        @NonNull Iterable<PropertyReader<? super O, P>> propertyElements) {
        super(propertyElements, ImmutableSortedSet.of());
    }

    protected SimpleReadableProperty(
        @NonNull Iterator<PropertyReader<? super O, P>> propertyReaders) {
        super(propertyReaders, ImmutableSortedSet.<PropertyWriter<? super O, P>>of().iterator());
    }

    protected SimpleReadableProperty(
        @NonNull Stream<PropertyReader<? super O, P>> propertyReaders) {
        super(propertyReaders, Stream.empty());
    }

    protected SimpleReadableProperty(
        @NonNull ImmutableSortedSet<PropertyReader<? super O, P>> propertyReaders) {
        super(propertyReaders, ImmutableSortedSet.of());
    }

    @Override
    public @NonNull Optional<P> read(@NonNull O target) {
        return propertyReaders.stream()
                .map(propertyGetter -> propertyGetter.read(target))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    @Override
    public boolean writable() {
        return false;
    }
}
