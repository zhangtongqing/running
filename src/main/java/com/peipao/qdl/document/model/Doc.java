package com.peipao.qdl.document.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author najun
 * @since 2017/10/17
 * 修订内容：t_doc 实体类
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "文章", description = "精品阅读")
public class Doc {
    @ApiModelProperty(value = "主键ID")
    private Long docId;

    @ApiModelProperty(value = "文章标题")
    private String docTitle;

    @ApiModelProperty(value = "列表展现方式")
    private Integer viewType;

    @ApiModelProperty(value = "文章链接")
    private String docUrl;

    @ApiModelProperty(value = "排序权重1-9999")
    private Integer sortWeight;

    @ApiModelProperty(value = "阅读量")
    private Long readAmount;

    @ApiModelProperty(value = "点赞量")
    private Long upvoteAmount;

    @ApiModelProperty(value = "评论数")
    private Long commentAmount;

    @ApiModelProperty(value = "转发量")
    private Long forwardAmount;

    @ApiModelProperty(value = "创建用户ID")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间(默认预创建时间相同)")
    private Date updateTime;

    @ApiModelProperty(value = "文章发布状态，默认0=草稿")
    private Integer publishState;

    @ApiModelProperty(value = "逻辑删除，默认0=未删除.有效， 1=已删除.无效")
    private Integer logicDelete;

    @ApiModelProperty(value = "文章内容")
    private String docContent;

    @ApiModelProperty(value = "创建用户名")
    private String createUserName;

    @ApiModelProperty(value = "是否是热点文章")
    private Integer isHot;

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public Integer getViewType() {
        return viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public Integer getSortWeight() {
        return sortWeight;
    }

    public void setSortWeight(Integer sortWeight) {
        this.sortWeight = sortWeight;
    }

    public Long getReadAmount() {
        return readAmount;
    }

    public void setReadAmount(Long readAmount) {
        this.readAmount = readAmount;
    }

    public Long getUpvoteAmount() {
        return upvoteAmount;
    }

    public void setUpvoteAmount(Long upvoteAmount) {
        this.upvoteAmount = upvoteAmount;
    }

    public Long getCommentAmount() {
        return commentAmount;
    }

    public void setCommentAmount(Long commentAmount) {
        this.commentAmount = commentAmount;
    }

    public Long getForwardAmount() {
        return forwardAmount;
    }

    public void setForwardAmount(Long forwardAmount) {
        this.forwardAmount = forwardAmount;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getPublishState() {
        return publishState;
    }

    public void setPublishState(Integer publishState) {
        this.publishState = publishState;
    }

    public Integer getLogicDelete() {
        return logicDelete;
    }

    public void setLogicDelete(Integer logicDelete) {
        this.logicDelete = logicDelete;
    }

    public String getDocContent() {
        return docContent;
    }

    public void setDocContent(String docContent) {
        this.docContent = docContent;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }
}
