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

import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
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
import org.caotc.unit4j.core.common.reflect.property.accessor.*;
import org.caotc.unit4j.core.exception.AccessiblePropertyNotFoundException;
import org.caotc.unit4j.core.exception.MethodNotFoundException;
import org.caotc.unit4j.core.exception.ReadablePropertyNotFoundException;
import org.caotc.unit4j.core.exception.WritablePropertyNotFoundException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//TODO 将所有方法优化到只有一次流操作

/**
 * 反射工具类
 *
 * @author caotc
 * @date 2019-05-10
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class ReflectionUtil {

    private static final PropertyAccessorMethodFormat[] DEFAULT_METHOD_NAME_STYLES = PropertyAccessorMethodFormat
            .values();
    private static final Function<PropertyElement<?, ?>, ImmutableList<?>> KEY_FUNCTION = propertyElement -> ImmutableList
            .of(propertyElement.propertyName(), /*propertyElement.propertyType(),*/ //todo remove?
                    propertyElement.isStatic());
    private static final ImmutableBiMap<Class<?>, Class<?>> PRIMITIVE_CLASS_TO_WRAPPER_CLASS = ImmutableBiMap.<Class<?>, Class<?>>builder()
            .put(byte.class, Byte.class)
            .put(short.class, Short.class)
            .put(int.class, Integer.class)
            .put(long.class, Long.class)
            .put(char.class, Character.class)
            .put(boolean.class, Boolean.class)
            .put(float.class, Float.class)
            .put(double.class, Double.class)
            .buildOrThrow();
    private static final ImmutableBiMap<Class<?>, Class<?>> WRAPPER_CLASS_TO_PRIMITIVE_CLASS = PRIMITIVE_CLASS_TO_WRAPPER_CLASS.inverse();
    private static final ImmutableBiMap<TypeToken<?>, TypeToken<?>> PRIMITIVE_TYPE_TO_WRAPPER_TYPE = PRIMITIVE_CLASS_TO_WRAPPER_CLASS.entrySet().stream()
            .collect(ImmutableBiMap.toImmutableBiMap(entry -> TypeToken.of(entry.getKey()), entry -> TypeToken.of(entry.getValue())));
    private static final ImmutableBiMap<TypeToken<?>, TypeToken<?>> WRAPPER_TYPE_TO_PRIMITIVE_TYPE = PRIMITIVE_TYPE_TO_WRAPPER_TYPE.inverse();

    @NonNull
    public static ImmutableSet<Field> fields(@NonNull Type type) {
        return fieldStream(type).collect(ImmutableSet.toImmutableSet());
    }

    /**
     * 从传入的类中获取包括所有超类和接口的所有属性
     *
     * @param type 需要获取属性的类
     * @return 包括所有超类和接口的所有属性
     * @author caotc
     * @date 2019-05-10
     * @since 1.0.0
     */
    @NonNull
    public static ImmutableSet<Field> fields(@NonNull Class<?> type) {
        return fieldStream(type).collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static ImmutableSet<Field> fields(@NonNull TypeToken<?> type) {
        return fieldStream(type).collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static Stream<Field> fieldStream(@NonNull Type type) {
        return fieldStream(TypeToken.of(type));
    }

    @NonNull
    public static Stream<Field> fieldStream(@NonNull Class<?> type) {
        return fieldStream(TypeToken.of(type));
    }

    @NonNull
    public static Stream<Field> fieldStream(@NonNull TypeToken<?> type) {
        return type.getTypes().rawTypes().stream().map(Class::getDeclaredFields).flatMap(
                Arrays::stream);
    }

    //todo need?
    @NonNull
    public static ImmutableSet<FieldElement<?, ?>> fieldElementsFromClass(
            @NonNull Type type) {
        return fieldElementStreamFromClass(type).collect(ImmutableSet.toImmutableSet());
    }

    //todo need?
    @NonNull
    public static <T> ImmutableSet<FieldElement<T, ?>> fieldElementsFromClass(
            @NonNull Class<T> type) {
        return fieldElementStreamFromClass(type).collect(ImmutableSet.toImmutableSet());
    }

    //todo need?
    @NonNull
    public static <T> ImmutableSet<FieldElement<T, ?>> fieldElementsFromClass(
            @NonNull TypeToken<T> type) {
        return fieldElementStreamFromClass(type).collect(ImmutableSet.toImmutableSet());
    }

    //todo need?
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<FieldElement<T, ?>> fieldElementStreamFromClass(
            @NonNull Type type) {
        return fieldElementStreamFromClass((TypeToken<T>) TypeToken.of(type));
    }

    //todo need?
    @NonNull
    public static <T> Stream<FieldElement<T, ?>> fieldElementStreamFromClass(
            @NonNull Class<T> type) {
        return fieldElementStreamFromClass(TypeToken.of(type));
    }

    //todo need?
    @NonNull
    public static <T> Stream<FieldElement<T, ?>> fieldElementStreamFromClass(
            @NonNull TypeToken<T> type) {
        return type.getTypes().rawTypes().stream().map(Class::getDeclaredFields).flatMap(
                Arrays::stream).map(FieldElement::of);
    }

    @NonNull
    public static ImmutableSet<Field> fields(@NonNull Type type,
                                             @NonNull String fieldName) {
        return fieldStream(type).filter(field -> fieldName.equals(field.getName()))
                .collect(ImmutableSet.toImmutableSet());
    }

    /**
     * 从传入的类中获取包括所有超类和接口的所有属性中属性名称为指定名称的属性
     *
     * @param type      需要获取属性的类
     * @param fieldName 指定属性名称
     * @return 包括所有超类和接口的所有属性中属性名称为指定名称的属性
     * @author caotc
     * @date 2019-05-22
     * @apiNote 在java中超类和字类可以定义相同名称的属性, 并且不会冲突, 因此返回的属性可能为复数
     * @since 1.0.0
     */
    @NonNull
    public static ImmutableSet<Field> fields(@NonNull Class<?> type,
                                             @NonNull String fieldName) {
        return fieldStream(type).filter(field -> fieldName.equals(field.getName()))
                .collect(ImmutableSet.toImmutableSet());
    }

    //todo nullable?
    @NonNull
    public static ImmutableSet<Field> fields(@NonNull TypeToken<?> type,
                                             @NonNull String fieldName) {
        return fieldStream(type).filter(field -> fieldName.equals(field.getName()))
                .collect(ImmutableSet.toImmutableSet());
    }

    /**
     * 从传入的类中获取包括所有超类和接口的所有方法
     *
     * @param type 需要获取方法的类
     * @return 所有方法(包括所有超类和接口的方法)
     * @author caotc
     * @date 2019-05-10
     * @since 1.0.0
     */
    @NonNull
    public static ImmutableSet<Method> methods(@NonNull Class<?> type) {
        return methodStream(type).collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static ImmutableSet<Method> methods(@NonNull Type type) {
        return methodStream(type).collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static ImmutableSet<Method> methods(@NonNull TypeToken<?> type) {
        return methodStream(type).collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static Stream<Method> methodStream(@NonNull Type type) {
        return methodStream(TypeToken.of(type));
    }

    @NonNull
    public static Stream<Method> methodStream(@NonNull Class<?> type) {
        return methodStream(TypeToken.of(type));
    }

    @NonNull
    public static Stream<Method> methodStream(@NonNull TypeToken<?> type) {
        return type.getTypes().rawTypes().stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream);
    }

    //todo Beta
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<Invokable<T, ?>> methodInvokablesFromClass(
            @NonNull Type type) {
        return methodInvokablesFromClass((TypeToken<T>) TypeToken.of(type));
    }

    //todo Beta
    @NonNull
    public static <T> ImmutableSet<Invokable<T, ?>> methodInvokablesFromClass(
            @NonNull Class<T> type) {
        return methodInvokablesFromClass(TypeToken.of(type));
    }

    //todo Beta
    @NonNull
    public static <T> ImmutableSet<Invokable<T, ?>> methodInvokablesFromClass(
            @NonNull TypeToken<T> type) {
        return methodInvokableStreamFromClass(type).collect(ImmutableSet.toImmutableSet());
    }

    //todo Beta
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<Invokable<T, ?>> methodInvokableStreamFromClass(
            @NonNull Type type) {
        return methodInvokableStreamFromClass((TypeToken<T>) TypeToken.of(type));
    }

    //todo Beta
    @NonNull
    public static <T> Stream<Invokable<T, ?>> methodInvokableStreamFromClass(
            @NonNull Class<T> type) {
        return methodInvokableStreamFromClass(TypeToken.of(type));
    }

    //todo Beta
    @NonNull
    public static <T> Stream<Invokable<T, ?>> methodInvokableStreamFromClass(
            @NonNull TypeToken<T> type) {
        return methodStream(type).map(type::method);
    }

    @NonNull
    public static ImmutableSet<Constructor<?>> constructors(@NonNull Type type) {
        return constructorStream(type).collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<Constructor<T>> constructors(@NonNull Class<T> type) {
        return constructorStream(type).collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<Constructor<T>> constructors(
            @NonNull TypeToken<T> type) {
        return constructorStream(type).collect(ImmutableSet.toImmutableSet());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<Constructor<T>> constructorStream(@NonNull Type type) {
        return constructorStream((TypeToken<T>) TypeToken.of(type));
    }

    @NonNull
    public static <T> Stream<Constructor<T>> constructorStream(@NonNull Class<T> type) {
        return constructorStream(TypeToken.of(type));
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<Constructor<T>> constructorStream(
            @NonNull TypeToken<T> type) {
        return Arrays.stream(type.getRawType().getDeclaredConstructors())
                .map(constructor -> (Constructor<T>) constructor);
    }

    //todo beta
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<Invokable<T, T>> constructorInvokablesFromClass(
            @NonNull Type type) {
        return constructorInvokablesFromClass((TypeToken<T>) TypeToken.of(type));
    }

    //todo beta
    @NonNull
    public static <T> ImmutableSet<Invokable<T, T>> constructorInvokablesFromClass(
            @NonNull Class<T> type) {
        return constructorInvokableStreamFromClass(type).collect(ImmutableSet.toImmutableSet());
    }

    //todo beta
    @NonNull
    public static <T> ImmutableSet<Invokable<T, T>> constructorInvokablesFromClass(
            @NonNull TypeToken<T> type) {
        return constructorInvokableStreamFromClass(type)
                .collect(ImmutableSet.toImmutableSet());
    }

    //todo beta
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<Invokable<T, T>> constructorInvokableStreamFromClass(
            @NonNull Type type) {
        return constructorInvokableStreamFromClass((TypeToken<T>) TypeToken.of(type));
    }

    //todo beta
    @NonNull
    public static <T> Stream<Invokable<T, T>> constructorInvokableStreamFromClass(
            @NonNull Class<T> type) {
        return constructorInvokableStreamFromClass(TypeToken.of(type));
    }

    //todo beta
    @NonNull
    public static <T> Stream<Invokable<T, T>> constructorInvokableStreamFromClass(
            @NonNull TypeToken<T> type) {
        return Arrays.stream(type.getRawType().getDeclaredConstructors())
                .map(type::constructor);
    }

    //todo beta
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<Invokable<T, ?>> invokablesFromClass(
            @NonNull Type type) {
        return invokablesFromClass((TypeToken<T>) TypeToken.of(type));
    }

    //todo beta
    @NonNull
    public static <T> ImmutableSet<Invokable<T, ?>> invokablesFromClass(
            @NonNull Class<T> type) {
        return invokablesFromClass(TypeToken.of(type));
    }

    //todo beta
    @NonNull
    public static <T> ImmutableSet<Invokable<T, ?>> invokablesFromClass(
            @NonNull TypeToken<T> type) {
        return invokableStreamFromClass(type).collect(ImmutableSet.toImmutableSet());
    }

    //todo beta
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<Invokable<T, ?>> invokableStreamFromClass(
            @NonNull Type type) {
        return invokableStreamFromClass((TypeToken<T>) TypeToken.of(type));
    }

    //todo beta
    @NonNull
    public static <T> Stream<Invokable<T, ?>> invokableStreamFromClass(
            @NonNull Class<T> type) {
        return invokableStreamFromClass(TypeToken.of(type));
    }

    //todo beta
    @NonNull
    public static <T> Stream<Invokable<T, ?>> invokableStreamFromClass(
            @NonNull TypeToken<T> type) {
        return Stream.concat(constructorInvokableStreamFromClass(type),
                methodInvokableStreamFromClass(type));
    }

    @NonNull
    public ImmutableSet<Method> getMethods(@NonNull Type type) {
        return getMethods(TypeToken.of(type));
    }

    @NonNull
    public ImmutableSet<Method> getMethods(@NonNull Class<?> type) {
        return getMethods(TypeToken.of(type));
    }

    @NonNull
    public ImmutableSet<Method> getMethods(@NonNull TypeToken<?> type) {
        return getMethodStream(type)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public Stream<Method> getMethodStream(@NonNull Type type) {
        return getMethodStream(TypeToken.of(type));
    }

    @NonNull
    public Stream<Method> getMethodStream(@NonNull Class<?> type) {
        return getMethodStream(TypeToken.of(type));
    }

    @NonNull
    public Stream<Method> getMethodStream(@NonNull TypeToken<?> type) {
        return methodStream(type)
                .filter(method -> isPropertyReader(method, PropertyAccessorMethodFormat.JAVA_BEAN));
    }

    //todo beta
    @SuppressWarnings("unchecked")
    @NonNull
    public <T> Invokable<T, ?> getInvokableFromClassExact(@NonNull Type type,
                                                          @NonNull String fieldName) {
        return getInvokableFromClassExact((TypeToken<T>) TypeToken.of(type), fieldName);
    }

    //todo beta
    @NonNull
    public <T> Invokable<T, ?> getInvokableFromClassExact(@NonNull Class<T> type,
                                                          @NonNull String fieldName) {
        return getInvokableFromClassExact(TypeToken.of(type), fieldName);
    }

    //todo beta
    @NonNull
    public <T> Invokable<T, ?> getInvokableFromClassExact(@NonNull TypeToken<T> type,
                                                          @NonNull String fieldName) {
        return getInvokableFromClass(type, fieldName)
                .orElseThrow(
                        () -> MethodNotFoundException.create(type, fieldName, ImmutableList.of()));
    }

    //todo beta
    @SuppressWarnings("unchecked")
    @NonNull
    public <T> Optional<Invokable<T, ?>> getInvokableFromClass(@NonNull Type type,
                                                               @NonNull String fieldName) {
        return getInvokableFromClass((TypeToken<T>) TypeToken.of(type), fieldName);
    }

    //todo beta
    @NonNull
    public <T> Optional<Invokable<T, ?>> getInvokableFromClass(@NonNull Class<T> type,
                                                               @NonNull String fieldName) {
        return getInvokableFromClass(TypeToken.of(type), fieldName);
    }

    //todo beta
    @NonNull
    public <T> Optional<Invokable<T, ?>> getInvokableFromClass(@NonNull TypeToken<T> type,
                                                               @NonNull String fieldName) {
        return getInvokableStreamFromClass(type)
                .filter(
                        getInvokable -> PropertyAccessorMethodFormat.JAVA_BEAN
                                .fieldNameFromGetInvokable(getInvokable)
                                .equals(fieldName))
                .findAny();
    }

    //todo beta
    @SuppressWarnings("unchecked")
    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> getInvokablesFromClass(@NonNull Type type) {
        return getInvokablesFromClass((TypeToken<T>) TypeToken.of(type));
    }

    //todo beta
    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> getInvokablesFromClass(@NonNull Class<T> type) {
        return getInvokablesFromClass(TypeToken.of(type));
    }

    //todo beta
    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> getInvokablesFromClass(
            @NonNull TypeToken<T> type) {
        return getInvokableStreamFromClass(type).collect(ImmutableSet.toImmutableSet());
    }

    //todo beta
    @SuppressWarnings("unchecked")
    @NonNull
    public <T> Stream<Invokable<T, ?>> getInvokableStreamFromClass(@NonNull Type type) {
        return getInvokableStreamFromClass((TypeToken<T>) TypeToken.of(type));
    }

    //todo beta
    @NonNull
    public <T> Stream<Invokable<T, ?>> getInvokableStreamFromClass(@NonNull Class<T> type) {
        return getInvokableStreamFromClass(TypeToken.of(type));
    }

    //todo beta
    @NonNull
    public <T> Stream<Invokable<T, ?>> getInvokableStreamFromClass(
            @NonNull TypeToken<T> type) {
        return methodInvokableStreamFromClass(type)
                .filter(
                        invokable -> isPropertyReader(invokable, PropertyAccessorMethodFormat.JAVA_BEAN));
    }

    @NonNull
    public ImmutableSet<Method> setMethods(@NonNull Type type) {
        return setMethods(TypeToken.of(type));
    }

    @NonNull
    public ImmutableSet<Method> setMethods(@NonNull Class<?> type) {
        return setMethods(TypeToken.of(type));
    }

    @NonNull
    public ImmutableSet<Method> setMethods(@NonNull TypeToken<?> type) {
        return setMethodStream(type)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public Stream<Method> setMethodStream(@NonNull Type type) {
        return setMethodStream(TypeToken.of(type));
    }

    @NonNull
    public Stream<Method> setMethodStream(@NonNull Class<?> type) {
        return setMethodStream(TypeToken.of(type));
    }

    @NonNull
    public Stream<Method> setMethodStream(@NonNull TypeToken<?> type) {
        return methodStream(type)
                .filter(method -> isPropertyWriter(method, PropertyAccessorMethodFormat.JAVA_BEAN));
    }

    //todo beta
    @SuppressWarnings("unchecked")
    @NonNull
    public <T, R> Invokable<T, R> setInvokableFromClassExact(@NonNull Type type,
                                                             @NonNull String fieldName, @NonNull Type parameterType) {
        return setInvokableFromClassExact((TypeToken<T>) TypeToken.of(type), fieldName,
                (TypeToken<R>) TypeToken.of(parameterType));
    }

    //todo beta
    @NonNull
    public <T, R> Invokable<T, R> setInvokableFromClassExact(@NonNull Class<T> type,
                                                             @NonNull String fieldName, @NonNull Class<R> parameterClass) {
        return setInvokableFromClassExact(TypeToken.of(type), fieldName,
                TypeToken.of(parameterClass));
    }

    //todo beta
    @NonNull
    public <T, R> Invokable<T, R> setInvokableFromClassExact(@NonNull TypeToken<T> type,
                                                             @NonNull String fieldName, @NonNull TypeToken<R> parameterTypeToken) {
        return setInvokableFromClass(type, fieldName, parameterTypeToken)
                .orElseThrow(() -> MethodNotFoundException
                        .create(type, fieldName, ImmutableList.of(parameterTypeToken)));
    }

    //todo beta
    @SuppressWarnings("unchecked")
    @NonNull
    public <T, R> Optional<Invokable<T, R>> setInvokableFromClass(@NonNull Type type,
                                                                  @NonNull String fieldName, @NonNull Type parameterType) {
        return setInvokableFromClass((TypeToken<T>) TypeToken.of(type), fieldName,
                (TypeToken<R>) TypeToken.of(parameterType));
    }

    //todo beta
    @NonNull
    public <T, R> Optional<Invokable<T, R>> setInvokableFromClass(@NonNull Class<T> type,
                                                                  @NonNull String fieldName, @NonNull Class<R> parameterClass) {
        return setInvokableFromClass(TypeToken.of(type), fieldName, TypeToken.of(parameterClass));
    }

    //todo beta
    @SuppressWarnings("unchecked")
    @NonNull
    public <T, R> Optional<Invokable<T, R>> setInvokableFromClass(@NonNull TypeToken<T> type,
                                                                  @NonNull String fieldName, @NonNull TypeToken<R> parameterTypeToken) {
        return setInvokableStreamFromClass(type, fieldName)
                .filter(setInvokable -> MethodSignature.from(setInvokable).parameterTypes()
                        .equals(ImmutableList.of(parameterTypeToken)))
                .findAny()
                .map(setInvokable -> (Invokable<T, R>) setInvokable);
    }

    //todo beta
    @SuppressWarnings("unchecked")
    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> setInvokablesFromClass(@NonNull Type type,
                                                                    @NonNull String fieldName) {
        return setInvokablesFromClass((TypeToken<T>) TypeToken.of(type), fieldName);
    }

    //todo beta
    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> setInvokablesFromClass(@NonNull Class<T> type,
                                                                    @NonNull String fieldName) {
        return setInvokablesFromClass(TypeToken.of(type), fieldName);
    }

    //todo beta
    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> setInvokablesFromClass(@NonNull TypeToken<T> type,
                                                                    @NonNull String fieldName) {
        return setInvokableStreamFromClass(type, fieldName)
                .collect(ImmutableSet.toImmutableSet());
    }

    //todo beta
    @SuppressWarnings("unchecked")
    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> setInvokablesFromClass(@NonNull Type type) {
        return setInvokablesFromClass((TypeToken<T>) TypeToken.of(type));
    }

    //todo beta
    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> setInvokablesFromClass(@NonNull Class<T> type) {
        return setInvokablesFromClass(TypeToken.of(type));
    }

    //todo beta
    @NonNull
    public <T> ImmutableSet<Invokable<T, ?>> setInvokablesFromClass(
            @NonNull TypeToken<T> type) {
        return setInvokableStreamFromClass(type).collect(ImmutableSet.toImmutableSet());
    }

    //todo beta
    @SuppressWarnings("unchecked")
    @NonNull
    public <T> Stream<Invokable<T, ?>> setInvokableStreamFromClass(@NonNull Type type,
                                                                   @NonNull String fieldName) {
        return setInvokableStreamFromClass((TypeToken<T>) TypeToken.of(type), fieldName);
    }

    //todo beta
    @NonNull
    public <T> Stream<Invokable<T, ?>> setInvokableStreamFromClass(@NonNull Class<T> type,
                                                                   @NonNull String fieldName) {
        return setInvokableStreamFromClass(TypeToken.of(type), fieldName);
    }

    //todo beta
    @NonNull
    public <T> Stream<Invokable<T, ?>> setInvokableStreamFromClass(@NonNull TypeToken<T> type,
                                                                   @NonNull String fieldName) {
        return setInvokableStreamFromClass(type)
                .filter(setInvokable -> PropertyAccessorMethodFormat.JAVA_BEAN
                        .fieldNameFromSetInvokable(setInvokable).equals(fieldName));
    }

    //todo beta
    @SuppressWarnings("unchecked")
    @NonNull
    public <T> Stream<Invokable<T, ?>> setInvokableStreamFromClass(@NonNull Type type) {
        return setInvokableStreamFromClass((TypeToken<T>) TypeToken.of(type));
    }

    //todo beta
    @NonNull
    public <T> Stream<Invokable<T, ?>> setInvokableStreamFromClass(@NonNull Class<T> type) {
        return setInvokableStreamFromClass(TypeToken.of(type));
    }

    //todo beta
    @NonNull
    public <T> Stream<Invokable<T, ?>> setInvokableStreamFromClass(
            @NonNull TypeToken<T> type) {
        return methodInvokableStreamFromClass(type)
                .filter(
                        invokable -> isPropertyWriter(invokable, PropertyAccessorMethodFormat.JAVA_BEAN));
    }

    @NonNull
    public Method getMethodExact(@NonNull Type type,
                                 @NonNull String fieldName) {
        return getMethodExact(TypeToken.of(type), fieldName);
    }

    @NonNull
    public Method getMethodExact(@NonNull Class<?> type,
                                 @NonNull String fieldName) {
        return getMethodExact(TypeToken.of(type), fieldName);
    }

    @NonNull
    public Method getMethodExact(@NonNull TypeToken<?> type,
                                 @NonNull String fieldName) {
        return getMethod(type, fieldName).orElseThrow(
                () -> MethodNotFoundException.create(type, fieldName, ImmutableList.of()));
    }

    @NonNull
    public Optional<Method> getMethod(@NonNull Type type,
                                      @NonNull String fieldName) {
        return getMethod(TypeToken.of(type), fieldName);
    }

    @NonNull
    public Optional<Method> getMethod(@NonNull Class<?> type,
                                      @NonNull String fieldName) {
        return getMethod(TypeToken.of(type), fieldName);
    }

    @NonNull
    public Optional<Method> getMethod(@NonNull TypeToken<?> type,
                                      @NonNull String fieldName) {
        return getMethodStream(type)
                .filter(getMethod -> fieldName.equals(PropertyAccessorMethodFormat.JAVA_BEAN.fieldNameFromGetMethod(getMethod)))
                //这些方法必然方法签名相同,有接口方法与父类方法之间平级关系和子类与接口或父类方法的重写关系.
                // 平级关系时保留哪个均可,最终一定会跟子类重写方法进行判定,返回子类重写方法
                .reduce((m1, m2) -> isOverride(m1, m2) ? m1 : m2);
    }

    @NonNull
    public Method setMethodExact(@NonNull Type type,
                                 @NonNull String fieldName) {
        return setMethodExact(TypeToken.of(type), fieldName);
    }

    @NonNull
    public Method setMethodExact(@NonNull Class<?> type,
                                 @NonNull String fieldName) {
        return setMethodExact(TypeToken.of(type), fieldName);
    }

    @NonNull
    public Method setMethodExact(@NonNull TypeToken<?> type,
                                 @NonNull String fieldName) {
        return setMethod(type, fieldName)
                .orElseThrow(() -> MethodNotFoundException.create(type, fieldName, ImmutableList.of()));
    }

    @NonNull
    public Method setMethodExact(@NonNull Type type,
                                 @NonNull String fieldName, @NonNull Type fieldType) {
        return setMethodExact(TypeToken.of(type), fieldName, TypeToken.of(fieldType));
    }

    @NonNull
    public Method setMethodExact(@NonNull Type type,
                                 @NonNull String fieldName, @NonNull Class<?> fieldClass) {
        return setMethodExact(TypeToken.of(type), fieldName, TypeToken.of(fieldClass));
    }

    @NonNull
    public Method setMethodExact(@NonNull Type type,
                                 @NonNull String fieldName, @NonNull TypeToken<?> fieldType) {
        return setMethodExact(TypeToken.of(type), fieldName, fieldType);
    }

    @NonNull
    public Method setMethodExact(@NonNull Class<?> type,
                                 @NonNull String fieldName, @NonNull Type fieldType) {
        return setMethodExact(TypeToken.of(type), fieldName, TypeToken.of(fieldType));
    }

    @NonNull
    public Method setMethodExact(@NonNull Class<?> type,
                                 @NonNull String fieldName, @NonNull Class<?> fieldClass) {
        return setMethodExact(TypeToken.of(type), fieldName, TypeToken.of(fieldClass));
    }

    @NonNull
    public Method setMethodExact(@NonNull Class<?> type,
                                 @NonNull String fieldName, @NonNull TypeToken<?> fieldType) {
        return setMethodExact(TypeToken.of(type), fieldName, fieldType);
    }

    @NonNull
    public Method setMethodExact(@NonNull TypeToken<?> type,
                                 @NonNull String fieldName, @NonNull Type fieldType) {
        return setMethodExact(type, fieldName, TypeToken.of(fieldType));
    }

    @NonNull
    public Method setMethodExact(@NonNull TypeToken<?> type,
                                 @NonNull String fieldName, @NonNull Class<?> fieldClass) {
        return setMethodExact(type, fieldName, TypeToken.of(fieldClass));

    }

    @NonNull
    public Method setMethodExact(@NonNull TypeToken<?> type,
                                 @NonNull String fieldName, @NonNull TypeToken<?> fieldType) {
        return setMethod(type, fieldName, fieldType)
                .orElseThrow(
                        () -> MethodNotFoundException.create(type, fieldName, ImmutableList.of()));
    }

    @NonNull
    public Optional<Method> setMethod(@NonNull Type type, @NonNull String fieldName) {
        return setMethod(TypeToken.of(type), fieldName);
    }

    @NonNull
    public Optional<Method> setMethod(@NonNull Class<?> type, @NonNull String fieldName) {
        return setMethod(TypeToken.of(type), fieldName);
    }

    @NonNull
    public Optional<Method> setMethod(@NonNull TypeToken<?> type, @NonNull String fieldName) {
        ImmutableSet<Method> setMethods = setMethods(type, fieldName);
        if (setMethods.size() > 1) {//当有同名set方法并且入参类型不同形成重载时认为这是不同的属性
            ImmutableSet<Class<?>> fieldTypes = setMethods.stream().map(Method::getParameterTypes)
                    .flatMap(Arrays::stream)
                    .collect(ImmutableSet.toImmutableSet());
            //todo exceptionType定义?
            Preconditions.checkArgument(fieldTypes.size() == 1, "set method that named %s is not only", fieldName);
        }
        return setMethods.stream()
                //这些方法必然方法签名相同,有接口方法与父类方法之间平级关系和子类与接口或父类方法的重写关系.
                // 平级关系时保留哪个均可,最终一定会跟子类重写方法进行判定,返回子类重写方法
                .reduce((m1, m2) -> isOverride(m1, m2) ? m1 : m2);
    }

    @NonNull
    public Optional<Method> setMethod(@NonNull Type type, @NonNull String fieldName, @NonNull Type fieldType) {
        return setMethod(TypeToken.of(type), fieldName, TypeToken.of(fieldType));
    }

    @NonNull
    public Optional<Method> setMethod(@NonNull Type type, @NonNull String fieldName, @NonNull Class<?> fieldClass) {
        return setMethod(TypeToken.of(type), fieldName, TypeToken.of(fieldClass));
    }

    @NonNull
    public Optional<Method> setMethod(@NonNull Type type, @NonNull String fieldName, @NonNull TypeToken<?> fieldType) {
        return setMethod(TypeToken.of(type), fieldName, fieldType);
    }

    @NonNull
    public Optional<Method> setMethod(@NonNull Class<?> type, @NonNull String fieldName, @NonNull Type fieldType) {
        return setMethod(TypeToken.of(type), fieldName, TypeToken.of(fieldType));
    }

    @NonNull
    public Optional<Method> setMethod(@NonNull Class<?> type, @NonNull String fieldName, @NonNull Class<?> fieldClass) {
        return setMethod(TypeToken.of(type), fieldName, TypeToken.of(fieldClass));
    }

    @NonNull
    public Optional<Method> setMethod(@NonNull Class<?> type, @NonNull String fieldName, @NonNull TypeToken<?> fieldType) {
        return setMethod(TypeToken.of(type), fieldName, fieldType);
    }

    @NonNull
    public Optional<Method> setMethod(@NonNull TypeToken<?> type, @NonNull String fieldName, @NonNull Type fieldType) {
        return setMethod(type, fieldName, TypeToken.of(fieldType));
    }

    @NonNull
    public Optional<Method> setMethod(@NonNull TypeToken<?> type, @NonNull String fieldName, @NonNull Class<?> fieldClass) {
        return setMethod(type, fieldName, TypeToken.of(fieldClass));
    }

    @NonNull
    public Optional<Method> setMethod(@NonNull TypeToken<?> type, @NonNull String fieldName, @NonNull TypeToken<?> fieldType) {
        return setMethodStream(type, fieldName)
                //todo TypeToken equals?
                .filter(setMethod -> TypeToken.of(setMethod.getGenericParameterTypes()[0]).equals(fieldType))
                //这些方法必然方法签名相同,有接口方法与父类方法之间平级关系和子类与接口或父类方法的重写关系.
                // 平级关系时保留哪个均可,最终一定会跟子类重写方法进行判定,返回子类重写方法
                .reduce((m1, m2) -> isOverride(m1, m2) ? m1 : m2);
    }

    @NonNull
    public ImmutableSet<Method> setMethods(@NonNull Type type,
                                           @NonNull String fieldName) {
        return setMethods(TypeToken.of(type), fieldName);
    }

    @NonNull
    public ImmutableSet<Method> setMethods(@NonNull Class<?> type,
                                           @NonNull String fieldName) {
        return setMethods(TypeToken.of(type), fieldName);
    }

    @NonNull
    public ImmutableSet<Method> setMethods(@NonNull TypeToken<?> type,
                                           @NonNull String fieldName) {
        return setMethodStream(type, fieldName)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public Stream<Method> setMethodStream(@NonNull Type type,
                                          @NonNull String fieldName) {
        return setMethodStream(TypeToken.of(type), fieldName);
    }

    @NonNull
    public Stream<Method> setMethodStream(@NonNull Class<?> type,
                                          @NonNull String fieldName) {
        return setMethodStream(TypeToken.of(type), fieldName);
    }

    @NonNull
    public Stream<Method> setMethodStream(@NonNull TypeToken<?> type,
                                          @NonNull String fieldName) {
        return setMethodStream(type)
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
    public static <T> Stream<Property<T, ?>> propertyStream(
            @NonNull Type type) {
        return propertyStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param type                          target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<Property<T, ?>> propertyStream(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStream((TypeToken<T>) TypeToken.of(type),
                propertyAccessorMethodFormats);
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
    public static <T> Stream<Property<T, ?>> propertyStream(
            @NonNull Class<T> type) {
        return propertyStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param type                          target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<Property<T, ?>> propertyStream(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStream(TypeToken.of(type), propertyAccessorMethodFormats);
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
    public static <T> Stream<Property<T, ?>> propertyStream(
            @NonNull TypeToken<T> type) {
        return propertyStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param type                          target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<Property<T, ?>> propertyStream(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {

        ImmutableListMultimap<@NonNull ImmutableList<?>, PropertyElement<T, ?>> signatureToPropertyElements =
                propertyElementStream(type, propertyAccessorMethodFormats)
                        .collect(ImmutableListMultimap
                                .toImmutableListMultimap(KEY_FUNCTION, Function.identity()));

        return signatureToPropertyElements.asMap().values().stream()
                .map(propertyElements -> propertyElements.stream().map(o -> (PropertyElement<T, ?>) o))
                .map(Property::create);
    }

    @NonNull
    public static <T> ImmutableSet<Property<T, ?>> properties(
            @NonNull Type type) {
        return properties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<Property<T, ?>> properties(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return properties((TypeToken<T>) TypeToken.of(type),
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> ImmutableSet<Property<T, ?>> properties(
            @NonNull Class<T> type) {
        return properties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<Property<T, ?>> properties(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStream(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<Property<T, ?>> properties(
            @NonNull TypeToken<T> type) {
        return properties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<Property<T, ?>> properties(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStream(type, propertyAccessorMethodFormats)
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
    public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStream(
            @NonNull Type type) {
        return readablePropertyStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param type                          target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStream(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyStream((TypeToken<T>) TypeToken.of(type),
                propertyAccessorMethodFormats);
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
    public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStream(
            @NonNull Class<T> type) {
        return readablePropertyStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param type                          target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStream(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyStream(TypeToken.of(type), propertyAccessorMethodFormats);
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
    public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStream(
            @NonNull TypeToken<T> type) {
        return readablePropertyStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param type                          target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStream(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStream(type, propertyAccessorMethodFormats)
                .filter(Property::readable)
                .map(Property::toReadable);
    }


    @NonNull
    public static <T> ImmutableSet<ReadableProperty<T, ?>> readableProperties(
            @NonNull Type type) {
        return readableProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<ReadableProperty<T, ?>> readableProperties(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readableProperties((TypeToken<T>) TypeToken.of(type),
                propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T> ImmutableSet<ReadableProperty<T, ?>> readableProperties(
            @NonNull Class<T> type) {
        return readableProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<ReadableProperty<T, ?>> readableProperties(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyStream(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<ReadableProperty<T, ?>> readableProperties(
            @NonNull TypeToken<T> type) {
        return readableProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<ReadableProperty<T, ?>> readableProperties(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyStream(type, propertyAccessorMethodFormats)
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
    public static <T> Stream<WritableProperty<T, ?>> writablePropertyStream(
            @NonNull Type type) {
        return writablePropertyStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param type                          target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<WritableProperty<T, ?>> writablePropertyStream(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyStream((TypeToken<T>) TypeToken.of(type),
                propertyAccessorMethodFormats);
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
    public static <T> Stream<WritableProperty<T, ?>> writablePropertyStream(
            @NonNull Class<T> type) {
        return writablePropertyStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param type                          target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<WritableProperty<T, ?>> writablePropertyStream(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyStream(TypeToken.of(type), propertyAccessorMethodFormats);
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
    public static <T> Stream<WritableProperty<T, ?>> writablePropertyStream(
            @NonNull TypeToken<T> type) {
        return writablePropertyStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param type                          target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<WritableProperty<T, ?>> writablePropertyStream(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStream(type, propertyAccessorMethodFormats)
                .filter(Property::writable)
                .map(Property::toWritable);
    }


    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> writableProperties(
            @NonNull Type type) {
        return writableProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> writableProperties(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writableProperties((TypeToken<T>) TypeToken.of(type),
                propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> writableProperties(
            @NonNull Class<T> type) {
        return writableProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> writableProperties(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyStream(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }


    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> writableProperties(
            @NonNull TypeToken<T> type) {
        return writableProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> writableProperties(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyStream(type, propertyAccessorMethodFormats)
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
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStream(
            @NonNull Type type) {
        return accessiblePropertyStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param type                          target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStream(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyStream((TypeToken<T>) TypeToken.of(type),
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
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStream(
            @NonNull T object) {
        return accessiblePropertyStream(object, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param object                        target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStream(
            @NonNull T object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyStream((Class<T>) object.getClass(),
                propertyAccessorMethodFormats);
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
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStream(
            @NonNull Class<T> type) {
        return accessiblePropertyStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param type                          target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStream(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyStream(TypeToken.of(type),
                propertyAccessorMethodFormats);
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
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStream(
            @NonNull TypeToken<T> type) {
        return accessiblePropertyStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * get all properties from class
     *
     * @param type                          target class
     * @param propertyAccessorMethodFormats get set methods styles
     * @return {@link Stream} of all properties
     * @author caotc
     * @date 2019-11-28
     * @apiNote
     * @since 1.0.0
     */
    @NonNull
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStream(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStream(type, propertyAccessorMethodFormats)
                .filter(Property::accessible)
                .map(Property::toAccessible);
    }


    @NonNull
    public static <T> ImmutableSet<AccessibleProperty<T, ?>> accessibleProperties(
            @NonNull Type type) {
        return accessibleProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<AccessibleProperty<T, ?>> accessibleProperties(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessibleProperties((TypeToken<T>) type,
                propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T> ImmutableSet<AccessibleProperty<T, ?>> accessibleProperties(
            @NonNull Class<T> type) {
        return accessibleProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<AccessibleProperty<T, ?>> accessibleProperties(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyStream(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<AccessibleProperty<T, ?>> accessibleProperties(
            @NonNull TypeToken<T> type) {
        return accessibleProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<AccessibleProperty<T, ?>> accessibleProperties(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyStream(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }


    @NonNull
    public static <R> ReadableProperty<?, R> readablePropertyExact(
            @NonNull Type type, @NonNull String fieldName) {
        return readablePropertyExact(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <R> ReadableProperty<?, R> readablePropertyExact(
            @NonNull Type type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyExact(TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T, R> ReadableProperty<T, R> readablePropertyExact(
            @NonNull Class<T> type, @NonNull String fieldName) {
        return readablePropertyExact(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * 获取指定{@link ReadableProperty}
     *
     * @param type      需要获取可读属性{@link ReadableProperty}的类
     * @param fieldName 属性名称
     * @return {@link ReadableProperty}
     * @throws ReadablePropertyNotFoundException readablePropertyNotFoundException
     * @author caotc
     * @date 2019-05-10
     * @apiNote included super interfaces and superclasses
     * @since 1.0.0
     */
    @NonNull
    public static <T, R> ReadableProperty<T, R> readablePropertyExact(
            @NonNull Class<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyExact(TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T, R> ReadableProperty<T, R> readablePropertyExact(
            @NonNull TypeToken<T> type, @NonNull String fieldName) {
        return readablePropertyExact(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> ReadableProperty<T, R> readablePropertyExact(
            @NonNull TypeToken<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<T, R>readableProperty(type, fieldName,
                        propertyAccessorMethodFormats)
                .orElseThrow(() -> ReadablePropertyNotFoundException
                        .create(type, fieldName));
    }


    public static <T, R> Optional<ReadableProperty<T, R>> readableProperty(
            @NonNull Type type, @NonNull String fieldName) {
        return readableProperty(type, fieldName, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    public static <T, R> Optional<ReadableProperty<T, R>> readableProperty(
            @NonNull Type type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readableProperty((TypeToken<T>) TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }

    /**
     * 从传入的类中获取包括所有超类和接口的所有可读属性{@link ReadableProperty}集合
     *
     * @param type      需要获取可读属性{@link ReadableProperty}集合的类
     * @param fieldName 指定属性名称
     * @return 所有可读属性 {@link ReadableProperty}集合
     * @author caotc
     * @date 2019-05-10
     * @since 1.0.0
     */
    @NonNull
    public static <T, R> Optional<ReadableProperty<T, R>> readableProperty(
            @NonNull Class<T> type, @NonNull String fieldName) {
        return readableProperty(type, fieldName, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * 从传入的类中获取包括所有超类和接口的所有可读属性{@link ReadableProperty}集合
     *
     * @param type                          需要获取可读属性{@link ReadableProperty}的类
     * @param fieldName                     指定属性名称
     * @param propertyAccessorMethodFormats get方法格式集合
     * @return 所有可读属性 {@link ReadableProperty}集合
     * @author caotc
     * @date 2019-05-10
     * @apiNote 如果只想要获取JavaBean规范的get方法, {@code methodNameStyles}参数使用{@link
     * PropertyAccessorMethodFormat#JAVA_BEAN}
     * @since 1.0.0
     */
    @NonNull
    public static <T, R> Optional<ReadableProperty<T, R>> readableProperty(
            @NonNull Class<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readableProperty(TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T, R> Optional<ReadableProperty<T, R>> readableProperty(
            @NonNull TypeToken<T> type, @NonNull String fieldName) {
        return readableProperty(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> Optional<ReadableProperty<T, R>> readableProperty(
            @NonNull TypeToken<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyInternalCompose(type, fieldName, propertyAccessorMethodFormats);
    }

    //todo 待优化
    @SuppressWarnings("unchecked")
    @NonNull
    private static <T, R, E> Optional<ReadableProperty<T, R>> readablePropertyInternalCompose(
            @NonNull TypeToken<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        PropertyName propertyName = PropertyName.from(fieldName);
        String firstTierPropertyName = propertyName.firstTier().flat();
        if (propertyName.complex()) {
            PropertyName sub = propertyName.sub(1);
            Optional<ReadableProperty<T, E>> transferReadableProperty = readablePropertyInternal(
                    type,
                    firstTierPropertyName,
                    propertyAccessorMethodFormats);
            return transferReadableProperty
                    .flatMap(f -> {
                        Optional<ReadableProperty<E, R>> result = readablePropertyInternal(
                                (TypeToken<E>) f.type(), sub.flat(),
                                propertyAccessorMethodFormats);
                                return result.map(f::compose);
                            }
                    );
        }
        return readablePropertyInternal(type,
                firstTierPropertyName,
                propertyAccessorMethodFormats);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private static <T, R> Optional<ReadableProperty<T, R>> readablePropertyInternal(
            @NonNull TypeToken<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readableProperties(type, propertyAccessorMethodFormats)
                .stream()
                .filter(propertyGetter -> propertyGetter.name().equals(fieldName))
                .map(propertyGetter -> (ReadableProperty<T, R>) propertyGetter)
                .findAny();
    }


    @NonNull
    public static <T, R> WritableProperty<T, R> writablePropertyExact(
            @NonNull Type type, @NonNull String fieldName) {
        return writablePropertyExact(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T, R> WritableProperty<T, R> writablePropertyExact(
            @NonNull Type type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyExact((TypeToken<T>) TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T, R> WritableProperty<T, R> writablePropertyExact(
            @NonNull Class<T> type, @NonNull String fieldName) {
        return writablePropertyExact(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> WritableProperty<T, R> writablePropertyExact(
            @NonNull Class<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyExact(TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T, R> WritableProperty<T, R> writablePropertyExact(
            @NonNull TypeToken<T> type, @NonNull String fieldName) {
        return writablePropertyExact(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> WritableProperty<T, R> writablePropertyExact(
            @NonNull TypeToken<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<T, R>writableProperty(type, fieldName,
                        propertyAccessorMethodFormats)
                .orElseThrow(() -> WritablePropertyNotFoundException
                        .create(type, fieldName));
    }

    @NonNull
    public static <T, R> Optional<WritableProperty<T, R>> writableProperty(
            @NonNull Type type, @NonNull String fieldName) {
        return writableProperty(type, fieldName, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T, R> Optional<WritableProperty<T, R>> writableProperty(
            @NonNull Type type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writableProperty((TypeToken<T>) TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T, R> Optional<WritableProperty<T, R>> writableProperty(
            @NonNull Class<T> type, @NonNull String fieldName) {
        return writableProperty(type, fieldName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> Optional<WritableProperty<T, R>> writableProperty(
            @NonNull Class<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writableProperty(TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T, R> Optional<WritableProperty<T, R>> writableProperty(
            @NonNull TypeToken<T> type, @NonNull String fieldName) {
        return writableProperty(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T, R> Optional<WritableProperty<T, R>> writableProperty(
            @NonNull TypeToken<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writableProperties(type, propertyAccessorMethodFormats)
                .stream()
                .filter(propertyWriter -> propertyWriter.name().equals(fieldName))
                .map(propertyWriter -> (WritableProperty<T, R>) propertyWriter)
                .findAny();
    }


    @NonNull
    public static <T, R> AccessibleProperty<T, R> accessiblePropertyExact(
            @NonNull Type type, @NonNull String fieldName) {
        return accessiblePropertyExact(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T, R> AccessibleProperty<T, R> accessiblePropertyExact(
            @NonNull Type type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyExact((TypeToken<T>) TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T, R> AccessibleProperty<T, R> accessiblePropertyExact(
            @NonNull Class<T> type, @NonNull String fieldName) {
        return accessiblePropertyExact(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> AccessibleProperty<T, R> accessiblePropertyExact(
            @NonNull Class<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyExact(TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T, R> AccessibleProperty<T, R> accessiblePropertyExact(
            @NonNull TypeToken<T> type, @NonNull String fieldName) {
        return accessiblePropertyExact(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> AccessibleProperty<T, R> accessiblePropertyExact(
            @NonNull TypeToken<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<T, R>accessibleProperty(type, fieldName,
                        propertyAccessorMethodFormats)
                .orElseThrow(() -> AccessiblePropertyNotFoundException
                        .create(type, fieldName));
    }

    @NonNull
    public static <T, R> Optional<AccessibleProperty<T, R>> accessibleProperty(
            @NonNull Type type, @NonNull String fieldName) {
        return accessibleProperty(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T, R> Optional<AccessibleProperty<T, R>> accessibleProperty(
            @NonNull Type type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessibleProperty((TypeToken<T>) TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T, R> Optional<AccessibleProperty<T, R>> accessibleProperty(
            @NonNull Class<T> type, @NonNull String fieldName) {
        return accessibleProperty(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> Optional<AccessibleProperty<T, R>> accessibleProperty(
            @NonNull Class<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessibleProperty(TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T, R> Optional<AccessibleProperty<T, R>> accessibleProperty(
            @NonNull TypeToken<T> type, @NonNull String fieldName) {
        return accessibleProperty(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T, R> Optional<AccessibleProperty<T, R>> accessibleProperty(
            @NonNull TypeToken<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessibleProperties(type, propertyAccessorMethodFormats)
                .stream()
                .filter(propertyWriter -> propertyWriter.name().equals(fieldName))
                .map(propertyWriter -> (AccessibleProperty<T, R>) propertyWriter)
                .findAny();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElements(
            @NonNull Type type) {
        return propertyElements((TypeToken<T>) TypeToken.of(type),
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElements(
            @NonNull Class<T> type) {
        return propertyElements(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElements(
            @NonNull TypeToken<T> type) {
        return propertyElements(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElements(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElements((TypeToken<T>) TypeToken.of(type),
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElements(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStream(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElements(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStream(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<PropertyElement<T, ?>> propertyElementStream(
            @NonNull Type type) {
        return propertyElementStream((TypeToken<T>) TypeToken.of(type),
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyElement<T, ?>> propertyElementStream(
            @NonNull Class<T> type) {
        return propertyElementStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyElement<T, ?>> propertyElementStream(
            @NonNull TypeToken<T> type) {
        return propertyElementStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<PropertyElement<T, ?>> propertyElementStream(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStream((TypeToken<T>) TypeToken.of(type),
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyElement<T, ?>> propertyElementStream(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStream(TypeToken.of(type), propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyElement<T, ?>> propertyElementStream(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {

        Stream<PropertyElement<T, ?>> propertyElementStream = methodInvokableStreamFromClass(type)
                .flatMap(invokable -> Arrays.stream(propertyAccessorMethodFormats)
                        .filter(methodNameStyle -> methodNameStyle.isPropertyWriter(invokable)
                                || methodNameStyle.isPropertyReader(invokable))
                        .map(methodNameStyle -> PropertyElement.from(invokable, methodNameStyle.propertyName(invokable))));

        return Stream
                .concat(fieldStream(type).filter(field -> isPropertyReader(field) || isPropertyWriter(field))
                                .map(PropertyElement::from),
                        propertyElementStream);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReaders(
            @NonNull Type type) {
        return propertyReaders(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReaders(
            @NonNull Class<T> type) {
        return propertyReaders(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReaders(
            @NonNull TypeToken<T> type) {
        return propertyReaders(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReaders(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyReaders((TypeToken<T>) TypeToken.of(type),
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReaders(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyReaderStream(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReaders(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyReaderStream(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> Stream<PropertyReader<T, ?>> propertyReaderStream(
            @NonNull Type type) {
        return propertyReaderStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyReader<T, ?>> propertyReaderStream(
            @NonNull Class<T> type) {
        return propertyReaderStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyReader<T, ?>> propertyReaderStream(
            @NonNull TypeToken<T> type) {
        return propertyReaderStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<PropertyReader<T, ?>> propertyReaderStream(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyReaderStream((TypeToken<T>) TypeToken.of(type),
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyReader<T, ?>> propertyReaderStream(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyReaderStream(TypeToken.of(type), propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyReader<T, ?>> propertyReaderStream(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStream(type, propertyAccessorMethodFormats)
                .filter(PropertyElement::isReader).map(PropertyElement::toReader);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWriters(
            @NonNull Type type) {
        return propertyWriters(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWriters(
            @NonNull Class<T> type) {
        return propertyWriters(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWriters(
            @NonNull TypeToken<T> type) {
        return propertyWriters(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWriters(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyWriters((TypeToken<T>) TypeToken.of(type),
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWriters(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyWriterStream(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWriters(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyWriterStream(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStream(
            @NonNull Type type) {
        return propertyWriterStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStream(
            @NonNull Class<T> type) {
        return propertyWriterStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStream(
            @NonNull TypeToken<T> type) {
        return propertyWriterStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStream(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyWriterStream((TypeToken<T>) TypeToken.of(type),
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStream(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyWriterStream(TypeToken.of(type), propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStream(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStream(type, propertyAccessorMethodFormats)
                .filter(PropertyElement::isWriter).map(PropertyElement::toWriter);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessors(
            @NonNull Type type) {
        return propertyAccessors(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessors(
            @NonNull Class<T> type) {
        return propertyAccessors(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessors(
            @NonNull TypeToken<T> type) {
        return propertyAccessors(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessors(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyAccessors((TypeToken<T>) TypeToken.of(type),
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessors(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyAccessorStream(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessors(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyAccessorStream(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStream(
            @NonNull Type type) {
        return propertyAccessorStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStream(
            @NonNull Class<T> type) {
        return propertyAccessorStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStream(
            @NonNull TypeToken<T> type) {
        return propertyAccessorStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStream(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyAccessorStream((TypeToken<T>) TypeToken.of(type),
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStream(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyAccessorStream(TypeToken.of(type), propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStream(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStream(type, propertyAccessorMethodFormats)
                .filter(PropertyElement::isAccessor).map(PropertyElement::toAccessor);
    }

    /**
     * @author caotc
     * @date 2019-12-08
     * @implNote
     * @implSpec
     * @apiNote 非static的final属性只有在值为固定值时会因为编译器优化导致get方法无法读取到修改后的值
     * @since 1.0.0
     */
    public static boolean isPropertyReader(@NonNull Field field) {
        return isPropertyWriter(FieldElement.of(field));
    }

    /**
     * @author caotc
     * @date 2019-12-08
     * @implNote
     * @implSpec
     * @apiNote 非static的final属性只有在值为固定值时会因为编译器优化导致get方法无法读取到修改后的值
     * @since 1.0.0
     */
    public static boolean isPropertyReader(@NonNull FieldElement<?, ?> fieldElement) {
        return !fieldElement.isStatic();
    }

    /**
     * 检查传入方法是否是get方法
     *
     * @param method                        需要检查的方法
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
     * @param invokable                     需要检查的方法
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
                                .isPropertyReader(invokable));
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
        return isPropertyWriter(FieldElement.of(field));
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
        return !fieldElement.isStatic();
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
     * @param method                        需要检查的方法
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
     * @param invokable                     需要检查的方法
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
                .anyMatch(methodNameStyle -> methodNameStyle.isPropertyWriter(invokable));
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
                .filter(type -> !type.equals(invokable.getOwnerType()))
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

    static Set<Class<?>> getSuperclasses(Class<?> clazz) {
        final Set<Class<?>> result = new LinkedHashSet<>();
        final Queue<Class<?>> queue = new ArrayDeque<>();
        queue.add(clazz);
        if (clazz.isInterface()) {
            queue.add(Object.class); // optional
        }
        while (!queue.isEmpty()) {
            Class<?> c = queue.remove();
            if (result.add(c)) {
                Class<?> sup = c.getSuperclass();
                if (sup != null) queue.add(sup);
                queue.addAll(Arrays.asList(c.getInterfaces()));
            }
        }
        return result;
    }

    static Set<Class<?>> commonSuperclasses(Iterable<Class<?>> classes) {
        Iterator<Class<?>> it = classes.iterator();
        if (!it.hasNext()) {
            return Collections.emptySet();
        }
        // begin with set from first hierarchy
        Set<Class<?>> result = getSuperclasses(it.next());
        // remove non-superclasses of remaining
        while (it.hasNext()) {
            Class<?> c = it.next();
            result.removeIf(sup -> !sup.isAssignableFrom(c));
        }
        return result;
    }

    public static List<Class<?>> lowestCommonSuperclasses(Iterable<Class<?>> classes) {
        Collection<Class<?>> commonSupers = commonSuperclasses(classes);
        return lowestClasses(commonSupers);
    }

    static List<Class<?>> lowestClasses(Collection<Class<?>> classes) {
        final LinkedList<Class<?>> source = new LinkedList<>(classes);
        final ArrayList<Class<?>> result = new ArrayList<>(classes.size());
        while (!source.isEmpty()) {
            Iterator<Class<?>> srcIt = source.iterator();
            Class<?> c = srcIt.next();
            srcIt.remove();
            while (srcIt.hasNext()) {
                Class<?> c2 = srcIt.next();
                if (c2.isAssignableFrom(c)) {
                    srcIt.remove();
                } else if (c.isAssignableFrom(c2)) {
                    c = c2;
                    srcIt.remove();
                }
            }
            result.add(c);
        }
        result.trimToSize();
        return result;
    }

    public static List<TypeToken<?>> lowestCommonAncestors(Iterable<? extends TypeToken<?>> classes) {
        Iterator<? extends TypeToken<?>> it = classes.iterator();
        if (!it.hasNext()) {
            return Collections.emptyList();
        }
        // begin with set from first hierarchy
        Set<? extends TypeToken<?>> result = it.next().getTypes();
        // remove non-superclasses of remaining
        result = result.stream()
                .filter(sup -> Streams.stream(classes)
                        .allMatch(type -> sup.isSupertypeOf(type)))
                .collect(Collectors.toSet());
        return lowestAncestors(result);
    }

    static List<TypeToken<?>> lowestAncestors(Collection<? extends TypeToken<?>> classes) {
        final LinkedList<TypeToken<?>> source = new LinkedList<>(classes);
        final ArrayList<TypeToken<?>> result = new ArrayList<>(classes.size());
        while (!source.isEmpty()) {
            Iterator<TypeToken<?>> srcIt = source.iterator();
            TypeToken<?> c = srcIt.next();
            srcIt.remove();
            while (srcIt.hasNext()) {
                TypeToken<?> c2 = srcIt.next();
                if (c2.isSupertypeOf(c)) {
                    srcIt.remove();
                } else if (c.isSupertypeOf(c2)) {
                    c = c2;
                    srcIt.remove();
                }
            }
            result.add(c);
        }
        result.trimToSize();
        return result;
    }

    public static Class<?> primitiveClassToWrapperClass(@NonNull Class<?> clazz) {
        return PRIMITIVE_CLASS_TO_WRAPPER_CLASS.getOrDefault(clazz, clazz);
    }

    public static Class<?> wrapperClassToPrimitiveClass(@NonNull Class<?> clazz) {
        return WRAPPER_CLASS_TO_PRIMITIVE_CLASS.getOrDefault(clazz, clazz);
    }

    public static TypeToken<?> primitiveTypeToWrapperType(@NonNull TypeToken<?> type) {
        return PRIMITIVE_TYPE_TO_WRAPPER_TYPE.getOrDefault(type, type);
    }

    public static TypeToken<?> wrapperTypeToPrimitiveType(@NonNull TypeToken<?> type) {
        return WRAPPER_TYPE_TO_PRIMITIVE_TYPE.getOrDefault(type, type);
    }
}
