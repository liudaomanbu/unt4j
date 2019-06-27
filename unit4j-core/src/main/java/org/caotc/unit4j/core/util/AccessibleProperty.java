package org.caotc.unit4j.core.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;


/**
 * 可使用属性,可由{@link Method}或者{@link Field}的包装实现,可以以统一的方式使用
 *
 * @param <T> 拥有该属性的类
 * @param <R> 属性类型
 * @author caotc
 * @date 2019-06-20
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public abstract class AccessibleProperty<T, R> extends AccessibleObject implements Member {

  @NonNull
  AccessibleObject accessibleObject;
  @NonNull
  Member member;

  protected <M extends AccessibleObject & Member> AccessibleProperty(@NonNull M member) {
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
   * 属性名称
   *
   * @return 属性名称
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  public abstract String propertyName();

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
    return (TypeToken<T>) TypeToken.of(getDeclaringClass());
  }

  /**
   * 属性类型
   *
   * @return 属性类型
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  public abstract TypeToken<? extends R> propertyType();

  /**
   * 修改返回类型
   *
   * @param propertyType 新的返回类型
   * @return 修改返回类型的可使用属性
   * @author caotc
   * @date 2019-06-25
   * @since 1.0.0
   */
  @NonNull
  public <R1 extends R> AccessibleProperty<T, R1> propertyType(@NonNull Class<R1> propertyType) {
    return propertyType(TypeToken.of(propertyType));
  }

  /**
   * 修改返回类型
   *
   * @param propertyType 新的返回类型
   * @return 修改返回类型的可使用属性
   * @author caotc
   * @date 2019-06-25
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public <R1 extends R> AccessibleProperty<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(propertyType())
        , "AccessibleProperty is known propertyType %s,not %s ", propertyType(), propertyType);
    return (AccessibleProperty<T, R1>) this;
  }

  /**
   * 获取该可使用属性的注解对象
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
  public AccessibleProperty<T, R> accessible(boolean accessible) {
    setAccessible(accessible);
    return this;
  }
}
