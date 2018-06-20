package com.peipao.qdl.running.model;

/**
 * 方法名称：RankTypeEnum
 * 功能描述：RankTypeEnum
 * 作者：Liu Fan
 * 版本：2.0.11
 * 创建日期：2018/2/6 16:47
 * 修订记录：
 */
public enum RankTypeEnum {

    RankingInCountry(1, "全国排行"),
    RankingInSchool(2, "全校排行"),
    RankingInCourse(3, "本部排行");

    private int code;
    private String msg;

    RankTypeEnum(int code, String msg){
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
