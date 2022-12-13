package org.caotc.unit4j.core.common.util.model;

import java.io.Serializable;

interface OverrideI<T> {
    OverrideI<T> setGenericField(T value);
}

/**
 * @author caotc
 * @date 2022-11-24
 * @since 1.0.0
 */
public class OverrideObject extends OverrideParentObject<Long> {
    public OverrideObject setGenericField(Long value) {
        return null;
    }

    public OverrideObject setGenericField(Comparable<Long> value) {
        return null;
    }

    public OverrideObject setGenericField(Integer value) {
        return null;
    }
}

class OverrideParentObject<T extends Number> implements OverrideI<T> {
    public OverrideI<T> setGenericField(T value) {
        return null;
    }

    public <G, E extends Serializable> GenericFieldSetter<T> setGenericField(T v, E value, T v2) {
        return null;
    }

    public GenericFieldSetter<T> setGenericField() {
        return null;
    }
}