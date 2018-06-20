package com.peipao.qdl.discover.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 方法名称：DiscoverReport
 * 功能描述：动力圈举报主表
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@ApiModel("动力圈举报")
public class DiscoverReport {
    @ApiModelProperty("主键ID")
    private Long reportId;
    @ApiModelProperty("外键-动力圈主键ID")
    private Long discoverId;
    @ApiModelProperty("举报处理状态(0=待处理；1=已屏蔽动力圈信息；2=已审核无违规)")
    private int dealState;
    @ApiModelProperty("是否已经关闭本条举报(0=未关闭；1=已关闭)")
    private int isClose;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("逻辑删除，默认0=未删除.有效， 1=已删除.无效")
    private int logicDelete;

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getDiscoverId() {
        return discoverId;
    }

    public void setDiscoverId(Long discoverId) {
        this.discoverId = discoverId;
    }

    public int getDealState() {
        return dealState;
    }

    public void setDealState(int dealState) {
        this.dealState = dealState;
    }

    public int getIsClose() {
        return isClose;
    }

    public void setIsClose(int isClose) {
        this.isClose = isClose;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getLogicDelete() {
        return logicDelete;
    }

    public void setLogicDelete(int logicDelete) {
        this.logicDelete = logicDelete;
    }
}
