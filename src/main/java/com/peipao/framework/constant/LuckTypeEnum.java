package com.peipao.framework.constant;

/**
 * 张同情 on 2018/4/18.
 */
public enum LuckTypeEnum
{
    _defaul("0", "自动"), roulette("1", "轮盘");

    private String code;
    private String desc;

    private LuckTypeEnum(String code, String desc)
    {
        this.code = code;
        this.desc = desc;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }
}
