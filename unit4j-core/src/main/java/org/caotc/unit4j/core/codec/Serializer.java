package org.caotc.unit4j.core.codec;

import lombok.NonNull;

import java.util.function.Function;

/**
 * @author caotc
 * @date 2023-04-13
 * @since 1.0.0
 */
@FunctionalInterface
public interface Serializer<E> extends Function<E, String> {
    @NonNull
    String serialize(@NonNull E element);

    @Override
    default String apply(E element) {
        return serialize(element);
    }
}
