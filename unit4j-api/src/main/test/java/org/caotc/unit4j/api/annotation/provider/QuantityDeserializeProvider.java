package org.caotc.unit4j.api.annotation.provider;

import lombok.experimental.FieldNameConstants;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.QuantityDeserialize;
import org.caotc.unit4j.core.Quantity;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2022-08-18
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class QuantityDeserializeProvider {

    static Stream<Arguments> quantityFields() throws NoSuchFieldException {
        return Stream.of(Arguments.of(QuantityDeserializeFieldObject.class.getDeclaredField(QuantityDeserializeFieldObject.Fields.QUANTITY_FIELD)));
    }

    static Stream<Arguments> quantitySetMethods() throws NoSuchMethodException {
        return Stream.of(Arguments.of(QuantityDeserializeSetMethodObject.class.getDeclaredMethod("setQuantityField", Quantity.class)));
    }
}

@FieldNameConstants
class QuantityDeserializeFieldObject {
    @QuantityDeserialize
    Quantity quantityField;
}

class QuantityDeserializeSetMethodObject {
    @QuantityDeserialize
    public void setQuantityField(Quantity quantity) {

    }
}
