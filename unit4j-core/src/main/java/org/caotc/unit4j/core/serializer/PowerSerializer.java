package org.caotc.unit4j.core.serializer;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Power;
import org.caotc.unit4j.core.common.util.Util;
import org.caotc.unit4j.core.constant.StringConstant;

/**
 * @author caotc
 * @date 2023-04-14
 * @since 1.0.0
 */
@Value
@Builder
public class PowerSerializer<E> implements Serializer<Power<? extends E>> {


    @NonNull
    Serializer<E> elementSerializer;
    @NonNull
    @Builder.Default
    String elementPrefix = StringConstant.HALF_WIDTH_LEFT_PARENTHESIS;
    @NonNull
    @Builder.Default
    String elementSuffix = StringConstant.HALF_WIDTH_RIGHT_PARENTHESIS;
    @NonNull
    @Builder.Default
    String operator = StringConstant.EMPTY;
    @NonNull
    @Builder.Default
    Serializer<Integer> exponentSerializer = Util::getSuperscript;
    @NonNull
    @Builder.Default
    String exponentPrefix = StringConstant.EMPTY;
    @NonNull
    @Builder.Default
    String exponentSuffix = StringConstant.EMPTY;

    @Override
    public @NonNull String serialize(@NonNull Power<? extends E> power) {
        return StringConstant.EMPTY_JOINER.join(elementPrefix(), elementSerializer().serialize(power.base()), elementSuffix()
                , operator(), exponentPrefix(), exponentSerializer().serialize(power.exponent()), exponentSuffix());
    }
}
