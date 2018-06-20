package com.peipao.qdl.luckdraw.model;

import java.util.Date;

/**
 * 方法名称：ActivityLuckRecord
 * 功能描述：抽奖记录
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/12/15 14:54
 * 修订记录：
 */
public class ActivityLuckRecord {
    private static final long serialVersionUID = 1L;
    Long recordId;//主键ID
    Long userId;//抽奖用户ID
    Long activityId;//活动主键ID
    String runningRecordId;//跑步记录主键ID
    int isLuck = 0;//是否中奖(0=没有  1=中奖)
    Long prizeId;//奖品id
    String prizeCode;//兑奖码
    Date createTime;//抽奖时间


    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getRunningRecordId() {
        return runningRecordId;
    }

    public void setRunningRecordId(String runningRecordId) {
        this.runningRecordId = runningRecordId;
    }

    public int getIsLuck() {
        return isLuck;
    }

    public void setIsLuck(int isLuck) {
        this.isLuck = isLuck;
    }

    public Long getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Long prizeId) {
        this.prizeId = prizeId;
    }

    public String getPrizeCode() {
        return prizeCode;
    }

    public void setPrizeCode(String prizeCode) {
        this.prizeCode = prizeCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
