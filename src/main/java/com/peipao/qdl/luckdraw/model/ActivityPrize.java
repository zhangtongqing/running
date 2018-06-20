package com.peipao.qdl.luckdraw.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 方法名称：ActivityPrize
 * 功能描述：活动奖品
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/12/15 14:54
 * 修订记录：
 */
public class ActivityPrize implements Serializable {
    private static final long serialVersionUID = 1L;
    Long prizeId; //主键ID
    Long activityId; //活动主键ID
    String prizeTitle; //奖品名称
    Integer prizeTotal; //奖品数量
    Integer prizeSendTotal; //已派出数量
    Float prizeWeight; //权重
    Integer userLimit; //同一用户可抽中次数限制
    Date startTime; //当前奖品抽奖开始时间
    Date endTime; //当前奖品抽奖结束时间
    Date createTime; //创建时间
    Long createUserId; //创建用户ID
    Date updateTime; //更新时间
    Long updateUserId; //更新用户ID
    int logicDelete = 0; //逻辑删除，默认0=未删除.有效， 1=已删除.无效
    String prizeImg;//中奖展示的图片链接

    long key;

    public Long getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Long prizeId) {
        this.prizeId = prizeId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getPrizeTitle() {
        return prizeTitle;
    }

    public void setPrizeTitle(String prizeTitle) {
        this.prizeTitle = prizeTitle;
    }

    public Integer getPrizeTotal() {
        return prizeTotal;
    }

    public void setPrizeTotal(Integer prizeTotal) {
        this.prizeTotal = prizeTotal;
    }

    public Integer getPrizeSendTotal() {
        return prizeSendTotal;
    }

    public void setPrizeSendTotal(Integer prizeSendTotal) {
        this.prizeSendTotal = prizeSendTotal;
    }

    public Float getPrizeWeight() {
        return prizeWeight;
    }

    public void setPrizeWeight(Float prizeWeight) {
        this.prizeWeight = prizeWeight;
    }

    public Integer getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(Integer userLimit) {
        this.userLimit = userLimit;
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

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getPrizeImg() {
        return prizeImg;
    }

    public void setPrizeImg(String prizeImg) {
        this.prizeImg = prizeImg;
    }
}
