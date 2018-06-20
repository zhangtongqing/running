package com.peipao.qdl.rule.model.enums;

/**
 * 方法名称：GetScoreType
 * 功能描述：得分方式
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/9/19 15:23
 * 修订记录：
 */
public enum GetScoreType {
    ByKiometer(1, "按里程计分"),
    ByCount(2, "按次计分");
    private int code;
    private String msg;
    GetScoreType(int code, String msg){
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
