package com.peipao.qdl.document.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author najun
 * @since 2017/10/17
 * 修订内容：t_doc_img 实体类
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "文章标题图片", description = "精品阅读")
public class DocImg {
    @ApiModelProperty(value = "主键ID")
    private Long docImgId;

    @ApiModelProperty(value = "文章主键ID")
    private Long docId;

    @ApiModelProperty(value = "图片地址")
    private String imgUrl;

    @ApiModelProperty(value = "图片排序号")
    private Integer sortNum;

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public Long getDocImgId() {
        return docImgId;
    }

    public void setDocImgId(Long docImgId) {
        this.docImgId = docImgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }
}
