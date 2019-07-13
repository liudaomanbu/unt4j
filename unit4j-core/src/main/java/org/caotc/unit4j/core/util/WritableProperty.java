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
 * @see PropertyWriter
 * @since 1.0.0
 */
@Value
public class WritableProperty<T, R> {

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
  public static <T, R> WritableProperty<T, R> create(
      @NonNull Iterable<PropertyWriter<T, R>> propertySetters) {
    return new WritableProperty<>(ImmutableList.copyOf(propertySetters));
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
  public static <T, R> WritableProperty<T, R> create(
      @NonNull Iterator<PropertyWriter<T, R>> propertySetters) {
    return new WritableProperty<>(ImmutableList.copyOf(propertySetters));
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
  public static <T, R> WritableProperty<T, R> create(
      @NonNull Stream<PropertyWriter<T, R>> propertySetters) {
    return new WritableProperty<>(propertySetters
        .collect(ImmutableList.toImmutableList()));
  }

  @NonNull
  ImmutableList<PropertyWriter<T, R>> propertyWriters;

  WritableProperty(
      @NonNull ImmutableList<PropertyWriter<T, R>> propertyWriters) {
    //属性设置器集合非空
    Preconditions
        .checkArgument(!propertyWriters.isEmpty(), "propertySetters can't be empty");
    //实际属性只能有一个
    Preconditions.checkArgument(
        propertyWriters.stream().filter(FieldPropertyWriter.class::isInstance).count() <= 1,
        "Multiple FieldPropertySetter are not allowed");
    this.propertyWriters = propertyWriters.stream().distinct()
        .collect(ImmutableList.toImmutableList());
  }

  public @NonNull WritableProperty<T, R> write(@NonNull T obj, @NonNull R value) {
    propertyWriters.get(0).write(obj, value);
    return this;
  }

  public @NonNull TypeToken<? extends R> propertyType() {
    return propertyWriters.get(0).propertyType();
  }

  @SuppressWarnings("unchecked")
  public @NonNull <R1 extends R> WritableProperty<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(propertyType())
        , "PropertySetter is known propertyType %s,not %s ", propertyType(), propertyType);
    return (WritableProperty<T, R1>) this;
  }

  public @NonNull <X extends Annotation> Optional<X> annotation(
      @NonNull Class<X> annotationClass) {
    return propertyWriters.stream()
        .map(propertyWriter -> propertyWriter.annotation(annotationClass))
        .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  public @NonNull <X extends Annotation> ImmutableList<X> annotations(
      @NonNull Class<X> annotationClass) {
    return propertyWriters.stream()
        .map(propertyWriter -> propertyWriter.annotations(annotationClass))
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  public @NonNull ImmutableList<Annotation> annotations() {
    return propertyWriters.stream().map(PropertyWriter::annotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  public ImmutableList<Annotation> declaredAnnotations() {
    return propertyWriters.stream().map(PropertyWriter::declaredAnnotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  public boolean accessible() {
    return propertyWriters.stream().allMatch(PropertyWriter::accessible);
  }

  public @NonNull String propertyName() {
    return propertyWriters.get(0).propertyName();
  }

  public @NonNull WritableProperty<T, R> accessible(boolean accessible) {
    propertyWriters.forEach(propertyWriter -> propertyWriter.accessible(accessible));
    return this;
  }
}
