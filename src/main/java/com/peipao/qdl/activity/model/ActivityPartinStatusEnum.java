package com.peipao.qdl.activity.model;

/**
 * 用户参与活动状态
 * @author meteor.wu
 * @since 2017/7/15
 **/
public enum ActivityPartinStatusEnum {
    UNENROLL(1), ENROLLED(2);
    private int value;

    ActivityPartinStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static ActivityPartinStatusEnum valueOf(int value){
        switch (value){
            case 1:return UNENROLL;
            case 2:return ENROLLED;
            default:return UNENROLL;
        }
    }
}
