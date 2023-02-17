package org.caotc.unit4j.core.common.reflect;

import com.google.common.reflect.TypeToken;
import lombok.NonNull;

import java.lang.reflect.Constructor;

/**
 * @param <O> owner type
 * @param <R> return type
 * @author caotc
 * @date 2023-02-17
 * @since 1.0.0
 */
public interface ConstructInvokable<O> extends Invokable<O, O> {

    static <O> ConstructInvokable<O> from(@NonNull Constructor<O> constructor) {
        return GuavaInvokableProxy.from(constructor);
    }

    static <O> ConstructInvokable<O> from(@NonNull Constructor<O> constructor, @NonNull TypeToken<O> owner) {
        return GuavaInvokableProxy.from(constructor, owner);
    }

    @NonNull
    ConstructInvokable<O> accessible(boolean accessible);

    @NonNull
    Constructor<O> source();

}
