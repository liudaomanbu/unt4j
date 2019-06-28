package org.caotc.unit4j.core.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.Value;

/**
 * 属性设置器,可由get{@link Method}或者{@link Field}的包装实现,可以以统一的方式使用
 *
 * @param <T> 拥有该属性的类
 * @param <R> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
public interface PropertySetter<T, R> {

  /**
   * 工厂方法
   *
   * @param propertySetters 属性设置器集合
   * @return 属性设置器
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  static <T, R> PropertySetter<T, R> create(
      @NonNull Iterable<PropertySetter<T, R>> propertySetters) {
    return new CompositePropertySetter<>(ImmutableList.copyOf(propertySetters));
  }

  /**
   * 工厂方法
   *
   * @param propertySetters 属性设置器集合
   * @return 属性设置器
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  static <T, R> PropertySetter<T, R> create(
      @NonNull Iterator<PropertySetter<T, R>> propertySetters) {
    return new CompositePropertySetter<>(ImmutableList.copyOf(propertySetters));
  }

  /**
   * 工厂方法
   *
   * @param propertySetters 属性设置器集合
   * @return 属性设置器
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  static <T, R> PropertySetter<T, R> create(
      @NonNull Stream<PropertySetter<T, R>> propertySetters) {
    return new CompositePropertySetter<>(
        propertySetters.collect(ImmutableList.toImmutableList()));
  }

  /**
   * 工厂方法
   *
   * @param setMethod set方法
   * @param methodNameStyle 方法名称风格
   * @return 属性设置器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  static <T, R> PropertySetter<T, R> create(@NonNull Method setMethod,
      @NonNull ReflectionUtil.MethodNameStyle methodNameStyle) {
    return create((Invokable<T, ?>) Invokable.from(setMethod), methodNameStyle);
  }

  /**
   * 工厂方法
   *
   * @param setInvokable set方法
   * @param methodNameStyle 方法名称风格
   * @return 属性设置器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */

  @NonNull
  static <T, R> PropertySetter<T, R> create(@NonNull Invokable<T, ?> setInvokable,
      @NonNull ReflectionUtil.MethodNameStyle methodNameStyle) {
    return new InvokablePropertySetter<>(setInvokable, methodNameStyle);
  }

  /**
   * 工厂方法
   *
   * @param field 属性
   * @return 属性设置器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */

  @NonNull
  static <T, R> PropertySetter<T, R> create(@NonNull Field field) {
    return new FieldPropertySetter<>(field);
  }

  /**
   * 给传入对象的该属性设置传入的值
   *
   * @param obj 设置属性值的对象
   * @param value 设置的属性值
   * @return {@code this}
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull PropertySetter<T, R> set(@NonNull T obj, @NonNull R value);

  /**
   * 属性名称
   *
   * @return 属性名称
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull String propertyName();

  /**
   * 属性类型
   *
   * @return 属性类型
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  TypeToken<? extends R> propertyType();

  /**
   * 修改返回类型
   *
   * @param propertyType 新的返回类型
   * @return 修改返回类型的属性设置器
   * @author caotc
   * @date 2019-06-25
   * @since 1.0.0
   */
  @NonNull
  default <R1 extends R> PropertySetter<T, R1> propertyType(@NonNull Class<R1> propertyType) {
    return propertyType(TypeToken.of(propertyType));
  }

  /**
   * 修改返回类型
   *
   * @param propertyType 新的返回类型
   * @return 修改返回类型的属性设置器
   * @author caotc
   * @date 2019-06-25
   * @since 1.0.0
   */
  @NonNull <R1 extends R> PropertySetter<T, R1> propertyType(@NonNull TypeToken<R1> propertyType);

  /**
   * 获取注解对象
   *
   * @param annotationClass 注解类型
   * @return 属性包装器的注解对象
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull <X extends Annotation> Optional<X> annotation(@NonNull Class<X> annotationClass);

  /**
   * 获取注解对象集合
   *
   * @param annotationClass 注解类型
   * @return 注解对象集合
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull <X extends Annotation> ImmutableList<X> annotations(@NonNull Class<X> annotationClass);

  /**
   * 获取注解对象集合
   *
   * @return 注解对象集合
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  ImmutableList<Annotation> annotations();

  /**
   * 注解对象集合
   *
   * @return 注解对象集合
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  ImmutableList<Annotation> declaredAnnotations();

  /**
   * 获取访问权限
   *
   * @return 获取访问权限
   * @author caotc
   * @date 2019-06-27
   * @since 1.0.0
   */
  boolean accessible();

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
  PropertySetter<T, R> accessible(boolean accessible);
}

