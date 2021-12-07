USE `love_cookbook`;

--
DROP TABLE IF EXISTS `recommend_the_day`;
CREATE TABLE IF NOT EXISTS `recommend_the_day`
(
    id           int unsigned auto_increment comment '主键id' primary key,
    name         varchar(50)                           not null comment '名称',
    the_day_date varchar(50)                           null comment '纪念日期',
    notify_type  int                                   null comment '提醒方式',
    create_time  timestamp default current_timestamp() not null,
    update_time  timestamp default current_timestamp() not null on update current_timestamp()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='纪念日推荐';

--
DROP TABLE IF EXISTS `recommend_todo_group`;
CREATE TABLE IF NOT EXISTS `recommend_todo_group`
(
    id          int unsigned auto_increment comment '主键id' primary key,
    name        varchar(50)                           not null comment '名称',
    create_time timestamp default current_timestamp() not null,
    update_time timestamp default current_timestamp() not null on update current_timestamp()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='清单组推荐';

--
DROP TABLE IF EXISTS `recommend_todo`;
CREATE TABLE IF NOT EXISTS `recommend_todo`
(
    id              int unsigned auto_increment comment '主键id' primary key,
    todo_group_name varchar(50)                            not null comment '所属清单组名称',
    done            tinyint(1) default 0                   not null comment '已完成',
    name            varchar(50)                            not null comment '名称',
    done_date       varchar(50)                            null comment '完成日期',
    detail          varchar(200)                           null comment '描述',
    images          varchar(3000)                          null comment '图片',
    create_time     timestamp  default current_timestamp() not null,
    update_time     timestamp  default current_timestamp() not null on update current_timestamp()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='清单推荐';

--

INSERT INTO `recommend_the_day` (name)
values ('情人节');
INSERT INTO `recommend_the_day` (name)
values ('第一次相识');
INSERT INTO `recommend_the_day` (name)
values ('第一次牵手');

--

INSERT INTO `recommend_todo_group` (name)
values ('爱的初体验');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('爱的初体验', '一起看一场电影');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('爱的初体验', '一起去跑步');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('爱的初体验', '拍一张合照');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('爱的初体验', '为对方做一顿饭');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('爱的初体验', '一起逛超市');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('爱的初体验', '一起骑一次单车');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('爱的初体验', '一起秀一次恩爱');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('爱的初体验', '一起去爬山');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('爱的初体验', '一起看一场演唱会');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('爱的初体验', '一起去游泳');

--

INSERT INTO `recommend_todo_group` (name)
values ('享受慢时光');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('享受慢时光', '一起去旅行');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('享受慢时光', '一起去自驾');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('享受慢时光', '一起看日出');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('享受慢时光', '一起看日落');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('享受慢时光', '一起去看海');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('享受慢时光', '互相推荐一本好书');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('享受慢时光', '一起泡温泉');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('享受慢时光', '一起看烟火');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('享受慢时光', '一起冥想');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('享受慢时光', '一起跑一天图书馆');

--

INSERT INTO `recommend_todo_group` (name)
values ('挑战不可能');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('挑战不可能', '一起做过山车');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('挑战不可能', '一起去蹦极');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('挑战不可能', '一起去跳伞');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('挑战不可能', '一起做热气球');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('挑战不可能', '一起去潜水');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('挑战不可能', '一起学习一个新技能');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('挑战不可能', '拍100张合照');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('挑战不可能', '一起做一天义工');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('挑战不可能', '午夜一起看恐怖片');
INSERT INTO `recommend_todo` (todo_group_name, name)
values ('挑战不可能', '来一次说走就走的旅行');