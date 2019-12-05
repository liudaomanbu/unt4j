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

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.Invokable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.reflect.MethodNameStyle;
import org.caotc.unit4j.core.common.reflect.ReadableProperty;
import org.caotc.unit4j.core.common.reflect.WritableProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
@Slf4j
class ReflectionUtilTest {

  @Test
  void fieldsFromClass() {
    ImmutableSet<Field> fields = ReflectionUtil.fieldsFromClass(Sub.class);
    log.debug("fields:{}", fields);
    Assertions.assertEquals(4, fields.size());
  }

  @Test
  void fieldsFromClass1() {
    ImmutableSet<Field> stringFields = ReflectionUtil.fieldsFromClass(Sub.class, "stringField");
    log.debug("stringFields:{}", stringFields);
    Assertions.assertEquals(2, stringFields.size());
    ImmutableSet<Field> numberFields = ReflectionUtil.fieldsFromClass(Sub.class, "numberField");
    log.debug("numberFields:{}", numberFields);
    Assertions.assertEquals(1, numberFields.size());
  }

  @Test
  void methodsFromClass() {
    ImmutableSet<Method> methods = ReflectionUtil.methodsFromClass(Sub.class);
    log.debug("methods:{}", methods);
    Assertions.assertEquals(17 + ReflectionUtil.methodsFromClass(Sub.class.getSuperclass()).size(),
        methods.size());
  }

  @Test
  void getMethodsFromClass() {
    ImmutableSet<Method> getMethods = ReflectionUtil.getMethodsFromClass(Sub.class);
    log.debug("getMethods:{}", getMethods);
    Assertions.assertEquals(5, getMethods.size());
  }

  @Test
  void getMethodsFromClass1() {
    ImmutableSet<Method> getMethods = ReflectionUtil.getMethodsFromClass(Sub.class, false);
    log.debug("getMethods:{}", getMethods);
    Assertions.assertEquals(6, getMethods.size());
  }

  @Test
  void getMethodsFromClass2() {
    ImmutableSet<Method> javaBeanGetMethods = ReflectionUtil.getMethodsFromClass(Sub.class, false,
        MethodNameStyle.JAVA_BEAN);
    log.debug("javaBeanGetMethods:{}", javaBeanGetMethods);
    Assertions.assertEquals(4, javaBeanGetMethods.size());
    ImmutableSet<Method> fluentGetMethods = ReflectionUtil.getMethodsFromClass(Sub.class, true,
        MethodNameStyle.FLUENT);
    log.debug("fluentGetMethods:{}", fluentGetMethods);
    Assertions.assertEquals(2, fluentGetMethods.size());
  }

  @Test
  void setMethodsFromClass() {
    ImmutableSet<Method> setMethods = ReflectionUtil.setMethodsFromClass(Sub.class);
    log.debug("setMethods:{}", setMethods);
    Assertions.assertEquals(5, setMethods.size());
  }

  @Test
  void setMethodsFromClass1() {
    ImmutableSet<Method> setMethods = ReflectionUtil.setMethodsFromClass(Sub.class, false);
    log.debug("setMethods:{}", setMethods);
    Assertions.assertEquals(7, setMethods.size());
  }

  @Test
  void setMethodsFromClass2() {
    ImmutableSet<Method> javaBeanSetMethods = ReflectionUtil
        .setMethodsFromClass(Sub.class, false, MethodNameStyle.JAVA_BEAN);
    log.debug("javaBeanSetMethods:{}", javaBeanSetMethods);
    Assertions.assertEquals(5, javaBeanSetMethods.size());
    ImmutableSet<Method> fluentBeanSetMethods = ReflectionUtil
        .setMethodsFromClass(Sub.class, false, MethodNameStyle.FLUENT);
    log.debug("fluentBeanSetMethods:{}", fluentBeanSetMethods);
    Assertions.assertEquals(7, fluentBeanSetMethods.size());
  }

  @Test
  void getMethodFromClass() {
    Optional<Method> numberField = ReflectionUtil.getMethodFromClass(Sub.class, "numberField");
    Assertions.assertTrue(numberField.isPresent());
    Optional<Method> test = ReflectionUtil.getMethodFromClass(Sub.class, "test");
    Assertions.assertFalse(test.isPresent());
  }

  @Test
  void getMethodFromClass1() {
    Optional<Method> readNumberField = ReflectionUtil
        .getMethodFromClass(Sub.class, "readNumberField", false);
    Assertions.assertTrue(readNumberField.isPresent());
    readNumberField = ReflectionUtil.getMethodFromClass(Sub.class, "readNumberField", true);
    Assertions.assertFalse(readNumberField.isPresent());
  }

  @Test
  void getMethodFromClass2() {
    Optional<Method> numberField = ReflectionUtil
        .getMethodFromClass(Sub.class, "numberField", false, MethodNameStyle.FLUENT);
    Assertions.assertTrue(numberField.isPresent());
  }

