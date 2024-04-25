package org.caotc.unit4j.api.annotation.provider;

import lombok.experimental.FieldNameConstants;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.WithUnit;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2022-08-18
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class WithUnitProvider {

    static Stream<Arguments> withUnitFieldAndValues() throws NoSuchFieldException {
        return Stream.of(Arguments.of(WithUnitFieldObject.class.getDeclaredField(WithUnitFieldObject.Fields.WITH_UNIT_FIELD), "METER"));
    }

    static Stream<Arguments> withUnitGetMethodAndValues() throws NoSuchMethodException {
        return Stream.of(Arguments.of(WithUniteGetMethodObject.class.getDeclaredMethod("getWithUnitField"), "METER"));
    }
}

@FieldNameConstants
class WithUnitFieldObject {
    @WithUnit("METER")
    Integer withUnitField;
}

class WithUniteGetMethodObject {
    @WithUnit("METER")
    public Integer getWithUnitField() {
        return 0;
    }
}
