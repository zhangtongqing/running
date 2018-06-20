package com.peipao.qdl.rule.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.qdl.running.model.Running;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.running.model.RunningLine;
import com.peipao.qdl.running.service.RunningCacheService;
import com.peipao.qdl.running.service.RunningLineCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 方法名称：
 * 功能描述：
 * 作者：Liu Fan
 * 版本：
 * 创建日期：2017/7/10 19:24
 * 修订记录：
 */

@Api(value = "/rule/app",description = "跑步规则")
@RestController
@RequestMapping({"/rule/app"})
public class RunningRuleControllerAPP {
    @Autowired
    private RunningCacheService runningCacheService;
    @Autowired
    private RunningLineCacheService runningLineCacheService;



    @Register
    @RequestMapping(value = {"/getActivityRunningRule"},method = {RequestMethod.POST})
    @ApiOperation(value = "获取跑步类活动有效性规则(活动--跑步类活动有效性规则)--app--新增接口")
    public Response<?> getActivityRunningRule(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true,value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json格式参数-- activityId [Long] --活动Id") @RequestBody JSONObject json
    ) throws Exception {
        if(null == json || null == json.get("activityId")) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);
        }
        Long activityId = json.getLong("activityId");
        Running running = runningCacheService.getByActivityId(activityId);
        if(null == running) {
            return Response.fail(ResultMsg.QUERY_ACTIVITY_RULE_EMTPY);//当前跑步活动没有找到运动规则,请检查活动信息
        }
        Integer type = running.getType();
        Integer nodeCount = running.getNodeCount();
        if(type == RunningEnum.ORIENTRUNNING.getValue()) {//定向跑
            nodeCount = 1;//如果是定向跑，路线点最起码有1个，终点
        }

        if(type == RunningEnum.RANDOMRUNNING.getValue() || type == RunningEnum.ORIENTRUNNING.getValue()) {//随机跑必须设置 经过随机点的数量 和 随机点集合(定向跑类似，必须有1个或多个点)
            if(nodeCount > 0) {//如果设置了随机点数量，则随机点集合不能为空(定向跑类似，必须有1个或多个点)
                List<RunningLine> runningLineList = runningLineCacheService.getRunningLineByRunningId(running.getRunningId());
                if(null == runningLineList || runningLineList.size() == 0) {
                    return Response.fail(ReturnStatus.RUNNING_LINE_EMPTY);
                }
                running.setRunningLineList(runningLineList);
            } else {
                return Response.fail(ReturnStatus.RANDOM_NODE_COUNAT_EMPTY);
            }
        }
        return Response.success(running);
    }

}
