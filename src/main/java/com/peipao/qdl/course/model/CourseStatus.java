package com.peipao.qdl.course.model;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
public enum CourseStatus {
    BEFORESTART(1,"未开始"),
    ONGONGING(2, "进行中"),
    OVER(3, "结束");

    private int value;
    private String chinese;

    CourseStatus(int value, String chinese) {
        this.value = value;
        this.chinese = chinese;
    }

    public int getValue() {
        return this.value;
    }

    public String getChinese(){
        return this.chinese;
    }
}
