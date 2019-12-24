/*
 * Copyright (C) 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.caotc.unit4j.core.common.util;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.Parameter;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.reflect.AbstractPropertyReader.FieldElementPropertyReader;
import org.caotc.unit4j.core.common.reflect.AbstractPropertyWriter.FieldElementPropertyWriter;
import org.caotc.unit4j.core.common.reflect.FieldElement;
import org.caotc.unit4j.core.common.reflect.MethodNameStyle;
import org.caotc.unit4j.core.common.reflect.Property;
import org.caotc.unit4j.core.common.reflect.PropertyAccessor;
import org.caotc.unit4j.core.common.reflect.PropertyElement;
import org.caotc.unit4j.core.common.reflect.PropertyReader;
import org.caotc.unit4j.core.common.reflect.PropertyWriter;
import org.caotc.unit4j.core.common.reflect.ReadableProperty;
import org.caotc.unit4j.core.common.reflect.WritableProperty;
import org.caotc.unit4j.core.exception.ReadablePropertyNotFoundException;
//TODO 将所有方法优化到只有一次流操作

/**
 * 反射工具类
 *
 * @author caotc
 * @date 2019-05-10
 * @since 1.0.0
 */
@UtilityClass
@Beta
@Slf4j
public class ReflectionUtil {

  private static final Function<PropertyElement<?, ?>, ImmutableList<?>> KEY_FUNCTION = propertyElement -> ImmutableList
      .of(propertyElement.propertyName(), propertyElement.propertyType(),
          propertyElement.isStatic());

