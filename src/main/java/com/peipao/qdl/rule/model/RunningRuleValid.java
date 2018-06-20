package com.peipao.qdl.rule.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 方法名称：RunningRuleValid
 * 功能描述：跑步有效规则
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/11 11:38
 * 修订记录：
 */
@ApiModel("跑步有效规则")
public class RunningRuleValid {
    @ApiModelProperty("主键ID")
    private Long runningRuleValidId;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("学期ID")
    private Long semesterId;

    @ApiModelProperty("最小配速")
    private Float speedMin;

    @ApiModelProperty("最大配速")
    private Float speedMax;

    @ApiModelProperty("每次跑步计入有效里程范围最小值")
    private Float validKiometerMin;

    @ApiModelProperty("每次跑步计入有效里程范围最大值")
    private Float validKiometerMax;

    @ApiModelProperty("是否使用校园电子围栏 true=是")
    private Boolean hasRail;

    @ApiModelProperty("本条规则记录创建时间")
    private Date createTime;

    @ApiModelProperty("校园电子围栏节点集合")
    private List<RunningRuleNode> runningRuleNodeList;

    public Long getRunningRuleValidId() {
        return runningRuleValidId;
    }

    public void setRunningRuleValidId(Long runningRuleValidId) {
        this.runningRuleValidId = runningRuleValidId;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
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

    public Float getValidKiometerMin() {
        return validKiometerMin;
    }

    public void setValidKiometerMin(Float validKiometerMin) {
        this.validKiometerMin = validKiometerMin;
    }

    public Float getValidKiometerMax() {
        return validKiometerMax;
    }

    public void setValidKiometerMax(Float validKiometerMax) {
        this.validKiometerMax = validKiometerMax;
    }

    public Boolean getHasRail() {
        return hasRail;
    }

    public void setHasRail(Boolean hasRail) {
        this.hasRail = hasRail;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<RunningRuleNode> getRunningRuleNodeList() {
        return runningRuleNodeList;
    }

    public void setRunningRuleNodeList(List<RunningRuleNode> runningRuleNodeList) {
        this.runningRuleNodeList = runningRuleNodeList;
    }
}
