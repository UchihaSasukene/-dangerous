/*
SQLyog Ultimate v8.32 
MySQL - 8.0.16 : Database - vueone
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`vueone` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `vueone`;

/*Table structure for table `chemical` */

DROP TABLE IF EXISTS `chemical`;

CREATE TABLE `chemical` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '危化品名称',
  `category` varchar(50) NOT NULL COMMENT '类别',
  `danger_level` varchar(20) NOT NULL COMMENT '危险等级',
  `storage_condition` varchar(200) NOT NULL COMMENT '存储条件',
  `warning_threshold` decimal(10,2) NOT NULL COMMENT '预警阈值',
  `description` text COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='危化品信息表';

/*Data for the table `chemical` */

insert  into `chemical`(`id`,`name`,`category`,`danger_level`,`storage_condition`,`warning_threshold`,`description`,`create_time`,`update_time`,`create_by`,`update_by`) values (1,'1','爆炸品','一级','1','0.30','111','2025-03-08 21:08:52','2025-03-08 21:08:52',NULL,NULL),(2,'硫酸','腐蚀品','一级','阴凉干燥处','100.00','强酸，具有强烈的腐蚀性','2025-03-08 21:11:45','2025-03-08 21:11:45',NULL,NULL),(3,'甲醇','易燃品','二级','通风处','200.00','易燃易爆，具有毒性','2025-03-08 21:11:45','2025-03-08 21:11:45',NULL,NULL),(4,'硝酸','腐蚀品','一级','阴凉处','150.00','强氧化性酸，具有强烈的腐蚀性','2025-03-08 21:11:45','2025-03-08 21:11:45',NULL,NULL),(5,'乙醇','易燃品','三级','阴凉干燥处','300.00','易燃，易挥发','2025-03-08 21:11:45','2025-03-08 21:11:45',NULL,NULL),(6,'12','爆炸品','二级','323','0.20','3232','2025-03-08 21:29:03','2025-03-08 21:29:03',NULL,NULL),(7,'2332','爆炸物','二级','2332','0.20','测试描述','2025-03-08 21:29:12','2025-03-22 12:50:29',NULL,NULL),(8,'t','腐蚀品','一级','t','0.10','tt','2025-03-08 21:29:23','2025-03-08 21:29:23',NULL,NULL),(9,'tt','腐蚀品','二级','t','0.10',NULL,'2025-03-08 21:29:33','2025-03-08 21:29:33',NULL,NULL),(10,'t','爆炸品','二级','tt','0.20','111','2025-03-08 21:29:38','2025-03-24 14:55:12',NULL,NULL),(11,'tt','腐蚀品','一级','t','0.20','2312','2025-03-08 21:29:44','2025-03-24 14:55:38',NULL,NULL),(12,'硫酸','腐蚀品','一级','阴凉干燥处','100.00','强酸，具有强烈的腐蚀性','2025-03-09 15:04:25','2025-03-09 15:04:25',NULL,NULL),(13,'甲醇','易燃品','二级','通风处','200.00','易燃易爆，具有毒性','2025-03-09 15:04:25','2025-03-09 15:04:25',NULL,NULL),(14,'硝酸','腐蚀品','一级','阴凉处','150.10','强氧化性酸，具有强烈的腐蚀性','2025-03-09 15:04:25','2025-04-23 11:12:01',NULL,NULL),(15,'乙醇','易燃品','三级','阴凉干燥处','300.00','易燃，易挥发','2025-03-09 15:04:25','2025-03-09 15:04:25',NULL,NULL);

/*Table structure for table `inventory` */

DROP TABLE IF EXISTS `inventory`;

CREATE TABLE `inventory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `chemical_id` bigint(20) NOT NULL COMMENT '危化品ID',
  `current_amount` decimal(10,2) NOT NULL COMMENT '当前库存量',
  `unit` varchar(20) NOT NULL COMMENT '单位',
  `location` varchar(100) DEFAULT NULL COMMENT '存储位置',
  `last_check_time` datetime DEFAULT NULL COMMENT '最后盘点时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_chemical_id` (`chemical_id`),
  CONSTRAINT `inventory_ibfk_1` FOREIGN KEY (`chemical_id`) REFERENCES `chemical` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='库存信息表';

/*Data for the table `inventory` */

