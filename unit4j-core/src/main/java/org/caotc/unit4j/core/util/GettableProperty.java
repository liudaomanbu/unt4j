package org.caotc.unit4j.core.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.Value;

/**
 * 可读取属性
 *
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@Value
public class GettableProperty<T, R> {

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
  public static <T, R> GettableProperty<T, R> create(
      @NonNull Iterable<PropertyGetter<T, R>> propertyGetters) {
    return new GettableProperty<T, R>(ImmutableList.copyOf(propertyGetters));
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
  public static <T, R> GettableProperty<T, R> create(
      @NonNull Iterator<PropertyGetter<T, R>> propertyGetters) {
    return new GettableProperty<T, R>(ImmutableList.copyOf(propertyGetters));
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
  public static <T, R> GettableProperty<T, R> create(
      @NonNull Stream<PropertyGetter<T, R>> propertyGetters) {
    return new GettableProperty<T, R>(propertyGetters
        .collect(ImmutableList.toImmutableList()));
  }

  @NonNull
  ImmutableList<PropertyGetter<T, R>> propertyGetters;

  GettableProperty(
      @NonNull ImmutableList<PropertyGetter<T, R>> propertyGetters) {
    //属性读取器集合不能为空
    Preconditions
        .checkArgument(!propertyGetters.isEmpty(), "propertyGetters can't be empty");
    //实际属性只能有一个
    Preconditions.checkArgument(
        propertyGetters.stream().filter(FieldPropertyGetter.class::isInstance).count() <= 1,
        "Multiple FieldPropertyGetter are not allowed");
    this.propertyGetters = propertyGetters.stream().distinct()
        .collect(ImmutableList.toImmutableList());
  }

  public @NonNull Optional<R> get(@NonNull T object) {
    return propertyGetters.stream().map(propertyGetter -> propertyGetter.get(object))
        .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  public @NonNull TypeToken<? extends R> propertyType() {
    return propertyGetters.get(0).propertyType();
  }

  @SuppressWarnings("unchecked")
  public @NonNull <R1 extends R> GettableProperty<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(propertyType())
        , "PropertyGetter is known propertyType %s,not %s ", propertyType(), propertyType);
    return (GettableProperty<T, R1>) this;
  }

  public @NonNull <X extends Annotation> Optional<X> annotation(
      @NonNull Class<X> annotationClass) {
    return propertyGetters.stream()
        .map(fieldWrapper -> fieldWrapper.annotation(annotationClass))
        .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  public @NonNull <X extends Annotation> ImmutableList<X> annotations(
      @NonNull Class<X> annotationClass) {
    return propertyGetters.stream()
        .map(propertyGetter -> propertyGetter.annotations(annotationClass))
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  public @NonNull ImmutableList<Annotation> annotations() {
    return propertyGetters.stream().map(PropertyGetter::annotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  public ImmutableList<Annotation> declaredAnnotations() {
    return propertyGetters.stream().map(PropertyGetter::declaredAnnotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  public boolean accessible() {
    return propertyGetters.stream().allMatch(PropertyGetter::accessible);
  }

  public @NonNull String propertyName() {
    return propertyGetters.get(0).propertyName();
  }

  public @NonNull GettableProperty<T, R> accessible(boolean accessible) {
    propertyGetters.forEach(propertyGetter -> propertyGetter.accessible(accessible));
    return this;
  }
}
