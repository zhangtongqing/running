package com.peipao.qdl.activity.model;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author meteor.wu
 * @since 2017/7/17
 **/
public class ActivityPartinRecord implements Serializable {
    private Long activityPartinRecordId;

    @ApiModelProperty(value = "activityMemberId活动成员ID")
    private Long activityMemberId;

    @ApiModelProperty(value = "签到时间")
    private Date signTime;

    @ApiModelProperty(value = "签到地址纬度")
    private Double latitude;

    @ApiModelProperty(value = "签到地址经度")
    private Double longitude;

    @ApiModelProperty(value = "签到地址")
    private String signAddress;

    @ApiModelProperty(value = "活动参加后获取到的积分奖励")
    private Float rewardScore;

    @ApiModelProperty(value = "活动参加后获取到的课时奖励")
    private Byte rewardDuration;

    @ApiModelProperty(value = "参与活动是否有效")
    private Integer isEffective;

    @ApiModelProperty(value = "跑步记录id")
    private String runningRecordId;
    public ActivityPartinRecord(){}
    public ActivityPartinRecord(Long activityMemberId, Date signTime, Double latitude, Double longitude, String signAddress) {
        this.activityMemberId = activityMemberId;
        this.signTime = signTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.signAddress = signAddress;
    }

    public ActivityPartinRecord(Long activityMemberId, Float rewardScore, Byte rewardDuration, int isEffective, String runningRecordId) {
        this.activityMemberId = activityMemberId;
        this.rewardScore = rewardScore;
        this.rewardDuration = rewardDuration;
        this.isEffective = isEffective;
        this.runningRecordId = runningRecordId;
    }

    public Long getActivityPartinRecordId() {
        return activityPartinRecordId;
    }

    public void setActivityPartinRecordId(Long activityPartinRecordId) {
        this.activityPartinRecordId = activityPartinRecordId;
    }

    public Long getActivityMemberId() {
        return activityMemberId;
    }

    public void setActivityMemberId(Long activityMemberId) {
        this.activityMemberId = activityMemberId;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getSignAddress() {
        return signAddress;
    }

    public void setSignAddress(String signAddress) {
        this.signAddress = signAddress;
    }

    public Integer getEffective() {
        return isEffective;
    }

    public void setEffective(Integer effective) {
        isEffective = effective;
    }

    public Float getRewardScore() {
        return rewardScore;
    }

    public void setRewardScore(Float rewardScore) {
        this.rewardScore = rewardScore;
    }

    public Byte getRewardDuration() {
        return rewardDuration;
    }

    public void setRewardDuration(Byte rewardDuration) {
        this.rewardDuration = rewardDuration;
    }

    public String getRunningRecordId() {
        return runningRecordId;
    }

    public void setRunningRecordId(String runningRecordId) {
        this.runningRecordId = runningRecordId;
    }
}
