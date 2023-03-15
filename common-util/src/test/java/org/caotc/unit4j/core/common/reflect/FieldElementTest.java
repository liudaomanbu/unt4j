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

import com.google.common.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyAccessor;
import org.caotc.unit4j.core.common.util.model.GenericFieldObject;
import org.caotc.unit4j.core.common.util.provider.Constant;
import org.junit.jupiter.api.Test;

/**
 * @author caotc
 * @date 2021-02-24
 * @since 1.0.0
 */
@Slf4j
class FieldElementTest {

  @Test
  void test() throws NoSuchFieldException {
    PropertyAccessor<GenericFieldObject<Object>, Object> accessor = PropertyAccessor.from(new TypeToken<GenericFieldObject<Object>>() {
    }, Constant.GENERIC_FIELD_OBJECT_GENERIC_FIELD);
    log.info("accessor:{}", accessor);
  }
}
