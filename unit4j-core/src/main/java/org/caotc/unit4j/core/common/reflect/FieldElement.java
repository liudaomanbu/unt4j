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
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import lombok.NonNull;
import lombok.SneakyThrows;

/**
 * @author caotc
 * @date 2019-11-29
 * @since 1.0.0
 */
public class FieldElement<O, P> extends Element {

  /**
   * @author caotc
   * @date 2019-12-08
   * @implNote
   * @implSpec
   * @apiNote
   * @since 1.0.0
   */
  @NonNull
  public static FieldElement from(@NonNull Field field) {
    return new FieldElement<>(field);
  }

  Field field;

  private FieldElement(
      @NonNull Field field) {
    super(field);
    this.field = field;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public final TypeToken<? extends P> type() {
    return (TypeToken<? extends P>) TypeToken.of(genericReturnType());
  }

  @NonNull
  public final <P1 extends P> FieldElement<O, P1> type(Class<P1> returnType) {
    return type(TypeToken.of(returnType));
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public final <P1 extends P> FieldElement<O, P1> type(TypeToken<P1> returnType) {
    if (!returnType.isSupertypeOf(type())) {
      throw new IllegalArgumentException(
          "FieldElement is known to return " + type() + ", not " + returnType);
    }
    return (FieldElement<O, P1>) this;
  }

  @SuppressWarnings("unchecked")
  @Override
  @NonNull
  public final Class<? super O> getDeclaringClass() {
    return (Class<? super O>) super.getDeclaringClass();
  }

  @SuppressWarnings("unchecked")
  @Override
  @NonNull
  public TypeToken<O> ownerType() {
    return (TypeToken<O>) TypeToken.of(getDeclaringClass());
  }

  @NonNull
  public AnnotatedType annotatedType() {
    return field.getAnnotatedType();
  }

  @NonNull
  Type genericReturnType() {
    return field.getGenericType();
  }

  @SneakyThrows
  @NonNull
  public FieldElement<O, P> set(O obj, P value) {
    field.set(obj, value);
    return this;
  }

  @SuppressWarnings("unchecked")
  @SneakyThrows
  public P get(O obj) {
    return (P) field.get(obj);
  }
}
