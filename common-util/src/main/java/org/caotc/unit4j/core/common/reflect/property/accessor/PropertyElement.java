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
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.NonNull;
import org.caotc.unit4j.core.common.reflect.FieldElement;
import org.caotc.unit4j.core.common.util.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Optional;

/**
 * 属性元素
 *
 * @param <O> 拥有该属性的类
 * @param <P> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
public interface PropertyElement<O, P> extends AnnotatedElement, Member {

    @NonNull
    static <T, R> PropertyElement<T, R> from(@NonNull Field field) {
        return from(FieldElement.from(field));
    }

    @NonNull
    static <T, R> PropertyElement<T, R> from(@NonNull FieldElement<T, R> fieldElement) {
        return ReflectionUtil.isPropertyWriter(fieldElement) ? PropertyAccessor.from(fieldElement)
                : PropertyReader.from(fieldElement);
    }

    @NonNull
    static <T, R> PropertyElement<T, R> from(@NonNull Invokable<T, R> invokable,
                                             @NonNull String propertyName) {
        if (ReflectionUtil.isPropertyReader(invokable)) {
            PropertyReader.from(invokable, propertyName);
        }
        if (ReflectionUtil.isPropertyWriter(invokable)) {
            PropertyReader.from(invokable, propertyName);
        }
        throw new IllegalArgumentException(String.format("%s is not a PropertyElement", invokable));
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
     * get owner type of {@code O}
     *
     * @return owner type
     * @author caotc
     * @date 2019-12-04
     * @since 1.0.0
     */
    @NonNull
    TypeToken<O> ownerType();

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

    boolean basedOnField();

    /**
     * @author caotc
     * @date 2019-12-09
     * @implNote
     * @implSpec
     * @apiNote
     * @since 1.0.0
     */
    boolean isStatic();

    /**
     * @author caotc
     * @date 2019-12-09
     * @implNote
     * @implSpec
     * @apiNote
     * @since 1.0.0
     */
    boolean isFinal();

    /**
     * @author caotc
     * @date 2019-12-09
     * @implNote
     * @implSpec
     * @apiNote
     * @since 1.0.0
     */
    boolean isAbstract();

    /**
     * @author caotc
     * @date 2019-12-09
     * @implNote
     * @implSpec
     * @apiNote
     * @since 1.0.0
     */
    boolean isSynchronized();

    /**
     * @author caotc
     * @date 2019-12-09
     * @implNote
     * @implSpec
     * @apiNote
     * @since 1.0.0
     */
    boolean isVolatile();

    /**
     * @author caotc
     * @date 2019-12-09
     * @implNote
     * @implSpec
     * @apiNote
     * @since 1.0.0
     */
    boolean isTransient();

    /**
     * 该元素的权限级别
     *
     * @return 权限级别
     * @author caotc
     * @date 2019-07-14
     * @since 1.0.0
     */
    @NonNull
    AccessLevel accessLevel();

    /**
     * 获取注解
     *
     * @param annotationClass 注解类
     * @return 注解的 {@link Optional}
     * @author caotc
     * @date 2019-11-22
     * @since 1.0.0
     */
    @NonNull <X extends Annotation> Optional<X> annotation(
            @NonNull Class<X> annotationClass);

    /**
     * 获取所有注解
     *
     * @return 所有注解集合
     * @author caotc
     * @date 2019-11-22
     * @since 1.0.0
     */
    @NonNull
    ImmutableList<Annotation> annotations();

    /**
     * 获取注解集合
     *
     * @param annotationClass 注解类
     * @return 该类注解集合
     * @author caotc
     * @date 2019-11-22
     * @since 1.0.0
     */
    @NonNull <X extends Annotation> ImmutableList<X> annotations(
            @NonNull Class<X> annotationClass);

    /**
     * 获取所有定义在该属性上的注解
     *
     * @return 所有定义在该属性上的集合
     * @author caotc
     * @date 2019-11-22
     * @since 1.0.0
     */
    @NonNull
    ImmutableList<Annotation> declaredAnnotations();


}