package org.caotc.unit4j.core.common.util.model;

/**
 * @author caotc
 * @date 2022-11-04
 * @since 1.0.0
 */
public class IntegerGenericFieldGetter implements GenericFieldGetter<Integer> {
    @Override
    public Integer getGenericField() {
        return Integer.MIN_VALUE;
    }
}
