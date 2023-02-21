package org.caotc.unit4j.core.common.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * todo 移到reflect包？
 *
 * @author caotc
 * @date 2023-01-30
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum AccessLevel {
    PUBLIC(0), MODULE(1), PROTECTED(2), PACKAGE(3), PRIVATE(4);
    int code;

    public boolean isMore(@NonNull AccessLevel other) {
        return code() < other.code();
    }

    public boolean isMoreOrEqual(@NonNull AccessLevel other) {
        return code() <= other.code();
    }

    public boolean isLess(@NonNull AccessLevel other) {
        return code() > other.code();
    }

    public boolean isLessOrEqual(@NonNull AccessLevel other) {
        return code() >= other.code();
    }
}
