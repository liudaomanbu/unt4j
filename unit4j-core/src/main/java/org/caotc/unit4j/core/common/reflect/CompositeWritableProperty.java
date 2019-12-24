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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.util.Optional;
import lombok.NonNull;
import lombok.Value;

/**
 * 可写属性
 *
 * @param <T> 拥有该属性的类
 * @param <R> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @see WritableProperty
 * @see PropertyWriter
 * @since 1.0.0
 */
@Value
public class CompositeWritableProperty<T, R, E> extends AbstractWritableProperty<T, R> implements
    WritableProperty<T, R> {

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
  protected static <T, R, E> CompositeWritableProperty<T, R, E> create(
      @NonNull ReadableProperty<T, E> targetReadableProperty,
      @NonNull WritableProperty<E, R> delegate) {
    return new CompositeWritableProperty<T, R, E>(targetReadableProperty, delegate);
  }

  @NonNull
  ReadableProperty<T, E> targetReadableProperty;
  @NonNull
  WritableProperty<E, R> delegate;

  private CompositeWritableProperty(
      @NonNull ReadableProperty<T, E> targetReadableProperty,
      @NonNull WritableProperty<E, R> delegate) {
    super(targetReadableProperty, delegate);
    this.targetReadableProperty = targetReadableProperty;
    this.delegate = delegate;
  }

  @Override
  public @NonNull CompositeWritableProperty<T, R, E> write(@NonNull T target, @NonNull R value) {
    targetReadableProperty.read(target)
        .ifPresent(actualTarget -> delegate.write(actualTarget, value));
    return this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull <R1 extends R> CompositeWritableProperty<T, R1, E> type(
      @NonNull TypeToken<R1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(type())
        , "PropertySetter is known propertyType %s,not %s ", type(), propertyType);
    return (CompositeWritableProperty<T, R1, E>) this;
  }

  @Override
  public @NonNull <X extends Annotation> Optional<X> annotation(
      @NonNull Class<X> annotationClass) {
    return delegate.annotation(annotationClass);
  }

  @Override
  public @NonNull <X extends Annotation> ImmutableList<X> annotations(
      @NonNull Class<X> annotationClass) {
    return delegate.annotations(annotationClass);
  }

  @Override
  public @NonNull ImmutableList<Annotation> annotations() {
    return delegate.annotations();
  }

  @Override
  @NonNull
  public ImmutableList<Annotation> declaredAnnotations() {
    return delegate.declaredAnnotations();
  }
}
