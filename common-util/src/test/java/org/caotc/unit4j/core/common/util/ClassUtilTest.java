package org.caotc.unit4j.core.common.util;

import com.google.common.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;

@Slf4j
class ClassUtilTest {
    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classArrayAndLowestCommonSuperClassSets")
    void lowestCommonSuperclasses(Class<?>[] classes, Set<Class<?>> lowestCommonAncestors) {
        Set<Class<?>> result = ClassUtil.lowestCommonSuperclasses(classes);
        log.debug("classes:{},result:{}", classes, result);
        Assertions.assertEquals(lowestCommonAncestors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classSetAndLowestCommonSuperClassSets")
    void lowestCommonSuperclasses(Iterable<Class<?>> classes, Set<Class<?>> lowestCommonAncestors) {
        Set<Class<?>> result = ClassUtil.lowestCommonSuperclasses(classes);
        log.debug("classes:{},result:{}", classes, result);
        Assertions.assertEquals(lowestCommonAncestors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#classArrayAndLowestCommonAncestorSets")
    void lowestCommonAncestors(Class<?>[] classes, Set<TypeToken<?>> lowestCommonAncestors) {
        Set<TypeToken<?>> result = ClassUtil.lowestCommonAncestors(classes);
        log.debug("classes:{},result:{}", classes, result);
        Assertions.assertEquals(lowestCommonAncestors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenArrayAndLowestCommonAncestorSets")
    void lowestCommonAncestors(TypeToken<?>[] types, Set<TypeToken<?>> lowestCommonAncestors) {
        Set<TypeToken<?>> result = ClassUtil.lowestCommonAncestors(types);
        log.debug("types:{},result:{}", types, result);
        Assertions.assertEquals(lowestCommonAncestors, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.util.provider.Provider#typeTokenSetAndLowestCommonAncestorSets")
    void lowestCommonAncestors(Set<TypeToken<?>> types, Set<TypeToken<?>> lowestCommonAncestors) {
        Set<TypeToken<?>> result = ClassUtil.lowestCommonAncestors(types);
        log.debug("types:{},result:{}", types, result);
        Assertions.assertEquals(lowestCommonAncestors, result);
    }
}