package com.peipao.qdl.running.model;

/**
 * 方法名称：RunningEffectiveEnum
 * 功能描述：跑步无效原因枚举
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/18 18:12
 * 修订记录：
 */
public enum RunningEffectiveEnum {
    NotInSchoolRail((byte) 0, "不在围栏内"),
    TooSlow((byte)1, "过慢"),
    TooFast((byte)2, "过快"),
    UnderMinDistance((byte)3, "低于最小有效里程"),
    NotInStipulateTime((byte)4, "不在规定时间内"),
    SubterNodeNumber((byte)5, "经过定点少于规定数量"),
    NotInProperOrder((byte)6, "定向跑未按照定点顺序"),

    Success((byte)88, "跑步记录有效");

    private byte code;
    private String msg;
    RunningEffectiveEnum(byte code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
