package org.caotc.unit4j.core;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.NonNull;

/**
 * @author caotc
 * @date 2023-04-14
 * @since 1.0.0
 */
public interface Component<C extends Component<C>> {
    @NonNull
    ImmutableMap<C, Integer> componentToExponents();

    @NonNull
    default ImmutableSet<Power<C>> components() {
        return componentToExponents().entrySet().stream()
                .map(entry -> new Power<>(entry.getKey(), entry.getValue()))
                .collect(ImmutableSet.toImmutableSet());
    }
}
