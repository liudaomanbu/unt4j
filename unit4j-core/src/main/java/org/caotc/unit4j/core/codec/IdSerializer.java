package org.caotc.unit4j.core.codec;

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Identifiable;

/**
 * @author caotc
 * @date 2023-04-13
 * @since 1.0.0
 */
@Value
public class IdSerializer implements Serializer<Identifiable> {
    @Override
    public @NonNull String serialize(@NonNull Identifiable identifiable) {
        return identifiable.id();
    }
}
