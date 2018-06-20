package com.peipao.qdl.discover.model;


import com.peipao.framework.constant.WebConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 方法名称：HotParams
 * 功能描述：动力圈自动热门参数表
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@ApiModel("动力圈自动热门参数")
public class HotParams {
    @ApiModelProperty("主键ID")
    private Long hotParamsId;
    @ApiModelProperty("点赞数指标")
    private int upvoteAmount = 50;
    @ApiModelProperty("评论数指标")
    private int commentAmount = 50;
    @ApiModelProperty("逻辑删除，默认0=未删除.有效， 1=已删除.无效")
    private int logicDelete;
    @ApiModelProperty("创建时间")
    private Date createTime;

    public HotParams(){}

    public HotParams(Long hotParamsId, int upvoteAmount, int commentAmount) {
        this.hotParamsId = hotParamsId;
        this.upvoteAmount = upvoteAmount;
        this.commentAmount = commentAmount;
        this.createTime = new Date();
        this.logicDelete = WebConstants.Boolean.FALSE.ordinal();
    }

    public HotParams(int upvoteAmount, int commentAmount) {
        this.upvoteAmount = upvoteAmount;
        this.commentAmount = commentAmount;
        this.createTime = new Date();
        this.logicDelete = WebConstants.Boolean.FALSE.ordinal();
    }

    public Long getHotParamsId() {
        return hotParamsId;
    }

    public void setHotParamsId(Long hotParamsId) {
        this.hotParamsId = hotParamsId;
    }

    public int getUpvoteAmount() {
        return upvoteAmount;
    }

    public void setUpvoteAmount(int upvoteAmount) {
        this.upvoteAmount = upvoteAmount;
    }

    public int getCommentAmount() {
        return commentAmount;
    }

    public void setCommentAmount(int commentAmount) {
        this.commentAmount = commentAmount;
    }

    public int getLogicDelete() {
        return logicDelete;
    }

    public void setLogicDelete(int logicDelete) {
        this.logicDelete = logicDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
