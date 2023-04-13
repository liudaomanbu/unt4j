package org.caotc.unit4j.core.codec;

import com.google.common.collect.ImmutableSet;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.unit.Prefix;

/**
 * @author caotc
 * @date 2023-04-13
 * @since 1.0.0
 */
@Value
public class AliasPrefixSerializer implements PrefixCodec, Serializer<Prefix> {
    @NonNull
    Configuration configuration;
    @NonNull
    Alias.Type aliasType;
    @NonNull
    Chooser<Alias> aliasChooser;
    @NonNull
    Serializer<Prefix> aliasUndefinedSerializer;

    @Override
    public @NonNull String serialize(@NonNull Prefix prefix) {
        ImmutableSet<Alias> aliases = configuration.aliases(prefix, aliasType);
        return aliases.isEmpty() ? aliasUndefinedSerializer.serialize(prefix)
                : aliasChooser.choose(aliases).value();
    }
}
