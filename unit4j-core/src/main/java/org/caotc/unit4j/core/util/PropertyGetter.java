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
 * 属性获取器,可由get{@link Method}或者{@link Field}的包装实现,可以以统一的方式使用
 *
 * @param <T> 拥有该属性的类
 * @param <R> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
public interface PropertyGetter<T, R> extends AccessibleProperty<T, R> {

  /**
   * 工厂方法
   *
   * @param propertyGetters 属性获取器集合
   * @return 属性获取器
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  static <T, R> PropertyGetter<T, R> create(
      @NonNull Iterable<PropertyGetter<T, R>> propertyGetters) {
    return new CompositePropertyGetter<>(ImmutableList.copyOf(propertyGetters));
  }

  /**
   * 工厂方法
   *
   * @param propertyGetters 属性获取器集合
   * @return 属性获取器
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  static <T, R> PropertyGetter<T, R> create(
      @NonNull Iterator<PropertyGetter<T, R>> propertyGetters) {
    return new CompositePropertyGetter<>(ImmutableList.copyOf(propertyGetters));
  }

  /**
   * 工厂方法
   *
   * @param propertyGetters 属性获取器集合
   * @return 属性获取器
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  static <T, R> PropertyGetter<T, R> create(
      @NonNull Stream<PropertyGetter<T, R>> propertyGetters) {
    return new CompositePropertyGetter<T, R>(
        propertyGetters.collect(ImmutableList.toImmutableList()));
  }

  /**
   * 工厂方法
   *
   * @param getMethod get方法
   * @param methodNameStyle 方法名风格
   * @return 属性获取器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  static <T, R> PropertyGetter<T, R> create(@NonNull Method getMethod,
      @NonNull ReflectionUtil.MethodNameStyle methodNameStyle) {
    return create((Invokable<T, R>) Invokable.from(getMethod), methodNameStyle);
  }

  /**
   * 工厂方法
   *
   * @param getInvokable get方法封装的Invokable
   * @param methodNameStyle 方法名风格
   * @return 属性获取器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @NonNull
  static <T, R> PropertyGetter<T, R> create(@NonNull Invokable<T, R> getInvokable,
      @NonNull ReflectionUtil.MethodNameStyle methodNameStyle) {
    return new InvokablePropertyGetter<>(getInvokable, methodNameStyle);
  }

  /**
   * 工厂方法
   *
   * @param field 属性
   * @return 属性获取器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @NonNull
  static <T, R> PropertyGetter<T, R> create(@NonNull Field field) {
    return new FieldPropertyGetter<>(field);
  }

  /**
   * 从传入的对象中获取该属性的值
   *
   * @param object 对象
   * @return 对象中该属性的值
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull Optional<R> get(@NonNull T object);

  /**
   * 访问权限的set方法
   *
   * @param accessible 是否可访问
   * @return {@code this}
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @Override
  @NonNull PropertyGetter<T, R> accessible(boolean accessible);
}

/**
 * 组合属性获取器,由{@link FieldPropertyGetter}和{@link InvokablePropertyGetter}实现
 *
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@Value
class CompositePropertyGetter<T, R> implements PropertyGetter<T, R> {

  @NonNull
  ImmutableList<PropertyGetter<T, R>> propertyGetters;

  CompositePropertyGetter(
      @NonNull ImmutableList<PropertyGetter<T, R>> propertyGetters) {
    //不能是空集合
    Preconditions.checkArgument(!propertyGetters.isEmpty(), "propertyGetters can't empty");
    //不能重复组合
    Preconditions
        .checkArgument(
            propertyGetters.stream().noneMatch(CompositePropertyGetter.class::isInstance),
            "CompositePropertyGetter are not allowed in a CompositePropertyGetter");
    //实际属性只能有一个
    Preconditions.checkArgument(
        propertyGetters.stream().filter(FieldPropertyGetter.class::isInstance).count() <= 1,
        "Multiple FieldPropertyGetter are not allowed");
    this.propertyGetters = propertyGetters.stream().distinct()
        .collect(ImmutableList.toImmutableList());
  }

  @Override
  public @NonNull Optional<R> get(@NonNull T object) {
    return propertyGetters.stream().map(propertyGetter -> propertyGetter.get(object))
        .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  @Override
  public @NonNull TypeToken<? extends R> type() {
    return propertyGetters.get(0).type();
  }

  @Override
  public @NonNull <X extends Annotation> Optional<X> annotation(
      @NonNull Class<X> annotationClass) {
    return propertyGetters.stream()
        .map(fieldWrapper -> fieldWrapper.annotation(annotationClass))
        .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  @Override
  public @NonNull <X extends Annotation> ImmutableList<X> annotations(
      @NonNull Class<X> annotationClass) {
    return propertyGetters.stream()
        .map(propertyGetter -> propertyGetter.annotations(annotationClass))
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  @Override
  public @NonNull ImmutableList<Annotation> annotations() {
    return propertyGetters.stream().map(PropertyGetter::annotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  @Override
  public @NonNull String name() {
    return propertyGetters.get(0).name();
  }

  @Override
  public @NonNull PropertyGetter<T, R> accessible(boolean accessible) {
    propertyGetters.forEach(propertyGetter -> propertyGetter.accessible(accessible));
    return this;
  }
}

/**
 * get{@link Invokable}实现的属性获取器
 *
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@Value
class InvokablePropertyGetter<T, R> implements PropertyGetter<T, R> {

  /**
   * get方法
   */
  @NonNull Invokable<T, R> getInvokable;
  /**
   * get方法名称风格
   */
  @NonNull ReflectionUtil.MethodNameStyle methodNameStyle;

