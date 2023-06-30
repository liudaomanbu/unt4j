package org.caotc.unit4j.core.common.reflect;

import com.google.common.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;

/**
 * @author caotc
 * @date 2022-11-17
 * @since 1.0.0
 */
@AllArgsConstructor(staticName = "of", access = AccessLevel.MODULE)
@SuppressWarnings("UnstableApiUsage")
public class GuavaParameterProxy implements Parameter {
    @NonNull
    com.google.common.reflect.Parameter delegate;
    @NonNull
    Invokable<?, ?> declaration;

    @Override
    public TypeToken<?> type() {
        return delegate.getType();
    }

    @Override
    public Invokable<?, ?> declaringInvokable() {
        return declaration;
    }

    @Override
    public AnnotatedType annotatedType() {
        return delegate.getAnnotatedType();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return delegate.getAnnotation(annotationClass);
    }

    @Override
    public Annotation[] getAnnotations() {
        return delegate.getAnnotations();
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return delegate.getDeclaredAnnotations();
    }
}