/**
 * 组合属性设置器,由{@link FieldPropertySetter}和{@link InvokablePropertySetter}实现
 *
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@Value
class CompositePropertySetter<T, R> implements PropertySetter<T, R> {

  @NonNull
  ImmutableList<PropertySetter<T, R>> propertySetters;

  CompositePropertySetter(
      @NonNull ImmutableList<PropertySetter<T, R>> propertySetters) {
    //不能是空集合
    Preconditions.checkArgument(!propertySetters.isEmpty(), "propertySetters can't empty");
    //不能重复组合
    Preconditions
        .checkArgument(
            propertySetters.stream().noneMatch(CompositePropertySetter.class::isInstance),
            "CompositePropertySetter are not allowed in a CompositePropertySetter");
    //实际属性只能有一个
    Preconditions.checkArgument(
        propertySetters.stream().filter(FieldPropertySetter.class::isInstance).count() <= 1,
        "Multiple FieldPropertySetter are not allowed");
    this.propertySetters = propertySetters.stream().distinct()
        .collect(ImmutableList.toImmutableList());
  }

  @Override
  public @NonNull PropertySetter<T, R> set(@NonNull T obj, @NonNull R value) {
    propertySetters.get(0).set(obj, value);
    return this;
  }

  @Override
  public @NonNull TypeToken<? extends R> propertyType() {
    return propertySetters.get(0).propertyType();
  }

  @SuppressWarnings("unchecked")
  @Override
  public @NonNull <R1 extends R> PropertySetter<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(propertyType())
        , "PropertySetter is known propertyType %s,not %s ", propertyType(), propertyType);
    return (PropertySetter<T, R1>) this;
  }

  @Override
  public @NonNull <X extends Annotation> Optional<X> annotation(
      @NonNull Class<X> annotationClass) {
    return propertySetters.stream()
        .map(propertySetter -> propertySetter.annotation(annotationClass))
        .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  @Override
  public @NonNull <X extends Annotation> ImmutableList<X> annotations(
      @NonNull Class<X> annotationClass) {
    return propertySetters.stream()
        .map(propertySetter -> propertySetter.annotations(annotationClass))
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  @Override
  public @NonNull ImmutableList<Annotation> annotations() {
    return propertySetters.stream().map(PropertySetter::annotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  @Override
  public ImmutableList<Annotation> declaredAnnotations() {
    return propertySetters.stream().map(PropertySetter::declaredAnnotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  @Override
  public boolean accessible() {
    return propertySetters.stream().allMatch(PropertySetter::accessible);
  }

  @Override
  public @NonNull String propertyName() {
    return propertySetters.get(0).propertyName();
  }

  @Override
  public @NonNull PropertySetter<T, R> accessible(boolean accessible) {
    propertySetters.forEach(propertySetter -> propertySetter.accessible(accessible));
    return this;
  }
}

/**
 * set{@link Invokable}实现的属性设置器
 *
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@Value
class InvokablePropertySetter<T, R> extends AccessibleProperty<T, R> implements
    PropertySetter<T, R> {

  /**
   * set方法
   */
  @NonNull
  Invokable<T, ?> setInvokable;
  /**
   * set方法名称风格
   */
  @NonNull
  ReflectionUtil.MethodNameStyle methodNameStyle;

  InvokablePropertySetter(@NonNull Invokable<T, ?> setInvokable,
      @NonNull ReflectionUtil.MethodNameStyle methodNameStyle) {
    super(setInvokable);
    Preconditions
        .checkArgument(methodNameStyle.isSetInvokable(setInvokable), "%s is not matches %s",
            setInvokable, methodNameStyle);
    this.setInvokable = setInvokable;
    this.methodNameStyle = methodNameStyle;
  }

  @Override
  @SneakyThrows
  public @NonNull PropertySetter<T, R> set(@NonNull T obj, @NonNull R value) {
    setInvokable.invoke(obj, value);
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public @NonNull TypeToken<? extends R> propertyType() {
    return (TypeToken<? extends R>) setInvokable.getParameters().get(0).getType();
  }

  @Override
  public @NonNull String propertyName() {
    return methodNameStyle.fieldNameFromGetInvokable(setInvokable);
  }

  @Override
  public @NonNull InvokablePropertySetter<T, R> accessible(boolean accessible) {
    return (InvokablePropertySetter<T, R>) super.accessible(accessible);
  }

  @Override
  public @NonNull <R1 extends R> InvokablePropertySetter<T, R1> propertyType(
      @NonNull Class<R1> propertyType) {
    return (InvokablePropertySetter<T, R1>) super.propertyType(propertyType);
  }

  @Override
  public @NonNull <R1 extends R> InvokablePropertySetter<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    return (InvokablePropertySetter<T, R1>) super.propertyType(propertyType);
  }
}

/**
 * {@link Field}实现的属性设置器
 *
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@Value
class FieldPropertySetter<T, R> extends AccessibleProperty<T, R> implements PropertySetter<T, R> {

  /**
   * 属性
   */
  @NonNull
  Field field;

  FieldPropertySetter(@NonNull Field field) {
    super(field);
    this.field = field;
  }

  @Override
  @SneakyThrows
  public @NonNull PropertySetter<T, R> set(@NonNull T obj, @NonNull R value) {
    field.set(obj, value);
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public @NonNull TypeToken<? extends R> propertyType() {
    return TypeToken.of((Class<? extends R>) field.getType());
  }

  @Override
  public @NonNull String propertyName() {
    return field.getName();
  }

  @Override
  public @NonNull FieldPropertySetter<T, R> accessible(boolean accessible) {
    return (FieldPropertySetter<T, R>) super.accessible(accessible);
  }

  @Override
  public @NonNull <R1 extends R> FieldPropertySetter<T, R1> propertyType(
      @NonNull Class<R1> propertyType) {
    return (FieldPropertySetter<T, R1>) super.propertyType(propertyType);
  }

  @Override
  public @NonNull <R1 extends R> FieldPropertySetter<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    return (FieldPropertySetter<T, R1>) super.propertyType(propertyType);
  }
}