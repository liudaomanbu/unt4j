package org.caotc.unit4j.core.serializer;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Component;
import org.caotc.unit4j.core.Power;
import org.caotc.unit4j.core.constant.StringConstant;

import java.util.stream.Collectors;

/**
 * @author caotc
 * @date 2023-10-25
 * @since 1.0.0
 */
@Value
@Builder
public class ComponentCompositeSerializer<E extends Component<E>> implements Serializer<E> {
    @NonNull
    Serializer<? super Power<E>> powerSerializer;
    @NonNull
    @Builder.Default
    CharSequence delimiter = StringConstant.EMPTY;
    @NonNull
    @Builder.Default
    CharSequence prefix = StringConstant.EMPTY;
    @NonNull
    @Builder.Default
    CharSequence suffix = StringConstant.EMPTY;

    @Override
    public @NonNull String serialize(@NonNull E element) {
        //todo components()?
        return element.componentToExponents().entrySet().stream()
                .map(entry -> new Power<>(entry.getKey(), entry.getValue()))
                .map(powerSerializer::serialize)
                .collect(Collectors.joining(delimiter(), prefix(), suffix()));
    }
}
