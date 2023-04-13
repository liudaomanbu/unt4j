package org.caotc.unit4j.core.codec;

import lombok.NonNull;
import org.caotc.unit4j.core.unit.type.UnitType;

/**
 * @author caotc
 * @date 2023-04-13
 * @since 1.0.0
 */
public interface UnitTypeCodec {
    @NonNull
    String serialize(@NonNull UnitType unitType);
}
