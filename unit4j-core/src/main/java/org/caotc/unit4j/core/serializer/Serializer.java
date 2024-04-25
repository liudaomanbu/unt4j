package org.caotc.unit4j.core.serializer;

import lombok.NonNull;

import java.util.function.Function;
import java.util.function.Predicate;

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

    @NonNull
    default Serializer<E> compose(@NonNull Predicate<E> serializerPredicate, @NonNull Serializer<E> serializer) {
        return (E element) -> serializerPredicate.test(element) ? serializer.serialize(element) : serialize(element);
    }

    @NonNull
    default Serializer<E> compose(@NonNull PredicateSerializer<E> serializer) {
        return compose(serializer::matches, serializer);
    }
}
