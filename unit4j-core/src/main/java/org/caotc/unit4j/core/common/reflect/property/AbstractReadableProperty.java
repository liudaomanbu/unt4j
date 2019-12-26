/*
 * Copyright (C) 2019 the original author or authors.
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

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;
import org.caotc.unit4j.core.exception.ReadablePropertyValueNotFoundException;

/**
 * @author caotc
 * @date 2019-11-27
 * @since 1.0.0
 */
@EqualsAndHashCode
@ToString
public abstract class AbstractReadableProperty<O, P> extends
    AbstractProperty<O, P> implements
    ReadableProperty<O, P> {

  protected <T> AbstractReadableProperty(@NonNull ReadableProperty<O, T> targetReadableProperty,
      @NonNull ReadableProperty<T, P> delegate) {
    super(targetReadableProperty, delegate);
  }

  protected AbstractReadableProperty(
      @NonNull ImmutableSet<PropertyReader<O, P>> properties) {
    super(properties);
  }

  @Override
  public final boolean readable() {
    return true;
  }

  @Override
  public final boolean writable() {
    return false;
  }

  @Override
  public final boolean accessible() {
    return false;
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
  public @NonNull <R1 extends P> AbstractReadableProperty<O, R1> type(
      @NonNull Class<R1> newType) {
    return (AbstractReadableProperty<O, R1>) super.type(newType);
  }

  @Override
  public @NonNull <R1 extends P> AbstractReadableProperty<O, R1> type(
      @NonNull TypeToken<R1> propertyType) {
    return (AbstractReadableProperty<O, R1>) super.type(propertyType);
  }
}
