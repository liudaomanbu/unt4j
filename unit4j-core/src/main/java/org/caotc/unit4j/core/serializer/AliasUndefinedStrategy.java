package org.caotc.unit4j.core.serializer;

import lombok.NonNull;
import org.caotc.unit4j.core.Alias;
import org.caotc.unit4j.core.Component;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Identifiable;

/**
 * 有别名的对象的{@link Alias}未注册时的策略枚举
 *
 * @author caotc
 * @date 2019-05-18
 * @since 1.0.0
 */
public enum AliasUndefinedStrategy {
    /**
     * 抛出异常
     */
    THROW_EXCEPTION {
        @Override
        public <E extends Component<E> & Identifiable> Serializer<E> createSerializer(@NonNull AliasFinder<? super E> aliasFinder, @NonNull Configuration configuration) {
            return (o) -> aliasFinder.findExact(configuration, o).value();
        }
    },
    /**
     * 自动组合
     */
    AUTO_COMPOSITE {
        @Override
        public <E extends Component<E> & Identifiable> Serializer<E> createSerializer(@NonNull AliasFinder<? super E> aliasFinder, @NonNull Configuration configuration) {
            PowerSerializer<E> powerSerializer = PowerSerializer.<E>builder()
                    .elementSerializer(element -> aliasFinder.findExact(configuration, element).value())
                    .build();
            return ComponentCompositeSerializer.<E>builder()
                    .powerSerializer(powerSerializer)
                    .build();//todo 确认分隔符是否需要
        }
    },
    /**
     * 使用id
     */
    USE_ID {
        @Override
        public <E extends Component<E> & Identifiable> Serializer<E> createSerializer(@NonNull AliasFinder<? super E> aliasFinder, @NonNull Configuration configuration) {
            return IdentifiableSerializer.instance()::serialize;
        }
    };

    public abstract <E extends Component<E> & Identifiable> Serializer<E> createSerializer(@NonNull AliasFinder<? super E> aliasFinder, @NonNull Configuration configuration);
}
