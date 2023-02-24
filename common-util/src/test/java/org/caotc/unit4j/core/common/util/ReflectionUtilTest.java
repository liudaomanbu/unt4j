/*
 * Copyright (C) 2022 the original author or authors.
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

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.reflect.property.AccessibleProperty;
import org.caotc.unit4j.core.common.reflect.property.Property;
import org.caotc.unit4j.core.common.reflect.property.ReadableProperty;
import org.caotc.unit4j.core.common.reflect.property.WritableProperty;
import org.caotc.unit4j.core.common.reflect.property.accessor.*;
import org.caotc.unit4j.core.exception.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.Set;

@Slf4j
class ReflectionUtilTest {
    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldSets")
    void fields(Type type, Set<Field> fields) {
        Set<Field> result = ReflectionUtil.fields(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(fields, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldSets")
    void fields(Class<?> clazz, Set<Field> fields) {
        Set<Field> result = ReflectionUtil.fields(clazz);
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(fields, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldSets")
    void fields(TypeToken<?> type, Set<Field> fields) {
        Set<Field> result = ReflectionUtil.fields(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(fields, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldSets")
    void fieldStream(Type type, Set<Field> fields) {
        Set<Field> result = ReflectionUtil.fieldStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(fields, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldSets")
    void fieldStream(Class<?> clazz, Set<Field> fields) {
        Set<Field> result = ReflectionUtil.fieldStream(clazz).collect(ImmutableSet.toImmutableSet());
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(fields, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldSets")
    void fieldStream(TypeToken<?> type, Set<Field> fields) {
        Set<Field> result = ReflectionUtil.fieldStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(fields, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndFields")
    void fields(Type type, String fieldName, Set<Field> fields) {
        Set<Field> result = ReflectionUtil.fields(type, fieldName);
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertEquals(fields, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndFields")
    void fields(Class<?> clazz, String fieldName, Set<Field> fields) {
        Set<Field> result = ReflectionUtil.fields(clazz, fieldName);
        log.debug("class:{},fieldName:{},result:{}", clazz, fieldName, result);
        Assertions.assertEquals(fields, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndFields")
    void fields(TypeToken<?> type, String fieldName, Set<Field> fields) {
        Set<Field> result = ReflectionUtil.fields(type, fieldName);
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertEquals(fields, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
    void fieldsWithErrorFieldName(Type type) {
        String errorFieldName = Math.random() + "";
        Set<Field> result = ReflectionUtil.fields(type, Math.random() + "");
        log.debug("type:{},errorFieldName:{},result:{}", type, errorFieldName, result);
        Assertions.assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
    void fieldsWithErrorFieldName(Class<?> clazz) {
        String errorFieldName = Math.random() + "";
        Set<Field> result = ReflectionUtil.fields(clazz, Math.random() + "");
        log.debug("class:{},errorFieldName:{},result:{}", clazz, errorFieldName, result);
        Assertions.assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokens")
    void fieldsWithErrorFieldName(TypeToken<?> type) {
        String errorFieldName = Math.random() + "";
        Set<Field> result = ReflectionUtil.fields(type, Math.random() + "");
        log.debug("type:{},errorFieldName:{},result:{}", type, errorFieldName, result);
        Assertions.assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndMethodSets")
    void methods(Type type, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.methods(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndMethodSets")
    void methods(Class<?> clazz, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.methods(clazz);
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndMethodSets")
    void methods(TypeToken<?> type, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.methods(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndMethodSets")
    void methodStream(Type type, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.methodStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndMethodSets")
    void methodStream(Class<?> clazz, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.methodStream(clazz).collect(ImmutableSet.toImmutableSet());
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndMethodSets")
    void methodStream(TypeToken<?> type, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.methodStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndConstructorSets")
    <T> void constructors(Type type, Set<Constructor<T>> constructors) {
        Set<Constructor<T>> result = ReflectionUtil.constructors(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(constructors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndConstructorSets")
    <T> void constructors(Class<T> clazz, Set<Constructor<T>> constructors) {
        Set<Constructor<T>> result = ReflectionUtil.constructors(clazz);
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(constructors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndConstructorSets")
    <T> void constructors(TypeToken<T> type, Set<Constructor<T>> constructors) {
        Set<Constructor<T>> result = ReflectionUtil.constructors(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(constructors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndConstructorSets")
    void constructorStream(Type type, Set<Constructor<?>> constructors) {
        Set<Constructor<?>> result = ReflectionUtil.constructorStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(constructors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndConstructorSets")
    <T> void constructorStream(Class<T> clazz, Set<Constructor<T>> constructors) {
        Set<Constructor<T>> result = ReflectionUtil.constructorStream(clazz).collect(ImmutableSet.toImmutableSet());
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(constructors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndConstructorSets")
    <T> void constructorStream(TypeToken<T> type, Set<Constructor<T>> constructors) {
        Set<Constructor<T>> result = ReflectionUtil.constructorStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(constructors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndGetMethodSets")
    void getMethods(Type type, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.getMethods(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndGetMethodSets")
    void getMethods(Class<?> clazz, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.getMethods(clazz);
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndGetMethodSets")
    void getMethods(TypeToken<?> type, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.getMethods(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndGetMethodSets")
    void getMethodStream(Type type, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.getMethodStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndGetMethodSets")
    void getMethodStream(Class<?> clazz, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.getMethodStream(clazz).collect(ImmutableSet.toImmutableSet());
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndGetMethodSets")
    void getMethodStream(TypeToken<?> type, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.getMethodStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndSetMethodSets")
    void setMethods(Type type, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.setMethods(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndSetMethodSets")
    void setMethods(Class<?> clazz, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.setMethods(clazz);
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndSetMethodSets")
    void setMethods(TypeToken<?> type, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.setMethods(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndSetMethodSets")
    void setMethodStream(Type type, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.setMethodStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndSetMethodSets")
    void setMethodStream(Class<?> clazz, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.setMethodStream(clazz).collect(ImmutableSet.toImmutableSet());
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndSetMethodSets")
    void setMethodStream(TypeToken<?> type, Set<Method> methods) {
        Set<Method> result = ReflectionUtil.setMethodStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(methods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndGetMethods")
    void getMethodExact(Type type, String fieldName, Method getMethod) {
        Method result = ReflectionUtil.getMethodExact(type, fieldName);
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertEquals(getMethod, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndGetMethods")
    void getMethodExact(Class<?> clazz, String fieldName, Method getMethod) {
        Method result = ReflectionUtil.getMethodExact(clazz, fieldName);
        log.debug("class:{},fieldName:{},result:{}", clazz, fieldName, result);
        Assertions.assertEquals(getMethod, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndGetMethods")
    void getMethodExact(TypeToken<?> type, String fieldName, Method getMethod) {
        Method result = ReflectionUtil.getMethodExact(type, fieldName);
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertEquals(getMethod, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
    void getMethodExactWithErrorFieldName(Type type) {
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(GetMethodNotFoundException.class, () -> ReflectionUtil.getMethodExact(type, errorFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
    void getMethodExactWithErrorFieldName(Class<?> clazz) {
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(GetMethodNotFoundException.class, () -> ReflectionUtil.getMethodExact(clazz, errorFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokens")
    void getMethodExactWithErrorFieldName(TypeToken<?> type) {
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(GetMethodNotFoundException.class, () -> ReflectionUtil.getMethodExact(type, errorFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndGetMethods")
    void getMethod(Type type, String fieldName, Method getMethod) {
        Optional<Method> result = ReflectionUtil.getMethod(type, fieldName);
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(getMethod, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndGetMethods")
    void getMethod(Class<?> clazz, String fieldName, Method getMethod) {
        Optional<Method> result = ReflectionUtil.getMethod(clazz, fieldName);
        log.debug("class:{},fieldName:{},result:{}", clazz, fieldName, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(getMethod, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndGetMethods")
    void getMethod(TypeToken<?> type, String fieldName, Method getMethod) {
        Optional<Method> result = ReflectionUtil.getMethod(type, fieldName);
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(getMethod, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
    void getMethodWithErrorFieldName(Type type) {
        String errorFieldName = Math.random() + "";
        Optional<Method> result = ReflectionUtil.getMethod(type, errorFieldName);
        log.debug("type:{},errorFieldName:{},result:{}", type, errorFieldName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
    void getMethodWithErrorFieldName(Class<?> clazz) {
        String errorFieldName = Math.random() + "";
        Optional<Method> result = ReflectionUtil.getMethod(clazz, errorFieldName);
        log.debug("class:{},errorFieldName:{},result:{}", clazz, errorFieldName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokens")
    void getMethodWithErrorFieldName(TypeToken<?> type) {
        String errorFieldName = Math.random() + "";
        Optional<Method> result = ReflectionUtil.getMethod(type, errorFieldName);
        log.debug("type:{},errorFieldName:{},result:{}", type, errorFieldName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExact(Type type, String fieldName, Method setMethod) {
        Method result = ReflectionUtil.setMethodExact(type, fieldName);
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertEquals(setMethod, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExact(Class<?> clazz, String fieldName, Method setMethod) {
        Method result = ReflectionUtil.setMethodExact(clazz, fieldName);
        log.debug("class:{},fieldName:{},result:{}", clazz, fieldName, result);
        Assertions.assertEquals(setMethod, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodExact(TypeToken<?> type, String fieldName, Method setMethod) {
        Method result = ReflectionUtil.setMethodExact(type, fieldName);
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertEquals(setMethod, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
    void setMethodExactWithErrorFieldName(Type type) {
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, errorFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
    void setMethodExactWithErrorFieldName(Class<?> clazz) {
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(clazz, errorFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokens")
    void setMethodExactWithErrorFieldName(TypeToken<?> type) {
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, errorFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndDuplicateSetMethodFieldNames")
    void setMethodExactWithDuplicateSetMethodFieldName(Type type, String duplicateSetMethodFieldName) {
        Assertions.assertThrows(IllegalStateException.class, () -> ReflectionUtil.setMethodExact(type, duplicateSetMethodFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndDuplicateSetMethodFieldNames")
    void setMethodExactWithDuplicateSetMethodFieldName(Class<?> clazz, String duplicateSetMethodFieldName) {
        Assertions.assertThrows(IllegalStateException.class, () -> ReflectionUtil.setMethodExact(clazz, duplicateSetMethodFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndDuplicateSetMethodFieldNames")
    void setMethodExactWithDuplicateSetMethodFieldName(TypeToken<?> type, String duplicateSetMethodFieldName) {
        Assertions.assertThrows(IllegalStateException.class, () -> ReflectionUtil.setMethodExact(type, duplicateSetMethodFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldType(Type type, String fieldName, Method setMethod) {
        Type fieldType = setMethod.getGenericParameterTypes()[0];
        Method result = ReflectionUtil.setMethodExact(type, fieldName, fieldType);
        log.debug("type:{},fieldName:{},fieldType:{},result:{}", type, fieldName, fieldType, result);
        Assertions.assertEquals(setMethod, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldClass(Type type, String fieldName, Method setMethod) {
        Class<?> fieldClass = setMethod.getParameterTypes()[0];
        Method result = ReflectionUtil.setMethodExact(type, fieldName, fieldClass);
        log.debug("type:{},fieldName:{},fieldClass:{},result:{}", type, fieldName, fieldClass, result);
        Assertions.assertEquals(setMethod, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldTypeToken(Type type, String fieldName, Method setMethod) {
        TypeToken<?> fieldType = TypeToken.of(setMethod.getGenericParameterTypes()[0]);
        Method result = ReflectionUtil.setMethodExact(type, fieldName, fieldType);
        log.debug("type:{},fieldName:{},fieldType:{},result:{}", type, fieldName, fieldType, result);
        Assertions.assertEquals(setMethod, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldType(Class<?> clazz, String fieldName, Method setMethod) {
        Type fieldType = setMethod.getGenericParameterTypes()[0];
        Method result = ReflectionUtil.setMethodExact(clazz, fieldName, fieldType);
        log.debug("class:{},fieldName:{},fieldType:{},result:{}", clazz, fieldName, fieldType, result);
        Assertions.assertEquals(setMethod, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldClass(Class<?> clazz, String fieldName, Method setMethod) {
        Class<?> fieldClass = setMethod.getParameterTypes()[0];
        Method result = ReflectionUtil.setMethodExact(clazz, fieldName, fieldClass);
        log.debug("class:{},fieldName:{},fieldClass:{},result:{}", clazz, fieldName, fieldClass, result);
        Assertions.assertEquals(setMethod, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldTypeToken(Class<?> clazz, String fieldName, Method setMethod) {
        TypeToken<?> fieldType = TypeToken.of(setMethod.getGenericParameterTypes()[0]);
        Method result = ReflectionUtil.setMethodExact(clazz, fieldName, fieldType);
        log.debug("class:{},fieldName:{},fieldType:{},result:{}", clazz, fieldName, fieldType, result);
        Assertions.assertEquals(setMethod, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodExactWithFieldType(TypeToken<?> type, String fieldName, Method setMethod) {
        Type fieldType = setMethod.getGenericParameterTypes()[0];
        Method result = ReflectionUtil.setMethodExact(type, fieldName, fieldType);
        log.debug("type:{},fieldName:{},fieldType:{},result:{}", type, fieldName, fieldType, result);
        Assertions.assertEquals(setMethod, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodExactWithFieldClass(TypeToken<?> type, String fieldName, Method setMethod) {
        Class<?> fieldClass = setMethod.getParameterTypes()[0];
        Method result = ReflectionUtil.setMethodExact(type, fieldName, fieldClass);
        log.debug("type:{},fieldName:{},fieldClass:{},result:{}", type, fieldName, fieldClass, result);
        Assertions.assertEquals(setMethod, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodExactWithFieldTypeToken(TypeToken<?> type, String fieldName, Method setMethod) {
        TypeToken<?> fieldType = TypeToken.of(setMethod.getGenericParameterTypes()[0]);
        Method result = ReflectionUtil.setMethodExact(type, fieldName, fieldType);
        log.debug("type:{},fieldName:{},fieldType:{},result:{}", type, fieldName, fieldType, result);
        Assertions.assertEquals(setMethod, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldType(Type type, String fieldName, Method setMethod) {
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, fieldName, (Type) Void.class));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldTypeAndErrorFieldName(Type type, String fieldName, Method setMethod) {
        Type fieldType = setMethod.getGenericParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, errorFieldName, fieldType));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldClass(Type type, String fieldName, Method setMethod) {
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, fieldName, Void.class));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldClassAndErrorFieldName(Type type, String fieldName, Method setMethod) {
        Class<?> fieldClass = setMethod.getParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, errorFieldName, fieldClass));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldTypeToken(Type type, String fieldName, Method setMethod) {
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, fieldName, TypeToken.of(Void.class)));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldTypeTokenAndErrorFieldName(Type type, String fieldName, Method setMethod) {
        TypeToken<?> fieldType = TypeToken.of(setMethod.getGenericParameterTypes()[0]);
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, errorFieldName, fieldType));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldType(Class<?> clazz, String fieldName, Method setMethod) {
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(clazz, fieldName, (Type) Void.class));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldTypeAndErrorFieldName(Class<?> clazz, String fieldName, Method setMethod) {
        Type fieldType = setMethod.getGenericParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(clazz, errorFieldName, fieldType));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldClass(Class<?> clazz, String fieldName, Method setMethod) {
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(clazz, fieldName, Void.class));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldClassAndErrorFieldName(Class<?> clazz, String fieldName, Method setMethod) {
        Class<?> fieldClass = setMethod.getParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(clazz, errorFieldName, fieldClass));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldTypeToken(Class<?> clazz, String fieldName, Method setMethod) {
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(clazz, fieldName, TypeToken.of(Void.class)));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldTypeTokenAndErrorFieldName(Class<?> clazz, String fieldName, Method setMethod) {
        TypeToken<?> fieldType = TypeToken.of(setMethod.getGenericParameterTypes()[0]);
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(clazz, errorFieldName, fieldType));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldType(TypeToken<?> type, String fieldName, Method setMethod) {
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, fieldName, (Type) Void.class));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodExactWithFieldTypeAndErrorFieldName(TypeToken<?> type, String fieldName, Method setMethod) {
        Type fieldType = setMethod.getGenericParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, errorFieldName, fieldType));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldClass(TypeToken<?> type, String fieldName, Method setMethod) {
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, fieldName, Void.class));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodExactWithFieldClassAndErrorFieldName(TypeToken<?> type, String fieldName, Method setMethod) {
        Class<?> fieldType = setMethod.getParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, errorFieldName, fieldType));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldTypeToken(TypeToken<?> type, String fieldName, Method setMethod) {
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, fieldName, TypeToken.of(Void.class)));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodExactWithFieldTypeTokenAndErrorFieldName(TypeToken<?> type, String fieldName, Method setMethod) {
        TypeToken<?> fieldType = TypeToken.of(setMethod.getGenericParameterTypes()[0]);
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(SetMethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, errorFieldName, fieldType));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethod(Type type, String fieldName, Method setMethod) {
        Optional<Method> result = ReflectionUtil.setMethod(type, fieldName);
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(setMethod, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethod(Class<?> clazz, String fieldName, Method setMethod) {
        Optional<Method> result = ReflectionUtil.setMethod(clazz, fieldName);
        log.debug("class:{},fieldName:{},result:{}", clazz, fieldName, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(setMethod, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethod(TypeToken<?> type, String fieldName, Method setMethod) {
        Optional<Method> result = ReflectionUtil.setMethod(type, fieldName);
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(setMethod, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
    void setMethodWithErrorFieldName(Type type) {
        String errorFieldName = Math.random() + "";
        Optional<Method> result = ReflectionUtil.setMethod(type, errorFieldName);
        log.debug("type:{},errorFieldName:{},result:{}", type, errorFieldName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
    void setMethodWithErrorFieldName(Class<?> clazz) {
        String errorFieldName = Math.random() + "";
        Optional<Method> result = ReflectionUtil.setMethod(clazz, errorFieldName);
        log.debug("class:{},errorFieldName:{},result:{}", clazz, errorFieldName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokens")
    void setMethodWithErrorFieldName(TypeToken<?> type) {
        String errorFieldName = Math.random() + "";
        Optional<Method> result = ReflectionUtil.setMethod(type, errorFieldName);
        log.debug("type:{},errorFieldName:{},result:{}", type, errorFieldName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndDuplicateSetMethodFieldNames")
    void setMethodWithDuplicateSetMethodFieldName(Type type, String duplicateSetMethodFieldName) {
        Assertions.assertThrows(IllegalStateException.class, () -> ReflectionUtil.setMethod(type, duplicateSetMethodFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndDuplicateSetMethodFieldNames")
    void setMethodWithDuplicateSetMethodFieldName(Class<?> clazz, String duplicateSetMethodFieldName) {
        Assertions.assertThrows(IllegalStateException.class, () -> ReflectionUtil.setMethod(clazz, duplicateSetMethodFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndDuplicateSetMethodFieldNames")
    void setMethodWithDuplicateSetMethodFieldName(TypeToken<?> type, String duplicateSetMethodFieldName) {
        Assertions.assertThrows(IllegalStateException.class, () -> ReflectionUtil.setMethod(type, duplicateSetMethodFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithFieldType(Type type, String fieldName, Method setMethod) {
        Type fieldType = setMethod.getGenericParameterTypes()[0];
        Optional<Method> result = ReflectionUtil.setMethod(type, fieldName, fieldType);
        log.debug("type:{},fieldName:{},fieldType:{},result:{}", type, fieldName, fieldType, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(setMethod, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithFieldClass(Type type, String fieldName, Method setMethod) {
        Class<?> fieldClass = setMethod.getParameterTypes()[0];
        Optional<Method> result = ReflectionUtil.setMethod(type, fieldName, fieldClass);
        log.debug("type:{},fieldName:{},fieldClass:{},result:{}", type, fieldName, fieldClass, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(setMethod, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithFieldTypeToken(Type type, String fieldName, Method setMethod) {
        TypeToken<?> fieldType = TypeToken.of(setMethod.getGenericParameterTypes()[0]);
        Optional<Method> result = ReflectionUtil.setMethod(type, fieldName, fieldType);
        log.debug("type:{},fieldName:{},fieldType:{},result:{}", type, fieldName, fieldType, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(setMethod, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithFieldType(Class<?> clazz, String fieldName, Method setMethod) {
        Type fieldType = setMethod.getGenericParameterTypes()[0];
        Optional<Method> result = ReflectionUtil.setMethod(clazz, fieldName, fieldType);
        log.debug("class:{},fieldName:{},fieldType:{},result:{}", clazz, fieldName, fieldType, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(setMethod, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithFieldClass(Class<?> clazz, String fieldName, Method setMethod) {
        Class<?> fieldClass = setMethod.getParameterTypes()[0];
        Optional<Method> result = ReflectionUtil.setMethod(clazz, fieldName, fieldClass);
        log.debug("class:{},fieldName:{},fieldClass:{},result:{}", clazz, fieldName, fieldClass, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(setMethod, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithFieldTypeToken(Class<?> clazz, String fieldName, Method setMethod) {
        TypeToken<?> fieldType = TypeToken.of(setMethod.getGenericParameterTypes()[0]);
        Optional<Method> result = ReflectionUtil.setMethod(clazz, fieldName, fieldType);
        log.debug("class:{},fieldName:{},fieldType:{},result:{}", clazz, fieldName, fieldType, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(setMethod, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodWithFieldType(TypeToken<?> type, String fieldName, Method setMethod) {
        Type fieldType = setMethod.getGenericParameterTypes()[0];
        Optional<Method> result = ReflectionUtil.setMethod(type, fieldName, fieldType);
        log.debug("type:{},fieldName:{},fieldType:{},result:{}", type, fieldName, fieldType, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(setMethod, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodWithFieldClass(TypeToken<?> type, String fieldName, Method setMethod) {
        Class<?> fieldClass = setMethod.getParameterTypes()[0];
        Optional<Method> result = ReflectionUtil.setMethod(type, fieldName, fieldClass);
        log.debug("type:{},fieldName:{},fieldClass:{},result:{}", type, fieldName, fieldClass, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(setMethod, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodWithFieldTypeToken(TypeToken<?> type, String fieldName, Method setMethod) {
        TypeToken<?> fieldType = TypeToken.of(setMethod.getGenericParameterTypes()[0]);
        Optional<Method> result = ReflectionUtil.setMethod(type, fieldName, fieldType);
        log.debug("type:{},fieldName:{},fieldType:{},result:{}", type, fieldName, fieldType, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(setMethod, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithErrorFieldType(Type type, String fieldName, Method setMethod) {
        Optional<Method> result = ReflectionUtil.setMethod(type, fieldName, (Type) Void.class);
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithFieldTypeAndErrorFieldName(Type type, String fieldName, Method setMethod) {
        Type fieldType = setMethod.getGenericParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Optional<Method> result = ReflectionUtil.setMethod(type, errorFieldName, fieldType);
        log.debug("type:{},errorFieldName:{},fieldType:{},result:{}", type, errorFieldName, fieldType, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithErrorFieldClass(Type type, String fieldName, Method setMethod) {
        Optional<Method> result = ReflectionUtil.setMethod(type, fieldName, Void.class);
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithFieldClassAndErrorFieldName(Type type, String fieldName, Method setMethod) {
        Class<?> fieldClass = setMethod.getParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Optional<Method> result = ReflectionUtil.setMethod(type, errorFieldName, fieldClass);
        log.debug("type:{},errorFieldName:{},fieldClass:{},result:{}", type, errorFieldName, fieldClass, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithErrorFieldTypeToken(Type type, String fieldName, Method setMethod) {
        Optional<Method> result = ReflectionUtil.setMethod(type, fieldName, TypeToken.of(Void.class));
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithFieldTypeTokenAndErrorFieldName(Type type, String fieldName, Method setMethod) {
        TypeToken<?> fieldType = TypeToken.of(setMethod.getGenericParameterTypes()[0]);
        String errorFieldName = Math.random() + "";
        Optional<Method> result = ReflectionUtil.setMethod(type, errorFieldName, fieldType);
        log.debug("type:{},errorFieldName:{},fieldType:{},result:{}", type, errorFieldName, fieldType, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithErrorFieldType(Class<?> clazz, String fieldName, Method setMethod) {
        Optional<Method> result = ReflectionUtil.setMethod(clazz, fieldName, (Type) Void.class);
        log.debug("class:{},fieldName:{},result:{}", clazz, fieldName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithFieldTypeAndErrorFieldName(Class<?> clazz, String fieldName, Method setMethod) {
        Type fieldType = setMethod.getGenericParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Optional<Method> result = ReflectionUtil.setMethod(clazz, errorFieldName, fieldType);
        log.debug("class:{},errorFieldName:{},fieldType:{},result:{}", clazz, errorFieldName, fieldType, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithErrorFieldClass(Class<?> clazz, String fieldName, Method setMethod) {
        Optional<Method> result = ReflectionUtil.setMethod(clazz, fieldName, Void.class);
        log.debug("class:{},fieldName:{},result:{}", clazz, fieldName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithFieldClassAndErrorFieldName(Class<?> clazz, String fieldName, Method setMethod) {
        Class<?> fieldClass = setMethod.getParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Optional<Method> result = ReflectionUtil.setMethod(clazz, errorFieldName, fieldClass);
        log.debug("class:{},errorFieldName:{},fieldClass:{},result:{}", clazz, errorFieldName, fieldClass, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithErrorFieldTypeToken(Class<?> clazz, String fieldName, Method setMethod) {
        Optional<Method> result = ReflectionUtil.setMethod(clazz, fieldName, TypeToken.of(Void.class));
        log.debug("class:{},fieldName:{},result:{}", clazz, fieldName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodWithFieldTypeTokenAndErrorFieldName(Class<?> clazz, String fieldName, Method setMethod) {
        TypeToken<?> fieldType = TypeToken.of(setMethod.getGenericParameterTypes()[0]);
        String errorFieldName = Math.random() + "";
        Optional<Method> result = ReflectionUtil.setMethod(clazz, errorFieldName, fieldType);
        log.debug("class:{},errorFieldName:{},fieldType:{},result:{}", clazz, errorFieldName, fieldType, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodWithErrorFieldType(TypeToken<?> type, String fieldName, Method setMethod) {
        Optional<Method> result = ReflectionUtil.setMethod(type, fieldName, (Type) Void.class);
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodWithFieldTypeAndErrorFieldName(TypeToken<?> type, String fieldName, Method setMethod) {
        Type fieldType = setMethod.getGenericParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Optional<Method> result = ReflectionUtil.setMethod(type, errorFieldName, fieldType);
        log.debug("type:{},errorFieldName:{},fieldType:{},result:{}", type, errorFieldName, fieldType, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodWithErrorFieldClass(TypeToken<?> type, String fieldName, Method setMethod) {
        Optional<Method> result = ReflectionUtil.setMethod(type, fieldName, Void.class);
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodWithFieldClassAndErrorFieldName(TypeToken<?> type, String fieldName, Method setMethod) {
        Class<?> fieldClass = setMethod.getParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Optional<Method> result = ReflectionUtil.setMethod(type, errorFieldName, fieldClass);
        log.debug("type:{},errorFieldName:{},fieldClass:{},result:{}", type, errorFieldName, fieldClass, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodWithErrorFieldTypeToken(TypeToken<?> type, String fieldName, Method setMethod) {
        Optional<Method> result = ReflectionUtil.setMethod(type, fieldName, TypeToken.of(Void.class));
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodWithFieldTypeTokenAndErrorFieldName(TypeToken<?> type, String fieldName, Method setMethod) {
        TypeToken<?> fieldType = TypeToken.of(setMethod.getGenericParameterTypes()[0]);
        String errorFieldName = Math.random() + "";
        Optional<Method> result = ReflectionUtil.setMethod(type, errorFieldName, fieldType);
        log.debug("type:{},errorFieldName:{},fieldType:{},result:{}", type, errorFieldName, fieldType, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethodSets")
    void setMethods(Type type, String fieldName, Set<Method> setMethods) {
        Set<Method> result = ReflectionUtil.setMethods(type, fieldName);
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertEquals(setMethods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethodSets")
    void setMethods(Class<?> clazz, String fieldName, Set<Method> setMethods) {
        Set<Method> result = ReflectionUtil.setMethods(clazz, fieldName);
        log.debug("class:{},fieldName:{},result:{}", clazz, fieldName, result);
        Assertions.assertEquals(setMethods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethodSets")
    void setMethods(TypeToken<?> type, String fieldName, Set<Method> setMethods) {
        Set<Method> result = ReflectionUtil.setMethods(type, fieldName);
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertEquals(setMethods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
    void setMethodsWithErrorFieldName(Type type) {
        String errorFieldName = Math.random() + "";
        Set<Method> result = ReflectionUtil.setMethods(type, errorFieldName);
        log.debug("type:{},errorFieldName:{},result:{}", type, errorFieldName, result);
        Assertions.assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
    void setMethodsWithErrorFieldName(Class<?> clazz) {
        String errorFieldName = Math.random() + "";
        Set<Method> result = ReflectionUtil.setMethods(clazz, errorFieldName);
        log.debug("class:{},errorFieldName:{},result:{}", clazz, errorFieldName, result);
        Assertions.assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokens")
    void setMethodsWithErrorFieldName(TypeToken<?> type) {
        String errorFieldName = Math.random() + "";
        Set<Method> result = ReflectionUtil.setMethods(type, errorFieldName);
        log.debug("type:{},errorFieldName:{},result:{}", type, errorFieldName, result);
        Assertions.assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethodSets")
    void setMethodStream(Type type, String fieldName, Set<Method> setMethods) {
        Set<Method> result = ReflectionUtil.setMethodStream(type, fieldName).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertEquals(setMethods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethodSets")
    void setMethodStream(Class<?> clazz, String fieldName, Set<Method> setMethods) {
        Set<Method> result = ReflectionUtil.setMethodStream(clazz, fieldName).collect(ImmutableSet.toImmutableSet());
        log.debug("class:{},fieldName:{},result:{}", clazz, fieldName, result);
        Assertions.assertEquals(setMethods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethodSets")
    void setMethodStream(TypeToken<?> type, String fieldName, Set<Method> setMethods) {
        Set<Method> result = ReflectionUtil.setMethodStream(type, fieldName).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},fieldName:{},result:{}", type, fieldName, result);
        Assertions.assertEquals(setMethods, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
    void setMethodStreamWithErrorFieldName(Type type) {
        String errorFieldName = Math.random() + "";
        Set<Method> result = ReflectionUtil.setMethodStream(type, errorFieldName).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},errorFieldName:{},result:{}", type, errorFieldName, result);
        Assertions.assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
    void setMethodStreamWithErrorFieldName(Class<?> clazz) {
        String errorFieldName = Math.random() + "";
        Set<Method> result = ReflectionUtil.setMethodStream(clazz, errorFieldName).collect(ImmutableSet.toImmutableSet());
        log.debug("class:{},errorFieldName:{},result:{}", clazz, errorFieldName, result);
        Assertions.assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokens")
    void setMethodStreamWithErrorFieldName(TypeToken<?> type) {
        String errorFieldName = Math.random() + "";
        Set<Method> result = ReflectionUtil.setMethodStream(type, errorFieldName).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},errorFieldName:{},result:{}", type, errorFieldName, result);
        Assertions.assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertySets")
    void propertyStream(Type type, Set<Property<?, ?>> properties) {
        Set<Property<?, ?>> result = ReflectionUtil.propertyStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(properties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatArrayAndPropertySets")
    void propertyStream(Type type, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<Property<?, ?>> properties) {
        Set<Property<?, ?>> result = ReflectionUtil.propertyStream(type, propertyAccessorMethodFormats).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},propertyAccessorMethodFormats:{},result:{}", type, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(properties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertySets")
    <T> void propertyStream(Class<T> clazz, Set<Property<T, ?>> properties) {
        Set<Property<T, ?>> result = ReflectionUtil.propertyStream(clazz).collect(ImmutableSet.toImmutableSet());
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(properties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatArrayAndPropertySets")
    <T> void propertyStream(Class<T> clazz, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<Property<T, ?>> properties) {
        Set<Property<T, ?>> result = ReflectionUtil.propertyStream(clazz, propertyAccessorMethodFormats).collect(ImmutableSet.toImmutableSet());
        log.debug("class:{},propertyAccessorMethodFormats:{},result:{}", clazz, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(properties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertySets")
    <T> void propertyStream(TypeToken<T> type, Set<Property<T, ?>> properties) {
        Set<Property<T, ?>> result = ReflectionUtil.propertyStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(properties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorMethodFormatArrayAndPropertySets")
    <T> void propertyStream(TypeToken<?> type, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<Property<T, ?>> properties) {
        Set<Property<?, ?>> result = ReflectionUtil.propertyStream(type, propertyAccessorMethodFormats).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},propertyAccessorMethodFormats:{},result:{}", type, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(properties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertySets")
    <T> void properties(Type type, Set<Property<T, ?>> properties) {
        Set<Property<T, ?>> result = ReflectionUtil.properties(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(properties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatArrayAndPropertySets")
    <T> void properties(Type type, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<Property<T, ?>> properties) {
        Set<Property<T, ?>> result = ReflectionUtil.properties(type, propertyAccessorMethodFormats);
        log.debug("type:{},propertyAccessorMethodFormats:{},result:{}", type, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(properties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertySets")
    <T> void properties(Class<T> clazz, Set<Property<T, ?>> properties) {
        Set<Property<T, ?>> result = ReflectionUtil.properties(clazz);
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(properties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatArrayAndPropertySets")
    <T> void properties(Class<T> clazz, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<Property<T, ?>> properties) {
        Set<Property<T, ?>> result = ReflectionUtil.properties(clazz, propertyAccessorMethodFormats);
        log.debug("class:{},propertyAccessorMethodFormats:{},result:{}", clazz, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(properties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertySets")
    <T> void properties(TypeToken<T> type, Set<Property<T, ?>> properties) {
        Set<Property<T, ?>> result = ReflectionUtil.properties(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(properties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorMethodFormatArrayAndPropertySets")
    <T> void properties(TypeToken<T> type, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<Property<T, ?>> properties) {
        Set<Property<T, ?>> result = ReflectionUtil.properties(type, propertyAccessorMethodFormats);
        log.debug("type:{},propertyAccessorMethodFormats:{},result:{}", type, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(properties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndReadablePropertySets")
    void readablePropertyStream(Type type, Set<ReadableProperty<?, ?>> readableProperties) {
        Set<ReadableProperty<?, ?>> result = ReflectionUtil.readablePropertyStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(readableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatArrayAndReadablePropertySets")
    void readablePropertyStream(Type type, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<ReadableProperty<?, ?>> readableProperties) {
        Set<ReadableProperty<?, ?>> result = ReflectionUtil.readablePropertyStream(type, propertyAccessorMethodFormats).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},propertyAccessorMethodFormats:{},result:{}", type, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(readableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndReadablePropertySets")
    <T> void readablePropertyStream(Class<T> clazz, Set<ReadableProperty<T, ?>> readableProperties) {
        Set<ReadableProperty<T, ?>> result = ReflectionUtil.readablePropertyStream(clazz).collect(ImmutableSet.toImmutableSet());
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(readableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatArrayAndReadablePropertySets")
    <T> void readablePropertyStream(Class<T> clazz, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<ReadableProperty<T, ?>> readableProperties) {
        Set<ReadableProperty<T, ?>> result = ReflectionUtil.readablePropertyStream(clazz, propertyAccessorMethodFormats).collect(ImmutableSet.toImmutableSet());
        log.debug("class:{},propertyAccessorMethodFormats:{},result:{}", clazz, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(readableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndReadablePropertySets")
    <T> void readablePropertyStream(TypeToken<T> type, Set<ReadableProperty<T, ?>> readableProperties) {
        Set<ReadableProperty<T, ?>> result = ReflectionUtil.readablePropertyStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(readableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorMethodFormatArrayAndReadablePropertySets")
    <T> void readablePropertyStream(TypeToken<T> type, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<ReadableProperty<T, ?>> readableProperties) {
        Set<ReadableProperty<?, ?>> result = ReflectionUtil.readablePropertyStream(type, propertyAccessorMethodFormats).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},propertyAccessorMethodFormats:{},result:{}", type, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(readableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndReadablePropertySets")
    <T> void readableProperties(Type type, Set<ReadableProperty<T, ?>> readableProperties) {
        Set<ReadableProperty<T, ?>> result = ReflectionUtil.readableProperties(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(readableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatArrayAndReadablePropertySets")
    <T> void readableProperties(Type type, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<ReadableProperty<T, ?>> readableProperties) {
        Set<ReadableProperty<T, ?>> result = ReflectionUtil.readableProperties(type, propertyAccessorMethodFormats);
        log.debug("type:{},propertyAccessorMethodFormats:{},result:{}", type, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(readableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndReadablePropertySets")
    <T> void readableProperties(Class<T> clazz, Set<ReadableProperty<T, ?>> readableProperties) {
        Set<ReadableProperty<T, ?>> result = ReflectionUtil.readableProperties(clazz);
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(readableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatArrayAndReadablePropertySets")
    <T> void readableProperties(Class<T> clazz, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<ReadableProperty<T, ?>> readableProperties) {
        Set<ReadableProperty<T, ?>> result = ReflectionUtil.readableProperties(clazz, propertyAccessorMethodFormats);
        log.debug("class:{},propertyAccessorMethodFormats:{},result:{}", clazz, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(readableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndReadablePropertySets")
    <T> void readableProperties(TypeToken<T> type, Set<ReadableProperty<T, ?>> readableProperties) {
        Set<ReadableProperty<T, ?>> result = ReflectionUtil.readableProperties(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(readableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorMethodFormatArrayAndReadablePropertySets")
    <T> void readableProperties(TypeToken<T> type, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<ReadableProperty<T, ?>> readableProperties) {
        Set<ReadableProperty<T, ?>> result = ReflectionUtil.readableProperties(type, propertyAccessorMethodFormats);
        log.debug("type:{},propertyAccessorMethodFormats:{},result:{}", type, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(readableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndWritablePropertySets")
    void writablePropertyStream(Type type, Set<WritableProperty<?, ?>> writableProperties) {
        Set<WritableProperty<?, ?>> result = ReflectionUtil.writablePropertyStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(writableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatArrayAndWritablePropertySets")
    void writablePropertyStream(Type type, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<WritableProperty<?, ?>> writableProperties) {
        Set<WritableProperty<?, ?>> result = ReflectionUtil.writablePropertyStream(type, propertyAccessorMethodFormats).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},propertyAccessorMethodFormats:{},result:{}", type, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(writableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndWritablePropertySets")
    <T> void writablePropertyStream(Class<T> clazz, Set<WritableProperty<T, ?>> writableProperties) {
        Set<WritableProperty<T, ?>> result = ReflectionUtil.writablePropertyStream(clazz).collect(ImmutableSet.toImmutableSet());
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(writableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatArrayAndWritablePropertySets")
    <T> void writablePropertyStream(Class<T> clazz, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<WritableProperty<T, ?>> writableProperties) {
        Set<WritableProperty<T, ?>> result = ReflectionUtil.writablePropertyStream(clazz, propertyAccessorMethodFormats).collect(ImmutableSet.toImmutableSet());
        log.debug("class:{},propertyAccessorMethodFormats:{},result:{}", clazz, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(writableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndWritablePropertySets")
    <T> void writablePropertyStream(TypeToken<T> type, Set<WritableProperty<T, ?>> writableProperties) {
        Set<WritableProperty<T, ?>> result = ReflectionUtil.writablePropertyStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(writableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorMethodFormatArrayAndWritablePropertySets")
    <T> void writablePropertyStream(TypeToken<T> type, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<WritableProperty<T, ?>> writableProperties) {
        Set<WritableProperty<?, ?>> result = ReflectionUtil.writablePropertyStream(type, propertyAccessorMethodFormats).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},propertyAccessorMethodFormats:{},result:{}", type, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(writableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndWritablePropertySets")
    <T> void writableProperties(Type type, Set<WritableProperty<T, ?>> writableProperties) {
        Set<WritableProperty<T, ?>> result = ReflectionUtil.writableProperties(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(writableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatArrayAndWritablePropertySets")
    <T> void writableProperties(Type type, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<WritableProperty<T, ?>> writableProperties) {
        Set<WritableProperty<T, ?>> result = ReflectionUtil.writableProperties(type, propertyAccessorMethodFormats);
        log.debug("type:{},propertyAccessorMethodFormats:{},result:{}", type, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(writableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndWritablePropertySets")
    <T> void writableProperties(Class<T> clazz, Set<WritableProperty<T, ?>> writableProperties) {
        Set<WritableProperty<T, ?>> result = ReflectionUtil.writableProperties(clazz);
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(writableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatArrayAndWritablePropertySets")
    <T> void writableProperties(Class<T> clazz, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<WritableProperty<T, ?>> writableProperties) {
        Set<WritableProperty<T, ?>> result = ReflectionUtil.writableProperties(clazz, propertyAccessorMethodFormats);
        log.debug("class:{},propertyAccessorMethodFormats:{},result:{}", clazz, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(writableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndWritablePropertySets")
    <T> void writableProperties(TypeToken<T> type, Set<WritableProperty<T, ?>> writableProperties) {
        Set<WritableProperty<T, ?>> result = ReflectionUtil.writableProperties(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(writableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorMethodFormatArrayAndWritablePropertySets")
    <T> void writableProperties(TypeToken<T> type, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<WritableProperty<T, ?>> writableProperties) {
        Set<WritableProperty<T, ?>> result = ReflectionUtil.writableProperties(type, propertyAccessorMethodFormats);
        log.debug("type:{},propertyAccessorMethodFormats:{},result:{}", type, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(writableProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndAccessiblePropertySets")
    void accessiblePropertyStream(Type type, Set<AccessibleProperty<?, ?>> accessibleProperties) {
        Set<AccessibleProperty<?, ?>> result = ReflectionUtil.accessiblePropertyStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(accessibleProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatArrayAndAccessiblePropertySets")
    void accessiblePropertyStream(Type type, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<AccessibleProperty<?, ?>> accessibleProperties) {
        Set<AccessibleProperty<?, ?>> result = ReflectionUtil.accessiblePropertyStream(type, propertyAccessorMethodFormats).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},propertyAccessorMethodFormats:{},result:{}", type, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(accessibleProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndAccessiblePropertySets")
    <T> void accessiblePropertyStream(Class<T> clazz, Set<AccessibleProperty<T, ?>> accessibleProperties) {
        Set<AccessibleProperty<T, ?>> result = ReflectionUtil.accessiblePropertyStream(clazz).collect(ImmutableSet.toImmutableSet());
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(accessibleProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatArrayAndAccessiblePropertySets")
    <T> void accessiblePropertyStream(Class<T> clazz, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<AccessibleProperty<T, ?>> accessibleProperties) {
        Set<AccessibleProperty<T, ?>> result = ReflectionUtil.accessiblePropertyStream(clazz, propertyAccessorMethodFormats).collect(ImmutableSet.toImmutableSet());
        log.debug("class:{},propertyAccessorMethodFormats:{},result:{}", clazz, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(accessibleProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndAccessiblePropertySets")
    <T> void accessiblePropertyStream(TypeToken<T> type, Set<AccessibleProperty<T, ?>> accessibleProperties) {
        Set<AccessibleProperty<T, ?>> result = ReflectionUtil.accessiblePropertyStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(accessibleProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorMethodFormatArrayAndAccessiblePropertySets")
    <T> void accessiblePropertyStream(TypeToken<T> type, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<AccessibleProperty<T, ?>> accessibleProperties) {
        Set<AccessibleProperty<?, ?>> result = ReflectionUtil.accessiblePropertyStream(type, propertyAccessorMethodFormats).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},propertyAccessorMethodFormats:{},result:{}", type, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(accessibleProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndAccessiblePropertySets")
    <T> void accessibleProperties(Type type, Set<AccessibleProperty<T, ?>> accessibleProperties) {
        Set<AccessibleProperty<T, ?>> result = ReflectionUtil.accessibleProperties(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(accessibleProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatArrayAndAccessiblePropertySets")
    <T> void accessibleProperties(Type type, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<AccessibleProperty<T, ?>> accessibleProperties) {
        Set<AccessibleProperty<T, ?>> result = ReflectionUtil.accessibleProperties(type, propertyAccessorMethodFormats);
        log.debug("type:{},propertyAccessorMethodFormats:{},result:{}", type, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(accessibleProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndAccessiblePropertySets")
    <T> void accessibleProperties(Class<T> clazz, Set<AccessibleProperty<T, ?>> accessibleProperties) {
        Set<AccessibleProperty<T, ?>> result = ReflectionUtil.accessibleProperties(clazz);
        log.debug("class:{},result:{}", clazz, result);
        Assertions.assertEquals(accessibleProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatArrayAndAccessiblePropertySets")
    <T> void accessibleProperties(Class<T> clazz, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<AccessibleProperty<T, ?>> accessibleProperties) {
        Set<AccessibleProperty<T, ?>> result = ReflectionUtil.accessibleProperties(clazz, propertyAccessorMethodFormats);
        log.debug("class:{},propertyAccessorMethodFormats:{},result:{}", clazz, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(accessibleProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndAccessiblePropertySets")
    <T> void accessibleProperties(TypeToken<T> type, Set<AccessibleProperty<T, ?>> accessibleProperties) {
        Set<AccessibleProperty<T, ?>> result = ReflectionUtil.accessibleProperties(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(accessibleProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorMethodFormatArrayAndAccessiblePropertySets")
    <T> void accessibleProperties(TypeToken<T> type, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, Set<AccessibleProperty<T, ?>> accessibleProperties) {
        Set<AccessibleProperty<T, ?>> result = ReflectionUtil.accessibleProperties(type, propertyAccessorMethodFormats);
        log.debug("type:{},propertyAccessorMethodFormats:{},result:{}", type, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(accessibleProperties, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndReadablePropertys")
    <T> void readablePropertyExact(Type type, String propertyName, ReadableProperty<T, ?> readableProperty) {
        ReadableProperty<T, ?> result = ReflectionUtil.readablePropertyExact(type, propertyName);
        log.debug("type:{},propertyName:{},result:{}", type, propertyName, result);
        Assertions.assertEquals(readableProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndReadablePropertys")
    <T> void readablePropertyExact(Type type, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, ReadableProperty<T, ?> readableProperty) {
        ReadableProperty<T, ?> result = ReflectionUtil.readablePropertyExact(type, propertyName, propertyAccessorMethodFormats);
        log.debug("type:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", type, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(readableProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndReadablePropertys")
    <T> void readablePropertyExact(Class<T> clazz, String propertyName, ReadableProperty<T, ?> readableProperty) {
        ReadableProperty<T, ?> result = ReflectionUtil.readablePropertyExact(clazz, propertyName);
        log.debug("class:{},propertyName:{},result:{}", clazz, propertyName, result);
        Assertions.assertEquals(readableProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndReadablePropertys")
    <T> void readablePropertyExact(Class<T> clazz, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, ReadableProperty<T, ?> readableProperty) {
        ReadableProperty<T, ?> result = ReflectionUtil.readablePropertyExact(clazz, propertyName, propertyAccessorMethodFormats);
        log.debug("class:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", clazz, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(readableProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyNameAndReadablePropertys")
    <T> void readablePropertyExact(TypeToken<T> type, String propertyName, ReadableProperty<T, ?> readableProperty) {
        ReadableProperty<T, ?> result = ReflectionUtil.readablePropertyExact(type, propertyName);
        log.debug("type:{},propertyName:{},result:{}", type, propertyName, result);
        Assertions.assertEquals(readableProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyNameAndPropertyAccessorMethodFormatArrayAndReadablePropertys")
    <T> void readablePropertyExact(TypeToken<T> type, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, ReadableProperty<T, ?> readableProperty) {
        ReadableProperty<T, ?> result = ReflectionUtil.readablePropertyExact(type, propertyName, propertyAccessorMethodFormats);
        log.debug("type:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", type, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(readableProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorReadablePropertyName")
    void readablePropertyExactWithErrorPropertyName(Type type, String errorPropertyName) {
        log.debug("type:{},errorPropertyName:{}", type, errorPropertyName);
        Assertions.assertThrows(ReadablePropertyNotFoundException.class, () -> ReflectionUtil.readablePropertyExact(type, errorPropertyName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorReadablePropertyNameAndPropertyAccessorMethodFormatArrays")
    void readablePropertyExactWithErrorPropertyName(Type type, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        log.debug("type:{},errorPropertyName:{},propertyAccessorMethodFormats:{}", type, errorPropertyName, propertyAccessorMethodFormats);
        Assertions.assertThrows(ReadablePropertyNotFoundException.class, () -> ReflectionUtil.readablePropertyExact(type, errorPropertyName, propertyAccessorMethodFormats));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorReadablePropertyName")
    void readablePropertyExactWithErrorPropertyName(Class<?> clazz, String errorPropertyName) {
        log.debug("class:{},errorPropertyName:{}", clazz, errorPropertyName);
        Assertions.assertThrows(ReadablePropertyNotFoundException.class, () -> ReflectionUtil.readablePropertyExact(clazz, errorPropertyName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorReadablePropertyNameAndPropertyAccessorMethodFormatArrays")
    void readablePropertyExactWithErrorPropertyName(Class<?> clazz, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        log.debug("class:{},errorPropertyName:{},propertyAccessorMethodFormats:{}", clazz, errorPropertyName, propertyAccessorMethodFormats);
        Assertions.assertThrows(ReadablePropertyNotFoundException.class, () -> ReflectionUtil.readablePropertyExact(clazz, errorPropertyName, propertyAccessorMethodFormats));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndErrorReadablePropertyName")
    void readablePropertyExactWithErrorPropertyName(TypeToken<?> type, String errorPropertyName) {
        log.debug("type:{},errorPropertyName:{}", type, errorPropertyName);
        Assertions.assertThrows(ReadablePropertyNotFoundException.class, () -> ReflectionUtil.readablePropertyExact(type, errorPropertyName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndErrorReadablePropertyNameAndPropertyAccessorMethodFormatArrays")
    void readablePropertyExactWithErrorPropertyName(TypeToken<?> type, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        log.debug("type:{},errorPropertyName:{},propertyAccessorMethodFormats:{}", type, errorPropertyName, propertyAccessorMethodFormats);
        Assertions.assertThrows(ReadablePropertyNotFoundException.class, () -> ReflectionUtil.readablePropertyExact(type, errorPropertyName, propertyAccessorMethodFormats));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndReadablePropertys")
    <T, R> void readableProperty(Type type, String propertyName, ReadableProperty<T, R> readableProperty) {
        Optional<ReadableProperty<T, R>> result = ReflectionUtil.readableProperty(type, propertyName);
        log.debug("type:{},propertyName:{},result:{}", type, propertyName, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(readableProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndReadablePropertys")
    <T, R> void readableProperty(Type type, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, ReadableProperty<T, R> readableProperty) {
        Optional<ReadableProperty<T, R>> result = ReflectionUtil.readableProperty(type, propertyName, propertyAccessorMethodFormats);
        log.debug("type:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", type, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(readableProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndReadablePropertys")
    <T, R> void readableProperty(Class<T> clazz, String propertyName, ReadableProperty<T, R> readableProperty) {
        Optional<ReadableProperty<T, R>> result = ReflectionUtil.readableProperty(clazz, propertyName);
        log.debug("class:{},propertyName:{},result:{}", clazz, propertyName, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(readableProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndReadablePropertys")
    <T, R> void readableProperty(Class<T> clazz, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, ReadableProperty<T, R> readableProperty) {
        Optional<ReadableProperty<T, R>> result = ReflectionUtil.readableProperty(clazz, propertyName, propertyAccessorMethodFormats);
        log.debug("class:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", clazz, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(readableProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyNameAndReadablePropertys")
    <T, R> void readableProperty(TypeToken<T> type, String propertyName, ReadableProperty<T, R> readableProperty) {
        Optional<ReadableProperty<T, R>> result = ReflectionUtil.readableProperty(type, propertyName);
        log.debug("type:{},propertyName:{},result:{}", type, propertyName, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(readableProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyNameAndPropertyAccessorMethodFormatArrayAndReadablePropertys")
    <T, R> void readableProperty(TypeToken<T> type, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, ReadableProperty<T, R> readableProperty) {
        Optional<ReadableProperty<T, R>> result = ReflectionUtil.readableProperty(type, propertyName, propertyAccessorMethodFormats);
        log.debug("type:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", type, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(readableProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorReadablePropertyName")
    <T, R> void readablePropertyWithErrorPropertyName(Type type, String errorPropertyName) {
        Optional<ReadableProperty<T, R>> result = ReflectionUtil.readableProperty(type, errorPropertyName);
        log.debug("type:{},errorPropertyName:{},result:{}", type, errorPropertyName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorReadablePropertyNameAndPropertyAccessorMethodFormatArrays")
    <T, R> void readablePropertyWithErrorPropertyName(Type type, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        Optional<ReadableProperty<T, R>> result = ReflectionUtil.readableProperty(type, errorPropertyName, propertyAccessorMethodFormats);
        log.debug("type:{},errorPropertyName:{},propertyAccessorMethodFormats:{},result:{}", type, errorPropertyName, propertyAccessorMethodFormats, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorReadablePropertyName")
    <T, R> void readablePropertyWithErrorPropertyName(Class<T> clazz, String errorPropertyName) {
        Optional<ReadableProperty<T, R>> result = ReflectionUtil.readableProperty(clazz, errorPropertyName);
        log.debug("class:{},errorPropertyName:{},result:{}", clazz, errorPropertyName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorReadablePropertyNameAndPropertyAccessorMethodFormatArrays")
    <T, R> void readablePropertyWithErrorPropertyName(Class<T> clazz, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        Optional<ReadableProperty<T, R>> result = ReflectionUtil.readableProperty(clazz, errorPropertyName, propertyAccessorMethodFormats);
        log.debug("class:{},errorPropertyName:{},propertyAccessorMethodFormats:{},result:{}", clazz, errorPropertyName, propertyAccessorMethodFormats, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndErrorReadablePropertyName")
    <T, R> void readablePropertyWithErrorPropertyName(TypeToken<T> type, String errorPropertyName) {
        Optional<ReadableProperty<T, R>> result = ReflectionUtil.readableProperty(type, errorPropertyName);
        log.debug("type:{},errorPropertyName:{},result:{}", type, errorPropertyName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndErrorReadablePropertyNameAndPropertyAccessorMethodFormatArrays")
    <T, R> void readablePropertyWithErrorPropertyName(TypeToken<T> type, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        Optional<ReadableProperty<T, R>> result = ReflectionUtil.readableProperty(type, errorPropertyName, propertyAccessorMethodFormats);
        log.debug("type:{},errorPropertyName:{},propertyAccessorMethodFormats:{},result:{}", type, errorPropertyName, propertyAccessorMethodFormats, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndWritablePropertys")
    <T> void writablePropertyExact(Type type, String propertyName, WritableProperty<T, ?> writableProperty) {
        WritableProperty<T, ?> result = ReflectionUtil.writablePropertyExact(type, propertyName);
        log.debug("type:{},propertyName:{},result:{}", type, propertyName, result);
        Assertions.assertEquals(writableProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndWritablePropertys")
    <T> void writablePropertyExact(Type type, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, WritableProperty<T, ?> writableProperty) {
        WritableProperty<T, ?> result = ReflectionUtil.writablePropertyExact(type, propertyName, propertyAccessorMethodFormats);
        log.debug("type:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", type, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(writableProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndWritablePropertys")
    <T> void writablePropertyExact(Class<T> clazz, String propertyName, WritableProperty<T, ?> writableProperty) {
        WritableProperty<T, ?> result = ReflectionUtil.writablePropertyExact(clazz, propertyName);
        log.debug("class:{},propertyName:{},result:{}", clazz, propertyName, result);
        Assertions.assertEquals(writableProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndWritablePropertys")
    <T> void writablePropertyExact(Class<T> clazz, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, WritableProperty<T, ?> writableProperty) {
        WritableProperty<T, ?> result = ReflectionUtil.writablePropertyExact(clazz, propertyName, propertyAccessorMethodFormats);
        log.debug("class:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", clazz, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(writableProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyNameAndWritablePropertys")
    <T> void writablePropertyExact(TypeToken<T> type, String propertyName, WritableProperty<T, ?> writableProperty) {
        WritableProperty<T, ?> result = ReflectionUtil.writablePropertyExact(type, propertyName);
        log.debug("type:{},propertyName:{},result:{}", type, propertyName, result);
        Assertions.assertEquals(writableProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyNameAndPropertyAccessorMethodFormatArrayAndWritablePropertys")
    <T> void writablePropertyExact(TypeToken<T> type, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, WritableProperty<T, ?> writableProperty) {
        WritableProperty<T, ?> result = ReflectionUtil.writablePropertyExact(type, propertyName, propertyAccessorMethodFormats);
        log.debug("type:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", type, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(writableProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorWritablePropertyName")
    void writablePropertyExactWithErrorPropertyName(Type type, String errorPropertyName) {
        log.debug("type:{},errorPropertyName:{}", type, errorPropertyName);
        Assertions.assertThrows(WritablePropertyNotFoundException.class, () -> ReflectionUtil.writablePropertyExact(type, errorPropertyName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorWritablePropertyNameAndPropertyAccessorMethodFormatArrays")
    void writablePropertyExactWithErrorPropertyName(Type type, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        log.debug("type:{},errorPropertyName:{},propertyAccessorMethodFormats:{}", type, errorPropertyName, propertyAccessorMethodFormats);
        Assertions.assertThrows(WritablePropertyNotFoundException.class, () -> ReflectionUtil.writablePropertyExact(type, errorPropertyName, propertyAccessorMethodFormats));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorWritablePropertyName")
    void writablePropertyExactWithErrorPropertyName(Class<?> clazz, String errorPropertyName) {
        log.debug("class:{},errorPropertyName:{}", clazz, errorPropertyName);
        Assertions.assertThrows(WritablePropertyNotFoundException.class, () -> ReflectionUtil.writablePropertyExact(clazz, errorPropertyName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorWritablePropertyNameAndPropertyAccessorMethodFormatArrays")
    void writablePropertyExactWithErrorPropertyName(Class<?> clazz, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        log.debug("class:{},errorPropertyName:{},propertyAccessorMethodFormats:{}", clazz, errorPropertyName, propertyAccessorMethodFormats);
        Assertions.assertThrows(WritablePropertyNotFoundException.class, () -> ReflectionUtil.writablePropertyExact(clazz, errorPropertyName, propertyAccessorMethodFormats));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndErrorWritablePropertyName")
    void writablePropertyExactWithErrorPropertyName(TypeToken<?> type, String errorPropertyName) {
        log.debug("type:{},errorPropertyName:{}", type, errorPropertyName);
        Assertions.assertThrows(WritablePropertyNotFoundException.class, () -> ReflectionUtil.writablePropertyExact(type, errorPropertyName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndErrorWritablePropertyNameAndPropertyAccessorMethodFormatArrays")
    void writablePropertyExactWithErrorPropertyName(TypeToken<?> type, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        log.debug("type:{},errorPropertyName:{},propertyAccessorMethodFormats:{}", type, errorPropertyName, propertyAccessorMethodFormats);
        Assertions.assertThrows(WritablePropertyNotFoundException.class, () -> ReflectionUtil.writablePropertyExact(type, errorPropertyName, propertyAccessorMethodFormats));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndWritablePropertys")
    <T, R> void writableProperty(Type type, String propertyName, WritableProperty<T, R> writableProperty) {
        Optional<WritableProperty<T, R>> result = ReflectionUtil.writableProperty(type, propertyName);
        log.debug("type:{},propertyName:{},result:{}", type, propertyName, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(writableProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndWritablePropertys")
    <T, R> void writableProperty(Type type, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, WritableProperty<T, R> writableProperty) {
        Optional<WritableProperty<T, R>> result = ReflectionUtil.writableProperty(type, propertyName, propertyAccessorMethodFormats);
        log.debug("type:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", type, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(writableProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndWritablePropertys")
    <T, R> void writableProperty(Class<T> clazz, String propertyName, WritableProperty<T, R> writableProperty) {
        Optional<WritableProperty<T, R>> result = ReflectionUtil.writableProperty(clazz, propertyName);
        log.debug("class:{},propertyName:{},result:{}", clazz, propertyName, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(writableProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndWritablePropertys")
    <T, R> void writableProperty(Class<T> clazz, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, WritableProperty<T, R> writableProperty) {
        Optional<WritableProperty<T, R>> result = ReflectionUtil.writableProperty(clazz, propertyName, propertyAccessorMethodFormats);
        log.debug("class:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", clazz, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(writableProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyNameAndWritablePropertys")
    <T, R> void writableProperty(TypeToken<T> type, String propertyName, WritableProperty<T, R> writableProperty) {
        Optional<WritableProperty<T, R>> result = ReflectionUtil.writableProperty(type, propertyName);
        log.debug("type:{},propertyName:{},result:{}", type, propertyName, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(writableProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyNameAndPropertyAccessorMethodFormatArrayAndWritablePropertys")
    <T, R> void writableProperty(TypeToken<T> type, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, WritableProperty<T, R> writableProperty) {
        Optional<WritableProperty<T, R>> result = ReflectionUtil.writableProperty(type, propertyName, propertyAccessorMethodFormats);
        log.debug("type:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", type, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(writableProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorWritablePropertyName")
    <T, R> void writablePropertyWithErrorPropertyName(Type type, String errorPropertyName) {
        Optional<WritableProperty<T, R>> result = ReflectionUtil.writableProperty(type, errorPropertyName);
        log.debug("type:{},errorPropertyName:{},result:{}", type, errorPropertyName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorWritablePropertyNameAndPropertyAccessorMethodFormatArrays")
    <T, R> void writablePropertyWithErrorPropertyName(Type type, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        Optional<WritableProperty<T, R>> result = ReflectionUtil.writableProperty(type, errorPropertyName, propertyAccessorMethodFormats);
        log.debug("type:{},errorPropertyName:{},propertyAccessorMethodFormats:{},result:{}", type, errorPropertyName, propertyAccessorMethodFormats, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorWritablePropertyName")
    <T, R> void writablePropertyWithErrorPropertyName(Class<T> clazz, String errorPropertyName) {
        Optional<WritableProperty<T, R>> result = ReflectionUtil.writableProperty(clazz, errorPropertyName);
        log.debug("class:{},errorPropertyName:{},result:{}", clazz, errorPropertyName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorWritablePropertyNameAndPropertyAccessorMethodFormatArrays")
    <T, R> void writablePropertyWithErrorPropertyName(Class<T> clazz, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        Optional<WritableProperty<T, R>> result = ReflectionUtil.writableProperty(clazz, errorPropertyName, propertyAccessorMethodFormats);
        log.debug("class:{},errorPropertyName:{},propertyAccessorMethodFormats:{},result:{}", clazz, errorPropertyName, propertyAccessorMethodFormats, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndErrorWritablePropertyName")
    <T, R> void writablePropertyWithErrorPropertyName(TypeToken<T> type, String errorPropertyName) {
        Optional<WritableProperty<T, R>> result = ReflectionUtil.writableProperty(type, errorPropertyName);
        log.debug("type:{},errorPropertyName:{},result:{}", type, errorPropertyName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndErrorWritablePropertyNameAndPropertyAccessorMethodFormatArrays")
    <T, R> void writablePropertyWithErrorPropertyName(TypeToken<T> type, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        Optional<WritableProperty<T, R>> result = ReflectionUtil.writableProperty(type, errorPropertyName, propertyAccessorMethodFormats);
        log.debug("type:{},errorPropertyName:{},propertyAccessorMethodFormats:{},result:{}", type, errorPropertyName, propertyAccessorMethodFormats, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndAccessiblePropertys")
    <T> void accessiblePropertyExact(Type type, String propertyName, AccessibleProperty<T, ?> accessibleProperty) {
        AccessibleProperty<T, ?> result = ReflectionUtil.accessiblePropertyExact(type, propertyName);
        log.debug("type:{},propertyName:{},result:{}", type, propertyName, result);
        Assertions.assertEquals(accessibleProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndAccessiblePropertys")
    <T> void accessiblePropertyExact(Type type, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, AccessibleProperty<T, ?> accessibleProperty) {
        AccessibleProperty<T, ?> result = ReflectionUtil.accessiblePropertyExact(type, propertyName, propertyAccessorMethodFormats);
        log.debug("type:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", type, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(accessibleProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndAccessiblePropertys")
    <T> void accessiblePropertyExact(Class<T> clazz, String propertyName, AccessibleProperty<T, ?> accessibleProperty) {
        AccessibleProperty<T, ?> result = ReflectionUtil.accessiblePropertyExact(clazz, propertyName);
        log.debug("class:{},propertyName:{},result:{}", clazz, propertyName, result);
        Assertions.assertEquals(accessibleProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndAccessiblePropertys")
    <T> void accessiblePropertyExact(Class<T> clazz, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, AccessibleProperty<T, ?> accessibleProperty) {
        AccessibleProperty<T, ?> result = ReflectionUtil.accessiblePropertyExact(clazz, propertyName, propertyAccessorMethodFormats);
        log.debug("class:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", clazz, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(accessibleProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyNameAndAccessiblePropertys")
    <T> void accessiblePropertyExact(TypeToken<T> type, String propertyName, AccessibleProperty<T, ?> accessibleProperty) {
        AccessibleProperty<T, ?> result = ReflectionUtil.accessiblePropertyExact(type, propertyName);
        log.debug("type:{},propertyName:{},result:{}", type, propertyName, result);
        Assertions.assertEquals(accessibleProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyNameAndPropertyAccessorMethodFormatArrayAndAccessiblePropertys")
    <T> void accessiblePropertyExact(TypeToken<T> type, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, AccessibleProperty<T, ?> accessibleProperty) {
        AccessibleProperty<T, ?> result = ReflectionUtil.accessiblePropertyExact(type, propertyName, propertyAccessorMethodFormats);
        log.debug("type:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", type, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertEquals(accessibleProperty, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorAccessiblePropertyName")
    void accessiblePropertyExactWithErrorPropertyName(Type type, String errorPropertyName) {
        log.debug("type:{},errorPropertyName:{}", type, errorPropertyName);
        Assertions.assertThrows(AccessiblePropertyNotFoundException.class, () -> ReflectionUtil.accessiblePropertyExact(type, errorPropertyName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorAccessiblePropertyNameAndPropertyAccessorMethodFormatArrays")
    void accessiblePropertyExactWithErrorPropertyName(Type type, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        log.debug("type:{},errorPropertyName:{},propertyAccessorMethodFormats:{}", type, errorPropertyName, propertyAccessorMethodFormats);
        Assertions.assertThrows(AccessiblePropertyNotFoundException.class, () -> ReflectionUtil.accessiblePropertyExact(type, errorPropertyName, propertyAccessorMethodFormats));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorAccessiblePropertyName")
    void accessiblePropertyExactWithErrorPropertyName(Class<?> clazz, String errorPropertyName) {
        log.debug("class:{},errorPropertyName:{}", clazz, errorPropertyName);
        Assertions.assertThrows(AccessiblePropertyNotFoundException.class, () -> ReflectionUtil.accessiblePropertyExact(clazz, errorPropertyName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorAccessiblePropertyNameAndPropertyAccessorMethodFormatArrays")
    void accessiblePropertyExactWithErrorPropertyName(Class<?> clazz, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        log.debug("class:{},errorPropertyName:{},propertyAccessorMethodFormats:{}", clazz, errorPropertyName, propertyAccessorMethodFormats);
        Assertions.assertThrows(AccessiblePropertyNotFoundException.class, () -> ReflectionUtil.accessiblePropertyExact(clazz, errorPropertyName, propertyAccessorMethodFormats));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndErrorAccessiblePropertyName")
    void accessiblePropertyExactWithErrorPropertyName(TypeToken<?> type, String errorPropertyName) {
        log.debug("type:{},errorPropertyName:{}", type, errorPropertyName);
        Assertions.assertThrows(AccessiblePropertyNotFoundException.class, () -> ReflectionUtil.accessiblePropertyExact(type, errorPropertyName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndErrorAccessiblePropertyNameAndPropertyAccessorMethodFormatArrays")
    void accessiblePropertyExactWithErrorPropertyName(TypeToken<?> type, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        log.debug("type:{},errorPropertyName:{},propertyAccessorMethodFormats:{}", type, errorPropertyName, propertyAccessorMethodFormats);
        Assertions.assertThrows(AccessiblePropertyNotFoundException.class, () -> ReflectionUtil.accessiblePropertyExact(type, errorPropertyName, propertyAccessorMethodFormats));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndAccessiblePropertys")
    <T, R> void accessibleProperty(Type type, String propertyName, AccessibleProperty<T, R> accessibleProperty) {
        Optional<AccessibleProperty<T, R>> result = ReflectionUtil.accessibleProperty(type, propertyName);
        log.debug("type:{},propertyName:{},result:{}", type, propertyName, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(accessibleProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndAccessiblePropertys")
    <T, R> void accessibleProperty(Type type, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, AccessibleProperty<T, R> accessibleProperty) {
        Optional<AccessibleProperty<T, R>> result = ReflectionUtil.accessibleProperty(type, propertyName, propertyAccessorMethodFormats);
        log.debug("type:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", type, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(accessibleProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndAccessiblePropertys")
    <T, R> void accessibleProperty(Class<T> clazz, String propertyName, AccessibleProperty<T, R> accessibleProperty) {
        Optional<AccessibleProperty<T, R>> result = ReflectionUtil.accessibleProperty(clazz, propertyName);
        log.debug("class:{},propertyName:{},result:{}", clazz, propertyName, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(accessibleProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndAccessiblePropertys")
    <T, R> void accessibleProperty(Class<T> clazz, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, AccessibleProperty<T, R> accessibleProperty) {
        Optional<AccessibleProperty<T, R>> result = ReflectionUtil.accessibleProperty(clazz, propertyName, propertyAccessorMethodFormats);
        log.debug("class:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", clazz, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(accessibleProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyNameAndAccessiblePropertys")
    <T, R> void accessibleProperty(TypeToken<T> type, String propertyName, AccessibleProperty<T, R> accessibleProperty) {
        Optional<AccessibleProperty<T, R>> result = ReflectionUtil.accessibleProperty(type, propertyName);
        log.debug("type:{},propertyName:{},result:{}", type, propertyName, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(accessibleProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyNameAndPropertyAccessorMethodFormatArrayAndAccessiblePropertys")
    <T, R> void accessibleProperty(TypeToken<T> type, String propertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats, AccessibleProperty<T, R> accessibleProperty) {
        Optional<AccessibleProperty<T, R>> result = ReflectionUtil.accessibleProperty(type, propertyName, propertyAccessorMethodFormats);
        log.debug("type:{},propertyName:{},propertyAccessorMethodFormats:{},result:{}", type, propertyName, propertyAccessorMethodFormats, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(accessibleProperty, result.get());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorAccessiblePropertyName")
    <T, R> void accessiblePropertyWithErrorPropertyName(Type type, String errorPropertyName) {
        Optional<AccessibleProperty<T, R>> result = ReflectionUtil.accessibleProperty(type, errorPropertyName);
        log.debug("type:{},errorPropertyName:{},result:{}", type, errorPropertyName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorAccessiblePropertyNameAndPropertyAccessorMethodFormatArrays")
    <T, R> void accessiblePropertyWithErrorPropertyName(Type type, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        Optional<AccessibleProperty<T, R>> result = ReflectionUtil.accessibleProperty(type, errorPropertyName, propertyAccessorMethodFormats);
        log.debug("type:{},errorPropertyName:{},propertyAccessorMethodFormats:{},result:{}", type, errorPropertyName, propertyAccessorMethodFormats, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorAccessiblePropertyName")
    <T, R> void accessiblePropertyWithErrorPropertyName(Class<T> clazz, String errorPropertyName) {
        Optional<AccessibleProperty<T, R>> result = ReflectionUtil.accessibleProperty(clazz, errorPropertyName);
        log.debug("class:{},errorPropertyName:{},result:{}", clazz, errorPropertyName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndErrorAccessiblePropertyNameAndPropertyAccessorMethodFormatArrays")
    <T, R> void accessiblePropertyWithErrorPropertyName(Class<T> clazz, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        Optional<AccessibleProperty<T, R>> result = ReflectionUtil.accessibleProperty(clazz, errorPropertyName, propertyAccessorMethodFormats);
        log.debug("class:{},errorPropertyName:{},propertyAccessorMethodFormats:{},result:{}", clazz, errorPropertyName, propertyAccessorMethodFormats, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndErrorAccessiblePropertyName")
    <T, R> void accessiblePropertyWithErrorPropertyName(TypeToken<T> type, String errorPropertyName) {
        Optional<AccessibleProperty<T, R>> result = ReflectionUtil.accessibleProperty(type, errorPropertyName);
        log.debug("type:{},errorPropertyName:{},result:{}", type, errorPropertyName, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndErrorAccessiblePropertyNameAndPropertyAccessorMethodFormatArrays")
    <T, R> void accessiblePropertyWithErrorPropertyName(TypeToken<T> type, String errorPropertyName, PropertyAccessorMethodFormat[] propertyAccessorMethodFormats) {
        Optional<AccessibleProperty<T, R>> result = ReflectionUtil.accessibleProperty(type, errorPropertyName, propertyAccessorMethodFormats);
        log.debug("type:{},errorPropertyName:{},propertyAccessorMethodFormats:{},result:{}", type, errorPropertyName, propertyAccessorMethodFormats, result);
        Assertions.assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyElementSets")
    <T> void propertyElements(Type type, Set<PropertyElement<T, ?>> propertyElements) {
        Set<PropertyElement<T, ?>> result = ReflectionUtil.propertyElements(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(propertyElements, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyElementSets")
    <T> void propertyElements(Class<T> clazz, Set<PropertyElement<T, ?>> propertyElements) {
        Set<PropertyElement<T, ?>> result = ReflectionUtil.propertyElements(clazz);
        log.debug("clazz:{},result:{}", clazz, result);
        Assertions.assertEquals(propertyElements, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyElementSets")
    <T> void propertyElements(TypeToken<T> type, Set<PropertyElement<T, ?>> propertyElements) {
        Set<PropertyElement<T, ?>> result = ReflectionUtil.propertyElements(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(propertyElements, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertyElementSets")
    <T> void propertyElements(Type type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyElement<T, ?>> propertyElements) {
        Set<PropertyElement<T, ?>> result = ReflectionUtil.propertyElements(type, propertyAccessorMethodFormat);
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyElements, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertyElementSets")
    <T> void propertyElements(Class<T> clazz, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyElement<T, ?>> propertyElements) {
        Set<PropertyElement<T, ?>> result = ReflectionUtil.propertyElements(clazz, propertyAccessorMethodFormat);
        log.debug("clazz:{},propertyAccessorMethodFormat:{},result:{}", clazz, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyElements, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorMethodFormatAndPropertyElementSets")
    <T> void propertyElements(TypeToken<T> type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyElement<T, ?>> propertyElements) {
        Set<PropertyElement<T, ?>> result = ReflectionUtil.propertyElements(type, propertyAccessorMethodFormat);
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyElements, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyElementSets")
    void propertyElementStream(Type type, Set<PropertyElement<?, ?>> propertyElements) {
        Set<PropertyElement<?, ?>> result = ReflectionUtil.propertyElementStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(propertyElements, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyElementSets")
    <T> void propertyElementStream(Class<T> clazz, Set<PropertyElement<T, ?>> propertyElements) {
        Set<PropertyElement<T, ?>> result = ReflectionUtil.propertyElementStream(clazz).collect(ImmutableSet.toImmutableSet());
        log.debug("clazz:{},result:{}", clazz, result);
        Assertions.assertEquals(propertyElements, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyElementSets")
    <T> void propertyElementStream(TypeToken<T> type, Set<PropertyElement<T, ?>> propertyElements) {
        Set<PropertyElement<T, ?>> result = ReflectionUtil.propertyElementStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(propertyElements, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertyElementSets")
    void propertyElementStream(Type type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyElement<?, ?>> propertyElements) {
        Set<PropertyElement<?, ?>> result = ReflectionUtil.propertyElementStream(type, propertyAccessorMethodFormat).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyElements, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertyElementSets")
    <T> void propertyElementStream(Class<T> clazz, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyElement<T, ?>> propertyElements) {
        Set<PropertyElement<T, ?>> result = ReflectionUtil.propertyElementStream(clazz, propertyAccessorMethodFormat).collect(ImmutableSet.toImmutableSet());
        log.debug("clazz:{},propertyAccessorMethodFormat:{},result:{}", clazz, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyElements, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorMethodFormatAndPropertyElementSets")
    <T> void propertyElementStream(TypeToken<T> type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyElement<T, ?>> propertyElements) {
        Set<PropertyElement<T, ?>> result = ReflectionUtil.propertyElementStream(type, propertyAccessorMethodFormat).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyElements, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyReaderSets")
    <T> void propertyReaders(Type type, Set<PropertyReader<T, ?>> propertyReaders) {
        Set<PropertyReader<T, ?>> result = ReflectionUtil.propertyReaders(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(propertyReaders, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyReaderSets")
    <T> void propertyReaders(Class<T> clazz, Set<PropertyReader<T, ?>> propertyReaders) {
        Set<PropertyReader<T, ?>> result = ReflectionUtil.propertyReaders(clazz);
        log.debug("clazz:{},result:{}", clazz, result);
        Assertions.assertEquals(propertyReaders, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyReaderSets")
    <T> void propertyReaders(TypeToken<T> type, Set<PropertyReader<T, ?>> propertyReaders) {
        Set<PropertyReader<T, ?>> result = ReflectionUtil.propertyReaders(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(propertyReaders, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertyReaderSets")
    <T> void propertyReaders(Type type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyReader<T, ?>> propertyReaders) {
        Set<PropertyReader<T, ?>> result = ReflectionUtil.propertyReaders(type, propertyAccessorMethodFormat);
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyReaders, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertyReaderSets")
    <T> void propertyReaders(Class<T> clazz, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyReader<T, ?>> propertyReaders) {
        Set<PropertyReader<T, ?>> result = ReflectionUtil.propertyReaders(clazz, propertyAccessorMethodFormat);
        log.debug("clazz:{},propertyAccessorMethodFormat:{},result:{}", clazz, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyReaders, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorMethodFormatAndPropertyReaderSets")
    <T> void propertyReaders(TypeToken<T> type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyReader<T, ?>> propertyReaders) {
        Set<PropertyReader<T, ?>> result = ReflectionUtil.propertyReaders(type, propertyAccessorMethodFormat);
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyReaders, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyReaderSets")
    void propertyReaderStream(Type type, Set<PropertyReader<?, ?>> propertyReaders) {
        Set<PropertyReader<?, ?>> result = ReflectionUtil.propertyReaderStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(propertyReaders, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyReaderSets")
    <T> void propertyReaderStream(Class<T> clazz, Set<PropertyReader<T, ?>> propertyReaders) {
        Set<PropertyReader<T, ?>> result = ReflectionUtil.propertyReaderStream(clazz).collect(ImmutableSet.toImmutableSet());
        log.debug("clazz:{},result:{}", clazz, result);
        Assertions.assertEquals(propertyReaders, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyReaderSets")
    <T> void propertyReaderStream(TypeToken<T> type, Set<PropertyReader<T, ?>> propertyReaders) {
        Set<PropertyReader<T, ?>> result = ReflectionUtil.propertyReaderStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(propertyReaders, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertyReaderSets")
    void propertyReaderStream(Type type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyReader<?, ?>> propertyReaders) {
        Set<PropertyReader<?, ?>> result = ReflectionUtil.propertyReaderStream(type, propertyAccessorMethodFormat).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyReaders, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertyReaderSets")
    <T> void propertyReaderStream(Class<T> clazz, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyReader<T, ?>> propertyReaders) {
        Set<PropertyReader<T, ?>> result = ReflectionUtil.propertyReaderStream(clazz, propertyAccessorMethodFormat).collect(ImmutableSet.toImmutableSet());
        log.debug("clazz:{},propertyAccessorMethodFormat:{},result:{}", clazz, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyReaders, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorMethodFormatAndPropertyReaderSets")
    <T> void propertyReaderStream(TypeToken<T> type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyReader<T, ?>> propertyReaders) {
        Set<PropertyReader<T, ?>> result = ReflectionUtil.propertyReaderStream(type, propertyAccessorMethodFormat).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyReaders, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyWriterSets")
    <T> void propertyWriters(Type type, Set<PropertyWriter<T, ?>> propertyWriters) {
        Set<PropertyWriter<T, ?>> result = ReflectionUtil.propertyWriters(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(propertyWriters, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyWriterSets")
    <T> void propertyWriters(Class<T> clazz, Set<PropertyWriter<T, ?>> propertyWriters) {
        Set<PropertyWriter<T, ?>> result = ReflectionUtil.propertyWriters(clazz);
        log.debug("clazz:{},result:{}", clazz, result);
        Assertions.assertEquals(propertyWriters, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyWriterSets")
    <T> void propertyWriters(TypeToken<T> type, Set<PropertyWriter<T, ?>> propertyWriters) {
        Set<PropertyWriter<T, ?>> result = ReflectionUtil.propertyWriters(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(propertyWriters, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertyWriterSets")
    <T> void propertyWriters(Type type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyWriter<T, ?>> propertyWriters) {
        Set<PropertyWriter<T, ?>> result = ReflectionUtil.propertyWriters(type, propertyAccessorMethodFormat);
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyWriters, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertyWriterSets")
    <T> void propertyWriters(Class<T> clazz, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyWriter<T, ?>> propertyWriters) {
        Set<PropertyWriter<T, ?>> result = ReflectionUtil.propertyWriters(clazz, propertyAccessorMethodFormat);
        log.debug("clazz:{},propertyAccessorMethodFormat:{},result:{}", clazz, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyWriters, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorMethodFormatAndPropertyWriterSets")
    <T> void propertyWriters(TypeToken<T> type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyWriter<T, ?>> propertyWriters) {
        Set<PropertyWriter<T, ?>> result = ReflectionUtil.propertyWriters(type, propertyAccessorMethodFormat);
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyWriters, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyWriterSets")
    void propertyWriterStream(Type type, Set<PropertyWriter<?, ?>> propertyWriters) {
        Set<PropertyWriter<?, ?>> result = ReflectionUtil.propertyWriterStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(propertyWriters, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyWriterSets")
    <T> void propertyWriterStream(Class<T> clazz, Set<PropertyWriter<T, ?>> propertyWriters) {
        Set<PropertyWriter<T, ?>> result = ReflectionUtil.propertyWriterStream(clazz).collect(ImmutableSet.toImmutableSet());
        log.debug("clazz:{},result:{}", clazz, result);
        Assertions.assertEquals(propertyWriters, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyWriterSets")
    <T> void propertyWriterStream(TypeToken<T> type, Set<PropertyWriter<T, ?>> propertyWriters) {
        Set<PropertyWriter<T, ?>> result = ReflectionUtil.propertyWriterStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(propertyWriters, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertyWriterSets")
    void propertyWriterStream(Type type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyWriter<?, ?>> propertyWriters) {
        Set<PropertyWriter<?, ?>> result = ReflectionUtil.propertyWriterStream(type, propertyAccessorMethodFormat).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyWriters, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertyWriterSets")
    <T> void propertyWriterStream(Class<T> clazz, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyWriter<T, ?>> propertyWriters) {
        Set<PropertyWriter<T, ?>> result = ReflectionUtil.propertyWriterStream(clazz, propertyAccessorMethodFormat).collect(ImmutableSet.toImmutableSet());
        log.debug("clazz:{},propertyAccessorMethodFormat:{},result:{}", clazz, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyWriters, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorMethodFormatAndPropertyWriterSets")
    <T> void propertyWriterStream(TypeToken<T> type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyWriter<T, ?>> propertyWriters) {
        Set<PropertyWriter<T, ?>> result = ReflectionUtil.propertyWriterStream(type, propertyAccessorMethodFormat).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyWriters, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorSets")
    <T> void propertyAccessors(Type type, Set<PropertyAccessor<T, ?>> propertyAccessors) {
        Set<PropertyAccessor<T, ?>> result = ReflectionUtil.propertyAccessors(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(propertyAccessors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorSets")
    <T> void propertyAccessors(Class<T> clazz, Set<PropertyAccessor<T, ?>> propertyAccessors) {
        Set<PropertyAccessor<T, ?>> result = ReflectionUtil.propertyAccessors(clazz);
        log.debug("clazz:{},result:{}", clazz, result);
        Assertions.assertEquals(propertyAccessors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorSets")
    <T> void propertyAccessors(TypeToken<T> type, Set<PropertyAccessor<T, ?>> propertyAccessors) {
        Set<PropertyAccessor<T, ?>> result = ReflectionUtil.propertyAccessors(type);
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(propertyAccessors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertyAccessorSets")
    <T> void propertyAccessors(Type type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyAccessor<T, ?>> propertyAccessors) {
        Set<PropertyAccessor<T, ?>> result = ReflectionUtil.propertyAccessors(type, propertyAccessorMethodFormat);
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyAccessors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertyAccessorSets")
    <T> void propertyAccessors(Class<T> clazz, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyAccessor<T, ?>> propertyAccessors) {
        Set<PropertyAccessor<T, ?>> result = ReflectionUtil.propertyAccessors(clazz, propertyAccessorMethodFormat);
        log.debug("clazz:{},propertyAccessorMethodFormat:{},result:{}", clazz, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyAccessors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorMethodFormatAndPropertyAccessorSets")
    <T> void propertyAccessors(TypeToken<T> type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyAccessor<T, ?>> propertyAccessors) {
        Set<PropertyAccessor<T, ?>> result = ReflectionUtil.propertyAccessors(type, propertyAccessorMethodFormat);
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyAccessors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorSets")
    void propertyAccessorStream(Type type, Set<PropertyAccessor<?, ?>> propertyAccessors) {
        Set<PropertyAccessor<?, ?>> result = ReflectionUtil.propertyAccessorStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(propertyAccessors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorSets")
    <T> void propertyAccessorStream(Class<T> clazz, Set<PropertyAccessor<T, ?>> propertyAccessors) {
        Set<PropertyAccessor<T, ?>> result = ReflectionUtil.propertyAccessorStream(clazz).collect(ImmutableSet.toImmutableSet());
        log.debug("clazz:{},result:{}", clazz, result);
        Assertions.assertEquals(propertyAccessors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorSets")
    <T> void propertyAccessorStream(TypeToken<T> type, Set<PropertyAccessor<T, ?>> propertyAccessors) {
        Set<PropertyAccessor<T, ?>> result = ReflectionUtil.propertyAccessorStream(type).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},result:{}", type, result);
        Assertions.assertEquals(propertyAccessors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertyAccessorSets")
    void propertyAccessorStream(Type type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyAccessor<?, ?>> propertyAccessors) {
        Set<PropertyAccessor<?, ?>> result = ReflectionUtil.propertyAccessorStream(type, propertyAccessorMethodFormat).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyAccessors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertyAccessorSets")
    <T> void propertyAccessorStream(Class<T> clazz, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyAccessor<T, ?>> propertyAccessors) {
        Set<PropertyAccessor<T, ?>> result = ReflectionUtil.propertyAccessorStream(clazz, propertyAccessorMethodFormat).collect(ImmutableSet.toImmutableSet());
        log.debug("clazz:{},propertyAccessorMethodFormat:{},result:{}", clazz, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyAccessors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndPropertyAccessorMethodFormatAndPropertyAccessorSets")
    <T> void propertyAccessorStream(TypeToken<T> type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<PropertyAccessor<T, ?>> propertyAccessors) {
        Set<PropertyAccessor<T, ?>> result = ReflectionUtil.propertyAccessorStream(type, propertyAccessorMethodFormat).collect(ImmutableSet.toImmutableSet());
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(propertyAccessors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#unstaticFields")
    void isPropertyElement(Field field) {
        boolean result = ReflectionUtil.isPropertyElement(field);
        log.debug("field:{},result:{}", field, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#staticFields")
    void isPropertyElementError(Field field) {
        boolean result = ReflectionUtil.isPropertyElement(field);
        log.debug("field:{},result:{}", field, result);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#propertyElementMethods")
    void isPropertyElement(Method propertyElementMethod) {
        boolean result = ReflectionUtil.isPropertyElement(propertyElementMethod);
        log.debug("propertyElementMethod:{},result:{}", propertyElementMethod, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#propertyElementMethodAndPropertyAccessorMethodFormats")
    void isPropertyElement(Method propertyElementMethod, PropertyAccessorMethodFormat propertyAccessorMethodFormat) {
        boolean result = ReflectionUtil.isPropertyElement(propertyElementMethod, propertyAccessorMethodFormat);
        log.debug("propertyElementMethod:{},result:{}", propertyElementMethod, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#unstaticFields")
    void isPropertyReader(Field field) {
        boolean result = ReflectionUtil.isPropertyReader(field);
        log.debug("field:{},result:{}", field, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#staticFields")
    void isPropertyReaderError(Field field) {
        boolean result = ReflectionUtil.isPropertyReader(field);
        log.debug("field:{},result:{}", field, result);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#propertyReaderMethods")
    void isPropertyReader(Method propertyElementMethod) {
        boolean result = ReflectionUtil.isPropertyReader(propertyElementMethod);
        log.debug("propertyElementMethod:{},result:{}", propertyElementMethod, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#propertyReaderMethodAndPropertyAccessorMethodFormats")
    void isPropertyReader(Method propertyElementMethod, PropertyAccessorMethodFormat propertyAccessorMethodFormat) {
        boolean result = ReflectionUtil.isPropertyReader(propertyElementMethod, propertyAccessorMethodFormat);
        log.debug("propertyElementMethod:{},result:{}", propertyElementMethod, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#unstaticFields")
    void isPropertyWriter(Field field) {
        boolean result = ReflectionUtil.isPropertyWriter(field);
        log.debug("field:{},result:{}", field, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#staticFields")
    void isPropertyWriterError(Field field) {
        boolean result = ReflectionUtil.isPropertyWriter(field);
        log.debug("field:{},result:{}", field, result);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#propertyWriterMethods")
    void isPropertyWriter(Method propertyElementMethod) {
        boolean result = ReflectionUtil.isPropertyWriter(propertyElementMethod);
        log.debug("propertyElementMethod:{},result:{}", propertyElementMethod, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#propertyWriterMethodAndPropertyAccessorMethodFormats")
    void isPropertyWriter(Method propertyElementMethod, PropertyAccessorMethodFormat propertyAccessorMethodFormat) {
        boolean result = ReflectionUtil.isPropertyWriter(propertyElementMethod, propertyAccessorMethodFormat);
        log.debug("propertyElementMethod:{},result:{}", propertyElementMethod, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#getMethods")
    void isGetMethod(Method method) {
        boolean result = ReflectionUtil.isGetMethod(method);
        log.debug("method:{},result:{}", method, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#notGetMethods")
    void isGetMethodError(Method method) {
        boolean result = ReflectionUtil.isGetMethod(method);
        log.debug("method:{},result:{}", method, result);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#setMethods")
    void isSetMethod(Method method) {
        boolean result = ReflectionUtil.isSetMethod(method);
        log.debug("method:{},result:{}", method, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#notSetMethods")
    void isSetMethodError(Method method) {
        boolean result = ReflectionUtil.isSetMethod(method);
        log.debug("method:{},result:{}", method, result);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#methodAndSuperMethods")
    void isOverride(Method method, Method superMethod) {
        boolean result = ReflectionUtil.isOverridden(method, superMethod);
        log.debug("method:{},superMethod:{},result:{}", method, superMethod, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#methodAndNotSuperMethods")
    void isOverrideError(Method method, Method notSuperMethod) {
        boolean result = ReflectionUtil.isOverridden(method, notSuperMethod);
        log.debug("method:{},notSuperMethod:{},result:{}", method, notSuperMethod, result);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#methodAndSuperClasss")
    void isOverride(Method method, Type superType) {
        boolean result = ReflectionUtil.isOverridden(method, superType);
        log.debug("method:{},superType:{},result:{}", method, superType, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#methodAndNotSuperClasss")
    void isOverrideError(Method method, Type notSuperType) {
        boolean result = ReflectionUtil.isOverridden(method, notSuperType);
        log.debug("method:{},notSuperType:{},result:{}", method, notSuperType, result);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#methodAndSuperClasss")
    void isOverride(Method method, Class<?> superClass) {
        boolean result = ReflectionUtil.isOverridden(method, superClass);
        log.debug("method:{},superClass:{},result:{}", method, superClass, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#methodAndNotSuperClasss")
    void isOverrideError(Method method, Class<?> notSuperClass) {
        boolean result = ReflectionUtil.isOverridden(method, notSuperClass);
        log.debug("method:{},notSuperClass:{},result:{}", method, notSuperClass, result);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#methodAndSuperTypeTokens")
    void isOverride(Method method, TypeToken<?> superType) {
        boolean result = ReflectionUtil.isOverridden(method, superType);
        log.debug("method:{},superType:{},result:{}", method, superType, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#methodAndNotSuperTypeTokens")
    void isOverrideError(Method method, TypeToken<?> notSuperType) {
        boolean result = ReflectionUtil.isOverridden(method, notSuperType);
        log.debug("method:{},notSuperType:{},result:{}", method, notSuperType, result);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#overrideMethods")
    void isOverride(Method method) {
        boolean result = ReflectionUtil.isOverridden(method);
        log.debug("method:{},result:{}", method, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#notOverrideMethods")
    void isOverrideError(Method method) {
        boolean result = ReflectionUtil.isOverridden(method);
        log.debug("method:{},result:{}", method, result);
        Assertions.assertFalse(result);
    }


}