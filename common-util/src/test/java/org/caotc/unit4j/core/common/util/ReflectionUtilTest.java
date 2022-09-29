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
import org.caotc.unit4j.core.common.reflect.property.Property;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyAccessorMethodFormat;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyWriter;
import org.caotc.unit4j.core.common.util.model.*;
import org.caotc.unit4j.core.exception.MethodNotFoundException;
import org.junit.jupiter.api.Assertions;
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
import java.util.stream.Collectors;

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

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldType(Type type, String fieldName, Method setMethod) {
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, fieldName, (Type) Void.class));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldTypeAndErrorFieldName(Type type, String fieldName, Method setMethod) {
        Type fieldType = setMethod.getGenericParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, errorFieldName, fieldType));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldClass(Type type, String fieldName, Method setMethod) {
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, fieldName, Void.class));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldClassAndErrorFieldName(Type type, String fieldName, Method setMethod) {
        Class<?> fieldClass = setMethod.getParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, errorFieldName, fieldClass));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldTypeToken(Type type, String fieldName, Method setMethod) {
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, fieldName, TypeToken.of(Void.class)));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldTypeTokenAndErrorFieldName(Type type, String fieldName, Method setMethod) {
        TypeToken<?> fieldType = TypeToken.of(setMethod.getGenericParameterTypes()[0]);
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, errorFieldName, fieldType));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldType(Class<?> clazz, String fieldName, Method setMethod) {
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(clazz, fieldName, (Type) Void.class));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldTypeAndErrorFieldName(Class<?> clazz, String fieldName, Method setMethod) {
        Type fieldType = setMethod.getGenericParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(clazz, errorFieldName, fieldType));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldClass(Class<?> clazz, String fieldName, Method setMethod) {
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(clazz, fieldName, Void.class));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldClassAndErrorFieldName(Class<?> clazz, String fieldName, Method setMethod) {
        Class<?> fieldClass = setMethod.getParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(clazz, errorFieldName, fieldClass));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldTypeToken(Class<?> clazz, String fieldName, Method setMethod) {
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(clazz, fieldName, TypeToken.of(Void.class)));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndFieldNameAndSetMethods")
    void setMethodExactWithFieldTypeTokenAndErrorFieldName(Class<?> clazz, String fieldName, Method setMethod) {
        TypeToken<?> fieldType = TypeToken.of(setMethod.getGenericParameterTypes()[0]);
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(clazz, errorFieldName, fieldType));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldType(TypeToken<?> type, String fieldName, Method setMethod) {
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, fieldName, (Type) Void.class));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodExactWithFieldTypeAndErrorFieldName(TypeToken<?> type, String fieldName, Method setMethod) {
        Type fieldType = setMethod.getGenericParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, errorFieldName, fieldType));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldClass(TypeToken<?> type, String fieldName, Method setMethod) {
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, fieldName, Void.class));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodExactWithFieldClassAndErrorFieldName(TypeToken<?> type, String fieldName, Method setMethod) {
        Class<?> fieldType = setMethod.getParameterTypes()[0];
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, errorFieldName, fieldType));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodExactWithErrorFieldTypeToken(TypeToken<?> type, String fieldName, Method setMethod) {
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, fieldName, TypeToken.of(Void.class)));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndFieldNameAndSetMethods")
    void setMethodExactWithFieldTypeTokenAndErrorFieldName(TypeToken<?> type, String fieldName, Method setMethod) {
        TypeToken<?> fieldType = TypeToken.of(setMethod.getGenericParameterTypes()[0]);
        String errorFieldName = Math.random() + "";
        Assertions.assertThrows(MethodNotFoundException.class, () -> ReflectionUtil.setMethodExact(type, errorFieldName, fieldType));
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
        Assertions.assertThrows(IllegalArgumentException.class, () -> ReflectionUtil.setMethod(type, duplicateSetMethodFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndDuplicateSetMethodFieldNames")
    void setMethodWithDuplicateSetMethodFieldName(Class<?> clazz, String duplicateSetMethodFieldName) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ReflectionUtil.setMethod(clazz, duplicateSetMethodFieldName));
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenAndDuplicateSetMethodFieldNames")
    void setMethodWithDuplicateSetMethodFieldName(TypeToken<?> type, String duplicateSetMethodFieldName) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ReflectionUtil.setMethod(type, duplicateSetMethodFieldName));
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

//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    void propertyStream(Type type) {
//        //todo
//        Set<Property<?, ?>> result = ReflectionUtil.propertyStream(type).collect(ImmutableSet.toImmutableSet());
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    void propertyStream(Type type, PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
//        //todo
//        Set<Property<?, ?>> result = ReflectionUtil.propertyStream(type).collect(ImmutableSet.toImmutableSet());
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    void propertyStream(Class<?> clazz) {
//        //todo
//        Set<Property<?, ?>> result = ReflectionUtil.propertyStream(clazz).collect(ImmutableSet.toImmutableSet());
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    void propertyStream(Class<?> clazz, PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
//        //todo
//        Set<Property<?, ?>> result = ReflectionUtil.propertyStream(clazz).collect(ImmutableSet.toImmutableSet());
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokens")
//    void propertyStream(TypeToken<?> type) {
//        //todo
//        Set<Property<?, ?>> result = ReflectionUtil.propertyStream(type).collect(ImmutableSet.toImmutableSet());
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    void propertyStream(TypeToken<?> type, PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
//        //todo
//        Set<Property<?, ?>> result = ReflectionUtil.propertyStream(type).collect(ImmutableSet.toImmutableSet());
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    <T> void properties(Type type) {
//        //todo
//        Set<Property<T, ?>> result = ReflectionUtil.properties(type);
//    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classAndPropertyAccessorMethodFormatAndPropertySets")
    <T> void properties(Type type, PropertyAccessorMethodFormat propertyAccessorMethodFormat, Set<Property<T, ?>> properties) {
        //todo
        Set<Property<T, ?>> result = ReflectionUtil.properties(type);
        log.debug("type:{},propertyAccessorMethodFormat:{},result:{}", type, propertyAccessorMethodFormat, result);
        Assertions.assertEquals(properties, result);
    }

    @Test
    <T> void test() {
        Property<BooleanFieldGetMethodObject, ?> property = ReflectionUtil.properties(BooleanFieldGetMethodObject.class).iterator().next();
        Assertions.assertEquals(Constant.BOOLEAN_FIELD_GET_METHOD_OBJECT_BOOLEAN_PROPERTY, property);
    }

//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    <T> void properties(Class<T> clazz) {
//        //todo
//        Set<Property<T, ?>> result = ReflectionUtil.properties(clazz);
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    <T> void properties(Class<T> clazz, PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
//        //todo
//        Set<Property<T, ?>> result = ReflectionUtil.properties(clazz);
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    <T> void properties(TypeToken<T> type) {
//        //todo
//        Set<Property<T, ?>> result = ReflectionUtil.properties(type);
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    <T> void properties(TypeToken<T> type, PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
//        //todo
//        Set<Property<T, ?>> result = ReflectionUtil.properties(type);
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    void readablePropertyStream(Type type) {
//        //todo
//        Set<ReadableProperty<?, ?>> result = ReflectionUtil.readablePropertyStream(type).collect(ImmutableSet.toImmutableSet());
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    void readablePropertyStream(Type type, PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
//        //todo
//        Set<ReadableProperty<?, ?>> result = ReflectionUtil.readablePropertyStream(type).collect(ImmutableSet.toImmutableSet());
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    void readablePropertyStream(Class<?> clazz) {
//        //todo
//        Set<ReadableProperty<?, ?>> result = ReflectionUtil.readablePropertyStream(clazz).collect(ImmutableSet.toImmutableSet());
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    void readablePropertyStream(Class<?> clazz, PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
//        //todo
//        Set<ReadableProperty<?, ?>> result = ReflectionUtil.readablePropertyStream(clazz).collect(ImmutableSet.toImmutableSet());
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    void readablePropertyStream(TypeToken<?> type) {
//        //todo
//        Set<ReadableProperty<?, ?>> result = ReflectionUtil.readablePropertyStream(type).collect(ImmutableSet.toImmutableSet());
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    void readablePropertyStream(TypeToken<?> type, PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
//        //todo
//        Set<ReadableProperty<?, ?>> result = ReflectionUtil.readablePropertyStream(type).collect(ImmutableSet.toImmutableSet());
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    <T> void readableProperties(Type type) {
//        //todo
//        Set<ReadableProperty<T, ?>> result = ReflectionUtil.readableProperties(type);
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    <T> void readableProperties(Type type, PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
//        //todo
//        Set<ReadableProperty<T, ?>> result = ReflectionUtil.readableProperties(type);
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    <T> void readableProperties(Class<T> clazz) {
//        //todo
//        Set<ReadableProperty<T, ?>> result = ReflectionUtil.readableProperties(clazz);
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    <T> void readableProperties(Class<T> clazz, PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
//        //todo
//        Set<ReadableProperty<T, ?>> result = ReflectionUtil.readableProperties(clazz);
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    <T> void readableProperties(TypeToken<T> type) {
//        //todo
//        Set<ReadableProperty<T, ?>> result = ReflectionUtil.readableProperties(type);
//    }
//
//    @ParameterizedTest
//    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classes")
//    <T> void readableProperties(TypeToken<T> type, PropertyAccessorMethodFormat... propertyAccessorMethodFormats) {
//        //todo
//        Set<ReadableProperty<T, ?>> result = ReflectionUtil.readableProperties(type);
//    }

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

    @SneakyThrows
    @Test
    void readerEquals() {
        Set<PropertyReader<Sub, ?>> result = ReflectionUtil.propertyReaders(Sub.class).stream().filter(r -> r.propertyName().equals("stringField") && !r.basedOnField() && !r.isAbstract()).collect(Collectors.toSet());
        Assertions.assertTrue(result.contains(PropertyConstant.SUB_STRING_FIELD_GET_METHOD_READER));


        Invokable<?, Object> invokable1 = Invokable.from(PropertyConstant.SUB_STRING_FIELD_GET_METHOD);
        Invokable<?, Object> invokable2 = TypeToken.of(Sub.class).method(PropertyConstant.SUB_STRING_FIELD_GET_METHOD);
        log.info("invokable1 getDeclaringClass:{}", invokable1.getDeclaringClass());
        log.info("invokable2 getDeclaringClass:{}", invokable2.getDeclaringClass());
        log.info("invokable1 getOwnerType:{}", invokable1.getOwnerType());
        log.info("invokable2 getOwnerType:{}", invokable2.getOwnerType());
        Assertions.assertEquals(invokable1, invokable2);
        //        Assertions.assertEquals(PropertyConstant.SUB_STRING_FIELD_GET_METHOD_READER, reader);
    }

//    @Test
//    void readablePropertyFromClass() {
//        Optional<ReadableProperty<Sub, Object>> stringField = ReflectionUtil
//                .readablePropertyFromClass(Sub.class, "stringField");
//        Assertions.assertTrue(stringField.isPresent());
//        Optional<ReadableProperty<Sub, Object>> nonField = ReflectionUtil
//                .readablePropertyFromClass(Sub.class, "nonField");
//        Assertions.assertFalse(nonField.isPresent());
//    }
//
//    @Test
//    void readablePropertyFromClass1() {
//        Optional<ReadableProperty<Sub, Object>> readNumberField = ReflectionUtil
//                .readablePropertyFromClass(Sub.class, "readNumberField");
//        Assertions.assertTrue(readNumberField.isPresent());
//    }
//
//    @Test
//    void readablePropertyFromClass2() {
//        Optional<ReadableProperty<Sub, Object>> javaBeanReadNumberField = ReflectionUtil
//                .readablePropertyFromClass(Sub.class, "readNumberField",
//                        PropertyAccessorMethodFormat.JAVA_BEAN);
//        Assertions.assertTrue(javaBeanReadNumberField.isPresent());
//        Optional<ReadableProperty<Sub, Object>> fluentReadNumberField = ReflectionUtil
//                .readablePropertyFromClass(Sub.class, "readNumberField",
//                        PropertyAccessorMethodFormat.FLUENT);
//        Assertions.assertTrue(fluentReadNumberField.isPresent());
//    }
//
//    @Test
//    void writablePropertiesFromClass() {
//        ImmutableSet<WritableProperty<Sub, ?>> writableProperties = ReflectionUtil
//                .writablePropertiesFromClass(Sub.class);
//        for (WritableProperty<Sub, ?> writableProperty : writableProperties) {
//            log.debug("writableProperty:{}", writableProperty.name() + "," + writableProperty.type());
//        }
//        Assertions.assertEquals(5, writableProperties.size());
//    }
//
//    @Test
//    void writablePropertiesFromClass1() {
//        ImmutableSet<WritableProperty<Sub, ?>> writableProperties = ReflectionUtil
//                .writablePropertiesFromClass(Sub.class);
//        writableProperties.forEach(
//                writableProperty -> log.debug(writableProperty.name() + ":" + writableProperty.type()));
//        Assertions.assertEquals(5, writableProperties.size());
//    }
//
//    @Test
//    void writablePropertiesFromClass2() {
//        ImmutableSet<WritableProperty<Sub, ?>> writableProperties = ReflectionUtil
//                .writablePropertiesFromClass(Sub.class, PropertyAccessorMethodFormat.FLUENT);
//        log.debug("writableProperties:{}", writableProperties);
//        Assertions.assertEquals(3, writableProperties.size());
//    }
//
//    @Test
//    void writablePropertyFromClass() {
//        Optional<WritableProperty<Sub, Object>> stringField = ReflectionUtil
//                .writablePropertyFromClass(Sub.class, "stringField");
//        Assertions.assertTrue(stringField.isPresent());
//        Optional<WritableProperty<Sub, Object>> nonField = ReflectionUtil
//                .writablePropertyFromClass(Sub.class, "nonField");
//        Assertions.assertFalse(nonField.isPresent());
//    }
//
//    @Test
//    void writablePropertyFromClass1() {
//        Optional<WritableProperty<Sub, Object>> stringField = ReflectionUtil
//                .writablePropertyFromClass(Sub.class, "stringField");
//        Assertions.assertTrue(stringField.isPresent());
//    }
//
//    @Test
//    void writablePropertyFromClass2() {
//        Optional<WritableProperty<Sub, Object>> stringField = ReflectionUtil
//                .writablePropertyFromClass(Sub.class, "stringField",
//                        PropertyAccessorMethodFormat.FLUENT);
//        Assertions.assertTrue(stringField.isPresent());
//    }

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

    //    @RepeatedTest(1000)
    void isOverrideTest() {
        List<Method> setMethods = Lists.newArrayList(PropertyConstant.SUPER_STRING_FIELD_SET_METHOD, PropertyConstant.SUB_STRING_FIELD_SET_METHOD, PropertyConstant.STRING_FIELD_SETTER_STRING_FIELD_SET_METHOD);
        Collections.shuffle(setMethods);
        log.debug("setMethods:{}", setMethods);
        Optional<Method> actual = setMethods.stream().reduce((m1, m2) -> ReflectionUtil.isOverride(m1, m2) ? m1 : m2);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(PropertyConstant.SUB_STRING_FIELD_SET_METHOD, actual.get());
    }

    @Test
    @SneakyThrows
    void aaae() {
        TypeToken<StringFieldGetter> stringFieldGetterType = TypeToken.of(StringFieldGetter.class);
        TypeToken<Object> objectType = TypeToken.of(Object.class);
        log.info("stringFieldGetterType.isSubtypeOf(objectType):{}", stringFieldGetterType.isSubtypeOf(objectType));
        log.info("objectType.isSubtypeOf(stringFieldGetterType):{}", objectType.isSubtypeOf(stringFieldGetterType));
        log.info("stringFieldGetterType.isSupertypeOf(objectType):{}", stringFieldGetterType.isSupertypeOf(objectType));
        log.info("objectType.isSupertypeOf(stringFieldGetterType):{}", objectType.isSupertypeOf(stringFieldGetterType));
        log.info("StringFieldGetter.class.isAssignableFrom(Object.class):{}", StringFieldGetter.class.isAssignableFrom(Object.class));
        log.info("Object.class.isAssignableFrom(StringFieldGetter.class):{}", Object.class.isAssignableFrom(StringFieldGetter.class));
        log.info("TypeToken.of(String.class).getTypes():{}", TypeToken.of(String.class).getTypes());
        log.info("TypeToken.of(Integer.class).getTypes():{}", TypeToken.of(Integer.class).getTypes());
        log.info("TypeToken.of(int.class).getTypes():{}", TypeToken.of(int.class).getTypes());
        log.info("lowestCommonSuperclasses(Lists.newArrayList(String.class,Integer.class)):{}", ReflectionUtil.lowestCommonSuperclasses(Lists.newArrayList(String.class, Integer.class)));
        log.info("lowestCommonSuperclasses(Lists.newArrayList(int.class,Integer.class)):{}", ReflectionUtil.lowestCommonSuperclasses(Lists.newArrayList(int.class, Integer.class)));
        log.info("lowestCommonSuperclasses(Lists.newArrayList(Integer.class,Long.class)):{}", ReflectionUtil.lowestCommonSuperclasses(Lists.newArrayList(Integer.class, Long.class)));
        log.info("lowestCommonAncestors(Lists.newArrayList(String.class,Integer.class)):{}", ReflectionUtil.lowestCommonAncestors(Lists.newArrayList(TypeToken.of(String.class), TypeToken.of(Integer.class))));
        log.info("lowestCommonAncestors(Lists.newArrayList(int.class,Integer.class)):{}", ReflectionUtil.lowestCommonAncestors(Lists.newArrayList(TypeToken.of(int.class), TypeToken.of(Integer.class))));
        log.info("lowestCommonAncestors(Lists.newArrayList(Integer.class,Long.class)):{}", ReflectionUtil.lowestCommonAncestors(Lists.newArrayList(TypeToken.of(Integer.class), TypeToken.of(Long.class))));
    }
}