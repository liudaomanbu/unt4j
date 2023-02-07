package org.caotc.unit4j.core.common.reflect.property.model;


import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author caotc
 * @date 2023-01-11
 * @since 1.0.0
 */
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class MultiplePropertyTypeListSuperPropertyElementObject {
    List<Integer> value;

    public List<Number> getValue() {
        return ImmutableList.of(Integer.MAX_VALUE);
    }
}



