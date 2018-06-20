package com.peipao.qdl.running.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 方法名称：UploadImg
 * 功能描述：大后台上传图片后保存图片路径
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/28 10:31
 * 修订记录：
 */
@ApiModel(value = "大后台各种上传的图片路径")
public class UploadImg {
    @ApiModelProperty(value = "主键ID")
    private Long imgId;
    @ApiModelProperty(value = "图片路径")
    private String imgUrl;
    @ApiModelProperty(value = "图片描述")
    private String imgDesc;
    @ApiModelProperty(value = "图片状态")
    private byte imgStatus;
    @ApiModelProperty(value = "所属功能模块分类")
    private byte modelType;
    @ApiModelProperty(value = "学校ID")
    private byte schoolId;
    @ApiModelProperty(value = "上传时间")
    private Date uploadTim;
    @ApiModelProperty(value = "上传userId")
    private Long uploadUserId;


    public Long getImgId() {
        return imgId;
    }

    public void setImgId(Long imgId) {
        this.imgId = imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgDesc() {
        return imgDesc;
    }

    public void setImgDesc(String imgDesc) {
        this.imgDesc = imgDesc;
    }

    public byte getImgStatus() {
        return imgStatus;
    }

    public void setImgStatus(byte imgStatus) {
        this.imgStatus = imgStatus;
    }

    public byte getModelType() {
        return modelType;
    }

    public void setModelType(byte modelType) {
        this.modelType = modelType;
    }

    public Date getUploadTim() {
        return uploadTim;
    }

    public void setUploadTim(Date uploadTim) {
        this.uploadTim = uploadTim;
    }

    public Long getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(Long uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    public byte getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(byte schoolId) {
        this.schoolId = schoolId;
    }
}
