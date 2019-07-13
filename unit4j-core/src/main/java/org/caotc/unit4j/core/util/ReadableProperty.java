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
public class ReadableProperty<T, R> {

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
  public static <T, R> ReadableProperty<T, R> create(
      @NonNull Iterable<PropertyReader<T, R>> propertyGetters) {
    return new ReadableProperty<T, R>(ImmutableList.copyOf(propertyGetters));
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
  public static <T, R> ReadableProperty<T, R> create(
      @NonNull Iterator<PropertyReader<T, R>> propertyGetters) {
    return new ReadableProperty<T, R>(ImmutableList.copyOf(propertyGetters));
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
  public static <T, R> ReadableProperty<T, R> create(
      @NonNull Stream<PropertyReader<T, R>> propertyGetters) {
    return new ReadableProperty<T, R>(propertyGetters
        .collect(ImmutableList.toImmutableList()));
  }

  @NonNull
  ImmutableList<PropertyReader<T, R>> propertyReaders;

  ReadableProperty(
      @NonNull ImmutableList<PropertyReader<T, R>> propertyReaders) {
    //属性读取器集合不能为空
    Preconditions
        .checkArgument(!propertyReaders.isEmpty(), "propertyGetters can't be empty");
    //实际属性只能有一个
    Preconditions.checkArgument(
        propertyReaders.stream().filter(FieldPropertyReader.class::isInstance).count() <= 1,
        "Multiple FieldPropertyGetter are not allowed");
    this.propertyReaders = propertyReaders.stream().distinct()
        .collect(ImmutableList.toImmutableList());
  }

  public @NonNull Optional<R> read(@NonNull T object) {
    return propertyReaders.stream().map(propertyGetter -> propertyGetter.read(object))
        .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  public @NonNull TypeToken<? extends R> propertyType() {
    return propertyReaders.get(0).propertyType();
  }

  @SuppressWarnings("unchecked")
  public @NonNull <R1 extends R> ReadableProperty<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(propertyType())
        , "PropertyGetter is known propertyType %s,not %s ", propertyType(), propertyType);
    return (ReadableProperty<T, R1>) this;
  }

  public @NonNull <X extends Annotation> Optional<X> annotation(
      @NonNull Class<X> annotationClass) {
    return propertyReaders.stream()
        .map(fieldWrapper -> fieldWrapper.annotation(annotationClass))
        .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  public @NonNull <X extends Annotation> ImmutableList<X> annotations(
      @NonNull Class<X> annotationClass) {
    return propertyReaders.stream()
        .map(propertyGetter -> propertyGetter.annotations(annotationClass))
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  public @NonNull ImmutableList<Annotation> annotations() {
    return propertyReaders.stream().map(PropertyReader::annotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  public ImmutableList<Annotation> declaredAnnotations() {
    return propertyReaders.stream().map(PropertyReader::declaredAnnotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  public boolean accessible() {
    return propertyReaders.stream().allMatch(PropertyReader::accessible);
  }

  public @NonNull String propertyName() {
    return propertyReaders.get(0).propertyName();
  }

  public @NonNull ReadableProperty<T, R> accessible(boolean accessible) {
    propertyReaders.forEach(propertyGetter -> propertyGetter.accessible(accessible));
    return this;
  }
}
