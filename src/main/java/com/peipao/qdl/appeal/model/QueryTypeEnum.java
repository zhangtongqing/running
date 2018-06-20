package com.peipao.qdl.appeal.model;

/**
 * 方法名称：QueryTypeEnum
 * 功能描述：QueryTypeEnum
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/11/29 11:05
 * 修订记录：
 */
public enum QueryTypeEnum {
    BY_USERNAME(1, "username"), //按姓名
    BY_STUDENT_NO(2, "student_no"),//按学号
    BY_MOBILE(3, "mobile");//按手机号
    private int code;
    private String name;
    QueryTypeEnum(int code, String name) {
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
