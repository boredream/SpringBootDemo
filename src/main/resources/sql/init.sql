-- 导出 love_cookbook 的数据库结构
DROP DATABASE IF EXISTS `love_cookbook`;
CREATE DATABASE IF NOT EXISTS `love_cookbook` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `love_cookbook`;

--
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user`
(
    id               int unsigned auto_increment comment '主键id' primary key,
    username         varchar(50)                           not null comment '用户名',
    password         varchar(100)                          null comment '密码',
    role             varchar(50)                           null comment '角色',
    open_id          varchar(50)                           null comment '第三方id',
    cp_user_id       int                                   null comment '伴侣用户id',
    cp_together_date varchar(50)                           null comment '伴侣在一起时间',

    nickname         varchar(50)                           null comment '昵称',
    avatar           varchar(200)                          null comment '头像',
    gender           varchar(50)                           null comment '性别',
    birthday         long                                  null comment '生日',
    create_time      timestamp default current_timestamp() not null,
    update_time      timestamp default current_timestamp() not null on update current_timestamp()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户';

--
DROP TABLE IF EXISTS `the_day`;
CREATE TABLE IF NOT EXISTS `the_day`
(
    id           int unsigned auto_increment comment '主键id' primary key,
    user_id      int                                   not null comment '所属用户id',
    name         varchar(50)                           not null comment '名称',
    the_day_date varchar(50)                           null comment '纪念日期',
    notify_type  int                                   null comment '提醒方式',
    create_time  timestamp default current_timestamp() not null,
    update_time  timestamp default current_timestamp() not null on update current_timestamp()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='纪念日';

--
DROP TABLE IF EXISTS `diary`;
CREATE TABLE IF NOT EXISTS `diary`
(
    id          int unsigned auto_increment comment '主键id' primary key,
    user_id     int                                   not null comment '所属用户id',
    content     varchar(500)                          not null comment '文字内容',
    diary_date  varchar(50)                           null comment '日记日期',
    images      varchar(3000)                         null comment '图片',
    create_time timestamp default current_timestamp() not null,
    update_time timestamp default current_timestamp() not null on update current_timestamp()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='日记';

--
DROP TABLE IF EXISTS `todo_group`;
CREATE TABLE IF NOT EXISTS `todo_group`
(
    id          int unsigned auto_increment comment '主键id' primary key,
    user_id     int                                   not null comment '所属用户id',
    icon        int                                   null comment '图标',
    name        varchar(50)                           not null comment '名称',
    create_time timestamp default current_timestamp() not null,
    update_time timestamp default current_timestamp() not null on update current_timestamp()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='清单组';

--
DROP TABLE IF EXISTS `todo`;
CREATE TABLE IF NOT EXISTS `todo`
(
    id            int unsigned auto_increment comment '主键id' primary key,
    user_id       int                                    not null comment '所属用户id',
    todo_group_id int                                    not null comment '所属清单组id',
    done          tinyint(1) default 0                   not null comment '已完成',
    name          varchar(50)                            not null comment '名称',
    done_date     varchar(50)                            null comment '完成日期',
    detail        varchar(200)                           null comment '描述',
    images        varchar(3000)                          null comment '图片',
    create_time   timestamp  default current_timestamp() not null,
    update_time   timestamp  default current_timestamp() not null on update current_timestamp()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='清单';

--
DROP TABLE IF EXISTS `feed_back`;
CREATE TABLE IF NOT EXISTS `feed_back`
(
    id          int unsigned auto_increment comment '主键id' primary key,
    user_id     int                                   not null comment '所属用户id',
    detail      varchar(200)                          null comment '描述',
    images      varchar(3000)                         null comment '图片',
    create_time timestamp default current_timestamp() not null,
    update_time timestamp default current_timestamp() not null on update current_timestamp()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='意见反馈';


