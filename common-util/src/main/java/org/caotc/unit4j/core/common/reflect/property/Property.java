/*
 * Copyright (C) 2021 the original author or authors.
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
import com.google.common.collect.Streams;
import com.google.common.reflect.TypeToken;
import lombok.NonNull;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyElement;
import org.caotc.unit4j.core.exception.AnnotationNotFoundException;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 属性
 * //todo ownerType转换
 *
 * @param <O> owner type
 * @param <P> property type
 * @author caotc
 * @date 2019-11-22
 * @see ReadableProperty
 * @see WritableProperty
 * @since 1.0.0
 */
public interface Property<O, P> {

  @NonNull
  static <T, R> Property<T, R> create(
          @NonNull Iterable<PropertyElement<T, R>> propertyElements) {
    boolean hasReader = Streams.stream(propertyElements).anyMatch(PropertyElement::isReader);
    if (!hasReader) {
      return WritableProperty
              .create(Streams.stream(propertyElements).map(PropertyElement::toWriter));
    }
    boolean hasWriter = Streams.stream(propertyElements).anyMatch(PropertyElement::isWriter);
    if (!hasWriter) {
      return ReadableProperty
              .create(Streams.stream(propertyElements).map(PropertyElement::toReader));
    }
    return new SimpleAccessibleProperty<>(Streams.stream(propertyElements).filter(PropertyElement::isReader).map(PropertyElement::toReader)
            , Streams.stream(propertyElements).filter(PropertyElement::isWriter).map(PropertyElement::toWriter));
  }


  @NonNull
  static <T, R> Property<T, R> create(
          @NonNull Iterator<PropertyElement<T, R>> propertyElements) {
    return create(Streams.stream(propertyElements));
  }


  @NonNull
  static <T, R> Property<T, R> create(
          @NonNull Stream<PropertyElement<T, R>> propertyElements) {
    return create(propertyElements.collect(ImmutableSet.toImmutableSet()));
  }

  @NonNull
  String name();

  /**
   * 获取属性类型
   *
   * @return 属性类型
   * @author caotc
   * @date 2019-11-22
   * @since 1.0.0
   */
  @NonNull
  TypeToken<P> type();

  @NonNull
  TypeToken<O> ownerType();

  /**
   * 设置属性类型
   *
   * @param propertyType 属性类型
   * @return this
   * @author caotc
   * @date 2019-06-25
   * @since 1.0.0
   */
  @NonNull
  default <P1 extends P> Property<O, P1> type(@NonNull Class<P1> propertyType) {
    return type(TypeToken.of(propertyType));
  }

  /**
   * 设置属性类型
   *
   * @param propertyType 属性类型
   * @return this
   * @author caotc
   * @date 2019-11-22
   * @since 1.0.0
   */
  @NonNull
  default <P1 extends P> Property<O, P1> type(
          @NonNull TypeToken<P1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(type())
            , "Property is known type %s,not %s ", type(), propertyType);
    //noinspection unchecked
    return (Property<O, P1>) this;
  }

  boolean readable();

  boolean writable();

  default boolean accessible() {
    return readable() && writable();
  }

  default ReadableProperty<O, P> toReadable() {
    Preconditions.checkArgument(readable(), "it is not a ReadableProperty");
    return (ReadableProperty<O, P>) this;
  }

  default WritableProperty<O, P> toWritable() {
    Preconditions.checkArgument(writable(), "it is not a WritableProperty");
    return (WritableProperty<O, P>) this;
  }

  default AccessibleProperty<O, P> toAccessible() {
    Preconditions.checkArgument(accessible(), "it is not a AccessibleProperty");
    return (AccessibleProperty<O, P>) this;
  }

  /**
   * 获取注解
   *
   * @param annotationClass 注解类
   * @return 注解的 {@link Optional}
   * @author caotc
   * @date 2019-11-22
   * @since 1.0.0
   */
  @NonNull <X extends Annotation> Optional<X> annotation(
          @NonNull Class<X> annotationClass);

  @NonNull
  default <X extends Annotation> X annotationExact(
          @NonNull Class<X> annotationClass) {
    return annotation(annotationClass).orElseThrow(() -> AnnotationNotFoundException.create(TypeToken.of(annotationClass), this));
  }

  /**
   * 获取注解集合
   *
   * @param annotationClass 注解类
   * @return 该类注解集合
   * @author caotc
   * @date 2019-11-22
   * @since 1.0.0
   */
  @NonNull <X extends Annotation> ImmutableList<X> annotations(
          @NonNull Class<X> annotationClass);

}
