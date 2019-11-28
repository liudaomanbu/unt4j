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

package org.caotc.unit4j.core.common.reflect;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.caotc.unit4j.core.exception.ReadablePropertyValueNotFoundException;

/**
 * @author caotc
 * @date 2019-11-28
 * @since 1.0.0
 */
@EqualsAndHashCode
@ToString
public abstract class AbstractAccessibleProperty<O, P> extends
    AbstractProperty<O, P> implements AccessibleProperty<O, P> {

  protected AbstractAccessibleProperty(@NonNull String propertyName,
      @NonNull TypeToken<? extends P> propertyType) {
    super(propertyName, propertyType);
  }

  protected AbstractAccessibleProperty(
      @NonNull ImmutableSet<? extends PropertyElement<O, P>> properties) {
    super(properties);
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
  public @NonNull <P1 extends P> AbstractAccessibleProperty<O, P1> propertyType(
      @NonNull Class<P1> propertyType) {
    return (AbstractAccessibleProperty<O, P1>) super.propertyType(propertyType);
  }

  @Override
  public @NonNull <P1 extends P> AbstractAccessibleProperty<O, P1> propertyType(
      @NonNull TypeToken<P1> propertyType) {
    return (AbstractAccessibleProperty<O, P1>) super.propertyType(propertyType);
  }
}
