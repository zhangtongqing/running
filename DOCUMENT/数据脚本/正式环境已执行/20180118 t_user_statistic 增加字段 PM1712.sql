alter table `t_user_statistic` add column `compensate_free_running_length` float DEFAULT '0';
alter table `t_user_statistic` add column `compensate_free_running_count` int(4) DEFAULT '0';
alter table `t_user_statistic` add column `compensate_morning_running_count` int(4) DEFAULT '0';
alter table `t_user_statistic` add column `compensate_morning_running_length` float DEFAULT '0';
alter table `t_user_statistic` add column `compensate_activity_score` float DEFAULT '0';
alter table `t_user_statistic` add column `compensate_activity_count` int(4) DEFAULT 0;

-- alter table `t_user_statistic` add column `is_lock` smallint(4) DEFAULT '0';


-- 删除字段
/*
alter table `t_user_statistic` drop column `morning_running_score`;
alter table `t_user_statistic` drop column `free_running_score`;
alter table `t_user_statistic` drop column `random_running_count`;
alter table `t_user_statistic` drop column `random_running_length`;
alter table `t_user_statistic` drop column `random_running_real_length`;
alter table `t_user_statistic` drop column `random_running_score`;
alter table `t_user_statistic` drop column `amusing_running_count`;
alter table `t_user_statistic` drop column `amusing_running_length`;
alter table `t_user_statistic` drop column `amusing_running_real_length`;
alter table `t_user_statistic` drop column `amusing_running_score`;
*/