  @NonNull
  public static ImmutableSet<Field> fieldsFromClass(@NonNull Type type) {
    return fieldStreamFromClass(type).collect(ImmutableSet.toImmutableSet());
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有属性
   *
   * @param clazz 需要获取属性的类
   * @return 包括所有超类和接口的所有属性
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static ImmutableSet<Field> fieldsFromClass(@NonNull Class<?> clazz) {
    return fieldStreamFromClass(clazz).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static ImmutableSet<Field> fieldsFromClass(@NonNull TypeToken<?> typeToken) {
    return fieldStreamFromClass(typeToken).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static Stream<Field> fieldStreamFromClass(@NonNull Type type) {
    return fieldStreamFromClass(TypeToken.of(type));
  }

  @NonNull
  public static Stream<Field> fieldStreamFromClass(@NonNull Class<?> clazz) {
    return fieldStreamFromClass(TypeToken.of(clazz));
  }

  @NonNull
  public static Stream<Field> fieldStreamFromClass(@NonNull TypeToken<?> typeToken) {
    return typeToken.getTypes().rawTypes().stream().map(Class::getDeclaredFields).flatMap(
        Arrays::stream);
  }

  @NonNull
  public static ImmutableSet<FieldElement<?, ?>> fieldElementsFromClass(
      @NonNull Type type) {
    return fieldElementStreamFromClass(type).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<FieldElement<T, ?>> fieldElementsFromClass(
      @NonNull Class<T> clazz) {
    return fieldElementStreamFromClass(clazz).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<FieldElement<T, ?>> fieldElementsFromClass(
      @NonNull TypeToken<T> typeToken) {
    return fieldElementStreamFromClass(typeToken).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static Stream<FieldElement<?, ?>> fieldElementStreamFromClass(
      @NonNull Type type) {
    return fieldElementStreamFromClass(TypeToken.of(type))
        .map(fieldElement -> (FieldElement<?, ?>) fieldElement);
  }

  @NonNull
  public static <T> Stream<FieldElement<T, ?>> fieldElementStreamFromClass(
      @NonNull Class<T> clazz) {
    return fieldElementStreamFromClass(TypeToken.of(clazz));
  }

  @NonNull
  public static <T> Stream<FieldElement<T, ?>> fieldElementStreamFromClass(
      @NonNull TypeToken<T> typeToken) {
    return typeToken.getTypes().rawTypes().stream().map(Class::getDeclaredFields).flatMap(
        Arrays::stream).map(FieldElement::from);
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有属性中属性名称为指定名称的属性
   *
   * @param clazz 需要获取属性的类
   * @param fieldName 指定属性名称
   * @return 包括所有超类和接口的所有属性中属性名称为指定名称的属性
   * @author caotc
   * @date 2019-05-22
   * @apiNote 在java中超类和字类可以定义相同名称的属性, 并且不会冲突, 因此返回的属性可能为复数
   * @since 1.0.0
   */
  @NonNull
  public static ImmutableSet<Field> fieldsFromClass(@NonNull Class<?> clazz,
      @NonNull String fieldName) {
    return fieldsFromClass(clazz).stream().filter(field -> fieldName.equals(field.getName()))
        .collect(ImmutableSet.toImmutableSet());
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有方法
   *
   * @param clazz 需要获取方法的类
   * @return 所有方法(包括所有超类和接口的方法)
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static ImmutableSet<Method> methodsFromClass(@NonNull Class<?> clazz) {
    return methodStreamFromClass(clazz).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static ImmutableSet<Method> methodsFromClass(@NonNull Type type) {
    return methodStreamFromClass(type).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static ImmutableSet<Method> methodsFromClass(@NonNull TypeToken<?> typeToken) {
    return methodStreamFromClass(typeToken).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static Stream<Method> methodStreamFromClass(@NonNull Type type) {
    return methodStreamFromClass(TypeToken.of(type));
  }

  @NonNull
  public static Stream<Method> methodStreamFromClass(@NonNull Class<?> clazz) {
    return methodStreamFromClass(TypeToken.of(clazz));
  }

  @NonNull
  public static Stream<Method> methodStreamFromClass(@NonNull TypeToken<?> typeToken) {
    return typeToken.getTypes().rawTypes().stream()
        .map(Class::getDeclaredMethods)
        .flatMap(Arrays::stream);
  }

  @NonNull
  public static ImmutableSet<Invokable<?, ?>> methodInvokablesFromClass(
      @NonNull Type type) {
    return methodInvokableStreamFromClass(type).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<Invokable<T, ?>> methodInvokablesFromClass(
      @NonNull Class<T> clazz) {
    return methodInvokableStreamFromClass(clazz).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<Invokable<T, ?>> methodInvokablesFromClass(
      @NonNull TypeToken<T> typeToken) {
    return methodInvokableStreamFromClass(typeToken).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static Stream<Invokable<?, ?>> methodInvokableStreamFromClass(
      @NonNull Type type) {
    return methodInvokableStreamFromClass(TypeToken.of(type))
        .map(invokable -> (Invokable<?, ?>) invokable);
  }

  @NonNull
  public static <T> Stream<Invokable<T, ?>> methodInvokableStreamFromClass(
      @NonNull Class<T> clazz) {
    return methodInvokableStreamFromClass(TypeToken.of(clazz));
  }

  @NonNull
  public static <T> Stream<Invokable<T, ?>> methodInvokableStreamFromClass(
      @NonNull TypeToken<T> typeToken) {
    return methodStreamFromClass(typeToken).map(typeToken::method);
  }

  @NonNull
  public static ImmutableSet<Constructor<?>> constructorsFromClass(@NonNull Type type) {
    return constructorStreamFromClass(type).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<Constructor<T>> constructorsFromClass(@NonNull Class<T> clazz) {
    return constructorStreamFromClass(clazz).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<Constructor<T>> constructorsFromClass(
      @NonNull TypeToken<T> typeToken) {
    return constructorStreamFromClass(typeToken).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static Stream<Constructor<?>> constructorStreamFromClass(@NonNull Type type) {
    return constructorStreamFromClass(TypeToken.of(type))
        .map(constructor -> (Constructor<?>) constructor);
  }

  @NonNull
  public static <T> Stream<Constructor<T>> constructorStreamFromClass(@NonNull Class<T> clazz) {
    return constructorStreamFromClass(TypeToken.of(clazz));
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public static <T> Stream<Constructor<T>> constructorStreamFromClass(
      @NonNull TypeToken<T> typeToken) {
    return Arrays.stream(typeToken.getRawType().getDeclaredConstructors())
        .map(constructor -> (Constructor<T>) constructor);
  }

  @NonNull
  public static ImmutableSet<Invokable<?, ?>> constructorInvokablesFromClass(
      @NonNull Type type) {
    return constructorInvokableStreamFromClass(type).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<Invokable<T, T>> constructorInvokablesFromClass(
      @NonNull Class<T> clazz) {
    return constructorInvokableStreamFromClass(clazz).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<Invokable<T, T>> constructorInvokablesFromClass(
      @NonNull TypeToken<T> typeToken) {
    return constructorInvokableStreamFromClass(typeToken).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static Stream<Invokable<?, ?>> constructorInvokableStreamFromClass(
      @NonNull Type type) {
    return constructorInvokableStreamFromClass(TypeToken.of(type))
        .map(invokable -> (Invokable<?, ?>) invokable);
  }

  @NonNull
  public static <T> Stream<Invokable<T, T>> constructorInvokableStreamFromClass(
      @NonNull Class<T> clazz) {
    return constructorInvokableStreamFromClass(TypeToken.of(clazz));
  }

  @NonNull
  public static <T> Stream<Invokable<T, T>> constructorInvokableStreamFromClass(
      @NonNull TypeToken<T> typeToken) {
    return Arrays.stream(typeToken.getRawType().getDeclaredConstructors())
        .map(typeToken::constructor);
  }

  @NonNull
  public static ImmutableSet<Invokable<?, ?>> invokablesFromClass(
      @NonNull Type type) {
    return invokableStreamFromClass(type).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<Invokable<T, ?>> invokablesFromClass(
      @NonNull Class<T> clazz) {
    return invokableStreamFromClass(clazz).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<Invokable<T, ?>> invokablesFromClass(
      @NonNull TypeToken<T> typeToken) {
    return invokableStreamFromClass(typeToken).collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static Stream<Invokable<?, ?>> invokableStreamFromClass(
      @NonNull Type type) {
    return invokableStreamFromClass(TypeToken.of(type))
        .map(invokable -> (Invokable<?, ?>) invokable);
  }

  @NonNull
  public static <T> Stream<Invokable<T, ?>> invokableStreamFromClass(
      @NonNull Class<T> clazz) {
    return invokableStreamFromClass(TypeToken.of(clazz));
  }

  @NonNull
  public static <T> Stream<Invokable<T, ?>> invokableStreamFromClass(
      @NonNull TypeToken<T> typeToken) {
    return Stream.concat(constructorInvokableStreamFromClass(typeToken),
        methodInvokableStreamFromClass(typeToken));
  }

  /**
   * 根据指定的get方法名称格式从传入的类中获取包括所有超类和接口的所有get方法
   *
   * @param clazz 需要获取get方法的类
   * @return 所有超类和接口的所有get方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public ImmutableSet<Method> getMethodsFromClass(@NonNull Class<?> clazz) {
    return getMethodsFromClass(clazz, true);
  }

  /**
   * 根据指定的get方法名称格式从传入的类中获取包括所有超类和接口的所有get方法
   *
   * @param clazz 需要获取get方法的类
   * @param fieldExistCheck 是否检查有对应{@link Field}存在
   * @return 所有超类和接口的所有get方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public ImmutableSet<Method> getMethodsFromClass(@NonNull Class<?> clazz,
      boolean fieldExistCheck) {
    return getMethodsFromClass(clazz, fieldExistCheck, MethodNameStyle.values());
  }

  /**
   * 根据指定的get方法名称格式从传入的类中获取包括所有超类和接口的所有get方法
   *
   * @param clazz 需要获取get方法的类
   * @param fieldExistCheck 是否检查有对应{@link Field}存在
   * @param methodNameStyles 指定的get方法名称格式集合 {@link MethodNameStyle}
   * @return 所有超类和接口的所有get方法
   * @author caotc
   * @date 2019-05-22
   * @apiNote 如果只想获得JavaBean规范的get方法, {@code methodNameStyles}请传入{@link MethodNameStyle#JAVA_BEAN}
   */
  @NonNull
  public ImmutableSet<Method> getMethodsFromClass(@NonNull Class<?> clazz, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {
    Stream<Method> methodStream = methodsFromClass(clazz).stream()
        .filter(method -> isPropertyReader(method, methodNameStyles));
    if (fieldExistCheck) {
      ImmutableSet<ImmutableList<?>> getMethodKeys = fieldsFromClass(clazz).stream()
          .flatMap(field -> Arrays.stream(methodNameStyles)
              .map(methodNameStyle -> ImmutableList
                  .of(methodNameStyle.getMethodNameFromField(field), field.getType())))
          .collect(ImmutableSet.toImmutableSet());
      methodStream = methodStream.filter(getMethod -> getMethodKeys
          .contains(ImmutableList.of(getMethod.getName(), getMethod.getReturnType())));
    }
    return methodStream.collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public ImmutableSet<Method> accessorMethodsFromClass(@NonNull Class<?> clazz,
      boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {
    Stream<Method> methodStream = methodsFromClass(clazz).stream()
        .filter(method -> isPropertyReader(method, methodNameStyles) || isPropertyWriter(method,
            methodNameStyles));
    if (fieldExistCheck) {
      ImmutableSet<ImmutableList<?>> accessorMethodKeys = fieldsFromClass(clazz).stream()
          .flatMap(field -> Arrays.stream(methodNameStyles)
              .flatMap(methodNameStyle -> Stream.of(ImmutableList
                  .of(methodNameStyle.getMethodNameFromField(field), field.getType()), ImmutableList
                  .of(methodNameStyle.setMethodNameFromField(field), field.getType()))))
          .collect(ImmutableSet.toImmutableSet());
      methodStream = methodStream.filter(accessorMethod -> accessorMethodKeys
          .contains(ImmutableList.of(accessorMethod.getName(), accessorMethod.getReturnType())));
    }
    return methodStream.collect(ImmutableSet.toImmutableSet());
  }

  /**
   * 根据指定的set方法名称格式从传入的类中获取包括所有超类和接口的所有set方法
   *
   * @param clazz 需要获取set方法的类
   * @return 所有超类和接口的所有set方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public ImmutableSet<Method> setMethodsFromClass(@NonNull Class<?> clazz) {
    return setMethodsFromClass(clazz, true);
  }

  /**
   * 根据指定的set方法名称格式从传入的类中获取包括所有超类和接口的所有set方法
   *
   * @param clazz 需要获取set方法的类
   * @param fieldExistCheck 是否检查有对应{@link Field}存在
   * @return 所有超类和接口的所有set方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public ImmutableSet<Method> setMethodsFromClass(@NonNull Class<?> clazz,
      boolean fieldExistCheck) {
    return setMethodsFromClass(clazz, fieldExistCheck, MethodNameStyle.values());
  }

  /**
   * 根据指定的set方法名称格式从传入的类中获取包括所有超类和接口的所有set方法
   *
   * @param clazz 需要获取set方法的类
   * @param fieldExistCheck 是否检查有对应{@link Field}存在
   * @param methodNameStyles 指定的set方法名称格式集合 {@link MethodNameStyle}
   * @return 所有超类和接口的所有set方法
   * @author caotc
   * @date 2019-05-22
   * @apiNote 如果只想获得JavaBean规范的set方法, {@code methodNameStyles}请传入{@link MethodNameStyle#JAVA_BEAN}
   */
  @NonNull
  public ImmutableSet<Method> setMethodsFromClass(@NonNull Class<?> clazz, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {
    Stream<Method> methodStream = methodsFromClass(clazz).stream()
        .filter(method -> isPropertyWriter(method, methodNameStyles));
    if (fieldExistCheck) {
      ImmutableSet<ImmutableList<?>> setMethodKeys = fieldsFromClass(clazz).stream()
          .flatMap(field -> Arrays.stream(methodNameStyles)
              .map(methodNameStyle -> ImmutableList
                  .of(methodNameStyle.setMethodNameFromField(field), field.getType())))
          .collect(ImmutableSet.toImmutableSet());
      methodStream = methodStream.filter(setMethod -> setMethodKeys
          .contains(ImmutableList.of(setMethod.getName(), setMethod.getParameterTypes()[0])));
    }
    return methodStream.collect(ImmutableSet.toImmutableSet());
  }

  /**
   * 根据指定的get方法名称格式从传入的类中获取包括所有超类和接口的所有get方法中指定属性名称的get方法
   *
   * @param clazz 需要获取get方法的类
   * @param fieldName 指定属性名称
   * @return 包括所有超类和接口的所有get方法中指定属性名称的get方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public Optional<Method> getMethodFromClass(@NonNull Class<?> clazz,
      @NonNull String fieldName) {
    return getMethodFromClass(clazz, fieldName, true);
  }

  /**
   * 根据指定的get方法名称格式从传入的类中获取包括所有超类和接口的所有get方法中指定属性名称的get方法
   *
   * @param clazz 需要获取get方法的类
   * @param fieldName 指定属性名称
   * @param fieldExistCheck 是否检查有对应{@link Field}存在
   * @return 包括所有超类和接口的所有get方法中指定属性名称的get方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public Optional<Method> getMethodFromClass(@NonNull Class<?> clazz,
      @NonNull String fieldName, boolean fieldExistCheck) {
    return getMethodFromClass(clazz, fieldName, fieldExistCheck, MethodNameStyle.JAVA_BEAN);
  }

  /**
   * 根据指定的get方法名称格式从传入的类中获取包括所有超类和接口的所有get方法中指定属性名称的get方法
   *
   * @param clazz 需要获取get方法的类
   * @param methodNameStyle 指定的get方法名称格式 {@link MethodNameStyle}
   * @param fieldName 指定属性名称
   * @param fieldExistCheck 是否检查有对应{@link Field}存在
   * @return 包括所有超类和接口的所有get方法中指定属性名称的get方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public Optional<Method> getMethodFromClass(@NonNull Class<?> clazz,
      @NonNull String fieldName, boolean fieldExistCheck,
      @NonNull MethodNameStyle methodNameStyle) {
    return getMethodsFromClass(clazz, fieldExistCheck, methodNameStyle).stream()
        .filter(getMethod -> fieldName.equals(methodNameStyle.fieldNameFromGetMethod(getMethod)))
        .findAny();
  }

  /**
   * 根据指定的set方法名称格式从传入的类中获取包括所有超类和接口的所有set方法中指定属性名称的set方法
   *
   * @param clazz 需要获取set方法的类
   * @param fieldName 指定属性名称
   * @return 包括所有超类和接口的所有set方法中指定属性名称的set方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public Optional<Method> setMethodFromClass(@NonNull Class<?> clazz,
      @NonNull String fieldName) {
    return setMethodFromClass(clazz, fieldName, true);
  }

  /**
   * 根据指定的set方法名称格式从传入的类中获取包括所有超类和接口的所有set方法中指定属性名称的set方法
   *
   * @param clazz 需要获取set方法的类
   * @param fieldName 指定属性名称
   * @param fieldExistCheck 是否检查有对应{@link Field}存在
   * @return 包括所有超类和接口的所有set方法中指定属性名称的set方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public Optional<Method> setMethodFromClass(@NonNull Class<?> clazz,
      @NonNull String fieldName, boolean fieldExistCheck) {
    return setMethodFromClass(clazz, fieldName, fieldExistCheck, MethodNameStyle.JAVA_BEAN);
  }

  /**
   * 根据指定的set方法名称格式从传入的类中获取包括所有超类和接口的所有set方法中指定属性名称的set方法
   *
   * @param clazz 需要获取set方法的类
   * @param methodNameStyle 指定的set方法名称格式 {@link MethodNameStyle}
   * @param fieldName 指定属性名称
   * @param fieldExistCheck 是否检查有对应{@link Field}存在
   * @return 包括所有超类和接口的所有set方法中指定属性名称的set方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public Optional<Method> setMethodFromClass(@NonNull Class<?> clazz,
      @NonNull String fieldName, boolean fieldExistCheck,
      @NonNull MethodNameStyle methodNameStyle) {
    return setMethodsFromClass(clazz, fieldExistCheck, methodNameStyle).stream()
        .filter(setMethod -> fieldName.equals(methodNameStyle.fieldNameFromSetMethod(setMethod)))
        .findAny();
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有get方法与属性的包装{@link PropertyReader}
   *
   * @param clazz 需要获取get方法的类
   * @return 包括所有超类和接口的所有get方法与属性的包装 {@link PropertyReader}
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static <T> ImmutableSet<ReadableProperty<T, ?>> readablePropertiesFromClass(
      @NonNull Class<T> clazz) {
    return readablePropertiesFromClass(clazz, true);
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有可读属性 {@link ReadableProperty}集合
   *
   * @param clazz 需要获取可读属性 {@link ReadableProperty}的类
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @return 所有可读属性 {@link ReadableProperty}集合
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static <T> ImmutableSet<ReadableProperty<T, ?>> readablePropertiesFromClass(
      @NonNull Class<T> clazz, boolean fieldExistCheck) {
    return readablePropertiesFromClass(clazz, fieldExistCheck, MethodNameStyle.values());
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有可读属性{@link ReadableProperty}集合
   *
   * @param clazz 需要获取可读属性{@link ReadableProperty}的类
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @param methodNameStyles get方法格式集合
   * @return 所有可读属性 {@link ReadableProperty}集合
   * @author caotc
   * @date 2019-05-10
   * @apiNote 如果只想要获取JavaBean规范的get方法, {@code methodNameStyles}参数使用{@link
   * MethodNameStyle#JAVA_BEAN}
   * @since 1.0.0
   */
  @NonNull
  public static <T> ImmutableSet<ReadableProperty<T, ?>> readablePropertiesFromClass(
      @NonNull Class<T> clazz, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {

    Function<PropertyReader<T, ?>, ImmutableList<?>> propertyGetterToKeyFunction = propertyGetter -> ImmutableList
        .of(propertyGetter.propertyName(), propertyGetter.propertyType());

    Stream<PropertyReader<T, ?>> getInvokablePropertyGetters = getMethodsFromClass(clazz,
        fieldExistCheck, methodNameStyles).stream()
        .flatMap(getMethod -> Arrays.stream(methodNameStyles)
            .filter(methodNameStyle -> methodNameStyle.isGetMethod(getMethod))
            .map(methodNameStyle -> PropertyReader.from(getMethod, methodNameStyle)));

    Stream<PropertyReader<T, ?>> fieldPropertyGetters = fieldsFromClass(
        clazz).stream().map(PropertyReader::from);

    ImmutableListMultimap<@NonNull ImmutableList<?>, PropertyReader<T, ?>> propertyGetterMultimap =
        Stream.concat(fieldPropertyGetters, getInvokablePropertyGetters)
            .collect(ImmutableListMultimap
                .toImmutableListMultimap(propertyGetterToKeyFunction, Function.identity()));

    return propertyGetterMultimap.asMap().values().stream()
        .filter(propertyReaders -> !fieldExistCheck || propertyReaders.stream()
            .anyMatch(FieldElementPropertyReader.class::isInstance))
        .map(propertyGetters -> propertyGetters.stream().map(o -> (PropertyReader<T, ?>) o))
        .map(ReadableProperty::create).collect(ImmutableSet.toImmutableSet());
  }

  /**
   * get all properties from class
   *
   * @param typeToken target class
   * @param methodNameStyles get set methods styles
   * @return {@link Stream} of all properties
   * @author caotc
   * @date 2019-11-28
   * @apiNote
   * @since 1.0.0
   */
  @NonNull
  public static Stream<Property<?, ?>> propertyStreamFromClass(
      @NonNull Type typeToken,
      @NonNull MethodNameStyle... methodNameStyles) {
    return propertyStreamFromClass(TypeToken.of(typeToken), methodNameStyles)
        .map(property -> (Property<?, ?>) property);
  }

  /**
   * get all properties from class
   *
   * @param clazz target class
   * @param methodNameStyles get set methods styles
   * @return {@link Stream} of all properties
   * @author caotc
   * @date 2019-11-28
   * @apiNote
   * @since 1.0.0
   */
  @NonNull
  public static <T> Stream<Property<T, ?>> propertyStreamFromClass(
      @NonNull Class<T> clazz,
      @NonNull MethodNameStyle... methodNameStyles) {
    return propertyStreamFromClass(TypeToken.of(clazz), methodNameStyles);
  }

  /**
   * get all properties from class
   *
   * @param typeToken target class
   * @param methodNameStyles get set methods styles
   * @return {@link Stream} of all properties
   * @author caotc
   * @date 2019-11-28
   * @apiNote
   * @since 1.0.0
   */
  @NonNull
  public static <T> Stream<Property<T, ?>> propertyStreamFromClass(
      @NonNull TypeToken<T> typeToken,
      @NonNull MethodNameStyle... methodNameStyles) {

    ImmutableListMultimap<@NonNull ImmutableList<?>, PropertyElement<T, ?>> signatureToPropertyElements =
        propertyElementStreamFromClass(typeToken, methodNameStyles)
            .collect(ImmutableListMultimap
                .toImmutableListMultimap(KEY_FUNCTION, Function.identity()));

    return signatureToPropertyElements.asMap().values().stream()
        .map(propertyElements -> propertyElements.stream().map(o -> (PropertyElement<T, ?>) o))
        .map(Property::create);
  }

  @NonNull
  public static ImmutableSet<Property<?, ?>> propertiesFromClass(
      @NonNull Type type, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {
    return propertyStreamFromClass(type, methodNameStyles)
        .filter(property -> !fieldExistCheck || property.fieldExist())
        .collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<Property<T, ?>> propertiesFromClass(
      @NonNull Class<T> clazz, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {
    return propertyStreamFromClass(clazz, methodNameStyles)
        .filter(property -> !fieldExistCheck || property.fieldExist())
        .collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<Property<T, ?>> propertiesFromClass(
      @NonNull TypeToken<T> typeToken, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {
    return propertyStreamFromClass(typeToken, methodNameStyles)
        .filter(property -> !fieldExistCheck || property.fieldExist())
        .collect(ImmutableSet.toImmutableSet());
  }

  /**
   * get all properties from class
   *
   * @param typeToken target class
   * @param methodNameStyles get set methods styles
   * @return {@link Stream} of all properties
   * @author caotc
   * @date 2019-11-28
   * @apiNote
   * @since 1.0.0
   */
  @NonNull
  public static Stream<ReadableProperty<?, ?>> readablePropertyStreamFromClass(
      @NonNull Type typeToken,
      @NonNull MethodNameStyle... methodNameStyles) {
    return readablePropertyStreamFromClass(TypeToken.of(typeToken), methodNameStyles)
        .map(property -> (ReadableProperty<?, ?>) property);
  }

  /**
   * get all properties from class
   *
   * @param clazz target class
   * @param methodNameStyles get set methods styles
   * @return {@link Stream} of all properties
   * @author caotc
   * @date 2019-11-28
   * @apiNote
   * @since 1.0.0
   */
  @NonNull
  public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStreamFromClass(
      @NonNull Class<T> clazz,
      @NonNull MethodNameStyle... methodNameStyles) {
    return readablePropertyStreamFromClass(TypeToken.of(clazz), methodNameStyles);
  }

  /**
   * get all properties from class
   *
   * @param typeToken target class
   * @param methodNameStyles get set methods styles
   * @return {@link Stream} of all properties
   * @author caotc
   * @date 2019-11-28
   * @apiNote
   * @since 1.0.0
   */
  @NonNull
  public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStreamFromClass(
      @NonNull TypeToken<T> typeToken,
      @NonNull MethodNameStyle... methodNameStyles) {
    return propertyStreamFromClass(typeToken, methodNameStyles).filter(Property::readable)
        .map(Property::toReadable);
  }

  /**
   * 获取指定{@link ReadableProperty}
   *
   * @param clazz 需要获取可读属性{@link ReadableProperty}的类
   * @param fieldName 属性名称
   * @return {@link ReadableProperty}
   * @throws ReadablePropertyNotFoundException readablePropertyNotFoundException
   * @author caotc
   * @date 2019-05-10
   * @apiNote included super interfaces and superclasses
   * @since 1.0.0
   */
  @NonNull
  public static <T, R> ReadableProperty<T, R> readablePropertyFromClassExact(
      @NonNull Class<T> clazz, @NonNull String fieldName) {
    return ReflectionUtil.<T, R>readablePropertyFromClass(clazz, fieldName)
        .orElseThrow(() -> ReadablePropertyNotFoundException
            .create(clazz, fieldName));
  }

  /**
   * 获取指定{@link ReadableProperty}
   *
   * @param clazz 需要获取可读属性{@link ReadableProperty}的类
   * @param fieldName 属性名称
   * @return {@link ReadableProperty}的{@link Optional}
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static <T, R> Optional<ReadableProperty<T, R>> readablePropertyFromClass(
      @NonNull Class<T> clazz, @NonNull String fieldName) {
    return readablePropertyFromClass(clazz, fieldName, false);
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有可读属性{@link ReadableProperty}集合
   *
   * @param clazz 需要获取可读属性{@link ReadableProperty}集合的类
   * @param fieldName 指定属性名称
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @return 所有可读属性 {@link ReadableProperty}集合
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static <T, R> Optional<ReadableProperty<T, R>> readablePropertyFromClass(
      @NonNull Class<T> clazz, @NonNull String fieldName, boolean fieldExistCheck) {
    return readablePropertyFromClass(clazz, fieldName, fieldExistCheck, MethodNameStyle.values());
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有可读属性{@link ReadableProperty}集合
   *
   * @param clazz 需要获取可读属性{@link ReadableProperty}的类
   * @param fieldName 指定属性名称
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @param methodNameStyles get方法格式集合
   * @return 所有可读属性 {@link ReadableProperty}集合
   * @author caotc
   * @date 2019-05-10
   * @apiNote 如果只想要获取JavaBean规范的get方法, {@code methodNameStyles}参数使用{@link
   * MethodNameStyle#JAVA_BEAN}
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public static <T, R> Optional<ReadableProperty<T, R>> readablePropertyFromClass(
      @NonNull Class<T> clazz, @NonNull String fieldName, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {
    return readablePropertiesFromClass(clazz, fieldExistCheck, methodNameStyles).stream()
        .filter(propertyGetter -> propertyGetter.name().equals(fieldName))
        .map(propertyGetter -> (ReadableProperty<T, R>) propertyGetter)
        .findAny();
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有可写属性{@link WritableProperty}集合
   *
   * @param clazz 需要获取可写属性{@link WritableProperty}的类
   * @return 所有可写属性 {@link WritableProperty}集合
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static <T> ImmutableSet<WritableProperty<T, ?>> writablePropertiesFromClass(
      @NonNull Class<T> clazz) {
    return writablePropertiesFromClass(clazz, true);
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有可写属性{@link WritableProperty}集合
   *
   * @param clazz 需要获取可写属性{@link WritableProperty}的类
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @return 所有可写属性 {@link WritableProperty}集合
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static <T> ImmutableSet<WritableProperty<T, ?>> writablePropertiesFromClass(
      @NonNull Class<T> clazz, boolean fieldExistCheck) {
    return writablePropertiesFromClass(clazz, fieldExistCheck, MethodNameStyle.values());
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有可写属性{@link WritableProperty}集合
   *
   * @param clazz 需要获取可写属性{@link WritableProperty}的类
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @param methodNameStyles 属性写方法格式集合
   * @return 所有可写属性 {@link WritableProperty}集合
   * @author caotc
   * @date 2019-05-10
   * @apiNote 如果只想要获取JavaBean规范的set方法,{@code methodNameStyles}参数使用{@link MethodNameStyle#JAVA_BEAN}
   * @since 1.0.0
   */
  @NonNull
  public static <T> ImmutableSet<WritableProperty<T, ?>> writablePropertiesFromClass(
      @NonNull Class<T> clazz, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {
    Function<PropertyWriter<T, ?>, ImmutableList<?>> propertySetterToKeyFunction = propertyWriter -> ImmutableList
        .of(propertyWriter.propertyName(), propertyWriter.propertyType());

    Stream<PropertyWriter<T, ?>> setInvokablePropertySetters = setMethodsFromClass(clazz,
        fieldExistCheck, methodNameStyles).stream()
        .flatMap(getMethod -> Arrays.stream(methodNameStyles)
            .filter(methodNameStyle -> methodNameStyle.isSetMethod(getMethod))
            .map(methodNameStyle -> PropertyWriter.from(getMethod, methodNameStyle)));

    Stream<PropertyWriter<T, ?>> fieldPropertySetters = fieldsFromClass(
        clazz).stream().map(PropertyWriter::from);

    ImmutableListMultimap<@NonNull ImmutableList<?>, PropertyWriter<T, ?>> propertySetterMultimap =
        Stream.concat(fieldPropertySetters, setInvokablePropertySetters)
            .collect(ImmutableListMultimap
                .toImmutableListMultimap(propertySetterToKeyFunction, Function.identity()));

    return propertySetterMultimap.asMap().values().stream()
        .filter(propertyWriters -> !fieldExistCheck || propertyWriters.stream()
            .anyMatch(FieldElementPropertyWriter.class::isInstance))
        .map(propertySetters -> propertySetters.stream().map(o -> (PropertyWriter<T, ?>) o))
        .map(WritableProperty::create).collect(ImmutableSet.toImmutableSet());
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有set方法与属性的包装{@link PropertyWriter}
   *
   * @param clazz 需要获取set方法的类
   * @param fieldName 指定属性名称
   * @return 包括所有超类和接口的所有set方法与属性的包装 {@link PropertyWriter}
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static <T, R> Optional<WritableProperty<T, R>> writablePropertyFromClass(
      @NonNull Class<T> clazz, @NonNull String fieldName) {
    return writablePropertyFromClass(clazz, fieldName, true);
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有set方法与属性的包装{@link PropertyWriter}
   *
   * @param clazz 需要获取set方法的类
   * @param fieldName 指定属性名称
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @return 包括所有超类和接口的所有set方法与属性的包装 {@link PropertyWriter}
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static <T, R> Optional<WritableProperty<T, R>> writablePropertyFromClass(
      @NonNull Class<T> clazz, @NonNull String fieldName, boolean fieldExistCheck) {
    return writablePropertyFromClass(clazz, fieldName, fieldExistCheck, MethodNameStyle.values());
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有set方法与属性的包装{@link PropertyWriter}
   *
   * @param clazz 需要获取set方法的类
   * @param fieldName 指定属性名称
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @param methodNameStyles set方法格式集合
   * @return 包括所有超类和接口的所有set方法与属性的包装 {@link PropertyWriter}
   * @author caotc
   * @date 2019-05-10
   * @apiNote 如果只想要获取JavaBean规范的set方法, {@code methodNameStyles}参数使用{@link
   * MethodNameStyle#JAVA_BEAN}
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public static <T, R> Optional<WritableProperty<T, R>> writablePropertyFromClass(
      @NonNull Class<T> clazz, @NonNull String fieldName, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {
    return writablePropertiesFromClass(clazz, fieldExistCheck, methodNameStyles).stream()
        .filter(propertyWriter -> propertyWriter.name().equals(fieldName))
        .map(propertyWriter -> (WritableProperty<T, R>) propertyWriter)
        .findAny();
  }

  @NonNull
  public static ImmutableSet<PropertyElement<?, ?>> propertyElementsFromClass(
      @NonNull Type type) {
    return propertyElementsFromClass(type, MethodNameStyle.values());
  }

  @NonNull
  public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElementsFromClass(
      @NonNull Class<T> clazz) {
    return propertyElementsFromClass(clazz, MethodNameStyle.values());
  }

  @NonNull
  public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElementsFromClass(
      @NonNull TypeToken<T> typeToken) {
    return propertyElementsFromClass(typeToken, MethodNameStyle.values());
  }

  @NonNull
  public static ImmutableSet<PropertyElement<?, ?>> propertyElementsFromClass(
      @NonNull Type type, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyElementStreamFromClass(type, methodNameStyles)
        .collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElementsFromClass(
      @NonNull Class<T> clazz, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyElementStreamFromClass(clazz, methodNameStyles)
        .collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElementsFromClass(
      @NonNull TypeToken<T> typeToken, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyElementStreamFromClass(typeToken, methodNameStyles)
        .collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static Stream<PropertyElement<?, ?>> propertyElementStreamFromClass(
      @NonNull Type type) {
    return propertyElementStreamFromClass(type, MethodNameStyle.values());
  }

  @NonNull
  public static <T> Stream<PropertyElement<T, ?>> propertyElementStreamFromClass(
      @NonNull Class<T> clazz) {
    return propertyElementStreamFromClass(clazz, MethodNameStyle.values());
  }

  @NonNull
  public static <T> Stream<PropertyElement<T, ?>> propertyElementStreamFromClass(
      @NonNull TypeToken<T> typeToken) {
    return propertyElementStreamFromClass(typeToken, MethodNameStyle.values());
  }

  @NonNull
  public static Stream<PropertyElement<?, ?>> propertyElementStreamFromClass(
      @NonNull Type type, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyElementStreamFromClass(TypeToken.of(type), methodNameStyles)
        .map(propertyElement -> (PropertyElement<?, ?>) propertyElement);
  }

  @NonNull
  public static <T> Stream<PropertyElement<T, ?>> propertyElementStreamFromClass(
      @NonNull Class<T> clazz, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyElementStreamFromClass(TypeToken.of(clazz), methodNameStyles);
  }

  @NonNull
  public static <T> Stream<PropertyElement<T, ?>> propertyElementStreamFromClass(
      @NonNull TypeToken<T> typeToken, @NonNull MethodNameStyle... methodNameStyles) {

    Stream<PropertyElement<T, ?>> propertyElementStream = methodInvokableStreamFromClass(typeToken)
        .flatMap(invokable -> Arrays.stream(methodNameStyles)
            .filter(methodNameStyle -> methodNameStyle.isSetInvokable(invokable) || methodNameStyle
                .isGetInvokable(invokable))
            .map(methodNameStyle -> PropertyElement.from(invokable, methodNameStyle)));

    return Stream
        .concat(fieldStreamFromClass(typeToken).map(PropertyElement::from), propertyElementStream);
  }

  @NonNull
  public static ImmutableSet<PropertyReader<?, ?>> propertyReadersFromClass(
      @NonNull Type type) {
    return propertyReadersFromClass(type, MethodNameStyle.values());
  }

  @NonNull
  public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReadersFromClass(
      @NonNull Class<T> clazz) {
    return propertyReadersFromClass(clazz, MethodNameStyle.values());
  }

  @NonNull
  public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReadersFromClass(
      @NonNull TypeToken<T> typeToken) {
    return propertyReadersFromClass(typeToken, MethodNameStyle.values());
  }

  @NonNull
  public static ImmutableSet<PropertyReader<?, ?>> propertyReadersFromClass(
      @NonNull Type type, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyReaderStreamFromClass(type, methodNameStyles)
        .collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReadersFromClass(
      @NonNull Class<T> clazz, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyReaderStreamFromClass(clazz, methodNameStyles)
        .collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReadersFromClass(
      @NonNull TypeToken<T> typeToken, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyReaderStreamFromClass(typeToken, methodNameStyles)
        .collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static Stream<PropertyReader<?, ?>> propertyReaderStreamFromClass(
      @NonNull Type type) {
    return propertyReaderStreamFromClass(type, MethodNameStyle.values());
  }

  @NonNull
  public static <T> Stream<PropertyReader<T, ?>> propertyReaderStreamFromClass(
      @NonNull Class<T> clazz) {
    return propertyReaderStreamFromClass(clazz, MethodNameStyle.values());
  }

  @NonNull
  public static <T> Stream<PropertyReader<T, ?>> propertyReaderStreamFromClass(
      @NonNull TypeToken<T> typeToken) {
    return propertyReaderStreamFromClass(typeToken, MethodNameStyle.values());
  }

  @NonNull
  public static Stream<PropertyReader<?, ?>> propertyReaderStreamFromClass(
      @NonNull Type type, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyReaderStreamFromClass(TypeToken.of(type), methodNameStyles)
        .map(propertyReader -> (PropertyReader<?, ?>) propertyReader);
  }

  @NonNull
  public static <T> Stream<PropertyReader<T, ?>> propertyReaderStreamFromClass(
      @NonNull Class<T> clazz, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyReaderStreamFromClass(TypeToken.of(clazz), methodNameStyles);
  }

  @NonNull
  public static <T> Stream<PropertyReader<T, ?>> propertyReaderStreamFromClass(
      @NonNull TypeToken<T> typeToken, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyElementStreamFromClass(typeToken, methodNameStyles)
        .filter(PropertyElement::isReader).map(PropertyElement::toReader);
  }

  @NonNull
  public static ImmutableSet<PropertyWriter<?, ?>> propertyWritersFromClass(
      @NonNull Type type) {
    return propertyWritersFromClass(type, MethodNameStyle.values());
  }

  @NonNull
  public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWritersFromClass(
      @NonNull Class<T> clazz) {
    return propertyWritersFromClass(clazz, MethodNameStyle.values());
  }

  @NonNull
  public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWritersFromClass(
      @NonNull TypeToken<T> typeToken) {
    return propertyWritersFromClass(typeToken, MethodNameStyle.values());
  }

  @NonNull
  public static ImmutableSet<PropertyWriter<?, ?>> propertyWritersFromClass(
      @NonNull Type type, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyWriterStreamFromClass(type, methodNameStyles)
        .collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWritersFromClass(
      @NonNull Class<T> clazz, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyWriterStreamFromClass(clazz, methodNameStyles)
        .collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWritersFromClass(
      @NonNull TypeToken<T> typeToken, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyWriterStreamFromClass(typeToken, methodNameStyles)
        .collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static Stream<PropertyWriter<?, ?>> propertyWriterStreamFromClass(
      @NonNull Type type) {
    return propertyWriterStreamFromClass(type, MethodNameStyle.values());
  }

  @NonNull
  public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStreamFromClass(
      @NonNull Class<T> clazz) {
    return propertyWriterStreamFromClass(clazz, MethodNameStyle.values());
  }

  @NonNull
  public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStreamFromClass(
      @NonNull TypeToken<T> typeToken) {
    return propertyWriterStreamFromClass(typeToken, MethodNameStyle.values());
  }

  @NonNull
  public static Stream<PropertyWriter<?, ?>> propertyWriterStreamFromClass(
      @NonNull Type type, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyWriterStreamFromClass(TypeToken.of(type), methodNameStyles)
        .map(propertyWriter -> (PropertyWriter<?, ?>) propertyWriter);
  }

  @NonNull
  public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStreamFromClass(
      @NonNull Class<T> clazz, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyWriterStreamFromClass(TypeToken.of(clazz), methodNameStyles);
  }

  @NonNull
  public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStreamFromClass(
      @NonNull TypeToken<T> typeToken, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyElementStreamFromClass(typeToken, methodNameStyles)
        .filter(PropertyElement::isWriter).map(PropertyElement::toWriter);
  }

  @NonNull
  public static ImmutableSet<PropertyAccessor<?, ?>> propertyAccessorsFromClass(
      @NonNull Type type) {
    return propertyAccessorsFromClass(type, MethodNameStyle.values());
  }

  @NonNull
  public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessorsFromClass(
      @NonNull Class<T> clazz) {
    return propertyAccessorsFromClass(clazz, MethodNameStyle.values());
  }

  @NonNull
  public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessorsFromClass(
      @NonNull TypeToken<T> typeToken) {
    return propertyAccessorsFromClass(typeToken, MethodNameStyle.values());
  }

  @NonNull
  public static ImmutableSet<PropertyAccessor<?, ?>> propertyAccessorsFromClass(
      @NonNull Type type, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyAccessorStreamFromClass(type, methodNameStyles)
        .collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessorsFromClass(
      @NonNull Class<T> clazz, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyAccessorStreamFromClass(clazz, methodNameStyles)
        .collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessorsFromClass(
      @NonNull TypeToken<T> typeToken, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyAccessorStreamFromClass(typeToken, methodNameStyles)
        .collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public static Stream<PropertyAccessor<?, ?>> propertyAccessorStreamFromClass(
      @NonNull Type type) {
    return propertyAccessorStreamFromClass(type, MethodNameStyle.values());
  }

  @NonNull
  public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStreamFromClass(
      @NonNull Class<T> clazz) {
    return propertyAccessorStreamFromClass(clazz, MethodNameStyle.values());
  }

  @NonNull
  public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStreamFromClass(
      @NonNull TypeToken<T> typeToken) {
    return propertyAccessorStreamFromClass(typeToken, MethodNameStyle.values());
  }

  @NonNull
  public static Stream<PropertyAccessor<?, ?>> propertyAccessorStreamFromClass(
      @NonNull Type type, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyAccessorStreamFromClass(TypeToken.of(type), methodNameStyles)
        .map(propertyAccessor -> (PropertyAccessor<?, ?>) propertyAccessor);
  }

  @NonNull
  public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStreamFromClass(
      @NonNull Class<T> clazz, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyAccessorStreamFromClass(TypeToken.of(clazz), methodNameStyles);
  }

  @NonNull
  public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStreamFromClass(
      @NonNull TypeToken<T> typeToken, @NonNull MethodNameStyle... methodNameStyles) {
    return propertyElementStreamFromClass(typeToken, methodNameStyles)
        .filter(PropertyElement::isAccessor).map(PropertyElement::toAccessor);
  }

  /**
   * 检查传入方法是否是get方法
   *
   * @param method 需要检查的方法
   * @param methodNameStyles 方法风格
   * @return 是否是get方法
   * @author caotc
   * @date 2019-05-23
   * @apiNote 这里所指的get方法并不是专指JavaBean规范的get方法, 而是所有获取属性的方法, 符合任意{@link MethodNameStyle}检查即视为get方法.
   * 所以如果只想要判断是否是JavaBean规范的get方法请使用 {@link MethodNameStyle#JAVA_BEAN#isGetMethod(Method)}
   * @since 1.0.0
   */
  public static boolean isPropertyReader(@NonNull Method method,
      @NonNull MethodNameStyle... methodNameStyles) {
    return isPropertyReader(Invokable.from(method), methodNameStyles);
  }

  /**
   * @author caotc
   * @date 2019-12-08
   * @since 1.0.0
   */
  public static boolean isPropertyReader(@NonNull Invokable<?, ?> invokable) {
    return isPropertyReader(invokable, MethodNameStyle.values());
  }

  /**
   * 检查传入方法是否是get方法
   *
   * @param invokable 需要检查的方法
   * @param methodNameStyles 方法风格
   * @return 是否是get方法
   * @author caotc
   * @date 2019-05-23
   * @apiNote 这里所指的get方法并不是专指JavaBean规范的get方法, 而是所有获取属性的方法, 符合任意{@link MethodNameStyle}检查即视为get方法.
   * 所以如果只想要判断是否是JavaBean规范的get方法请使用 {@link MethodNameStyle#JAVA_BEAN#isPropertyReader(Invokable)}
   * @since 1.0.0
   */
  public static boolean isPropertyReader(@NonNull Invokable<?, ?> invokable,
      @NonNull MethodNameStyle... methodNameStyles) {
    return Arrays.stream(methodNameStyles)
        .anyMatch(methodNameStyle -> methodNameStyle.isGetInvokable(invokable));
  }

  /**
   * @author caotc
   * @date 2019-12-08
   * @implNote
   * @implSpec
   * @apiNote 非static的final属性只有在值为固定值时会因为编译器优化导致get方法无法读取到修改后的值
   * @since 1.0.0
   */
  public static boolean isPropertyWriter(@NonNull Field field) {
    return isPropertyWriter(FieldElement.from(field));
  }

  /**
   * @author caotc
   * @date 2019-12-08
   * @implNote
   * @implSpec
   * @apiNote 非static的final属性只有在值为固定值时会因为编译器优化导致get方法无法读取到修改后的值
   * @since 1.0.0
   */
  public static boolean isPropertyWriter(@NonNull FieldElement<?, ?> fieldElement) {
    return !fieldElement.isFinal() || !fieldElement.isStatic();
  }

  /**
   * @author caotc
   * @date 2019-12-05
   * @since 1.0.0
   */
  public static boolean isPropertyWriter(@NonNull Method method) {
    return isPropertyWriter(method, MethodNameStyle.values());
  }

  /**
   * 检查传入方法是否是set方法
   *
   * @param method 需要检查的方法
   * @param methodNameStyles 方法风格
   * @return 是否是set方法
   * @author caotc
   * @date 2019-05-23
   * @apiNote 这里所指的set方法并不是专指JavaBean规范的set方法, 而是所有获取属性的方法, 符合任意{@link MethodNameStyle}检查即视为set方法.
   * 所以如果只想要判断是否是JavaBean规范的set方法请使用 {@link MethodNameStyle#JAVA_BEAN#isPropertyWriter(Method)}
   * @since 1.0.0
   */
  public static boolean isPropertyWriter(@NonNull Method method,
      @NonNull MethodNameStyle... methodNameStyles) {
    return isPropertyWriter(Invokable.from(method), methodNameStyles);
  }

  /**
   * @author caotc
   * @date 2019-12-05
   * @since 1.0.0
   */
  public static boolean isPropertyWriter(@NonNull Invokable<?, ?> invokable) {
    return isPropertyWriter(invokable, MethodNameStyle.values());
  }

  /**
   * 检查传入方法是否是set方法
   *
   * @param invokable 需要检查的方法
   * @param methodNameStyles 方法风格
   * @return 是否是set方法
   * @author caotc
   * @date 2019-05-23
   * @apiNote 这里所指的set方法并不是专指JavaBean规范的set方法, 而是所有获取属性的方法, 符合任意{@link MethodNameStyle}检查即视为set方法.
   * 所以如果只想要判断是否是JavaBean规范的set方法请使用 {@link MethodNameStyle#JAVA_BEAN#isPropertyWriter(Invokable)}
   * @since 1.0.0
   */
  public static boolean isPropertyWriter(@NonNull Invokable<?, ?> invokable,
      @NonNull MethodNameStyle... methodNameStyles) {
    return Arrays.stream(methodNameStyles)
        .anyMatch(methodNameStyle -> methodNameStyle.isSetInvokable(invokable));
  }

  /**
   * @author caotc
   * @date 2019-12-08
   * @since 1.0.0
   */
  public static boolean isGetMethod(@NonNull Method method) {
    return isGetInvokable(Invokable.from(method));
  }

  /**
   * @author caotc
   * @date 2019-12-08
   * @since 1.0.0
   */
  public static boolean isGetInvokable(@NonNull Invokable<?, ?> invokable) {
    return isPropertyReader(invokable, MethodNameStyle.JAVA_BEAN);
  }

  /**
   * @author caotc
   * @date 2019-12-08
   * @since 1.0.0
   */
  public static boolean isSetMethod(@NonNull Method method) {
    return isSetInvokable(Invokable.from(method));
  }

  /**
   * @author caotc
   * @date 2019-12-08
   * @since 1.0.0
   */
  public static boolean isSetInvokable(@NonNull Invokable<?, ?> invokable) {
    return isPropertyWriter(invokable, MethodNameStyle.JAVA_BEAN);
  }

  public static boolean isOverride(@NonNull Method method) {
    return isOverride(Invokable.from(method));
  }

  public static boolean isOverride(@NonNull Invokable<?, ?> invokable) {
    return invokable.getOwnerType().getTypes().stream()
        .filter(typeToken -> !typeToken.equals(invokable.getOwnerType()))
        .anyMatch(superTypeToken -> isOverride(invokable, superTypeToken));
  }

  public static boolean isOverride(@NonNull Method method,
      @NonNull Type superType) {
    return isOverride(Invokable.from(method), TypeToken.of(superType));
  }

  public static boolean isOverride(@NonNull Method method,
      @NonNull Class<?> superClass) {
    return isOverride(Invokable.from(method), TypeToken.of(superClass));
  }

  public static boolean isOverride(@NonNull Method method,
      @NonNull TypeToken<?> superTypeToken) {
    return isOverride(Invokable.from(method), superTypeToken);
  }

  public static boolean isOverride(@NonNull Invokable<?, ?> invokable,
      @NonNull Type superType) {
    return isOverride(invokable, TypeToken.of(superType));
  }

  public static boolean isOverride(@NonNull Invokable<?, ?> invokable,
      @NonNull Class<?> superClass) {
    return isOverride(invokable, TypeToken.of(superClass));
  }

  public static boolean isOverride(@NonNull Invokable<?, ?> invokable,
      @NonNull TypeToken<?> superTypeToken) {
    return ReflectionUtil.methodInvokableStreamFromClass(superTypeToken)
        .anyMatch(superMethodInvokable -> isOverride(invokable, superMethodInvokable));
  }

  public static boolean isOverride(@NonNull Method method,
      @NonNull Method superMethod) {
    return isOverride(Invokable.from(method), Invokable.from(superMethod));
  }

  public static boolean isOverride(@NonNull Invokable<?, ?> invokable,
      @NonNull Invokable<?, ?> superInvokable) {
    return superInvokable.isOverridable()
        && superInvokable.getOwnerType().isSupertypeOf(invokable.getOwnerType())
        && MethodSignature.from(superInvokable).equals(MethodSignature.from(invokable));
  }

  @Value
  private static class MethodSignature {

    @NonNull
    static MethodSignature from(@NonNull Method method) {
      return new MethodSignature(method);
    }

    @NonNull
    static MethodSignature from(@NonNull Invokable<?, ?> invokable) {
      return new MethodSignature(invokable);
    }

    @NonNull
    String name;
    @NonNull
    Class<?>[] parameterTypes;

    private MethodSignature(@NonNull Method method) {
      this.name = method.getName();
      this.parameterTypes = method.getParameterTypes();
    }

    private MethodSignature(@NonNull Invokable<?, ?> invokable) {
      this.name = invokable.getName();
      this.parameterTypes = invokable.getParameters().stream().map(Parameter::getType)
          .map(TypeToken::getRawType)
          .toArray(Class<?>[]::new);
    }
  }
}
