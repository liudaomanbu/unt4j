package org.caotc.unit4j.core.util;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.Value;

/**
 * 属性存取器
 *
 * @author caotc
 * @date 2019-06-16
 * @since 1.0.0
 */
@Value(staticConstructor = "create")
public class PropertyAccessor<T, R> implements PropertySetter<T, R>, PropertyGetter<T, R> {

  /**
   * 工厂方法
   *
   * @param field 属性
   * @return 属性存取器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @NonNull
  public static <T, R> PropertyAccessor<T, R> create(@NonNull Field field) {
    return create(PropertySetter.create(field), PropertyGetter.create(field));
  }

  @NonNull
  PropertySetter<T, R> propertySetter;
  @NonNull
  PropertyGetter<T, R> propertyGetter;

  @Override
  public @NonNull Optional<R> get(@NonNull T object) {
    return propertyGetter.get(object);
  }

  @Override
  public @NonNull PropertySetter<T, R> set(@NonNull T obj, @NonNull R value) {
    propertySetter.set(obj, value);
    return this;
  }

  @Override
  public @NonNull TypeToken<? extends R> type() {
    return propertyGetter.type();
  }

  @Override
  public @NonNull <X extends Annotation> Optional<X> annotation(@NonNull Class<X> annotationClass) {
    return Stream
        .of(propertyGetter.annotation(annotationClass), propertySetter.annotation(annotationClass))
        .filter(Optional::isPresent).map(Optional::get).findAny();
  }

  @Override
  public @NonNull <X extends Annotation> ImmutableList<X> annotations(
      @NonNull Class<X> annotationClass) {
    return Stream.of(propertyGetter.annotations(annotationClass),
        propertySetter.annotations(annotationClass)).flatMap(
        Collection::stream).collect(ImmutableList.toImmutableList());
  }

  @Override
  public @NonNull ImmutableList<Annotation> annotations() {
    return Stream.of(propertyGetter.annotations(), propertySetter.annotations()).flatMap(
        Collection::stream).collect(ImmutableList.toImmutableList());
  }

  @Override
  public @NonNull String name() {
    return propertyGetter.name();
  }

  @Override
  public @NonNull PropertyAccessor<T, R> accessible(boolean accessible) {
    propertySetter.accessible(accessible);
    propertySetter.accessible(accessible);
    return this;
  }
}
