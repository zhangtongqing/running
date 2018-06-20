package com.peipao.qdl.appeal.model;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
public enum AppealStatusEnum {
    INVALID(-2,"判定为无效"),
    AUDIT(-1,"待审核"),
    NORMAL(0,"普通状态"),
    EFFECTIVE(2,"判定为有效");

    private int value;
    private String chinese;
    AppealStatusEnum(int value, String chinese){
        this.value = value;
        this.chinese = chinese;
    }

    public int getValue(){
        return value;
    }

    public String getChinese() {
        return chinese;
    }
}
