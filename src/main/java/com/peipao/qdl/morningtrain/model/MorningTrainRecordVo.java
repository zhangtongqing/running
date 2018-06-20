package com.peipao.qdl.morningtrain.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.peipao.qdl.running.model.RunningNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@ApiModel(value = "Running跑步记录", description = "跑步记录")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MorningTrainRecordVo implements Serializable{
    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "当前时间戳")
    private Long currTimeLong;

    /*---------------------------------------------- 以下 get、set --------------------------------------------------*/

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

    public Long getCurrTimeLong() {
        return currTimeLong;
    }

    public void setCurrTimeLong(Long currTimeLong) {
        this.currTimeLong = currTimeLong;
    }
}
