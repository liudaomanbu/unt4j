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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyElement;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyWriter;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2019-11-28
 * @since 1.0.0
 */
@Value
public class SimpleAccessibleProperty<O, P> extends AbstractAccessibleProperty<O, P> {

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
  public @NonNull AccessibleProperty<O, P> write(@NonNull O target, @NonNull P value) {
    propertyWriters.first().write(target, value);
    return this;
  }

  @Override
  public @NonNull Optional<P> read(@NonNull O target) {
    return propertyReaders.stream().map(propertyGetter -> propertyGetter.read(target))
        .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  @Override
  public @NonNull <X extends Annotation> Optional<X> annotation(@NonNull Class<X> annotationClass) {
    return propertyReaders.stream()
            .map(propertyElement -> AnnotatedElementUtils.findMergedAnnotation(propertyElement, annotationClass))
            .filter(Objects::nonNull)
            .findFirst();
  }

  @Override
  public @NonNull <X extends Annotation> ImmutableList<X> annotations(
      @NonNull Class<X> annotationClass) {
    return propertyReaders.stream()
            .map(propertyGetter -> AnnotatedElementUtils.findAllMergedAnnotations(propertyGetter, annotationClass))
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

}
