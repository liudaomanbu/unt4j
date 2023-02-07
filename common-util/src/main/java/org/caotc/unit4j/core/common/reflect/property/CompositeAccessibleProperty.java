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

import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;

import java.util.Optional;

/**
 * 可读取属性
 * todo 范型字母
 *
 * @param <O> 拥有该属性的类
 * @param <P> 属性类型
 * @author caotc
 * @date 2019-05-27
 * @see PropertyReader
 * @since 1.0.0
 */
@Value
public class CompositeAccessibleProperty<O, P, T> extends
        AbstractCompositeProperty<O, P, T, AccessibleProperty<T, P>> implements
        AccessibleProperty<O, P> {

  CompositeAccessibleProperty(
          @NonNull ReadableProperty<O, T> targetReadableProperty,
          @NonNull AccessibleProperty<T, P> delegate) {
    super(targetReadableProperty, delegate);
  }

  @Override
  public boolean readable() {
    return delegate.readable();
  }

  @Override
  public boolean writable() {
    return delegate.writable();
  }

  @Override
  public @NonNull Optional<P> read(@NonNull O target) {
    return transferProperty.read(target).flatMap(delegate::read);
  }

  @Override
  public @NonNull AccessibleProperty<O, P> write(@NonNull O target, @NonNull P value) {
    transferProperty.read(target)
            .ifPresent(actualTarget -> delegate.write(actualTarget, value));
    return this;
  }

}
