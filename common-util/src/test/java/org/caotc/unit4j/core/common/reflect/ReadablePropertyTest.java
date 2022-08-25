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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.reflect.property.ReadableProperty;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;
import org.caotc.unit4j.core.common.util.model.Sub;
import org.caotc.unit4j.core.common.util.model.Super;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * @author caotc
 * @date 2021-02-24
 * @since 1.0.0
 */
@Slf4j
class ReadablePropertyTest {

  @Test
  void test() throws NoSuchFieldException {
    FieldElement<Super, Object> superStringField = FieldElement
            .of(Super.class.getDeclaredField("stringField"));
    FieldElement<Sub, Object> subStringField = FieldElement
            .of(Sub.class.getDeclaredField("stringField"));
    FieldElement<Sub, Object> subNumberField = FieldElement
            .of(Sub.class.getDeclaredField("numberField"));

    PropertyReader<Super, Object> superStringFieldPropertyReader = PropertyReader
        .from(superStringField);
    PropertyReader<Sub, Object> subStringFieldPropertyReader = PropertyReader.from(subStringField);
    PropertyReader<Sub, Object> subNumberFieldPropertyReader = PropertyReader.from(subNumberField);

    ImmutableList<PropertyReader<? extends Super, Object>> propertyReaders = ImmutableList
        .of(superStringFieldPropertyReader, subStringFieldPropertyReader);

    ReadableProperty<Super, Object> readableProperty1 = ReadableProperty
        .create(ImmutableSet.of(superStringFieldPropertyReader));

    ReadableProperty<Sub, Object> readableProperty2 = ReadableProperty
        .create(ImmutableSet.of(subStringFieldPropertyReader));
    ReadableProperty<Sub, Object> readableProperty3 = ReadableProperty
        .create(ImmutableSet.of(subNumberFieldPropertyReader));

    ImmutableSet<ReadableProperty<? super Sub, Object>> of = ImmutableSet
        .of(readableProperty1, readableProperty2, readableProperty3);

    Sub sub = new Sub();
    for (ReadableProperty<? super Sub, Object> readableProperty : of) {
      Optional<Object> value = readableProperty.read(sub);
      log.debug("property:{},value:{}", readableProperty.name(), value);
    }
  }
}
