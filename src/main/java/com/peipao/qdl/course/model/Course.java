package com.peipao.qdl.course.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * @author meteor.wu
 * @since 2017/6/26
 **/
@ApiModel(value = "Course课程")
public class Course {

    private Long key;

    @ApiModelProperty(value = "主键ID")
    private Long courseId;

    @ApiModelProperty(value = "课程名字")
    private String name;

    @ApiModelProperty(value = "所属学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "学期ID")
    private Long semesterId;

    @ApiModelProperty(value = "授课老师")
    private Long userId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "备注")
    private String desc;

    @ApiModelProperty(value = "签到点纬度")
    private Double latitude;

    @ApiModelProperty(value = "签到点经度")
    private Double longitude;

    @ApiModelProperty("打卡位置是否校验")
    private Integer needSignLocation;

    @ApiModelProperty("打卡时间：开课后x分钟内")
    private Integer signIntevalTime;

    private String username;

//    @ApiModelProperty("人数是否限制")
//    private Integer isMaxCapacity;

    @ApiModelProperty("限制人数")
    private Integer maxCapacity;

    private List<CourseSchedule> courseScheduleList;

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<CourseSchedule> getCourseScheduleList() {
        return courseScheduleList;
    }

    public void setCourseScheduleList(List<CourseSchedule> courseScheduleList) {
        this.courseScheduleList = courseScheduleList;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Integer getNeedSignLocation() {
        return needSignLocation;
    }

    public void setNeedSignLocation(Integer needSignLocation) {
        this.needSignLocation = needSignLocation;
    }

    public Integer getSignIntevalTime() {
        return signIntevalTime;
    }

    public void setSignIntevalTime(Integer signIntevalTime) {
        this.signIntevalTime = signIntevalTime;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}
