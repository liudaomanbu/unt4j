package org.caotc.unit4j.api.annotation.provider;

import lombok.experimental.FieldNameConstants;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.DataType;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2022-08-18
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class DataTypeProvider {

    static Stream<Arguments> dataTypeFieldAndValues() throws NoSuchFieldException {
        return Stream.of(Arguments.of(DataTypeFieldObject.class.getDeclaredField(DataTypeFieldObject.Fields.DATA_TYPE_FIELD), "LENGTH"));
    }

    static Stream<Arguments> dataTypeGetMethodAndValues() throws NoSuchMethodException {
        return Stream.of(Arguments.of(DataTypeGetMethodObject.class.getDeclaredMethod("getDataTypeField"), "LENGTH"));
    }
}

@FieldNameConstants
class DataTypeFieldObject {
    @DataType("LENGTH")
    Integer dataTypeField;
}

class DataTypeGetMethodObject {
    @DataType("LENGTH")
    public Integer getDataTypeField() {
        return 0;
    }
}
