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

package org.caotc.unit4j.core.common.reflect;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString.Exclude;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.*;

/**
 * @author caotc
 * @date 2019-06-20
 * @see Field
 * @see Method
 * @see Constructor
 * @since 1.0.0
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public abstract class BaseElement implements Element {

  @NonNull
  @Exclude
  AnnotatedElement annotatedElement;
  @NonNull
  Member member;

  protected <M extends AnnotatedElement & Member> BaseElement(@NonNull M member) {
    this(member, member);
  }

  @Override
  public final boolean isAnnotationPresent(@NonNull Class<? extends Annotation> annotationClass) {
    return annotatedElement.isAnnotationPresent(annotationClass);
  }

  @Override
  public final <A extends Annotation> A getAnnotation(@NonNull Class<A> annotationClass) {
    return annotatedElement.getAnnotation(annotationClass);
  }

  @Override
  public final Annotation[] getAnnotations() {
    return annotatedElement.getAnnotations();
  }

  @Override
  public final Annotation[] getDeclaredAnnotations() {
    return annotatedElement.getDeclaredAnnotations();
  }

  @Override
  public Class<?> getDeclaringClass() {
    return member.getDeclaringClass();
  }

  @Override
  public final String getName() {
    return member.getName();
  }

  @Override
  public final int getModifiers() {
    return member.getModifiers();
  }

  @Override
  public final boolean isSynthetic() {
    return member.isSynthetic();
  }

  @Override
  public String toString() {
    return member.toString();
  }
}
