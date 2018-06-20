package com.peipao.qdl.document.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author najun
 * @since 2017/10/17
 * 修订内容：t_doc_tag 实体类
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "文章标签", description = "精品阅读")
public class DocTag {
    @ApiModelProperty(value = "主键ID")
    private Long docTagId;

    @ApiModelProperty(value = "文章主键ID")
    private Long docId;

    @ApiModelProperty(value = "文章标签")
    private String tagName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "逻辑删除，默认0=未删除.有效， 1=已删除.无效")
    private Integer logicDelete;

    public Long getDocTagId() {
        return docTagId;
    }

    public void setDocTagId(Long docTagId) {
        this.docTagId = docTagId;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
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
