package com.peipao.qdl.guide.model;


import java.io.Serializable;
import java.util.Date;

/**
 * 指南主表
 *
 * @author Meteor.wu
 * @since 2018/1/11 10:18
 */

public class Guide implements Serializable {

    private Long guideId;

    private String title;

    private String keyword;

    private Date createTime;

    private Long createUserId;

    private Date updateTime;

    private Long updateUserId;

    // 逻辑删除，默认0=未删除.有效， 1=已删除.无效
    private Integer logicDelete;

    public Long getGuideId() {
        return guideId;
    }

    public void setGuideId(Long guideId) {
        this.guideId = guideId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public Integer getLogicDelete() {
        return logicDelete;
    }

    public void setLogicDelete(Integer logicDelete) {
        this.logicDelete = logicDelete;
    }
}
