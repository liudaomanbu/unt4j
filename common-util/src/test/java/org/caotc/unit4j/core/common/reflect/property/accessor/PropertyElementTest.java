package org.caotc.unit4j.core.common.reflect.property.accessor;

import com.google.common.collect.Iterables;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.util.ReflectionUtil;
import org.junit.jupiter.api.Test;

@Slf4j
class PropertyElementTest {

    @Test
    void propertyType() {
        PropertyAccessor<B, Object> propertyAccessor = Iterables.getFirst(ReflectionUtil.propertyAccessors(B.class), null);
        PropertyAccessor<B, Number> propertyAccessor1 = propertyAccessor.propertyType(Number.class);

//        PropertyAccessor<B, Integer> propertyAccessor2 = propertyAccessor1.propertyType(Integer.class);
//        Optional<Integer> optional=propertyAccessor2.read(new B(1.1111));
//        Integer value=optional.get();
//        log.info("value:{}",value);
        PropertyAccessor<B, Object> propertyAccessor3 = propertyAccessor.propertyType(Object.class);
    }

    static class A {

    }

    @AllArgsConstructor
    static class B extends A {
        Number number;
    }
}