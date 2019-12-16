/*
 * Copyright (C) 2019 the original author or authors.
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

package org.caotc.unit4j.core.common.reflect;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.util.Optional;
import lombok.NonNull;

/**
 * 属性
 *
 * @param <O> 拥有该属性的类
 * @param <P> 属性类型
 * @author caotc
 * @date 2019-11-22
 * @see ReadableProperty
 * @see WritableProperty
 * @since 1.0.0
 */
public interface Property<O, P> {

  /**
   * 获取属性名称
   *
   * @return 属性名称
   * @author caotc
   * @date 2019-11-22
   * @since 1.0.0
   */
  @NonNull
  String name();

  /**
   * 获取属性类型
   *
   * @return 属性类型
   * @author caotc
   * @date 2019-11-22
   * @since 1.0.0
   */
  @NonNull
  TypeToken<? extends P> type();

  /**
   * 设置属性类型
   *
   * @param propertyType 属性类型
   * @return this
   * @author caotc
   * @date 2019-06-25
   * @since 1.0.0
   */
  @NonNull <P1 extends P> Property<O, P1> type(@NonNull Class<P1> propertyType);

  /**
   * 设置属性类型
   *
   * @param propertyType 属性类型
   * @return this
   * @author caotc
   * @date 2019-11-22
   * @since 1.0.0
   */
  @NonNull <P1 extends P> Property<O, P1> type(
      @NonNull TypeToken<P1> propertyType);

  boolean fieldExist();

  /**
   * 获取注解
   *
   * @param annotationClass 注解类
   * @return 注解的 {@link Optional}
   * @author caotc
   * @date 2019-11-22
   * @since 1.0.0
   */
  @NonNull <X extends Annotation> Optional<X> annotation(
      @NonNull Class<X> annotationClass);

  /**
   * 获取所有注解
   *
   * @return 所有注解集合
   * @author caotc
   * @date 2019-11-22
   * @since 1.0.0
   */
  @NonNull
  ImmutableList<Annotation> annotations();

  /**
   * 获取注解集合
   *
   * @param annotationClass 注解类
   * @return 该类注解集合
   * @author caotc
   * @date 2019-11-22
   * @since 1.0.0
   */
  @NonNull <X extends Annotation> ImmutableList<X> annotations(
      @NonNull Class<X> annotationClass);

  /**
   * 获取所有定义在该属性上的注解
   *
   * @return 所有定义在该属性上的集合
   * @author caotc
   * @date 2019-11-22
   * @since 1.0.0
   */
  @NonNull
  ImmutableList<Annotation> declaredAnnotations();
}
