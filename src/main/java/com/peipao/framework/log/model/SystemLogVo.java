package com.peipao.framework.log.model;


import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 方法名称：SystemLogVo
 * 功能描述：日志VO
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/8/7 14:30
 * 修订记录：
 */
public class SystemLogVo {

    private Long key;

    @ApiModelProperty(value = "主键ID")
    private Long logId;

    @ApiModelProperty(value = "序号")
    private Long number;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "方法")
    private String method;

//    @ApiModelProperty(value = "日志类型")
//    private Integer type;

    @ApiModelProperty(value = "IP地址")
    private String ip;

//    @ApiModelProperty(value = "用户ID")
//    private String userId;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "姓名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "用户类型1：学生，2：老师")//详见UserTypeEnum枚举
    private Integer userType;

    @ApiModelProperty(value = "学校名字")
    private String schoolName;

    @ApiModelProperty(value = "学校是否签约 0=未签约[青动力官方]，1=已签约")
    private Integer isAuth;


    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }
}
