package com.peipao.qdl.school.model.enums;

/**
 * @author meteor.wu
 * @since 2017/7/24
 **/
public enum SemesterType {
    FIRST_SEMESTER(1, "上学期"),SECOND_SEMESTER(2, "下学期");

    private int value;
    private String typeName;

    SemesterType(int value, String typeName){
        this.value = value;
        this.typeName = typeName;
    }

    public int getValue() {
        return this.value;
    }

    public String getTypeName() {
        return typeName;
    }
}
