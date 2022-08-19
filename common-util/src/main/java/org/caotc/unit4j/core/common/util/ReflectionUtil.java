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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
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
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
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
            .of(propertyElement.propertyName(), propertyElement.propertyType(),
                    propertyElement.isStatic());

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
                Arrays::stream).map(FieldElement::from);
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
        ImmutableSet<Method> setMethods = setMethods(type, fieldName);
        if (setMethods.size() > 1) {//当有同名set方法并且入参类型不同形成重载时认为这是不同的属性
            ImmutableSet<Class<?>> fieldTypes = setMethods.stream().map(Method::getParameterTypes)
                    .flatMap(Arrays::stream)
                    .collect(ImmutableSet.toImmutableSet());
            //todo expectionType定义?
            Preconditions.checkArgument(fieldTypes.size() == 1, "set method that named %s is not only", fieldName);
        }
        return setMethods.stream()
                //这些方法必然方法签名相同,有接口方法与父类方法之间平级关系和子类与接口或父类方法的重写关系.
                // 平级关系时保留哪个均可,最终一定会跟子类重写方法进行判定,返回子类重写方法
                .reduce((m1, m2) -> isOverride(m1, m2) ? m1 : m2)
                .orElseThrow(
                        () -> MethodNotFoundException.create(type, fieldName, ImmutableList.of()));
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
    public Optional<Method> setMethod(@NonNull Type type,
                                      @NonNull String fieldName, @NonNull Type argumentType) {
        return setMethod(TypeToken.of(type), fieldName, TypeToken.of(argumentType));
    }

    @NonNull
    public Optional<Method> setMethod(@NonNull Class<?> type,
                                      @NonNull String fieldName, @NonNull Class<?> argumentClass) {
        return setMethod(TypeToken.of(type), fieldName, TypeToken.of(argumentClass));
    }

    @NonNull
    public Optional<Method> setMethod(@NonNull TypeToken<?> type,
                                      @NonNull String fieldName, @NonNull TypeToken<?> argumentTypeToken) {
        return setMethodStream(type, fieldName)
                //todo TypeToken equals?
                .filter(
                        setMethod -> TypeToken.of(setMethod.getGenericParameterTypes()[0])
                                .equals(argumentTypeToken))
                .findAny();
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
    public static <T> Stream<Property<T, ?>> propertyStreamFromClass(
            @NonNull Type type) {
        return propertyStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
    public static <T> Stream<Property<T, ?>> propertyStreamFromClass(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStreamFromClass((TypeToken<T>) TypeToken.of(type),
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
    public static <T> Stream<Property<T, ?>> propertyStreamFromClass(
            @NonNull Class<T> type) {
        return propertyStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
    public static <T> Stream<Property<T, ?>> propertyStreamFromClass(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStreamFromClass(TypeToken.of(type), propertyAccessorMethodFormats);
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
            @NonNull TypeToken<T> type) {
        return propertyStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
    public static <T> Stream<Property<T, ?>> propertyStreamFromClass(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {

        ImmutableListMultimap<@NonNull ImmutableList<?>, PropertyElement<T, ?>> signatureToPropertyElements =
                propertyElementStreamFromClass(type, propertyAccessorMethodFormats)
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
            @NonNull Class<T> type) {
        return propertiesFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<Property<T, ?>> propertiesFromClass(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStreamFromClass(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<Property<T, ?>> propertiesFromClass(
            @NonNull TypeToken<T> type) {
        return propertiesFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<Property<T, ?>> propertiesFromClass(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStreamFromClass(type, propertyAccessorMethodFormats)
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
    public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStreamFromClass(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyStreamFromClass((TypeToken<T>) TypeToken.of(type),
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
    public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStreamFromClass(
            @NonNull Class<T> type) {
        return readablePropertyStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
    public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStreamFromClass(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyStreamFromClass(TypeToken.of(type), propertyAccessorMethodFormats);
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
            @NonNull TypeToken<T> type) {
        return readablePropertyStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
    public static <T> Stream<ReadableProperty<T, ?>> readablePropertyStreamFromClass(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStreamFromClass(type, propertyAccessorMethodFormats)
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
            @NonNull Class<T> type) {
        return readablePropertiesFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<ReadableProperty<T, ?>> readablePropertiesFromClass(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyStreamFromClass(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<ReadableProperty<T, ?>> readablePropertiesFromClass(
            @NonNull TypeToken<T> type) {
        return readablePropertiesFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<ReadableProperty<T, ?>> readablePropertiesFromClass(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyStreamFromClass(type, propertyAccessorMethodFormats)
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
    public static <T> Stream<WritableProperty<T, ?>> writablePropertyStreamFromClass(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyStreamFromClass((TypeToken<T>) TypeToken.of(type),
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
    public static <T> Stream<WritableProperty<T, ?>> writablePropertyStreamFromClass(
            @NonNull Class<T> type) {
        return writablePropertyStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
    public static <T> Stream<WritableProperty<T, ?>> writablePropertyStreamFromClass(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyStreamFromClass(TypeToken.of(type), propertyAccessorMethodFormats);
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
            @NonNull TypeToken<T> type) {
        return writablePropertyStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
    public static <T> Stream<WritableProperty<T, ?>> writablePropertyStreamFromClass(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStreamFromClass(type, propertyAccessorMethodFormats)
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
            @NonNull Class<T> type) {
        return writablePropertiesFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> writablePropertiesFromClass(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyStreamFromClass(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }


    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> writablePropertiesFromClass(
            @NonNull TypeToken<T> type) {
        return writablePropertiesFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<WritableProperty<T, ?>> writablePropertiesFromClass(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyStreamFromClass(type, propertyAccessorMethodFormats)
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
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStreamFromClass(
            @NonNull T object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyStreamFromClass((Class<T>) object.getClass(),
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
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStreamFromClass(
            @NonNull Class<T> type) {
        return accessiblePropertyStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStreamFromClass(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyStreamFromClass(TypeToken.of(type),
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
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStreamFromClass(
            @NonNull TypeToken<T> type) {
        return accessiblePropertyStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
    public static <T> Stream<AccessibleProperty<T, ?>> accessiblePropertyStreamFromClass(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStreamFromClass(type, propertyAccessorMethodFormats)
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
            @NonNull Class<T> type) {
        return accessiblePropertiesFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<AccessibleProperty<T, ?>> accessiblePropertiesFromClass(
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyStreamFromClass(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<AccessibleProperty<T, ?>> accessiblePropertiesFromClass(
            @NonNull TypeToken<T> type) {
        return accessiblePropertiesFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<AccessibleProperty<T, ?>> accessiblePropertiesFromClass(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyStreamFromClass(type, propertyAccessorMethodFormats)
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
            @NonNull Class<T> type, @NonNull String fieldName) {
        return readablePropertyFromClassExact(type, fieldName,
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
    public static <T, R> ReadableProperty<T, R> readablePropertyFromClassExact(
            @NonNull Class<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyFromClassExact(TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T, R> ReadableProperty<T, R> readablePropertyFromClassExact(
            @NonNull TypeToken<T> type, @NonNull String fieldName) {
        return readablePropertyFromClassExact(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> ReadableProperty<T, R> readablePropertyFromClassExact(
            @NonNull TypeToken<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<T, R>readablePropertyFromClass(type, fieldName,
                        propertyAccessorMethodFormats)
                .orElseThrow(() -> ReadablePropertyNotFoundException
                        .create(type, fieldName));
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
     * @param type      需要获取可读属性{@link ReadableProperty}集合的类
     * @param fieldName 指定属性名称
     * @return 所有可读属性 {@link ReadableProperty}集合
     * @author caotc
     * @date 2019-05-10
     * @since 1.0.0
     */
    @NonNull
    public static <T, R> Optional<ReadableProperty<T, R>> readablePropertyFromClass(
            @NonNull Class<T> type, @NonNull String fieldName) {
        return readablePropertyFromClass(type, fieldName, DEFAULT_METHOD_NAME_STYLES);
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
    public static <T, R> Optional<ReadableProperty<T, R>> readablePropertyFromClass(
            @NonNull Class<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyFromClass(TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T, R> Optional<ReadableProperty<T, R>> readablePropertyFromClass(
            @NonNull TypeToken<T> type, @NonNull String fieldName) {
        return readablePropertyFromClass(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> Optional<ReadableProperty<T, R>> readablePropertyFromClass(
            @NonNull TypeToken<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyFromClassInternalCompose(type, fieldName, propertyAccessorMethodFormats);
    }

    //todo 待优化
    @SuppressWarnings("unchecked")
    @NonNull
    private static <T, R, E> Optional<ReadableProperty<T, R>> readablePropertyFromClassInternalCompose(
            @NonNull TypeToken<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        PropertyName propertyName = PropertyName.from(fieldName);
        String firstTierPropertyName = propertyName.firstTier().flat();
        if (propertyName.complex()) {
            PropertyName sub = propertyName.sub(1);
            Optional<ReadableProperty<T, E>> transferReadableProperty = readablePropertyFromClassInternal(
                    type,
                    firstTierPropertyName,
                    propertyAccessorMethodFormats);
            return transferReadableProperty
                    .flatMap(f -> {
                                Optional<ReadableProperty<E, R>> result = readablePropertyFromClassInternal(
                                        (TypeToken<E>) f.type(), sub.flat(),
                                        propertyAccessorMethodFormats);
                                return result.map(f::compose);
                            }
                    );
        }
        return readablePropertyFromClassInternal(type,
                firstTierPropertyName,
                propertyAccessorMethodFormats);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private static <T, R> Optional<ReadableProperty<T, R>> readablePropertyFromClassInternal(
            @NonNull TypeToken<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertiesFromClass(type, propertyAccessorMethodFormats)
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
            @NonNull Class<T> type, @NonNull String fieldName) {
        return writablePropertyFromClassExact(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> WritableProperty<T, R> writablePropertyFromClassExact(
            @NonNull Class<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyFromClassExact(TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T, R> WritableProperty<T, R> writablePropertyFromClassExact(
            @NonNull TypeToken<T> type, @NonNull String fieldName) {
        return writablePropertyFromClassExact(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> WritableProperty<T, R> writablePropertyFromClassExact(
            @NonNull TypeToken<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<T, R>writablePropertyFromClass(type, fieldName,
                        propertyAccessorMethodFormats)
                .orElseThrow(() -> WritablePropertyNotFoundException
                        .create(type, fieldName));
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
            @NonNull Class<T> type, @NonNull String fieldName) {
        return writablePropertyFromClass(type, fieldName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> Optional<WritableProperty<T, R>> writablePropertyFromClass(
            @NonNull Class<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyFromClass(TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T, R> Optional<WritableProperty<T, R>> writablePropertyFromClass(
            @NonNull TypeToken<T> type, @NonNull String fieldName) {
        return writablePropertyFromClass(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T, R> Optional<WritableProperty<T, R>> writablePropertyFromClass(
            @NonNull TypeToken<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertiesFromClass(type, propertyAccessorMethodFormats)
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
            @NonNull Class<T> type, @NonNull String fieldName) {
        return accessiblePropertyFromClassExact(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> AccessibleProperty<T, R> accessiblePropertyFromClassExact(
            @NonNull Class<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyFromClassExact(TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T, R> AccessibleProperty<T, R> accessiblePropertyFromClassExact(
            @NonNull TypeToken<T> type, @NonNull String fieldName) {
        return accessiblePropertyFromClassExact(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> AccessibleProperty<T, R> accessiblePropertyFromClassExact(
            @NonNull TypeToken<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<T, R>accessiblePropertyFromClass(type, fieldName,
                        propertyAccessorMethodFormats)
                .orElseThrow(() -> AccessiblePropertyNotFoundException
                        .create(type, fieldName));
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
            @NonNull Class<T> type, @NonNull String fieldName) {
        return accessiblePropertyFromClass(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T, R> Optional<AccessibleProperty<T, R>> accessiblePropertyFromClass(
            @NonNull Class<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyFromClass(TypeToken.of(type), fieldName,
                propertyAccessorMethodFormats);
    }


    @NonNull
    public static <T, R> Optional<AccessibleProperty<T, R>> accessiblePropertyFromClass(
            @NonNull TypeToken<T> type, @NonNull String fieldName) {
        return accessiblePropertyFromClass(type, fieldName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T, R> Optional<AccessibleProperty<T, R>> accessiblePropertyFromClass(
            @NonNull TypeToken<T> type, @NonNull String fieldName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertiesFromClass(type, propertyAccessorMethodFormats)
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
            @NonNull Class<T> type) {
        return propertyElementsFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElementsFromClass(
            @NonNull TypeToken<T> type) {
        return propertyElementsFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStreamFromClass(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<PropertyElement<T, ?>> propertyElementsFromClass(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStreamFromClass(type, propertyAccessorMethodFormats)
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
            @NonNull Class<T> type) {
        return propertyElementStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyElement<T, ?>> propertyElementStreamFromClass(
            @NonNull TypeToken<T> type) {
        return propertyElementStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStreamFromClass(TypeToken.of(type), propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyElement<T, ?>> propertyElementStreamFromClass(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {

        Stream<PropertyElement<T, ?>> propertyElementStream = methodInvokableStreamFromClass(type)
                .flatMap(invokable -> Arrays.stream(propertyAccessorMethodFormats)
                        .filter(methodNameStyle -> methodNameStyle.isSetInvokable(invokable)
                                || methodNameStyle.isGetInvokable(invokable))
                        .map(methodNameStyle -> PropertyElement.from(invokable, methodNameStyle.propertyName(invokable))));

        return Stream
                .concat(fieldStream(type).map(PropertyElement::from),
                        propertyElementStream);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReadersFromClass(
            @NonNull Type type) {
        return propertyReadersFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReadersFromClass(
            @NonNull Class<T> type) {
        return propertyReadersFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReadersFromClass(
            @NonNull TypeToken<T> type) {
        return propertyReadersFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyReaderStreamFromClass(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<PropertyReader<T, ?>> propertyReadersFromClass(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyReaderStreamFromClass(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> Stream<PropertyReader<T, ?>> propertyReaderStreamFromClass(
            @NonNull Type type) {
        return propertyReaderStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyReader<T, ?>> propertyReaderStreamFromClass(
            @NonNull Class<T> type) {
        return propertyReaderStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyReader<T, ?>> propertyReaderStreamFromClass(
            @NonNull TypeToken<T> type) {
        return propertyReaderStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyReaderStreamFromClass(TypeToken.of(type), propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyReader<T, ?>> propertyReaderStreamFromClass(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStreamFromClass(type, propertyAccessorMethodFormats)
                .filter(PropertyElement::isReader).map(PropertyElement::toReader);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWritersFromClass(
            @NonNull Type type) {
        return propertyWritersFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWritersFromClass(
            @NonNull Class<T> type) {
        return propertyWritersFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWritersFromClass(
            @NonNull TypeToken<T> type) {
        return propertyWritersFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyWriterStreamFromClass(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<PropertyWriter<T, ?>> propertyWritersFromClass(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyWriterStreamFromClass(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStreamFromClass(
            @NonNull Type type) {
        return propertyWriterStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStreamFromClass(
            @NonNull Class<T> type) {
        return propertyWriterStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStreamFromClass(
            @NonNull TypeToken<T> type) {
        return propertyWriterStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyWriterStreamFromClass(TypeToken.of(type), propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyWriter<T, ?>> propertyWriterStreamFromClass(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStreamFromClass(type, propertyAccessorMethodFormats)
                .filter(PropertyElement::isWriter).map(PropertyElement::toWriter);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessorsFromClass(
            @NonNull Type type) {
        return propertyAccessorsFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessorsFromClass(
            @NonNull Class<T> type) {
        return propertyAccessorsFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessorsFromClass(
            @NonNull TypeToken<T> type) {
        return propertyAccessorsFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyAccessorStreamFromClass(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessorsFromClass(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyAccessorStreamFromClass(type, propertyAccessorMethodFormats)
                .collect(ImmutableSet.toImmutableSet());
    }

    @NonNull
    public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStreamFromClass(
            @NonNull Type type) {
        return propertyAccessorStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStreamFromClass(
            @NonNull Class<T> type) {
        return propertyAccessorStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStreamFromClass(
            @NonNull TypeToken<T> type) {
        return propertyAccessorStreamFromClass(type, DEFAULT_METHOD_NAME_STYLES);
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
            @NonNull Class<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyAccessorStreamFromClass(TypeToken.of(type), propertyAccessorMethodFormats);
    }

    @NonNull
    public static <T> Stream<PropertyAccessor<T, ?>> propertyAccessorStreamFromClass(
            @NonNull TypeToken<T> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStreamFromClass(type, propertyAccessorMethodFormats)
                .filter(PropertyElement::isAccessor).map(PropertyElement::toAccessor);
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
}
