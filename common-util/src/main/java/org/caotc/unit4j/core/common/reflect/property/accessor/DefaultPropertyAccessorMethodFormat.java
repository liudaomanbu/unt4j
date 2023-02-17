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

package org.caotc.unit4j.core.common.reflect.property.accessor;

import lombok.NonNull;
import org.caotc.unit4j.core.common.base.CaseFormat;
import org.caotc.unit4j.core.common.reflect.Invokable;
import org.caotc.unit4j.core.common.reflect.MethodInvokable;
import org.caotc.unit4j.core.common.util.ReflectionUtil;
import org.caotc.unit4j.core.constant.StringConstant;

import java.lang.reflect.Field;

/**
 * get/set方法的命名风格枚举
 *
 * @author caotc
 * @date 2019-05-29
 * @since 1.0.0
 */
public enum DefaultPropertyAccessorMethodFormat implements PropertyAccessorMethodFormat {
    /**
     * 默认的javaBean规范风格
     */
    JAVA_BEAN {
        private static final String GET_METHOD_PREFIX = "get";
        private static final String IS_METHOD_PREFIX = "is";
        private static final String SET_METHOD_PREFIX = "set";

        @Override
        public boolean propertyReaderNameMatches(@NonNull Invokable<?, ?> invokable) {
            return invokable.getName().startsWith(GET_METHOD_PREFIX)
                    || (boolean.class.equals(invokable.returnType().getRawType()) && invokable.getName()
                    .startsWith(IS_METHOD_PREFIX));
        }

        @Override
        protected boolean propertyWriterNameMatches(@NonNull Invokable<?, ?> invokable) {
            return invokable.getName().startsWith(SET_METHOD_PREFIX);
        }

        @NonNull
        @Override
        public String propertyReaderMethodNameFromField(@NonNull Field field) {
            String methodFieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, field.getName());
            String prefix =
                    boolean.class.equals(field.getType()) ? IS_METHOD_PREFIX : GET_METHOD_PREFIX;
            return StringConstant.EMPTY_JOINER.join(prefix, methodFieldName);
        }

        @Override
        public @NonNull String propertyNameFromPropertyReader(@NonNull Invokable<?, ?> getInvokable) {
            String prefix = getInvokable.getName().startsWith(GET_METHOD_PREFIX) ? GET_METHOD_PREFIX
                    : IS_METHOD_PREFIX;
            String methodFieldName = getInvokable.getName().replaceFirst(prefix, StringConstant.EMPTY);
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, methodFieldName);
        }

        @Override
        public @NonNull String PropertyWriterNameFromField(@NonNull Field field) {
            String methodFieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, field.getName());
            return StringConstant.EMPTY_JOINER.join(SET_METHOD_PREFIX, methodFieldName);
        }

        @Override
        public @NonNull String propertyNameFromPropertyWriter(@NonNull Invokable<?, ?> setInvokable) {
            String methodFieldName = setInvokable.getName()
                    .replaceFirst(SET_METHOD_PREFIX, StringConstant.EMPTY);
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, methodFieldName);
        }

    },
    /**
     * 流式风格,get/set方法名和属性名称一样
     */
    FLUENT {
        @Override
        public boolean propertyReaderNameMatches(@NonNull Invokable<?, ?> invokable) {
            return !JAVA_BEAN.propertyReaderNameMatches(invokable) && !JAVA_BEAN
                    .propertyWriterNameMatches(invokable);
        }

        @Override
        protected boolean propertyWriterNameMatches(@NonNull Invokable<?, ?> invokable) {
            return !JAVA_BEAN.propertyReaderNameMatches(invokable) && !JAVA_BEAN
                    .propertyWriterNameMatches(invokable);
        }

        @NonNull
        @Override
        public String propertyReaderMethodNameFromField(@NonNull Field field) {
            return field.getName();
        }

        @Override
        public @NonNull String propertyNameFromPropertyReader(@NonNull Invokable<?, ?> getInvokable) {
            return getInvokable.getName();
        }

        @Override
        public @NonNull String PropertyWriterNameFromField(@NonNull Field field) {
            return field.getName();
        }

        @Override
        public @NonNull String propertyNameFromPropertyWriter(@NonNull Invokable<?, ?> setInvokable) {
            return setInvokable.getName();
        }
    };

    /**
     * 检查传入方法是否是属于该命名风格的get方法
     *
     * @param invokable 需要检查的方法
     * @return 是否是属于该命名风格的get方法
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    public boolean isPropertyReader(@NonNull MethodInvokable<?, ?> invokable) {
        return !invokable.getDeclaringClass().equals(Object.class)
                && !invokable.isStatic()
                && invokable.parameters().isEmpty()
                && !invokable.returnType().getRawType().equals(void.class)
                && propertyReaderNameMatches(invokable)
                && !ReflectionUtil.isOverride(invokable, Object.class);
    }

    /**
     * 检查传入方法是否是属于该命名风格的set方法
     *
     * @param invokable 需要检查的方法
     * @return 是否是属于该命名风格的set方法
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    public boolean isPropertyWriter(@NonNull MethodInvokable<?, ?> invokable) {
        return !invokable.getDeclaringClass().equals(Object.class)
                && (invokable.returnType().getRawType().equals(void.class) || invokable.returnType()
                .equals(invokable.ownerType()))
                && invokable.parameters().size() == 1
                && propertyWriterNameMatches(invokable)
                && !ReflectionUtil.isOverride(invokable, Object.class);
    }

    /**
     * 检查传入方法是否在方法名上属于该命名风格的get方法
     *
     * @param invokable 需要检查的方法
     * @return 是否在方法名上属于该命名风格的get方法
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    protected abstract boolean propertyReaderNameMatches(@NonNull Invokable<?, ?> invokable);

    /**
     * 检查传入方法是否在方法名上属于该命名风格的set方法
     *
     * @param invokable 需要检查的方法
     * @return 是否在方法名上属于该命名风格的set方法
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    protected abstract boolean propertyWriterNameMatches(@NonNull Invokable<?, ?> invokable);
}
