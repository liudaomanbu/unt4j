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

import com.google.common.reflect.TypeToken;
import java.util.function.Predicate;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * @author caotc
 * @date 2019-12-26
 * @since 1.0.0
 */
@Value
@Builder
public class ReflectAction<T, R> {

  @NonNull
  TypeToken<T> target;

  @NonNull
  R reflectTypeClass;

  @NonNull
  Predicate<?> filter;

}
