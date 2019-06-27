CREATE TABLE `User`
(
    `AutoId`         bigint(20)  NOT NULL AUTO_INCREMENT,
    `UserId`         bigint(20)  NOT NULL COMMENT '用户Id',
    `UserName`       varchar(64) NOT NULL COMMENT '用户姓名',
    `Age`            int(10)     NOT NULL COMMENT '年龄',
    `PointValue`     int(11)     NOT NULL DEFAULT '0' COMMENT '积分',
    `Status`         smallint(6) NOT NULL DEFAULT '0' COMMENT '记录可用状态',
    `CreateTime`     datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建日期',
    `LastModifyTime` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改日期',
    PRIMARY KEY (`AutoId`)
);

CREATE TABLE `doctor_team_member`
(
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `is_deleted`     tinyint(3) unsigned NOT NULL COMMENT '逻辑删除,0.未删除 1.已删除',
    `gmt_created`    datetime    NOT NULL COMMENT '创建时间',
    `gmt_modified`   datetime    NOT NULL COMMENT '修改时间',
    `doctor_team_id` varchar(40) NOT NULL COMMENT '医生团队主键',
    `doctor_id`      varchar(40) NOT NULL COMMENT '医生主键',
    PRIMARY KEY (`id`)
);