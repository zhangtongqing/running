package com.peipao.qdl.course.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author meteor.wu
 * @since 2017/6/26
 **/
@ApiModel(value = "CourseMember课程成员")
public class CourseMember implements Serializable {
    @ApiModelProperty("主键ID")
    private Long courseMemberId;

    @ApiModelProperty("课程表ID")
    private Long courseScheduleId;

    @ApiModelProperty("课程ID")
    private Long courseId;

    @ApiModelProperty("成员ID")
    private Long userId;

    @ApiModelProperty(value = "签到点纬度")
    private Double latitude;

    @ApiModelProperty(value = "签到点经度")
    private Double longitude;

    @ApiModelProperty(value = "姓名")
    private String username;

    @ApiModelProperty(value = "性别")
    private Byte sex;

    @ApiModelProperty(value = "头像URL")
    private String imageURL;

    private Integer signType;

    private Date signTime;

    public CourseMember() {
    }

    public CourseMember(Long courseScheduleId, Long courseId, Long userId, Double latitude, Double longitude, Integer signType) {
        this.courseScheduleId = courseScheduleId;
        this.courseId = courseId;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.signType = signType;
    }

    public Long getCourseMemberId() {
        return courseMemberId;
    }

    public void setCourseMemberId(Long courseMemberId) {
        this.courseMemberId = courseMemberId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getSignType() {
        return signType;
    }

    public void setSignType(Integer signType) {
        this.signType = signType;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }
}
