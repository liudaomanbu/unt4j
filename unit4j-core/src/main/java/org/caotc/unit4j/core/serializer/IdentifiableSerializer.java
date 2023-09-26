package org.caotc.unit4j.core.serializer;

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Identifiable;

/**
 * @author caotc
 * @date 2023-04-13
 * @since 1.0.0
 */
@Value
public class IdentifiableSerializer<E extends Identifiable> implements Serializer<E> {
    @Override
    public @NonNull String serialize(@NonNull E identifiable) {
        return identifiable.id();
    }
}
