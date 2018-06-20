CREATE TABLE `t_compensate` (
  `compensate_id` int(20) NOT NULL AUTO_INCREMENT,
  `semester_id` int(20) DEFAULT NULL,
  `user_id` int(20) DEFAULT NULL,
  `running_record_id` varchar(32) DEFAULT NULL,
  `running_length` float(6,2) DEFAULT '0.00',
  `running_count` int(4) DEFAULT '0',
  `compensate_score` float(6,2) DEFAULT '0.00',
  `running_type` smallint(4) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_user_id` int(20) DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`compensate_id`),
  KEY `user_id` (`user_id`,`semester_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;