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

package org.caotc.unit4j.core.common.reflect;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.Parameter;
import com.google.common.reflect.TypeToken;
import lombok.NonNull;
import lombok.Value;

import java.lang.reflect.Method;

/**
 * @author caotc
 * @date 2019-12-26
 * @since 1.0.0
 */
@Value
public class MethodSignature {

    @NonNull
  public static MethodSignature from(@NonNull Method method) {
    return from(Invokable.from(method));
  }

  @NonNull
  public static MethodSignature from(@NonNull Invokable<?, ?> invokable) {
    return new MethodSignature(invokable);
  }

  @NonNull
  String name;
  @NonNull
  ImmutableList<TypeToken<?>> parameterTypes;

  private MethodSignature(@NonNull Invokable<?, ?> invokable) {
    this.name = invokable.getName();
    this.parameterTypes = invokable.getParameters().stream().map(Parameter::getType)
        .collect(ImmutableList.toImmutableList());
  }
}
