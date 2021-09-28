-- 导出 spring_boot_demo 的数据库结构
DROP DATABASE IF EXISTS `spring_boot_demo`;
CREATE DATABASE IF NOT EXISTS `spring_boot_demo` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `spring_boot_demo`;

--
DROP TABLE IF EXISTS `todo`;
CREATE TABLE IF NOT EXISTS `todo`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`        varchar(50)  NOT NULL COMMENT '名称',
    `type`        varchar(50)           DEFAULT NULL COMMENT '类型',
    `todo_date`   varchar(50)           DEFAULT NULL COMMENT '代办日期',
    `notify_date` varchar(50)           DEFAULT NULL COMMENT '提醒日期',
    `detail`      varchar(200)          DEFAULT NULL COMMENT '详情',
    `images`      varchar(200)          DEFAULT NULL COMMENT '图片',
    `create_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='待办事项';

DELETE
FROM `todo`;
INSERT INTO `todo` (`id`, `name`, `type`, `todo_date`, `notify_date`, `detail`, `images`)
VALUES (1, 'name1', '出游', '2021-04-03', '2021-04-03', 'detail1',
        'http://image11.m1905.cn/mdb/uploadfile/2017/0518/thumb_1_168_230_20170518040407120632.jpg'),
       (2, 'name2', '出游', '2021-05-03', '2021-05-03', 'detail2',
        'http://image11.m1905.cn/mdb/uploadfile/2017/0518/thumb_1_168_230_20170518040407120632.jpg'),
       (3, 'name3', '出游', '2021-04-04', '2021-04-05', 'detail3',
        'http://image11.m1905.cn/mdb/uploadfile/2017/0518/thumb_1_168_230_20170518040407120632.jpg'),
       (4, 'name4', '出游', '2021-02-02', '2021-02-03', 'detail4',
        'http://image11.m1905.cn/mdb/uploadfile/2017/0518/thumb_1_168_230_20170518040407120632.jpg');

--
DROP TABLE IF EXISTS `the_day`;
CREATE TABLE IF NOT EXISTS `the_day`
(
    `id`           int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`         varchar(50)  NOT NULL COMMENT '名称',
    `the_day_date` varchar(50)           DEFAULT NULL COMMENT '代办日期',
    `notify_date`  varchar(50)           DEFAULT NULL COMMENT '提醒日期',
    `detail`       varchar(200)          DEFAULT NULL COMMENT '详情',
    `images`       varchar(200)          DEFAULT NULL COMMENT '图片',
    `create_time`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='纪念日';

DELETE
FROM `the_day`;
INSERT INTO `the_day` (`id`, `name`, `the_day_date`, `notify_date`, `detail`, `images`)
VALUES (1, 'name1', '2021-04-03', '2021-04-03', 'detail1',
        'http://image11.m1905.cn/mdb/uploadfile/2017/0518/thumb_1_168_230_20170518040407120632.jpg'),
       (2, 'name2', '2021-05-03', '2021-05-03', 'detail2',
        'http://image11.m1905.cn/mdb/uploadfile/2017/0518/thumb_1_168_230_20170518040407120632.jpg'),
       (3, 'name3', '2021-04-04', '2021-04-05', 'detail3',
        'http://image11.m1905.cn/mdb/uploadfile/2017/0518/thumb_1_168_230_20170518040407120632.jpg'),
       (4, 'name4', '2021-02-02', '2021-02-03', 'detail4',
        'http://image11.m1905.cn/mdb/uploadfile/2017/0518/thumb_1_168_230_20170518040407120632.jpg');

