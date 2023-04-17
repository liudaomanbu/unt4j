
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

package org.caotc.unit4j.support.mybatis.mapper;

import org.caotc.unit4j.support.mybatis.model.TestAmountField;
import org.caotc.unit4j.support.mybatis.model.TestQuantity;


public interface TestQuantityMapper {
    /**
     * 对象入库
     *
     * @param entity 入库对象
     * @return 入库数目
     */
    int insertSelective(TestQuantity entity);

    /**
     * 按主键更新
     *
     * @param entity 更新对象
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(TestQuantity entity);

    /**
     * 按主键查询
     *
     * @param primaryKey 业务主键
     * @return 查询结果
     */
    TestQuantity findByPrimaryKeyOnResultType(Long primaryKey);

    /**
     * 按主键查询
     *
     * @param primaryKey 业务主键
     * @return 查询结果
     */
    TestQuantity findByPrimaryKeyOnResultMap(Long primaryKey);

    /**
     * 按主键查询
     *
     * @param primaryKey 业务主键
     * @return 查询结果
     */
    TestAmountField findByPrimaryKeyOnTestAmountField(Long primaryKey);

    /**
     * 按主键删除
     *
     * @param primaryKey 业务主键
     * @return 删除数目
     */
    int deleteByPrimaryKey(Long primaryKey);


}