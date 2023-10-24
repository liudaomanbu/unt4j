package org.caotc.unit4j.core.serializer;

import lombok.NonNull;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Configuration;

import java.util.Optional;

/**
 * @author caotc
 * @date 2023-04-17
 * @since 1.0.0
 */

public interface AliasFinder<E> {
    @NonNull
    Optional<Alias> find(@NonNull Configuration configuration, @NonNull E element);

    @NonNull
    Alias findExact(@NonNull Configuration configuration, @NonNull E element);
}
