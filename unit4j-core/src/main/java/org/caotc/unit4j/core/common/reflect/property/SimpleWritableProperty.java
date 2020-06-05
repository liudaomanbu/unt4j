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
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.reflect.TypeToken;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyWriter;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * 可写属性
 *
 * @param <O> 拥有该属性的类
 * @param <P> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @see WritableProperty
 * @see PropertyWriter
 * @since 1.0.0
 */
@Value
public class SimpleWritableProperty<O, P> extends AbstractSimpleProperty<O, P> implements
        WritableProperty<O, P> {

    protected SimpleWritableProperty(
            @NonNull Iterable<PropertyWriter<O, P>> propertyElements) {
        super(ImmutableSortedSet.of(), propertyElements);
    }

    protected SimpleWritableProperty(
            @NonNull Iterator<PropertyWriter<O, P>> propertyElements) {
        super(ImmutableSortedSet.<PropertyReader<O, P>>of().iterator(), propertyElements);
  }

  protected SimpleWritableProperty(
          @NonNull Stream<PropertyWriter<O, P>> propertyElements) {
      super(Stream.empty(), propertyElements);
  }

  protected SimpleWritableProperty(
          @NonNull ImmutableSortedSet<PropertyWriter<O, P>> propertyElements) {
      super(ImmutableSortedSet.of(), propertyElements);
  }

  @Override
  public @NonNull SimpleWritableProperty<O, P> write(@NonNull O target, @NonNull P value) {
      propertyWriters.first().write(target, value);
    return this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull <R1 extends P> SimpleWritableProperty<O, R1> type(
          @NonNull TypeToken<R1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(type())
            , "PropertySetter is known propertyType %s,not %s ", type(), propertyType);
    return (SimpleWritableProperty<O, R1>) this;
  }

  @Override
  public boolean readable() {
    return false;
  }
}
