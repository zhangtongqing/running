package com.peipao.qdl.school.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 方法名称：UserSchool
 * 功能描述：UserSchool
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/11 19:14
 * 修订记录：
 */

@ApiModel(value = "Semester学期")
public class UserSchool {
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户身份类型 1=学生")
    private Integer userType;

    @ApiModelProperty(value = "用户所在学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "当前学期ID")
    private Long semesterId;

    @ApiModelProperty(value = "课程ID")
    private Long courseId;

    //...可以增加其他user相关的业务参数到此


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
