package com.peipao.qdl.discover.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 方法名称：DiscoverReportDetail
 * 功能描述：动力圈举报明细表
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@ApiModel("动力圈举报明细")
public class DiscoverReportDetail {
    @ApiModelProperty("主键ID")
    private Long reportDetailId;
    @ApiModelProperty("外键-举报主表ID")
    private Long reportId;
    @ApiModelProperty("外键-举报用户ID")
    private Long userId;
    @ApiModelProperty("创建时间")
    private Date createTime;

    public Long getReportDetailId() {
        return reportDetailId;
    }

    public void setReportDetailId(Long reportDetailId) {
        this.reportDetailId = reportDetailId;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
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
