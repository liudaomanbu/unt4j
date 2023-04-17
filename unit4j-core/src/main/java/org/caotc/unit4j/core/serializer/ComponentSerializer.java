package org.caotc.unit4j.core.serializer;

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Component;
import org.caotc.unit4j.core.Power;

import java.util.stream.Collectors;

/**
 * @author caotc
 * @date 2023-04-14
 * @since 1.0.0
 */
@Value
public class ComponentSerializer<E extends Component<E>> implements Serializer<E> {
    @NonNull
    PowerSerializer<E> powerSerializer;

    @Override
    public @NonNull String serialize(@NonNull E element) {
        return element.componentToExponents().entrySet().stream()
                .map(entry -> new Power<>(entry.getKey(), entry.getValue()))
                .map(powerSerializer::serialize)
                .collect(Collectors.joining());
    }
}
