CREATE TABLE `doctor_team_member`
(
    `id`             bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `is_deleted`     tinyint(3)  NOT NULL COMMENT '逻辑删除,0.未删除 1.已删除',
    `gmt_created`    datetime    NOT NULL COMMENT '创建时间',
    `gmt_modified`   datetime    NOT NULL COMMENT '修改时间',
    `doctor_team_id` varchar(40) NOT NULL COMMENT '医生团队主键',
    `doctor_id`      varchar(40) NOT NULL COMMENT '医生主键',
    PRIMARY KEY (`id`)
);
insert into doctor_team_member (id, is_deleted, gmt_created, gmt_modified, doctor_team_id,
                                doctor_id)
values (1, 0, now(), now(), 2, 3);