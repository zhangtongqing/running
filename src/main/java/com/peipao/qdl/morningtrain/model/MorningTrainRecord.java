package com.peipao.qdl.morningtrain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;


@ApiModel(value = "晨练记录", description = "晨练记录")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MorningTrainRecord implements Serializable{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键ID")
    private Long morningTrainRecordId;

    @ApiModelProperty(value = "userId")
    private Long userId;

    @ApiModelProperty(value = "学期ID")
    private Long semesterId;

    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @ApiModelProperty(value = "时长")
    private Long duration;

    @ApiModelProperty(value = "时长字符") //String类型便于查看数据可看
    private String durationStr;

    @ApiModelProperty(value = "是否有效 88=有效")
    private int isEffective;

    @ApiModelProperty(value = "签到-纬度")
    private Double signLatitude;
    @ApiModelProperty(value = "签到-经度")
    private Double signLongitude;
    @ApiModelProperty(value = "签退-纬度")
    private Double latitudeOut;
    @ApiModelProperty(value = "签退-经度")
    private Double longitudeOut;

    /*---------------------------------------------- 以下 get、set --------------------------------------------------*/

    public Long getMorningTrainRecordId() {
        return morningTrainRecordId;
    }

    public void setMorningTrainRecordId(Long morningTrainRecordId) {
        this.morningTrainRecordId = morningTrainRecordId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getDurationStr() {
        return durationStr;
    }

    public void setDurationStr(String durationStr) {
        this.durationStr = durationStr;
    }

    public int getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(int isEffective) {
        this.isEffective = isEffective;
    }

    public Double getSignLatitude() {
        return signLatitude;
    }

    public void setSignLatitude(Double signLatitude) {
        this.signLatitude = signLatitude;
    }

    public Double getSignLongitude() {
        return signLongitude;
    }

    public void setSignLongitude(Double signLongitude) {
        this.signLongitude = signLongitude;
    }

    public Double getLatitudeOut() {
        return latitudeOut;
    }

    public void setLatitudeOut(Double latitudeOut) {
        this.latitudeOut = latitudeOut;
    }

    public Double getLongitudeOut() {
        return longitudeOut;
    }

    public void setLongitudeOut(Double longitudeOut) {
        this.longitudeOut = longitudeOut;
    }

}
