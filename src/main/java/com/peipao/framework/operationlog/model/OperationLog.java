package com.peipao.framework.operationlog.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 方法名称：OperationLog
 * 功能描述：操作日志
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/8/11 17:39
 * 修订记录：
 */
@Entity
public class OperationLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @TableGenerator(name = "EXCEPT_ID_GENERATOR",
            table="t_id_generator",
            pkColumnName = "PK_NAME",
            pkColumnValue = "pk_log_id",
            valueColumnName = "PK_VALUE",
            initialValue = 1000,
            allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "EXCEPT_ID_GENERATOR")
    @Column(length = 32)
    private Long logId;
    private String description;
    @Column(length = 200)
    private String method;
    private String ip;
    private String userId;
    private String username;
    private String mobile;
    private String account;
    private Date createDate;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
