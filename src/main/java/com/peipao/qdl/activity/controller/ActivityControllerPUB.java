package com.peipao.qdl.activity.controller;


import com.peipao.framework.annotation.Visitor;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.qdl.activity.model.Activity;
import com.peipao.qdl.activity.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meteor.wu
 * @since 2017/6/26
 **/
@Api(value = "/activity/pub", description = "课程")
@RestController
@RequestMapping("/activity/pub")
public class ActivityControllerPUB {
    @Autowired
    private ActivityService activityService;

    @Visitor
    @RequestMapping(value = {"/getActivityDetail"},method = {RequestMethod.POST})
    @ApiOperation("活动分享")
    public Response<?> getActivityDetailForShare(
            @ApiParam(required = true,value = "json:activityId") @RequestBody Activity activity) throws Exception {
        if (activity.getActivityId() == null){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        return Response.success(activityService.getActivityDetailForShare(activity.getActivityId()));
    }


}