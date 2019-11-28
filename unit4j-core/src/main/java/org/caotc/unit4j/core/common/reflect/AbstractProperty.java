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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractProperty<O, P> implements
    Property<O, P> {

  @NonNull
  String propertyName;
  @NonNull
  TypeToken<? extends P> propertyType;

  protected AbstractProperty(
      @NonNull ImmutableSet<? extends PropertyElement<O, P>> properties) {
    //属性读取器集合不能为空
    Preconditions
        .checkArgument(!properties.isEmpty(), "properties can't be empty");
    //属性只能有一个
    ImmutableSet<@NonNull String> propertyNames = properties.stream()
        .map(Property::propertyName).collect(ImmutableSet.toImmutableSet());
    ImmutableSet<? extends TypeToken<? extends P>> propertyTypes = properties.stream()
        .map(Property::propertyType).collect(ImmutableSet.toImmutableSet());
    Preconditions.checkArgument(propertyNames.size() == 1 && propertyTypes.size() == 1,
        "properties is not a common property");
    this.propertyName = Iterables.getOnlyElement(propertyNames);
    this.propertyType = Iterables.getOnlyElement(propertyTypes);
  }

  @NonNull
  @Override
  public final String propertyName() {
    return propertyName;
  }

  @NonNull
  @Override
  public final TypeToken<? extends P> propertyType() {
    return propertyType;
  }

  @Override
  @NonNull
  public <P1 extends P> AbstractProperty<O, P1> propertyType(
      @NonNull Class<P1> propertyType) {
    return propertyType(TypeToken.of(propertyType));
  }

  @Override
  @SuppressWarnings("unchecked")
  @NonNull
  public <P1 extends P> AbstractProperty<O, P1> propertyType(
      @NonNull TypeToken<P1> propertyType) {
    Preconditions.checkArgument(propertyType.isSupertypeOf(propertyType())
        , "Property is known propertyType %s,not %s ", propertyType(), propertyType);
    return (AbstractProperty<O, P1>) this;
  }

}
