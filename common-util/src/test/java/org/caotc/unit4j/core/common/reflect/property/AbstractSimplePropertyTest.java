package org.caotc.unit4j.core.common.reflect.property;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;

@Slf4j
class AbstractSimplePropertyTest {
    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.reflect.property.provider.Provider#propertyElementOrderArguments")
    <O> void propertyElementOrder(ReadableProperty<O, ?> readableProperty, O target, Object value) {
        Optional<?> result = readableProperty.read(target);
        log.debug("readableProperty:{},target:{},result:{}", readableProperty, target, result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(value, result.get());
    }
}