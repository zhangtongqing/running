package com.peipao.framework.log.model;

/**
 * 方法名称：SystemLogTypeEnum
 * 功能描述：SystemLogTypeEnum
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/8/3 17:56
 * 修订记录：
 */
public enum SystemLogTypeEnum {
    COMMON(0), ERROR(1);
    private Integer value;
    SystemLogTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
