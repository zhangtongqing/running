package com.peipao.qdl.document.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author najun
 * @since 2017/10/17
 * 修订内容：t_doc_comment 实体类
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "文章评论", description = "精品阅读")
public class DocComment {
    @ApiModelProperty(value = "主键ID")
    private Long commentId;

    @ApiModelProperty(value = "文章主键ID")
    private Long docId;

    @ApiModelProperty(value = "评论用户ID")
    private Long userId;

    @ApiModelProperty(value = "评论内容")
    private String commentContent;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "逻辑删除，默认0=未删除.有效， 1=已删除.无效")
    private Integer logicDelete;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "学校名")
    private String schoolName;

    @ApiModelProperty(value = "评论用户头像链接")
    private String userImg;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
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

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getLogicDelete() {
        return logicDelete;
    }

    public void setLogicDelete(Integer logicDelete) {
        this.logicDelete = logicDelete;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserImg() {
        return userImg;
    }
}
