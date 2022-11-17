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
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import lombok.EqualsAndHashCode;
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
@SuppressWarnings("UnstableApiUsage")
@EqualsAndHashCode(callSuper = true)
public class GuavaInvokableProxy<O, P> extends BaseElement implements org.caotc.unit4j.core.common.reflect.Invokable<O, P> {
    @NonNull
    Invokable<O, P> invokable;

    GuavaInvokableProxy(@NonNull Invokable<O, P> delegate) {
        super(delegate);
        this.invokable = delegate;
    }

    @NonNull
    public static <O, P> GuavaInvokableProxy<O, P> from(@NonNull Invokable<O, P> delegate) {
        return new GuavaInvokableProxy<>(delegate);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <O, P> GuavaInvokableProxy<O, P> from(@NonNull Method method) {
        return from((Invokable<O, P>) Invokable.from(method));
    }

    @NonNull
    public static <O> GuavaInvokableProxy<O, O> from(@NonNull Constructor<O> constructor) {
        return from(Invokable.from(constructor));
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <O, P> GuavaInvokableProxy<O, P> from(@NonNull Method method, @NonNull TypeToken<O> owner) {
        return from((Invokable<O, P>) owner.method(method));
    }

    @NonNull
    public static <O> GuavaInvokableProxy<O, O> from(@NonNull Constructor<O> constructor, @NonNull TypeToken<O> owner) {
        return from(owner.constructor(constructor));
    }

    @NonNull
    public TypeToken<O> ownerType() {
        return invokable.getOwnerType();
    }

    @NonNull
    public final TypeToken<? extends P> returnType() {
        return invokable.getReturnType();
    }

    @NonNull
    public final <P1 extends P> GuavaInvokableProxy<O, P1> returning(Class<P1> returnType) {
        return returning(TypeToken.of(returnType));
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public final <P1 extends P> GuavaInvokableProxy<O, P1> returning(TypeToken<P1> returnType) {
        if (!returnType.isSupertypeOf(returnType())) {
            throw new IllegalArgumentException(
                    "FieldElement is known to return " + returnType() + ", not " + returnType);
        }
        invokable.returning(returnType);
        return (GuavaInvokableProxy<O, P1>) this;
    }

    @Override
    @NonNull
    public final Class<? super O> getDeclaringClass() {
        return invokable.getDeclaringClass();
    }

    @NonNull
    public final ImmutableList<Parameter> parameters() {
        return invokable.getParameters().stream().map(GuavaParameterProxy::of).collect(ImmutableList.toImmutableList());
    }

    @NonNull
    public AnnotatedType annotatedReturnType() {
        return invokable.getAnnotatedReturnType();
    }

    @Override
    public boolean accessible() {
        return invokable.isAccessible();
    }

    @Override
    public @NonNull GuavaInvokableProxy<O, P> accessible(boolean accessible) {
        invokable.setAccessible(accessible);
        return this;
    }

    public boolean isOverridable() {
        return invokable.isOverridable();
    }

    public boolean isVarArgs() {
        return invokable.isVarArgs();
    }

    @NonNull
    public final P invoke(@CheckForNull O receiver, @Nullable Object... args)
            throws InvocationTargetException, IllegalAccessException {
        return invokable.invoke(receiver, args);
    }

    @NonNull
    public final ImmutableList<TypeToken<? extends Throwable>> exceptionTypes() {
        return invokable.getExceptionTypes();
    }

    @Override
    public String toString() {
        return invokable.toString();
    }
}