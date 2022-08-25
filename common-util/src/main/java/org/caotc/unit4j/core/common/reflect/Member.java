package org.caotc.unit4j.core.common.reflect;

import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.NonNull;
import org.caotc.unit4j.core.exception.NeverHappenException;

import java.lang.reflect.Modifier;

/**
 * @author caotc
 * @date 2022-08-25
 * @since 1.0.0
 */
public interface Member extends java.lang.reflect.Member {
    @NonNull
    default TypeToken<?> ownerType() {
        return TypeToken.of(getDeclaringClass());
    }

    @NonNull
    default String name() {
        return getName();
    }

    /**
     * Returns true if the element is public.
     */
    default boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }

    /**
     * Returns true if the element is protected.
     */
    default boolean isProtected() {
        return Modifier.isProtected(getModifiers());
    }

    /**
     * Returns true if the element is package-private.
     */
    default boolean isPackagePrivate() {
        return !isPrivate() && !isPublic() && !isProtected();
    }

    /**
     * Returns true if the element is private.
     */
    default boolean isPrivate() {
        return Modifier.isPrivate(getModifiers());
    }

    /**
     * Returns true if the element is static.
     */
    default boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    /**
     * Returns {@code true} if this method is final, per {@code Modifier.isFinal(getModifiers())}.
     *
     * <p>Note that a method may still be effectively "final", or non-overridable when it has no
     * {@code final} keyword. For example, it could be private, or it could be declared by a final
     * class. To tell whether a method is overridable, use {@link Invokable#isOverridable}.
     */
    default boolean isFinal() {
        return Modifier.isFinal(getModifiers());
    }

    /**
     * Returns true if the method is abstract.
     */
    default boolean isAbstract() {
        return Modifier.isAbstract(getModifiers());
    }

    /**
     * Returns true if the element is native.
     */
    default boolean isNative() {
        return Modifier.isNative(getModifiers());
    }

    /**
     * Returns true if the method is synchronized.
     */
    default boolean isSynchronized() {
        return Modifier.isSynchronized(getModifiers());
    }

    /**
     * Returns true if the field is volatile.
     */
    default boolean isVolatile() {
        return Modifier.isVolatile(getModifiers());
    }

    /**
     * Returns true if the field is transient.
     */
    default boolean isTransient() {
        return Modifier.isTransient(getModifiers());
    }

    /**
     * 该元素的权限级别
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
        throw NeverHappenException.instance();
    }
}
