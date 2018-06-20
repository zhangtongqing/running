package com.peipao.qdl.compensate.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 方法名称：CompensateMain
 * 功能描述：成绩补偿主表
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/11/30 16:22
 * 修订记录：
 */
@ApiModel(value = "学生成绩补偿")
public class CompensateMain {

    @ApiModelProperty(value = "统计表主键ID")
    private Long compensateId;

    @ApiModelProperty(value = "学期ID")
    private Long semesterId;

    @ApiModelProperty(value = "学生ID")
    private Long userId;

    @ApiModelProperty(value = "补偿总成绩")
    private float compensateScore = 0f;

    @ApiModelProperty(value = "补偿有效次数晨跑")
    private int morningRunningCount = 0;

    @ApiModelProperty(value = "补偿有效跑步里程")
    private float runningLength;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "更新操作用户ID")
    private Long updateUserId;

    public Long getCompensateId() {
        return compensateId;
    }

    public void setCompensateId(Long compensateId) {
        this.compensateId = compensateId;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public float getCompensateScore() {
        return compensateScore;
    }

    public void setCompensateScore(float compensateScore) {
        this.compensateScore = compensateScore;
    }

    public int getMorningRunningCount() {
        return morningRunningCount;
    }

    public void setMorningRunningCount(int morningRunningCount) {
        this.morningRunningCount = morningRunningCount;
    }

    public float getRunningLength() {
        return runningLength;
    }

    public void setRunningLength(float runningLength) {
        this.runningLength = runningLength;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }
}
