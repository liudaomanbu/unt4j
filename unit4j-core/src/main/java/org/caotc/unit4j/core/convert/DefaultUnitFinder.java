package org.caotc.unit4j.core.convert;

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.unit.Unit;

import java.util.Collection;

/**
 * @author caotc
 * @date 2023-06-27
 * @since 1.0.0
 */
@Value(staticConstructor = "of")
public class DefaultUnitFinder implements UnitFinder {
    @NonNull
    SingletonUnitFinder singletonUnitFinder;
    @NonNull
    QuantityChooser quantityChooser;

    @Override
    public @NonNull Unit find(@NonNull Configuration configuration, @NonNull Collection<Quantity> quantities) {
        return quantityChooser().choose(quantities, configuration).unit();
    }

    @Override
    public @NonNull Unit find(@NonNull Configuration configuration, @NonNull Quantity quantity) {
        return singletonUnitFinder().find(configuration, quantity);
    }
}
