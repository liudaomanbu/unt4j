package org.caotc.unit4j.core.convert;

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.unit.Unit;

/**
 * @author caotc
 * @date 2024-04-28
 * @since 1.0.0
 */
@Value(staticConstructor = "of")
public class FixedUnitFinder implements SingletonUnitFinder {
    @NonNull
    Unit targetUnit;

    @Override
    public @NonNull Unit find(@NonNull Configuration configuration, @NonNull Quantity quantity) {
        return targetUnit();
    }
}
