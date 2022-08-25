package org.caotc.unit4j.core.common.reflect;

import com.google.common.collect.ImmutableList;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * @author caotc
 * @date 2022-08-25
 * @since 1.0.0
 */
public interface AnnotatedElement extends java.lang.reflect.AnnotatedElement {
    @NonNull
    default <X extends Annotation> Optional<X> annotation(@NonNull Class<X> annotationClass) {
        return Optional.ofNullable(getAnnotation(annotationClass));
    }

    @NonNull
    default <X extends Annotation> ImmutableList<X> annotations(
            @NonNull Class<X> annotationClass) {
        return ImmutableList.copyOf(getAnnotationsByType(annotationClass));
    }

    @NonNull
    default ImmutableList<Annotation> declaredAnnotations() {
        return ImmutableList.copyOf(getDeclaredAnnotations());
    }

    @NonNull
    default ImmutableList<Annotation> annotations() {
        return ImmutableList.copyOf(getAnnotations());
    }
}
