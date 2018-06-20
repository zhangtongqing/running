package com.peipao.qdl.running.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author meteor.wu
 * @since 2017/6/27
 * 修订内容：删除属性Byte intervalLength[获取学分基础里程],Byte score[完成每一个基础里程所获取的学分]
 *           增加属性 学校Id，学期Id
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Running跑步", description = "跑步")
public class Running {
    @ApiModelProperty(value = "主键ID")
    private Long runningId;

    @ApiModelProperty(value = "活动ID")
    private Long activityId;

    @ApiModelProperty(value = "跑步最低里程")
    private Float length;

    @ApiModelProperty(value = "配速-区间开始")
    private Float speedMin;

    @ApiModelProperty(value = "配速-区间结束")
    private Float speedMax;

    @ApiModelProperty(value = "跑步经过点的数量")
    private Integer nodeCount;

    @ApiModelProperty(value = "跑步类型1,晨跑,2,趣味,3,随机,4,自由,5,定向,6,创意,100活动跑")
    private Integer type;

    private Boolean passNode;

    private Boolean byOrder;

    @ApiModelProperty(value = "跑步打点,多个点集合List")
    private List<RunningLine> runningLineList;

    public Long getRunningId() {
        return runningId;
    }

    public void setRunningId(Long runningId) {
        this.runningId = runningId;
    }

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public Float getSpeedMin() {
        return speedMin;
    }

    public void setSpeedMin(Float speedMin) {
        this.speedMin = speedMin;
    }

    public Float getSpeedMax() {
        return speedMax;
    }

    public void setSpeedMax(Float speedMax) {
        this.speedMax = speedMax;
    }

    public Integer getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(Integer nodeCount) {
        this.nodeCount = nodeCount;
    }

    public List<RunningLine> getRunningLineList() {
        return runningLineList;
    }

    public void setRunningLineList(List<RunningLine> runningLineList) {
        this.runningLineList = runningLineList;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getPassNode() {
        return passNode;
    }

    public void setPassNode(Boolean passNode) {
        this.passNode = passNode;
    }

    public Boolean getByOrder() {
        return byOrder;
    }

    public void setByOrder(Boolean byOrder) {
        this.byOrder = byOrder;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
}
