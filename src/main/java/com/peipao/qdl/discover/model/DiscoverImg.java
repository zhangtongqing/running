package com.peipao.qdl.discover.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 方法名称：DiscoverImg
 * 功能描述：动力圈图片表
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@ApiModel("动力圈图片")
public class DiscoverImg {

    @ApiModelProperty("主键ID")
    private Long imgId;
    @ApiModelProperty("外键-动力圈表主键ID")
    private Long discoverId;
    @ApiModelProperty("图片地址")
    private String imgUrl;
    @ApiModelProperty("图片排序号")
    private int sortNum;

    public Long getImgId() {
        return imgId;
    }

    public void setImgId(Long imgId) {
        this.imgId = imgId;
    }

    public Long getDiscoverId() {
        return discoverId;
    }

    public void setDiscoverId(Long discoverId) {
        this.discoverId = discoverId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }
}
