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

import java.lang.reflect.*;
import java.util.Arrays;

/**
 * @param <O> owner type
 * @param <R> return type
 * @author caotc
 * @date 2019-11-29
 * @since 1.0.0
 */
public interface Invokable<O, R> extends Element {
    static <O, R> MethodInvokable<O, R> from(@NonNull Method method) {
        return MethodInvokable.from(method);
    }

    static <O> ConstructInvokable<O> from(@NonNull Constructor<O> constructor) {
        return ConstructInvokable.from(constructor);
    }

    static <O, R> MethodInvokable<O, R> from(@NonNull Method method, @NonNull TypeToken<O> owner) {
        return MethodInvokable.from(method, owner);
    }

    static <O> ConstructInvokable<O> from(@NonNull Constructor<O> constructor, @NonNull TypeToken<O> owner) {
        return ConstructInvokable.from(constructor, owner);
    }

    @NonNull
    TypeToken<O> ownerType();

    //todo guava Invokable returnType为? extends R,但是ownerType为O
    @NonNull
    TypeToken<? extends R> returnType();

    @NonNull
    AnnotatedType annotatedReturnType();

    @Override
    @NonNull
    Invokable<O, R> accessible(boolean accessible);

    boolean isOverridable();

    boolean isVarArgs();

    boolean isBridge();

    @NonNull
    Executable source();

    R invoke(@NonNull O receiver, Object... args)
            throws InvocationTargetException, IllegalAccessException;

    @NonNull
    ImmutableList<Parameter> parameters();

    /**
     * Returns an array of {@code Type} objects that represent the formal
     * parameter types, in declaration order, of the executable represented by
     * this object. Returns an array of length 0 if the
     * underlying executable takes no parameters.
     *
     * <p>If a formal parameter type is a parameterized type,
     * the {@code Type} object returned for it must accurately reflect
     * the actual type parameters used in the source code.
     *
     * <p>If a formal parameter type is a type variable or a parameterized
     * type, it is created. Otherwise, it is resolved.
     *
     * @return an array of {@code Type}s that represent the formal
     * parameter types of the underlying executable, in declaration order
     * @throws GenericSignatureFormatError         if the generic method signature does not conform to the format
     *                                             specified in
     *                                             <cite>The Java&trade; Virtual Machine Specification</cite>
     * @throws TypeNotPresentException             if any of the parameter
     *                                             types of the underlying executable refers to a non-existent type
     *                                             declaration
     * @throws MalformedParameterizedTypeException if any of
     *                                             the underlying executable's parameter types refer to a parameterized
     *                                             type that cannot be instantiated for any reason
     */
    @NonNull
    default ImmutableList<Type> genericParameterTypes() {
        return Arrays.stream(source().getGenericParameterTypes()).collect(ImmutableList.toImmutableList());
    }

    @NonNull
    ImmutableList<TypeToken<? extends Throwable>> exceptionTypes();

    /**
     * this Invokable is overridable in type.
     * owner type generic make no difference.
     *
     * @param type type
     * @return is overridable in type
     */
    default boolean isOverridableIn(@NonNull TypeToken<?> type) {
        if (!isOverridable()) return false;
        if (!isSubclassVisible()) return false;
        if (!declaringType().isSupertypeOf(type)) return false;

        if (isPublic()) return true;
        if (isPackageVisible() && type.getRawType().getPackage() == declaringClass().getPackage()) return true;

        return false;
    }
}