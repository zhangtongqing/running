package com.peipao.qdl.runningrule.model.vo;


/**
 * 方法名称：RunningRuleVo
 * 功能描述：2.0.10-跑步规则
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/12 10:58
 * 修订记录：
 */

public class RunningRuleVo {
    private Long runningRuleId;
    private int type;
    private int hasRail = 0;
    private Float speedMin;
    private Float speedMax;
    private Float validKiometerMin;
    private Float validKiometerMax = 99999f;
    private String startTime;
    private String endTime;
    private int target;
    private int sportCountMax = 1;//每天运动次数上限,新增字段，暂时在晨跑中使用到
    private int isDefaultFlag;//是否默认通用规则 0=通用
    private int isUse;
    /*************************************************** get  set ****************************************************/

    public Long getRunningRuleId() {
        return runningRuleId;
    }

    public void setRunningRuleId(Long runningRuleId) {
        this.runningRuleId = runningRuleId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHasRail() {
        return hasRail;
    }

    public void setHasRail(int hasRail) {
        this.hasRail = hasRail;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getSportCountMax() {
        return sportCountMax;
    }

    public void setSportCountMax(int sportCountMax) {
        this.sportCountMax = sportCountMax;
    }

    public int getIsDefaultFlag() {
        return isDefaultFlag;
    }

    public void setIsDefaultFlag(int isDefaultFlag) {
        this.isDefaultFlag = isDefaultFlag;
    }

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }
}

