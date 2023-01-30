package org.caotc.unit4j.core.common.reflect;

import lombok.NonNull;
import org.caotc.unit4j.core.common.base.AccessLevel;

/**
 * @author caotc
 * @date 2022-10-14
 * @since 1.0.0
 */
public interface WithAccessLevel {
    /**
     * Returns true if the element is public.
     */
    boolean isPublic();

    /**
     * Returns true if the element is protected.
     */
    boolean isProtected();

    /**
     * Returns true if the element is package-private.
     */
    boolean isPackagePrivate();

    /**
     * Returns true if the element is private.
     */
    boolean isPrivate();

    /**
     * 该元素的权限级别
     * //todo lombok强依赖？
     *
     * @return 权限级别
     * @author caotc
     * @date 2019-07-14
     * @since 1.0.0
     */
    @NonNull
    default AccessLevel accessLevel() {
        if (isPrivate()) {
            return AccessLevel.PRIVATE;
        }
        if (isPackagePrivate()) {
            return AccessLevel.PACKAGE;
        }
        if (isProtected()) {
            return AccessLevel.PROTECTED;
        }
        if (isPublic()) {
            return AccessLevel.PUBLIC;
        }
        throw new AssertionError("never happen");
    }
}
