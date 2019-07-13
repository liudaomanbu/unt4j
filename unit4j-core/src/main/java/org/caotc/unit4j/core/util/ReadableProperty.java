package org.caotc.unit4j.core.util;

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
 * 可读取属性
 *
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@Value
public class ReadableProperty<T, R> {

  /**
   * 权限级别元素排序器,{@link AccessLevel#PUBLIC}最前
   */
  private static final Ordering<Element<?>> ORDERING = Ordering.natural()
      .onResultOf(Element::accessLevel);

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
    return new ReadableProperty<>(ImmutableSortedSet.copyOf(ORDERING, propertyGetters));
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
    return new ReadableProperty<>(ImmutableSortedSet.copyOf(ORDERING, propertyGetters));
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
    return new ReadableProperty<>(propertyGetters
        .collect(ImmutableSortedSet.toImmutableSortedSet(ORDERING)));
  }

  @NonNull
  ImmutableSortedSet<PropertyReader<T, R>> propertyReaders;

  ReadableProperty(
      @NonNull ImmutableSortedSet<PropertyReader<T, R>> propertyReaders) {
    //属性读取器集合不能为空
    Preconditions
        .checkArgument(!propertyReaders.isEmpty(), "propertyReaders can't be empty");
    //属性只能有一个
    Preconditions.checkArgument(
        propertyReaders.stream().map(PropertyReader::propertyName).distinct().count() == 1
            && propertyReaders.stream().map(PropertyReader::propertyType).distinct().count() == 1,
        "propertyReaders is not a common property");
    this.propertyReaders = propertyReaders;
  }

  public @NonNull Optional<R> read(@NonNull T object) {
    return propertyReaders.stream().map(propertyGetter -> propertyGetter.read(object))
        .filter(Optional::isPresent).map(Optional::get).findFirst();
  }

  public @NonNull TypeToken<? extends R> propertyType() {
    return propertyReaders.first().propertyType();
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

  public @NonNull String propertyName() {
    return propertyReaders.first().propertyName();
  }
}
