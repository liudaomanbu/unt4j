package org.caotc.unit4j.core.common.reflect.property.provider;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.reflect.property.model.*;
import org.caotc.unit4j.core.common.util.ReflectionUtil;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Random;
import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2022-08-18
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class Provider {
    static Stream<Arguments> propertyElementOrderArguments() {
        Random random = new Random();
        int value = random.nextInt();
        int[] valueArray = new int[]{value};
        Integer[] valueWarpArray = new Integer[]{value};
        int[][] valueMultipleArray = new int[][]{{value}};
        Integer[][] valueWarpMultipleArray = new Integer[][]{{value}};
        return Stream.of(Arguments.of(ReflectionUtil.readablePropertyExact(MultipleAccessLevelPropertyElementObject.class, "value"), new MultipleAccessLevelPropertyElementObject(value), value)
                , Arguments.of(ReflectionUtil.readablePropertyExact(MultipleDeclaringTypePropertyElementObject.class, "value"), new MultipleDeclaringTypePropertyElementObject(value), value)
                , Arguments.of(ReflectionUtil.readablePropertyExact(MultiplePropertyTypeSuperPropertyElementObject.class, "value"), new MultiplePropertyTypeSuperPropertyElementObject(value), value)
                , Arguments.of(ReflectionUtil.readablePropertyExact(MultiplePropertyTypePrimitiveAndWarpSuperPropertyElementObject.class, "value"), new MultiplePropertyTypePrimitiveAndWarpSuperPropertyElementObject(value), value)
                , Arguments.of(ReflectionUtil.readablePropertyExact(MultiplePropertyTypePrimitiveAndWarpPropertyElementObject.class, "value"), new MultiplePropertyTypePrimitiveAndWarpPropertyElementObject(value), value)
                , Arguments.of(ReflectionUtil.readablePropertyExact(MultiplePropertyTypeArraySuperPropertyElementObject.class, "value"), new MultiplePropertyTypeArraySuperPropertyElementObject(valueWarpArray), valueWarpArray)
                , Arguments.of(ReflectionUtil.readablePropertyExact(MultiplePropertyTypeArrayPrimitiveAndWarpSuperPropertyElementObject.class, "value"), new MultiplePropertyTypeArrayPrimitiveAndWarpSuperPropertyElementObject(valueArray), valueArray)
                , Arguments.of(ReflectionUtil.readablePropertyExact(MultiplePropertyTypeArrayPrimitiveAndWarpPropertyElementObject.class, "value"), new MultiplePropertyTypeArrayPrimitiveAndWarpPropertyElementObject(valueArray), valueArray)
                , Arguments.of(ReflectionUtil.readablePropertyExact(MultiplePropertyTypeMultipleArraySuperPropertyElementObject.class, "value"), new MultiplePropertyTypeMultipleArraySuperPropertyElementObject(valueWarpMultipleArray), valueWarpMultipleArray)
                , Arguments.of(ReflectionUtil.readablePropertyExact(MultiplePropertyTypeMultipleArrayPrimitiveAndWarpSuperPropertyElementObject.class, "value"), new MultiplePropertyTypeMultipleArrayPrimitiveAndWarpSuperPropertyElementObject(valueMultipleArray), valueMultipleArray)
                , Arguments.of(ReflectionUtil.readablePropertyExact(MultiplePropertyTypeMultipleArrayPrimitiveAndWarpPropertyElementObject.class, "value"), new MultiplePropertyTypeMultipleArrayPrimitiveAndWarpPropertyElementObject(valueMultipleArray), valueMultipleArray));
    }
}