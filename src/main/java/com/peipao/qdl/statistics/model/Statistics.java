package com.peipao.qdl.statistics.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author meteor.wu
 * @since 2017/6/27
 **/
@ApiModel(value = "Statistics成绩统计模板")
public class Statistics {
    @ApiModelProperty(value = "学期时间")
    private String semesterTime;

    @ApiModelProperty(value = "有效里程数")
    private Float runninglength;

    @ApiModelProperty(value = "有效得分")
    private Integer runningScore;

    @ApiModelProperty(value = "课程得分")
    private Float courseScore;

    @ApiModelProperty(value = "活动得分")
    private Float activtiyScore;

    @ApiModelProperty(value = "是否达标")
    private Boolean isAchievement;

    @ApiModelProperty(value = "晨跑次数")
    private Integer morningRunningCount;


    public String getSemesterTime() {
        return semesterTime;
    }

    public void setSemesterTime(String semesterTime) {
        this.semesterTime = semesterTime;
    }

    public Float getRunninglength() {
        return runninglength;
    }

    public void setRunninglength(Float runninglength) {
        this.runninglength = runninglength;
    }

    public Integer getRunningScore() {
        return runningScore;
    }

    public void setRunningScore(Integer runningScore) {
        this.runningScore = runningScore;
    }

    public Float getCourseScore() {
        return courseScore;
    }

    public void setCourseScore(Float courseScore) {
        this.courseScore = courseScore;
    }

    public Float getActivtiyScore() {
        return activtiyScore;
    }

    public void setActivtiyScore(Float activtiyScore) {
        this.activtiyScore = activtiyScore;
    }

    public Boolean getAchievement() {
        return isAchievement;
    }

    public void setAchievement(Boolean achievement) {
        isAchievement = achievement;
    }

    public Integer getMorningRunningCount() {
        return morningRunningCount;
    }

    public void setMorningRunningCount(Integer morningRunningCount) {
        this.morningRunningCount = morningRunningCount;
    }
}
