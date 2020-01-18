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

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * @author caotc
 * @date 2019-12-26
 * @since 1.0.0
 */
@Value
@Builder
public class ReflectAction<T, R> {

  @FieldDefaults(makeFinal = false, level = AccessLevel.PROTECTED)
  private static class MethodBuilder<T> extends ReflectActionBuilder<T, Method> {

//    @NonNull
//    public MethodBuilder<T> method(){
//      return new MethodBuilder<>(from);
//    }

  }

//  public static <T,R> ReflectAction.MethodBuilder<T, R> from(TypeToken<T> typeToken) {
//    return ReflectAction.<T, R>builder().from(typeToken);
//  }

  private static <T, R> ReflectAction.ReflectActionBuilder<T, R> builder() {
    return new ReflectAction.ReflectActionBuilder<>();
  }

  @NonNull
  TypeToken<T> from;

  @NonNull
  @Singular
  ImmutableList<Predicate<R>> filters;

  @NonNull
  @Setter(AccessLevel.NONE)
  Function<Class<?>, Stream<R>> reflectTypeClassGetter;

  @NonNull
  @Getter(lazy = true)
  Predicate<R> filterCompose = filterComposeInternal();

  @NonNull
  private Predicate<R> filterComposeInternal() {
    return filters().stream().reduce(Predicate::and).orElseGet(
        Predicates::alwaysTrue);
  }

  @NonNull
  public Stream<R> stream() {
    return from.getTypes().rawTypes().stream()
        .flatMap(reflectTypeClassGetter)
        .filter(filterCompose());
  }

  @NonNull
  public ImmutableSet<R> set() {
    return stream().collect(ImmutableSet.toImmutableSet());
  }

  @NonNull
  public Optional<R> only() {
    return stream().findAny();
  }

  @NonNull
  public R onlyExact() {
    return only().orElseThrow(IllegalAccessError::new);
  }
}
