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
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

@Slf4j
public class AnnotationTest {
  public static void main(String[] args) {
    System.out.println(AnnotatedElementUtils.findAllMergedAnnotations(ChildController.class, PostMapping.class));
    System.out.println(Arrays.toString(AnnotationUtils.getAnnotations(ChildController.class)));
    System.out.println("ParentController getAnnotation @RequestMapping: " + AnnotationUtils.getAnnotation(ParentController.class, RequestMapping.class));
    System.out.println("ChildController getAnnotation @RequestMapping: " + AnnotationUtils.getAnnotation(ChildController.class, RequestMapping.class));
    System.out.println("ParentController findAnnotation @RequestMapping: " + AnnotationUtils.findAnnotation(ParentController.class, RequestMapping.class));
    System.out.println("ChildController findAnnotation @RequestMapping: " + AnnotationUtils.findAnnotation(ChildController.class, RequestMapping.class));
    System.out.println();

    System.out.println("ParentController getMergedAnnotation @RequestMapping: " + AnnotatedElementUtils.getMergedAnnotation(ParentController.class, RequestMapping.class));
    System.out.println("ChildController getMergedAnnotation @RequestMapping: " + AnnotatedElementUtils.getMergedAnnotation(ChildController.class, RequestMapping.class));
    System.out.println("ParentController findMergedAnnotation @RequestMapping: " + AnnotatedElementUtils.findMergedAnnotation(ParentController.class, RequestMapping.class));
    System.out.println("ChildController findMergedAnnotation @RequestMapping: " + AnnotatedElementUtils.findMergedAnnotation(ChildController.class, RequestMapping.class));
  }
}

@RequestMapping(name = "parent", path = "parent/controller")
class ParentController {
}

@PostMapping(name = "child", value = "child/controller", consume = "application/json")
class ChildController extends ParentController {
}


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Inherited
@interface RequestMapping {

  String name() default "";

  @AliasFor("path")
  String[] value() default {};


  @AliasFor("value")
  String[] path() default {};

  String[] consume() default {};
}


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping
@interface PostMapping {

  @AliasFor(annotation = RequestMapping.class)
  String name() default "";

  @AliasFor(annotation = RequestMapping.class)
  String[] value() default {};

  @AliasFor(annotation = RequestMapping.class)
  String[] path() default {};

  @AliasFor(annotation = RequestMapping.class)
  String[] consume() default "";
}
