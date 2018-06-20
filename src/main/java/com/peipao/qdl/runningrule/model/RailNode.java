package com.peipao.qdl.runningrule.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 方法名称： RunningRailNode
 * 功能描述： RunningRailNode
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/12 11:58
 * 修订记录：
 */
@ApiModel("电子围栏规则")
public class RailNode {
    @ApiModelProperty("主键ID")
    private Long nodeId;

    @ApiModelProperty(value = "学期ID")
    private Long semesterId;

    @ApiModelProperty(value = "纬度值")
    private Double latitude;

    @ApiModelProperty(value = "经度值")
    private Double longitude;

    @ApiModelProperty(value = "默认 围栏=1 晨练打卡数据点=2")
    private int type = 1;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
