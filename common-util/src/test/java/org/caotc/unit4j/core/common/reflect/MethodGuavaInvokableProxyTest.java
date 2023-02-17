package org.caotc.unit4j.core.common.reflect;

import com.google.common.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Slf4j
class MethodGuavaInvokableProxyTest {

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.reflect.provider.Provider#overriddenInvokableAndOverridingInvokable")
    void isOverridden(MethodInvokable<?, ?> overriddenInvokable, MethodInvokable<?, ?> overridingInvokable) {
        boolean result = overriddenInvokable.isOverridden(overridingInvokable);
        log.debug("overriddenInvokable:{},overridingInvokable:{},result:{}", overriddenInvokable, overridingInvokable, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.reflect.provider.Provider#invokableAndNotOverridingInvokable")
    void notIsOverridden(MethodInvokable<?, ?> invokable, MethodInvokable<?, ?> notOverridingInvokable) {
        boolean result = invokable.isOverridden(notOverridingInvokable);
        log.debug("invokable:{},notOverridingInvokable:{},result:{}", invokable, notOverridingInvokable, result);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.reflect.provider.Provider#overriddenInvokableAndOverridingInvokable")
    void isOverriding(MethodInvokable<?, ?> overriddenInvokable, MethodInvokable<?, ?> overridingInvokable) {
        boolean result = overridingInvokable.isOverriding(overriddenInvokable);
        log.debug("overriddenInvokable:{},overridingInvokable:{},result:{}", overriddenInvokable, overridingInvokable, result);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.reflect.provider.Provider#invokableAndNotOverridingInvokable")
    void notIsOverriding(MethodInvokable<?, ?> invokable, MethodInvokable<?, ?> notOverridingInvokable) {
        boolean result = notOverridingInvokable.isOverriding(invokable);
        log.debug("invokable:{},notOverridingInvokable:{},result:{}", invokable, notOverridingInvokable, result);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.reflect.provider.Provider#invokableAndOverridableType")
    void isOverridableIn(MethodInvokable<?, ?> invokable, TypeToken<?> type) {
        boolean result = invokable.isOverridableIn(type);
        log.debug("invokable:{},type:{},result:{}", invokable, type, result);
        Assertions.assertTrue(result);
    }
}