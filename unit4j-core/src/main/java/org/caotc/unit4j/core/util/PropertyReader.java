package org.caotc.unit4j.core.util;

import com.google.common.base.Preconditions;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Optional;
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
public abstract class PropertyReader<T, R> extends Element<T> {

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
  public static <T, R> PropertyReader<T, R> create(@NonNull Method getMethod,
      @NonNull ReflectionUtil.MethodNameStyle methodNameStyle) {
    return create((Invokable<T, R>) Invokable.from(getMethod), methodNameStyle);
  }

  /**
   * 工厂方法
   *
   * @param getMethod get方法
   * @param propertyName 属性名称
   * @return 属性获取器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public static <T, R> PropertyReader<T, R> create(@NonNull Method getMethod,
      @NonNull String propertyName) {
    return create((Invokable<T, R>) Invokable.from(getMethod), propertyName);
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
  public static <T, R> PropertyReader<T, R> create(@NonNull Invokable<T, R> getInvokable,
      @NonNull ReflectionUtil.MethodNameStyle methodNameStyle) {
    return new InvokablePropertyReader<>(getInvokable,
        methodNameStyle.fieldNameFromGetInvokable(getInvokable));
  }

  /**
   * 工厂方法
   *
   * @param getInvokable get方法封装的Invokable
   * @param propertyName 属性名称
   * @return 属性获取器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @NonNull
  public static <T, R> PropertyReader<T, R> create(@NonNull Invokable<T, R> getInvokable,
      @NonNull String propertyName) {
    return new InvokablePropertyReader<>(getInvokable, propertyName);
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
  public static <T, R> PropertyReader<T, R> create(@NonNull Field field) {
    return new FieldPropertyReader<>(field);
  }

  <M extends AccessibleObject & Member> PropertyReader(
      @NonNull M member) {
    super(member);
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
  @NonNull
  public final Optional<R> read(@NonNull T object) {
    if (!accessible()) {
      accessible(true);
    }
    return readInternal(object);
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
  @NonNull
  protected abstract Optional<R> readInternal(@NonNull T object);

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
   * @return 修改返回类型的属性获取器
   * @author caotc
   * @date 2019-06-25
   * @since 1.0.0
   */
  @NonNull
  public <R1 extends R> PropertyReader<T, R1> propertyType(@NonNull Class<R1> propertyType) {
    return propertyType(TypeToken.of(propertyType));
  }

  /**
   * 修改返回类型
   *
   * @param propertyType 新的返回类型
   * @return 修改返回类型的属性获取器
   * @author caotc
   * @date 2019-06-25
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public <R1 extends R> PropertyReader<T, R1> propertyType(@NonNull TypeToken<R1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(propertyType())
        , "AccessibleProperty is known propertyType %s,not %s ", propertyType(), propertyType);
    return (PropertyReader<T, R1>) this;
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
class InvokablePropertyReader<T, R> extends PropertyReader<T, R> {

  /**
   * get方法
   */
  @NonNull Invokable<T, R> getInvokable;
  /**
   * 属性名称
   */
  @NonNull String propertyName;

  InvokablePropertyReader(@NonNull Invokable<T, R> invokable,
      @NonNull String propertyName) {
    super(invokable);
    Preconditions
        .checkArgument(!invokable.isStatic()
                && invokable.getParameters().isEmpty()
                && !invokable.getReturnType().getRawType().equals(void.class)
            , "%s is not a getInvokable",
            invokable);
    this.getInvokable = invokable;
    this.propertyName = propertyName;
  }

  @NonNull
  @Override
  @SneakyThrows
  public Optional<R> readInternal(@NonNull T object) {
    return Optional.ofNullable(getInvokable.invoke(object));
  }

  @Override
  public @NonNull TypeToken<? extends R> propertyType() {
    return getInvokable.getReturnType();
  }

  @Override
  public @NonNull <R1 extends R> InvokablePropertyReader<T, R1> propertyType(
      @NonNull Class<R1> propertyType) {
    return (InvokablePropertyReader<T, R1>) super.propertyType(propertyType);
  }

  @Override
  public @NonNull <R1 extends R> InvokablePropertyReader<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    return (InvokablePropertyReader<T, R1>) super.propertyType(propertyType);
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
class FieldPropertyReader<T, R> extends PropertyReader<T, R> {

  /**
   * 属性
   */
  @NonNull
  Field field;

  FieldPropertyReader(@NonNull Field field) {
    super(field);
    this.field = field;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  @Override
  @SneakyThrows
  public Optional<R> readInternal(@NonNull T object) {
    return Optional.ofNullable((R) field.get(object));
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull TypeToken<? extends R> propertyType() {
    return TypeToken.of((Class<? extends R>) field.getType());
  }

  @Override
  public @NonNull String propertyName() {
    return field.getName();
  }

  @Override
  public @NonNull <R1 extends R> FieldPropertyReader<T, R1> propertyType(
      @NonNull Class<R1> propertyType) {
    return (FieldPropertyReader<T, R1>) super.propertyType(propertyType);
  }

  @Override
  public @NonNull <R1 extends R> FieldPropertyReader<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    return (FieldPropertyReader<T, R1>) super.propertyType(propertyType);
  }
}