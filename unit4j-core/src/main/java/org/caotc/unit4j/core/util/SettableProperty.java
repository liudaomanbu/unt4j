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
 * 可设置属性
 *
 * @author caotc
 * @date 2019-05-27
 * @see PropertySetter
 * @since 1.0.0
 */
@Value
public class SettableProperty<T, R> {

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
  public static <T, R> SettableProperty<T, R> create(
      @NonNull Iterable<PropertySetter<T, R>> propertySetters) {
    return new SettableProperty<>(ImmutableList.copyOf(propertySetters));
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
  public static <T, R> SettableProperty<T, R> create(
      @NonNull Iterator<PropertySetter<T, R>> propertySetters) {
    return new SettableProperty<>(ImmutableList.copyOf(propertySetters));
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
  public static <T, R> SettableProperty<T, R> create(
      @NonNull Stream<PropertySetter<T, R>> propertySetters) {
    return new SettableProperty<>(propertySetters
        .collect(ImmutableList.toImmutableList()));
  }

  @NonNull
  ImmutableList<PropertySetter<T, R>> propertySetters;

  SettableProperty(
      @NonNull ImmutableList<PropertySetter<T, R>> propertySetters) {
    //属性设置器集合非空
    Preconditions
        .checkArgument(!propertySetters.isEmpty(), "propertySetters can't be empty");
    //实际属性只能有一个
    Preconditions.checkArgument(
        propertySetters.stream().filter(FieldPropertySetter.class::isInstance).count() <= 1,
        "Multiple FieldPropertySetter are not allowed");
    this.propertySetters = propertySetters.stream().distinct()
        .collect(ImmutableList.toImmutableList());
  }

  public @NonNull SettableProperty<T, R> set(@NonNull T obj, @NonNull R value) {
    propertySetters.get(0).set(obj, value);
    return this;
  }

  public @NonNull TypeToken<? extends R> propertyType() {
    return propertySetters.get(0).propertyType();
  }

  @SuppressWarnings("unchecked")
  public @NonNull <R1 extends R> SettableProperty<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(propertyType())
        , "PropertySetter is known propertyType %s,not %s ", propertyType(), propertyType);
    return (SettableProperty<T, R1>) this;
  }

  public @NonNull <X extends Annotation> Optional<X> annotation(
      @NonNull Class<X> annotationClass) {
    return propertySetters.stream()
        .map(propertySetter -> propertySetter.annotation(annotationClass))
        .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  public @NonNull <X extends Annotation> ImmutableList<X> annotations(
      @NonNull Class<X> annotationClass) {
    return propertySetters.stream()
        .map(propertySetter -> propertySetter.annotations(annotationClass))
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  public @NonNull ImmutableList<Annotation> annotations() {
    return propertySetters.stream().map(PropertySetter::annotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  public ImmutableList<Annotation> declaredAnnotations() {
    return propertySetters.stream().map(PropertySetter::declaredAnnotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  public boolean accessible() {
    return propertySetters.stream().allMatch(PropertySetter::accessible);
  }

  public @NonNull String propertyName() {
    return propertySetters.get(0).propertyName();
  }

  public @NonNull SettableProperty<T, R> accessible(boolean accessible) {
    propertySetters.forEach(propertySetter -> propertySetter.accessible(accessible));
    return this;
  }
}
