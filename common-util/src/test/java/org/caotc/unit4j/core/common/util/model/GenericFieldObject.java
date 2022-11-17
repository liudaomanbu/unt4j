package org.caotc.unit4j.core.common.util.model;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;

/**
 * @author caotc
 * @date 2022-09-07
 * @since 1.0.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
@FieldNameConstants
public class GenericFieldObject<T> {
    T genericField;
}
