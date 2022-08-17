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
import com.google.common.reflect.TypeToken;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.reflect.property.ReadableProperty;
import org.caotc.unit4j.core.common.reflect.property.WritableProperty;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyAccessorMethodFormat;
import org.caotc.unit4j.core.common.util.model.PropertyConstant;
import org.caotc.unit4j.core.common.util.model.StringSetter;
import org.caotc.unit4j.core.common.util.model.Sub;
import org.caotc.unit4j.core.common.util.model.Super;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

@Slf4j
class ReflectionUtilTest {

    @Test
    void fieldsFromType() {
        Assertions.assertEquals(PropertyConstant.SUPER_FIELDS, ReflectionUtil.fields((Type) Super.class));
        Assertions.assertEquals(PropertyConstant.SUB_FIELDS, ReflectionUtil.fields((Type) Sub.class));
    }

    @Test
    void fieldsFromClass() {
        Assertions.assertEquals(PropertyConstant.SUPER_FIELDS, ReflectionUtil.fields(Super.class));
        Assertions.assertEquals(PropertyConstant.SUB_FIELDS, ReflectionUtil.fields(Sub.class));
    }

    @Test
    void fieldsFromTypeToken() {
        Assertions.assertEquals(PropertyConstant.SUPER_FIELDS, ReflectionUtil.fields(TypeToken.of(Super.class)));
        Assertions.assertEquals(PropertyConstant.SUB_FIELDS, ReflectionUtil.fields(Sub.class));
    }

    @Test
    void fieldStreamFromType() {
        Assertions.assertEquals(PropertyConstant.SUPER_FIELDS, ReflectionUtil.fieldStream((Type) Super.class).collect(ImmutableSet.toImmutableSet()));
        Assertions.assertEquals(PropertyConstant.SUB_FIELDS, ReflectionUtil.fieldStream((Type) Sub.class).collect(ImmutableSet.toImmutableSet()));
    }

    @Test
    void fieldStreamFromClass() {
        Assertions.assertEquals(PropertyConstant.SUPER_FIELDS, ReflectionUtil.fieldStream(Super.class).collect(ImmutableSet.toImmutableSet()));
        Assertions.assertEquals(PropertyConstant.SUB_FIELDS, ReflectionUtil.fieldStream(Sub.class).collect(ImmutableSet.toImmutableSet()));
    }

    @Test
    void fieldStreamFromTypeToken() {
        Assertions.assertEquals(PropertyConstant.SUPER_FIELDS, ReflectionUtil.fieldStream(TypeToken.of(Super.class)).collect(ImmutableSet.toImmutableSet()));
        Assertions.assertEquals(PropertyConstant.SUB_FIELDS, ReflectionUtil.fieldStream(Sub.class).collect(ImmutableSet.toImmutableSet()));
    }

    @Test
    void fieldsByNameFromType() {
        Assertions.assertEquals(PropertyConstant.SUB_STRING_FIELDS, ReflectionUtil.fields((Type) Sub.class, "stringField"));
        Assertions.assertEquals(ImmutableSet.of(PropertyConstant.SUB_NUMBER_FIELD), ReflectionUtil.fields((Type) Sub.class, "numberField"));
    }

    @Test
    void fieldsByNameFromClass() {
        Assertions.assertEquals(PropertyConstant.SUB_STRING_FIELDS, ReflectionUtil.fields(Sub.class, "stringField"));
        Assertions.assertEquals(ImmutableSet.of(PropertyConstant.SUB_NUMBER_FIELD), ReflectionUtil.fields(Sub.class, "numberField"));
    }

    @Test
    void fieldsByNameFromTypeToken() {
        Assertions.assertEquals(PropertyConstant.SUB_STRING_FIELDS, ReflectionUtil.fields(TypeToken.of(Sub.class), "stringField"));
        Assertions.assertEquals(ImmutableSet.of(PropertyConstant.SUB_NUMBER_FIELD), ReflectionUtil.fields(TypeToken.of(Sub.class), "numberField"));
    }

