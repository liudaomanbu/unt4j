package org.caotc.unit4j.core.convert;

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.math.number.AbstractNumber;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.UnitGroup;

/**
 * @author caotc
 * @date 2023-06-16
 * @since 1.0.0
 */
@Value
public class TargetValueTargetUnitChooser implements Chooser {
    @NonNull AbstractNumber targetValue;

    @Override
    public @NonNull Unit choose(@NonNull Quantity quantity, @NonNull Configuration configuration) {
        UnitGroup unitGroup = configuration.getUnitGroup(quantity.unit());

        return null;
    }
}
