CREATE TABLE `t_rail_node` (
  `node_id` int(20) NOT NULL AUTO_INCREMENT,
  `semester_id` int(20) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  KEY `ruleIdIndex` (`semester_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;


CREATE TABLE `t_run_rule` (
  `running_rule_id` int(20) NOT NULL AUTO_INCREMENT,
  `school_id` int(20) NOT NULL,
  `semester_id` int(20) NOT NULL,
  `type` smallint(2) NOT NULL,
  `has_rail` tinyint(3) NOT NULL DEFAULT '0',
  `start_time` varchar(6) DEFAULT NULL,
  `end_time` varchar(6) DEFAULT NULL,
  `speed_min` float DEFAULT '0',
  `speed_max` float DEFAULT '0',
  `valid_kiometer_min` float DEFAULT '0',
  `valid_kiometer_max` float DEFAULT '99999',
  `get_score_type` tinyint(1) DEFAULT '1',
  `sport_count_max` int(10) DEFAULT '1',
  `target` smallint(4) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `create_user` int(20) DEFAULT NULL,
  PRIMARY KEY (`running_rule_id`),
  KEY `semesterIndex` (`semester_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

