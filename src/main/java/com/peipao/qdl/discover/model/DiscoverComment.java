package com.peipao.qdl.discover.model;


import com.peipao.framework.constant.WebConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 方法名称：DiscoverComment
 * 功能描述：动力圈评论表
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@ApiModel("动力圈评论")
public class DiscoverComment {

    @ApiModelProperty("主键ID")
    private Long commentId;
    @ApiModelProperty("外键-动力圈主键ID")
    private Long discoverId;
    @ApiModelProperty("评论用户ID")
    private Long userId;
    @ApiModelProperty("评论内容")
    private String commentContent;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("逻辑删除，默认0=未删除.有效， 1=已删除.无效")
    private int logicDelete;

    public DiscoverComment() {}

    public DiscoverComment(Long discoverId, Long userId, String commentContent) {
        this.discoverId = discoverId;
        this.userId = userId;
        this.commentContent = commentContent;
        this.createTime = new Date();
        this.logicDelete = WebConstants.Boolean.FALSE.ordinal();
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getDiscoverId() {
        return discoverId;
    }

    public void setDiscoverId(Long discoverId) {
        this.discoverId = discoverId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getLogicDelete() {
        return logicDelete;
    }

    public void setLogicDelete(int logicDelete) {
        this.logicDelete = logicDelete;
    }
}
