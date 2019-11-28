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

/**
 * @author caotc
 * @date 2019-11-27
 * @since 1.0.0
 */
@EqualsAndHashCode
@ToString
public abstract class AbstractWritableProperty<T, R> extends AbstractProperty<T, R> implements
    WritableProperty<T, R> {

  protected AbstractWritableProperty(@NonNull String propertyName,
      @NonNull TypeToken<? extends R> propertyType) {
    super(propertyName, propertyType);
  }

  protected AbstractWritableProperty(
      @NonNull ImmutableSet<PropertyWriter<T, R>> properties) {
    super(properties);
  }

  @Override
  public @NonNull <R1 extends R> AbstractWritableProperty<T, R1> propertyType(
      @NonNull Class<R1> propertyType) {
    return (AbstractWritableProperty<T, R1>) super.propertyType(propertyType);
  }

  @Override
  public @NonNull <R1 extends R> AbstractWritableProperty<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    return (AbstractWritableProperty<T, R1>) super.propertyType(propertyType);
  }
}
