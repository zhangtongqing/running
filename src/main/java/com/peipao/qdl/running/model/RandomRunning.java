package com.peipao.qdl.running.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author meteor.wu
 * @since 2017/6/27
 **/
@ApiModel(value = "Running随机跑", description = "随机跑")
public class RandomRunning extends Running {
    @ApiModelProperty(value = "定点跑需要经过的点")
    private List<String> address;

    @ApiModelProperty(value = "定点跑需要经过的点, 经度")
    private List<Double> longitudeList;

    @ApiModelProperty(value = "定点跑需要经过的点, 纬度")
    private List<Double> latitudeList;

    @ApiModelProperty(value = "定点跑最少经过的点")
    private Byte lestestPoint;

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public List<Double> getLongitudeList() {
        return longitudeList;
    }

    public void setLongitudeList(List<Double> longitudeList) {
        this.longitudeList = longitudeList;
    }

    public List<Double> getLatitudeList() {
        return latitudeList;
    }

    public void setLatitudeList(List<Double> latitudeList) {
        this.latitudeList = latitudeList;
    }

    public Byte getLestestPoint() {
        return lestestPoint;
    }

    public void setLestestPoint(Byte lestestPoint) {
        this.lestestPoint = lestestPoint;
    }
}
