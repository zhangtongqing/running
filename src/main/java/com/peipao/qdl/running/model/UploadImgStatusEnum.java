package com.peipao.qdl.running.model;

/**
 * 方法名称：
 * 功能描述：
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/28 11:43
 * 修订记录：
 */
public enum UploadImgStatusEnum {
    ENABLE(1, "正常"),
    DISABLE(0, "禁用");
    private int code;
    private String msg;

    UploadImgStatusEnum(int code, String msg){
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
