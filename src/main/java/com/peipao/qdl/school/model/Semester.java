package com.peipao.qdl.school.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
@ApiModel(value = "Semester学期")
public class Semester {
    @ApiModelProperty(value = "主键ID")
    private Long semesterId;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "学年,eg:2016~2017学年")
    private String SemesterYear;

    @ApiModelProperty(value = "学期类型:0:上学期,1:下学期")
    private Byte semesterType;

    @ApiModelProperty(value = "学期开始时间2016-09-01")
    private Date startTime;

//    @ApiModelProperty(value = "学期结束时间2017-01-31")
//    private Date endTime;

    @ApiModelProperty(value = "学期结束时间2017-01-31")
    private Date createTime;

    public Semester(){

    }

    public Semester(Long schoolId, String semesterYear, Byte semesterType, Date startTime, Date endTime, Date createTime) {
        this.schoolId = schoolId;
        SemesterYear = semesterYear;
        this.semesterType = semesterType;
        this.startTime = startTime;
//        this.endTime = endTime;
        this.createTime = createTime;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getSemesterYear() {
        return SemesterYear;
    }

    public void setSemesterYear(String semesterYear) {
        SemesterYear = semesterYear;
    }

    public Byte getSemesterType() {
        return semesterType;
    }

    public void setSemesterType(Byte semesterType) {
        this.semesterType = semesterType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

//    public Date getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(Date endTime) {
//        this.endTime = endTime;
//    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
