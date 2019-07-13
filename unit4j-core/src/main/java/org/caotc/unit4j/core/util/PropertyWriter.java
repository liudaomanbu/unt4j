package org.caotc.unit4j.core.util;

import com.google.common.base.Preconditions;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.Value;

/**
 * 属性设置器,可由get{@link Method}或者{@link Field}的包装实现,可以以统一的方式使用
 *
 * @param <T> 拥有该属性的类
 * @param <R> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
public abstract class PropertyWriter<T, R> extends Element<T> {

  /**
   * 工厂方法
   *
   * @param setMethod set方法
   * @param methodNameStyle 方法名称风格
   * @return 属性设置器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public static <T, R> PropertyWriter<T, R> create(@NonNull Method setMethod,
      @NonNull ReflectionUtil.MethodNameStyle methodNameStyle) {
    return create((Invokable<T, ?>) Invokable.from(setMethod), methodNameStyle);
  }

  /**
   * 工厂方法
   *
   * @param setMethod set方法
   * @param propertyName 属性名称
   * @return 属性设置器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public static <T, R> PropertyWriter<T, R> create(@NonNull Method setMethod,
      @NonNull String propertyName) {
    return create((Invokable<T, ?>) Invokable.from(setMethod), propertyName);
  }

  /**
   * 工厂方法
   *
   * @param setInvokable set方法
   * @param methodNameStyle 方法名称风格
   * @return 属性设置器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */

  @NonNull
  public static <T, R> PropertyWriter<T, R> create(@NonNull Invokable<T, ?> setInvokable,
      @NonNull ReflectionUtil.MethodNameStyle methodNameStyle) {
    return new InvokablePropertyWriter<>(setInvokable,
        methodNameStyle.fieldNameFromSetInvokable(setInvokable));
  }

  /**
   * 工厂方法
   *
   * @param setInvokable set方法
   * @param propertyName 属性名称
   * @return 属性设置器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */

  @NonNull
  public static <T, R> PropertyWriter<T, R> create(@NonNull Invokable<T, ?> setInvokable,
      @NonNull String propertyName) {
    return new InvokablePropertyWriter<>(setInvokable, propertyName);
  }

  /**
   * 工厂方法
   *
   * @param field 属性
   * @return 属性设置器
   * @author caotc
   * @date 2019-06-16
   * @since 1.0.0
   */

  @NonNull
  public static <T, R> PropertyWriter<T, R> create(@NonNull Field field) {
    return new FieldPropertyWriter<>(field);
  }

  <M extends AccessibleObject & Member> PropertyWriter(
      @NonNull M member) {
    super(member);
  }

  /**
   * 给传入对象的该属性设置传入的值
   *
   * @param obj 设置属性值的对象
   * @param value 设置的属性值
   * @return {@code this}
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  @NonNull
  public abstract PropertyWriter<T, R> write(@NonNull T obj, @NonNull R value);

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
   * @return 修改返回类型的属性设置器
   * @author caotc
   * @date 2019-06-25
   * @since 1.0.0
   */
  @NonNull
  public <R1 extends R> PropertyWriter<T, R1> propertyType(@NonNull Class<R1> propertyType) {
    return propertyType(TypeToken.of(propertyType));
  }

  /**
   * 修改返回类型
   *
   * @param propertyType 新的返回类型
   * @return 修改返回类型的属性设置器
   * @author caotc
   * @date 2019-06-25
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public <R1 extends R> PropertyWriter<T, R1> propertyType(@NonNull TypeToken<R1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(propertyType())
        , "AccessibleProperty is known propertyType %s,not %s ", propertyType(), propertyType);
    return (PropertyWriter<T, R1>) this;
  }
}

/**
 * set{@link Invokable}实现的属性设置器
 *
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@Value
class InvokablePropertyWriter<T, R> extends PropertyWriter<T, R> {

  /**
   * set方法
   */
  @NonNull
  Invokable<T, ?> setInvokable;
  /**
   * set方法名称风格
   */
  @NonNull
  String propertyName;

  InvokablePropertyWriter(@NonNull Invokable<T, ?> invokable,
      @NonNull String propertyName) {
    super(invokable);
    Preconditions
        .checkArgument(!invokable.isStatic()
                && (invokable.getReturnType().getRawType().equals(void.class) || invokable
                .getReturnType()
                .equals(invokable.getOwnerType()))
                && invokable.getParameters().size() == 1, "%s is not a setInvokable",
            invokable);
    this.setInvokable = invokable;
    this.propertyName = propertyName;
  }

  @Override
  @SneakyThrows
  public @NonNull PropertyWriter<T, R> write(@NonNull T obj, @NonNull R value) {
    setInvokable.invoke(obj, value);
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public @NonNull TypeToken<? extends R> propertyType() {
    return (TypeToken<? extends R>) setInvokable.getParameters().get(0).getType();
  }

  @Override
  public @NonNull InvokablePropertyWriter<T, R> accessible(boolean accessible) {
    return (InvokablePropertyWriter<T, R>) super.accessible(accessible);
  }

  @Override
  public @NonNull <R1 extends R> InvokablePropertyWriter<T, R1> propertyType(
      @NonNull Class<R1> propertyType) {
    return (InvokablePropertyWriter<T, R1>) super.propertyType(propertyType);
  }

  @Override
  public @NonNull <R1 extends R> InvokablePropertyWriter<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    return (InvokablePropertyWriter<T, R1>) super.propertyType(propertyType);
  }
}

/**
 * {@link Field}实现的属性设置器
 *
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@Value
class FieldPropertyWriter<T, R> extends PropertyWriter<T, R> {

  /**
   * 属性
   */
  @NonNull
  Field field;

  FieldPropertyWriter(@NonNull Field field) {
    super(field);
    this.field = field;
  }

  @Override
  @SneakyThrows
  public @NonNull PropertyWriter<T, R> write(@NonNull T obj, @NonNull R value) {
    field.set(obj, value);
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public @NonNull TypeToken<? extends R> propertyType() {
    return TypeToken.of((Class<? extends R>) field.getType());
  }

  @Override
  public @NonNull String propertyName() {
    return field.getName();
  }

  @Override
  public @NonNull FieldPropertyWriter<T, R> accessible(boolean accessible) {
    return (FieldPropertyWriter<T, R>) super.accessible(accessible);
  }

  @Override
  public @NonNull <R1 extends R> FieldPropertyWriter<T, R1> propertyType(
      @NonNull Class<R1> propertyType) {
    return (FieldPropertyWriter<T, R1>) super.propertyType(propertyType);
  }

  @Override
  public @NonNull <R1 extends R> FieldPropertyWriter<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    return (FieldPropertyWriter<T, R1>) super.propertyType(propertyType);
  }
}