package com.peipao.qdl.running.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 方法名称：FilePath
 * 功能描述：FilePath
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/11/7 14:27
 * 修订记录：
 */
@ApiModel(value = "Running跑步记录", description = "跑步记录")
public class FilePath {
    @ApiModelProperty(value = "主键ID")
    private Long pathId;

    @ApiModelProperty(value = "跑步记录ID")
    private String runningRecordId;

    @ApiModelProperty(value = "文件路径")
    private String filePath;

    public FilePath(String runningRecordId, String filePath) {
        this.runningRecordId = runningRecordId;
        this.filePath = filePath;
    }

    public FilePath() {}

    public Long getPathId() {
        return pathId;
    }

    public void setPathId(Long pathId) {
        this.pathId = pathId;
    }
}
