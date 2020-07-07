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

package org.caotc.unit4j.support.mybatis.factory;

import lombok.Value;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.caotc.unit4j.core.Amount;

import java.util.List;

/**
 * @author caotc
 * @date 2020-06-25
 * @since 1.0.0
 */
@Value
public class AmountObjectFactory extends DefaultObjectFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(Class<T> type) {
        if (Amount.class.equals(type)) {
            return (T) Amount.UNKNOWN;
        }
        return super.create(type);
    }

    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        return super.create(type, constructorArgTypes, constructorArgs);
    }
}
