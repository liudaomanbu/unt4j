package org.caotc.unit4j.core.common.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Slf4j
class AccessLevelTest {

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.base.provider.AccessLevelProvider#isMoreArguments")
    void isMore(AccessLevel accessLevel1, AccessLevel accessLevel2, boolean isMore) {
        boolean result = accessLevel1.isMore(accessLevel2);
        log.debug("{} is More {},result:{}", accessLevel1, accessLevel2, result);
        Assertions.assertEquals(isMore, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.base.provider.AccessLevelProvider#isMoreOrEqualArguments")
    void isMoreOrEqual(AccessLevel accessLevel1, AccessLevel accessLevel2, boolean isMoreOrEqual) {
        boolean result = accessLevel1.isMoreOrEqual(accessLevel2);
        log.debug("{} is isMoreOrEqual {},result:{}", accessLevel1, accessLevel2, result);
        Assertions.assertEquals(isMoreOrEqual, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.base.provider.AccessLevelProvider#isLessArguments")
    void isLess(AccessLevel accessLevel1, AccessLevel accessLevel2, boolean isLess) {
        boolean result = accessLevel1.isLess(accessLevel2);
        log.debug("{} is isLess {},result:{}", accessLevel1, accessLevel2, result);
        Assertions.assertEquals(isLess, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.base.provider.AccessLevelProvider#isLessOrEqualArguments")
    void isLessOrEqual(AccessLevel accessLevel1, AccessLevel accessLevel2, boolean isLessOrEqual) {
        boolean result = accessLevel1.isLessOrEqual(accessLevel2);
        log.debug("{} is isLess {},result:{}", accessLevel1, accessLevel2, result);
        Assertions.assertEquals(isLessOrEqual, result);
    }
}