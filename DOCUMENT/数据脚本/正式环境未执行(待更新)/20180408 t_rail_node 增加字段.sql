ALTER TABLE t_rail_node ADD COLUMN rail_name VARCHAR(50) DEFAULT NULL COMMENT '围栏名称' AFTER type ;
#轮盘抽奖
ALTER TABLE t_luck_draw_rule ADD COLUMN luckType int DEFAULT 0 COMMENT '抽奖类型0=自动，1=轮盘' AFTER activity_id ;
ALTER TABLE t_luck_draw_rule ADD COLUMN luckLimit int DEFAULT NULL COMMENT '活动抽奖上限次数' AFTER luckType ;
