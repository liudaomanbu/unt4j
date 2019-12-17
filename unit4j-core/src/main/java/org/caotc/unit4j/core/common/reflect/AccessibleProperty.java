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

import com.google.common.reflect.TypeToken;
import java.util.Iterator;
import java.util.stream.Stream;
import lombok.NonNull;

/**
 * @param <O> 拥有该属性的类
 * @param <P> 属性类型
 * @author caotc
 * @date 2019-11-22
 * @see ReadableProperty
 * @see WritableProperty
 * @since 1.0.0
 */
public interface AccessibleProperty<O, P> extends ReadableProperty<O, P>,
    WritableProperty<O, P> {

  @NonNull
  static <T, R> SimpleAccessibleProperty<T, R> create(
      @NonNull Iterable<PropertyElement<T, R>> propertyReaders) {
    return new SimpleAccessibleProperty<>(propertyReaders);
  }

  @NonNull
  static <T, R> SimpleAccessibleProperty<T, R> create(
      @NonNull Iterator<PropertyElement<T, R>> propertyReaders) {
    return new SimpleAccessibleProperty<>(propertyReaders);
  }

  @NonNull
  static <T, R> SimpleAccessibleProperty<T, R> create(
      @NonNull Stream<PropertyElement<T, R>> propertyReaders) {
    return new SimpleAccessibleProperty<>(propertyReaders);
  }

  @Override
  @NonNull AccessibleProperty<O, P> write(@NonNull O target, @NonNull P value);

  @Override
  @NonNull <P1 extends P> AccessibleProperty<O, P1> type(@NonNull Class<P1> propertyType);

  @Override
  @NonNull <R1 extends P> AccessibleProperty<O, R1> type(
      @NonNull TypeToken<R1> propertyType);
}
