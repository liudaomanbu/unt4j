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
import lombok.AccessLevel;
import lombok.NonNull;

/**
 * 属性元素
 *
 * @param <O> 拥有该属性的类
 * @param <P> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
public interface PropertyElement<O, P> {

  /**
   * 获取属性名称
   *
   * @return 属性名称
   * @author caotc
   * @date 2019-11-22
   * @since 1.0.0
   */
  @NonNull
  String propertyName();

  /**
   * 获取属性类型
   *
   * @return 属性类型
   * @author caotc
   * @date 2019-11-22
   * @since 1.0.0
   */
  @NonNull
  TypeToken<? extends P> propertyType();

  /**
   * get owner type of {@code O}
   *
   * @return owner type
   * @author caotc
   * @date 2019-12-04
   * @since 1.0.0
   */
  @NonNull
  TypeToken<O> ownerType();

  /**
   * 设置属性类型
   *
   * @param propertyType 属性类型
   * @return this
   * @author caotc
   * @date 2019-06-25
   * @since 1.0.0
   */
  @NonNull <R1 extends P> PropertyElement<O, R1> propertyType(@NonNull Class<R1> propertyType);

  /**
   * 设置属性类型
   *
   * @param propertyType 属性类型
   * @return this
   * @author caotc
   * @date 2019-06-25
   * @since 1.0.0
   */
  @NonNull <R1 extends P> PropertyElement<O, R1> propertyType(@NonNull TypeToken<R1> propertyType);

  /**
   * 该元素的权限级别
   *
   * @return 权限级别
   * @author caotc
   * @date 2019-07-14
   * @since 1.0.0
   */
  @NonNull
  AccessLevel accessLevel();

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