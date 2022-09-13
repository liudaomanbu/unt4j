package org.caotc.unit4j.core.common.util.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;

/**
 * @author caotc
 * @date 2022-09-07
 * @since 1.0.0
 */
@Getter
@Accessors(fluent = false)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
@FieldNameConstants
public class StringFieldAndStringFieldGetMethodObject {
    String stringField;
}
