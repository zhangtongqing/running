CREATE TABLE `t_compensate_main` (
  `compensate_id` int(20) NOT NULL AUTO_INCREMENT,
  `semester_id` int(20) DEFAULT NULL,
  `user_id` int(20) DEFAULT NULL,
  `compensate_score` float(6,2) DEFAULT '0.00',
  `running_length` float(6,2) DEFAULT '0.00',
  `morning_running_count` int(4) DEFAULT '0',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_user_id` int(20) DEFAULT NULL,
  PRIMARY KEY (`compensate_id`),
  KEY `user_id` (`user_id`,`semester_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

