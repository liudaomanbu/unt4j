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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Optional;
import lombok.NonNull;
import lombok.Value;

/**
 * @author caotc
 * @date 2019-11-28
 * @since 1.0.0
 */
@Value
public class SimpleAccessibleProperty<O, P> extends AbstractAccessibleProperty<O, P> {

  @NonNull
  ImmutableSortedSet<PropertyAccessor<O, P>> propertyAccessors;

  private SimpleAccessibleProperty(
      @NonNull ImmutableSortedSet<PropertyAccessor<O, P>> propertyAccessors) {
    super(propertyAccessors);
    this.propertyAccessors = propertyAccessors;
  }

  @Override
  public @NonNull AccessibleProperty<O, P> write(@NonNull O target, @NonNull P value) {
    propertyAccessors.first().write(target, value);
    return this;
  }

  @Override
  public @NonNull Optional<P> read(@NonNull O target) {
    return propertyAccessors.stream().map(propertyGetter -> propertyGetter.read(target))
        .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  @Override
  public @NonNull <X extends Annotation> Optional<X> annotation(@NonNull Class<X> annotationClass) {
    return propertyAccessors.stream()
        .map(fieldWrapper -> fieldWrapper.annotation(annotationClass))
        .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  @Override
  public @NonNull ImmutableList<Annotation> annotations() {
    return propertyAccessors.stream().map(PropertyReader::annotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  @Override
  public @NonNull <X extends Annotation> ImmutableList<X> annotations(
      @NonNull Class<X> annotationClass) {
    return propertyAccessors.stream()
        .map(propertyGetter -> propertyGetter.annotations(annotationClass))
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  @Override
  public @NonNull ImmutableList<Annotation> declaredAnnotations() {
    return propertyAccessors.stream().map(PropertyReader::declaredAnnotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }
}
