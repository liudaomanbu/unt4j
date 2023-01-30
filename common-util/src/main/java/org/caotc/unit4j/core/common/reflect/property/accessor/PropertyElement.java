/*
 * Copyright (C) 2020 the original author or authors.
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

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import lombok.NonNull;
import org.caotc.unit4j.core.common.reflect.AnnotatedElement;
import org.caotc.unit4j.core.common.reflect.WithAccessLevel;
import org.caotc.unit4j.core.common.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 属性元素
 * todo 所有类的范型字母
 *
 * @param <O> 拥有该属性的类
 * @param <P> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
public interface PropertyElement<O, P> extends WithAccessLevel, AnnotatedElement {
    @NonNull
    static <T, R> PropertyElement<T, R> from(@NonNull Type ownerType, @NonNull Field field) {
        return PropertyAccessor.from(ownerType, field);
    }

    @NonNull
    static <T, R> PropertyElement<T, R> from(@NonNull Class<T> ownerClass, @NonNull Field field) {
        return PropertyAccessor.from(ownerClass, field);
    }

    //todo from名字修改?
    @NonNull
    static <T, R> PropertyElement<T, R> from(@NonNull TypeToken<T> ownerType, @NonNull Field field) {
        return PropertyAccessor.from(ownerType, field);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    static <T, R> PropertyElement<T, R> from(@NonNull Type ownerType, @NonNull Method method,
                                             @NonNull String propertyName) {
        return from((TypeToken<T>) TypeToken.of(ownerType), method, propertyName);
    }

    @NonNull
    static <T, R> PropertyElement<T, R> from(@NonNull Class<T> ownerClass, @NonNull Method method,
                                             @NonNull String propertyName) {
        return from(TypeToken.of(ownerClass), method, propertyName);
    }

    @NonNull
    static <T, R> PropertyElement<T, R> from(@NonNull TypeToken<T> ownerType, @NonNull Method method,
                                             @NonNull String propertyName) {
        if (ReflectionUtil.isPropertyReader(method)) {
            return PropertyReader.from(ownerType, method, propertyName);
        }
        if (ReflectionUtil.isPropertyWriter(method)) {
            return PropertyWriter.from(ownerType, method, propertyName);
        }
        throw new IllegalArgumentException(String.format("%s is not a PropertyElement", method));
    }

    /**
     * 获取属性名称
     *
     * @return 属性名称
     * @author caotc
     * @date 2019-11-22
     * @since 1.0.0
     */
    @NonNull
    String propertyName();

    /**
     * 获取属性类型
     *
     * @return 属性类型
     * @author caotc
     * @date 2019-11-22
     * @since 1.0.0
     */
    @NonNull
    TypeToken<? extends P> propertyType();

    /**
     * 设置属性类型
     *
     * @param propertyType 属性类型
     * @return this
     * @author caotc
     * @date 2019-06-25
     * @since 1.0.0
     */
    @NonNull <R1 extends P> PropertyElement<O, R1> propertyType(@NonNull Class<R1> propertyType);

    /**
     * 设置属性类型
     *
     * @param propertyType 属性类型
     * @return this
     * @author caotc
     * @date 2019-06-25
     * @since 1.0.0
     */
    @NonNull <R1 extends P> PropertyElement<O, R1> propertyType(@NonNull TypeToken<R1> propertyType);

    boolean isReader();

    boolean isWriter();

    default boolean isAccessor() {
        return isReader() && isWriter();
    }

    default PropertyReader<O, P> toReader() {
        Preconditions.checkArgument(isReader(), "it is not a PropertyReader");
        return (PropertyReader<O, P>) this;
    }

    default PropertyWriter<O, P> toWriter() {
        Preconditions.checkArgument(isWriter(), "it is not a PropertyWriter");
        return (PropertyWriter<O, P>) this;
    }

    default PropertyAccessor<O, P> toAccessor() {
        Preconditions.checkArgument(isAccessor(), "it is not a PropertyAccessor");
        return (PropertyAccessor<O, P>) this;
    }

    TypeToken<O> ownerType();//todo 范型

    TypeToken<O> declaringType();//todo 范型

    boolean accessible();

    @NonNull
    PropertyElement<O, P> accessible(boolean accessible);

}