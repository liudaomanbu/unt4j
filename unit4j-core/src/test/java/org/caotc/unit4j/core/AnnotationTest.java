/*
 * Copyright (C) 2020 the original author or authors.
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

package org.caotc.unit4j.core;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author caotc
 * @date 2020-04-17
 * @since 1.0.0
 */
@Slf4j
public class AnnotationTest {
    @MyCustomRole
    public static class Parent {
    }

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @Repeatable(Roles.class)
    public @interface Role {
        String value() default "";
    }

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public @interface Roles {
        Role[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Role("role_a")
    @Role("role_b")
    public @interface MyCustomRole {
    }

    @Test
    public void test() {
        log.info("findMergedAnnotation Role:{}", AnnotatedElementUtils.findMergedAnnotation(Parent.class, Role.class));
        log.info("findAllMergedAnnotations Role:{}", AnnotatedElementUtils.findAllMergedAnnotations(Parent.class, Role.class));
        log.info("findMergedRepeatableAnnotations Role:{}", AnnotatedElementUtils.findMergedRepeatableAnnotations(Parent.class, Role.class));
    }
}
