package org.caotc.unit4j.core.util;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * {@link Field},{@link Method},{@link Constructor}的父类
 *
 * @param <T> 拥有该属性的类
 * @author caotc
 * @date 2019-06-20
 * @since 1.0.0
 */
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class Element<T> extends AccessibleObject implements Member {

  @NonNull
  AccessibleObject accessibleObject;
  @NonNull
  Member member;

  <M extends AccessibleObject & Member> Element(@NonNull M member) {
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
  public String name() {
    return getName();
  }

  /**
   * 拥有该属性的类
   *
   * @return 拥有该属性的类
   * @author caotc
   * @date 2019-06-27
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public TypeToken<T> ownerType() {
    return TypeToken.of((Class<T>) getDeclaringClass());
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
  public final Element<T> accessible(boolean accessible) {
    setAccessible(accessible);
    return this;
  }
}
