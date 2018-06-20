package com.peipao.qdl.discover.model;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 方法名称：DiscoverUpvote
 * 功能描述：动力圈点赞表
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@ApiModel("动力圈点赞")
public class DiscoverUpvote {
    @ApiModelProperty("主键ID")
    private Long upvoteId;
    @ApiModelProperty("外键-动力圈主键ID")
    private Long discoverId;
    @ApiModelProperty("评论用户ID")
    private Long userId;
    @ApiModelProperty("创建时间")
    private Date createTime;

    public DiscoverUpvote(){}

    public DiscoverUpvote(Long discoverId, Long userId) {
        this.discoverId = discoverId;
        this.userId = userId;
        this.createTime = new Date();
    }

    public Long getUpvoteId() {
        return upvoteId;
    }

    public void setUpvoteId(Long upvoteId) {
        this.upvoteId = upvoteId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
