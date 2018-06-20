package com.peipao.qdl.rule.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 方法名称：RunningRuleImg
 * 功能描述：趣味跑图片路径模型
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/11 11:47
 * 修订记录：
 */
@ApiModel("跑步规则")
public class RunningRuleImg {
    @ApiModelProperty("主键ID")
    private Long runningRuleImgId;

    @ApiModelProperty("分类跑步规则ID")
    private Long runningRuleId;

    @ApiModelProperty("图片路径")
    private String imgPath;

    @ApiModelProperty("本条规则记录创建时间")
    private Date createTime;

    public Long getRunningRuleImgId() {
        return runningRuleImgId;
    }

    public void setRunningRuleImgId(Long runningRuleImgId) {
        this.runningRuleImgId = runningRuleImgId;
    }

    public Long getRunningRuleId() {
        return runningRuleId;
    }

    public void setRunningRuleId(Long runningRuleId) {
        this.runningRuleId = runningRuleId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
