/*
 * Copyright (C) 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.caotc.unit4j.core.common.base.provider;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.base.CaseFormat;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2022-08-18
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class CaseFormatProvider {
    private static final String LOWER_HYPHEN_STRING = "case-format";
    private static final String UPPER_HYPHEN_STRING = "CASE-FORMAT";
    private static final String LOWER_UNDERSCORE_STRING = "case_format";
    private static final String UPPER_UNDERSCORE_STRING = "CASE_FORMAT";
    private static final String LOWER_CAMEL_STRING = "caseFormat";
    private static final String UPPER_CAMEL_STRING = "CaseFormat";
    private static final String LOWER_SINGLE_STRING = "case";
    private static final String UPPER_SINGLE_STRING = "CASE";
    private static final String UPPER_CAMEL_SINGLE_STRING = "Case";

    static Stream<Arguments> matchSplitArguments() {
        return Stream.of(Arguments.of(LOWER_HYPHEN_STRING, Lists.newArrayList("case", "format")),
                Arguments.of(UPPER_HYPHEN_STRING, Lists.newArrayList("CASE", "FORMAT")),
                Arguments.of(LOWER_UNDERSCORE_STRING, Lists.newArrayList("case", "format")),
                Arguments.of(UPPER_UNDERSCORE_STRING, Lists.newArrayList("CASE", "FORMAT")),
                Arguments.of(LOWER_CAMEL_STRING, Lists.newArrayList("case", "Format")),
                Arguments.of(UPPER_CAMEL_STRING, Lists.newArrayList("Case", "Format")),
                Arguments.of(LOWER_SINGLE_STRING, Lists.newArrayList(LOWER_SINGLE_STRING)),
                Arguments.of(UPPER_SINGLE_STRING, Lists.newArrayList(UPPER_SINGLE_STRING)),
                Arguments.of(UPPER_CAMEL_SINGLE_STRING, Lists.newArrayList(UPPER_CAMEL_SINGLE_STRING)),
                Arguments.of("A", Lists.newArrayList("A"))
        );
    }

    static Stream<Arguments> matchesArguments() {
        return Stream.of(Arguments.of(CaseFormat.LOWER_HYPHEN, LOWER_HYPHEN_STRING, true),
                Arguments.of(CaseFormat.LOWER_HYPHEN, LOWER_SINGLE_STRING, true),
                Arguments.of(CaseFormat.LOWER_HYPHEN, "a", true),
                Arguments.of(CaseFormat.LOWER_HYPHEN, "a-b", true),

                Arguments.of(CaseFormat.UPPER_HYPHEN, UPPER_HYPHEN_STRING, true),
                Arguments.of(CaseFormat.UPPER_HYPHEN, UPPER_SINGLE_STRING, true),
                Arguments.of(CaseFormat.UPPER_HYPHEN, "A", true),
                Arguments.of(CaseFormat.UPPER_HYPHEN, "A-B", true),

                Arguments.of(CaseFormat.LOWER_UNDERSCORE, LOWER_UNDERSCORE_STRING, true),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, LOWER_SINGLE_STRING, true),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, "a", true),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, "a_b", true),

                Arguments.of(CaseFormat.UPPER_UNDERSCORE, UPPER_UNDERSCORE_STRING, true),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, UPPER_SINGLE_STRING, true),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, "A", true),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, "A_B", true),

                Arguments.of(CaseFormat.LOWER_CAMEL, LOWER_CAMEL_STRING, true),
                Arguments.of(CaseFormat.LOWER_CAMEL, LOWER_SINGLE_STRING, true),
                Arguments.of(CaseFormat.LOWER_CAMEL, "a", true),

                Arguments.of(CaseFormat.UPPER_CAMEL, UPPER_CAMEL_STRING, true),
                Arguments.of(CaseFormat.UPPER_CAMEL, UPPER_CAMEL_SINGLE_STRING, true),
                Arguments.of(CaseFormat.UPPER_CAMEL, "A", true),

                Arguments.of(CaseFormat.LOWER_HYPHEN, "A", false),
                Arguments.of(CaseFormat.LOWER_HYPHEN, "aB", false),
                Arguments.of(CaseFormat.LOWER_HYPHEN, "-", false),
                Arguments.of(CaseFormat.LOWER_HYPHEN, "-ab", false),
                Arguments.of(CaseFormat.LOWER_HYPHEN, "ab-", false),
                Arguments.of(CaseFormat.LOWER_HYPHEN, "ab--cd", false),

                Arguments.of(CaseFormat.UPPER_HYPHEN, "a", false),
                Arguments.of(CaseFormat.UPPER_HYPHEN, "aB", false),
                Arguments.of(CaseFormat.UPPER_HYPHEN, "-", false),
                Arguments.of(CaseFormat.UPPER_HYPHEN, "-AB", false),
                Arguments.of(CaseFormat.UPPER_HYPHEN, "AB-", false),
                Arguments.of(CaseFormat.UPPER_HYPHEN, "AB--CD", false),

                Arguments.of(CaseFormat.LOWER_UNDERSCORE, "A", false),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, "aB", false),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, "_", false),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, "_ab", false),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, "ab_", false),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, "ab__cd", false),

                Arguments.of(CaseFormat.UPPER_UNDERSCORE, "a", false),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, "aB", false),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, "_", false),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, "_AB", false),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, "AB_", false),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, "AB__CD", false),

                Arguments.of(CaseFormat.LOWER_CAMEL, "A", false),
                Arguments.of(CaseFormat.LOWER_CAMEL, "ABC", false),
                Arguments.of(CaseFormat.LOWER_CAMEL, "ab-Cd", false),
                Arguments.of(CaseFormat.LOWER_CAMEL, "ab_Cd", false),

                Arguments.of(CaseFormat.UPPER_CAMEL, "a", false),
                Arguments.of(CaseFormat.UPPER_CAMEL, "ABC", false),
                Arguments.of(CaseFormat.UPPER_CAMEL, "Ab-Cd", false),
                Arguments.of(CaseFormat.UPPER_CAMEL, "Ab_Cd", false)
        );
    }

    static Stream<Arguments> toArguments() {
        return Stream.of(Arguments.of(CaseFormat.LOWER_HYPHEN, CaseFormat.LOWER_HYPHEN, LOWER_HYPHEN_STRING, LOWER_HYPHEN_STRING),
                Arguments.of(CaseFormat.LOWER_HYPHEN, CaseFormat.UPPER_HYPHEN, LOWER_HYPHEN_STRING, UPPER_HYPHEN_STRING),
                Arguments.of(CaseFormat.LOWER_HYPHEN, CaseFormat.LOWER_UNDERSCORE, LOWER_HYPHEN_STRING, LOWER_UNDERSCORE_STRING),
                Arguments.of(CaseFormat.LOWER_HYPHEN, CaseFormat.UPPER_UNDERSCORE, LOWER_HYPHEN_STRING, UPPER_UNDERSCORE_STRING),
                Arguments.of(CaseFormat.LOWER_HYPHEN, CaseFormat.LOWER_CAMEL, LOWER_HYPHEN_STRING, LOWER_CAMEL_STRING),
                Arguments.of(CaseFormat.LOWER_HYPHEN, CaseFormat.UPPER_CAMEL, LOWER_HYPHEN_STRING, UPPER_CAMEL_STRING),
                Arguments.of(CaseFormat.UPPER_HYPHEN, CaseFormat.LOWER_HYPHEN, UPPER_HYPHEN_STRING, LOWER_HYPHEN_STRING),
                Arguments.of(CaseFormat.UPPER_HYPHEN, CaseFormat.UPPER_HYPHEN, UPPER_HYPHEN_STRING, UPPER_HYPHEN_STRING),
                Arguments.of(CaseFormat.UPPER_HYPHEN, CaseFormat.LOWER_UNDERSCORE, UPPER_HYPHEN_STRING, LOWER_UNDERSCORE_STRING),
                Arguments.of(CaseFormat.UPPER_HYPHEN, CaseFormat.UPPER_UNDERSCORE, UPPER_HYPHEN_STRING, UPPER_UNDERSCORE_STRING),
                Arguments.of(CaseFormat.UPPER_HYPHEN, CaseFormat.LOWER_CAMEL, UPPER_HYPHEN_STRING, LOWER_CAMEL_STRING),
                Arguments.of(CaseFormat.UPPER_HYPHEN, CaseFormat.UPPER_CAMEL, UPPER_HYPHEN_STRING, UPPER_CAMEL_STRING),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, CaseFormat.LOWER_HYPHEN, LOWER_UNDERSCORE_STRING, LOWER_HYPHEN_STRING),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, CaseFormat.UPPER_HYPHEN, LOWER_UNDERSCORE_STRING, UPPER_HYPHEN_STRING),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, CaseFormat.LOWER_UNDERSCORE, LOWER_UNDERSCORE_STRING, LOWER_UNDERSCORE_STRING),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, CaseFormat.UPPER_UNDERSCORE, LOWER_UNDERSCORE_STRING, UPPER_UNDERSCORE_STRING),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, CaseFormat.LOWER_CAMEL, LOWER_UNDERSCORE_STRING, LOWER_CAMEL_STRING),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, CaseFormat.UPPER_CAMEL, LOWER_UNDERSCORE_STRING, UPPER_CAMEL_STRING),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, CaseFormat.LOWER_HYPHEN, UPPER_UNDERSCORE_STRING, LOWER_HYPHEN_STRING),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, CaseFormat.UPPER_HYPHEN, UPPER_UNDERSCORE_STRING, UPPER_HYPHEN_STRING),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, CaseFormat.LOWER_UNDERSCORE, UPPER_UNDERSCORE_STRING, LOWER_UNDERSCORE_STRING),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, CaseFormat.UPPER_UNDERSCORE, UPPER_UNDERSCORE_STRING, UPPER_UNDERSCORE_STRING),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, CaseFormat.LOWER_CAMEL, UPPER_UNDERSCORE_STRING, LOWER_CAMEL_STRING),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, CaseFormat.UPPER_CAMEL, UPPER_UNDERSCORE_STRING, UPPER_CAMEL_STRING),
                Arguments.of(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_HYPHEN, LOWER_CAMEL_STRING, LOWER_HYPHEN_STRING),
                Arguments.of(CaseFormat.LOWER_CAMEL, CaseFormat.UPPER_HYPHEN, LOWER_CAMEL_STRING, UPPER_HYPHEN_STRING),
                Arguments.of(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_UNDERSCORE, LOWER_CAMEL_STRING, LOWER_UNDERSCORE_STRING),
                Arguments.of(CaseFormat.LOWER_CAMEL, CaseFormat.UPPER_UNDERSCORE, LOWER_CAMEL_STRING, UPPER_UNDERSCORE_STRING),
                Arguments.of(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_CAMEL, LOWER_CAMEL_STRING, LOWER_CAMEL_STRING),
                Arguments.of(CaseFormat.LOWER_CAMEL, CaseFormat.UPPER_CAMEL, LOWER_CAMEL_STRING, UPPER_CAMEL_STRING),
                Arguments.of(CaseFormat.UPPER_CAMEL, CaseFormat.LOWER_HYPHEN, UPPER_CAMEL_STRING, LOWER_HYPHEN_STRING),
                Arguments.of(CaseFormat.UPPER_CAMEL, CaseFormat.UPPER_HYPHEN, UPPER_CAMEL_STRING, UPPER_HYPHEN_STRING),
                Arguments.of(CaseFormat.UPPER_CAMEL, CaseFormat.LOWER_UNDERSCORE, UPPER_CAMEL_STRING, LOWER_UNDERSCORE_STRING),
                Arguments.of(CaseFormat.UPPER_CAMEL, CaseFormat.UPPER_UNDERSCORE, UPPER_CAMEL_STRING, UPPER_UNDERSCORE_STRING),
                Arguments.of(CaseFormat.UPPER_CAMEL, CaseFormat.LOWER_CAMEL, UPPER_CAMEL_STRING, LOWER_CAMEL_STRING),
                Arguments.of(CaseFormat.UPPER_CAMEL, CaseFormat.UPPER_CAMEL, UPPER_CAMEL_STRING, UPPER_CAMEL_STRING));
    }

    static Stream<Arguments> splitArguments() {
        return Stream.of(Arguments.of(CaseFormat.LOWER_HYPHEN, LOWER_HYPHEN_STRING, Lists.newArrayList("case", "format")),
                Arguments.of(CaseFormat.UPPER_HYPHEN, UPPER_HYPHEN_STRING, Lists.newArrayList("CASE", "FORMAT")),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, LOWER_UNDERSCORE_STRING, Lists.newArrayList("case", "format")),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, UPPER_UNDERSCORE_STRING, Lists.newArrayList("CASE", "FORMAT")),
                Arguments.of(CaseFormat.LOWER_CAMEL, LOWER_CAMEL_STRING, Lists.newArrayList("case", "Format")),
                Arguments.of(CaseFormat.UPPER_CAMEL, UPPER_CAMEL_STRING, Lists.newArrayList("Case", "Format")),

                Arguments.of(CaseFormat.LOWER_HYPHEN, UPPER_HYPHEN_STRING, Lists.newArrayList("CASE", "FORMAT")),
                Arguments.of(CaseFormat.LOWER_HYPHEN, LOWER_UNDERSCORE_STRING, Lists.newArrayList(LOWER_UNDERSCORE_STRING)),
                Arguments.of(CaseFormat.LOWER_HYPHEN, UPPER_UNDERSCORE_STRING, Lists.newArrayList(UPPER_UNDERSCORE_STRING)),
                Arguments.of(CaseFormat.LOWER_HYPHEN, LOWER_CAMEL_STRING, Lists.newArrayList(LOWER_CAMEL_STRING)),
                Arguments.of(CaseFormat.LOWER_HYPHEN, UPPER_CAMEL_STRING, Lists.newArrayList(UPPER_CAMEL_STRING)),

                Arguments.of(CaseFormat.UPPER_HYPHEN, LOWER_HYPHEN_STRING, Lists.newArrayList("case", "format")),
                Arguments.of(CaseFormat.UPPER_HYPHEN, LOWER_UNDERSCORE_STRING, Lists.newArrayList(LOWER_UNDERSCORE_STRING)),
                Arguments.of(CaseFormat.UPPER_HYPHEN, UPPER_UNDERSCORE_STRING, Lists.newArrayList(UPPER_UNDERSCORE_STRING)),
                Arguments.of(CaseFormat.UPPER_HYPHEN, LOWER_CAMEL_STRING, Lists.newArrayList(LOWER_CAMEL_STRING)),
                Arguments.of(CaseFormat.UPPER_HYPHEN, UPPER_CAMEL_STRING, Lists.newArrayList(UPPER_CAMEL_STRING)),

                Arguments.of(CaseFormat.LOWER_UNDERSCORE, LOWER_HYPHEN_STRING, Lists.newArrayList(LOWER_HYPHEN_STRING)),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, UPPER_HYPHEN_STRING, Lists.newArrayList(UPPER_HYPHEN_STRING)),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, UPPER_UNDERSCORE_STRING, Lists.newArrayList("CASE", "FORMAT")),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, LOWER_CAMEL_STRING, Lists.newArrayList(LOWER_CAMEL_STRING)),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, UPPER_CAMEL_STRING, Lists.newArrayList(UPPER_CAMEL_STRING)),

                Arguments.of(CaseFormat.UPPER_UNDERSCORE, LOWER_HYPHEN_STRING, Lists.newArrayList(LOWER_HYPHEN_STRING)),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, UPPER_HYPHEN_STRING, Lists.newArrayList(UPPER_HYPHEN_STRING)),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, LOWER_UNDERSCORE_STRING, Lists.newArrayList("case", "format")),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, LOWER_CAMEL_STRING, Lists.newArrayList(LOWER_CAMEL_STRING)),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, UPPER_CAMEL_STRING, Lists.newArrayList(UPPER_CAMEL_STRING)),

                Arguments.of(CaseFormat.LOWER_CAMEL, LOWER_HYPHEN_STRING, Lists.newArrayList(LOWER_HYPHEN_STRING)),
                Arguments.of(CaseFormat.LOWER_CAMEL, UPPER_HYPHEN_STRING, Lists.newArrayList("C", "A", "S", "E-", "F", "O", "R", "M", "A", "T")),
                Arguments.of(CaseFormat.LOWER_CAMEL, LOWER_UNDERSCORE_STRING, Lists.newArrayList(LOWER_UNDERSCORE_STRING)),
                Arguments.of(CaseFormat.LOWER_CAMEL, UPPER_UNDERSCORE_STRING, Lists.newArrayList("C", "A", "S", "E_", "F", "O", "R", "M", "A", "T")),
                Arguments.of(CaseFormat.LOWER_CAMEL, UPPER_CAMEL_STRING, Lists.newArrayList("Case", "Format")),

                Arguments.of(CaseFormat.UPPER_CAMEL, LOWER_HYPHEN_STRING, Lists.newArrayList(LOWER_HYPHEN_STRING)),
                Arguments.of(CaseFormat.UPPER_CAMEL, UPPER_HYPHEN_STRING, Lists.newArrayList("C", "A", "S", "E-", "F", "O", "R", "M", "A", "T")),
                Arguments.of(CaseFormat.UPPER_CAMEL, LOWER_UNDERSCORE_STRING, Lists.newArrayList(LOWER_UNDERSCORE_STRING)),
                Arguments.of(CaseFormat.UPPER_CAMEL, UPPER_UNDERSCORE_STRING, Lists.newArrayList("C", "A", "S", "E_", "F", "O", "R", "M", "A", "T")),
                Arguments.of(CaseFormat.UPPER_CAMEL, LOWER_CAMEL_STRING, Lists.newArrayList("case", "Format")),

                Arguments.of(CaseFormat.LOWER_HYPHEN, LOWER_SINGLE_STRING, Lists.newArrayList(LOWER_SINGLE_STRING)),
                Arguments.of(CaseFormat.LOWER_HYPHEN, UPPER_SINGLE_STRING, Lists.newArrayList(UPPER_SINGLE_STRING)),
                Arguments.of(CaseFormat.LOWER_HYPHEN, UPPER_CAMEL_SINGLE_STRING, Lists.newArrayList(UPPER_CAMEL_SINGLE_STRING)),
                Arguments.of(CaseFormat.UPPER_HYPHEN, LOWER_SINGLE_STRING, Lists.newArrayList(LOWER_SINGLE_STRING)),
                Arguments.of(CaseFormat.UPPER_HYPHEN, UPPER_SINGLE_STRING, Lists.newArrayList(UPPER_SINGLE_STRING)),
                Arguments.of(CaseFormat.UPPER_HYPHEN, UPPER_CAMEL_SINGLE_STRING, Lists.newArrayList(UPPER_CAMEL_SINGLE_STRING)),

                Arguments.of(CaseFormat.LOWER_UNDERSCORE, LOWER_SINGLE_STRING, Lists.newArrayList(LOWER_SINGLE_STRING)),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, UPPER_SINGLE_STRING, Lists.newArrayList(UPPER_SINGLE_STRING)),
                Arguments.of(CaseFormat.LOWER_UNDERSCORE, UPPER_CAMEL_SINGLE_STRING, Lists.newArrayList(UPPER_CAMEL_SINGLE_STRING)),

                Arguments.of(CaseFormat.UPPER_UNDERSCORE, LOWER_SINGLE_STRING, Lists.newArrayList(LOWER_SINGLE_STRING)),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, UPPER_SINGLE_STRING, Lists.newArrayList(UPPER_SINGLE_STRING)),
                Arguments.of(CaseFormat.UPPER_UNDERSCORE, UPPER_CAMEL_SINGLE_STRING, Lists.newArrayList(UPPER_CAMEL_SINGLE_STRING)),

                Arguments.of(CaseFormat.LOWER_CAMEL, LOWER_SINGLE_STRING, Lists.newArrayList(LOWER_SINGLE_STRING)),
                Arguments.of(CaseFormat.LOWER_CAMEL, UPPER_SINGLE_STRING, Lists.newArrayList("C", "A", "S", "E")),
                Arguments.of(CaseFormat.LOWER_CAMEL, UPPER_CAMEL_SINGLE_STRING, Lists.newArrayList(UPPER_CAMEL_SINGLE_STRING)),

                Arguments.of(CaseFormat.UPPER_CAMEL, LOWER_SINGLE_STRING, Lists.newArrayList(LOWER_SINGLE_STRING)),
                Arguments.of(CaseFormat.UPPER_CAMEL, UPPER_SINGLE_STRING, Lists.newArrayList("C", "A", "S", "E")),
                Arguments.of(CaseFormat.UPPER_CAMEL, UPPER_CAMEL_SINGLE_STRING, Lists.newArrayList(UPPER_CAMEL_SINGLE_STRING)));
    }

    static Stream<Arguments> joinToArguments() {
        return Stream.of(Arguments.of(CaseFormat.LOWER_HYPHEN, LOWER_HYPHEN_STRING),
                        Arguments.of(CaseFormat.UPPER_HYPHEN, UPPER_HYPHEN_STRING),
                        Arguments.of(CaseFormat.LOWER_UNDERSCORE, LOWER_UNDERSCORE_STRING),
                        Arguments.of(CaseFormat.UPPER_UNDERSCORE, UPPER_UNDERSCORE_STRING),
                        Arguments.of(CaseFormat.LOWER_CAMEL, LOWER_CAMEL_STRING),
                        Arguments.of(CaseFormat.UPPER_CAMEL, UPPER_CAMEL_STRING))
                .flatMap(arguments -> Stream.of(Arguments.of(arguments.get()[0], Lists.newArrayList("case", "format"), arguments.get()[1]),
                        Arguments.of(arguments.get()[0], Lists.newArrayList("CASE", "FORMAT"), arguments.get()[1]),
                        Arguments.of(arguments.get()[0], Lists.newArrayList("case", "Format"), arguments.get()[1]),
                        Arguments.of(arguments.get()[0], Lists.newArrayList("Case", "Format"), arguments.get()[1])));
    }

    @SuppressWarnings({"unchecked"})
    static Stream<Arguments> joinToArrayArguments() {
        return joinToArguments()
                .map(arguments -> Arguments.of(arguments.get()[0], ((List<String>) arguments.get()[1]).toArray(new String[0]), arguments.get()[2]));
    }
}