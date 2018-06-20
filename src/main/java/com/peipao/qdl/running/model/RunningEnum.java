package com.peipao.qdl.running.model;

/**
 * @author meteor.wu
 * @since 2017/6/28
 **/
public enum RunningEnum {
    MORNINGRUNNING(1, "晨跑"),
    FUNRUNNING(2, "趣味跑"),
    RANDOMRUNNING(3, "随机跑"),
    FREERUNNING(4, "自由跑"),
    ORIENTRUNNING(5, "定向跑"),
    ORIGINALRUNNING(6, "创意跑"),
    MORNINGTRAINING(10, "晨练"),
    ACTIVITYRUN(100, "活动跑");

    private int value;
    private String chinesename;
    RunningEnum(int value, String chinesename){
        this.value = value;
        this.chinesename = chinesename;
    }

    public int getValue(){
        return value;
    }

    public String getChinesename(){
        return chinesename;
    }

    public static RunningEnum valueOf(int value){
        switch (value) {
            case 1:return MORNINGRUNNING;
            case 2:return FUNRUNNING;
            case 3:return RANDOMRUNNING;
            case 4:return FREERUNNING;
            case 5:return ORIENTRUNNING;
            case 6:return ORIGINALRUNNING;
            case 10:return MORNINGTRAINING;
            case 100:return ACTIVITYRUN;
            default:return null;
        }
    }
}