insert  into `inventory`(`id`,`chemical_id`,`current_amount`,`unit`,`location`,`last_check_time`,`create_time`,`update_time`) values (1,1,'1.20','2','1','2025-03-24 23:48:23','2025-03-14 16:48:16','2025-04-25 23:29:02'),(2,2,'100.00','10.','木叶村村口','2025-03-08 17:22:45','2025-03-11 17:22:53','2025-03-11 19:22:55'),(3,3,'7.50','L','危化品仓库B区-01','2025-04-25 23:36:00','2025-04-25 23:36:00','2025-04-25 23:36:00'),(4,4,'6.20','L','危化品仓库B区-02','2025-04-25 23:36:00','2025-04-25 23:36:00','2025-04-25 23:36:00'),(5,5,'8.50','L','危化品仓库A区-03','2025-04-25 23:36:00','2025-04-25 23:36:00','2025-04-25 23:36:00'),(6,6,'8.70','L','危化品仓库C区-01','2025-04-25 23:36:00','2025-04-25 23:36:00','2025-04-25 23:46:44'),(7,7,'8.80','L','危化品仓库C区-02','2025-04-25 23:36:00','2025-04-25 23:36:00','2025-04-25 23:48:16'),(8,8,'8.00','kg','普通仓库D区-01','2025-04-25 23:36:00','2025-04-25 23:36:00','2025-04-25 23:36:00'),(9,9,'6.80','L','危化品仓库B区-03','2025-04-25 23:36:00','2025-04-25 23:36:00','2025-04-25 23:36:00'),(10,10,'10.00','L','普通仓库D区-02','2025-04-25 23:35:38','2025-04-25 23:35:38','2025-04-25 23:35:38'),(11,11,'7.30','kg','普通仓库D区-03','2025-04-25 23:35:39','2025-04-25 23:35:39','2025-04-25 23:35:39'),(12,12,'5.00','kg','普通仓库D区-04','2025-04-25 23:35:39','2025-04-25 23:35:39','2025-04-25 23:35:39'),(13,13,'0.80','kg','危化品仓库A区-04','2025-04-25 23:35:39','2025-04-25 23:35:39','2025-04-25 23:35:39'),(14,14,'1.20','L','危化品仓库A区-05','2025-04-25 23:35:39','2025-04-25 23:35:39','2025-04-25 23:35:39'),(15,15,'0.50','L','危化品仓库C区-03','2025-04-25 23:35:39','2025-04-25 23:35:39','2025-04-25 23:35:39');

/*Table structure for table `man` */

DROP TABLE IF EXISTS `man`;

CREATE TABLE `man` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '名字',
  `phone` varchar(15) DEFAULT NULL,
  `gender` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '性别',
  `email` varchar(255) NOT NULL,
  `department` varchar(100) DEFAULT NULL,
  `position` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='学生表';

/*Data for the table `man` */

insert  into `man`(`id`,`name`,`phone`,`gender`,`email`,`department`,`position`,`create_time`) values (1,'张三1','14778305832','男','3050952301@qq.com','木叶','下忍',NULL),(2,'李四','15778603582','女','222@qq.com','好部门','摸鱼',NULL),(3,'王五','14222235156','男','333@qq.com','111','11',NULL),(4,'赵六','15123123123','男','444@qq.com','2131','2',NULL),(5,'钱七','13756158951','女','555@qq.com','宇智波','跑龙套',NULL),(8,'李四','32','男','上海市浦东新区',NULL,NULL,NULL),(9,'王五','25','女','广州市天河区',NULL,NULL,NULL),(11,'钱七','35','女','杭州市西湖区',NULL,NULL,NULL),(12,'张三','28','男','北京市海淀区',NULL,NULL,NULL),(13,'李四','32','男','上海市浦东新区',NULL,NULL,NULL),(14,'王五','25','女','广州市天河区',NULL,NULL,NULL),(15,'赵六','40','男','深圳市南山区',NULL,NULL,NULL),(16,'钱七','35','女','杭州市西湖区',NULL,NULL,NULL),(17,'张五','28','男','北京市海淀区',NULL,NULL,NULL),(18,'fuker','32','男','上海市浦东新区',NULL,NULL,NULL),(19,'老王','25','女','广州市天河区',NULL,NULL,NULL),(20,'老赵','40','男','深圳市南山区',NULL,NULL,NULL),(22,NULL,NULL,NULL,'',NULL,NULL,NULL),(23,'zbc','15778603582','男','123456789@qq.com','宇智波','跑龙套',NULL);

/*Table structure for table `outbound_record` */

DROP TABLE IF EXISTS `outbound_record`;

