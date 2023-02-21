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
public class CompositeWritableProperty<O, P, T> extends AbstractCompositeProperty<O, P, T> implements
        WritableProperty<O, P> {

  /**
   * static constructor
   *
   * @param targetReadableProperty targetReadableProperty
   * @param delegate delegate
   * @return {@link CompositeWritableProperty}
   * @author caotc
   * @date 2019-11-27
   * @since 1.0.0
   */
  @NonNull
  static <O, P, T> CompositeWritableProperty<O, P, T> create(
          @NonNull ReadableProperty<O, T> targetReadableProperty,
          @NonNull WritableProperty<T, P> delegate) {
    return new CompositeWritableProperty<>(targetReadableProperty, delegate);
  }

  @NonNull
  WritableProperty<T, P> delegate;

  private CompositeWritableProperty(
          @NonNull ReadableProperty<O, T> targetReadableProperty,
          @NonNull WritableProperty<T, P> delegate) {
    super(targetReadableProperty, delegate);
    this.delegate = delegate;
  }

  @Override
  public @NonNull CompositeWritableProperty<O, P, T> write(@NonNull O target, @NonNull P value) {
    transferProperty().read(target)
            .ifPresent(actualTarget -> delegate().toWritable().write(actualTarget, value));
    return this;
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
  public @NonNull <O1> WritableProperty<O1, P> ownBy(@NonNull TypeToken<O1> ownerType) {
    return new CompositeWritableProperty<>(transferProperty().ownBy(ownerType), delegate());
  }

  @Override
  public boolean readable() {
    return false;
  }
}
