/*
 * Copyright (C) 2021 the original author or authors.
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

package org.caotc.unit4j.core.common.reflect.property.accessor;

import com.google.common.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.caotc.unit4j.core.common.reflect.Element;

import java.lang.annotation.Annotation;

/**
 * 属性元素抽象类
 * todo 范型字母
 *
 * @param <O> 拥有该属性的类
 * @param <P> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractPropertyElement<O, P> implements
        PropertyElement<O, P> {
    @NonNull
    Element element;

    @SuppressWarnings("unchecked")
    @Override
    public TypeToken<? super O> declaringType() {
        return (TypeToken<? super O>) TypeToken.of(element.getDeclaringClass());
    }

    @Override
    public boolean accessible() {
        return element.accessible();
    }

    @Override
    public @NonNull PropertyElement<O, P> accessible(boolean accessible) {
        element.accessible(accessible);
        return this;
    }

    @Override
    public boolean isPublic() {
        return element.isPublic();
    }

    @Override
    public boolean isProtected() {
        return element.isProtected();
    }

    @Override
    public boolean isPackagePrivate() {
        return element.isPackagePrivate();
    }

    @Override
    public boolean isPrivate() {
        return element.isPrivate();
    }

    @Override
    public <T extends Annotation> T getAnnotation(@NonNull Class<T> annotationClass) {
        return element.getAnnotation(annotationClass);
    }

    @Override
    public Annotation[] getAnnotations() {
        return element.getAnnotations();
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return element.getDeclaredAnnotations();
    }

    @Override
    public String toString() {
        return element.toString();
    }
}