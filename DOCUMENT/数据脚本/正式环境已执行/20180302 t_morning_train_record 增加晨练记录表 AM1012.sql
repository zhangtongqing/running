-- 增加晨练记录表

CREATE TABLE `t_morning_train_record` (
  `morning_train_record_id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(32) DEFAULT NULL,
  `semester_id` int(20) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `duration` int(20) DEFAULT '0',
  `duration_str` varchar(100) DEFAULT NULL,
  `is_effective` tinyint(3) DEFAULT NULL,
  `sign_latitude` double DEFAULT NULL,
  `sign_longitude` double DEFAULT NULL,
  `longitude_out` double DEFAULT NULL,
  `latitude_out` double DEFAULT NULL,
  PRIMARY KEY (`morning_train_record_id`),
  KEY `userIdIndex` (`user_id`,`semester_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

