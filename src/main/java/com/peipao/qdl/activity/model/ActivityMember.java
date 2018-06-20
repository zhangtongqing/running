package com.peipao.qdl.activity.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * @author meteor.wu
 * @since 2017/6/26
 **/
@ApiModel(value = "Activity活动成员")
public class ActivityMember {

    @ApiModelProperty(value = "activityMemberId活动成员ID")
    private Long activityMemberId;

    @ApiModelProperty(value = "activityId活动ID")
    private Long activityId;

    @ApiModelProperty(value = "成员ID")
    private Long userId;

    @ApiModelProperty(value = "报名时间")
    private Date enrollTime;

    @ApiModelProperty(value = "打卡次数")
    private Integer signcount;

    @ApiModelProperty(value = "有效运动次数")
    private Integer sucCount;

    @ApiModelProperty(value = "运动里程数")
    private Float runningLenght;

    @ApiModelProperty(value = "时长")
    private Integer duration;

    @ApiModelProperty(value = "消耗卡路里值")
    private Integer calorieCount;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public ActivityMember(){

    }

    public ActivityMember(Long activityMemberId, Long activityId, Long userId, Date enrollTime, Integer signcount, Integer sucCount, Float runningLenght, Integer duration, Integer calorieCount) {
        this.activityMemberId = activityMemberId;
        this.activityId = activityId;
        this.userId = userId;
        this.enrollTime = enrollTime;
        this.signcount = signcount;
        this.sucCount = sucCount;
        this.runningLenght = runningLenght;
        this.duration = duration;
        this.calorieCount = calorieCount;
    }

    public Long getActivityMemberId() {
        return activityMemberId;
    }

    public void setActivityMemberId(Long activityMemberId) {
        this.activityMemberId = activityMemberId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getEnrollTime() {
        return enrollTime;
    }

    public void setEnrollTime(Date enrollTime) {
        this.enrollTime = enrollTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getSigncount() {
        return signcount;
    }

    public void setSigncount(Integer signcount) {
        this.signcount = signcount;
    }

    public Integer getSucCount() {
        return sucCount;
    }

    public void setSucCount(Integer sucCount) {
        this.sucCount = sucCount;
    }

    public Float getRunningLenght() {
        return runningLenght;
    }

    public void setRunningLenght(Float runningLenght) {
        this.runningLenght = runningLenght;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getCalorieCount() {
        return calorieCount;
    }

    public void setCalorieCount(Integer calorieCount) {
        this.calorieCount = calorieCount;
    }
}
