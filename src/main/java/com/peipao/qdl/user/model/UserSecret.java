package com.peipao.qdl.user.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 方法名称：UserSecret
 * 功能描述：用户隐私保护开关表
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@ApiModel("用户隐私保护开关")
public class UserSecret {
    @ApiModelProperty("主键ID")
    private Long userSecretId;
    @ApiModelProperty("外键-用户ID")
    private Long userId;
    @ApiModelProperty("隐私保护开关(默认0=关闭；1=开启隐私保护)")
    private int secretControl = 0;
    @ApiModelProperty("创建时间")
    private Date createTime;

    public Long getUserSecretId() {
        return userSecretId;
    }

    public void setUserSecretId(Long userSecretId) {
        this.userSecretId = userSecretId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getSecretControl() {
        return secretControl;
    }

    public void setSecretControl(int secretControl) {
        this.secretControl = secretControl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
