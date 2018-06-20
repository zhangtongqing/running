package com.peipao.qdl.activity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.peipao.qdl.running.model.Running;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/6/26
 **/
@ApiModel(value = "Activity活动")
public class Activity {

    @ApiModelProperty(value = "主键ID")
    private Long activityId;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "课程ID")
    private String courseId;

    @ApiModelProperty("活动说明及规则")
    private String desc;

    @ApiModelProperty(value = "活动开始时间")
    private Date startTime;

    @ApiModelProperty(value = "活动结束时间")
    private Date endTime;

    @ApiModelProperty(value = "打卡位置：纬度值")
    private Double latitude;

    @ApiModelProperty(value = "打卡位置：经度值")
    private Double longitude;

    @ApiModelProperty(value = "活动封面URL")
    private String frontCoverURL;

    @ApiModelProperty(value = "成员参与类型，0:官方，其他数字是其对应的courseId")
    private Integer memberType;

    @ApiModelProperty(value = "活动是否需要签到")
    private Boolean isSign;

    @ApiModelProperty(value = "活动报名开始时间")
    private Date enrollStartTime;

    @ApiModelProperty(value = "活动报名结束时间")
    private Date enrollEndTime;

    @JsonIgnore
    @ApiModelProperty(value = "活动记录创建时间")
    private Date createTime;

    @ApiModelProperty(value = "活动参加后获取到的积分奖励")
    private Float rewardScore;

    @ApiModelProperty(value = "create userId")
    private Long userId;

    @ApiModelProperty(value = "活动状态未开始:1,报名中:2,进行中:3,4:结束")
    private Integer status;

    @ApiModelProperty(value = "已经报名人数")
    private Integer enrollCount;

    @ApiModelProperty(value = "实际参与人数")
    private Integer signCount;

    @ApiModelProperty("有效打卡次数")
    private Integer effectiveSignCount;

    @ApiModelProperty("活动类型,ActivityTypeEnum 枚举")
    private Integer type;

    @ApiModelProperty("跑步活动,是否抽奖,0:不抽奖 1:抽奖")
    private Integer hasLuckDraw;

    @ApiModelProperty("活动氛围头像url")
    private String atmosphereURL;

    private String memberStr;

    @ApiModelProperty("缩略图url")
    private String thumbNailURL;

    @ApiModelProperty("活动报名人数上限")
    private Integer maxCapacity;

    private Running running;

    private int allSucCount;

    private String signAddress;

//    @Transient
    private String username;

    private int enrollStatus;

    private int partinStatus;

    private int sucCount;

    private int luckCount = 0;

    private List<Map<String,Object>> activityMember;

    /*****************************************setter  getter  ***********************************/
    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getFrontCoverURL() {
        return frontCoverURL;
    }

    public void setFrontCoverURL(String frontCoverURL) {
        this.frontCoverURL = frontCoverURL;
    }

    public Boolean getSign() {
        return isSign;
    }

    public void setSign(Boolean sign) {
        isSign = sign;
    }

    public Date getEnrollStartTime() {
        return enrollStartTime;
    }

    public void setEnrollStartTime(Date enrollStartTime) {
        this.enrollStartTime = enrollStartTime;
    }

    public Date getEnrollEndTime() {
        return enrollEndTime;
    }

    public void setEnrollEndTime(Date enrollEndTime) {
        this.enrollEndTime = enrollEndTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Float getRewardScore() {
        return rewardScore;
    }

    public void setRewardScore(Float rewardScore) {
        this.rewardScore = rewardScore;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getEnrollCount() {
        return enrollCount;
    }

    public void setEnrollCount(Integer enrollCount) {
        this.enrollCount = enrollCount;
    }

    public Integer getSignCount() {
        return signCount;
    }

    public void setSignCount(Integer signCount) {
        this.signCount = signCount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getEffectiveSignCount() {
        return effectiveSignCount;
    }

    public void setEffectiveSignCount(Integer effectiveSignCount) {
        this.effectiveSignCount = effectiveSignCount;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMemberStr() {
        return memberStr;
    }

    public void setMemberStr(String memberStr) {
        this.memberStr = memberStr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getHasLuckDraw() {
        return hasLuckDraw;
    }

    public void setHasLuckDraw(Integer hasLuckDraw) {
        this.hasLuckDraw = hasLuckDraw;
    }

    public String getAtmosphereURL() {
        return atmosphereURL;
    }

    public void setAtmosphereURL(String atmosphereURL) {
        this.atmosphereURL = atmosphereURL;
    }

    public String getThumbNailURL() {
        return thumbNailURL;
    }

    public void setThumbNailURL(String thumbNailURL) {
        this.thumbNailURL = thumbNailURL;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Running getRunning() {
        return running;
    }

    public void setRunning(Running running) {
        this.running = running;
    }

    public int getEnrollStatus() {
        return enrollStatus;
    }

    public void setEnrollStatus(int enrollStatus) {
        this.enrollStatus = enrollStatus;
    }

    public int getPartinStatus() {
        return partinStatus;
    }

    public void setPartinStatus(int partinStatus) {
        this.partinStatus = partinStatus;
    }

    public int getSucCount() {
        return sucCount;
    }

    public void setSucCount(int sucCount) {
        this.sucCount = sucCount;
    }

    public int getAllSucCount() {
        return allSucCount;
    }

    public void setAllSucCount(int allSucCount) {
        this.allSucCount = allSucCount;
    }

    public String getSignAddress() {
        return signAddress;
    }

    public void setSignAddress(String signAddress) {
        this.signAddress = signAddress;
    }

    public int getLuckCount() {
        return luckCount;
    }

    public void setLuckCount(int luckCount) {
        this.luckCount = luckCount;
    }

    public List<Map<String, Object>> getActivityMember() {
        return activityMember;
    }

    public void setActivityMember(List<Map<String, Object>> activityMember) {
        this.activityMember = activityMember;
    }
}
