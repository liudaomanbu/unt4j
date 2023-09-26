package org.caotc.unit4j.core.serializer;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Component;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Identifiable;
import org.caotc.unit4j.core.Power;
import org.caotc.unit4j.core.exception.AliasUndefinedException;

import java.util.stream.Collectors;

/**
 * @author caotc
 * @date 2023-04-14
 * @since 1.0.0
 */
@Value
public class ComponentSerializer<E extends Component<E> & Identifiable> implements Serializer<E> {
    @NonNull
    Configuration configuration;
    @NonNull
    AliasFinder<? super E> aliasFinder;
    @NonNull
    Serializer<E> aliasUndefinedSerializer;
    @NonNull
    PowerSerializer<E> powerSerializer;

//    AliasUndefinedStrategy aliasUndefinedStrategy = null;

    @Builder
    private ComponentSerializer(@NonNull Configuration configuration, @NonNull AliasFinder<? super E> aliasFinder,
                                @NonNull PowerSerializer.PowerSerializerBuilder<E> powerSerializer, @NonNull AliasUndefinedStrategy aliasUndefinedStrategy) {

        this.configuration = configuration;
        this.aliasFinder = aliasFinder;
        this.powerSerializer = powerSerializer.baseSerializer(this).build();
        switch (aliasUndefinedStrategy) {
            case ID:
                this.aliasUndefinedSerializer = new IdentifiableSerializer<>();
                break;
            case THROW_EXCEPTION:
                this.aliasUndefinedSerializer = (o) -> {
                    throw AliasUndefinedException.create(o, configuration(), null);
                };
                break;
            case AUTO_COMPOSITE:
            default:
                this.aliasUndefinedSerializer = (element) -> element.componentToExponents().entrySet().stream()
                        .map(entry -> new Power<>(entry.getKey(), entry.getValue()))
                        .map(this.powerSerializer::serialize)
                        .collect(Collectors.joining());
        }
    }

    @Override
    public @NonNull String serialize(@NonNull E element) {
        return element.componentToExponents().entrySet().stream()
                .map(entry -> new Power<>(entry.getKey(), entry.getValue()))
                .map(powerSerializer::serialize)
                .collect(Collectors.joining());
    }


}
