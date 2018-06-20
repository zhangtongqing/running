package com.peipao.qdl.rule.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：RunningRule
 * 功能描述：跑步规则
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/10 18:57
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
    private Integer type;
    
    
    /**************************************** 根据需求调整，配速及有效里程范围，可以在每一种跑步规则中自定义 ***************************************/
    @ApiModelProperty("每天运动次数上限")
    private int sportCountMax = 1;//每天运动次数上限,新增字段，暂时在晨跑中使用到

    @ApiModelProperty("得分方式 枚举：1=按里程得分；2=按次数得分 --默认1，按里程")
    private int getScoreType = 1;//按次数得分时，和下面的score配合使用即可，完成每次得score分

    @ApiModelProperty("是否使用默认配速true=是、false=不使用默认，加载以下自定义")
    private int useDefaultSpd = 1;
    @ApiModelProperty("最小配速")
    private Float speedMin;
    @ApiModelProperty("最大配速")
    private Float speedMax;

    @ApiModelProperty("是否使用默认有效里程设置 true=是、false=不使用默认，加载以下自定义")
    private int useDefaultKit = 1;
    @ApiModelProperty("每次跑步计入有效里程范围最小值")
    private Float validKiometerMin;

    @ApiModelProperty("每次跑步计入有效里程范围最大值")
    private Float validKiometerMax;
    /**************************************** 根据需求调整，配速及有效里程范围，可以在每一种跑步规则中自定义 ***************************************/
    

    @ApiModelProperty("perKiometer公里获得score分数")
    private Float perKiometer;

    @ApiModelProperty("perKiometer公里获得score分数")
    private Integer score;//Float

    @ApiModelProperty("随机跑每次需经过 nodeCount 个位置点")
    private Integer nodeCount;

    @ApiModelProperty("跑步开始时间(在规定时间内跑步)")
    private String startTime;

    @ApiModelProperty("跑步结束时间(在规定时间内跑步)")
    private String endTime;

    @ApiModelProperty("本条规则记录创建时间")
    private Date createTime;

    @ApiModelProperty("随机跑节点集合")
    private List<RunningRuleNode> runningRuleNodeList;

    @ApiModelProperty("趣味跑图片路径集合")
    private List<Map<String, Object>> runningRuleImgList;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Float getPerKiometer() {
        return perKiometer;
    }

    public void setPerKiometer(Float perKiometer) {
        this.perKiometer = perKiometer;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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

    public Integer getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(Integer nodeCount) {
        this.nodeCount = nodeCount;
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

    public List<Map<String, Object>> getRunningRuleImgList() {
        return runningRuleImgList;
    }

    public void setRunningRuleImgList(List<Map<String, Object>> runningRuleImgList) {
        this.runningRuleImgList = runningRuleImgList;
    }

    public int getGetScoreType() {
        return getScoreType;
    }

    public void setGetScoreType(int getScoreType) {
        this.getScoreType = getScoreType;
    }

    public int getUseDefaultSpd() {
        return useDefaultSpd;
    }

    public void setUseDefaultSpd(int useDefaultSpd) {
        this.useDefaultSpd = useDefaultSpd;
    }

    public int getUseDefaultKit() {
        return useDefaultKit;
    }

    public void setUseDefaultKit(int useDefaultKit) {
        this.useDefaultKit = useDefaultKit;
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
}
