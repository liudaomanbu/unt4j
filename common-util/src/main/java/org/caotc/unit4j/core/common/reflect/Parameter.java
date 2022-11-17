package org.caotc.unit4j.core.common.reflect;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.AnnotatedType;

/**
 * @author caotc
 * @date 2022-11-17
 * @since 1.0.0
 */
public interface Parameter extends AnnotatedElement {
    TypeToken<?> type();

    /**
     * Returns the {@link Invokable} that declares this parameter.
     */
    Invokable<?, ?> declaringInvokable();

    // @Override on JDK8
    AnnotatedType annotatedType();
}
