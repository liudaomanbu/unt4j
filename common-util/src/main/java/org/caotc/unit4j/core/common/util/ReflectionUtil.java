/*
 * Copyright (C) 2021 the original author or authors.
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
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.Invokable;
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
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.reflect.FieldElement;
import org.caotc.unit4j.core.common.reflect.MethodSignature;
import org.caotc.unit4j.core.common.reflect.PropertyName;
import org.caotc.unit4j.core.common.reflect.property.AccessibleProperty;
import org.caotc.unit4j.core.common.reflect.property.Property;
import org.caotc.unit4j.core.common.reflect.property.ReadableProperty;
import org.caotc.unit4j.core.common.reflect.property.WritableProperty;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyAccessor;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyAccessorMethodFormat;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyElement;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyWriter;
import org.caotc.unit4j.core.exception.AccessiblePropertyNotFoundException;
import org.caotc.unit4j.core.exception.MethodNotFoundException;
import org.caotc.unit4j.core.exception.ReadablePropertyNotFoundException;
import org.caotc.unit4j.core.exception.WritablePropertyNotFoundException;
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

    private static final PropertyAccessorMethodFormat[] DEFAULT_METHOD_NAME_STYLES = PropertyAccessorMethodFormat
        .values();
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

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<FieldElement<T, ?>> fieldElementStreamFromClass(
        @NonNull Type type) {
        return fieldElementStreamFromClass((TypeToken<T>) TypeToken.of(type));
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

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<Invokable<T, ?>> methodInvokablesFromClass(
        @NonNull Type type) {
        return methodInvokablesFromClass((TypeToken<T>) TypeToken.of(type));
    }

    @NonNull
    public static <T> ImmutableSet<Invokable<T, ?>> methodInvokablesFromClass(
        @NonNull Class<T> clazz) {
        return methodInvokablesFromClass(TypeToken.of(clazz));
    }

    @NonNull
    public static <T> ImmutableSet<Invokable<T, ?>> methodInvokablesFromClass(
        @NonNull TypeToken<T> typeToken) {
        return methodInvokableStreamFromClass(typeToken).collect(ImmutableSet.toImmutableSet());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<Invokable<T, ?>> methodInvokableStreamFromClass(
        @NonNull Type type) {
        return methodInvokableStreamFromClass((TypeToken<T>) TypeToken.of(type));
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

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<Constructor<T>> constructorStreamFromClass(@NonNull Type type) {
        return constructorStreamFromClass((TypeToken<T>) TypeToken.of(type));
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

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<Invokable<T, T>> constructorInvokablesFromClass(
        @NonNull Type type) {
        return constructorInvokablesFromClass((TypeToken<T>) TypeToken.of(type));
    }

    @NonNull
    public static <T> ImmutableSet<Invokable<T, T>> constructorInvokablesFromClass(
        @NonNull Class<T> clazz) {
        return constructorInvokableStreamFromClass(clazz).collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<Invokable<T, T>> constructorInvokablesFromClass(
        @NonNull TypeToken<T> typeToken) {
        return constructorInvokableStreamFromClass(typeToken)
            .collect(ImmutableSet.toImmutableSet());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<Invokable<T, T>> constructorInvokableStreamFromClass(
        @NonNull Type type) {
        return constructorInvokableStreamFromClass((TypeToken<T>) TypeToken.of(type));
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

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<Invokable<T, ?>> invokablesFromClass(
        @NonNull Type type) {
        return invokablesFromClass((TypeToken<T>) TypeToken.of(type));
    }

    @NonNull
    public static <T> ImmutableSet<Invokable<T, ?>> invokablesFromClass(
        @NonNull Class<T> clazz) {
        return invokablesFromClass(TypeToken.of(clazz));
    }

    @NonNull
    public static <T> ImmutableSet<Invokable<T, ?>> invokablesFromClass(
        @NonNull TypeToken<T> typeToken) {
        return invokableStreamFromClass(typeToken).collect(ImmutableSet.toImmutableSet());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<Invokable<T, ?>> invokableStreamFromClass(
        @NonNull Type type) {
        return invokableStreamFromClass((TypeToken<T>) TypeToken.of(type));
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

    @NonNull
    public ImmutableSet<Method> getMethodsFromClass(@NonNull Type type) {
        return getMethodsFromClass(TypeToken.of(type));
    }

    @NonNull
    public ImmutableSet<Method> getMethodsFromClass(@NonNull Class<?> clazz) {
        return getMethodsFromClass(TypeToken.of(clazz));
    }

    @NonNull
    public ImmutableSet<Method> getMethodsFromClass(@NonNull TypeToken<?> typeToken) {
        return getMethodStreamFromClass(typeToken)
            .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public Stream<Method> getMethodStreamFromClass(@NonNull Type type) {
        return getMethodStreamFromClass(TypeToken.of(type));
    }

    @NonNull
    public Stream<Method> getMethodStreamFromClass(@NonNull Class<?> clazz) {
        return getMethodStreamFromClass(TypeToken.of(clazz));
    }

    @NonNull
    public Stream<Method> getMethodStreamFromClass(@NonNull TypeToken<?> typeToken) {
        return methodStreamFromClass(typeToken)
            .filter(method -> isPropertyReader(method, PropertyAccessorMethodFormat.JAVA_BEAN));
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T> Invokable<T, ?> getInvokableFromClassExact(@NonNull Type type,
        @NonNull String fieldName) {
        return getInvokableFromClassExact((TypeToken<T>) TypeToken.of(type), fieldName);
    }

    @NonNull
    public <T> Invokable<T, ?> getInvokableFromClassExact(@NonNull Class<T> clazz,
        @NonNull String fieldName) {
        return getInvokableFromClassExact(TypeToken.of(clazz), fieldName);
    }

    @NonNull
    public <T> Invokable<T, ?> getInvokableFromClassExact(@NonNull TypeToken<T> typeToken,
        @NonNull String fieldName) {
        return getInvokableFromClass(typeToken, fieldName)
            .orElseThrow(
                () -> MethodNotFoundException.create(typeToken, fieldName, ImmutableList.of()));
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T> Optional<Invokable<T, ?>> getInvokableFromClass(@NonNull Type type,
        @NonNull String fieldName) {
        return getInvokableFromClass((TypeToken<T>) TypeToken.of(type), fieldName);
    }

    @NonNull
    public <T> Optional<Invokable<T, ?>> getInvokableFromClass(@NonNull Class<T> clazz,
        @NonNull String fieldName) {
        return getInvokableFromClass(TypeToken.of(clazz), fieldName);
    }

    @NonNull
    public <T> Optional<Invokable<T, ?>> getInvokableFromClass(@NonNull TypeToken<T> typeToken,
        @NonNull String fieldName) {
        return getInvokableStreamFromClass(typeToken)
            .filter(
                getInvokable -> PropertyAccessorMethodFormat.JAVA_BEAN
                    .fieldNameFromGetInvokable(getInvokable)
                    .equals(fieldName))
            .findAny();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> getInvokablesFromClass(@NonNull Type type) {
        return getInvokablesFromClass((TypeToken<T>) TypeToken.of(type));
    }

    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> getInvokablesFromClass(@NonNull Class<T> clazz) {
        return getInvokablesFromClass(TypeToken.of(clazz));
    }

    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> getInvokablesFromClass(
        @NonNull TypeToken<T> typeToken) {
        return getInvokableStreamFromClass(typeToken).collect(ImmutableSet.toImmutableSet());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T> Stream<Invokable<T, ?>> getInvokableStreamFromClass(@NonNull Type type) {
        return getInvokableStreamFromClass((TypeToken<T>) TypeToken.of(type));
    }

    @NonNull
    public <T> Stream<Invokable<T, ?>> getInvokableStreamFromClass(@NonNull Class<T> clazz) {
        return getInvokableStreamFromClass(TypeToken.of(clazz));
    }

    @NonNull
    public <T> Stream<Invokable<T, ?>> getInvokableStreamFromClass(
        @NonNull TypeToken<T> typeToken) {
        return methodInvokableStreamFromClass(typeToken)
            .filter(
                invokable -> isPropertyReader(invokable, PropertyAccessorMethodFormat.JAVA_BEAN));
    }

    @NonNull
    public ImmutableSet<Method> setMethodsFromClass(@NonNull Type type) {
        return setMethodsFromClass(TypeToken.of(type));
    }

    @NonNull
    public ImmutableSet<Method> setMethodsFromClass(@NonNull Class<?> clazz) {
        return setMethodsFromClass(TypeToken.of(clazz));
    }

    @NonNull
    public ImmutableSet<Method> setMethodsFromClass(@NonNull TypeToken<?> typeToken) {
        return setMethodStreamFromClass(typeToken)
            .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public Stream<Method> setMethodStreamFromClass(@NonNull Type type) {
        return setMethodStreamFromClass(TypeToken.of(type));
    }

    @NonNull
    public Stream<Method> setMethodStreamFromClass(@NonNull Class<?> clazz) {
        return setMethodStreamFromClass(TypeToken.of(clazz));
    }

    @NonNull
    public Stream<Method> setMethodStreamFromClass(@NonNull TypeToken<?> typeToken) {
        return methodStreamFromClass(typeToken)
            .filter(method -> isPropertyWriter(method, PropertyAccessorMethodFormat.JAVA_BEAN));
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T, R> Invokable<T, R> setInvokableFromClassExact(@NonNull Type type,
        @NonNull String fieldName, @NonNull Type parameterType) {
        return setInvokableFromClassExact((TypeToken<T>) TypeToken.of(type), fieldName,
            (TypeToken<R>) TypeToken.of(parameterType));
    }

    @NonNull
    public <T, R> Invokable<T, R> setInvokableFromClassExact(@NonNull Class<T> clazz,
        @NonNull String fieldName, @NonNull Class<R> parameterClass) {
        return setInvokableFromClassExact(TypeToken.of(clazz), fieldName,
            TypeToken.of(parameterClass));
    }

    @NonNull
    public <T, R> Invokable<T, R> setInvokableFromClassExact(@NonNull TypeToken<T> typeToken,
        @NonNull String fieldName, @NonNull TypeToken<R> parameterTypeToken) {
        return setInvokableFromClass(typeToken, fieldName, parameterTypeToken)
            .orElseThrow(() -> MethodNotFoundException
                .create(typeToken, fieldName, ImmutableList.of(parameterTypeToken)));
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T, R> Optional<Invokable<T, R>> setInvokableFromClass(@NonNull Type type,
        @NonNull String fieldName, @NonNull Type parameterType) {
        return setInvokableFromClass((TypeToken<T>) TypeToken.of(type), fieldName,
            (TypeToken<R>) TypeToken.of(parameterType));
    }

    @NonNull
    public <T, R> Optional<Invokable<T, R>> setInvokableFromClass(@NonNull Class<T> clazz,
        @NonNull String fieldName, @NonNull Class<R> parameterClass) {
        return setInvokableFromClass(TypeToken.of(clazz), fieldName, TypeToken.of(parameterClass));
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T, R> Optional<Invokable<T, R>> setInvokableFromClass(@NonNull TypeToken<T> typeToken,
        @NonNull String fieldName, @NonNull TypeToken<R> parameterTypeToken) {
        return setInvokableStreamFromClass(typeToken, fieldName)
            .filter(setInvokeable -> MethodSignature.from(setInvokeable).parameterTypes()
                .equals(ImmutableList.of(parameterTypeToken)))
            .findAny()
            .map(setInvokeable -> (Invokable<T, R>) setInvokeable);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> setInvokablesFromClass(@NonNull Type type,
        @NonNull String fieldName) {
        return setInvokablesFromClass((TypeToken<T>) TypeToken.of(type), fieldName);
    }

    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> setInvokablesFromClass(@NonNull Class<T> clazz,
        @NonNull String fieldName) {
        return setInvokablesFromClass(TypeToken.of(clazz), fieldName);
    }

    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> setInvokablesFromClass(@NonNull TypeToken<T> typeToken,
        @NonNull String fieldName) {
        return setInvokableStreamFromClass(typeToken, fieldName)
            .collect(ImmutableSet.toImmutableSet());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> setInvokablesFromClass(@NonNull Type type) {
        return setInvokablesFromClass((TypeToken<T>) TypeToken.of(type));
    }

    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> setInvokablesFromClass(@NonNull Class<T> clazz) {
        return setInvokablesFromClass(TypeToken.of(clazz));
    }

    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> setInvokablesFromClass(
        @NonNull TypeToken<T> typeToken) {
        return setInvokableStreamFromClass(typeToken).collect(ImmutableSet.toImmutableSet());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T> Stream<Invokable<T, ?>> setInvokableStreamFromClass(@NonNull Type type,
        @NonNull String fieldName) {
        return setInvokableStreamFromClass((TypeToken<T>) TypeToken.of(type), fieldName);
    }

    @NonNull
    public <T> Stream<Invokable<T, ?>> setInvokableStreamFromClass(@NonNull Class<T> clazz,
        @NonNull String fieldName) {
        return setInvokableStreamFromClass(TypeToken.of(clazz), fieldName);
    }

    @NonNull
    public <T> Stream<Invokable<T, ?>> setInvokableStreamFromClass(@NonNull TypeToken<T> typeToken,
        @NonNull String fieldName) {
        return setInvokableStreamFromClass(typeToken)
            .filter(setInvokable -> PropertyAccessorMethodFormat.JAVA_BEAN
                .fieldNameFromSetInvokable(setInvokable).equals(fieldName));
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T> Stream<Invokable<T, ?>> setInvokableStreamFromClass(@NonNull Type type) {
        return setInvokableStreamFromClass((TypeToken<T>) TypeToken.of(type));
    }

    @NonNull
    public <T> Stream<Invokable<T, ?>> setInvokableStreamFromClass(@NonNull Class<T> clazz) {
        return setInvokableStreamFromClass(TypeToken.of(clazz));
    }

    @NonNull
    public <T> Stream<Invokable<T, ?>> setInvokableStreamFromClass(
        @NonNull TypeToken<T> typeToken) {
        return methodInvokableStreamFromClass(typeToken)
            .filter(
                invokable -> isPropertyWriter(invokable, PropertyAccessorMethodFormat.JAVA_BEAN));
    }

    @NonNull
    public Method getMethodFromClassExact(@NonNull Type type,
        @NonNull String fieldName) {
        return getMethodFromClassExact(TypeToken.of(type), fieldName);
    }

    @NonNull
    public Method getMethodFromClassExact(@NonNull Class<?> clazz,
        @NonNull String fieldName) {
        return getMethodFromClassExact(TypeToken.of(clazz), fieldName);
    }

    @NonNull
    public Method getMethodFromClassExact(@NonNull TypeToken<?> typeToken,
        @NonNull String fieldName) {
        return getMethodFromClass(typeToken, fieldName).orElseThrow(
            () -> MethodNotFoundException.create(typeToken, fieldName, ImmutableList.of()));
    }

    @NonNull
    public Optional<Method> getMethodFromClass(@NonNull Type type,
        @NonNull String fieldName) {
        return getMethodFromClass(TypeToken.of(type), fieldName);
    }

    @NonNull
    public Optional<Method> getMethodFromClass(@NonNull Class<?> clazz,
        @NonNull String fieldName) {
        return getMethodFromClass(TypeToken.of(clazz), fieldName);
    }

    @NonNull
    public Optional<Method> getMethodFromClass(@NonNull TypeToken<?> typeToken,
        @NonNull String fieldName) {
        return getMethodStreamFromClass(typeToken)
            .filter(getMethod -> fieldName
                .equals(PropertyAccessorMethodFormat.JAVA_BEAN.fieldNameFromGetMethod(getMethod)))
            .findAny();
    }

    @NonNull
    public Method setMethodFromClassExact(@NonNull Type type,
        @NonNull String fieldName) {
        return setMethodFromClassExact(TypeToken.of(type), fieldName);
    }

    @NonNull
    public Method setMethodFromClassExact(@NonNull Class<?> clazz,
        @NonNull String fieldName) {
        return setMethodFromClassExact(TypeToken.of(clazz), fieldName);
    }

    @NonNull
    public Method setMethodFromClassExact(@NonNull TypeToken<?> typeToken,
        @NonNull String fieldName) {
        ImmutableSet<Method> setMethods = setMethodsFromClass(typeToken, fieldName);
        Preconditions
            .checkArgument(setMethods.size() > 1, "set method that named %s is not only",
                fieldName);
        return setMethods.stream().findAny()
            .orElseThrow(
                () -> MethodNotFoundException.create(typeToken, fieldName, ImmutableList.of()));
    }

    @NonNull
    public Method setMethodFromClassExact(@NonNull Type type,
        @NonNull String fieldName, @NonNull Type argumentType) {
        return setMethodFromClassExact(TypeToken.of(type), fieldName, TypeToken.of(argumentType));
    }

    @NonNull
    public Method setMethodFromClassExact(@NonNull Class<?> clazz,
        @NonNull String fieldName, @NonNull Class<?> argumentClass) {
        return setMethodFromClassExact(TypeToken.of(clazz), fieldName, TypeToken.of(argumentClass));
    }

    @NonNull
    public Method setMethodFromClassExact(@NonNull TypeToken<?> typeToken,
        @NonNull String fieldName, @NonNull TypeToken<?> argumentTypeToken) {
        return setMethodFromClass(typeToken, fieldName, argumentTypeToken)
            .orElseThrow(
                () -> MethodNotFoundException.create(typeToken, fieldName, ImmutableList.of()));
    }

    @NonNull
    public Optional<Method> setMethodFromClass(@NonNull Type type,
        @NonNull String fieldName, @NonNull Type argumentType) {
        return setMethodFromClass(TypeToken.of(type), fieldName, TypeToken.of(argumentType));
    }

    @NonNull
    public Optional<Method> setMethodFromClass(@NonNull Class<?> clazz,
        @NonNull String fieldName, @NonNull Class<?> argumentClass) {
        return setMethodFromClass(TypeToken.of(clazz), fieldName, TypeToken.of(argumentClass));
    }

    @NonNull
    public Optional<Method> setMethodFromClass(@NonNull TypeToken<?> typeToken,
        @NonNull String fieldName, @NonNull TypeToken<?> argumentTypeToken) {
        return setMethodStreamFromClass(typeToken, fieldName)
            .filter(
                setMethod -> setMethod.getParameterTypes()[0]
                    .equals(argumentTypeToken.getRawType()))
            .findAny();
    }

    @NonNull
    public ImmutableSet<Method> setMethodsFromClass(@NonNull Type type,
        @NonNull String fieldName) {
        return setMethodsFromClass(TypeToken.of(type), fieldName);
    }

    @NonNull
    public ImmutableSet<Method> setMethodsFromClass(@NonNull Class<?> clazz,
        @NonNull String fieldName) {
        return setMethodsFromClass(TypeToken.of(clazz), fieldName);
    }

    @NonNull
    public ImmutableSet<Method> setMethodsFromClass(@NonNull TypeToken<?> typeToken,
        @NonNull String fieldName) {
        return setMethodStreamFromClass(typeToken, fieldName)
            .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public Stream<Method> setMethodStreamFromClass(@NonNull Type type,
        @NonNull String fieldName) {
        return setMethodStreamFromClass(TypeToken.of(type), fieldName);
    }

    @NonNull
    public Stream<Method> setMethodStreamFromClass(@NonNull Class<?> clazz,
        @NonNull String fieldName) {
        return setMethodStreamFromClass(TypeToken.of(clazz), fieldName);
    }

    @NonNull
    public Stream<Method> setMethodStreamFromClass(@NonNull TypeToken<?> typeToken,
        @NonNull String fieldName) {
        return setMethodStreamFromClass(typeToken)
            .filter(setMethod -> fieldName
                .equals(PropertyAccessorMethodFormat.JAVA_BEAN.fieldNameFromSetMethod(setMethod)));
    }

    /**
     * get all properties from class
     *
     * @param type target class
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<Property<T, ?>> propertyStreamFromClass(
        @NonNull Type type) {
        return propertyStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param type target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<Property<T, ?>> propertyStreamFromClass(
        @NonNull Type type,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStreamFromClass((TypeToken<T>) TypeToken.of(type),
            propertyAccessorMethodFormats);
    }

    /**
     * get all properties from class
     *
     * @param clazz target class
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<Property<T, ?>> propertyStreamFromClass(
        @NonNull Class<T> clazz) {
        return propertyStreamFromClass(clazz, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param clazz target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<Property<T, ?>> propertyStreamFromClass(
        @NonNull Class<T> clazz,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStreamFromClass(TypeToken.of(clazz), propertyAccessorMethodFormats);
    }

    /**
     * get all properties from class
     *
     * @param typeToken target class
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<Property<T, ?>> propertyStreamFromClass(
        @NonNull TypeToken<T> typeToken) {
        return propertyStreamFromClass(typeToken, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param typeToken target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<Property<T, ?>> propertyStreamFromClass(
        @NonNull TypeToken<T> typeToken,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {

        ImmutableListMultimap<@NonNull ImmutableList<?>, PropertyElement<T, ?>> signatureToPropertyElements =
            propertyElementStreamFromClass(typeToken, propertyAccessorMethodFormats)
                .collect(ImmutableListMultimap
                    .toImmutableListMultimap(KEY_FUNCTION, Function.identity()));

        return signatureToPropertyElements.asMap().values().stream()
            .map(propertyElements -> propertyElements.stream().map(o -> (PropertyElement<T, ?>) o))
            .map(Property::create);
    }

    @NonNull
    public static <T> ImmutableSet<Property<T, ?>> propertiesFromClass(
        @NonNull Type type) {
        return propertiesFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<Property<T, ?>> propertiesFromClass(
        @NonNull Type type,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertiesFromClass((TypeToken<T>) TypeToken.of(type),
            propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> ImmutableSet<Property<T, ?>> propertiesFromClass(
        @NonNull Class<T> clazz) {
        return propertiesFromClass(clazz, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<Property<T, ?>> propertiesFromClass(
        @NonNull Class<T> clazz,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStreamFromClass(clazz, propertyAccessorMethodFormats)
            .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<Property<T, ?>> propertiesFromClass(
        @NonNull TypeToken<T> typeToken) {
        return propertiesFromClass(typeToken, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<Property<T, ?>> propertiesFromClass(
        @NonNull TypeToken<T> typeToken,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStreamFromClass(typeToken, propertyAccessorMethodFormats)
            .collect(ImmutableSet.toImmutableSet());
    }

    /**
     * get all properties from class
     *
     * @param type target class
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStreamFromClass(
        @NonNull Type type) {
        return readablePropertyStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param type target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStreamFromClass(
        @NonNull Type type,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyStreamFromClass((TypeToken<T>) TypeToken.of(type),
            propertyAccessorMethodFormats);
    }

    /**
     * get all properties from class
     *
     * @param clazz target class
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStreamFromClass(
        @NonNull Class<T> clazz) {
        return readablePropertyStreamFromClass(clazz, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param clazz target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStreamFromClass(
        @NonNull Class<T> clazz,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyStreamFromClass(TypeToken.of(clazz), propertyAccessorMethodFormats);
    }

    /**
     * get all properties from class
     *
     * @param typeToken target class
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStreamFromClass(
        @NonNull TypeToken<T> typeToken) {
        return readablePropertyStreamFromClass(typeToken, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param typeToken target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStreamFromClass(
        @NonNull TypeToken<T> typeToken,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStreamFromClass(typeToken, propertyAccessorMethodFormats)
            .filter(Property::readable)
            .map(Property::toReadable);
    }


    @NonNull
    public static <T> ImmutableSet<ReadableProperty<T, ?>> readablePropertiesFromClass(
        @NonNull Type type) {
        return readablePropertiesFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<ReadableProperty<T, ?>> readablePropertiesFromClass(
        @NonNull Type type,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertiesFromClass((TypeToken<T>) TypeToken.of(type),
            propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T> ImmutableSet<ReadableProperty<T, ?>> readablePropertiesFromClass(
        @NonNull Class<T> clazz) {
        return readablePropertiesFromClass(clazz, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<ReadableProperty<T, ?>> readablePropertiesFromClass(
        @NonNull Class<T> clazz,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyStreamFromClass(clazz, propertyAccessorMethodFormats)
            .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<ReadableProperty<T, ?>> readablePropertiesFromClass(
        @NonNull TypeToken<T> typeToken) {
        return readablePropertiesFromClass(typeToken, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<ReadableProperty<T, ?>> readablePropertiesFromClass(
        @NonNull TypeToken<T> typeToken,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyStreamFromClass(typeToken, propertyAccessorMethodFormats)
            .collect(ImmutableSet.toImmutableSet());
    }

    /**
     * get all properties from class
     *
     * @param type target class
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<WritableProperty<T, ?>> writablePropertyStreamFromClass(
        @NonNull Type type) {
        return writablePropertyStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param type target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<WritableProperty<T, ?>> writablePropertyStreamFromClass(
        @NonNull Type type,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyStreamFromClass((TypeToken<T>) TypeToken.of(type),
            propertyAccessorMethodFormats);
    }

    /**
     * get all properties from class
     *
     * @param clazz target class
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<WritableProperty<T, ?>> writablePropertyStreamFromClass(
        @NonNull Class<T> clazz) {
        return writablePropertyStreamFromClass(clazz, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param clazz target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<WritableProperty<T, ?>> writablePropertyStreamFromClass(
        @NonNull Class<T> clazz,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyStreamFromClass(TypeToken.of(clazz), propertyAccessorMethodFormats);
    }

    /**
     * get all properties from class
     *
     * @param typeToken target class
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<WritableProperty<T, ?>> writablePropertyStreamFromClass(
        @NonNull TypeToken<T> typeToken) {
        return writablePropertyStreamFromClass(typeToken, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param typeToken target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<WritableProperty<T, ?>> writablePropertyStreamFromClass(
        @NonNull TypeToken<T> typeToken,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStreamFromClass(typeToken, propertyAccessorMethodFormats)
            .filter(Property::writable)
            .map(Property::toWritable);
    }


    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> writablePropertiesFromClass(
        @NonNull Type type) {
        return writablePropertiesFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> writablePropertiesFromClass(
        @NonNull Type type,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertiesFromClass((TypeToken<T>) TypeToken.of(type),
            propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> writablePropertiesFromClass(
        @NonNull Class<T> clazz) {
        return writablePropertiesFromClass(clazz, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> writablePropertiesFromClass(
        @NonNull Class<T> clazz,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyStreamFromClass(clazz, propertyAccessorMethodFormats)
            .collect(ImmutableSet.toImmutableSet());
    }


    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> writablePropertiesFromClass(
        @NonNull TypeToken<T> typeToken) {
        return writablePropertiesFromClass(typeToken, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> writablePropertiesFromClass(
        @NonNull TypeToken<T> typeToken,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyStreamFromClass(typeToken, propertyAccessorMethodFormats)
            .collect(ImmutableSet.toImmutableSet());
    }

    /**
     * get all properties from class
     *
     * @param type target class
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStreamFromClass(
        @NonNull Type type) {
        return accessiblePropertyStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param type target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStreamFromClass(
        @NonNull Type type,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyStreamFromClass((TypeToken<T>) TypeToken.of(type),
            propertyAccessorMethodFormats);
    }

    /**
     * get all properties from object
     *
     * @param object target object
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStreamFromClass(
        @NonNull T object) {
        return accessiblePropertyStreamFromClass(object, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param object target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStreamFromClass(
        @NonNull T object,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyStreamFromClass((Class<T>) object.getClass(),
            propertyAccessorMethodFormats);
    }

    /**
     * get all properties from class
     *
     * @param clazz target class
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStreamFromClass(
        @NonNull Class<T> clazz) {
        return accessiblePropertyStreamFromClass(clazz, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param clazz target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStreamFromClass(
        @NonNull Class<T> clazz,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyStreamFromClass(TypeToken.of(clazz),
            propertyAccessorMethodFormats);
    }

    /**
     * get all properties from class
     *
     * @param typeToken target class
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStreamFromClass(
        @NonNull TypeToken<T> typeToken) {
        return accessiblePropertyStreamFromClass(typeToken, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param typeToken target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStreamFromClass(
        @NonNull TypeToken<T> typeToken,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStreamFromClass(typeToken, propertyAccessorMethodFormats)
            .filter(Property::accessible)
            .map(Property::toAccessible);
    }


    @NonNull
    public static <T> ImmutableSet<AccessibleProperty<T, ?>> accessiblePropertiesFromClass(
        @NonNull Type type) {
        return accessiblePropertiesFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<AccessibleProperty<T, ?>> accessiblePropertiesFromClass(
        @NonNull Type type,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertiesFromClass((TypeToken<T>) type,
            propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T> ImmutableSet<AccessibleProperty<T, ?>> accessiblePropertiesFromClass(
        @NonNull Class<T> clazz) {
        return accessiblePropertiesFromClass(clazz, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<AccessibleProperty<T, ?>> accessiblePropertiesFromClass(
        @NonNull Class<T> clazz,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyStreamFromClass(clazz, propertyAccessorMethodFormats)
            .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<AccessibleProperty<T, ?>> accessiblePropertiesFromClass(
        @NonNull TypeToken<T> typeToken) {
        return accessiblePropertiesFromClass(typeToken, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<AccessibleProperty<T, ?>> accessiblePropertiesFromClass(
        @NonNull TypeToken<T> typeToken,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyStreamFromClass(typeToken, propertyAccessorMethodFormats)
            .collect(ImmutableSet.toImmutableSet());
    }


    @NonNull
    public static <R> ReadableProperty<?, R> readablePropertyFromClassExact(
        @NonNull Type type, @NonNull String fieldName) {
        return readablePropertyFromClassExact(type, fieldName,
            DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <R> ReadableProperty<?, R> readablePropertyFromClassExact(
        @NonNull Type type, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyFromClassExact(TypeToken.of(type), fieldName,
            propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T, R> ReadableProperty<T, R> readablePropertyFromClassExact(
        @NonNull Class<T> clazz, @NonNull String fieldName) {
        return readablePropertyFromClassExact(clazz, fieldName,
            DEFAULT_METHOD_NAME_STYLES);
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
        @NonNull Class<T> clazz, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyFromClassExact(TypeToken.of(clazz), fieldName,
            propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T, R> ReadableProperty<T, R> readablePropertyFromClassExact(
        @NonNull TypeToken<T> typeToken, @NonNull String fieldName) {
        return readablePropertyFromClassExact(typeToken, fieldName,
            DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> ReadableProperty<T, R> readablePropertyFromClassExact(
        @NonNull TypeToken<T> typeToken, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<T, R>readablePropertyFromClass(typeToken, fieldName,
            propertyAccessorMethodFormats)
            .orElseThrow(() -> ReadablePropertyNotFoundException
                .create(typeToken, fieldName));
    }


    public static <T, R> Optional<ReadableProperty<T, R>> readablePropertyFromClass(
        @NonNull Type type, @NonNull String fieldName) {
        return readablePropertyFromClass(type, fieldName, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    public static <T, R> Optional<ReadableProperty<T, R>> readablePropertyFromClass(
        @NonNull Type type, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyFromClass((TypeToken<T>) TypeToken.of(type), fieldName,
            propertyAccessorMethodFormats);
    }

    /**
     * 从传入的类中获取包括所有超类和接口的所有可读属性{@link ReadableProperty}集合
     *
     * @param clazz 需要获取可读属性{@link ReadableProperty}集合的类
     * @param fieldName 指定属性名称
     * @return 所有可读属性 {@link ReadableProperty}集合
     * @author caotc
     * @date 2019-05-10
     * @since 1.0.0
     */
    @NonNull
    public static <T, R> Optional<ReadableProperty<T, R>> readablePropertyFromClass(
        @NonNull Class<T> clazz, @NonNull String fieldName) {
        return readablePropertyFromClass(clazz, fieldName, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * 从传入的类中获取包括所有超类和接口的所有可读属性{@link ReadableProperty}集合
     *
     * @param clazz 需要获取可读属性{@link ReadableProperty}的类
     * @param fieldName 指定属性名称
     * @param propertyAccessorMethodFormats get方法格式集合
     * @return 所有可读属性 {@link ReadableProperty}集合
     * @author caotc
     * @date 2019-05-10
     * @apiNote 如果只想要获取JavaBean规范的get方法, {@code methodNameStyles}参数使用{@link
     * PropertyAccessorMethodFormat#JAVA_BEAN}
     * @since 1.0.0
     */
    @NonNull
    public static <T, R> Optional<ReadableProperty<T, R>> readablePropertyFromClass(
        @NonNull Class<T> clazz, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyFromClass(TypeToken.of(clazz), fieldName,
            propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T, R> Optional<ReadableProperty<T, R>> readablePropertyFromClass(
        @NonNull TypeToken<T> typeToken, @NonNull String fieldName) {
        return readablePropertyFromClass(typeToken, fieldName,
            DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T, R> Optional<ReadableProperty<T, R>> readablePropertyFromClass(
        @NonNull TypeToken<T> typeToken, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        PropertyName propertyName = PropertyName.from(fieldName);
        String firstTierPropertyName = propertyName.firstTier().flat();
//        if (propertyName.complex()) {
//            PropertyName sub = propertyName.sub(1);
//            return readablePropertyFromClassInternal(typeToken,
//                firstTierPropertyName,fieldExistCheck,
//                propertyAccessorMethodFormats)
//                .flatMap(f -> readablePropertyFromClassInternal(
//                     f.type(), sub.flat(), fieldExistCheck,
//                    propertyAccessorMethodFormats).map(r->f.compose(r))
//                );
//        }
        return readablePropertyFromClassInternal(typeToken,
            firstTierPropertyName,
            propertyAccessorMethodFormats);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private static <T, R> Optional<ReadableProperty<T, R>> readablePropertyFromClassInternal(
        @NonNull TypeToken<T> typeToken, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertiesFromClass(typeToken, propertyAccessorMethodFormats)
            .stream()
            .filter(propertyGetter -> propertyGetter.name().equals(fieldName))
            .map(propertyGetter -> (ReadableProperty<T, R>) propertyGetter)
            .findAny();
    }


    @NonNull
    public static <T, R> WritableProperty<T, R> writablePropertyFromClassExact(
        @NonNull Type type, @NonNull String fieldName) {
        return writablePropertyFromClassExact(type, fieldName,
            DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T, R> WritableProperty<T, R> writablePropertyFromClassExact(
        @NonNull Type type, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyFromClassExact((TypeToken<T>) TypeToken.of(type), fieldName,
            propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T, R> WritableProperty<T, R> writablePropertyFromClassExact(
        @NonNull Class<T> clazz, @NonNull String fieldName) {
        return writablePropertyFromClassExact(clazz, fieldName,
            DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> WritableProperty<T, R> writablePropertyFromClassExact(
        @NonNull Class<T> clazz, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyFromClassExact(TypeToken.of(clazz), fieldName,
            propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T, R> WritableProperty<T, R> writablePropertyFromClassExact(
        @NonNull TypeToken<T> typeToken, @NonNull String fieldName) {
        return writablePropertyFromClassExact(typeToken, fieldName,
            DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> WritableProperty<T, R> writablePropertyFromClassExact(
        @NonNull TypeToken<T> typeToken, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<T, R>writablePropertyFromClass(typeToken, fieldName,
            propertyAccessorMethodFormats)
            .orElseThrow(() -> WritablePropertyNotFoundException
                .create(typeToken, fieldName));
    }

    @NonNull
    public static <T, R> Optional<WritableProperty<T, R>> writablePropertyFromClass(
        @NonNull Type type, @NonNull String fieldName) {
        return writablePropertyFromClass(type, fieldName, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T, R> Optional<WritableProperty<T, R>> writablePropertyFromClass(
        @NonNull Type type, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyFromClass((TypeToken<T>) TypeToken.of(type), fieldName,
            propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T, R> Optional<WritableProperty<T, R>> writablePropertyFromClass(
        @NonNull Class<T> clazz, @NonNull String fieldName) {
        return writablePropertyFromClass(clazz, fieldName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> Optional<WritableProperty<T, R>> writablePropertyFromClass(
        @NonNull Class<T> clazz, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyFromClass(TypeToken.of(clazz), fieldName,
            propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T, R> Optional<WritableProperty<T, R>> writablePropertyFromClass(
        @NonNull TypeToken<T> typeToken, @NonNull String fieldName) {
        return writablePropertyFromClass(typeToken, fieldName,
            DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T, R> Optional<WritableProperty<T, R>> writablePropertyFromClass(
        @NonNull TypeToken<T> typeToken, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertiesFromClass(typeToken, propertyAccessorMethodFormats)
            .stream()
            .filter(propertyWriter -> propertyWriter.name().equals(fieldName))
            .map(propertyWriter -> (WritableProperty<T, R>) propertyWriter)
            .findAny();
    }


    @NonNull
    public static <T, R> AccessibleProperty<T, R> accessiblePropertyFromClassExact(
        @NonNull Type type, @NonNull String fieldName) {
        return accessiblePropertyFromClassExact(type, fieldName,
            DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T, R> AccessibleProperty<T, R> accessiblePropertyFromClassExact(
        @NonNull Type type, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyFromClassExact((TypeToken<T>) TypeToken.of(type), fieldName,
            propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T, R> AccessibleProperty<T, R> accessiblePropertyFromClassExact(
        @NonNull Class<T> clazz, @NonNull String fieldName) {
        return accessiblePropertyFromClassExact(clazz, fieldName,
            DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> AccessibleProperty<T, R> accessiblePropertyFromClassExact(
        @NonNull Class<T> clazz, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyFromClassExact(TypeToken.of(clazz), fieldName,
            propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T, R> AccessibleProperty<T, R> accessiblePropertyFromClassExact(
        @NonNull TypeToken<T> typeToken, @NonNull String fieldName) {
        return accessiblePropertyFromClassExact(typeToken, fieldName,
            DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> AccessibleProperty<T, R> accessiblePropertyFromClassExact(
        @NonNull TypeToken<T> typeToken, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<T, R>accessiblePropertyFromClass(typeToken, fieldName,
            propertyAccessorMethodFormats)
            .orElseThrow(() -> AccessiblePropertyNotFoundException
                .create(typeToken, fieldName));
    }

    @NonNull
    public static <T, R> Optional<AccessibleProperty<T, R>> accessiblePropertyFromClass(
        @NonNull Type type, @NonNull String fieldName) {
        return accessiblePropertyFromClass(type, fieldName,
            DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T, R> Optional<AccessibleProperty<T, R>> accessiblePropertyFromClass(
        @NonNull Type type, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyFromClass((TypeToken<T>) TypeToken.of(type), fieldName,
            propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T, R> Optional<AccessibleProperty<T, R>> accessiblePropertyFromClass(
        @NonNull Class<T> clazz, @NonNull String fieldName) {
        return accessiblePropertyFromClass(clazz, fieldName,
            DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> Optional<AccessibleProperty<T, R>> accessiblePropertyFromClass(
        @NonNull Class<T> clazz, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyFromClass(TypeToken.of(clazz), fieldName,
            propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T, R> Optional<AccessibleProperty<T, R>> accessiblePropertyFromClass(
        @NonNull TypeToken<T> typeToken, @NonNull String fieldName) {
        return accessiblePropertyFromClass(typeToken, fieldName,
            DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T, R> Optional<AccessibleProperty<T, R>> accessiblePropertyFromClass(
        @NonNull TypeToken<T> typeToken, @NonNull String fieldName,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertiesFromClass(typeToken, propertyAccessorMethodFormats)
            .stream()
            .filter(propertyWriter -> propertyWriter.name().equals(fieldName))
            .map(propertyWriter -> (AccessibleProperty<T, R>) propertyWriter)
            .findAny();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElementsFromClass(
        @NonNull Type type) {
        return propertyElementsFromClass((TypeToken<T>) TypeToken.of(type),
            DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElementsFromClass(
        @NonNull Class<T> clazz) {
        return propertyElementsFromClass(clazz, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElementsFromClass(
        @NonNull TypeToken<T> typeToken) {
        return propertyElementsFromClass(typeToken, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElementsFromClass(
        @NonNull Type type,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementsFromClass((TypeToken<T>) TypeToken.of(type),
            propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElementsFromClass(
        @NonNull Class<T> clazz,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStreamFromClass(clazz, propertyAccessorMethodFormats)
            .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElementsFromClass(
        @NonNull TypeToken<T> typeToken,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStreamFromClass(typeToken, propertyAccessorMethodFormats)
            .collect(ImmutableSet.toImmutableSet());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<PropertyElement<T, ?>> propertyElementStreamFromClass(
        @NonNull Type type) {
        return propertyElementStreamFromClass((TypeToken<T>) TypeToken.of(type),
            DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyElement<T, ?>> propertyElementStreamFromClass(
        @NonNull Class<T> clazz) {
        return propertyElementStreamFromClass(clazz, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyElement<T, ?>> propertyElementStreamFromClass(
        @NonNull TypeToken<T> typeToken) {
        return propertyElementStreamFromClass(typeToken, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<PropertyElement<T, ?>> propertyElementStreamFromClass(
        @NonNull Type type,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStreamFromClass((TypeToken<T>) TypeToken.of(type),
            propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyElement<T, ?>> propertyElementStreamFromClass(
        @NonNull Class<T> clazz,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStreamFromClass(TypeToken.of(clazz), propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyElement<T, ?>> propertyElementStreamFromClass(
        @NonNull TypeToken<T> typeToken,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {

        Stream<PropertyElement<T, ?>> propertyElementStream = methodInvokableStreamFromClass(
            typeToken)
            .flatMap(invokable -> Arrays.stream(propertyAccessorMethodFormats)
                .filter(
                    methodNameStyle -> methodNameStyle.isSetInvokable(invokable) || methodNameStyle
                        .isGetInvokable(invokable))
                .map(methodNameStyle -> PropertyElement.from(invokable, methodNameStyle)));

        return Stream
            .concat(fieldStreamFromClass(typeToken).map(PropertyElement::from),
                propertyElementStream);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReadersFromClass(
        @NonNull Type type) {
        return propertyReadersFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReadersFromClass(
        @NonNull Class<T> clazz) {
        return propertyReadersFromClass(clazz, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReadersFromClass(
        @NonNull TypeToken<T> typeToken) {
        return propertyReadersFromClass(typeToken, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReadersFromClass(
        @NonNull Type type,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyReadersFromClass((TypeToken<T>) TypeToken.of(type),
            propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReadersFromClass(
        @NonNull Class<T> clazz,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyReaderStreamFromClass(clazz, propertyAccessorMethodFormats)
            .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReadersFromClass(
        @NonNull TypeToken<T> typeToken,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyReaderStreamFromClass(typeToken, propertyAccessorMethodFormats)
            .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> Stream<PropertyReader<T, ?>> propertyReaderStreamFromClass(
        @NonNull Type type) {
        return propertyReaderStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyReader<T, ?>> propertyReaderStreamFromClass(
        @NonNull Class<T> clazz) {
        return propertyReaderStreamFromClass(clazz, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyReader<T, ?>> propertyReaderStreamFromClass(
        @NonNull TypeToken<T> typeToken) {
        return propertyReaderStreamFromClass(typeToken, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<PropertyReader<T, ?>> propertyReaderStreamFromClass(
        @NonNull Type type,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyReaderStreamFromClass((TypeToken<T>) TypeToken.of(type),
            propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyReader<T, ?>> propertyReaderStreamFromClass(
        @NonNull Class<T> clazz,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyReaderStreamFromClass(TypeToken.of(clazz), propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyReader<T, ?>> propertyReaderStreamFromClass(
        @NonNull TypeToken<T> typeToken,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStreamFromClass(typeToken, propertyAccessorMethodFormats)
            .filter(PropertyElement::isReader).map(PropertyElement::toReader);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWritersFromClass(
        @NonNull Type type) {
        return propertyWritersFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWritersFromClass(
        @NonNull Class<T> clazz) {
        return propertyWritersFromClass(clazz, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWritersFromClass(
        @NonNull TypeToken<T> typeToken) {
        return propertyWritersFromClass(typeToken, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWritersFromClass(
        @NonNull Type type,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyWritersFromClass((TypeToken<T>) TypeToken.of(type),
            propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWritersFromClass(
        @NonNull Class<T> clazz,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyWriterStreamFromClass(clazz, propertyAccessorMethodFormats)
            .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWritersFromClass(
        @NonNull TypeToken<T> typeToken,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyWriterStreamFromClass(typeToken, propertyAccessorMethodFormats)
            .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStreamFromClass(
        @NonNull Type type) {
        return propertyWriterStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStreamFromClass(
        @NonNull Class<T> clazz) {
        return propertyWriterStreamFromClass(clazz, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStreamFromClass(
        @NonNull TypeToken<T> typeToken) {
        return propertyWriterStreamFromClass(typeToken, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStreamFromClass(
        @NonNull Type type,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyWriterStreamFromClass((TypeToken<T>) TypeToken.of(type),
            propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStreamFromClass(
        @NonNull Class<T> clazz,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyWriterStreamFromClass(TypeToken.of(clazz), propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStreamFromClass(
        @NonNull TypeToken<T> typeToken,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStreamFromClass(typeToken, propertyAccessorMethodFormats)
            .filter(PropertyElement::isWriter).map(PropertyElement::toWriter);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessorsFromClass(
        @NonNull Type type) {
        return propertyAccessorsFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessorsFromClass(
        @NonNull Class<T> clazz) {
        return propertyAccessorsFromClass(clazz, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessorsFromClass(
        @NonNull TypeToken<T> typeToken) {
        return propertyAccessorsFromClass(typeToken, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessorsFromClass(
        @NonNull Type type,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyAccessorsFromClass((TypeToken<T>) TypeToken.of(type),
            propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessorsFromClass(
        @NonNull Class<T> clazz,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyAccessorStreamFromClass(clazz, propertyAccessorMethodFormats)
            .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessorsFromClass(
        @NonNull TypeToken<T> typeToken,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyAccessorStreamFromClass(typeToken, propertyAccessorMethodFormats)
            .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStreamFromClass(
        @NonNull Type type) {
        return propertyAccessorStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStreamFromClass(
        @NonNull Class<T> clazz) {
        return propertyAccessorStreamFromClass(clazz, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStreamFromClass(
        @NonNull TypeToken<T> typeToken) {
        return propertyAccessorStreamFromClass(typeToken, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStreamFromClass(
        @NonNull Type type,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyAccessorStreamFromClass((TypeToken<T>) TypeToken.of(type),
            propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStreamFromClass(
        @NonNull Class<T> clazz,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyAccessorStreamFromClass(TypeToken.of(clazz), propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStreamFromClass(
        @NonNull TypeToken<T> typeToken,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStreamFromClass(typeToken, propertyAccessorMethodFormats)
            .filter(PropertyElement::isAccessor).map(PropertyElement::toAccessor);
    }

    /**
     * 检查传入方法是否是get方法
     *
     * @param method 需要检查的方法
     * @param propertyAccessorMethodFormats 方法风格
     * @return 是否是get方法
     * @author caotc
     * @date 2019-05-23
     * @apiNote 这里所指的get方法并不是专指JavaBean规范的get方法, 而是所有获取属性的方法, 符合任意{@link
     * PropertyAccessorMethodFormat}检查即视为get方法. 所以如果只想要判断是否是JavaBean规范的get方法请使用 {@link
     * PropertyAccessorMethodFormat#JAVA_BEAN#isGetMethod(Method)}
     * @since 1.0.0
     */
    public static boolean isPropertyReader(@NonNull Method method,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return isPropertyReader(Invokable.from(method), propertyAccessorMethodFormats);
    }

    /**
     * @author caotc
     * @date 2019-12-08
     * @since 1.0.0
     */
    public static boolean isPropertyReader(@NonNull Invokable<?, ?> invokable) {
        return isPropertyReader(invokable, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * 检查传入方法是否是get方法
     *
     * @param invokable 需要检查的方法
     * @param propertyAccessorMethodFormats 方法风格
     * @return 是否是get方法
     * @author caotc
     * @date 2019-05-23
     * @apiNote 这里所指的get方法并不是专指JavaBean规范的get方法, 而是所有获取属性的方法, 符合任意{@link
     * PropertyAccessorMethodFormat}检查即视为get方法. 所以如果只想要判断是否是JavaBean规范的get方法请使用 {@link
     * PropertyAccessorMethodFormat#JAVA_BEAN#isPropertyReader(Invokable)}
     * @since 1.0.0
     */
    public static boolean isPropertyReader(@NonNull Invokable<?, ?> invokable,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return Arrays.stream(propertyAccessorMethodFormats)
            .anyMatch(
                propertyAccessorMethodFormat -> propertyAccessorMethodFormat
                    .isGetInvokable(invokable));
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
        return isPropertyWriter(method, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * 检查传入方法是否是set方法
     *
     * @param method 需要检查的方法
     * @param propertyAccessorMethodFormats 方法风格
     * @return 是否是set方法
     * @author caotc
     * @date 2019-05-23
     * @apiNote 这里所指的set方法并不是专指JavaBean规范的set方法, 而是所有获取属性的方法, 符合任意{@link
     * PropertyAccessorMethodFormat}检查即视为set方法. 所以如果只想要判断是否是JavaBean规范的set方法请使用 {@link
     * PropertyAccessorMethodFormat#JAVA_BEAN#isPropertyWriter(Method)}
     * @since 1.0.0
     */
    public static boolean isPropertyWriter(@NonNull Method method,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return isPropertyWriter(Invokable.from(method), propertyAccessorMethodFormats);
    }

    /**
     * @author caotc
     * @date 2019-12-05
     * @since 1.0.0
     */
    public static boolean isPropertyWriter(@NonNull Invokable<?, ?> invokable) {
        return isPropertyWriter(invokable, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * 检查传入方法是否是set方法
     *
     * @param invokable 需要检查的方法
     * @param propertyAccessorMethodFormats 方法风格
     * @return 是否是set方法
     * @author caotc
     * @date 2019-05-23
     * @apiNote 这里所指的set方法并不是专指JavaBean规范的set方法, 而是所有获取属性的方法, 符合任意{@link
     * PropertyAccessorMethodFormat}检查即视为set方法. 所以如果只想要判断是否是JavaBean规范的set方法请使用 {@link
     * PropertyAccessorMethodFormat#JAVA_BEAN#isPropertyWriter(Invokable)}
     * @since 1.0.0
     */
    public static boolean isPropertyWriter(@NonNull Invokable<?, ?> invokable,
        @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return Arrays.stream(propertyAccessorMethodFormats)
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
        return isPropertyReader(invokable, PropertyAccessorMethodFormat.JAVA_BEAN);
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
        return isPropertyWriter(invokable, PropertyAccessorMethodFormat.JAVA_BEAN);
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
}
