-- 导出 spring_boot_demo 的数据库结构
DROP DATABASE IF EXISTS `spring_boot_demo`;
CREATE DATABASE IF NOT EXISTS `spring_boot_demo` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `spring_boot_demo`;

DROP TABLE IF EXISTS `todo`;
CREATE TABLE IF NOT EXISTS `todo` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `type` varchar(50) DEFAULT NULL COMMENT '类型',
  `todo_date` varchar(50) DEFAULT NULL COMMENT '代办日期',
  `notify_date` varchar(50) DEFAULT NULL COMMENT '提醒日期',
  `detail` varchar(200) DEFAULT NULL COMMENT '详情',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办事项';

DELETE FROM `todo`;
INSERT INTO `todo` (`id`, `name`, `type`, `todo_date`, `notify_date`, `detail`) VALUES
	(1, 'name1', '出游', '2021-04-03 10:41:25', '2021-04-03 10:41:25', 'detail1'),
	(2, 'name2', '出游', '2021-05-03 10:41:25', '2021-05-03 10:41:25', 'detail2'),
	(3, 'name3', '出游', '2021-04-04 10:41:25', '2021-04-05 10:41:25', 'detail3'),
	(4, 'name4', '出游', '2021-02-02 10:41:25', '2021-02-03 10:41:25', 'detail4');
