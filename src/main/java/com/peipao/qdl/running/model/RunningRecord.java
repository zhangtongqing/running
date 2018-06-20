package com.peipao.qdl.running.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author meteor.wu
 * @since 2017/6/26
 **/
@ApiModel(value = "Running跑步记录", description = "跑步记录")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RunningRecord implements Serializable{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键ID")
    private String runningRecordId;

    @ApiModelProperty(value = "活动ID  与跑步Id相关")
    private Long activityId;

    @ApiModelProperty(value = "学期ID")
    private Long semesterId;

    @ApiModelProperty(value = "活动的跑步ID,活动ID为空，则使用默认的跑步规则")
    private Long runningId;

    @ApiModelProperty(value = "消耗卡路里值")
    private Float calorieCount;

    @ApiModelProperty(value = "实际跑步里程值")
    private Float kilometeorCount;

    @ApiModelProperty(value = "有效跑步里程值,后台计算后保存")
    private Float validKilometeorCount;

    @ApiModelProperty(value = "时长")
    private Long duration;

    @ApiModelProperty(value = "平均速度")
    private Float equallySpeed;

    @ApiModelProperty(value = "平均配速")
    private String equallyPace;

    @ApiModelProperty(value = "userId")
    private Long userId;

    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @ApiModelProperty(value = "是否有效 88=有效")
    private byte isEffective;

    @ApiModelProperty(value = "无效原因：0 不在围栏内 1 过慢 2 过快 3低于最小有效里程 4不在规定时间内 5经过定点少于规定数量 6定向跑未按照定点顺序")
    private String invalidReason;

    @ApiModelProperty("是否删除")
    private Boolean enabled;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "跑步节点信息")
    private List<RunningNode> runningNodeList;

    @ApiModelProperty(value = "跑步类型 1,晨跑,2,趣味,3,随机,4,自由,5,定向,6,创意 ,100活动跑")
    private Byte type;

    @ApiModelProperty(value = "时长字符") //String类型便于查看数据可看 -----------------------
    private String durationStr;

    @ApiModelProperty(value = "手机简要信息") //简要的手机信息 -----------------------
    private String phoneStr;

    @ApiModelProperty(value = "登陆的用户Token,Android 有用") //Android 有用，请加上 -----------------------
    private String userToken;

    //-----------------------------------以下属性跑步结束才可以更新-----------------------------------
    @ApiModelProperty(value = "跑步得分")
    private Float runningScore;

    @ApiModelProperty(value = "最高时速")
    private Float highSpeed;

    @ApiModelProperty(value = "最低时速")
    private Float lowSpeed;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    //-----------------------------------以下属性上传成功才会更新-----------------------------------
    @ApiModelProperty(value = "缩略图图片地址")
    private String thumbUrl;

    //-----------------------------------以下属性关联活动时获得-----------------------------------
    @ApiModelProperty(value = "活动置换课时")
    private Integer activityRewardDuration;

    @ApiModelProperty(value = "记录状态: 2= 成功 -2=申诉无效,-1=申诉中, 0 = 正常")
    private Integer status;

    @ApiModelProperty(value = "跑步记录文件状态")
    private int nodeStatus;

    @ApiModelProperty(value = "跑步记录文件上传时间")
    private String nodeTime;

    @ApiModelProperty(value = "申诉时间")
    private Date appealTime;

    @ApiModelProperty(value = "处理人ID")
    private Long appealUserId;

    /*---------------------------------------------- 以下 get、set --------------------------------------------------*/
    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getRunningRecordId() {
        return runningRecordId;
    }

    public void setRunningRecordId(String runningRecordId) {
        this.runningRecordId = runningRecordId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public Long getRunningId() {
        return runningId;
    }

    public void setRunningId(Long runningId) {
        this.runningId = runningId;
    }

    public Float getCalorieCount() {
        return calorieCount;
    }

    public void setCalorieCount(Float calorieCount) {
        this.calorieCount = calorieCount;
    }

    public Float getKilometeorCount() {
        return kilometeorCount;
    }

    public void setKilometeorCount(Float kilometeorCount) {
        this.kilometeorCount = kilometeorCount;
    }

    public Float getValidKilometeorCount() {
        return validKilometeorCount;
    }

    public void setValidKilometeorCount(Float validKilometeorCount) {
        this.validKilometeorCount = validKilometeorCount;
    }

    public Float getRunningScore() {
        return runningScore;
    }

    public void setRunningScore(Float runningScore) {
        this.runningScore = runningScore;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Float getEquallySpeed() {
        return equallySpeed;
    }

    public void setEquallySpeed(Float equallySpeed) {
        this.equallySpeed = equallySpeed;
    }

    public String getEquallyPace() {
        return equallyPace;
    }

    public void setEquallyPace(String equallyPace) {
        this.equallyPace = equallyPace;
    }

    public Float getHighSpeed() {
        return highSpeed;
    }

    public void setHighSpeed(Float highSpeed) {
        this.highSpeed = highSpeed;
    }

    public Float getLowSpeed() {
        return lowSpeed;
    }

    public void setLowSpeed(Float lowSpeed) {
        this.lowSpeed = lowSpeed;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public byte getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(byte isEffective) {
        this.isEffective = isEffective;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<RunningNode> getRunningNodeList() {
        return runningNodeList;
    }

    public void setRunningNodeList(List<RunningNode> runningNodeList) {
        this.runningNodeList = runningNodeList;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getDurationStr() {
        return durationStr;
    }

    public void setDurationStr(String durationStr) {
        this.durationStr = durationStr;
    }

    public String getPhoneStr() {
        return phoneStr;
    }

    public void setPhoneStr(String phoneStr) {
        this.phoneStr = phoneStr;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Integer getActivityRewardDuration() {
        return activityRewardDuration;
    }

    public void setActivityRewardDuration(Integer activityRewardDuration) {
        this.activityRewardDuration = activityRewardDuration;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(int nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public String getNodeTime() {
        return nodeTime;
    }

    public void setNodeTime(String nodeTime) {
        this.nodeTime = nodeTime;
    }

    public String getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }

    public Date getAppealTime() {
        return appealTime;
    }

    public void setAppealTime(Date appealTime) {
        this.appealTime = appealTime;
    }

    public Long getAppealUserId() {
        return appealUserId;
    }

    public void setAppealUserId(Long appealUserId) {
        this.appealUserId = appealUserId;
    }
}
