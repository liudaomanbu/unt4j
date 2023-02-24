package org.caotc.unit4j.core.common.reflect;

import com.google.common.reflect.TypeToken;
import lombok.NonNull;

import java.lang.reflect.Method;

/**
 * @param <O> owner type
 * @param <R> return type
 * @author caotc
 * @date 2023-02-17
 * @since 1.0.0
 */
public interface MethodInvokable<O, R> extends Invokable<O, R> {
    static <O, R> MethodInvokable<O, R> from(@NonNull Method method) {
        return GuavaInvokableProxy.from(method);
    }


    static <O, R> MethodInvokable<O, R> from(@NonNull Method method, @NonNull TypeToken<O> owner) {
        return GuavaInvokableProxy.from(method, owner);
    }

    default boolean canOwnBy(@NonNull TypeToken<?> newOwnerType) {
        return declaringType().isSupertypeOf(newOwnerType);
    }

    @NonNull <O1> MethodInvokable<O1, R> ownBy(@NonNull TypeToken<O1> ownerType);

    @NonNull
    MethodInvokable<O, R> accessible(boolean accessible);

    @NonNull
    Method source();

    @NonNull
    default <R1 extends R> MethodInvokable<O, R1> returning(Class<R1> returnType) {
        return returning(TypeToken.of(returnType));
    }

    @SuppressWarnings("unchecked")
    @NonNull
    default <R1 extends R> MethodInvokable<O, R1> returning(TypeToken<R1> returnType) {
        if (!returnType.isSupertypeOf(returnType())) {
            throw new IllegalArgumentException(
                    "FieldElement is known to return " + returnType() + ", not " + returnType);
        }
        return (MethodInvokable<O, R1>) this;
    }

    boolean isOverridden(@NonNull MethodInvokable<?, ?> other);

    default boolean isOverriding(@NonNull MethodInvokable<?, ?> other) {
        return other.isOverridden(this);
    }
}
