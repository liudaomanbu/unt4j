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

package org.caotc.unit4j.support.mybatis.constant;

import lombok.experimental.UtilityClass;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.caotc.unit4j.core.constant.StringConstant;

/**
 * @author caotc
 * @date 2020-07-02
 * @since 1.0.0
 */
@UtilityClass
public class AmountPropertyConstant {
    /**
     * 由于mybatis对于x.x的多级属性get和set操作是通过{@link MetaObject}完成的,
     * 而不是通过 {@link ObjectWrapper}完成的.
     * 所以没有现成的插件改造入口,所以通过自定义属性分隔符的方式来达到将x.x的多级属性操作自定义目的.
     * 修改{@link ResultMapping}时使用该分隔符代替".",{@link MetaObject}就会直接传入{@link ObjectWrapper},
     * 可以通过自定义配置{@link ObjectWrapperFactory}来自定义{@link ObjectWrapper}操作.
     */
    public static final String DELIMITER = StringConstant.HYPHEN;
}
