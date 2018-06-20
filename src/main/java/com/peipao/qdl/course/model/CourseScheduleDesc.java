package com.peipao.qdl.course.model;

/**
 * @author Meteor.wu
 * @since 2018/3/2 16:45
 */

public class CourseScheduleDesc {
    private Long courseScheduleDescId;
    private Long courseId;
    private Integer weekday;
    private Integer courseIndex;
    private Integer duration;
    private String time;
    private Integer weekStart;
    private Integer weekEnd;

    public CourseScheduleDesc() {
    }

    public CourseScheduleDesc(Long courseId, Integer weekday, Integer courseIndex, Integer duration, String time, Integer weekStart, Integer weekEnd) {
        this.courseId = courseId;
        this.weekday = weekday;
        this.courseIndex = courseIndex;
        this.duration = duration;
        this.time = time;
        this.weekStart = weekStart;
        this.weekEnd = weekEnd;
    }

    public Long getCourseScheduleDescId() {
        return courseScheduleDescId;
    }

    public void setCourseScheduleDescId(Long courseScheduleDescId) {
        this.courseScheduleDescId = courseScheduleDescId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public Integer getCourseIndex() {
        return courseIndex;
    }

    public void setCourseIndex(Integer courseIndex) {
        this.courseIndex = courseIndex;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(Integer weekStart) {
        this.weekStart = weekStart;
    }

    public Integer getWeekEnd() {
        return weekEnd;
    }

    public void setWeekEnd(Integer weekEnd) {
        this.weekEnd = weekEnd;
    }
}
