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
import com.google.common.reflect.TypeToken;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.reflect.*;
import org.caotc.unit4j.core.common.reflect.property.AccessibleProperty;
import org.caotc.unit4j.core.common.reflect.property.Property;
import org.caotc.unit4j.core.common.reflect.property.ReadableProperty;
import org.caotc.unit4j.core.common.reflect.property.WritableProperty;
import org.caotc.unit4j.core.common.reflect.property.accessor.*;
import org.caotc.unit4j.core.exception.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//TODO cache和热加载修改类对象

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

    private static final PropertyAccessorMethodFormat[] DEFAULT_METHOD_NAME_STYLES = DefaultPropertyAccessorMethodFormat
            .values();

    @NonNull
    public static Set<Field> fields(@NonNull Type type) {
        return fieldStream(type).collect(Collectors.toSet());
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
    public static Set<Field> fields(@NonNull Class<?> type) {
        return fieldStream(type).collect(Collectors.toSet());
    }

    @NonNull
    public static Set<Field> fields(@NonNull TypeToken<?> type) {
        return fieldStream(type).collect(Collectors.toSet());
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

    @NonNull
    public static Set<Field> fields(@NonNull Type type,
                                    @NonNull String fieldName) {
        return fieldStream(type).filter(field -> fieldName.equals(field.getName()))
                .collect(Collectors.toSet());
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
    public static Set<Field> fields(@NonNull Class<?> type,
                                    @NonNull String fieldName) {
        return fieldStream(type).filter(field -> fieldName.equals(field.getName()))
                .collect(Collectors.toSet());
    }

    @NonNull
    public static Set<Field> fields(@NonNull TypeToken<?> type,
                                    @NonNull String fieldName) {
        return fieldStream(type).filter(field -> fieldName.equals(field.getName()))
                .collect(Collectors.toSet());
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
    public static Set<Method> methods(@NonNull Class<?> type) {
        return methodStream(type).collect(Collectors.toSet());
    }

    @NonNull
    public static Set<Method> methods(@NonNull Type type) {
        return methodStream(type).collect(Collectors.toSet());
    }

    @NonNull
    public static Set<Method> methods(@NonNull TypeToken<?> type) {
        return methodStream(type).collect(Collectors.toSet());
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

    @NonNull
    public static <O> Set<MethodInvokable<O, ?>> methodInvokables(
            @NonNull Type type) {
        return ReflectionUtil.<O>methodInvokableStream(type).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<MethodInvokable<O, ?>> methodInvokables(
            @NonNull Class<O> type) {
        return methodInvokableStream(type).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<MethodInvokable<O, ?>> methodInvokables(
            @NonNull TypeToken<O> type) {
        return methodInvokableStream(type).collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <O> Stream<MethodInvokable<O, ?>> methodInvokableStream(
            @NonNull Type type) {
        return methodInvokableStream((TypeToken<O>) TypeToken.of(type));
    }

    @NonNull
    public static <O> Stream<MethodInvokable<O, ?>> methodInvokableStream(
            @NonNull Class<O> type) {
        return methodInvokableStream(TypeToken.of(type));
    }

    @NonNull
    public static <O> Stream<MethodInvokable<O, ?>> methodInvokableStream(
            @NonNull TypeToken<O> type) {
        return methodStream(type).map(method -> Invokable.from(method, type));
    }

    @NonNull
    public static <O> Set<Constructor<O>> constructors(@NonNull Type type) {
        return ReflectionUtil.<O>constructorStream(type).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<Constructor<O>> constructors(@NonNull Class<O> type) {
        return constructorStream(type).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<Constructor<O>> constructors(
            @NonNull TypeToken<O> type) {
        return constructorStream(type).collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <O> Stream<Constructor<O>> constructorStream(@NonNull Type type) {
        return constructorStream((TypeToken<O>) TypeToken.of(type));
    }

    @NonNull
    public static <O> Stream<Constructor<O>> constructorStream(@NonNull Class<O> type) {
        return constructorStream(TypeToken.of(type));
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <O> Stream<Constructor<O>> constructorStream(
            @NonNull TypeToken<O> type) {
        return Arrays.stream(type.getRawType().getDeclaredConstructors())
                /*
                  Class#getDeclaredConstructors()方法由于数组不支持泛型,所以返回值为Constructor<?>[].
                  实际上必然是Constructor<O>,可强制转换
                 */
                .map(constructor -> (Constructor<O>) constructor);
    }

    @NonNull
    public static <O> Set<ConstructorInvokable<O>> constructorInvokables(
            @NonNull Type type) {
        return ReflectionUtil.<O>constructorInvokableStream(type).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<ConstructorInvokable<O>> constructorInvokables(
            @NonNull Class<O> type) {
        return constructorInvokableStream(type).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<ConstructorInvokable<O>> constructorInvokables(
            @NonNull TypeToken<O> type) {
        return constructorInvokableStream(type).collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <O> Stream<ConstructorInvokable<O>> constructorInvokableStream(
            @NonNull Type type) {
        return constructorInvokableStream((TypeToken<O>) TypeToken.of(type));
    }

    @NonNull
    public static <O> Stream<ConstructorInvokable<O>> constructorInvokableStream(
            @NonNull Class<O> type) {
        return constructorInvokableStream(TypeToken.of(type));
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <O> Stream<ConstructorInvokable<O>> constructorInvokableStream(
            @NonNull TypeToken<O> type) {
        return Arrays.stream(type.getRawType().getDeclaredConstructors())
                /*
                Class#getDeclaredConstructors()方法由于数组不支持泛型,所以返回值为Constructor<?>[].
                实际上必然是Constructor<O>,可强制转换
                 */
                .map(constructor -> Invokable.from((Constructor<O>) constructor, type));
    }

    @NonNull
    public static <O> Set<Invokable<O, ?>> invokables(
            @NonNull Type type) {
        return ReflectionUtil.<O>invokableStream(type).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<Invokable<O, ?>> invokables(
            @NonNull Class<O> type) {
        return invokableStream(type).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<Invokable<O, ?>> invokables(
            @NonNull TypeToken<O> type) {
        return invokableStream(type).collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <O> Stream<Invokable<O, ?>> invokableStream(
            @NonNull Type type) {
        return invokableStream((TypeToken<O>) TypeToken.of(type));
    }

    @NonNull
    public static <O> Stream<Invokable<O, ?>> invokableStream(
            @NonNull Class<O> type) {
        return invokableStream(TypeToken.of(type));
    }

    @NonNull
    public static <O> Stream<Invokable<O, ?>> invokableStream(
            @NonNull TypeToken<O> type) {
        return Stream.concat(constructorInvokableStream(type), methodInvokableStream(type));
    }

    @NonNull
    public Set<Method> getMethods(@NonNull Type type) {
        return getMethods(TypeToken.of(type));
    }

    @NonNull
    public Set<Method> getMethods(@NonNull Class<?> type) {
        return getMethods(TypeToken.of(type));
    }

    @NonNull
    public Set<Method> getMethods(@NonNull TypeToken<?> type) {
        return getMethodStream(type).collect(Collectors.toSet());
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
                .filter(method -> isPropertyReader(method, DefaultPropertyAccessorMethodFormat.JAVA_BEAN));
    }

    @NonNull
    public Set<Method> setMethods(@NonNull Type type) {
        return setMethods(TypeToken.of(type));
    }

    @NonNull
    public Set<Method> setMethods(@NonNull Class<?> type) {
        return setMethods(TypeToken.of(type));
    }

    @NonNull
    public Set<Method> setMethods(@NonNull TypeToken<?> type) {
        return setMethodStream(type)
                .collect(Collectors.toSet());
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
                .filter(method -> isPropertyWriter(method, DefaultPropertyAccessorMethodFormat.JAVA_BEAN));
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
        return getMethod(type, fieldName)
                .orElseThrow(() -> GetMethodNotFoundException.create(type, fieldName));
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
                .filter(getMethod -> fieldName.equals(DefaultPropertyAccessorMethodFormat.JAVA_BEAN.propertyNameFromPropertyReader(getMethod)))
                .filter(method -> !method.isBridge())
                /*
                  这些方法必然签名相同,有接口方法与父类方法之间平级关系和子类与接口或父类方法的重写关系.
                  平级关系时保留哪个均可,最终一定会跟子类重写方法进行判定,返回子类重写方法
                 */
                .reduce((m1, m2) -> isOverridden(m1, m2) ? m1 : m2);
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
                                 @NonNull String propertyName, @NonNull TypeToken<?> propertyType) {
        return setMethod(type, propertyName, propertyType)
                .orElseThrow(() -> SetMethodNotFoundException.create(type, propertyName, propertyType));
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
    public Optional<Method> setMethod(@NonNull TypeToken<?> type, @NonNull String propertyName) {
        ImmutableSet<Method> setMethods = setMethods(type, propertyName).stream()
                .filter(method -> !method.isBridge())//桥接方法一定有对应更具体类型的方法，因此一定不会被选为最后的方法
                .collect(ImmutableSet.toImmutableSet());
        if (setMethods.size() > 1) {
            ImmutableSet<Class<?>> propertyTypes = setMethods.stream()
                    .map(Method::getParameterTypes)
                    .flatMap(Arrays::stream)
                    .collect(ImmutableSet.toImmutableSet());
            //当有多个属性类型时，如果所有类型形成单链条的继承关系，选择最子类
            ImmutableSet<Class<?>> subPropertyTypes = propertyTypes.stream()
                    .filter(propertyType1 -> propertyTypes.stream().filter(propertyType2 -> !propertyType1.equals(propertyType2)).noneMatch(propertyType1::isAssignableFrom))
                    .collect(ImmutableSet.toImmutableSet());
            setMethods = setMethods.stream().filter(setMethod -> subPropertyTypes.contains(setMethod.getParameterTypes()[0]))
                    .collect(ImmutableSet.toImmutableSet());
            Preconditions.checkState(subPropertyTypes.size() == 1, "SetMethod for %s is not only and can not choose,methods:%s", propertyName, setMethods);
        }
        return setMethods.stream()
                //这些方法必然方法签名相同,有接口方法与父类/接口方法之间平级关系和子类与接口或父类方法的重写关系.
                // 平级关系时保留哪个均可,最终一定会跟子类重写方法进行判定,返回子类重写方法
                .reduce((m1, m2) -> isOverridden(m1, m2) ? m1 : m2);
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
                .filter(setMethod -> TypeToken.of(setMethod.getParameterTypes()[0]).equals(fieldType)
                        //需要支持根据泛型类型查询
                        || type.resolveType(setMethod.getGenericParameterTypes()[0]).equals(fieldType))
                //这些方法必然方法签名相同,有接口方法与父类方法之间平级关系和子类与接口或父类方法的重写关系.
                // 平级关系时保留哪个均可,最终一定会跟子类重写方法进行判定,返回子类重写方法
                .reduce((m1, m2) -> isOverridden(m1, m2) ? m1 : m2);
    }

    @NonNull
    public Set<Method> setMethods(@NonNull Type type,
                                  @NonNull String fieldName) {
        return setMethods(TypeToken.of(type), fieldName);
    }

    @NonNull
    public Set<Method> setMethods(@NonNull Class<?> type,
                                  @NonNull String fieldName) {
        return setMethods(TypeToken.of(type), fieldName);
    }

    @NonNull
    public Set<Method> setMethods(@NonNull TypeToken<?> type,
                                  @NonNull String fieldName) {
        return setMethodStream(type, fieldName)
                .collect(Collectors.toSet());
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
                .filter(setMethod -> fieldName.equals(DefaultPropertyAccessorMethodFormat.JAVA_BEAN.propertyNameFromPropertyWriter(setMethod)));
    }

    @NonNull
    public static <O> Stream<Property<O, ?>> propertyStream(
            @NonNull O object) {
        return propertyStream(object, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<Property<O, ?>> propertyStream(
            @NonNull O object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyStream(object.getClass(),
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
    public static <O> Stream<Property<O, ?>> propertyStream(
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
    public static <O> Stream<Property<O, ?>> propertyStream(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStream((TypeToken<O>) TypeToken.of(type),
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
    public static <O> Stream<Property<O, ?>> propertyStream(
            @NonNull Class<O> type) {
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
    public static <O> Stream<Property<O, ?>> propertyStream(
            @NonNull Class<O> type,
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
    public static <O> Stream<Property<O, ?>> propertyStream(
            @NonNull TypeToken<O> type) {
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
    public static <O> Stream<Property<O, ?>> propertyStream(
            @NonNull TypeToken<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {

        ImmutableListMultimap<String, PropertyElement<O, ?>> propertyNameToPropertyElements =
                propertyElementStream(type, propertyAccessorMethodFormats)
                        .collect(ImmutableListMultimap.toImmutableListMultimap(PropertyElement::propertyName, Function.identity()));
        //todo propertyType和returning是否应该放弃P1 extends P的约束,propertyStream是否应该返回<O,Object>
        return propertyNameToPropertyElements.asMap().values().stream()
                .map(propertyElements -> propertyElements.stream().map(o -> o))
                .map(Property::create);
    }

    @NonNull
    public static <O> Set<Property<O, ?>> properties(
            @NonNull O object) {
        return properties(object, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<Property<O, ?>> properties(
            @NonNull O object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyStream(object.getClass(), propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<Property<O, ?>> properties(
            @NonNull Type type) {
        return properties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<Property<O, ?>> properties(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<Property<O, ?>> properties(
            @NonNull Class<O> type) {
        return properties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<Property<O, ?>> properties(
            @NonNull Class<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<Property<O, ?>> properties(
            @NonNull TypeToken<O> type) {
        return properties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<Property<O, ?>> properties(
            @NonNull TypeToken<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    public static <O> Stream<ReadableProperty<O, ?>> readablePropertyStream(
            @NonNull O object) {
        return readablePropertyStream(object, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<ReadableProperty<O, ?>> readablePropertyStream(
            @NonNull O object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.propertyStream(object, propertyAccessorMethodFormats)
                .filter(Property::readable)
                .map(Property::toReadable);
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
    public static <O> Stream<ReadableProperty<O, ?>> readablePropertyStream(
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
    @NonNull
    public static <O> Stream<ReadableProperty<O, ?>> readablePropertyStream(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyStream(type, propertyAccessorMethodFormats)
                .filter(Property::readable)
                .map(Property::toReadable);
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
    public static <O> Stream<ReadableProperty<O, ?>> readablePropertyStream(
            @NonNull Class<O> type) {
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
    public static <O> Stream<ReadableProperty<O, ?>> readablePropertyStream(
            @NonNull Class<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStream(type, propertyAccessorMethodFormats)
                .filter(Property::readable)
                .map(Property::toReadable);
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
    public static <O> Stream<ReadableProperty<O, ?>> readablePropertyStream(
            @NonNull TypeToken<O> type) {
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
    public static <O> Stream<ReadableProperty<O, ?>> readablePropertyStream(
            @NonNull TypeToken<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStream(type, propertyAccessorMethodFormats)
                .filter(Property::readable)
                .map(Property::toReadable);
    }

    @NonNull
    public static <O> Set<ReadableProperty<O, ?>> readableProperties(
            @NonNull O object) {
        return readableProperties(object, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<ReadableProperty<O, ?>> readableProperties(
            @NonNull O object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.readablePropertyStream(object, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<ReadableProperty<O, ?>> readableProperties(
            @NonNull Type type) {
        return readableProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<ReadableProperty<O, ?>> readableProperties(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>readablePropertyStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }


    @NonNull
    public static <O> Set<ReadableProperty<O, ?>> readableProperties(
            @NonNull Class<O> type) {
        return readableProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<ReadableProperty<O, ?>> readableProperties(
            @NonNull Class<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyStream(type, propertyAccessorMethodFormats)
                .collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<ReadableProperty<O, ?>> readableProperties(
            @NonNull TypeToken<O> type) {
        return readableProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<ReadableProperty<O, ?>> readableProperties(
            @NonNull TypeToken<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return readablePropertyStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Stream<WritableProperty<O, ?>> writablePropertyStream(
            @NonNull O object) {
        return writablePropertyStream(object, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<WritableProperty<O, ?>> writablePropertyStream(
            @NonNull O object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.propertyStream(object, propertyAccessorMethodFormats)
                .filter(Property::writable)
                .map(Property::toWritable);
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
    public static <O> Stream<WritableProperty<O, ?>> writablePropertyStream(
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
    @NonNull
    public static <O> Stream<WritableProperty<O, ?>> writablePropertyStream(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyStream(type, propertyAccessorMethodFormats)
                .filter(Property::writable)
                .map(Property::toWritable);
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
    public static <O> Stream<WritableProperty<O, ?>> writablePropertyStream(
            @NonNull Class<O> type) {
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
    public static <O> Stream<WritableProperty<O, ?>> writablePropertyStream(
            @NonNull Class<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStream(type, propertyAccessorMethodFormats)
                .filter(Property::writable)
                .map(Property::toWritable);
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
    public static <O> Stream<WritableProperty<O, ?>> writablePropertyStream(
            @NonNull TypeToken<O> type) {
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
    public static <O> Stream<WritableProperty<O, ?>> writablePropertyStream(
            @NonNull TypeToken<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStream(type, propertyAccessorMethodFormats)
                .filter(Property::writable)
                .map(Property::toWritable);
    }

    @NonNull
    public static <O> Set<WritableProperty<O, ?>> writableProperties(
            @NonNull O object) {
        return writableProperties(object, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<WritableProperty<O, ?>> writableProperties(
            @NonNull O object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.writablePropertyStream(object, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<WritableProperty<O, ?>> writableProperties(
            @NonNull Type type) {
        return writableProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<WritableProperty<O, ?>> writableProperties(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>writablePropertyStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }


    @NonNull
    public static <O> Set<WritableProperty<O, ?>> writableProperties(
            @NonNull Class<O> type) {
        return writableProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<WritableProperty<O, ?>> writableProperties(
            @NonNull Class<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyStream(type, propertyAccessorMethodFormats)
                .collect(Collectors.toSet());
    }


    @NonNull
    public static <O> Set<WritableProperty<O, ?>> writableProperties(
            @NonNull TypeToken<O> type) {
        return writableProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<WritableProperty<O, ?>> writableProperties(
            @NonNull TypeToken<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return writablePropertyStream(type, propertyAccessorMethodFormats)
                .collect(Collectors.toSet());
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
    public static <O> Stream<AccessibleProperty<O, ?>> accessiblePropertyStream(
            @NonNull O object) {
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
    @NonNull
    public static <O> Stream<AccessibleProperty<O, ?>> accessiblePropertyStream(
            @NonNull O object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyStream(object.getClass(), propertyAccessorMethodFormats)
                .filter(Property::accessible)
                .map(Property::toAccessible);
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
    public static <O> Stream<AccessibleProperty<O, ?>> accessiblePropertyStream(
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
    @NonNull
    public static <O> Stream<AccessibleProperty<O, ?>> accessiblePropertyStream(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyStream(type, propertyAccessorMethodFormats)
                .filter(Property::accessible)
                .map(Property::toAccessible);
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
    public static <O> Stream<AccessibleProperty<O, ?>> accessiblePropertyStream(
            @NonNull Class<O> type) {
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
    public static <O> Stream<AccessibleProperty<O, ?>> accessiblePropertyStream(
            @NonNull Class<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStream(type, propertyAccessorMethodFormats)
                .filter(Property::accessible)
                .map(Property::toAccessible);
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
    public static <O> Stream<AccessibleProperty<O, ?>> accessiblePropertyStream(
            @NonNull TypeToken<O> type) {
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
    public static <O> Stream<AccessibleProperty<O, ?>> accessiblePropertyStream(
            @NonNull TypeToken<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyStream(type, propertyAccessorMethodFormats)
                .filter(Property::accessible)
                .map(Property::toAccessible);
    }

    @NonNull
    public static <O> Set<AccessibleProperty<O, ?>> accessibleProperties(
            @NonNull O object) {
        return accessibleProperties(object, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<AccessibleProperty<O, ?>> accessibleProperties(
            @NonNull O object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.accessiblePropertyStream(object, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<AccessibleProperty<O, ?>> accessibleProperties(
            @NonNull Type type) {
        return accessibleProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<AccessibleProperty<O, ?>> accessibleProperties(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>accessiblePropertyStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<AccessibleProperty<O, ?>> accessibleProperties(
            @NonNull Class<O> type) {
        return accessibleProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<AccessibleProperty<O, ?>> accessibleProperties(
            @NonNull Class<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<AccessibleProperty<O, ?>> accessibleProperties(
            @NonNull TypeToken<O> type) {
        return accessibleProperties(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<AccessibleProperty<O, ?>> accessibleProperties(
            @NonNull TypeToken<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return accessiblePropertyStream(type, propertyAccessorMethodFormats)
                .collect(Collectors.toSet());
    }

    @NonNull
    public static <O, P> Property<O, P> propertyExact(
            @NonNull O object, @NonNull String propertyName) {
        return propertyExact(object, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> Property<O, P> propertyExact(
            @NonNull O object, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(object.getClass(), propertyName, propertyAccessorMethodFormats)
                .orElseThrow(() -> PropertyNotFoundException.create(object.getClass(), propertyName));
    }

    @NonNull
    public static <O, P> Property<O, P> propertyExact(
            @NonNull Type type, @NonNull String propertyName) {
        return propertyExact(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> Property<O, P> propertyExact(
            @NonNull Type type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(type, propertyName, propertyAccessorMethodFormats)
                .orElseThrow(() -> PropertyNotFoundException.create(type, propertyName));
    }

    @NonNull
    public static <O, P> Property<O, P> propertyExact(
            @NonNull Class<O> type, @NonNull String propertyName) {
        return propertyExact(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * 获取指定{@link Property}
     *
     * @param type         需要获取可读属性{@link Property}的类
     * @param propertyName 属性名称
     * @return {@link Property}
     * @throws PropertyNotFoundException PropertyNotFoundException
     * @author caotc
     * @date 2019-05-10
     * @apiNote included super interfaces and superclasses
     * @since 1.0.0
     */
    @NonNull
    public static <O, P> Property<O, P> propertyExact(
            @NonNull Class<O> type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(type, propertyName, propertyAccessorMethodFormats)
                .orElseThrow(() -> PropertyNotFoundException.create(type, propertyName));
    }

    @NonNull
    public static <O, P> Property<O, P> propertyExact(
            @NonNull TypeToken<O> type, @NonNull String propertyName) {
        return propertyExact(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> Property<O, P> propertyExact(
            @NonNull TypeToken<O> type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(type, propertyName, propertyAccessorMethodFormats)
                .orElseThrow(() -> PropertyNotFoundException.create(type, propertyName));
    }

    public static <O, P> Optional<Property<O, P>> property(
            @NonNull O object, @NonNull String propertyName) {
        return property(object, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    public static <O, P> Optional<Property<O, P>> property(
            @NonNull O object, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(object.getClass(), propertyName, propertyAccessorMethodFormats);
    }

    public static <O, P> Optional<Property<O, P>> property(
            @NonNull Type type, @NonNull String propertyName) {
        return property(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @SuppressWarnings("unchecked")
    public static <O, P> Optional<Property<O, P>> property(
            @NonNull Type type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return property((TypeToken<O>) TypeToken.of(type), propertyName, propertyAccessorMethodFormats);
    }

    /**
     * 从传入的类中获取包括所有超类和接口的所有属性{@link Property}集合
     *
     * @param type         需要获取可读属性{@link Property}集合的类
     * @param propertyName 指定属性名称
     * @return 所有可读属性 {@link Property}集合
     * @author caotc
     * @date 2019-05-10
     * @since 1.0.0
     */
    @NonNull
    public static <O, P> Optional<Property<O, P>> property(
            @NonNull Class<O> type, @NonNull String propertyName) {
        return property(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * 从传入的类中获取包括所有超类和接口的所有可读属性{@link Property}集合
     *
     * @param type                          需要获取可读属性{@link Property}的类
     * @param propertyName                  指定属性名称
     * @param propertyAccessorMethodFormats get方法格式集合
     * @return 所有可读属性 {@link Property}集合
     * @author caotc
     * @date 2019-05-10
     * @apiNote 如果只想要获取JavaBean规范的get方法, {@code methodNameStyles}参数使用{@link
     * DefaultPropertyAccessorMethodFormat#JAVA_BEAN}
     * @since 1.0.0
     */
    @NonNull
    public static <O, P> Optional<Property<O, P>> property(
            @NonNull Class<O> type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return property(TypeToken.of(type), propertyName, propertyAccessorMethodFormats);
    }


    @NonNull
    public static <O, P> Optional<Property<O, P>> property(
            @NonNull TypeToken<O> type, @NonNull String propertyName) {
        return property(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> Optional<Property<O, P>> property(
            @NonNull TypeToken<O> type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return property(type, PropertyName.from(propertyName), propertyAccessorMethodFormats);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private static <O, P> Optional<Property<O, P>> property(
            @NonNull TypeToken<O> type, @NonNull PropertyName propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        if (propertyName.composite()) {
            return property(type, propertyName.removeLastTier(), propertyAccessorMethodFormats)
                    .filter(Property::readable)
                    .map(Property::toReadable)
                    .flatMap(transferProperty -> ReflectionUtil.<Object, P>property(
                            transferProperty.type(), propertyName.lastTier(),
                            propertyAccessorMethodFormats).map(transferProperty::compose)
                    );
        }
        return properties(type, propertyAccessorMethodFormats)
                .stream()
                .filter(property -> property.name().equals(propertyName.flat()))
                .map(property -> (Property<O, P>) property)
                .findAny();
    }

    @NonNull
    public static <O, P> ReadableProperty<O, P> readablePropertyExact(
            @NonNull O object, @NonNull String propertyName) {
        return readablePropertyExact(object, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> ReadableProperty<O, P> readablePropertyExact(
            @NonNull O object, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>readableProperty(object.getClass(), propertyName, propertyAccessorMethodFormats)
                .orElseThrow(() -> ReadablePropertyNotFoundException.create(object.getClass(), propertyName));
    }

    @NonNull
    public static <O, P> ReadableProperty<O, P> readablePropertyExact(
            @NonNull Type type, @NonNull String propertyName) {
        return readablePropertyExact(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> ReadableProperty<O, P> readablePropertyExact(
            @NonNull Type type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>readableProperty(type, propertyName, propertyAccessorMethodFormats)
                .orElseThrow(() -> ReadablePropertyNotFoundException.create(type, propertyName));
    }


    @NonNull
    public static <O, P> ReadableProperty<O, P> readablePropertyExact(
            @NonNull Class<O> type, @NonNull String propertyName) {
        return readablePropertyExact(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * 获取指定{@link ReadableProperty}
     *
     * @param type         需要获取可读属性{@link ReadableProperty}的类
     * @param propertyName 属性名称
     * @return {@link ReadableProperty}
     * @throws ReadablePropertyNotFoundException readablePropertyNotFoundException
     * @author caotc
     * @date 2019-05-10
     * @apiNote included super interfaces and superclasses
     * @since 1.0.0
     */
    @NonNull
    public static <O, P> ReadableProperty<O, P> readablePropertyExact(
            @NonNull Class<O> type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>readableProperty(type, propertyName, propertyAccessorMethodFormats)
                .orElseThrow(() -> ReadablePropertyNotFoundException.create(type, propertyName));
    }

    @NonNull
    public static <O, P> ReadableProperty<O, P> readablePropertyExact(
            @NonNull TypeToken<O> type, @NonNull String propertyName) {
        return readablePropertyExact(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> ReadableProperty<O, P> readablePropertyExact(
            @NonNull TypeToken<O> type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>readableProperty(type, propertyName, propertyAccessorMethodFormats)
                .orElseThrow(() -> ReadablePropertyNotFoundException.create(type, propertyName));
    }

    public static <O, P> Optional<ReadableProperty<O, P>> readableProperty(
            @NonNull O object, @NonNull String propertyName) {
        return readableProperty(object, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    public static <O, P> Optional<ReadableProperty<O, P>> readableProperty(
            @NonNull O object, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(object, propertyName, propertyAccessorMethodFormats)
                .filter(Property::readable)
                .map(Property::toReadable);
    }

    public static <O, P> Optional<ReadableProperty<O, P>> readableProperty(
            @NonNull Type type, @NonNull String propertyName) {
        return readableProperty(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    public static <O, P> Optional<ReadableProperty<O, P>> readableProperty(
            @NonNull Type type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(type, propertyName, propertyAccessorMethodFormats)
                .filter(Property::readable)
                .map(Property::toReadable);
    }

    /**
     * 从传入的类中获取包括所有超类和接口的所有可读属性{@link ReadableProperty}集合
     *
     * @param type         需要获取可读属性{@link ReadableProperty}集合的类
     * @param propertyName 指定属性名称
     * @return 所有可读属性 {@link ReadableProperty}集合
     * @author caotc
     * @date 2019-05-10
     * @since 1.0.0
     */
    @NonNull
    public static <O, P> Optional<ReadableProperty<O, P>> readableProperty(
            @NonNull Class<O> type, @NonNull String propertyName) {
        return readableProperty(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    /**
     * 从传入的类中获取包括所有超类和接口的所有可读属性{@link ReadableProperty}集合
     *
     * @param type                          需要获取可读属性{@link ReadableProperty}的类
     * @param propertyName                  指定属性名称
     * @param propertyAccessorMethodFormats get方法格式集合
     * @return 所有可读属性 {@link ReadableProperty}集合
     * @author caotc
     * @date 2019-05-10
     * @apiNote 如果只想要获取JavaBean规范的get方法, {@code methodNameStyles}参数使用{@link
     * DefaultPropertyAccessorMethodFormat#JAVA_BEAN}
     * @since 1.0.0
     */
    @NonNull
    public static <O, P> Optional<ReadableProperty<O, P>> readableProperty(
            @NonNull Class<O> type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(type, propertyName, propertyAccessorMethodFormats)
                .filter(Property::readable)
                .map(Property::toReadable);
    }


    @NonNull
    public static <O, P> Optional<ReadableProperty<O, P>> readableProperty(
            @NonNull TypeToken<O> type, @NonNull String propertyName) {
        return readableProperty(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> Optional<ReadableProperty<O, P>> readableProperty(
            @NonNull TypeToken<O> type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(type, propertyName, propertyAccessorMethodFormats)
                .filter(Property::readable)
                .map(Property::toReadable);
    }

    @NonNull
    public static <O, P> WritableProperty<O, P> writablePropertyExact(
            @NonNull O object, @NonNull String propertyName) {
        return writablePropertyExact(object, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> WritableProperty<O, P> writablePropertyExact(
            @NonNull O object, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>writableProperty(object.getClass(), propertyName, propertyAccessorMethodFormats)
                .orElseThrow(() -> WritablePropertyNotFoundException.create(object.getClass(), propertyName));
    }

    @NonNull
    public static <O, P> WritableProperty<O, P> writablePropertyExact(
            @NonNull Type type, @NonNull String propertyName) {
        return writablePropertyExact(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> WritableProperty<O, P> writablePropertyExact(
            @NonNull Type type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>writableProperty(type, propertyName, propertyAccessorMethodFormats)
                .orElseThrow(() -> WritablePropertyNotFoundException.create(type, propertyName));
    }

    @NonNull
    public static <O, P> WritableProperty<O, P> writablePropertyExact(
            @NonNull Class<O> type, @NonNull String propertyName) {
        return writablePropertyExact(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> WritableProperty<O, P> writablePropertyExact(
            @NonNull Class<O> type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>writableProperty(type, propertyName, propertyAccessorMethodFormats)
                .orElseThrow(() -> WritablePropertyNotFoundException.create(type, propertyName));
    }


    @NonNull
    public static <O, P> WritableProperty<O, P> writablePropertyExact(
            @NonNull TypeToken<O> type, @NonNull String propertyName) {
        return writablePropertyExact(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> WritableProperty<O, P> writablePropertyExact(
            @NonNull TypeToken<O> type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>writableProperty(type, propertyName, propertyAccessorMethodFormats)
                .orElseThrow(() -> WritablePropertyNotFoundException.create(type, propertyName));
    }

    @NonNull
    public static <O, P> Optional<WritableProperty<O, P>> writableProperty(
            @NonNull O object, @NonNull String propertyName) {
        return writableProperty(object, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> Optional<WritableProperty<O, P>> writableProperty(
            @NonNull O object, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(object, propertyName, propertyAccessorMethodFormats)
                .filter(Property::writable)
                .map(Property::toWritable);
    }

    @NonNull
    public static <O, P> Optional<WritableProperty<O, P>> writableProperty(
            @NonNull Type type, @NonNull String propertyName) {
        return writableProperty(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> Optional<WritableProperty<O, P>> writableProperty(
            @NonNull Type type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(type, propertyName, propertyAccessorMethodFormats)
                .filter(Property::writable)
                .map(Property::toWritable);
    }

    @NonNull
    public static <O, P> Optional<WritableProperty<O, P>> writableProperty(
            @NonNull Class<O> type, @NonNull String propertyName) {
        return writableProperty(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> Optional<WritableProperty<O, P>> writableProperty(
            @NonNull Class<O> type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(type, propertyName, propertyAccessorMethodFormats)
                .filter(Property::writable)
                .map(Property::toWritable);
    }

    @NonNull
    public static <O, P> Optional<WritableProperty<O, P>> writableProperty(
            @NonNull TypeToken<O> type, @NonNull String propertyName) {
        return writableProperty(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> Optional<WritableProperty<O, P>> writableProperty(
            @NonNull TypeToken<O> type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(type, propertyName, propertyAccessorMethodFormats)
                .filter(Property::writable)
                .map(Property::toWritable);
    }

    @NonNull
    public static <O, P> AccessibleProperty<O, P> accessiblePropertyExact(
            @NonNull O object, @NonNull String propertyName) {
        return accessiblePropertyExact(object, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> AccessibleProperty<O, P> accessiblePropertyExact(
            @NonNull O object, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>accessibleProperty(object.getClass(), propertyName, propertyAccessorMethodFormats)
                .orElseThrow(() -> AccessiblePropertyNotFoundException.create(object.getClass(), propertyName));
    }

    @NonNull
    public static <O, P> AccessibleProperty<O, P> accessiblePropertyExact(
            @NonNull Type type, @NonNull String propertyName) {
        return accessiblePropertyExact(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> AccessibleProperty<O, P> accessiblePropertyExact(
            @NonNull Type type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>accessibleProperty(type, propertyName, propertyAccessorMethodFormats)
                .orElseThrow(() -> AccessiblePropertyNotFoundException.create(type, propertyName));
    }

    @NonNull
    public static <O, P> AccessibleProperty<O, P> accessiblePropertyExact(
            @NonNull Class<O> type, @NonNull String propertyName) {
        return accessiblePropertyExact(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> AccessibleProperty<O, P> accessiblePropertyExact(
            @NonNull Class<O> type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>accessibleProperty(type, propertyName, propertyAccessorMethodFormats)
                .orElseThrow(() -> AccessiblePropertyNotFoundException.create(type, propertyName));
    }

    @NonNull
    public static <O, P> AccessibleProperty<O, P> accessiblePropertyExact(
            @NonNull TypeToken<O> type, @NonNull String propertyName) {
        return accessiblePropertyExact(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> AccessibleProperty<O, P> accessiblePropertyExact(
            @NonNull TypeToken<O> type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>accessibleProperty(type, propertyName, propertyAccessorMethodFormats)
                .orElseThrow(() -> AccessiblePropertyNotFoundException.create(type, propertyName));
    }

    @NonNull
    public static <O, P> Optional<AccessibleProperty<O, P>> accessibleProperty(
            @NonNull O object, @NonNull String propertyName) {
        return accessibleProperty(object, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> Optional<AccessibleProperty<O, P>> accessibleProperty(
            @NonNull O object, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(object, propertyName, propertyAccessorMethodFormats)
                .filter(Property::accessible)
                .map(Property::toAccessible);
    }

    @NonNull
    public static <O, P> Optional<AccessibleProperty<O, P>> accessibleProperty(
            @NonNull Type type, @NonNull String propertyName) {
        return accessibleProperty(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> Optional<AccessibleProperty<O, P>> accessibleProperty(
            @NonNull Type type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(type, propertyName, propertyAccessorMethodFormats)
                .filter(Property::accessible)
                .map(Property::toAccessible);
    }


    @NonNull
    public static <O, P> Optional<AccessibleProperty<O, P>> accessibleProperty(
            @NonNull Class<O> type, @NonNull String propertyName) {
        return accessibleProperty(type, propertyName,
                DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> Optional<AccessibleProperty<O, P>> accessibleProperty(
            @NonNull Class<O> type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(type, propertyName, propertyAccessorMethodFormats)
                .filter(Property::accessible)
                .map(Property::toAccessible);
    }


    @NonNull
    public static <O, P> Optional<AccessibleProperty<O, P>> accessibleProperty(
            @NonNull TypeToken<O> type, @NonNull String propertyName) {
        return accessibleProperty(type, propertyName, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O, P> Optional<AccessibleProperty<O, P>> accessibleProperty(
            @NonNull TypeToken<O> type, @NonNull String propertyName,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O, P>property(type, propertyName, propertyAccessorMethodFormats)
                .filter(Property::accessible)
                .map(Property::toAccessible);
    }

    @NonNull
    public static <O> Set<PropertyElement<O, ?>> propertyElements(@NonNull O object) {
        return propertyElements(object, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<PropertyElement<O, ?>> propertyElements(@NonNull Type type) {
        return propertyElements(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<PropertyElement<O, ?>> propertyElements(@NonNull Class<O> type) {
        return propertyElements(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<PropertyElement<O, ?>> propertyElements(@NonNull TypeToken<O> type) {
        return propertyElements(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<PropertyElement<O, ?>> propertyElements(
            @NonNull O object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyElementStream(object.getClass(), propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<PropertyElement<O, ?>> propertyElements(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyElementStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<PropertyElement<O, ?>> propertyElements(
            @NonNull Class<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<PropertyElement<O, ?>> propertyElements(
            @NonNull TypeToken<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Stream<PropertyElement<O, ?>> propertyElementStream(
            @NonNull O object) {
        return propertyElementStream(object, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<PropertyElement<O, ?>> propertyElementStream(
            @NonNull Type type) {
        return propertyElementStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<PropertyElement<O, ?>> propertyElementStream(
            @NonNull Class<O> type) {
        return propertyElementStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<PropertyElement<O, ?>> propertyElementStream(
            @NonNull TypeToken<O> type) {
        return propertyElementStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<PropertyElement<O, ?>> propertyElementStream(
            @NonNull O object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyElementStream(object.getClass(),
                propertyAccessorMethodFormats);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <O> Stream<PropertyElement<O, ?>> propertyElementStream(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStream((TypeToken<O>) TypeToken.of(type),
                propertyAccessorMethodFormats);
    }

    @NonNull
    public static <O> Stream<PropertyElement<O, ?>> propertyElementStream(
            @NonNull Class<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStream(TypeToken.of(type), propertyAccessorMethodFormats);
    }

    //todo fluent风格的属性方法应该永远存在
    @NonNull
    public static <O> Stream<PropertyElement<O, ?>> propertyElementStream(
            @NonNull TypeToken<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {

        Stream<PropertyElement<O, ?>> propertyElementStream = methodStream(type)
                .flatMap(method -> Arrays.stream(propertyAccessorMethodFormats)
                        .filter(methodNameStyle -> methodNameStyle.isPropertyWriter(method)
                                || methodNameStyle.isPropertyReader(method))
                        .map(methodNameStyle -> PropertyElement.from(type, method, methodNameStyle.propertyName(method))));
        return Stream
                .concat(fieldStream(type).filter(field -> isPropertyReader(field) || isPropertyWriter(field))
                                .map(field -> PropertyElement.from(type, field)),
                        propertyElementStream);
    }

    @NonNull
    public static <O> Set<PropertyReader<O, ?>> propertyReaders(
            @NonNull O object) {
        return propertyReaders(object, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<PropertyReader<O, ?>> propertyReaders(
            @NonNull Type type) {
        return propertyReaders(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<PropertyReader<O, ?>> propertyReaders(
            @NonNull Class<O> type) {
        return propertyReaders(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<PropertyReader<O, ?>> propertyReaders(
            @NonNull TypeToken<O> type) {
        return propertyReaders(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<PropertyReader<O, ?>> propertyReaders(
            @NonNull O object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyReaderStream(object.getClass(), propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<PropertyReader<O, ?>> propertyReaders(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyReaderStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<PropertyReader<O, ?>> propertyReaders(
            @NonNull Class<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyReaderStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<PropertyReader<O, ?>> propertyReaders(
            @NonNull TypeToken<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyReaderStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Stream<PropertyReader<O, ?>> propertyReaderStream(
            @NonNull O object) {
        return propertyReaderStream(object, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<PropertyReader<O, ?>> propertyReaderStream(
            @NonNull Type type) {
        return propertyReaderStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<PropertyReader<O, ?>> propertyReaderStream(
            @NonNull Class<O> type) {
        return propertyReaderStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<PropertyReader<O, ?>> propertyReaderStream(
            @NonNull TypeToken<O> type) {
        return propertyReaderStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<PropertyReader<O, ?>> propertyReaderStream(
            @NonNull O object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.propertyElementStream(object, propertyAccessorMethodFormats)
                .filter(PropertyElement::isReader)
                .map(PropertyElement::toReader);
    }

    @NonNull
    public static <O> Stream<PropertyReader<O, ?>> propertyReaderStream(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyElementStream(type, propertyAccessorMethodFormats)
                .filter(PropertyElement::isReader)
                .map(PropertyElement::toReader);
    }

    @NonNull
    public static <O> Stream<PropertyReader<O, ?>> propertyReaderStream(
            @NonNull Class<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyReaderStream(TypeToken.of(type), propertyAccessorMethodFormats);
    }

    @NonNull
    public static <O> Stream<PropertyReader<O, ?>> propertyReaderStream(
            @NonNull TypeToken<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStream(type, propertyAccessorMethodFormats)
                .filter(PropertyElement::isReader)
                .map(PropertyElement::toReader);
    }

    @NonNull
    public static <O> Set<PropertyWriter<O, ?>> propertyWriters(
            @NonNull O object) {
        return propertyWriters(object, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<PropertyWriter<O, ?>> propertyWriters(
            @NonNull Type type) {
        return propertyWriters(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<PropertyWriter<O, ?>> propertyWriters(
            @NonNull Class<O> type) {
        return propertyWriters(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<PropertyWriter<O, ?>> propertyWriters(
            @NonNull TypeToken<O> type) {
        return propertyWriters(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<PropertyWriter<O, ?>> propertyWriters(
            @NonNull O object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyWriterStream(object.getClass(), propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<PropertyWriter<O, ?>> propertyWriters(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyWriterStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<PropertyWriter<O, ?>> propertyWriters(
            @NonNull Class<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyWriterStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<PropertyWriter<O, ?>> propertyWriters(
            @NonNull TypeToken<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyWriterStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Stream<PropertyWriter<O, ?>> propertyWriterStream(
            @NonNull O object) {
        return propertyWriterStream(object, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<PropertyWriter<O, ?>> propertyWriterStream(
            @NonNull Type type) {
        return propertyWriterStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<PropertyWriter<O, ?>> propertyWriterStream(
            @NonNull Class<O> type) {
        return propertyWriterStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<PropertyWriter<O, ?>> propertyWriterStream(
            @NonNull TypeToken<O> type) {
        return propertyWriterStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<PropertyWriter<O, ?>> propertyWriterStream(
            @NonNull O object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.propertyElementStream(object, propertyAccessorMethodFormats)
                .filter(PropertyElement::isWriter)
                .map(PropertyElement::toWriter);
    }

    @NonNull
    public static <O> Stream<PropertyWriter<O, ?>> propertyWriterStream(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyElementStream(type, propertyAccessorMethodFormats)
                .filter(PropertyElement::isWriter)
                .map(PropertyElement::toWriter);
    }

    @NonNull
    public static <O> Stream<PropertyWriter<O, ?>> propertyWriterStream(
            @NonNull Class<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStream(type, propertyAccessorMethodFormats)
                .filter(PropertyElement::isWriter)
                .map(PropertyElement::toWriter);
    }

    @NonNull
    public static <O> Stream<PropertyWriter<O, ?>> propertyWriterStream(
            @NonNull TypeToken<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStream(type, propertyAccessorMethodFormats)
                .filter(PropertyElement::isWriter)
                .map(PropertyElement::toWriter);
    }

    @NonNull
    public static <O> Set<PropertyAccessor<O, ?>> propertyAccessors(
            @NonNull O object) {
        return propertyAccessors(object, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<PropertyAccessor<O, ?>> propertyAccessors(
            @NonNull Type type) {
        return propertyAccessors(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<PropertyAccessor<O, ?>> propertyAccessors(
            @NonNull Class<O> type) {
        return propertyAccessors(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<PropertyAccessor<O, ?>> propertyAccessors(
            @NonNull TypeToken<O> type) {
        return propertyAccessors(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Set<PropertyAccessor<O, ?>> propertyAccessors(
            @NonNull O object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyAccessorStream(object.getClass(), propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<PropertyAccessor<O, ?>> propertyAccessors(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyAccessorStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<PropertyAccessor<O, ?>> propertyAccessors(
            @NonNull Class<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyAccessorStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Set<PropertyAccessor<O, ?>> propertyAccessors(
            @NonNull TypeToken<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyAccessorStream(type, propertyAccessorMethodFormats).collect(Collectors.toSet());
    }

    @NonNull
    public static <O> Stream<PropertyAccessor<O, ?>> propertyAccessorStream(
            @NonNull O object) {
        return propertyAccessorStream(object, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<PropertyAccessor<O, ?>> propertyAccessorStream(
            @NonNull Type type) {
        return propertyAccessorStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<PropertyAccessor<O, ?>> propertyAccessorStream(
            @NonNull Class<O> type) {
        return propertyAccessorStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<PropertyAccessor<O, ?>> propertyAccessorStream(
            @NonNull TypeToken<O> type) {
        return propertyAccessorStream(type, DEFAULT_METHOD_NAME_STYLES);
    }

    @NonNull
    public static <O> Stream<PropertyAccessor<O, ?>> propertyAccessorStream(
            @NonNull O object,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.propertyElementStream(object, propertyAccessorMethodFormats)
                .filter(PropertyElement::isAccessor)
                .map(PropertyElement::toAccessor);
    }

    @NonNull
    public static <O> Stream<PropertyAccessor<O, ?>> propertyAccessorStream(
            @NonNull Type type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return ReflectionUtil.<O>propertyElementStream(type, propertyAccessorMethodFormats)
                .filter(PropertyElement::isAccessor)
                .map(PropertyElement::toAccessor);
    }

    @NonNull
    public static <O> Stream<PropertyAccessor<O, ?>> propertyAccessorStream(
            @NonNull Class<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStream(type, propertyAccessorMethodFormats)
                .filter(PropertyElement::isAccessor)
                .map(PropertyElement::toAccessor);
    }

    @NonNull
    public static <O> Stream<PropertyAccessor<O, ?>> propertyAccessorStream(
            @NonNull TypeToken<O> type,
            @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return propertyElementStream(type, propertyAccessorMethodFormats)
                .filter(PropertyElement::isAccessor)
                .map(PropertyElement::toAccessor);
    }

    public static boolean isPropertyElement(@NonNull Field field) {
        return !FieldElement.of(field).isStatic();
    }

    public static boolean isPropertyElement(@NonNull Method method) {
        return isPropertyReader(method) || isPropertyWriter(method);
    }

    public static boolean isPropertyElement(@NonNull Method method, @NonNull PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
        return isPropertyReader(method, propertyAccessorMethodFormats) || isPropertyWriter(method, propertyAccessorMethodFormats);
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
        return isPropertyElement(field);
    }

    /**
     * @author caotc
     * @date 2019-12-08
     * @implNote
     * @implSpec
     * @apiNote 非static的final属性只有在值为固定值时会因为编译器优化导致get方法无法读取到修改后的值
     * @since 1.0.0
     */
    public static boolean isPropertyReader(@NonNull FieldElement fieldElement) {
        return !fieldElement.isStatic();
    }

    public static boolean isPropertyReader(@NonNull Method method) {
        return isPropertyReader(method, DEFAULT_METHOD_NAME_STYLES);
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
     * DefaultPropertyAccessorMethodFormat#JAVA_BEAN#isGetMethod(Method)}
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
    public static boolean isPropertyReader(@NonNull MethodInvokable<?, ?> invokable) {
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
     * DefaultPropertyAccessorMethodFormat#JAVA_BEAN#isPropertyReader(MethodInvokable)}
     * @since 1.0.0
     */
    public static boolean isPropertyReader(@NonNull MethodInvokable<?, ?> invokable,
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
        return isPropertyElement(field);
    }

    /**
     * @author caotc
     * @date 2019-12-08
     * @implNote
     * @implSpec
     * @apiNote 非static的final属性只有在值为固定值时会因为编译器优化导致get方法无法读取到修改后的值
     * @since 1.0.0
     */
    public static boolean isPropertyWriter(@NonNull FieldElement fieldElement) {
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
     * DefaultPropertyAccessorMethodFormat#JAVA_BEAN#isPropertyWriter(Method)}
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
    public static boolean isPropertyWriter(@NonNull MethodInvokable<?, ?> invokable) {
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
     * DefaultPropertyAccessorMethodFormat#JAVA_BEAN#isPropertyWriter(MethodInvokable)}
     * @since 1.0.0
     */
    public static boolean isPropertyWriter(@NonNull MethodInvokable<?, ?> invokable,
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
        return isGetMethod(Invokable.from(method));
    }

    /**
     * @author caotc
     * @date 2019-12-08
     * @since 1.0.0
     */
    public static boolean isGetMethod(@NonNull MethodInvokable<?, ?> invokable) {
        return isPropertyReader(invokable, DefaultPropertyAccessorMethodFormat.JAVA_BEAN);
    }

    /**
     * @author caotc
     * @date 2019-12-08
     * @since 1.0.0
     */
    public static boolean isSetMethod(@NonNull Method method) {
        return isSetMethod(Invokable.from(method));
    }

    /**
     * @author caotc
     * @date 2019-12-08
     * @since 1.0.0
     */
    public static boolean isSetMethod(@NonNull MethodInvokable<?, ?> invokable) {
        return isPropertyWriter(invokable, DefaultPropertyAccessorMethodFormat.JAVA_BEAN);
    }

    public static boolean isOverridden(@NonNull Method method) {
        return isOverridden(Invokable.from(method));
    }

    public static boolean isOverridden(@NonNull MethodInvokable<?, ?> invokable) {
        return invokable.ownerType().getTypes().stream()
                .filter(type -> !type.equals(invokable.ownerType()))
                .anyMatch(type -> isOverridden(invokable, type));
    }

    public static boolean isOverridden(@NonNull Method method,
                                       @NonNull Type superType) {
        return isOverridden(method, TypeToken.of(superType));
    }

    public static boolean isOverridden(@NonNull Method method,
                                       @NonNull Class<?> superClass) {
        return isOverridden(method, TypeToken.of(superClass));
    }

    public static boolean isOverridden(@NonNull Method method,
                                       @NonNull TypeToken<?> superTypeToken) {
        return isOverridden(Invokable.from(method), superTypeToken);
    }

    public static boolean isOverridden(@NonNull MethodInvokable<?, ?> invokable,
                                       @NonNull Type superType) {
        return isOverridden(invokable, TypeToken.of(superType));
    }

    public static boolean isOverridden(@NonNull MethodInvokable<?, ?> invokable,
                                       @NonNull Class<?> superClass) {
        return isOverridden(invokable, TypeToken.of(superClass));
    }

    /**
     * invokable是否在superTypeToken中被重写
     *
     * @param invokable
     * @param superTypeToken
     * @return
     * @apiNote 仅判断当前superTypeToken, superTypeToken的super class重写忽略
     */
    public static boolean isOverridden(@NonNull MethodInvokable<?, ?> invokable,
                                       @NonNull TypeToken<?> superTypeToken) {
        if (invokable.declaringType().equals(superTypeToken)) {
            return false;
        }
        return Arrays.stream(superTypeToken.getRawType().getDeclaredMethods())
                .map(method -> Invokable.from(method, superTypeToken))
                .anyMatch(superMethodInvokable -> isOverridden(invokable, superMethodInvokable));
    }

    public static boolean isOverridden(@NonNull Method method,
                                       @NonNull Method superMethod) {
        TypeToken<?> type = TypeToken.of(method.getDeclaringClass());
        if (type.isSubtypeOf(superMethod.getDeclaringClass())) {
            return isOverridden(Invokable.from(method, type), Invokable.from(superMethod, type));
        }
        return false;
    }

    public static boolean isOverridden(@NonNull MethodInvokable<?, ?> invokable,
                                       @NonNull MethodInvokable<?, ?> superInvokable) {
        return superInvokable.isOverridden(invokable);
    }


}
