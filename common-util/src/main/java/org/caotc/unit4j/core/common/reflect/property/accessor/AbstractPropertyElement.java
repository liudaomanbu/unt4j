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
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.caotc.unit4j.core.common.reflect.Element;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;

/**
 * 属性元素抽象类
 *
 * @param <O> 拥有该属性的类
 * @param <P> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractPropertyElement<O, P> extends Element implements
    PropertyElement<O, P> {

  protected <M extends AnnotatedElement & Member> AbstractPropertyElement(
          @NonNull M member) {
    super(member);
  }

  @SuppressWarnings("unchecked")
  @NonNull
  @Override
  public final TypeToken<O> ownerType() {
    return (TypeToken<O>) super.ownerType();
  }
}