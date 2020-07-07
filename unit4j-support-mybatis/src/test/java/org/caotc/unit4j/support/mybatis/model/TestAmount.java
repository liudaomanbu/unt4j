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

package org.caotc.unit4j.support.mybatis.model;

import lombok.Data;
import org.caotc.unit4j.api.annotation.AmountDeserialize;
import org.caotc.unit4j.api.annotation.AmountSerialize;
import org.caotc.unit4j.api.annotation.CodecStrategy;
import org.caotc.unit4j.core.Amount;


/**
 * `
 *
 * @author caotc
 * @date 2019-09-27
 * @since 1.0.0
 */
@Data
public class TestAmount {

    Long id;

    @AmountSerialize(strategy = CodecStrategy.FLAT)
    @AmountDeserialize(strategy = CodecStrategy.FLAT)
    Amount data;
}