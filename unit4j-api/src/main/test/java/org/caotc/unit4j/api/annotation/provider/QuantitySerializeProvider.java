package org.caotc.unit4j.api.annotation.provider;

import lombok.experimental.FieldNameConstants;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.QuantitySerialize;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.unit.Units;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2022-08-18
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class QuantitySerializeProvider {

    static Stream<Arguments> quantityFields() throws NoSuchFieldException {
        return Stream.of(Arguments.of(QuantitySerializeFieldObject.class.getDeclaredField(QuantitySerializeFieldObject.Fields.QUANTITY_FIELD)));
    }

    static Stream<Arguments> quantitySetMethods() throws NoSuchMethodException {
        return Stream.of(Arguments.of(QuantitySerializeGetMethodObject.class.getDeclaredMethod("getQuantityField")));
    }
}

@FieldNameConstants
class QuantitySerializeFieldObject {
    @QuantitySerialize
    Quantity quantityField;
}

class QuantitySerializeGetMethodObject {
    @QuantitySerialize
    public Quantity getQuantityField() {
        return Quantity.create(999, Units.METER);
    }
}
