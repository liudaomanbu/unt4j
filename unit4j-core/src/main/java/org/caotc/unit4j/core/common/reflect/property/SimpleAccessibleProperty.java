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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.google.common.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyElement;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyWriter;
import org.caotc.unit4j.core.exception.ReadablePropertyValueNotFoundException;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2019-11-28
 * @since 1.0.0
 */
@Value
public class SimpleAccessibleProperty<O, P> extends AbstractSimpleProperty<O, P, PropertyElement<O, P>> implements AccessibleProperty<O, P> {

  /**
   * 权限级别元素排序器,{@link AccessLevel#PUBLIC}最前+内存地址比较器
   */
  private static final Ordering<PropertyElement<?, ?>> ORDERING = Ordering.natural()
          .<PropertyElement<?, ?>>onResultOf(PropertyElement::accessLevel)
          .compound(Ordering.arbitrary());

  @NonNull
  ImmutableSortedSet<PropertyReader<O, P>> propertyReaders;
  @NonNull
  ImmutableSortedSet<PropertyWriter<O, P>> propertyWriters;

  protected SimpleAccessibleProperty(
      @NonNull Iterable<PropertyElement<O, P>> propertyReaders) {
    this(ImmutableSet.copyOf(propertyReaders));
  }

  protected SimpleAccessibleProperty(
      @NonNull Iterator<PropertyElement<O, P>> propertyReaders) {
    this(ImmutableSet.copyOf(propertyReaders));
  }

  protected SimpleAccessibleProperty(
      @NonNull Stream<PropertyElement<O, P>> propertyReaders) {
    this(propertyReaders
        .collect(ImmutableSet.toImmutableSet()));
  }

  protected SimpleAccessibleProperty(
      @NonNull ImmutableSet<PropertyElement<O, P>> propertyElements) {
    super(propertyElements);
    this.propertyReaders = propertyElements.stream().filter(PropertyElement::isReader)
            .map(PropertyElement::toReader).collect(ImmutableSortedSet.toImmutableSortedSet(ORDERING));
    this.propertyWriters = propertyElements.stream().filter(PropertyElement::isWriter)
            .map(PropertyElement::toWriter).collect(ImmutableSortedSet.toImmutableSortedSet(ORDERING));
    Preconditions
            .checkArgument(!propertyReaders.isEmpty(), "propertyElements do not have PropertyReader");
    Preconditions
            .checkArgument(!propertyWriters.isEmpty(), "propertyElements do not have PropertyWriter");
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
  public @NonNull AccessibleProperty<O, P> write(@NonNull O target, @NonNull P value) {
    propertyWriters.first().write(target, value);
    return this;
  }

  @Override
  public @NonNull Optional<P> read(@NonNull O target) {
    return propertyReaders.stream().map(propertyGetter -> propertyGetter.read(target))
            .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  @NonNull
  @Override
  public final P readExact(@NonNull O target) {
    return read(target)
            .orElseThrow(() -> ReadablePropertyValueNotFoundException.create(this, target));
  }

  @NonNull
  @Override
  public final <S> CompositeReadableProperty<O, S, P> compose(
          ReadableProperty<P, S> readableProperty) {
    return CompositeReadableProperty.create(this, readableProperty);
  }

  @Override
  public final @NonNull <S> CompositeWritableProperty<O, S, P> compose(
          WritableProperty<P, S> writableProperty) {
    return CompositeWritableProperty.create(this, writableProperty);
  }

  @Override
  public @NonNull <S> CompositeAccessibleProperty<O, S, P> compose(
          AccessibleProperty<P, S> accessibleProperty) {
    return new CompositeAccessibleProperty<>(this, accessibleProperty);
  }

  @Override
  @NonNull
  public <P1 extends P> SimpleAccessibleProperty<O, P1> type(
          @NonNull Class<P1> newType) {
    return type(TypeToken.of(newType));
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull <R1 extends P> SimpleAccessibleProperty<O, R1> type(
          @NonNull TypeToken<R1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(type())
            , "PropertySetter is known propertyType %s,not %s ", type(), propertyType);
    return (SimpleAccessibleProperty<O, R1>) this;
  }

}
