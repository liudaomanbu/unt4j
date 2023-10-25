package org.caotc.unit4j.core.serializer;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Component;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Identifiable;
import org.caotc.unit4j.core.Power;

import java.util.Objects;
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
    Serializer<E> aliasUndefinedSerializer;

    @Builder
    private ComponentSerializer(@NonNull Configuration configuration, @NonNull AliasFinder<? super E> aliasFinder,
                                Serializer<E> aliasUndefinedSerializer, @NonNull PowerSerializer.PowerSerializerBuilder<E> powerSerializerBuilder
            , @NonNull AliasUndefinedStrategy aliasUndefinedStrategy) {
        this.configuration = configuration;
        this.aliasFinder = aliasFinder;
        if (Objects.isNull(aliasUndefinedSerializer)) {
            switch (aliasUndefinedStrategy) {
                case ID:
//                    aliasUndefinedSerializer = new IdentifiableSerializer<>();
                    aliasUndefinedSerializer = IdentifiableSerializer.INSTANCE::serialize;
                    break;
                case THROW_EXCEPTION:
                    aliasUndefinedSerializer = (o) -> aliasFinder.findExact(configuration(), o).value();
                    break;
                case AUTO_COMPOSITE:
                default:
                    PowerSerializer<E> powerSerializer = powerSerializerBuilder.build();
//                    PowerSerializer<E> powerSerializer = powerSerializerBuilder.baseSerializer(element -> aliasFinder.findExact(configuration,element).value()).build();
                    aliasUndefinedSerializer = (element) -> element.componentToExponents().entrySet().stream()
                            .map(entry -> {
                                System.out.println("key:" + entry.getKey() + ",value:" + entry.getValue());
                                return new Power<>(entry.getKey(), entry.getValue());
                            })
                            .map(powerSerializer::serialize)
                            .collect(Collectors.joining());
            }
        }
        this.aliasUndefinedSerializer = aliasUndefinedSerializer;
    }

    @Override
    public @NonNull String serialize(@NonNull E element) {
        return aliasFinder.find(configuration, element).map(Alias::value)
                .orElseGet(() -> aliasUndefinedSerializer.serialize(element));
    }

    public static class ComponentSerializerBuilder<E extends Component<E> & Identifiable> {
        @NonFinal
        PowerSerializer.PowerSerializerBuilder<E> powerSerializerBuilder = PowerSerializer.builder();

        public ComponentSerializerBuilder<E> baseSerializer(Serializer<E> baseSerializer) {
            this.powerSerializerBuilder.elementSerializer(baseSerializer);
            return this;
        }

        public ComponentSerializerBuilder<E> powerBaseLeftDelimiter(String baseLeftDelimiter) {
            this.powerSerializerBuilder.elementPrefix(baseLeftDelimiter);
            return this;
        }

        public ComponentSerializerBuilder<E> powerBaseRightDelimiter(String baseRightDelimiter) {
            this.powerSerializerBuilder.elementSuffix(baseRightDelimiter);
            return this;
        }

        public ComponentSerializerBuilder<E> powerOperator(String operator) {
            this.powerSerializerBuilder.operator(operator);
            return this;
        }

        public ComponentSerializerBuilder<E> powerExponentSerializer(Serializer<Integer> exponentSerializer) {
            this.powerSerializerBuilder.exponentSerializer(exponentSerializer);
            return this;
        }
    }
}
