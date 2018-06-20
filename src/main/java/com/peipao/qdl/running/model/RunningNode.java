package com.peipao.qdl.running.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author meteor.wu
 * @since 2017/6/26
 **/
@ApiModel(value = "RunningNode跑步节点", description = "跑步节点")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RunningNode implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键ID")
    private String nodeId;

    @ApiModelProperty(value = "跑步ID")
    private String runningRecordId;

    @ApiModelProperty(value = "userId")
    private Long userId;

    @ApiModelProperty(value = "当前时间yyyy-MM-dd HH:mm:ss")
    private Date time;

    @ApiModelProperty(value = "当前用时00:00:00")
    private String duration;

    @ApiModelProperty(value = "当前已经跑的距离：m")
    private Float length;

    @ApiModelProperty(value = "消耗卡路里值")
    private Integer calorieCount;

    @ApiModelProperty(value = "当前定位类型")     //----String类型，如Gps，网络定位，基站定位，wifi定位
    private String locationInfo;

    @ApiModelProperty(value = "当前定位精度")
    private Float locationPrecision;

    @ApiModelProperty(value = "是否为公里节点")
    private Boolean isKilometerNode;

    @ApiModelProperty(value = "轨迹颜色")
    private String color;

    @ApiModelProperty(value = "纬度值")
    private Double latitude;

    @ApiModelProperty(value = "经度值")
    private Double longitude;

    @ApiModelProperty(value = "是否有效")     //改为0～5,表示无效的原因状态，类型用Byte？？
    private Byte isEffective;

    @ApiModelProperty(value = "是否为暂停节点, 0：不是，1：是")
    private Byte pauseStatus;

    @ApiModelProperty(value = "平均速度km/h")
    private Float equallySpeed;

    @ApiModelProperty(value = "平均配速00'00")
    private String equallyPace;

    @ApiModelProperty(value = "瞬时速度km/h")
    private Float momentSpeed;

    @ApiModelProperty(value = "排序")
    private Integer sort;


    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getRunningRecordId() {
        return runningRecordId;
    }

    public void setRunningRecordId(String runningRecordId) {
        this.runningRecordId = runningRecordId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public Integer getCalorieCount() {
        return calorieCount;
    }

    public void setCalorieCount(Integer calorieCount) {
        this.calorieCount = calorieCount;
    }

    public Float getLocationPrecision() {
        return locationPrecision;
    }

    public void setLocationPrecision(Float locationPrecision) {
        this.locationPrecision = locationPrecision;
    }

    public Boolean getIsKilometerNode() {
        return isKilometerNode;
    }

    public void setIsKilometerNode(Boolean isKilometerNode) {
        this.isKilometerNode = isKilometerNode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public Byte getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(Byte isEffective) {
        this.isEffective = isEffective;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }

    public Byte getPauseStatus() {
        return pauseStatus;
    }

    public void setPauseStatus(Byte pauseStatus) {
        this.pauseStatus = pauseStatus;
    }

    public String getEquallyPace() {
        return equallyPace;
    }

    public void setEquallyPace(String equallyPace) {
        this.equallyPace = equallyPace;
    }

    public Float getEquallySpeed() {
        return equallySpeed;
    }

    public void setEquallySpeed(Float equallySpeed) {
        this.equallySpeed = equallySpeed;
    }

    public Float getMomentSpeed() {
        return momentSpeed;
    }

    public void setMomentSpeed(Float momentSpeed) {
        this.momentSpeed = momentSpeed;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
