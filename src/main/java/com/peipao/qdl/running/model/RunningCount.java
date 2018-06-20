package com.peipao.qdl.running.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author meteor.wu
 * @since 2017/6/26
 **/
@ApiModel(value = "Running跑步统计", description = "跑步统计")
public class RunningCount {
    @ApiModelProperty(value = "主键ID")
    private Long runningCountId;

    @ApiModelProperty(value = "userId")
    private Long userId;

    @ApiModelProperty(value = "平均配速")
    private Float equallySpeed;

    @ApiModelProperty(value = "消耗卡路里值")
    private Float calorieCount;

    @ApiModelProperty(value = "里程值")
    private Float kilometerCount;

    @ApiModelProperty(value = "平均配速")
    private Float equallyPace;

    @ApiModelProperty(value = "时长")
    private Long duration;
    private Integer count;

    @ApiModelProperty(value = "排名")
    private Integer ranking;

    @ApiModelProperty(value = "类型0为天，1为周，2为月，3为本学期，4为总")
    private Integer type;

    @ApiModelProperty(value = "是否有效 是否最有效数据 1：有效 2：校外，3：过快，4过慢")
    private Boolean isEffective;

    @ApiModelProperty(value = "统计时间")
    private Long countTime;

    @ApiModelProperty(value = "上学期开始月份")
    private Date startTime;

    @ApiModelProperty(value = "上学期结束月份")
    private Date endTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
