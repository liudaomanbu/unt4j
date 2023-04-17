package org.caotc.unit4j.core.serializer;

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Configuration;

/**
 * @author caotc
 * @date 2023-04-13
 * @since 1.0.0
 */
@Value
public class AliasSerializer<E> implements Serializer<E> {
    @NonNull
    Configuration configuration;
    @NonNull
    AliasFinder<E> aliasFinder;
    @NonNull
    Serializer<? super E> aliasUndefinedSerializer;

    @Override
    public @NonNull String serialize(@NonNull E element) {
        return aliasFinder.find(configuration, element)
                .map(Alias::value)
                .orElseGet(() -> aliasUndefinedSerializer.serialize(element));
    }
}
