package com.peipao.qdl.running.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author meteor.wu
 * @since 2017/6/27
 **/
@ApiModel(value = "Running晨跑", description = "晨跑")
public class MorningRunning extends Running {
    @ApiModelProperty(value = "晨跑开始时间")
    private String startTime;

    @ApiModelProperty(value = "晨跑结束时间")
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
