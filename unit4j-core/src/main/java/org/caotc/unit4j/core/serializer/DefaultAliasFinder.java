package org.caotc.unit4j.core.serializer;

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.exception.AliasUndefinedException;

import java.util.Optional;

/**
 * @author caotc
 * @date 2023-10-07
 * @since 1.0.0
 */
@Value(staticConstructor = "of")
public class DefaultAliasFinder<E> implements AliasFinder<E> {
    @NonNull
    Alias.Type aliasType;

    @Override
    public @NonNull Optional<Alias> find(@NonNull Configuration configuration, @NonNull E element) {
        return configuration.aliases(element).stream().findFirst();
    }

    @Override
    public @NonNull Alias findExact(@NonNull Configuration configuration, @NonNull E element) {
        return find(configuration, element).orElseThrow(() -> AliasUndefinedException.create(element, configuration, aliasType));
    }
}
