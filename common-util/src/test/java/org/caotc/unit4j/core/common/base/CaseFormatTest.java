package org.caotc.unit4j.core.common.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

@Slf4j
class CaseFormatTest {

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.base.provider.CaseFormatProvider#toArguments")
    void to(CaseFormat sourceFormat, CaseFormat targetFormat, String source, String target) {
        String result = sourceFormat.to(targetFormat, source);
        log.debug("sourceFormat:{},targetFormat:{},source:{},result:{}", sourceFormat, targetFormat, source, result);
        Assertions.assertEquals(target, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.base.provider.CaseFormatProvider#splitToArguments")
    void split(CaseFormat format, String str, List<String> words) {
        List<String> result = format.split(str);
        log.debug("format:{},str:{},result:{}", format, str, result);
        Assertions.assertEquals(words, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.base.provider.CaseFormatProvider#joinToArguments")
    void join(CaseFormat format, List<String> words, String str) {
        String result = format.join(words);
        log.debug("format:{},words:{},result:{}", format, words, result);
        Assertions.assertEquals(str, result);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.core.common.base.provider.CaseFormatProvider#joinToArrayArguments")
    void join(CaseFormat format, String[] words, String str) {
        String result = format.join(words);
        log.debug("format:{},words:{},result:{}", format, words, result);
        Assertions.assertEquals(str, result);
    }

}