  @Test
  void setMethodFromClass() {
    Optional<Method> numberField = ReflectionUtil.setMethodFromClass(Sub.class, "numberField");
    Assertions.assertTrue(numberField.isPresent());
    Optional<Method> test = ReflectionUtil.setMethodFromClass(Sub.class, "test");
    Assertions.assertFalse(test.isPresent());
  }

  @Test
  void setMethodFromClass1() {
    Optional<Method> numberField = ReflectionUtil
        .setMethodFromClass(Sub.class, "numberField", false);
    Assertions.assertTrue(numberField.isPresent());
  }

  @Test
  void setMethodFromClass2() {
    Optional<Method> numberField = ReflectionUtil
        .setMethodFromClass(Sub.class, "numberField", false, MethodNameStyle.FLUENT);
    Assertions.assertTrue(numberField.isPresent());
  }

  @Test
  void readablePropertiesFromClass() {
    ImmutableSet<ReadableProperty<Sub, ?>> readableProperties = ReflectionUtil
        .readablePropertiesFromClass(Sub.class);
    log.debug("readableProperties:{}", readableProperties);
    Assertions.assertEquals(3, readableProperties.size());
  }

  @Test
  void readablePropertiesFromClass1() {
    ImmutableSet<ReadableProperty<Sub, ?>> readableProperties = ReflectionUtil
        .readablePropertiesFromClass(Sub.class, false);
    log.debug("readableProperties:{}", readableProperties);
    readableProperties.forEach(readableProperty -> System.out
        .println(readableProperty.name() + ":" + readableProperty.type()));
    Assertions.assertEquals(7, readableProperties.size());
  }

  @Test
  void readablePropertiesFromClass2() {
    ImmutableSet<ReadableProperty<Sub, ?>> readableProperties = ReflectionUtil
        .readablePropertiesFromClass(Sub.class, true, MethodNameStyle.FLUENT);
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
        .readablePropertyFromClass(Sub.class, "readNumberField", false);
    Assertions.assertTrue(readNumberField.isPresent());
  }

  @Test
  void readablePropertyFromClass2() {
    Optional<ReadableProperty<Sub, Object>> javaBeanReadNumberField = ReflectionUtil
        .readablePropertyFromClass(Sub.class, "readNumberField", false, MethodNameStyle.JAVA_BEAN);
    Assertions.assertTrue(javaBeanReadNumberField.isPresent());
    Optional<ReadableProperty<Sub, Object>> fluentReadNumberField = ReflectionUtil
        .readablePropertyFromClass(Sub.class, "readNumberField", false, MethodNameStyle.FLUENT);
    Assertions.assertFalse(fluentReadNumberField.isPresent());
  }

  @Test
  void writablePropertiesFromClass() {
    ImmutableSet<WritableProperty<Sub, ?>> writableProperties = ReflectionUtil
        .writablePropertiesFromClass(Sub.class);
    log.debug("writableProperties:{}", writableProperties);
    Assertions.assertEquals(3, writableProperties.size());
  }

  @Test
  void writablePropertiesFromClass1() {
    ImmutableSet<WritableProperty<Sub, ?>> writableProperties = ReflectionUtil
        .writablePropertiesFromClass(Sub.class, false);
    log.debug("writableProperties:{}", writableProperties);
    Assertions.assertEquals(7, writableProperties.size());
  }

  @Test
  void writablePropertiesFromClass2() {
    ImmutableSet<WritableProperty<Sub, ?>> writableProperties = ReflectionUtil
        .writablePropertiesFromClass(Sub.class, true, MethodNameStyle.FLUENT);
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
        .writablePropertyFromClass(Sub.class, "stringField", false);
    Assertions.assertTrue(stringField.isPresent());
  }

  @Test
  void writablePropertyFromClass2() {
    Optional<WritableProperty<Sub, Object>> stringField = ReflectionUtil
        .writablePropertyFromClass(Sub.class, "stringField", false, MethodNameStyle.FLUENT);
    Assertions.assertTrue(stringField.isPresent());
  }

  @Test
  @SneakyThrows
  void isGetMethod() {
    boolean isGetMethod = ReflectionUtil
        .isGetMethod(Sub.class.getDeclaredMethod("stringField"), MethodNameStyle.FLUENT);
    Assertions.assertTrue(isGetMethod);
  }

  @Test
  @SneakyThrows
  void isGetInvokable() {
    boolean isGetInvokable = ReflectionUtil
        .isGetInvokable(Invokable.from(Sub.class.getDeclaredMethod("stringField")),
            MethodNameStyle.FLUENT);
    Assertions.assertTrue(isGetInvokable);
  }

  @Test
  @SneakyThrows
  void isSetMethod() {
    boolean isSetMethod = ReflectionUtil
        .isSetMethod(Sub.class.getDeclaredMethod("stringField", String.class),
            MethodNameStyle.FLUENT);
    Assertions.assertTrue(isSetMethod);
  }

  @Test
  @SneakyThrows
  void isSetInvokable() {
    boolean isSetInvokable = ReflectionUtil
        .isSetInvokable(Invokable.from(Sub.class.getDeclaredMethod("stringField", String.class)),
            MethodNameStyle.FLUENT);
    Assertions.assertTrue(isSetInvokable);
  }
}