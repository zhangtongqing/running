alter table `t_running_record` add column `invalid_reason` varchar(20) DEFAULT NULL;
alter table `t_running_record` add column `appeal_user_id` int(11) DEFAULT NULL;
alter table `t_running_record` add column `appeal_time` datetime DEFAULT NULL;

-- 删除字段 type
alter table `t_running_record` drop column `node_key`;

-- 为老数据填充该字段
update `t_running_record` set `invalid_reason`=is_effective where is_effective != 88 and invalid_reason is null;
