package com.peipao.qdl.running.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author meteor.wu
 * @since 2017/6/27
 **/
@ApiModel(value = "FunRunning趣味跑", description = "趣味跑")
public class FunRunning extends Running {

    @ApiModelProperty(value = "趣味跑所选取的图片index")
    private Byte picIndex;
}
