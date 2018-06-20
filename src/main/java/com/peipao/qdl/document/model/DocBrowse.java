package com.peipao.qdl.document.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author najun
 * @since 2017/10/17
 * 修订内容：t_doc_browse 实体类
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "文章浏览信息", description = "精品阅读")
public class DocBrowse {
    @ApiModelProperty(value = "主键ID")
    private Long browseId;

    @ApiModelProperty(value = "文章表主键ID")
    private Long docId;

    @ApiModelProperty(value = "浏览用户ID")
    private Long userId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "逻辑删除，默认0=未删除.有效， 1=已删除.无效")
    private Integer logicDelete;

    public Long getBrowseId() {
        return browseId;
    }

    public void setBrowseId(Long browseId) {
        this.browseId = browseId;
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

    public Integer getLogicDelete() {
        return logicDelete;
    }

    public void setLogicDelete(Integer logicDelete) {
        this.logicDelete = logicDelete;
    }
}
