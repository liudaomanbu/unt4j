package org.caotc.unit4j.core.common.reflect;

import lombok.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author caotc
 * @date 2022-08-25
 * @see Field
 * @see Method
 * @see Constructor
 * @since 1.0.0
 */
public interface Element extends AnnotatedElement, Member {

    boolean accessible();

    @NonNull
    Element accessible(boolean accessible);
}
