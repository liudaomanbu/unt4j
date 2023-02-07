package org.caotc.unit4j.core.common.reflect.property.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author caotc
 * @date 2023-01-11
 * @since 1.0.0
 */
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class MultipleDeclaringTypePropertyElementObject extends Parent {
    int value;
}

@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
class Parent {
    int value = Integer.MAX_VALUE;
}



