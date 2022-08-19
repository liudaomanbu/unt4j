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
import com.google.common.collect.Lists;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.reflect.property.ReadableProperty;
import org.caotc.unit4j.core.common.reflect.property.WritableProperty;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyAccessorMethodFormat;
import org.caotc.unit4j.core.common.util.model.PropertyConstant;
import org.caotc.unit4j.core.common.util.model.Sub;
import org.caotc.unit4j.core.exception.MethodNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
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
    void constructors(Type type, Set<Constructor<?>> constructors) {
        Set<Constructor<?>> result = ReflectionUtil.constructors(type);
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
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.getMethodExact(type, errorFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
    void getMethodExactWithErrorFieldName(Class<?> clazz) {
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.getMethodExact(clazz, errorFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokens")
    void getMethodExactWithErrorFieldName(TypeToken<?> type) {
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.getMethodExact(type, errorFieldName));
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
        Assertions.assertThrows(IllegalArgumentException.class, () -> ReflectionUtil.setMethodExact(type, duplicateSetMethodFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndDuplicateSetMethodFieldNames")
    void setMethodExactWithDuplicateSetMethodFieldName(Class<?> clazz, String duplicateSetMethodFieldName) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ReflectionUtil.setMethodExact(clazz, duplicateSetMethodFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndDuplicateSetMethodFieldNames")
    void setMethodExactWithDuplicateSetMethodFieldName(TypeToken<?> type, String duplicateSetMethodFieldName) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ReflectionUtil.setMethodExact(type, duplicateSetMethodFieldName));
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

    @Test
    void readablePropertiesFromClass() {
        ImmutableSet<ReadableProperty<Sub, ?>> readableProperties = ReflectionUtil
                .readablePropertiesFromClass(Sub.class);
        log.debug("readableProperties:{}", readableProperties);
        Assertions.assertEquals(4, readableProperties.size());
    }

    @Test
    void readablePropertiesFromClass2() {
        ImmutableSet<ReadableProperty<Sub, ?>> readableProperties = ReflectionUtil
                .readablePropertiesFromClass(Sub.class, PropertyAccessorMethodFormat.FLUENT);
        log.debug("readableProperties:{}", readableProperties);
        Assertions.assertEquals(3, readableProperties.size());
    }

    @Test
    void readablePropertyFromClass() {
        Optional<ReadableProperty<Sub, Object>> stringField = ReflectionUtil
                .readablePropertyFromClass(Sub.class, "stringField");
        Assertions.assertTrue(stringField.isPresent());
        Optional<ReadableProperty<Sub, Object>> nonField = ReflectionUtil
                .readablePropertyFromClass(Sub.class, "nonField");
        Assertions.assertFalse(nonField.isPresent());
    }

    @Test
    void readablePropertyFromClass1() {
        Optional<ReadableProperty<Sub, Object>> readNumberField = ReflectionUtil
                .readablePropertyFromClass(Sub.class, "readNumberField");
        Assertions.assertTrue(readNumberField.isPresent());
    }

    @Test
    void readablePropertyFromClass2() {
        Optional<ReadableProperty<Sub, Object>> javaBeanReadNumberField = ReflectionUtil
                .readablePropertyFromClass(Sub.class, "readNumberField",
                        PropertyAccessorMethodFormat.JAVA_BEAN);
        Assertions.assertTrue(javaBeanReadNumberField.isPresent());
        Optional<ReadableProperty<Sub, Object>> fluentReadNumberField = ReflectionUtil
                .readablePropertyFromClass(Sub.class, "readNumberField",
                        PropertyAccessorMethodFormat.FLUENT);
        Assertions.assertTrue(fluentReadNumberField.isPresent());
    }

    @Test
    void writablePropertiesFromClass() {
        ImmutableSet<WritableProperty<Sub, ?>> writableProperties = ReflectionUtil
                .writablePropertiesFromClass(Sub.class);
        for (WritableProperty<Sub, ?> writableProperty : writableProperties) {
            log.debug("writableProperty:{}", writableProperty.name() + "," + writableProperty.type());
        }
        Assertions.assertEquals(5, writableProperties.size());
    }

    @Test
    void writablePropertiesFromClass1() {
        ImmutableSet<WritableProperty<Sub, ?>> writableProperties = ReflectionUtil
                .writablePropertiesFromClass(Sub.class);
        writableProperties.forEach(
                writableProperty -> log.debug(writableProperty.name() + ":" + writableProperty.type()));
        Assertions.assertEquals(5, writableProperties.size());
    }

    @Test
    void writablePropertiesFromClass2() {
        ImmutableSet<WritableProperty<Sub, ?>> writableProperties = ReflectionUtil
                .writablePropertiesFromClass(Sub.class, PropertyAccessorMethodFormat.FLUENT);
        log.debug("writableProperties:{}", writableProperties);
        Assertions.assertEquals(3, writableProperties.size());
    }

    @Test
    void writablePropertyFromClass() {
        Optional<WritableProperty<Sub, Object>> stringField = ReflectionUtil
                .writablePropertyFromClass(Sub.class, "stringField");
        Assertions.assertTrue(stringField.isPresent());
        Optional<WritableProperty<Sub, Object>> nonField = ReflectionUtil
                .writablePropertyFromClass(Sub.class, "nonField");
        Assertions.assertFalse(nonField.isPresent());
    }

    @Test
    void writablePropertyFromClass1() {
        Optional<WritableProperty<Sub, Object>> stringField = ReflectionUtil
                .writablePropertyFromClass(Sub.class, "stringField");
        Assertions.assertTrue(stringField.isPresent());
    }

    @Test
    void writablePropertyFromClass2() {
        Optional<WritableProperty<Sub, Object>> stringField = ReflectionUtil
                .writablePropertyFromClass(Sub.class, "stringField",
                        PropertyAccessorMethodFormat.FLUENT);
        Assertions.assertTrue(stringField.isPresent());
    }

    @Test
    @SneakyThrows
    void isGetMethod() {
        boolean isGetMethod = ReflectionUtil
                .isPropertyReader(Sub.class.getDeclaredMethod("stringField"),
                        PropertyAccessorMethodFormat.FLUENT);
        Assertions.assertTrue(isGetMethod);
    }

    @Test
    @SneakyThrows
    void isGetInvokable() {
        boolean isGetInvokable = ReflectionUtil
                .isPropertyReader(Invokable.from(Sub.class.getDeclaredMethod("stringField")),
                        PropertyAccessorMethodFormat.FLUENT);
        Assertions.assertTrue(isGetInvokable);
    }

    @Test
    @SneakyThrows
    void isSetMethod() {
        boolean isSetMethod = ReflectionUtil
                .isPropertyWriter(Sub.class.getDeclaredMethod("stringField", String.class),
                        PropertyAccessorMethodFormat.FLUENT);
        Assertions.assertTrue(isSetMethod);
    }

    @Test
    @SneakyThrows
    void isSetInvokable() {
        boolean isSetInvokable = ReflectionUtil
                .isPropertyWriter(Invokable.from(Sub.class.getDeclaredMethod("stringField", String.class)),
                        PropertyAccessorMethodFormat.FLUENT);
        Assertions.assertTrue(isSetInvokable);
    }

    @RepeatedTest(1000)
    void isOverrideTest() {
        List<Method> setMethods = Lists.newArrayList(PropertyConstant.SUPER_STRING_FIELD_SET_METHOD, PropertyConstant.SUB_STRING_FIELD_SET_METHOD, PropertyConstant.STRING_FIELD_SETTER_STRING_FIELD_SET_METHOD);
        Collections.shuffle(setMethods);
        log.debug("setMethods:{}", setMethods);
        Optional<Method> actual = setMethods.stream().reduce((m1, m2) -> ReflectionUtil.isOverride(m1, m2) ? m1 : m2);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(PropertyConstant.SUB_STRING_FIELD_SET_METHOD, actual.get());
    }
}