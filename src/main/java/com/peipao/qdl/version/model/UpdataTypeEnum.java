package com.peipao.qdl.version.model;

/**
 * 方法名称：UpdataTypeEnum
 * 功能描述：更新类型枚举（0=所有版本无条件提示更新，1=所有版本无条件强制更新，2=所有版本有条件提示更新，3=所有版本有条件强制更新）
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/31 11:06
 * 修订记录：
 */
public enum UpdataTypeEnum {
    NOT_UPDATE_ALL(-1, "所有版本不再校验更新"),
    TIP_UPDATE_ALL(0, "所有版本无条件提示更新"),
    UPDATE_ALL(1, "所有版本无条件强制更新"),
    TIP_UPDATE_SOME(2, "部分版本有条件提示更新"),
    UPDATE_SOME(3, "部分版本有条件强制更新");
    private int type;
    private String desc;

    UpdataTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
