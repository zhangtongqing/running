package com.peipao.qdl.version.model;



import java.util.Date;

/**
 * 方法名称：Version
 * 功能描述：版本查询管理
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/9/27 11:57
 * 修订记录：
 */
public class Version {
    private Long versionId;
    private int versionCode = 0;//版本号
    private int versionCodeMin = 0;//可以正常支持的最小app版本号
    private String constraintUpdateCodes = "";//有条件的强制更新的版本号信息
    private String tipUpdateCodes = "";//有条件的提示更新的版本号信息
    private int clientType = ClientType.ANDROID.ordinal();//客户端类型,值见枚举 cn.boce.cloud.common.model.ClientType
    private int appType = AppTypeEnum.STUDENT.ordinal();//app类型，0=学生端；1=老师端
    private String downloadUrl = "";//app安装包下载地址(安卓端使用)
    private int updateType = UpdataTypeEnum.TIP_UPDATE_ALL.getType();//更新类型（0=所有版本无条件提示更新，1=所有版本无条件强制更新，2=部分版本有条件提示更新，3=部分版本有条件强制更新）
    private String updateContent = "";//更新内容说明
    private Date createTime;//信息新建的时间
    private Date updateTime;//信息修改的时间
    private Long createUserId = 0L;//新建信息的用户ID
    private Long updateUserId = 0L;//修改信息的用户ID

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getConstraintUpdateCodes() {
        return constraintUpdateCodes;
    }

    public void setConstraintUpdateCodes(String constraintUpdateCodes) {
        this.constraintUpdateCodes = constraintUpdateCodes;
    }

    public String getTipUpdateCodes() {
        return tipUpdateCodes;
    }

    public void setTipUpdateCodes(String tipUpdateCodes) {
        this.tipUpdateCodes = tipUpdateCodes;
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getVersionCodeMin() {
        return versionCodeMin;
    }

    public void setVersionCodeMin(int versionCodeMin) {
        this.versionCodeMin = versionCodeMin;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }
}
