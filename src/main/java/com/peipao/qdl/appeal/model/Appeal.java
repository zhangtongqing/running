package com.peipao.qdl.appeal.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * @author meteor.wu
 * @since 2017/6/26
 **/
@ApiModel("Appeal申诉")
public class Appeal {
    @ApiModelProperty("主键ID")
    private Long appealId;

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("学校code")
    private String schoolCode;

    @ApiModelProperty("跑步ID")
    private Long runningId;

    @ApiModelProperty("申诉理由")
    private String reason;

    @ApiModelProperty("申诉状态,1:审核中2:已处理")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createTime;

    public Long getAppealId() {
        return this.appealId;
    }

    public void setAppealId(Long appealId) {
        this.appealId = appealId;
    }

    public Long getRunningId() {
        return this.runningId;
    }

    public void setRunningId(Long runningId) {
        this.runningId = runningId;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long useId) {
        this.userId = useId;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }
}
