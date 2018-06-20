package com.peipao.qdl.activity.model;

/**
 * @author meteor.wu
 * @since 2017/7/5
 **/
public enum ActivityPublishTypeEnum {
    OFFICIAL((byte)1), SCHOOL((byte)2), COURSE((byte)3);

    private byte value;

    ActivityPublishTypeEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return this.value;
    }

    public static ActivityPublishTypeEnum valueOf(byte value){
        switch (value){
            case 1:return OFFICIAL;
            case 2:return SCHOOL;
            case 3:return COURSE;
            default:return null;
        }
    }
}
