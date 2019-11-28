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

package org.caotc.unit4j.core.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.caotc.unit4j.core.common.reflect.ReadableProperty;

/**
 * {@link ReadableProperty}不存在异常
 *
 * @author caotc
 * @date 2019-11-01
 * @see ReadableProperty
 * @since 1.0.0
 */
@Getter
@EqualsAndHashCode
public class ReadablePropertyNotFoundException extends PropertyNotFoundException {

  /**
   * 工厂方法
   *
   * @param propertyName 属性名称
   * @param clazz 类
   * @return {@link ReadablePropertyNotFoundException}
   * @author caotc
   * @date 2019-05-25
   * @since 1.0.0
   */
  @NonNull
  public static ReadablePropertyNotFoundException create(@NonNull Class<?> clazz,
      @NonNull String propertyName) {
    return new ReadablePropertyNotFoundException(clazz, propertyName);
  }

  private ReadablePropertyNotFoundException(@NonNull Class<?> clazz, @NonNull String propertyName) {
    super(clazz, propertyName);
  }

  @Override
  public String getMessage() {
    return String.format("%s not found the ReadableProperty named %s", clazz(), propertyName());
  }
}