package org.caotc.unit4j.core.common.base.provider;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.base.AccessLevel;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2022-08-18
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class AccessLevelProvider {
    static Stream<Arguments> isMoreArguments() {
        return Stream.of(Arguments.of(AccessLevel.PUBLIC, AccessLevel.PUBLIC, false),
                Arguments.of(AccessLevel.PUBLIC, AccessLevel.MODULE, true),
                Arguments.of(AccessLevel.PUBLIC, AccessLevel.PROTECTED, true),
                Arguments.of(AccessLevel.PUBLIC, AccessLevel.PACKAGE, true),
                Arguments.of(AccessLevel.PUBLIC, AccessLevel.PRIVATE, true),
                Arguments.of(AccessLevel.MODULE, AccessLevel.PUBLIC, false),
                Arguments.of(AccessLevel.MODULE, AccessLevel.MODULE, false),
                Arguments.of(AccessLevel.MODULE, AccessLevel.PROTECTED, true),
                Arguments.of(AccessLevel.MODULE, AccessLevel.PACKAGE, true),
                Arguments.of(AccessLevel.MODULE, AccessLevel.PRIVATE, true),
                Arguments.of(AccessLevel.PROTECTED, AccessLevel.PUBLIC, false),
                Arguments.of(AccessLevel.PROTECTED, AccessLevel.MODULE, false),
                Arguments.of(AccessLevel.PROTECTED, AccessLevel.PROTECTED, false),
                Arguments.of(AccessLevel.PROTECTED, AccessLevel.PACKAGE, true),
                Arguments.of(AccessLevel.PROTECTED, AccessLevel.PRIVATE, true),
                Arguments.of(AccessLevel.PACKAGE, AccessLevel.PUBLIC, false),
                Arguments.of(AccessLevel.PACKAGE, AccessLevel.MODULE, false),
                Arguments.of(AccessLevel.PACKAGE, AccessLevel.PROTECTED, false),
                Arguments.of(AccessLevel.PACKAGE, AccessLevel.PACKAGE, false),
                Arguments.of(AccessLevel.PACKAGE, AccessLevel.PRIVATE, true),
                Arguments.of(AccessLevel.PRIVATE, AccessLevel.PUBLIC, false),
                Arguments.of(AccessLevel.PRIVATE, AccessLevel.MODULE, false),
                Arguments.of(AccessLevel.PRIVATE, AccessLevel.PROTECTED, false),
                Arguments.of(AccessLevel.PRIVATE, AccessLevel.PACKAGE, false),
                Arguments.of(AccessLevel.PRIVATE, AccessLevel.PRIVATE, false));
    }

    static Stream<Arguments> isMoreOrEqualArguments() {
        return isMoreArguments()
                .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[1], ((boolean) arguments.get()[2] || Objects.equals(arguments.get()[0], arguments.get()[1]))));
    }

    static Stream<Arguments> isLessArguments() {
        return isMoreOrEqualArguments()
                .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[1], (!(boolean) arguments.get()[2])));
    }

    static Stream<Arguments> isLessOrEqualArguments() {
        return isLessArguments()
                .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[1], ((boolean) arguments.get()[2] || Objects.equals(arguments.get()[0], arguments.get()[1]))));
    }
}