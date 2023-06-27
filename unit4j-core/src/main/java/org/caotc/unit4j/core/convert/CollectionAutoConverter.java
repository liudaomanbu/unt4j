/*
 * Copyright (C) 2023 the original author or authors.
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

package org.caotc.unit4j.core.convert;

import lombok.NonNull;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;

import java.util.Collection;

/**
 * @author caotc
 * @date 2023-04-13
 * @since 1.0.0
 */
@FunctionalInterface
public interface CollectionAutoConverter {
    @NonNull
    Collection<Quantity> autoConvert(@NonNull Configuration configuration, @NonNull Collection<Quantity> quantities, boolean unitConsistency);

    default Collection<Quantity> autoConvert(@NonNull Configuration configuration, @NonNull Collection<Quantity> quantities) {
        return autoConvert(Configuration.defaultInstance(), quantities, true);
    }
}
