package com.peipao.qdl.discover.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 方法名称：Discover
 * 功能描述：发现-动力圈实体类
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@ApiModel("发现-动力圈")
public class Discover {

    @ApiModelProperty("主键ID")
    private Long discoverId;
    @ApiModelProperty("文本内容")
    private String discoverContent;
    @ApiModelProperty("点赞量")
    private int upvoteAmount;
    @ApiModelProperty("评论数")
    private int commentAmount;
    @ApiModelProperty("举报次数")
    private int reportAmount;
    @ApiModelProperty("位置")
    private String location;
    @ApiModelProperty("创建用户ID")
    private Long createUserId;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("是否控制HOT，默认0=不控制，1=被控制")
    private int hotControl = 0;
    @ApiModelProperty("是否HOT，默认0=否")
    private int isHot = 0;
    @ApiModelProperty("逻辑删除，默认0=未删除.有效， 1=已删除.无效")
    private int logicDelete = 0;

    public Long getDiscoverId() {
        return discoverId;
    }

    public void setDiscoverId(Long discoverId) {
        this.discoverId = discoverId;
    }

    public String getDiscoverContent() {
        return discoverContent;
    }

    public void setDiscoverContent(String discoverContent) {
        this.discoverContent = discoverContent;
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

    public int getReportAmount() {
        return reportAmount;
    }

    public void setReportAmount(int reportAmount) {
        this.reportAmount = reportAmount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public int getHotControl() {
        return hotControl;
    }

    public void setHotControl(int hotControl) {
        this.hotControl = hotControl;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }

    public int getLogicDelete() {
        return logicDelete;
    }

    public void setLogicDelete(int logicDelete) {
        this.logicDelete = logicDelete;
    }
}
