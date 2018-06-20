package com.peipao.qdl.statistics.model;

/**
 * 方法名称：SortTypeEnum
 * 功能描述：SortTypeEnum
 * 作者：Liu Fan
 * 版本：
 * 创建日期：2017/11/29 15:48
 * 修订记录：
 */
public enum SortTypeEnum {
    morningRunningCount(1, "有效晨跑总次数"),
    runningLength(2, "有效跑步总里程"),
    score(3, "综合成绩"),
    studentNo(4, "学号"),
    mobile(5, "手机帐号"),
    classname(6, "专业班级"),
    courseName(7, "所属课程"),
    freeRunningLength(8, "自由跑计分总里程（公里）"),
    schoolActivityScore(9, "校方活动总得分");

    private int code;
    private String name;

    SortTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
