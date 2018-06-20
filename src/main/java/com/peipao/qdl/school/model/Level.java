package com.peipao.qdl.school.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author meteor.wu
 * @since 2017/6/30
 **/
@ApiModel("Level等级")
public class Level {
    @ApiModelProperty("主键ID")
    private Long levelId;

    @ApiModelProperty("学校id")
    private Long schoolId;

    @ApiModelProperty("顺序index")
    private String title;

    @ApiModelProperty("等级名称")
    private String name;

    @ApiModelProperty("运动里程")
    private Integer length;

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
