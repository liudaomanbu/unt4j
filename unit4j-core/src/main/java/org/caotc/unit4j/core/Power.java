package org.caotc.unit4j.core;

import lombok.NonNull;
import lombok.Value;

/**
 * //todo 移到另一个包
 *
 * @author caotc
 * @date 2023-04-14
 * @since 1.0.0
 */
@Value
public class Power<E> {
    @NonNull
    E base;
    int exponent;
}
