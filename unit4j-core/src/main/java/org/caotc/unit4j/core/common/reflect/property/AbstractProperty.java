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

package org.caotc.unit4j.core.common.reflect.property;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * 属性抽象类
 *
 * @param <O> 拥有该属性的类
 * @param <P> 属性类型
 * @author caotc
 * @date 2019-11-23
 * @since 1.0.0
 */
@EqualsAndHashCode
@ToString
public abstract class AbstractProperty<O, P> implements
    Property<O, P> {

  @Override
  public final boolean accessible() {
    return readable() && writable();
  }

  @Override
  @NonNull
  public <P1 extends P> AbstractProperty<O, P1> type(
          @NonNull Class<P1> newType) {
    return type(TypeToken.of(newType));
  }

  @Override
  @SuppressWarnings("unchecked")
  @NonNull
  public <P1 extends P> AbstractProperty<O, P1> type(
      @NonNull TypeToken<P1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(type())
        , "Property is known type %s,not %s ", type(), propertyType);
    return (AbstractProperty<O, P1>) this;
  }

}
