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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.google.common.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyElement;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyWriter;

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
public class SimpleWritableProperty<T, R> extends AbstractWritableProperty<T, R> implements
    WritableProperty<T, R> {

  /**
   * 权限级别元素排序器,{@link AccessLevel#PUBLIC}最前+内存地址比较器
   */
  private static final Ordering<PropertyElement<?, ?>> ORDERING = Ordering.natural()
      .<PropertyElement<?, ?>>onResultOf(PropertyElement::accessLevel)
      .compound(Ordering.arbitrary());

  @NonNull
  ImmutableSortedSet<PropertyWriter<T, R>> propertyWriters;

  protected SimpleWritableProperty(
      @NonNull Iterable<PropertyWriter<T, R>> propertyWriters) {
    this(ImmutableSortedSet.copyOf(ORDERING, propertyWriters));
  }

  protected SimpleWritableProperty(
      @NonNull Iterator<PropertyWriter<T, R>> propertyWriters) {
    this(ImmutableSortedSet.copyOf(ORDERING, propertyWriters));
  }

  protected SimpleWritableProperty(
      @NonNull Stream<PropertyWriter<T, R>> propertyWriters) {
    this(propertyWriters
        .collect(ImmutableSortedSet.toImmutableSortedSet(ORDERING)));
  }

  protected SimpleWritableProperty(
      @NonNull ImmutableSortedSet<PropertyWriter<T, R>> propertyWriters) {
    super(propertyWriters);
    this.propertyWriters = propertyWriters;
  }

  @Override
  public @NonNull SimpleWritableProperty<T, R> write(@NonNull T target, @NonNull R value) {
    propertyWriters.first().write(target, value);
    return this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull <R1 extends R> SimpleWritableProperty<T, R1> type(
      @NonNull TypeToken<R1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(type())
        , "PropertySetter is known propertyType %s,not %s ", type(), propertyType);
    return (SimpleWritableProperty<T, R1>) this;
  }

  @Override
  public @NonNull <X extends Annotation> Optional<X> annotation(
      @NonNull Class<X> annotationClass) {
    return propertyWriters.stream()
        .map(propertyWriter -> propertyWriter.annotation(annotationClass))
        .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  @Override
  public @NonNull <X extends Annotation> ImmutableList<X> annotations(
      @NonNull Class<X> annotationClass) {
    return propertyWriters.stream()
        .map(propertyWriter -> propertyWriter.annotations(annotationClass))
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  @Override
  public @NonNull ImmutableList<Annotation> annotations() {
    return propertyWriters.stream().map(PropertyWriter::annotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  @Override
  @NonNull
  public ImmutableList<Annotation> declaredAnnotations() {
    return propertyWriters.stream().map(PropertyWriter::declaredAnnotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }
}
