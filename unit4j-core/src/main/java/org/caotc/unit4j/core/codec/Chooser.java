package org.caotc.unit4j.core.codec;

import lombok.NonNull;

import java.util.Collection;
import java.util.function.Function;

/**
 * @author caotc
 * @date 2023-04-13
 * @since 1.0.0
 */
@FunctionalInterface
public interface Chooser<E> extends Function<Collection<E>, E> {
    @NonNull
    E choose(@NonNull Collection<E> elements);

    @Override
    default E apply(Collection<E> elements) {
        return choose(elements);
    }
}
