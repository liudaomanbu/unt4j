package org.caotc.unit4j.core.serializer;

import lombok.NonNull;

/**
 * @author caotc
 * @date 2023-11-17
 * @since 1.0.0
 */
public interface PredicateSerializer<E> extends Serializer<E> {
    boolean matches(@NonNull E element);
}
