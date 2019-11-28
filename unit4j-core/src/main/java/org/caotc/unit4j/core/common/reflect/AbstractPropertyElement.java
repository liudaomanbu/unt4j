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

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import lombok.NonNull;

/**
 * 属性元素抽象类
 *
 * @param <T> 拥有该属性的类
 * @param <R> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
public abstract class AbstractPropertyElement<T, R> extends Element<T> implements
    PropertyElement<T, R> {

  protected <M extends AccessibleObject & Member> AbstractPropertyElement(
      @NonNull M member) {
    super(member);
  }

  /**
   * 设置属性类型
   *
   * @param propertyType 属性类型
   * @return this
   * @author caotc
   * @date 2019-06-25
   * @since 1.0.0
   */
  @Override
  @NonNull
  public <R1 extends R> AbstractPropertyElement<T, R1> propertyType(
      @NonNull Class<R1> propertyType) {
    return propertyType(TypeToken.of(propertyType));
  }

  /**
   * 设置属性类型
   *
   * @param propertyType 属性类型
   * @return this
   * @author caotc
   * @date 2019-06-25
   * @since 1.0.0
   */
  @Override
  @SuppressWarnings("unchecked")
  @NonNull
  public <R1 extends R> AbstractPropertyElement<T, R1> propertyType(
      @NonNull TypeToken<R1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(propertyType())
        , "PropertyElement is known propertyType %s,not %s ", propertyType(), propertyType);
    return (AbstractPropertyElement<T, R1>) this;
  }
}