    @Test
    void methodsFromType() {
        Assertions.assertEquals(PropertyConstant.SUPER_METHODS, ReflectionUtil.methods((Type) Super.class));
        Assertions.assertEquals(PropertyConstant.SUB_METHODS, ReflectionUtil.methods((Type) Sub.class));
    }

    @Test
    void methodsFromClass() {
        Assertions.assertEquals(PropertyConstant.SUPER_METHODS, ReflectionUtil.methods(Super.class));
        Assertions.assertEquals(PropertyConstant.SUB_METHODS, ReflectionUtil.methods(Sub.class));
    }

    @Test
    void methodsFromTypeToken() {
        Assertions.assertEquals(PropertyConstant.SUPER_METHODS, ReflectionUtil.methods(TypeToken.of(Super.class)));
        Assertions.assertEquals(PropertyConstant.SUB_METHODS, ReflectionUtil.methods(TypeToken.of(Sub.class)));
    }

    @Test
    void methodStreamFromType() {
        Assertions.assertEquals(PropertyConstant.SUPER_METHODS, ReflectionUtil.methodStream((Type) Super.class).collect(ImmutableSet.toImmutableSet()));
        Assertions.assertEquals(PropertyConstant.SUB_METHODS, ReflectionUtil.methodStream((Type) Sub.class).collect(ImmutableSet.toImmutableSet()));
    }

    @Test
    void methodStreamFromClass() {
        Assertions.assertEquals(PropertyConstant.SUPER_METHODS, ReflectionUtil.methodStream(Super.class).collect(ImmutableSet.toImmutableSet()));
        Assertions.assertEquals(PropertyConstant.SUB_METHODS, ReflectionUtil.methodStream(Sub.class).collect(ImmutableSet.toImmutableSet()));
    }

    @Test
    void methodStreamFromTypeToken() {
        Assertions.assertEquals(PropertyConstant.SUPER_METHODS, ReflectionUtil.methodStream(TypeToken.of(Super.class)).collect(ImmutableSet.toImmutableSet()));
        Assertions.assertEquals(PropertyConstant.SUB_METHODS, ReflectionUtil.methodStream(TypeToken.of(Sub.class)).collect(ImmutableSet.toImmutableSet()));
    }

    @Test
    void constructorsFromType() {
        Assertions.assertEquals(PropertyConstant.SUPER_CONSTRUCTORS, ReflectionUtil.constructors((Type) Super.class));
        Assertions.assertEquals(PropertyConstant.SUB_CONSTRUCTORS, ReflectionUtil.constructors((Type) Sub.class));
    }

    @Test
    void constructorsFromClass() {
        Assertions.assertEquals(PropertyConstant.SUPER_CONSTRUCTORS, ReflectionUtil.constructors(Super.class));
        Assertions.assertEquals(PropertyConstant.SUB_CONSTRUCTORS, ReflectionUtil.constructors(Sub.class));
    }

    @Test
    void constructorsFromTypeToken() {
        Assertions.assertEquals(PropertyConstant.SUPER_CONSTRUCTORS, ReflectionUtil.constructors(TypeToken.of(Super.class)));
        Assertions.assertEquals(PropertyConstant.SUB_CONSTRUCTORS, ReflectionUtil.constructors(TypeToken.of(Sub.class)));
    }

    @Test
    void constructorStreamFromType() {
        Assertions.assertEquals(PropertyConstant.SUPER_CONSTRUCTORS, ReflectionUtil.constructorStream((Type) Super.class).collect(ImmutableSet.toImmutableSet()));
        Assertions.assertEquals(PropertyConstant.SUB_CONSTRUCTORS, ReflectionUtil.constructorStream((Type) Sub.class).collect(ImmutableSet.toImmutableSet()));
    }

