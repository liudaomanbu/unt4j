CREATE TABLE `test_amount`
(
    `id`                bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `amount_value`      integer     NOT NULL COMMENT 'value策略的amount字段',
    `amount_flat_value` integer     NOT NULL COMMENT 'flat策略的amount的value字段',
    `amount_flat_unit`  varchar(40) NOT NULL COMMENT 'flat策略的amount的unit字段',
    PRIMARY KEY (`id`)
);
insert into test_amount (id, amount_value, amount_flat_value, amount_flat_unit)
values (1, 99, 88, 'SECOND');