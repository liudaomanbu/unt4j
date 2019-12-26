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

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.util.Optional;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;

/**
 * 可读取属性
 *
 * @param <T> 拥有该属性的类
 * @param <R> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @see PropertyReader
 * @since 1.0.0
 */
@Value
public class CompositeAccessibleProperty<T, R, E> extends
    AbstractAccessibleProperty<T, R> implements
    AccessibleProperty<T, R> {

  @NonNull
  ReadableProperty<T, E> targetReadableProperty;
  @NonNull
  AccessibleProperty<E, R> delegate;

  protected CompositeAccessibleProperty(
      @NonNull ReadableProperty<T, E> targetReadableProperty,
      @NonNull AccessibleProperty<E, R> delegate) {
    super(targetReadableProperty, delegate);
    this.targetReadableProperty = targetReadableProperty;
    this.delegate = delegate;
  }

  @Override
  public @NonNull Optional<R> read(@NonNull T target) {
    return targetReadableProperty.read(target).flatMap(delegate::read);
  }

  @Override
  public @NonNull AccessibleProperty<T, R> write(@NonNull T target, @NonNull R value) {
    targetReadableProperty.read(target)
        .ifPresent(actualTarget -> delegate.write(actualTarget, value));
    return this;
  }

  @Override
  public @NonNull <R1 extends R> CompositeAccessibleProperty<T, R1, E> type(
      @NonNull TypeToken<R1> propertyType) {
    return (CompositeAccessibleProperty<T, R1, E>) super.type(propertyType);
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

  @NonNull
  @Override
  public ImmutableList<Annotation> declaredAnnotations() {
    return delegate.declaredAnnotations();
  }
}
