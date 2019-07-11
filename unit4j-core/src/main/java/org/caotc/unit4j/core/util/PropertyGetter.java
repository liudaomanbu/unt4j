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
public abstract class PropertyGetter<T, R> extends Element<T> {

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
  public static <T, R> PropertyGetter<T, R> create(@NonNull Method getMethod,
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
  public static <T, R> PropertyGetter<T, R> create(@NonNull Invokable<T, R> getInvokable,
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
  public static <T, R> PropertyGetter<T, R> create(@NonNull Field field) {
    return new FieldPropertyGetter<>(field);
  }

  <M extends AccessibleObject & Member> PropertyGetter(
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
  public abstract Optional<R> get(@NonNull T object);

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
  public <R1 extends R> PropertyGetter<T, R1> propertyType(@NonNull Class<R1> propertyType) {
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
  public <R1 extends R> PropertyGetter<T, R1> propertyType(@NonNull TypeToken<R1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(propertyType())
        , "AccessibleProperty is known propertyType %s,not %s ", propertyType(), propertyType);
    return (PropertyGetter<T, R1>) this;
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
class InvokablePropertyGetter<T, R> extends PropertyGetter<T, R> {

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
    super(getInvokable);
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
  public @NonNull TypeToken<? extends R> propertyType() {
    return getInvokable.getReturnType();
  }

  @Override
  public @NonNull <R1 extends R> InvokablePropertyGetter<T, R1> propertyType(
      @NonNull Class<R1> propertyType) {
    return (InvokablePropertyGetter<T, R1>) super.propertyType(propertyType);
  }

  @Override
  public @NonNull <R1 extends R> InvokablePropertyGetter<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    return (InvokablePropertyGetter<T, R1>) super.propertyType(propertyType);
  }

  @Override
  public @NonNull String propertyName() {
    return methodNameStyle.fieldNameFromGetInvokable(getInvokable);
  }

  @Override
  public @NonNull InvokablePropertyGetter<T, R> accessible(boolean accessible) {
    return (InvokablePropertyGetter<T, R>) super.accessible(accessible);
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
class FieldPropertyGetter<T, R> extends PropertyGetter<T, R> {

  /**
   * 属性
   */
  @NonNull
  Field field;

  FieldPropertyGetter(@NonNull Field field) {
    super(field);
    this.field = field;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  @Override
  @SneakyThrows
  public Optional<R> get(@NonNull T object) {
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
  public @NonNull <R1 extends R> FieldPropertyGetter<T, R1> propertyType(
      @NonNull Class<R1> propertyType) {
    return (FieldPropertyGetter<T, R1>) super.propertyType(propertyType);
  }

  @Override
  public @NonNull <R1 extends R> FieldPropertyGetter<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    return (FieldPropertyGetter<T, R1>) super.propertyType(propertyType);
  }

  @Override
  public @NonNull FieldPropertyGetter<T, R> accessible(boolean accessible) {
    return (FieldPropertyGetter<T, R>) super.accessible(accessible);
  }
}