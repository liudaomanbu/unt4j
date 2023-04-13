package org.caotc.unit4j.core.codec;

import lombok.NonNull;
import org.caotc.unit4j.core.unit.Prefix;

/**
 * @author caotc
 * @date 2023-04-13
 * @since 1.0.0
 */
public interface PrefixCodec {
    @NonNull
    String serialize(@NonNull Prefix prefix);
}
