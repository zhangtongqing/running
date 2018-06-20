package com.peipao.qdl.running.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.StringUtil;
import com.peipao.qdl.running.service.RunningLineCacheService;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author meteor.wu
 * @since 2017/6/26
 **/
@Api(value = "/runing/pc",description = "跑步")
@RestController
@RequestMapping({"/runing/pc"})
public class RunningControllerPC {
    @Autowired
    private RunningLineCacheService runningLineCacheService;

    protected static final Logger LOG = LoggerFactory.getLogger(RunningControllerPC.class);

    @Register
    @RequestMapping(value = {"/getRunningLineByRunningId"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询跑步列表(活动管理-活动跑步-获得跑步线路点集合)-pc")
    @ApiOperation(value = "查询跑步列表(活动管理-活动跑步-获得跑步线路点集合)--pc--55")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
            @ApiResponse(code = 10004,message = "参数错误"),
            @ApiResponse(code = 9001,message = "其他异常")})
    public Response getRunningLineByRunningId(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "用户Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json: runningId 跑步Id") @RequestBody JSONObject json
    ) throws Exception {
        if(null == json || null == json.get("runningId") || StringUtil.isEmpty(json.getString("runningId"))) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);
        }
        Long runningId = Long.parseLong(json.getString("runningId"));
        List list = runningLineCacheService.getRunningLineByRunningId(runningId);
        return Response.success(list);
    }
}

