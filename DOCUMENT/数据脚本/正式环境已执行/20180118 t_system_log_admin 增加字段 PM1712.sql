-- 增加字段 params
alter table `t_system_log_admin` add column `params` varchar(1000) DEFAULT NULL;

-- 删除字段 type
alter table `t_system_log_admin` drop column type;
