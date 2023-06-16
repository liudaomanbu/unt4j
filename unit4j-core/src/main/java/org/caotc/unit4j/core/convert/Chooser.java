package org.caotc.unit4j.core.convert;

import lombok.NonNull;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.unit.Unit;

/**
 * @author caotc
 * @date 2023-04-13
 * @since 1.0.0
 */
@FunctionalInterface
public interface Chooser {
    @NonNull
    Unit choose(@NonNull Quantity quantity, @NonNull Configuration configuration);

    default Unit choose(@NonNull Quantity quantity) {
        return choose(quantity, Configuration.defaultInstance());
    }
}
