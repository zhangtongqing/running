package com.peipao.qdl.user.model;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
public enum UserStateEnum {
    NORMAL((byte)1),
    DELETE((byte)0);
    private Byte value;

    UserStateEnum(Byte value) {
        this.value = value;
    }

    public Byte getValue() {
        return this.value;
    }
}