CREATE TABLE `outbound_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `chemical_id` bigint(20) NOT NULL COMMENT '危化品ID',
  `amount` decimal(10,2) NOT NULL COMMENT '出库数量',
  `unit` varchar(20) NOT NULL COMMENT '单位',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `outbound_time` datetime NOT NULL COMMENT '出库时间',
  `operator_id` int(11) NOT NULL COMMENT '操作员ID',
  `recipient` varchar(100) NOT NULL COMMENT '领用人/部门',
  `purpose` varchar(200) DEFAULT NULL COMMENT '用途',
  `notes` text COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `operator_id` (`operator_id`),
  KEY `idx_chemical_id` (`chemical_id`),
  KEY `idx_outbound_time` (`outbound_time`),
  CONSTRAINT `outbound_record_ibfk_1` FOREIGN KEY (`chemical_id`) REFERENCES `chemical` (`id`),
  CONSTRAINT `outbound_record_ibfk_2` FOREIGN KEY (`operator_id`) REFERENCES `man` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='出库记录表';

/*Data for the table `outbound_record` */

insert  into `outbound_record`(`id`,`chemical_id`,`amount`,`unit`,`batch_no`,`outbound_time`,`operator_id`,`recipient`,`purpose`,`notes`,`create_time`) values (1,1,'1.00','1','1','2025-03-10 13:47:49',1,'1','1',NULL,'2025-03-10 16:47:57'),(2,2,'10.00','L','SUL2024031501','2025-03-15 10:00:00',1,'实验室A组','酸碱中和实验','用于常规实验','2025-03-15 10:00:00'),(3,2,'5.00','L','SUL2024031501','2025-03-15 14:30:00',2,'实验室B组','设备清洗','用于清洗实验器材','2025-03-15 14:30:00'),(4,3,'8.00','L','MET2024031501','2025-03-15 10:30:00',3,'实验室C组','样品制备','用于样品溶解','2025-03-15 10:30:00'),(5,3,'6.00','L','MET2024031501','2025-03-15 15:00:00',4,'实验室D组','实验分析','用于色谱分析','2025-03-15 15:00:00'),(6,4,'7.00','L','NIT2024031501','2025-03-15 11:30:00',5,'实验室E组','溶液配制','配制标准溶液','2025-03-15 11:30:00'),(7,4,'0.00','L','NIT2024031501','2025-03-15 16:00:00',1,'实验室F组','实验分析','用于滴定分析','2025-03-15 16:00:00'),(8,5,'9.00','L','ETH2024031501','2025-03-15 13:30:00',2,'实验室G组','溶剂萃取','用于有机物萃取','2025-03-15 13:30:00'),(9,5,'0.00','L','ETH2024031501','2025-03-15 17:30:00',3,'实验室H组','设备清洗','用于清洗实验器材','2025-03-15 17:30:00');

/*Table structure for table `storage_record` */

DROP TABLE IF EXISTS `storage_record`;

CREATE TABLE `storage_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `chemical_id` bigint(20) NOT NULL COMMENT '危化品ID',
  `amount` decimal(10,2) NOT NULL COMMENT '入库数量',
  `unit` varchar(20) NOT NULL COMMENT '单位',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `storage_time` datetime NOT NULL COMMENT '入库时间',
  `operator_id` int(11) NOT NULL COMMENT '操作员ID',
  `supplier` varchar(100) DEFAULT NULL COMMENT '供应商',
  `notes` text COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `operator_id` (`operator_id`),
  KEY `idx_chemical_id` (`chemical_id`),
  KEY `idx_storage_time` (`storage_time`),
  CONSTRAINT `storage_record_ibfk_1` FOREIGN KEY (`chemical_id`) REFERENCES `chemical` (`id`),
  CONSTRAINT `storage_record_ibfk_2` FOREIGN KEY (`operator_id`) REFERENCES `man` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='入库记录表';

/*Data for the table `storage_record` */

