-- 导出 love_cookbook 的数据库结构
DROP DATABASE IF EXISTS `sep_talk`;
CREATE DATABASE IF NOT EXISTS `sep_talk` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `sep_talk`;

--
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user`
(
    id               int unsigned auto_increment comment '主键id' primary key,
    username         varchar(50)                           not null comment '用户名',
    password         varchar(100)                          null comment '密码',
    role             varchar(50)                           null comment '角色',
    open_id          varchar(50)                           null comment '第三方id',
    nickname         varchar(50)                           null comment '昵称',
    avatar           varchar(200)                          null comment '头像',
    gender           varchar(50)                           null comment '性别',
    birthday         long                                  null comment '生日',
    create_time      timestamp default current_timestamp() not null,
    update_time      timestamp default current_timestamp() not null on update current_timestamp()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户';