    @Test
    void constructorStreamFromClass() {
        Assertions.assertEquals(PropertyConstant.SUPER_CONSTRUCTORS, ReflectionUtil.constructorStream(Super.class).collect(ImmutableSet.toImmutableSet()));
        Assertions.assertEquals(PropertyConstant.SUB_CONSTRUCTORS, ReflectionUtil.constructorStream(Sub.class).collect(ImmutableSet.toImmutableSet()));
    }

    @Test
    void constructorStreamFromTypeToken() {
        Assertions.assertEquals(PropertyConstant.SUPER_CONSTRUCTORS, ReflectionUtil.constructorStream(TypeToken.of(Super.class)).collect(ImmutableSet.toImmutableSet()));
        Assertions.assertEquals(PropertyConstant.SUB_CONSTRUCTORS, ReflectionUtil.constructorStream(TypeToken.of(Sub.class)).collect(ImmutableSet.toImmutableSet()));
    }

    @Test
    void getMethodsFromType() {
        Assertions.assertEquals(PropertyConstant.SUPER_GET_METHODS, ReflectionUtil.getMethods((Type) Super.class));
        Assertions.assertEquals(PropertyConstant.SUB_GET_METHODS, ReflectionUtil.getMethods((Type) Sub.class));
    }

    @Test
    void getMethodsFromClass() {
        Sub sub = new Sub();
        ReflectionUtil.getMethodStream(Sub.class).filter(method -> method.getName().contains("getStringField"))
                .forEach(method -> {
                    try {
                        log.debug("method:{},result:{}", method, method.invoke(sub));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
        Assertions.assertEquals(PropertyConstant.SUPER_GET_METHODS, ReflectionUtil.getMethods(Super.class));
        Assertions.assertEquals(PropertyConstant.SUB_GET_METHODS, ReflectionUtil.getMethods(Sub.class));
    }

    @Test
    void getMethodsFromTypeToken() {
        Assertions.assertEquals(PropertyConstant.SUPER_GET_METHODS, ReflectionUtil.getMethods(TypeToken.of(Super.class)));
        Assertions.assertEquals(PropertyConstant.SUB_GET_METHODS, ReflectionUtil.getMethods(TypeToken.of(Sub.class)));
    }

    @Test
    void getMethodStreamFromType() {
        Assertions.assertEquals(PropertyConstant.SUPER_GET_METHODS, ReflectionUtil.getMethodStream((Type) Super.class).collect(ImmutableSet.toImmutableSet()));
        Assertions.assertEquals(PropertyConstant.SUB_GET_METHODS, ReflectionUtil.getMethodStream((Type) Sub.class).collect(ImmutableSet.toImmutableSet()));
    }

    @Test
    void getMethodStreamFromClass() {
        Assertions.assertEquals(PropertyConstant.SUPER_GET_METHODS, ReflectionUtil.getMethodStream(Super.class).collect(ImmutableSet.toImmutableSet()));
        Assertions.assertEquals(PropertyConstant.SUB_GET_METHODS, ReflectionUtil.getMethodStream(Sub.class).collect(ImmutableSet.toImmutableSet()));
    }

    @Test
    void getMethodStreamFromTypeToken() {
        Assertions.assertEquals(PropertyConstant.SUPER_GET_METHODS, ReflectionUtil.getMethodStream(TypeToken.of(Super.class)).collect(ImmutableSet.toImmutableSet()));
        Assertions.assertEquals(PropertyConstant.SUB_GET_METHODS, ReflectionUtil.getMethodStream(TypeToken.of(Sub.class)).collect(ImmutableSet.toImmutableSet()));
    }

    @Test
    void setMethodsFromType() {
        Assertions.assertEquals(PropertyConstant.SUPER_SET_METHODS, ReflectionUtil.setMethods((Type) Super.class));
        Assertions.assertEquals(PropertyConstant.SUB_SET_METHODS, ReflectionUtil.setMethods((Type) Sub.class));
    }

    @Test
    void setMethodsFromClass() {
        Assertions.assertEquals(PropertyConstant.SUPER_SET_METHODS, ReflectionUtil.setMethods(Super.class));
        Assertions.assertEquals(PropertyConstant.SUB_SET_METHODS, ReflectionUtil.setMethods(Sub.class));
    }

    @Test
    void setMethodsFromTypeToken() {
        Assertions.assertEquals(PropertyConstant.SUPER_SET_METHODS, ReflectionUtil.setMethods(TypeToken.of(Super.class)));
        Assertions.assertEquals(PropertyConstant.SUB_SET_METHODS, ReflectionUtil.setMethods(TypeToken.of(Sub.class)));
    }

    @Test
    void setMethodStreamFromType() {
        Assertions.assertEquals(PropertyConstant.SUPER_SET_METHODS, ReflectionUtil.setMethodStream((Type) Super.class).collect(ImmutableSet.toImmutableSet()));
        Assertions.assertEquals(PropertyConstant.SUB_SET_METHODS, ReflectionUtil.setMethodStream((Type) Sub.class).collect(ImmutableSet.toImmutableSet()));
    }

    @Test
    void setMethodStreamFromClass() {
        Assertions.assertEquals(PropertyConstant.SUPER_SET_METHODS, ReflectionUtil.setMethodStream(Super.class).collect(ImmutableSet.toImmutableSet()));
        Assertions.assertEquals(PropertyConstant.SUB_SET_METHODS, ReflectionUtil.setMethodStream(Sub.class).collect(ImmutableSet.toImmutableSet()));
    }

    @Test
    void setMethodStreamFromTypeToken() {
        Assertions.assertEquals(PropertyConstant.SUPER_SET_METHODS, ReflectionUtil.setMethodStream(TypeToken.of(Super.class)).collect(ImmutableSet.toImmutableSet()));
        Assertions.assertEquals(PropertyConstant.SUB_SET_METHODS, ReflectionUtil.setMethodStream(TypeToken.of(Sub.class)).collect(ImmutableSet.toImmutableSet()));
    }

    @Test
    void getMethodExactFromType() {
        Assertions.assertEquals(PropertyConstant.SUPER_STRING_FIELD_GET_METHOD, ReflectionUtil.getMethodExact((Type) Super.class, "stringField"));
        Assertions.assertEquals(PropertyConstant.SUPER_INT_FIELD_GET_METHOD, ReflectionUtil.getMethodExact((Type) Super.class, "intField"));
        Assertions.assertEquals(PropertyConstant.SUB_STRING_FIELD_GET_METHOD, ReflectionUtil.getMethodExact((Type) Sub.class, "stringField"));
        Assertions.assertEquals(PropertyConstant.SUB_NUMBER_FIELD_GET_METHOD, ReflectionUtil.getMethodExact((Type) Sub.class, "numberField"));
        Assertions.assertEquals(PropertyConstant.SUB_READ_NUMBER_FIELD_GET_METHOD, ReflectionUtil.getMethodExact((Type) Sub.class, "readNumberField"));
        Assertions.assertEquals(PropertyConstant.SUB_INT_FIELD_GET_METHOD, ReflectionUtil.getMethodExact((Type) Sub.class, "intField"));
    }

    @Test
    void getMethodExactFromClass() {
        Assertions.assertEquals(PropertyConstant.SUPER_STRING_FIELD_GET_METHOD, ReflectionUtil.getMethodExact(Super.class, "stringField"));
        Assertions.assertEquals(PropertyConstant.SUPER_INT_FIELD_GET_METHOD, ReflectionUtil.getMethodExact(Super.class, "intField"));
        Assertions.assertEquals(PropertyConstant.SUB_STRING_FIELD_GET_METHOD, ReflectionUtil.getMethodExact(Sub.class, "stringField"));
        Assertions.assertEquals(PropertyConstant.SUB_NUMBER_FIELD_GET_METHOD, ReflectionUtil.getMethodExact(Sub.class, "numberField"));
        Assertions.assertEquals(PropertyConstant.SUB_READ_NUMBER_FIELD_GET_METHOD, ReflectionUtil.getMethodExact(Sub.class, "readNumberField"));
        Assertions.assertEquals(PropertyConstant.SUB_INT_FIELD_GET_METHOD, ReflectionUtil.getMethodExact(Sub.class, "intField"));
    }

    @Test
    void getMethodExactFromTypeToken() {
        Assertions.assertEquals(PropertyConstant.SUPER_STRING_FIELD_GET_METHOD, ReflectionUtil.getMethodExact(TypeToken.of(Super.class), "stringField"));
        Assertions.assertEquals(PropertyConstant.SUPER_INT_FIELD_GET_METHOD, ReflectionUtil.getMethodExact(TypeToken.of(Super.class), "intField"));
        Assertions.assertEquals(PropertyConstant.SUB_STRING_FIELD_GET_METHOD, ReflectionUtil.getMethodExact(TypeToken.of(Sub.class), "stringField"));
        Assertions.assertEquals(PropertyConstant.SUB_NUMBER_FIELD_GET_METHOD, ReflectionUtil.getMethodExact(TypeToken.of(Sub.class), "numberField"));
        Assertions.assertEquals(PropertyConstant.SUB_READ_NUMBER_FIELD_GET_METHOD, ReflectionUtil.getMethodExact(TypeToken.of(Sub.class), "readNumberField"));
        Assertions.assertEquals(PropertyConstant.SUB_INT_FIELD_GET_METHOD, ReflectionUtil.getMethodExact(TypeToken.of(Sub.class), "intField"));
    }

    @Test
    void getMethodFromType() {
        Optional<Method> superStringField = ReflectionUtil.getMethod((Type) Super.class, "stringField");
        Assertions.assertTrue(superStringField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUPER_STRING_FIELD_GET_METHOD, superStringField.get());

        Optional<Method> superIntField = ReflectionUtil.getMethod((Type) Super.class, "intField");
        Assertions.assertTrue(superIntField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUPER_INT_FIELD_GET_METHOD, superIntField.get());

        Optional<Method> subStringField = ReflectionUtil.getMethod((Type) Sub.class, "stringField");
        Assertions.assertTrue(subStringField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUB_STRING_FIELD_GET_METHOD, subStringField.get());

        Optional<Method> subNumberField = ReflectionUtil.getMethod((Type) Sub.class, "numberField");
        Assertions.assertTrue(subNumberField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUB_NUMBER_FIELD_GET_METHOD, subNumberField.get());

        Optional<Method> subReadNumberField = ReflectionUtil.getMethod((Type) Sub.class, "readNumberField");
        Assertions.assertTrue(subReadNumberField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUB_READ_NUMBER_FIELD_GET_METHOD, subReadNumberField.get());

        Optional<Method> subIntField = ReflectionUtil.getMethod((Type) Sub.class, "intField");
        Assertions.assertTrue(subIntField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUB_INT_FIELD_GET_METHOD, subIntField.get());

        Optional<Method> test = ReflectionUtil.getMethod((Type) Sub.class, "test");
        Assertions.assertFalse(test.isPresent());
    }

    @Test
    void getMethodFromClass() {
        Optional<Method> superStringField = ReflectionUtil.getMethod(Super.class, "stringField");
        Assertions.assertTrue(superStringField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUPER_STRING_FIELD_GET_METHOD, superStringField.get());

        Optional<Method> superIntField = ReflectionUtil.getMethod(Super.class, "intField");
        Assertions.assertTrue(superIntField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUPER_INT_FIELD_GET_METHOD, superIntField.get());

        Optional<Method> subStringField = ReflectionUtil.getMethod(Sub.class, "stringField");
        Assertions.assertTrue(subStringField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUB_STRING_FIELD_GET_METHOD, subStringField.get());

        Optional<Method> subNumberField = ReflectionUtil.getMethod(Sub.class, "numberField");
        Assertions.assertTrue(subNumberField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUB_NUMBER_FIELD_GET_METHOD, subNumberField.get());

        Optional<Method> subReadNumberField = ReflectionUtil.getMethod(Sub.class, "readNumberField");
        Assertions.assertTrue(subReadNumberField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUB_READ_NUMBER_FIELD_GET_METHOD, subReadNumberField.get());

        Optional<Method> subIntField = ReflectionUtil.getMethod(Sub.class, "intField");
        Assertions.assertTrue(subIntField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUB_INT_FIELD_GET_METHOD, subIntField.get());

        Optional<Method> test = ReflectionUtil.getMethod(Sub.class, "test");
        Assertions.assertFalse(test.isPresent());
    }

    @Test
    void getMethodFromTypeToken() {
        Optional<Method> superStringField = ReflectionUtil.getMethod(TypeToken.of(Super.class), "stringField");
        Assertions.assertTrue(superStringField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUPER_STRING_FIELD_GET_METHOD, superStringField.get());

        Optional<Method> superIntField = ReflectionUtil.getMethod(TypeToken.of(Super.class), "intField");
        Assertions.assertTrue(superIntField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUPER_INT_FIELD_GET_METHOD, superIntField.get());

        Optional<Method> subStringField = ReflectionUtil.getMethod(TypeToken.of(Sub.class), "stringField");
        Assertions.assertTrue(subStringField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUB_STRING_FIELD_GET_METHOD, subStringField.get());

        Optional<Method> subNumberField = ReflectionUtil.getMethod(TypeToken.of(Sub.class), "numberField");
        Assertions.assertTrue(subNumberField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUB_NUMBER_FIELD_GET_METHOD, subNumberField.get());

        Optional<Method> subReadNumberField = ReflectionUtil.getMethod(TypeToken.of(Sub.class), "readNumberField");
        Assertions.assertTrue(subReadNumberField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUB_READ_NUMBER_FIELD_GET_METHOD, subReadNumberField.get());

        Optional<Method> subIntField = ReflectionUtil.getMethod(TypeToken.of(Sub.class), "intField");
        Assertions.assertTrue(subIntField.isPresent());
        Assertions.assertEquals(PropertyConstant.SUB_INT_FIELD_GET_METHOD, subIntField.get());

        Optional<Method> test = ReflectionUtil.getMethod(TypeToken.of(Sub.class), "test");
        Assertions.assertFalse(test.isPresent());
    }

    @Test
    void setMethodExactFromType() {
        Assertions.assertEquals(PropertyConstant.SUPER_STRING_FIELD_SET_METHOD, ReflectionUtil.setMethodExact((Type) Super.class, "stringField"));
        Assertions.assertEquals(PropertyConstant.SUPER_INT_FIELD_SET_METHOD, ReflectionUtil.setMethodExact((Type) Super.class, "intField"));
        Assertions.assertEquals(PropertyConstant.SUB_STRING_FIELD_SET_METHOD, ReflectionUtil.setMethodExact((Type) Sub.class, "stringField"));
        Assertions.assertEquals(PropertyConstant.SUB_INT_FIELD_SET_METHOD, ReflectionUtil.setMethodExact((Type) Sub.class, "intField"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ReflectionUtil.setMethodExact((Type) Sub.class, "numberField"));
    }

    @Test
    void setMethodExactFromClass() {
        Assertions.assertEquals(PropertyConstant.SUPER_STRING_FIELD_GET_METHOD, ReflectionUtil.setMethodExact(Super.class, "stringField"));
        Assertions.assertEquals(PropertyConstant.SUPER_INT_FIELD_GET_METHOD, ReflectionUtil.setMethodExact(Super.class, "intField"));
        Assertions.assertEquals(PropertyConstant.SUB_STRING_FIELD_GET_METHOD, ReflectionUtil.setMethodExact(Sub.class, "stringField"));
        Assertions.assertEquals(PropertyConstant.SUB_NUMBER_FIELD_GET_METHOD, ReflectionUtil.setMethodExact(Sub.class, "numberField"));
        Assertions.assertEquals(PropertyConstant.SUB_READ_NUMBER_FIELD_GET_METHOD, ReflectionUtil.setMethodExact(Sub.class, "readNumberField"));
        Assertions.assertEquals(PropertyConstant.SUB_INT_FIELD_GET_METHOD, ReflectionUtil.setMethodExact(Sub.class, "intField"));
    }

    @Test
    void setMethodExactFromTypeToken() {
        Assertions.assertEquals(PropertyConstant.SUPER_STRING_FIELD_GET_METHOD, ReflectionUtil.setMethodExact(TypeToken.of(Super.class), "stringField"));
        Assertions.assertEquals(PropertyConstant.SUPER_INT_FIELD_GET_METHOD, ReflectionUtil.setMethodExact(TypeToken.of(Super.class), "intField"));
        Assertions.assertEquals(PropertyConstant.SUB_STRING_FIELD_GET_METHOD, ReflectionUtil.setMethodExact(TypeToken.of(Sub.class), "stringField"));
        Assertions.assertEquals(PropertyConstant.SUB_NUMBER_FIELD_GET_METHOD, ReflectionUtil.setMethodExact(TypeToken.of(Sub.class), "numberField"));
        Assertions.assertEquals(PropertyConstant.SUB_READ_NUMBER_FIELD_GET_METHOD, ReflectionUtil.setMethodExact(TypeToken.of(Sub.class), "readNumberField"));
        Assertions.assertEquals(PropertyConstant.SUB_INT_FIELD_GET_METHOD, ReflectionUtil.setMethodExact(TypeToken.of(Sub.class), "intField"));
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

    @Test
    @SneakyThrows
    void isOverrideTest() {
        Method interfaceMethod = StringSetter.class.getDeclaredMethod("setStringField", String.class);
        log.debug("SUPER_STRING_FIELD_SET_METHOD isOverride interfaceMethod:{}", ReflectionUtil.isOverride(PropertyConstant.SUPER_STRING_FIELD_SET_METHOD, interfaceMethod));
        log.debug("SUPER_STRING_FIELD_SET_METHOD isOverride SUB_STRING_FIELD_SET_METHOD:{}", ReflectionUtil.isOverride(PropertyConstant.SUPER_STRING_FIELD_SET_METHOD, PropertyConstant.SUB_STRING_FIELD_SET_METHOD));
        log.debug("SUB_STRING_FIELD_SET_METHOD isOverride interfaceMethod:{}", ReflectionUtil.isOverride(PropertyConstant.SUB_STRING_FIELD_SET_METHOD, interfaceMethod));
        log.debug("SUB_STRING_FIELD_SET_METHOD isOverride SUPER_STRING_FIELD_SET_METHOD:{}", ReflectionUtil.isOverride(PropertyConstant.SUB_STRING_FIELD_SET_METHOD, PropertyConstant.SUPER_STRING_FIELD_SET_METHOD));
        log.debug("interfaceMethod isOverride SUPER_STRING_FIELD_SET_METHOD:{}", ReflectionUtil.isOverride(interfaceMethod, PropertyConstant.SUPER_STRING_FIELD_SET_METHOD));
        log.debug("interfaceMethod isOverride SUB_STRING_FIELD_SET_METHOD:{}", ReflectionUtil.isOverride(interfaceMethod, PropertyConstant.SUB_STRING_FIELD_SET_METHOD));
    }
}