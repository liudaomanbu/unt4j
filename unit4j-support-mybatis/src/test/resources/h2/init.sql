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
    `id`                 bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `with_unit_value`    integer     NOT NULL COMMENT 'value策略的amount字段',
    `with_unit_property` integer     NOT NULL COMMENT 'flat策略的amount的value字段',
    `unit`               varchar(40) NOT NULL COMMENT 'flat策略的amount的unit字段',
    PRIMARY KEY (`id`)
);
insert into test_amount (id, with_unit_value, with_unit_property, unit)
values (1, 10, 60, 'SECOND');