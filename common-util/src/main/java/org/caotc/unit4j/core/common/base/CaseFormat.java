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

package org.caotc.unit4j.core.common.base;

import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * @author caotc
 * @date 2019-06-05
 * @see com.google.common.base.CaseFormat
 * @since 1.0.0
 */
@AllArgsConstructor
public enum CaseFormat {
    /**
     * {@link com.google.common.base.CaseFormat#LOWER_HYPHEN}
     */
    LOWER_HYPHEN(CharMatcher.is('-'), "-") {
        @Override
        String normalizeWord(String word) {
            return Ascii.toLowerCase(word);
        }
    },
    UPPER_HYPHEN(CharMatcher.is('-'), "-") {
        @Override
        String normalizeWord(String word) {
            return Ascii.toUpperCase(word);
        }
    },
    /**
     * {@link com.google.common.base.CaseFormat#LOWER_UNDERSCORE}
     */
    LOWER_UNDERSCORE(CharMatcher.is('_'), "_") {
        @Override
        String normalizeWord(String word) {
            return Ascii.toLowerCase(word);
        }
    },
    /**
     * {@link com.google.common.base.CaseFormat#UPPER_UNDERSCORE}
     */
    UPPER_UNDERSCORE(CharMatcher.is('_'), "_") {
        @Override
        String normalizeWord(String word) {
            return Ascii.toUpperCase(word);
        }
    },
    /**
     * {@link com.google.common.base.CaseFormat#LOWER_CAMEL}
     */
    LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
        @Override
        String normalizeWord(String word) {
            return firstCharOnlyToUpper(word);
        }

        @Override
        String normalizeFirstWord(String word) {
            return Ascii.toLowerCase(word);
        }
    },
    /**
     * {@link com.google.common.base.CaseFormat#UPPER_CAMEL}
     */
    UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
        @Override
        String normalizeWord(String word) {
            return firstCharOnlyToUpper(word);
        }
    };

    @NonNull
    private static final CharMatcher NOT_LOWER_MATCHER = CharMatcher.forPredicate(CharMatcher.inRange('a', 'z')).negate();
    @NonNull
    private static final CharMatcher NOT_UPPER_MATCHER = CharMatcher.forPredicate(CharMatcher.inRange('A', 'Z')).negate();

    private static String firstCharOnlyToUpper(String word) {
        return word.isEmpty()
                ? word
                : Ascii.toUpperCase(word.charAt(0)) + Ascii.toLowerCase(word.substring(1));
    }

    @NonNull
    CharMatcher wordBoundary;
    @NonNull
    String wordSeparator;

    public boolean matches(@NonNull String string) {
        char[] chars = string.toCharArray();
        //[currentWordStartIndex,currentWordEndIndex)
        int currentWordStartIndex = 0;
        int currentWordEndIndex;
        for (int i = 0; i < chars.length; i++) {
            if (wordBoundary.matches(chars[i])) {
                currentWordEndIndex = i;
                if (currentWordStartIndex >= currentWordEndIndex) {
                    //大驼峰可以大写字母开头,但是中横线或者下划线风格不能以分隔符开头
                    if (i != 0 || wordSeparator.length() > 0) {
                        return false;
                    }
                }
                currentWordStartIndex = i + wordSeparator.length();
            }
        }
        currentWordEndIndex = chars.length;
        return currentWordStartIndex < currentWordEndIndex;
    }

    public static void main(String[] args) {
        System.out.println(!LOWER_HYPHEN.matches("-"));
        System.out.println(!LOWER_HYPHEN.matches("--"));
        System.out.println(!LOWER_HYPHEN.matches("-abc"));
        System.out.println(!LOWER_HYPHEN.matches("-abc-"));
        System.out.println(!LOWER_HYPHEN.matches("-abc--"));
        System.out.println(!LOWER_HYPHEN.matches("abc--abc"));
        System.out.println(!LOWER_HYPHEN.matches("-abc--abc"));
        System.out.println(LOWER_HYPHEN.matches("a"));
        System.out.println(LOWER_HYPHEN.matches("a-b"));
        System.out.println(LOWER_HYPHEN.matches("abc"));
        System.out.println(LOWER_HYPHEN.matches("abc-def"));

        System.out.println(LOWER_CAMEL.matches("a"));
        System.out.println(LOWER_CAMEL.matches("ab"));
        System.out.println(LOWER_CAMEL.matches("aB"));
        System.out.println(LOWER_CAMEL.matches("aBc"));
        System.out.println(LOWER_CAMEL.matches("abCd"));
        System.out.println(LOWER_CAMEL.matches("A"));
        System.out.println(LOWER_CAMEL.matches("ABC"));
    }

    /**
     * 将名称从{@code this}格式转换为{@code targetFormat}格式
     *
     * @param targetFormat 目标格式
     * @param str          需要转换格式的名称
     * @return 转换格式后的名称
     * @author caotc
     * @date 2019-06-06
     * @see com.google.common.base.CaseFormat#to(com.google.common.base.CaseFormat, String)
     * @since 1.0.0
     */
    @NonNull
    public String to(@NonNull CaseFormat targetFormat, @NonNull String str) {
        return (targetFormat == this) ? str : convert(targetFormat, str);
    }

    /**
     * 将传入名称按照当前格式拆分为单词集合
     *
     * @param s 需要拆分的名称
     * @return 组成名称的单词集合
     * @author caotc
     * @date 2019-06-06
     * @since 1.0.0
     */
    @NonNull
    public List<String> split(@NonNull String s) {
        List<String> words = Lists.newArrayList();
        int i = 0;
        int j = -1;
        while ((j = wordBoundary.indexIn(s, ++j)) != -1) {
            if (j != 0) {
                words.add(s.substring(i, j));
            }
            i = j + wordSeparator.length();
        }
        words.add(s.substring(i));
        return words;
    }

    /**
     * 将单词集合按照当前格式生成名称
     *
     * @param words 单词集合
     * @return 当前格式生成的名称
     * @author caotc
     * @date 2019-06-06
     * @since 1.0.0
     */
    @NonNull
    public String join(@NonNull String... words) {
        return join(Arrays.asList(words));
    }

    /**
     * 将单词集合按照当前格式生成名称
     *
     * @param words 单词集合
     * @return 当前格式生成的名称
     * @author caotc
     * @date 2019-06-06
     * @since 1.0.0
     */
    @NonNull
    public String join(@NonNull Iterable<String> words) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        for (String word : words) {
            if (i == 0) {
                out.append(normalizeFirstWord(word));
            } else {
                out.append(wordSeparator);
                out.append(normalizeWord(word));
            }
            i++;
        }
        return out.toString();
    }

    /**
     * Enum values can override for performance reasons.
     */
    String convert(CaseFormat format, String s) {
        // deal with camel conversion
        StringBuilder out = null;
        int i = 0;
        int j = -1;
        while ((j = wordBoundary.indexIn(s, ++j)) != -1) {
            if (i == 0) {
                // include some extra space for separators
                out = new StringBuilder(s.length() + 4 * format.wordSeparator.length());
                out.append(format.normalizeFirstWord(s.substring(i, j)));
            } else {
                requireNonNull(out).append(format.normalizeWord(s.substring(i, j)));
            }
            out.append(format.wordSeparator);
            i = j + wordSeparator.length();
        }
        return (i == 0)
                ? format.normalizeFirstWord(s)
                : requireNonNull(out).append(format.normalizeWord(s.substring(i))).toString();
    }

    abstract String normalizeWord(String word);

    String normalizeFirstWord(String word) {
        return normalizeWord(word);
    }
}
