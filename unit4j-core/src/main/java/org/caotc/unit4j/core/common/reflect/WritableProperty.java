package org.caotc.unit4j.core.common.reflect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.google.common.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.AccessLevel;
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
   * 权限级别元素排序器,{@link AccessLevel#PUBLIC}最前
   */
  private static final Ordering<Element<?>> ORDERING = Ordering.natural()
      .onResultOf(Element::accessLevel);

  /**
   * 工厂方法
   *
   * @param propertyWriters 属性设置器集合
   * @return 属性设置器
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  public static <T, R> WritableProperty<T, R> create(
      @NonNull Iterable<PropertyWriter<T, R>> propertyWriters) {
    return new WritableProperty<>(ImmutableSortedSet.copyOf(ORDERING, propertyWriters));
  }

  /**
   * 工厂方法
   *
   * @param propertyWriters 属性设置器集合
   * @return 属性设置器
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  public static <T, R> WritableProperty<T, R> create(
      @NonNull Iterator<PropertyWriter<T, R>> propertyWriters) {
    return new WritableProperty<>(ImmutableSortedSet.copyOf(ORDERING, propertyWriters));
  }

  /**
   * 工厂方法
   *
   * @param propertyWriterStream 属性设置器集合
   * @return 属性设置器
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  public static <T, R> WritableProperty<T, R> create(
      @NonNull Stream<PropertyWriter<T, R>> propertyWriterStream) {
    return new WritableProperty<>(propertyWriterStream
        .collect(ImmutableSortedSet.toImmutableSortedSet(ORDERING)));
  }

  @NonNull
  ImmutableSortedSet<PropertyWriter<T, R>> propertyWriters;

  WritableProperty(
      @NonNull ImmutableSortedSet<PropertyWriter<T, R>> propertyWriters) {
    //属性设置器集合非空
    Preconditions
        .checkArgument(!propertyWriters.isEmpty(), "propertyWriters can't be empty");
    //属性只能有一个
    Preconditions.checkArgument(
        propertyWriters.stream().map(PropertyWriter::propertyName).distinct().count() == 1
            && propertyWriters.stream().map(PropertyWriter::propertyType).distinct().count() == 1,
        "propertyWriters is not a common property");
    this.propertyWriters = propertyWriters;
  }

  public @NonNull WritableProperty<T, R> write(@NonNull T obj, @NonNull R value) {
    propertyWriters.first().write(obj, value);
    return this;
  }

  public @NonNull TypeToken<? extends R> propertyType() {
    return propertyWriters.first().propertyType();
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

  @NonNull
  public ImmutableList<Annotation> declaredAnnotations() {
    return propertyWriters.stream().map(PropertyWriter::declaredAnnotations)
        .flatMap(Collection::stream).collect(ImmutableList.toImmutableList());
  }

  public @NonNull String propertyName() {
    return propertyWriters.first().propertyName();
  }
}
