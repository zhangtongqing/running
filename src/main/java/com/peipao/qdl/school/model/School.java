package com.peipao.qdl.school.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
@ApiModel(value = "School学校信息")
public class School implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long schoolId;

    @ApiModelProperty(value = "学校名字")
    private String name;

    @ApiModelProperty(value = "联系人电话")
    private String mobile;

    @ApiModelProperty(value = "联系人")
    private String contact;

    @ApiModelProperty(value = "签约时间")
    private Date contractTime;

    @ApiModelProperty(value = "所在省,eg：北京，陕西")
    private String city;

    @ApiModelProperty(value = "学校编码")
    private String code;

    @ApiModelProperty(value = "学校logoURL")
    private String logoURL;

    @ApiModelProperty(value = "学校地址")
    private String address;

    private Boolean isAuth;

    private Integer isCon;

    private Integer usernum;

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getContractTime() {
        return contractTime;
    }

    public void setContractTime(Date contractTime) {
        this.contractTime = contractTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getAuth() {
        return isAuth;
    }

    public void setAuth(Boolean auth) {
        isAuth = auth;
    }

    public Integer getIsCon() {
        return isCon;
    }

    public void setIsCon(Integer isCon) {
        this.isCon = isCon;
    }

    public Integer getUsernum() {
        return usernum;
    }

    public void setUsernum(Integer usernum) {
        this.usernum = usernum;
    }
}