  InvokablePropertyGetter(@NonNull Invokable<T, R> getInvokable,
      @NonNull ReflectionUtil.MethodNameStyle methodNameStyle) {
    Preconditions
        .checkArgument(methodNameStyle.isGetInvokable(getInvokable), "%s is not matches %s",
            getInvokable, methodNameStyle);
    this.getInvokable = getInvokable;
    this.methodNameStyle = methodNameStyle;
  }

  @NonNull
  @Override
  @SneakyThrows
  public Optional<R> get(@NonNull T object) {
    return Optional.ofNullable(getInvokable.invoke(object));
  }

  @Override
  public @NonNull TypeToken<? extends R> type() {
    return getInvokable.getReturnType();
  }

  @Override
  public @NonNull <X extends Annotation> Optional<X> annotation(
      @NonNull Class<X> annotationClass) {
    return Optional.ofNullable(getInvokable.getAnnotation(annotationClass));
  }

  @Override
  public @NonNull <X extends Annotation> ImmutableList<X> annotations(
      @NonNull Class<X> annotationClass) {
    return ImmutableList.copyOf(getInvokable.getAnnotationsByType(annotationClass));
  }

  @Override
  public @NonNull ImmutableList<Annotation> annotations() {
    return ImmutableList.copyOf(getInvokable.getAnnotations());
  }

  @Override
  public @NonNull String name() {
    return methodNameStyle.fieldNameFromGetInvokable(getInvokable);
  }

  @Override
  public @NonNull PropertyGetter<T, R> accessible(boolean accessible) {
    getInvokable.setAccessible(accessible);
    return this;
  }

}

/**
 * {@link Field}实现的属性获取器
 *
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@Value
class FieldPropertyGetter<T, R> implements PropertyGetter<T, R> {

  /**
   * 属性
   */
  @NonNull
  Field field;

  @NonNull
  @SuppressWarnings("unchecked")
  @Override
  @SneakyThrows
  public Optional<R> get(@NonNull T object) {
    return Optional.ofNullable((R) field.get(object));
  }

  @SuppressWarnings("unchecked")
  @Override
  public @NonNull TypeToken<? extends R> type() {
    return TypeToken.of((Class<? extends R>) field.getType());
  }

  @Override
  public @NonNull <X extends Annotation> Optional<X> annotation(
      @NonNull Class<X> annotationClass) {
    return Optional.ofNullable(field.getAnnotation(annotationClass));
  }

  @Override
  public @NonNull <X extends Annotation> ImmutableList<X> annotations(
      @NonNull Class<X> annotationClass) {
    return ImmutableList.copyOf(field.getAnnotationsByType(annotationClass));
  }

  @Override
  public @NonNull ImmutableList<Annotation> annotations() {
    return ImmutableList.copyOf(field.getAnnotations());
  }

  @Override
  public @NonNull String name() {
    return field.getName();
  }

  @Override
  public @NonNull PropertyGetter<T, R> accessible(boolean accessible) {
    field.setAccessible(accessible);
    return this;
  }
}