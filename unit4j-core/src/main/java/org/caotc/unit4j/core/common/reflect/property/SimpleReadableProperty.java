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
public class SimpleReadableProperty<T, R> extends AbstractReadableProperty<T, R> implements
    ReadableProperty<T, R> {

  /**
   * 权限级别元素排序器,{@link AccessLevel#PUBLIC}最前+内存地址比较器
   */
  private static final Ordering<PropertyElement<?, ?>> ORDERING = Ordering.natural()
      .<PropertyElement<?, ?>>onResultOf(PropertyElement::accessLevel)
      .compound(Ordering.arbitrary());

  @NonNull
  ImmutableSortedSet<PropertyReader<T, R>> propertyReaders;

  protected SimpleReadableProperty(
      @NonNull Iterable<PropertyReader<T, R>> propertyReaders) {
    this(ImmutableSortedSet.copyOf(ORDERING, propertyReaders));
  }

  protected SimpleReadableProperty(
      @NonNull Iterator<PropertyReader<T, R>> propertyReaders) {
    this(ImmutableSortedSet.copyOf(ORDERING, propertyReaders));
  }

  protected SimpleReadableProperty(
      @NonNull Stream<PropertyReader<T, R>> propertyReaders) {
    this(propertyReaders
        .collect(ImmutableSortedSet.toImmutableSortedSet(ORDERING)));
  }

  private SimpleReadableProperty(
      @NonNull ImmutableSortedSet<PropertyReader<T, R>> propertyReaders) {
    super(propertyReaders);
    this.propertyReaders = propertyReaders;
  }

  @Override
  public @NonNull Optional<R> read(@NonNull T target) {
    return propertyReaders.stream().map(propertyGetter -> propertyGetter.read(target))
        .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  @Override
  public @NonNull <R1 extends R> SimpleReadableProperty<T, R1> type(
      @NonNull TypeToken<R1> propertyType) {
    return (SimpleReadableProperty<T, R1>) super.type(propertyType);
  }

  @Override
  public @NonNull <X extends Annotation> Optional<X> annotation(
      @NonNull Class<X> annotationClass) {
    return propertyReaders.stream()
        .map(fieldWrapper -> fieldWrapper.annotation(annotationClass))
        .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  @Override
  public @NonNull <X extends Annotation> ImmutableList<X> annotations(
      @NonNull Class<X> annotationClass) {
    return propertyReaders.stream()
        .map(propertyGetter -> propertyGetter.annotations(annotationClass))
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  @Override
  public @NonNull ImmutableList<Annotation> annotations() {
    return propertyReaders.stream().map(PropertyReader::annotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  @NonNull
  @Override
  public ImmutableList<Annotation> declaredAnnotations() {
    return propertyReaders.stream().map(PropertyReader::declaredAnnotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }
}
