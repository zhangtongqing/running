package com.peipao.qdl.course.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author meteor.wu
 * @since 2017/6/27
 **/
@ApiModel(value = "CourseSchedule课程表")
public class CourseSchedule {
    @ApiModelProperty(value = "主键ID")
    private Long courseScheduleId;

    @ApiModelProperty(value = "课程ID")
    private Long courseId;

    @ApiModelProperty(value = "课程周序号")
    private Integer weekIndex;

    @ApiModelProperty(value = "课程上课时间日 2017-07-04")
    private String date;

    @ApiModelProperty(value = "课程上课时间日 10:00~12:00")
    private String time;

    @ApiModelProperty(value = "课程时长 2个课时")
    private Integer duration;

    @ApiModelProperty(value = "课程第几节课 第1节课")
    private Integer courseIndex;

    @ApiModelProperty(value = "老师名字")
    private String username;

    @ApiModelProperty(value = "已经签到人数")
    private Integer signCount;

    @ApiModelProperty(value = "课程表状态 2:报名中,3:报名中,4:结束")
    private String status;

    @ApiModelProperty("CourseScheduleDesc主键ID")
    private Long descId;

//    private Long courseScheduleDescId;

    public CourseSchedule() {
    }

    public CourseSchedule(Long courseScheduleId, Long courseId, Integer weekIndex, String date, String time, Integer duration, Integer courseIndex){
        this.courseScheduleId = courseScheduleId;
        this.courseId = courseId;
//        this.courseScheduleDescId = courseScheduleDescId;
        this.weekIndex = weekIndex;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.courseIndex = courseIndex;
    }

    public Long getCourseScheduleId() {
        return courseScheduleId;
    }

    public void setCourseScheduleId(Long courseScheduleId) {
        this.courseScheduleId = courseScheduleId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Integer getWeekIndex() {
        return weekIndex;
    }

    public void setWeekIndex(Integer weekIndex) {
        this.weekIndex = weekIndex;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getCourseIndex() {
        return courseIndex;
    }

    public void setCourseIndex(Integer courseIndex) {
        this.courseIndex = courseIndex;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getSignCount() {
        return signCount;
    }

    public void setSignCount(Integer signCount) {
        this.signCount = signCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getDescId() {
        return descId;
    }

    public void setDescId(Long descId) {
        this.descId = descId;
    }
}
