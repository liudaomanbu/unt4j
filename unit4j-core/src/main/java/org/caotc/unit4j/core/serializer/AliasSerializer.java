package org.caotc.unit4j.core.serializer;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Identifiable;

/**
 * @author caotc
 * @date 2023-04-13
 * @since 1.0.0
 */
@Value
@Builder
public class AliasSerializer<E extends Identifiable> implements Serializer<E> {
    @NonNull
    Configuration configuration;
    @NonNull
    AliasFinder<E> aliasFinder;
    @NonNull
    Serializer<E> aliasUndefinedSerializer;

    @Override
    public @NonNull String serialize(@NonNull E element) {
        return aliasFinder.find(configuration, element)
                .map(Alias::value)
                .orElseGet(() -> aliasUndefinedSerializer.serialize(element));
    }
}
