package com.peipao.qdl.document.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author najun
 * @since 2017/10/17
 * 修订内容：t_doc_upvote 实体类
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "文章点赞", description = "精品阅读")
public class DocUpvote {
    @ApiModelProperty(value = "主键ID")
    private Long upvoteId;

    @ApiModelProperty(value = "文章表主键ID")
    private Long docId;

    @ApiModelProperty(value = "评论用户ID")
    private Long userId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public Long getUpvoteId() {
        return upvoteId;
    }

    public void setUpvoteId(Long upvoteId) {
        this.upvoteId = upvoteId;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
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
