package com.peipao.qdl.appeal.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author meteor.wu
 * @since 2017/7/21
 **/

public class FeedbackRecord implements Serializable {

    private Long feedbackId;
    private Byte type;
    private Long userId;
    private String content;
    private Date time;
    private Byte platform;
    private Byte operate;

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getPlatform() {
        return platform;
    }

    public void setPlatform(Byte platform) {
        this.platform = platform;
    }

    public Byte getOperate() {
        return operate;
    }

    public void setOperate(Byte operate) {
        this.operate = operate;
    }
}
