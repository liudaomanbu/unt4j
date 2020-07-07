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

package org.caotc.unit4j.core.common.reflect;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.caotc.unit4j.core.exception.NeverHappenException;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;

/**
 * @author caotc
 * @date 2019-06-20
 * @see Field
 * @see Method
 * @see Constructor
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Element extends AccessibleObject implements Member {

  @NonNull
  AccessibleObject accessibleObject;
  @NonNull
  Member member;

  protected <M extends AccessibleObject & Member> Element(@NonNull M member) {
    this(member, member);
  }

  @Override
  public final boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return accessibleObject.isAnnotationPresent(annotationClass);
  }

  @Override
  public final <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
    return accessibleObject.getAnnotation(annotationClass);
  }

  @Override
  public final Annotation[] getAnnotations() {
    return accessibleObject.getAnnotations();
  }

  @Override
  public final Annotation[] getDeclaredAnnotations() {
    return accessibleObject.getDeclaredAnnotations();
  }

  @Override
  public final void setAccessible(boolean flag) throws SecurityException {
    accessibleObject.setAccessible(flag);
  }

  @Override
  public final boolean isAccessible() {
    return accessibleObject.isAccessible();
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

  /**
   * Returns true if the element is public.
   */
  public final boolean isPublic() {
    return Modifier.isPublic(getModifiers());
  }

  /**
   * Returns true if the element is protected.
   */
  public final boolean isProtected() {
    return Modifier.isProtected(getModifiers());
  }

  /**
   * Returns true if the element is package-private.
   */
  public final boolean isPackagePrivate() {
    return !isPrivate() && !isPublic() && !isProtected();
  }

  /**
   * Returns true if the element is private.
   */
  public final boolean isPrivate() {
    return Modifier.isPrivate(getModifiers());
  }

  /**
   * Returns true if the element is static.
   */
  public final boolean isStatic() {
    return Modifier.isStatic(getModifiers());
  }

  /**
   * Returns {@code true} if this method is final, per {@code Modifier.isFinal(getModifiers())}.
   *
   * <p>Note that a method may still be effectively "final", or non-overridable when it has no
   * {@code final} keyword. For example, it could be private, or it could be declared by a final
   * class. To tell whether a method is overridable, use {@link Invokable#isOverridable}.
   */
  public final boolean isFinal() {
    return Modifier.isFinal(getModifiers());
  }

  /**
   * Returns true if the method is abstract.
   */
  public final boolean isAbstract() {
    return Modifier.isAbstract(getModifiers());
  }

  /**
   * Returns true if the element is native.
   */
  public final boolean isNative() {
    return Modifier.isNative(getModifiers());
  }

  /**
   * Returns true if the method is synchronized.
   */
  public final boolean isSynchronized() {
    return Modifier.isSynchronized(getModifiers());
  }

  /**
   * Returns true if the field is volatile.
   */
  public final boolean isVolatile() {
    return Modifier.isVolatile(getModifiers());
  }

  /**
   * Returns true if the field is transient.
   */
  public final boolean isTransient() {
    return Modifier.isTransient(getModifiers());
  }

  /**
   * 名称
   *
   * @return 名称
   * @author caotc
   * @date 2019-07-13
   * @since 1.0.0
   */
  @NonNull
  public final String name() {
    return getName();
  }

  @NonNull
  @Override
  public Class<?> getDeclaringClass() {
    return member.getDeclaringClass();
  }

  /**
   * 拥有该属性的类
   *
   * @return 拥有该属性的类
   * @author caotc
   * @date 2019-06-27
   * @since 1.0.0
   */
  @NonNull
  public TypeToken<?> ownerType() {
    return TypeToken.of(getDeclaringClass());
  }

  /**
   * 获取该元素的注解对象
   *
   * @param annotationClass 注解类型
   * @return 注解对象
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  public final <X extends Annotation> Optional<X> annotation(@NonNull Class<X> annotationClass) {
    return Optional.ofNullable(getAnnotation(annotationClass));
  }

  /**
   * 获取该可使用属性的注解对象集合
   *
   * @param annotationClass 注解类型
   * @return 属性包装器的注解对象集合
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public final <X extends Annotation> ImmutableList<X> annotations(
      @NonNull Class<X> annotationClass) {
    return ImmutableList.copyOf(getAnnotationsByType(annotationClass));
  }

  /**
   * 注解对象集合
   *
   * @return 注解对象集合
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public final ImmutableList<Annotation> declaredAnnotations() {
    return ImmutableList.copyOf(getDeclaredAnnotations());
  }

  /**
   * 注解对象集合
   *
   * @return 注解对象集合
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public final ImmutableList<Annotation> annotations() {
    return ImmutableList.copyOf(getAnnotations());
  }

  public final boolean accessible() {
    return isAccessible();
  }

  /**
   * 设置访问权限
   *
   * @param accessible 是否可访问
   * @return {@code this}
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public final Element accessible(boolean accessible) {
    setAccessible(accessible);
    return this;
  }

  /**
   * 该元素的权限级别
   *
   * @return 权限级别
   * @author caotc
   * @date 2019-07-14
   * @since 1.0.0
   */
  @NonNull
  public final AccessLevel accessLevel() {
    if (isPrivate()) {
      return AccessLevel.PRIVATE;
    }
    if (isPackagePrivate()) {
      return AccessLevel.PACKAGE;
    }
    if (isProtected()) {
      return AccessLevel.PROTECTED;
    }
    if (isPublic()) {
      return AccessLevel.PUBLIC;
    }
    throw NeverHappenException.instance();
  }

  @Override
  public final <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
    return super.getAnnotationsByType(annotationClass);
  }

  @Override
  public final <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
    return super.getDeclaredAnnotation(annotationClass);
  }

  @Override
  public final <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass) {
    return super.getDeclaredAnnotationsByType(annotationClass);
  }
}