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

package org.caotc.unit4j.core.exception;

import lombok.NonNull;

/**
 * 理论上不会发生的异常,如果被抛出,说明该库代码存在bug
 *
 * @author caotc
 * @date 2019-05-24
 * @since 1.0.0
 */
public class NeverHappenException extends IllegalStateException {

    /**
     * 实例对象
     */
    private static final NeverHappenException INSTANCE = new NeverHappenException();

    /**
     * 获取实例对象
     *
     * @return 实例对象
     * @author caotc
     * @date 2019-05-26
     * @since 1.0.0
     */
    @NonNull
    public static NeverHappenException instance() {
        return INSTANCE;
    }

    private NeverHappenException() {
        super("never happen");
    }
}
