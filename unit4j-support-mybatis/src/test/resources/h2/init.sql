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

CREATE TABLE `test_amount`
(
    `id`         bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `data_value` decimal     NOT NULL COMMENT '数据值',
    `data_unit`  varchar(40) NOT NULL COMMENT '数据单位',
    PRIMARY KEY (`id`)
);