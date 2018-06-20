package com.peipao.qdl.running.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 方法名称：RunningLine
 * 功能描述：跑步线路打点
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/6 18:25
 * 修订记录：
 */
@ApiModel(value = "RunningLine跑步线路打点", description = "跑步线路打点")
public class RunningLine {
    @ApiModelProperty(value = "主键ID")
    private Long runningLineId;

    @ApiModelProperty(value = "跑步主表ID")
    private Long runningId;

    @ApiModelProperty(value = "纬度值")
    private Double latitude;

    @ApiModelProperty(value = "经度值")
    private Double longitude;

    @ApiModelProperty(value = "点名称")
    private String lineName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    public Long getRunningLineId() {
        return runningLineId;
    }

    public void setRunningLineId(Long runningLineId) {
        this.runningLineId = runningLineId;
    }

    public Long getRunningId() {
        return runningId;
    }

    public void setRunningId(Long runningId) {
        this.runningId = runningId;
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

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
