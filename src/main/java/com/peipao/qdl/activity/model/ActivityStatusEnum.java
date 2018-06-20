package com.peipao.qdl.activity.model;

/**
 * @author meteor.wu
 * @since 2017/7/5
 **/
public enum ActivityStatusEnum {
    DRAFT(1), UNENROLL(2), ENROLLING(3), ENROLLED(4), INPROCESS(5), OVER(6);
    private int value;

    ActivityStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static ActivityStatusEnum valueOf(int value){
        switch (value){
            case 1:return DRAFT;
            case 2:return UNENROLL;
            case 3:return ENROLLING;
            case 4:return ENROLLED;
            case 5:return INPROCESS;
            case 6:return OVER;
            default:return null;
        }
    }
}
