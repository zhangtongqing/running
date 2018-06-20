package com.peipao.qdl.runningrule.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 方法名称：RunningRule
 * 功能描述：2.0.10-跑步规则
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/12 10:58
 * 修订记录：
 */
@ApiModel("跑步规则")
public class RunningRule {
    @ApiModelProperty("主键ID")
    private Long runningRuleId;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("学期ID")
    private Long semesterId;

    @ApiModelProperty("跑步类型")
    private int type;

    /**************************************** 根据需求调整，配速及有效里程范围***************************************/
    @ApiModelProperty("每天运动次数上限")
    private int sportCountMax = 1;//每天运动次数上限,新增字段，暂时在晨跑中使用到

    @ApiModelProperty("得分方式 枚举：1=按里程得分；2=按次数得分 --默认1，按里程")
    private int getScoreType = 1;

    @ApiModelProperty("是否开启围栏：1=开启；0=关闭 --默认0，关闭围栏")
    private int hasRail = 0;

    @ApiModelProperty("最小配速")
    private Float speedMin;

    @ApiModelProperty("最大配速")
    private Float speedMax;

    @ApiModelProperty("每次跑步计入有效里程范围最小值")
    private Float validKiometerMin;

    @ApiModelProperty("每次跑步计入有效里程范围最大值")
    private Float validKiometerMax = 99999f;
    /**************************************** 根据需求调整，配速及有效里程范围***************************************/
    @ApiModelProperty("跑步开始时间(在规定时间内跑步)")
    private String startTime;

    @ApiModelProperty("跑步结束时间(在规定时间内跑步)")
    private String endTime;

    @ApiModelProperty("学期目标")
    private int target;

    @ApiModelProperty("本条规则记录创建时间")
    private Date createTime;

    @ApiModelProperty("修改人ID")
    private long createUser;

    @ApiModelProperty("是否启用0=关闭，1=启用 默认")
    private int isUse=1;

    /*************************************************** get  set ****************************************************/
    public Long getRunningRuleId() {
        return runningRuleId;
    }

    public void setRunningRuleId(Long runningRuleId) {
        this.runningRuleId = runningRuleId;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getGetScoreType() {
        return getScoreType;
    }

    public void setGetScoreType(int getScoreType) {
        this.getScoreType = getScoreType;
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

    public int getSportCountMax() {
        return sportCountMax;
    }

    public void setSportCountMax(int sportCountMax) {
        this.sportCountMax = sportCountMax;
    }

    public int getHasRail() {
        return hasRail;
    }

    public void setHasRail(int hasRail) {
        this.hasRail = hasRail;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(long createUser) {
        this.createUser = createUser;
    }

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }
}
