package com.peipao.qdl.rule.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 方法名称：RunningRuleNode
 * 功能描述：RunningRuleNode
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/11 9:50
 * 修订记录：
 */
@ApiModel("跑步规则")
public class RunningRuleNode {
    @ApiModelProperty("主键ID")
    private Long nodeId;

    @ApiModelProperty(value = "规则表ID")
    private Long runningRuleId;

    @ApiModelProperty(value = "纬度值")
    private Double latitude;

    @ApiModelProperty(value = "经度值")
    private Double longitude;

    @ApiModelProperty(value = "点名称")
    private String nodeName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "规则类型,0=跑步有效性规则")
    private Integer type;



    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getRunningRuleId() {
        return runningRuleId;
    }

    public void setRunningRuleId(Long runningRuleId) {
        this.runningRuleId = runningRuleId;
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

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
