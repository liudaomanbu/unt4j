package org.caotc.unit4j.core.codec;

import lombok.NonNull;
import org.caotc.unit4j.core.unit.Unit;

/**
 * @author caotc
 * @date 2023-04-13
 * @since 1.0.0
 */
public interface UnitCodec {
    @NonNull
    String serialize(@NonNull Unit unit);
}
