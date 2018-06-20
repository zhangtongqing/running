package com.peipao.qdl.running.model;

/**
 * 方法名称：NodeStatusEnum
 * 功能描述：NodeStatusEnum
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/11/7 11:38
 * 修订记录：
 */
public enum NodeStatusEnum {
    SQLWAY(0, "SQL数据保存"),
    UPLOADING(10, "跑步节点文件处理中"),
    SUCCESS(200, "上传成功");

    private int code;
    private String msg;

    NodeStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
