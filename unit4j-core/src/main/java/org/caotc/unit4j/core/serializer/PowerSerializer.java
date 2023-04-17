package org.caotc.unit4j.core.serializer;

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Power;
import org.caotc.unit4j.core.constant.StringConstant;

/**
 * @author caotc
 * @date 2023-04-14
 * @since 1.0.0
 */
@Value
public class PowerSerializer<E> implements Serializer<Power<E>> {
    @NonNull
    Serializer<E> baseSerializer;
    @NonNull
    String baseLeftDelimiter;
    @NonNull
    String baseRightDelimiter;
    @NonNull
    String operator;
    @NonNull
    Serializer<Integer> exponentSerializer;

    @Override
    public @NonNull String serialize(@NonNull Power<E> power) {
        return StringConstant.EMPTY_JOINER.join(baseLeftDelimiter(), baseSerializer().serialize(power.base())
                , baseRightDelimiter(), operator(), exponentSerializer().serialize(power.exponent()));
    }
}
