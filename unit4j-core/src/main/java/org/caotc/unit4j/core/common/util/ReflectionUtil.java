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
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.reflect.AbstractPropertyReader.FieldPropertyReader;
import org.caotc.unit4j.core.common.reflect.AbstractPropertyWriter.FieldElementPropertyWriter;
import org.caotc.unit4j.core.common.reflect.FieldElement;
import org.caotc.unit4j.core.common.reflect.MethodNameStyle;
import org.caotc.unit4j.core.common.reflect.Property;
import org.caotc.unit4j.core.common.reflect.PropertyAccessor;
import org.caotc.unit4j.core.common.reflect.PropertyElement;
import org.caotc.unit4j.core.common.reflect.PropertyReader;
import org.caotc.unit4j.core.common.reflect.PropertyWriter;
import org.caotc.unit4j.core.common.reflect.ReadableProperty;
import org.caotc.unit4j.core.common.reflect.SimpleReadableProperty;
import org.caotc.unit4j.core.common.reflect.SimpleWritableProperty;
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
      .of(propertyElement.propertyName(), propertyElement.propertyType());

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
    return TypeToken.of(clazz).getTypes().rawTypes().stream().map(Class::getDeclaredFields).flatMap(
        Arrays::stream).collect(ImmutableSet.toImmutableSet());
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
    return TypeToken.of(clazz).getTypes().rawTypes().stream().map(Class::getDeclaredMethods)
        .flatMap(Arrays::stream).collect(ImmutableSet.toImmutableSet());
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
            .anyMatch(FieldPropertyReader.class::isInstance))
        .map(propertyGetters -> propertyGetters.stream().map(o -> (PropertyReader<T, ?>) o))
        .map(SimpleReadableProperty::create).collect(ImmutableSet.toImmutableSet());
  }

  /**
   * get all properties from class
   *
   * @param clazz target class
   * @param fieldExistCheck need check field exist
   * @param methodNameStyles get set methods styles
   * @return {@link ImmutableSet} of properties
   * @author caotc
   * @date 2019-11-28
   * @apiNote
   * @since 1.0.0
   */
  @NonNull
  public static <T> ImmutableSet<Property<T, ?>> propertiesFromClass(
      @NonNull Class<T> clazz, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {

    Function<PropertyReader<T, ?>, ImmutableList<?>> propertyGetterToKeyFunction = propertyGetter -> ImmutableList
        .of(propertyGetter.propertyName(), propertyGetter.propertyType());

    Stream<PropertyReader<T, ?>> getInvokablePropertyGetters = accessorMethodsFromClass(clazz,
        fieldExistCheck, methodNameStyles).stream()
        .flatMap(getMethod -> Arrays.stream(methodNameStyles)
            .filter(methodNameStyle -> methodNameStyle.isGetMethod(getMethod))
            .map(methodNameStyle -> PropertyReader.from(getMethod, methodNameStyle)));

    Stream<PropertyAccessor<T, ?>> fieldPropertyAccessors = fieldsFromClass(
        clazz).stream().map(PropertyAccessor::from);

    ImmutableListMultimap<@NonNull ImmutableList<?>, PropertyReader<T, ?>> propertyGetterMultimap =
        Stream.concat(fieldPropertyAccessors, getInvokablePropertyGetters)
            .collect(ImmutableListMultimap
                .toImmutableListMultimap(propertyGetterToKeyFunction, Function.identity()));

    return propertyGetterMultimap.asMap().values().stream()
        .filter(propertyReaders -> !fieldExistCheck || propertyReaders.stream()
            .anyMatch(FieldPropertyReader.class::isInstance))
        .map(propertyGetters -> propertyGetters.stream().map(o -> (PropertyReader<T, ?>) o))
        .map(SimpleReadableProperty::create).collect(ImmutableSet.toImmutableSet());
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
        .map(SimpleWritableProperty::create).collect(ImmutableSet.toImmutableSet());
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
  public static <T> ImmutableSet<WritableProperty<T, ?>> propertyElementsFromClass(
      @NonNull Class<T> clazz, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {

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
                .toImmutableListMultimap(KEY_FUNCTION, Function.identity()));

    return propertySetterMultimap.asMap().values().stream()
        .filter(propertyWriters -> !fieldExistCheck || propertyWriters.stream()
            .anyMatch(FieldElementPropertyWriter.class::isInstance))
        .map(propertySetters -> propertySetters.stream().map(o -> (PropertyWriter<T, ?>) o))
        .map(SimpleWritableProperty::create).collect(ImmutableSet.toImmutableSet());
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
}
