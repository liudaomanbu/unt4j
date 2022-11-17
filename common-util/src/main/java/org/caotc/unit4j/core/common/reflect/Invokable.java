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

package org.caotc.unit4j.core.common.reflect;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import lombok.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.annotation.CheckForNull;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author caotc
 * @date 2019-11-29
 * @since 1.0.0
 */
public interface Invokable<O, R> extends Element {
    static <O, R> Invokable<O, R> from(@NonNull Method method) {
        return GuavaInvokableProxy.from(method);
    }

    static <O> Invokable<O, O> from(@NonNull Constructor<O> constructor) {
        return GuavaInvokableProxy.from(constructor);
    }

    static <O, R> Invokable<O, R> from(@NonNull Method method, @NonNull TypeToken<O> owner) {
        return GuavaInvokableProxy.from(method, owner);
    }

    static <O> Invokable<O, O> from(@NonNull Constructor<O> constructor, @NonNull TypeToken<O> owner) {
        return GuavaInvokableProxy.from(constructor, owner);
    }

    @NonNull TypeToken<O> ownerType();

    @NonNull
    TypeToken<? extends R> returnType();

    @NonNull
    default <R1 extends R> Invokable<O, R1> returning(Class<R1> returnType) {
        return returning(TypeToken.of(returnType));
    }

    @SuppressWarnings("unchecked")
    @NonNull
    default <R1 extends R> Invokable<O, R1> returning(TypeToken<R1> returnType) {
        if (!returnType.isSupertypeOf(returnType())) {
            throw new IllegalArgumentException(
                    "FieldElement is known to return " + returnType() + ", not " + returnType);
        }
//        invokable.returning(returnType);todo
        return (Invokable<O, R1>) this;
    }

    @Override
    @NonNull Class<? super O> getDeclaringClass();

    @NonNull AnnotatedType annotatedReturnType();


    @Override
    @NonNull Invokable<O, R> accessible(boolean accessible);

    boolean isOverridable();

    boolean isVarArgs();

    @NonNull R invoke(@CheckForNull O receiver, @Nullable Object... args)
            throws InvocationTargetException, IllegalAccessException;

    @NonNull ImmutableList<Parameter> parameters();

    @NonNull ImmutableList<TypeToken<? extends Throwable>> exceptionTypes();
}