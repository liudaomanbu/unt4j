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

package org.caotc.unit4j.core.common.reflect;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.sun.istack.internal.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

/**
 * @author caotc
 * @date 2020-11-16
 * @since 1.0.0
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyName {

  private static final String SEPARATOR = ".";
  private static final Splitter SPLITTER = Splitter.on(SEPARATOR).omitEmptyStrings().trimResults();
  private static final Joiner JOINER = Joiner.on(SEPARATOR);

  @NonNull
  public static PropertyName from(@NotNull String propertyName) {
    return create(Streams.stream(SPLITTER.split(propertyName))
        .collect(ImmutableList.toImmutableList()));
  }

  @NonNull
  public static PropertyName create(@NotNull ImmutableList<String> propertyNames) {
    Preconditions.checkArgument(!propertyNames.isEmpty(), "propertyName can not be empty");
    //TODO 检查每个属性名格式
    return new PropertyName(propertyNames);
  }

  @NonNull
  ImmutableList<String> propertyNames;

  @NonNull
  public PropertyName firstTier() {
    return new PropertyName(propertyNames().subList(0, 1));
  }

  public boolean complex() {
    return propertyNames().size() != 1;
  }

  @NonNull
  public PropertyName sub(int fromIndex) {
    return sub(fromIndex, propertyNames().size());
  }

  @NonNull
  public PropertyName sub(int fromIndex, int toIndex) {
    return new PropertyName(propertyNames().subList(fromIndex, toIndex));
  }

  @NonNull
  public String flat() {
    return JOINER.join(propertyNames());
  }
}
