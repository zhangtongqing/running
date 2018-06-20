package com.peipao.qdl.statistics.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 方法名称：UserStatistic
 * 功能描述：
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/4 16:52
 * 修订记录：
 */
@ApiModel(value = "学生成绩统计表")
public class UserStatistic implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "统计表主键ID")
    private Long statisticId;
    @ApiModelProperty(value = "学校ID")
    private Long schoolId;
    @ApiModelProperty(value = "学期ID")
    private Long semesterId;
    @ApiModelProperty(value = "学生ID")
    private Long userId;

/*----------------------- 晨跑 ---------------------------*/
    @ApiModelProperty(value = "晨跑次数")
    private Integer morningRunningCount = 0;
    @ApiModelProperty(value = "晨跑里程")
    private Float morningRunningLength = 0f;
    @ApiModelProperty(value = "晨跑实际里程")
    private Float morningRunningRealLength = 0f;
/*----------------------- 自由跑 ---------------------------*/
    @ApiModelProperty(value = "自由跑次数")
    private Integer freeRunningCount = 0;
    @ApiModelProperty(value = "自由跑里程")
    private Float freeRunningLength = 0f;
    @ApiModelProperty(value = "自由跑实际里程")
    private Float freeRunningRealLength = 0f;
/*----------------------- 活动跑步 ---------------------------*/
    @ApiModelProperty(value = "活动次数")
    private Integer activityRunningCount = 0;
    @ApiModelProperty(value = "活动成绩")
    private Float activityRunningScore = 0f;
    @ApiModelProperty(value = "活动置换课时")
    private Integer activityRewardDuration = 0;
    @ApiModelProperty(value = "活动跑步里程")
    private Float activityRunningLength = 0f;
    @ApiModelProperty(value = "活动跑步实际里程")
    private Float activityRunningRealLength = 0f;
/*----------------------- 课程考勤 ---------------------------*/
    @ApiModelProperty(value = "考勤次数")
    private Integer attendanceCount = 0;
    @ApiModelProperty(value = "课程考勤得分")
    private Float attendanceScore = 0f;
    @ApiModelProperty(value = "学生上课课时")
    private Integer attendanceDuration = 0;
/*----------------------- 新增字段 ---------------------------*/
     @ApiModelProperty(value = "补偿自由跑次数")
     private int compensateFreeRunningCount = 0;
     @ApiModelProperty(value = "补偿自由跑里程")
     private Float compensateFreeRunningLength = 0f;
     @ApiModelProperty(value = "补偿晨跑次数")
     private int compensateMorningRunningCount = 0;
     @ApiModelProperty(value = "补偿晨跑里程")
     private Float compensateMorningRunningLength = 0f;
     @ApiModelProperty(value = "补偿活动跑得分")
     private Float compensateActivityScore = 0f;
    @ApiModelProperty(value = "补偿活动跑次数")
     private int compensateActivityCount = 0;
/*----------------------- 新增字段 ---------------------------*/
    @ApiModelProperty(value = "晨练次数")
    private int morningTrainCount = 0;