insert  into `storage_record`(`id`,`chemical_id`,`amount`,`unit`,`batch_no`,`storage_time`,`operator_id`,`supplier`,`notes`,`create_time`) values (1,1,'1.00','1','1','2025-03-10 16:47:31',1,'1','1','2025-03-10 16:47:37'),(2,2,'50.00','L','SUL2024031501','2025-03-15 09:30:00',1,'华东化工有限公司','新批次硫酸入库，用于实验室常规使用','2025-03-15 09:30:00'),(4,3,'40.00','L','MET2024031501','2025-03-15 10:15:00',3,'北方化工有限公司','新批次甲醇入库，用于实验分析','2025-03-15 10:15:00'),(5,3,'25.00','L','MET2024031502','2025-03-15 15:45:00',4,'东方化工有限公司','补充库存，用于样品制备','2025-03-15 15:45:00'),(6,4,'35.00','L','NIT2024031501','2025-03-15 11:00:00',5,'西部化工有限公司','新批次硝酸入库，用于实验分析','2025-03-15 11:00:00'),(7,4,'20.00','L','NIT2024031502','2025-03-15 16:30:00',1,'南方化工有限公司','补充库存，用于溶液配制','2025-03-15 16:30:00'),(8,5,'45.00','L','ETH2024031501','2025-03-15 13:00:00',2,'中原化工有限公司','新批次乙醇入库，用于实验分析','2025-03-15 13:00:00'),(9,5,'30.00','L','ETH2024031502','2025-03-15 17:00:00',3,'东南化工有限公司','补充库存，用于设备清洗1','2025-03-15 17:00:00');

/*Table structure for table `usage_record` */

DROP TABLE IF EXISTS `usage_record`;

