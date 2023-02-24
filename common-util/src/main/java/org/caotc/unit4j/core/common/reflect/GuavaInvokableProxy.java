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
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.core.BridgeMethodResolver;

import javax.annotation.CheckForNull;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @param <O> owner type
 * @param <R> return type
 * @author caotc
 * @date 2019-11-29
 * @since 1.0.0
 */
@SuppressWarnings("UnstableApiUsage")
@EqualsAndHashCode(callSuper = true)
public abstract class GuavaInvokableProxy<O, R> extends BaseElement implements Invokable<O, R> {
    @Getter(AccessLevel.PROTECTED)
    @NonNull
    com.google.common.reflect.Invokable<O, R> invokable;

    GuavaInvokableProxy(@NonNull com.google.common.reflect.Invokable<O, R> delegate) {
        super(delegate);
        this.invokable = delegate;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <O, P> MethodInvokable<O, P> from(@NonNull Method method) {
        return new MethodGuavaInvokableProxy<>((com.google.common.reflect.Invokable<O, P>) com.google.common.reflect.Invokable.from(method), method);
    }

    @NonNull
    public static <O> ConstructorInvokable<O> from(@NonNull Constructor<O> constructor) {
        return new ConstructorGuavaInvokableProxy<>(com.google.common.reflect.Invokable.from(constructor), constructor);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <O, P> MethodInvokable<O, P> from(@NonNull Method method, @NonNull TypeToken<O> owner) {
        return new MethodGuavaInvokableProxy<>((com.google.common.reflect.Invokable<O, P>) owner.method(method), method);
    }

    @NonNull
    public static <O> ConstructorInvokable<O> from(@NonNull Constructor<O> constructor, @NonNull TypeToken<O> owner) {
        return new ConstructorGuavaInvokableProxy<>(owner.constructor(constructor), constructor);
    }

    @NonNull
    public TypeToken<O> ownerType() {
        return invokable.getOwnerType();
    }

    @NonNull
    public final TypeToken<? extends R> returnType() {
        return invokable.getReturnType();
    }

    @Override
    @NonNull
    public final Class<? super O> declaringClass() {
        return invokable.getDeclaringClass();
    }

    @NonNull
    public final ImmutableList<Parameter> parameters() {
        return invokable.getParameters().stream()
                .map(parameter -> GuavaParameterProxy.of(parameter, this))
                .collect(ImmutableList.toImmutableList());
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
    public @NonNull Invokable<O, R> accessible(boolean accessible) {
        invokable.setAccessible(accessible);
        return this;
    }

    public boolean isOverridable() {
        return invokable.isOverridable();
    }

    public boolean isVarArgs() {
        return invokable.isVarArgs();
    }

    public final R invoke(@CheckForNull O receiver, @Nullable Object... args)
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

@Getter
@SuppressWarnings("UnstableApiUsage")
class MethodGuavaInvokableProxy<O, P> extends GuavaInvokableProxy<O, P> implements MethodInvokable<O, P> {
    @NonNull
    Method source;

    MethodGuavaInvokableProxy(@NonNull com.google.common.reflect.Invokable<O, P> delegate, @NonNull Method source) {
        super(delegate);
        this.source = source;
    }

    @NonNull
    @Override
    public MethodInvokable<O, P> accessible(boolean accessible) {
        super.accessible(accessible);
        return this;
    }

    @NonNull
    public <P1 extends P> MethodInvokable<O, P1> returning(Class<P1> returnType) {
        return returning(TypeToken.of(returnType));
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <P1 extends P> MethodInvokable<O, P1> returning(TypeToken<P1> returnType) {
        if (!returnType.isSupertypeOf(returnType())) {
            throw new IllegalArgumentException(
                    "FieldElement is known to return " + returnType() + ", not " + returnType);
        }
        invokable().returning(returnType);
        return (MethodInvokable<O, P1>) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <O1> MethodInvokable<O1, P> ownBy(@NonNull TypeToken<O1> newOwnerType) {
        if (!canOwnBy(newOwnerType)) {
            throw new IllegalArgumentException(String.format("%s can not own by %s", source(), newOwnerType));
        }
        return new MethodGuavaInvokableProxy<>((com.google.common.reflect.Invokable<O1, P>) newOwnerType.method(source()), source());
    }

    @Override
    public boolean isBridge() {
        return source().isBridge();
    }

    public boolean isOverridden(@NonNull MethodInvokable<?, ?> other) {
        //same method can't override
        if (Objects.equals(source(), other.source())
                //same declaring type can't override
                || Objects.equals(declaringType(), other.declaringType())
                || !Objects.equals(getName(), other.getName())
                //overriding method must give more or equal access than the overridden method
                || accessLevel().isMore(other.accessLevel())
                || !ownerType().isSupertypeOf(other.ownerType())
                || !isOverridableIn(other.declaringType())) {
            return false;
        }

        if (isBridge() || other.isBridge()) {
            MethodInvokable<?, ?> bridgeInvokable = isBridge() ? this : other;
            //bridge invokable source must be method
            Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(bridgeInvokable.source());
            //visibility bridge method is overriding
            if (BridgeMethodResolver.isVisibilityBridgeMethodPair(bridgeInvokable.source(), bridgedMethod)) {
                return true;
            }
            MethodInvokable<?, ?> bridgedInvokable = Invokable.from(bridgedMethod, bridgeInvokable.ownerType());
            return isBridge() ? bridgedInvokable.isOverridden(other) : isOverridden(bridgedInvokable);
        }

        return isReturnTypeTheSameAs(other) && areParametersTheSameAs(other);
    }

    boolean areParametersTheSameAs(Invokable<?, ?> other) {
        ImmutableList<TypeToken<?>> myPrmTypes = parameters().stream().map(Parameter::type).collect(ImmutableList.toImmutableList());
        ImmutableList<TypeToken<?>> otherPrmTypes = other.parameters().stream().map(Parameter::type).collect(ImmutableList.toImmutableList());
        return myPrmTypes.equals(otherPrmTypes);
    }

    boolean isReturnTypeTheSameAs(Invokable<?, ?> other) {
        return other.returnType().equals(returnType());
    }
}

@Getter
@SuppressWarnings("UnstableApiUsage")
class ConstructorGuavaInvokableProxy<O> extends GuavaInvokableProxy<O, O> implements ConstructorInvokable<O> {
    @NonNull
    Constructor<O> source;

    ConstructorGuavaInvokableProxy(@NonNull com.google.common.reflect.Invokable<O, O> delegate, @NonNull Constructor<O> source) {
        super(delegate);
        this.source = source;
    }

    @NonNull
    @Override
    public ConstructorInvokable<O> accessible(boolean accessible) {
        super.accessible(accessible);
        return this;
    }

    @Override
    public boolean isBridge() {
        return false;
    }

}