/*----------------------------------------------------------------------------------------------------------------*/

    public UserStatistic(){}

    public UserStatistic(long schoolId, long semesterId, long userId){
        this.schoolId = schoolId;
        this.semesterId = semesterId;
        this.userId = userId;
    }

    public Long getStatisticId() {
        return statisticId;
    }

    public void setStatisticId(Long statisticId) {
        this.statisticId = statisticId;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
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

    public Integer getMorningRunningCount() {
        return morningRunningCount;
    }

    public void setMorningRunningCount(Integer morningRunningCount) {
        this.morningRunningCount = morningRunningCount;
    }

    public Float getMorningRunningLength() {
        return morningRunningLength;
    }

    public void setMorningRunningLength(Float morningRunningLength) {
        this.morningRunningLength = morningRunningLength;
    }

    public Float getMorningRunningRealLength() {
        return morningRunningRealLength;
    }

    public void setMorningRunningRealLength(Float morningRunningRealLength) {
        this.morningRunningRealLength = morningRunningRealLength;
    }

    public Integer getFreeRunningCount() {
        return freeRunningCount;
    }

    public void setFreeRunningCount(Integer freeRunningCount) {
        this.freeRunningCount = freeRunningCount;
    }

    public Float getFreeRunningLength() {
        return freeRunningLength;
    }

    public void setFreeRunningLength(Float freeRunningLength) {
        this.freeRunningLength = freeRunningLength;
    }

    public Float getFreeRunningRealLength() {
        return freeRunningRealLength;
    }

    public void setFreeRunningRealLength(Float freeRunningRealLength) {
        this.freeRunningRealLength = freeRunningRealLength;
    }

    public Integer getActivityRunningCount() {
        return activityRunningCount;
    }

    public void setActivityRunningCount(Integer activityRunningCount) {
        this.activityRunningCount = activityRunningCount;
    }

    public Float getActivityRunningScore() {
        return activityRunningScore;
    }

    public void setActivityRunningScore(Float activityRunningScore) {
        this.activityRunningScore = activityRunningScore;
    }

    public Integer getActivityRewardDuration() {
        return activityRewardDuration;
    }

    public void setActivityRewardDuration(Integer activityRewardDuration) {
        this.activityRewardDuration = activityRewardDuration;
    }

    public Float getActivityRunningLength() {
        return activityRunningLength;
    }

    public void setActivityRunningLength(Float activityRunningLength) {
        this.activityRunningLength = activityRunningLength;
    }

    public Float getActivityRunningRealLength() {
        return activityRunningRealLength;
    }

    public void setActivityRunningRealLength(Float activityRunningRealLength) {
        this.activityRunningRealLength = activityRunningRealLength;
    }

    public Integer getAttendanceCount() {
        return attendanceCount;
    }

    public void setAttendanceCount(Integer attendanceCount) {
        this.attendanceCount = attendanceCount;
    }

    public Float getAttendanceScore() {
        return attendanceScore;
    }

    public void setAttendanceScore(Float attendanceScore) {
        this.attendanceScore = attendanceScore;
    }

    public Integer getAttendanceDuration() {
        return attendanceDuration;
    }

    public void setAttendanceDuration(Integer attendanceDuration) {
        this.attendanceDuration = attendanceDuration;
    }

    public int getCompensateFreeRunningCount() {
        return compensateFreeRunningCount;
    }

    public void setCompensateFreeRunningCount(int compensateFreeRunningCount) {
        this.compensateFreeRunningCount = compensateFreeRunningCount;
    }

    public Float getCompensateFreeRunningLength() {
        return compensateFreeRunningLength;
    }

    public void setCompensateFreeRunningLength(Float compensateFreeRunningLength) {
        this.compensateFreeRunningLength = compensateFreeRunningLength;
    }

    public int getCompensateMorningRunningCount() {
        return compensateMorningRunningCount;
    }

    public void setCompensateMorningRunningCount(int compensateMorningRunningCount) {
        this.compensateMorningRunningCount = compensateMorningRunningCount;
    }

    public Float getCompensateMorningRunningLength() {
        return compensateMorningRunningLength;
    }

    public void setCompensateMorningRunningLength(Float compensateMorningRunningLength) {
        this.compensateMorningRunningLength = compensateMorningRunningLength;
    }

    public Float getCompensateActivityScore() {
        return compensateActivityScore;
    }

    public void setCompensateActivityScore(Float compensateActivityScore) {
        this.compensateActivityScore = compensateActivityScore;
    }

    public int getCompensateActivityCount() {
        return compensateActivityCount;
    }

    public void setCompensateActivityCount(int compensateActivityCount) {
        this.compensateActivityCount = compensateActivityCount;
    }

    public int getMorningTrainCount() {
        return morningTrainCount;
    }

    public void setMorningTrainCount(int morningTrainCount) {
        this.morningTrainCount = morningTrainCount;
    }
}