CREATE TABLE `usage_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `chemical_id` bigint(20) NOT NULL COMMENT '危化品ID',
  `user_id` int(11) NOT NULL COMMENT '使用人ID',
  `amount` decimal(10,2) NOT NULL COMMENT '使用量',
  `unit` varchar(20) NOT NULL COMMENT '单位',
  `usage_time` datetime NOT NULL COMMENT '使用时间',
  `usage_purpose` varchar(200) NOT NULL COMMENT '使用目的',
  `notes` text COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_chemical_id` (`chemical_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_usage_time` (`usage_time`),
  CONSTRAINT `usage_record_ibfk_1` FOREIGN KEY (`chemical_id`) REFERENCES `chemical` (`id`),
  CONSTRAINT `usage_record_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `man` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='使用记录表';

/*Data for the table `usage_record` */

insert  into `usage_record`(`id`,`chemical_id`,`user_id`,`amount`,`unit`,`usage_time`,`usage_purpose`,`notes`,`create_time`) values (1,1,1,'1.00','1','2025-03-08 16:48:37','1','1','2025-03-10 16:48:44'),(2,1,1,'2.50','L','2025-03-10 09:30:00','实验室测试','用于酸碱中和实验','2025-03-10 09:30:00'),(3,2,2,'1.00','L','2025-03-10 10:15:00','清洗设备','用于清洗实验器材','2025-03-10 10:15:00'),(4,3,3,'0.50','L','2025-03-09 14:20:00','样品制备','用于样品溶解','2025-03-09 14:20:00'),(5,4,1,'1.50','kg','2025-03-09 11:30:00','溶液配制','配制标准溶液','2025-03-09 11:30:00'),(6,5,4,'0.75','L','2025-03-08 16:45:00','实验分析','用于色谱分析','2025-03-08 16:45:00'),(7,1,2,'1.20','L','2025-03-08 10:10:00','实验室测试','用于酸碱滴定','2025-03-08 10:10:00'),(8,6,3,'0.30','kg','2025-03-07 15:20:00','样品制备','用于样品前处理','2025-03-07 15:20:00'),(9,7,5,'2.00','L','2025-03-07 09:40:00','溶剂萃取','用于有机物萃取','2025-03-07 09:40:00'),(10,8,4,'0.25','L','2025-03-06 13:50:00','实验分析','用于光谱分析','2025-03-06 13:50:00'),(11,9,1,'1.80','L','2025-03-06 11:15:00','反应溶剂','用作有机合成溶剂','2025-03-06 11:15:00'),(12,10,5,'0.90','kg','2025-03-05 14:30:00','缓冲液配制','用于配制pH缓冲液','2025-03-05 14:30:00'),(13,2,3,'1.50','L','2025-03-05 10:20:00','清洗设备','用于清洗反应釜','2025-03-05 10:20:00'),(14,3,2,'0.60','L','2025-03-04 16:10:00','样品制备','用于样品稀释','2025-03-04 16:10:00'),(15,4,4,'1.20','kg','2025-03-04 11:45:00','溶液配制','配制标准缓冲液','2025-03-04 11:45:00'),(16,5,1,'0.40','L','2025-03-03 15:30:00','实验分析','用于液相色谱分析','2025-03-03 15:30:00'),(17,1,1,'5.00','kg','2025-04-17 17:01:51','实验研究','样例数据1','2025-04-17 17:01:51'),(18,2,2,'3.00','L','2025-04-17 17:01:51','生产用途','样例数据21','2025-04-17 17:01:51'),(19,3,1,'2.00','g','2025-04-17 17:01:51','教学演示','样例数据3','2025-04-17 17:01:51'),(20,1,1,'5.00','kg','2025-04-17 17:09:02','测试用途','测试数据1','2025-04-17 17:09:02'),(21,2,2,'3.00','L','2025-04-17 17:09:02','测试用途','测试数据2','2025-04-17 17:09:02');

/*Table structure for table `warning_record` */

DROP TABLE IF EXISTS `warning_record`;

CREATE TABLE `warning_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '预警记录ID',
  `chemical_id` int(11) NOT NULL COMMENT '化学品ID',
  `warning_type` varchar(50) NOT NULL COMMENT '预警类型：stock(库存预警)、low(库存不足)、high(超储预警)',
  `warning_level` varchar(50) NOT NULL COMMENT '预警等级：normal(一般)、serious(严重)、urgent(紧急)',
  `warning_content` varchar(500) NOT NULL COMMENT '预警内容',
  `status` varchar(50) NOT NULL DEFAULT 'unprocessed' COMMENT '处理状态：unprocessed(未处理)、processing(处理中)、processed(已处理)',
  `warning_time` datetime NOT NULL COMMENT '预警时间',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handler` varchar(100) DEFAULT NULL COMMENT '处理人',
  `handle_result` varchar(500) DEFAULT NULL COMMENT '处理结果',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_chemical_id` (`chemical_id`),
  KEY `idx_warning_type` (`warning_type`),
  KEY `idx_status` (`status`),
  KEY `idx_warning_time` (`warning_time`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='预警记录表';

/*Data for the table `warning_record` */

insert  into `warning_record`(`id`,`chemical_id`,`warning_type`,`warning_level`,`warning_content`,`status`,`warning_time`,`handle_time`,`handler`,`handle_result`,`create_time`) values (1,1,'low','urgent','硫酸库存不足，当前库存低于预警阈值，请及时补充','unprocessed','2025-03-10 10:00:00',NULL,NULL,NULL,'2025-03-10 10:00:00'),(2,2,'stock','serious','盐酸库存接近预警阈值，请注意库存管理','processing','2025-03-09 14:30:00',NULL,'张三',NULL,'2025-03-09 14:30:00'),(3,3,'high','normal','乙醇库存超出安全存储量，请注意安全风险','processed','2025-03-08 09:15:00','2025-03-08 11:20:00','李四','已调整存储方案，分散存储降低风险','2025-03-08 09:15:00'),(4,1,'low','serious','硫酸库存持续下降，即将耗尽','unprocessed','2025-03-10 16:45:00',NULL,NULL,NULL,'2025-03-10 16:45:00'),(5,4,'stock','normal','氢氧化钠库存接近预警阈值','processed','2025-03-07 13:20:00','2025-03-07 15:30:00','王五','已下单补充库存，预计3天后到货','2025-03-07 13:20:00'),(6,5,'high','urgent','甲醇库存严重超标，存在重大安全隐患','processing','2025-03-10 08:30:00',NULL,'赵六',NULL,'2025-03-10 08:30:00'),(7,6,'low','urgent','丙酮库存不足，影响正常生产','unprocessed','2025-03-10 11:10:00',NULL,NULL,NULL,'2025-03-10 11:10:00'),(8,7,'stock','serious','二氯甲烷库存接近预警阈值','unprocessed','2025-03-09 16:20:00',NULL,NULL,NULL,'2025-03-09 16:20:00'),(9,8,'high','normal','乙醚库存超出安全存储量','processed','2025-03-06 10:45:00','2025-03-06 14:15:00','钱七','已转移部分库存至备用仓库','2025-03-06 10:45:00'),(10,9,'low','urgent','四氢呋喃库存不足，请紧急补充','processing','2025-03-10 09:50:00',NULL,'孙八',NULL,'2025-03-10 09:50:00'),(11,10,'stock','normal','乙酸库存接近预警阈值','processed','2025-03-05 15:30:00','2025-03-05 17:00:00','周九','已安排采购补充库存','2025-03-05 15:30:00'),(12,2,'low','serious','化学品ID:2的库存量低于预警阈值，当前库存量：100.0','unprocessed','2025-04-25 09:36:38',NULL,NULL,NULL,'2025-04-25 17:36:38');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
