package com.peipao.qdl.user.model;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
public enum UserTypeEnum {
    STUDENT(1),
    TEACHER(2),
    SCHOOLMANAGER(3),
    OFFICIALMANAGER(4);

    private int value;

    UserTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static UserTypeEnum valueOf(int value){
        switch (value){
            case 1:return STUDENT;
            case 2:return TEACHER;
            default:return null;
        }
    }
}
