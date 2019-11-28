CREATE TABLE `test_amount`
(
    `id`                 bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `gmt_created`        datetime    NOT NULL COMMENT '创建时间',
    `with_unit_value`    integer     NOT NULL COMMENT 'value策略的amount字段',
    `with_unit_property` integer     NOT NULL COMMENT 'flat策略的amount的value字段',
    `unit`               varchar(40) NOT NULL COMMENT 'flat策略的amount的unit字段',
    PRIMARY KEY (`id`)
);