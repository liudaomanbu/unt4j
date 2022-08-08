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
import com.google.common.reflect.Invokable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.reflect.property.ReadableProperty;
import org.caotc.unit4j.core.common.reflect.property.WritableProperty;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyAccessorMethodFormat;
import org.caotc.unit4j.core.common.util.model.Sub;
import org.caotc.unit4j.core.common.util.model.Super;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

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
    ImmutableSet<Method> expected = ImmutableSet.<Method>builder()
        .add(Sub.class.getDeclaredMethods())
        .add(Super.class.getDeclaredMethods())
        .add(Object.class.getDeclaredMethods())
        .build();
    ImmutableSet<Method> methods = ReflectionUtil.methodsFromClass(Sub.class);
    log.debug("methods:{}", methods);
    Assertions.assertEquals(expected, methods);
  }

  @Test
  void getMethodsFromClass() throws NoSuchMethodException {
    ImmutableSet<Method> expected = ImmutableSet.of(Sub.class.getMethod("getStringField")
        , Sub.class.getMethod("getNumberField"), Sub.class.getMethod("getReadNumberField")
        , Sub.class.getMethod("getIntField"));
    ImmutableSet<Method> getMethods = ReflectionUtil.getMethodsFromClass(Sub.class);
    log.debug("getMethods:{}", getMethods);
    Assertions.assertEquals(expected, getMethods);
  }

  @Test
  void getMethodsFromClass1() throws NoSuchMethodException {
    ImmutableSet<Method> expected = ImmutableSet.of(Sub.class.getMethod("getStringField")
        , Sub.class.getMethod("getNumberField"), Sub.class.getMethod("getReadNumberField")
        , Sub.class.getMethod("getIntField"));
    ImmutableSet<Method> getMethods = ReflectionUtil.getMethodsFromClass(Sub.class);
    log.debug("getMethods:{}", getMethods);
    Assertions.assertEquals(expected, getMethods);
  }

  @Test
  void getMethodsFromClass2() throws NoSuchMethodException {
    ImmutableSet<Method> expectedGetMethods = ImmutableSet.of(Sub.class.getMethod("getStringField")
        , Sub.class.getMethod("getNumberField"), Sub.class.getMethod("getReadNumberField")
        , Sub.class.getMethod("getIntField"));
    ImmutableSet<Method> javaBeanGetMethods = ReflectionUtil.getMethodsFromClass(Sub.class);
    log.debug("javaBeanGetMethods:{}", javaBeanGetMethods);
    Assertions.assertEquals(expectedGetMethods, javaBeanGetMethods);
  }

  @Test
  void setMethodsFromClass() {
    ImmutableSet<Method> setMethods = ReflectionUtil.setMethodsFromClass(Sub.class);
    log.debug("setMethods:{}", setMethods);
    Assertions.assertEquals(5, setMethods.size());
  }

  @Test
  void setMethodsFromClass1() throws NoSuchMethodException {
    ImmutableSet<Method> expectedSetMethods = ImmutableSet
        .of(Sub.class.getMethod("setNumberField", Number.class)
            , Sub.class.getMethod("setNumberField", int.class),
            Sub.class.getMethod("setNumberField", long.class)
            , Sub.class.getMethod("setStringField", String.class),
            Sub.class.getMethod("setIntField", int.class));
    ImmutableSet<Method> setMethods = ReflectionUtil.setMethodsFromClass(Sub.class);
    log.debug("setMethods:{}", setMethods);
    Assertions.assertEquals(expectedSetMethods, setMethods);
  }

  @Test
  void setMethodsFromClass2() {
    ImmutableSet<Method> javaBeanSetMethods = ReflectionUtil
        .setMethodsFromClass(Sub.class);
    log.debug("javaBeanSetMethods:{}", javaBeanSetMethods);
    Assertions.assertEquals(5, javaBeanSetMethods.size());
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
        .getMethodFromClass(Sub.class, "readNumberField");
    Assertions.assertTrue(readNumberField.isPresent());
  }

  @Test
  void getMethodFromClass2() {
    Optional<Method> numberField = ReflectionUtil
        .getMethodFromClass(Sub.class, "numberField");
    Assertions.assertTrue(numberField.isPresent());
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
}