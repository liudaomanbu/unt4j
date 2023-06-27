package org.caotc.unit4j.core.convert;

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author caotc
 * @date 2023-06-27
 * @since 1.0.0
 */
@Value(staticConstructor = "of")
public class DefaultAutoConverter implements AutoConverter {
    @NonNull
    SingletonAutoConverter singletonAutoConverter;
    @NonNull
    QuantityChooser quantityChooser;

    @Override
    public @NonNull Collection<Quantity> autoConvert(@NonNull Configuration configuration, @NonNull Collection<Quantity> quantities, boolean unitConsistency) {
        if (unitConsistency) {
            Quantity consistencyQuantity = quantityChooser().choose(quantities, configuration);
            return quantities.stream().map(quantity -> configuration.convert(quantity, consistencyQuantity.unit())).collect(Collectors.toList());
        }
        return quantities.stream().map(quantity -> autoConvert(configuration, quantity)).collect(Collectors.toList());
    }

    @Override
    public @NonNull Quantity autoConvert(@NonNull Configuration configuration, @NonNull Quantity quantity) {
        return singletonAutoConverter().autoConvert(configuration, quantity);
    }
}
