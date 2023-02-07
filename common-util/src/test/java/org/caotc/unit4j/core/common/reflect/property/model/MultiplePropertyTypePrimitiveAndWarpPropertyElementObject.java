package org.caotc.unit4j.core.common.reflect.property.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * @author caotc
 * @date 2023-01-11
 * @since 1.0.0
 */
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class MultiplePropertyTypePrimitiveAndWarpPropertyElementObject {
    int value;

    public Integer getValue() {
        return Integer.MAX_VALUE;
    }
}



