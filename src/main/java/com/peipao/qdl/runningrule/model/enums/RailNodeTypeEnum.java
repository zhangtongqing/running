package com.peipao.qdl.runningrule.model.enums;

/**
 * 方法名称：RailNodeTypeEnum
 * 功能描述：RailNodeTypeEnum
 * 作者：Liu Fan
 * 版本：2.1.0
 * 创建日期：2018/2/28 15:49
 * 修订记录：
 */

public enum RailNodeTypeEnum {
    SchoolRail(1), MorningTraining(2);
    private int value;
    RailNodeTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
