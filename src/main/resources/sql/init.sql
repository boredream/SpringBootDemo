-- 导出 love_cookbook 的数据库结构
DROP DATABASE IF EXISTS `sep_talk`;
CREATE DATABASE IF NOT EXISTS `sep_talk` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION = 'N' */;
USE `sep_talk`;

--
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user`
(
    id          int unsigned auto_increment comment '主键id' primary key,
    username    varchar(50)                           not null comment '用户名',
    password    varchar(100)                          null comment '密码',
    role        varchar(50)                           null comment '角色',
    open_id     varchar(50)                           null comment '第三方id',
    nickname    varchar(50)                           null comment '昵称',
    avatar      varchar(200)                          null comment '头像',
    gender      varchar(50)                           null comment '性别',
    birthday    long                                  null comment '生日',
    create_time timestamp default current_timestamp() not null,
    update_time timestamp default current_timestamp() not null on update current_timestamp()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户';

--
DROP TABLE IF EXISTS `visitor`;
CREATE TABLE IF NOT EXISTS `visitor`
(
    id                 int unsigned auto_increment comment '主键id' primary key,
    user_id            long                                  not null comment '所属用户',
    # 基本信息
    name               varchar(50)                           not null comment '姓名',
    first_contact_time long                                  not null comment '初次来访时间',
    school_num         varchar(50)                           null comment '学号',
    impression         varchar(50)                           null comment '印象',
    age                int                                   null comment '年龄',
    gender             varchar(50)                           null comment '性别',
    nationality        varchar(50)                           null comment '国籍',
    place_of_birth     varchar(50)                           null comment '出生地',
    address            varchar(50)                           null comment '现居地',
    education_level    varchar(50)                           null comment '受教育程度',
    current_occupation varchar(50)                           null comment '现职业/专业',
    religion           varchar(50)                           null comment '宗教信仰',
    marriage           varchar(50)                           null comment '婚姻状况',
    number             varchar(50)                           null comment '联系方式',
    # 历史信息
    family             varchar(50)                           null comment '家庭成员构成',
    personality        varchar(50)                           null comment '家庭成员人格特点',
    relationship       varchar(50)                           null comment '家庭成员与来访的关系',
    parent             varchar(50)                           null comment '父母关系',
    events             varchar(50)                           null comment '重大生活事件',
    trauma             varchar(50)                           null comment '既往创伤',
    self_rated         varchar(50)                           null comment '自我评价',
    peer               varchar(50)                           null comment '同伴关系',
    intimat            varchar(50)                           null comment '亲密关系',
    symptoms           varchar(50)                           null comment '症状主诉',
    present            varchar(50)                           null comment '现病史',
    past               varchar(50)                           null comment '既往病史',
    addiction          varchar(50)                           null comment '上瘾史',
    create_time        timestamp default current_timestamp() not null,
    update_time        timestamp default current_timestamp() not null on update current_timestamp()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户';

--
DROP TABLE IF EXISTS `talk_case`;
CREATE TABLE IF NOT EXISTS `talk_case`
(
    id              int unsigned auto_increment comment '主键id' primary key,
    user_id         long                                  not null comment '所属用户',
    type            int                                   not null comment '类型 1-评估 2-咨询',
    file_url        varchar(200)                          not null comment '文件地址',
    ai_parse_status int                                   null comment 'AI解析状态 0-闲置 1-解析中 2-解析成功 3-解析失败',
    visitor_id      long                                  not null comment '访客id',
    contact_time    long                                  not null comment '当次来访时间',
    create_time     timestamp default current_timestamp() not null,
    update_time     timestamp default current_timestamp() not null on update current_timestamp()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='案例';


--
DROP TABLE IF EXISTS `talk_case_detail`;
CREATE TABLE IF NOT EXISTS `talk_case_detail`
(
    id          int unsigned auto_increment comment '主键id' primary key,
    case_id     long                                  not null comment '所属案例',
    result_type varchar(50)                           not null comment '解析结果类型 result_1/2/3/4...',
    ai_result   TEXT                                  null comment '内容',
    create_time timestamp default current_timestamp() not null,
    update_time timestamp default current_timestamp() not null on update current_timestamp()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='案例详情';
