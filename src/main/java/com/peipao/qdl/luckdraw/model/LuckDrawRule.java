package com.peipao.qdl.luckdraw.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 方法名称：LuckDrawRule
 * 功能描述：抽奖规则
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/12/15 14:54
 * 修订记录：
 */
public class LuckDrawRule implements Serializable{
    private static final long serialVersionUID = 1L;
    private Long ruleId;      //主键ID
    private Long activityId; //活动主键ID
    private String shareImg; //活动分享海报
    private Integer userDayLimit = 0; //单日同一用户中奖次数上限
    private Float colligateRate = 0f; // 综合中奖率
    private Date createTime; //创建时间
    private Long createUserId; // 创建用户ID
    private Date updateTime; // 更新时间
    private Long updateUserId; //更新用户ID
    private int logicDelete = 0; // 逻辑删除，默认0=未删除.有效， 1=已删除.无效
    private int luckType = 0;   //抽奖类型 0=自动，1=轮盘
    private Integer luckLimit = 0;  //同一用户中奖次数上限-针对活动

    private Integer isUse = 0; //是否启用抽奖

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getShareImg() {
        return shareImg;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg;
    }

    public Integer getUserDayLimit() {
        return userDayLimit;
    }

    public void setUserDayLimit(Integer userDayLimit) {
        this.userDayLimit = userDayLimit;
    }

    public Float getColligateRate() {
        return colligateRate;
    }

    public void setColligateRate(Float colligateRate) {
        this.colligateRate = colligateRate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public int getLogicDelete() {
        return logicDelete;
    }

    public void setLogicDelete(int logicDelete) {
        this.logicDelete = logicDelete;
    }

    public int getLuckType() {
        return luckType;
    }

    public void setLuckType(int luckType) {
        this.luckType = luckType;
    }

    public Integer getLuckLimit() {
        return luckLimit;
    }

    public void setLuckLimit(Integer luckLimit) {
        this.luckLimit = luckLimit;
    }

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }
}
