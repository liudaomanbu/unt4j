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

package org.caotc.unit4j.api.annotation;

/**
 * 序列化、反序列化时的策略
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
public enum CodecStrategy {
    /**
     * 像普通对象一样序列化，即输出为一个json对象，里面包含所有属性
     */
    OBJECT,
    /**
     * 只输出值,单位为默认的固定单位
     */
    VALUE,
    /**
     * 与{@see OBJECT}一样输出所有属性, 但是改为扁平化输出，即自己不是一个对象，而是多个字段
     */
    FLAT;
}
