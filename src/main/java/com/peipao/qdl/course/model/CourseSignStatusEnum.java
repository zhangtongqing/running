package com.peipao.qdl.course.model;

/**
 * 方法名称：CourseSignStatusEnum
 * 功能描述：课程签到状态枚举
 * 作者：Liu Fan
 * 版本：2.1.0
 * 创建日期：2018/3/7 15:32
 * 修订记录：
 */

public enum CourseSignStatusEnum {
    not_begin(10, "无"), missing(20, "缺勤"), ok(88, "已签到");
    public int code;
    public String description;
    CourseSignStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
