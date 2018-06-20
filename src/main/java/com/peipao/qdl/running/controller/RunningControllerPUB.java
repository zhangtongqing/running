package com.peipao.qdl.running.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.running.model.RunningNode;
import com.peipao.qdl.running.model.RunningRecord;
import com.peipao.qdl.running.service.RunningService;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "/runing/pub",description = "跑步")
@RestController
@RequestMapping({"/runing/pub"})
public class RunningControllerPUB {
    @Autowired
    private RunningService runningService;

    @Register
    @RequestMapping(value = {"/getRunningRecordDetailById"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询跑步轨迹详情-pub")
    @ApiOperation(value = "根据跑步ID查询跑步详情(点击显示轨迹)--pub--27")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
            @ApiResponse(code = 10004,message = "参数错误"),
            @ApiResponse(code = 9001,message = "其他异常")})
    public Response<?> getRunningRecordDetailById(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "runingRecordId:跑步记录Id") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"runingRecordId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);//必填项请求参数不完整，请检查
        }
        String runingRecordId = json.getString("runingRecordId");

        RunningRecord runningRecord = runningService.getRunningRecordById(runingRecordId);
        if(null == runningRecord || null == runningRecord.getCreateTime()) {
            return Response.fail(ReturnStatus.RUNNING_RECORD_INCOMPLETE);//跑步记录信息不正确
        }
        //以sql存储node节点数据
        String tableDay = null;
        if(null != runningRecord.getCreateTime()) {
            tableDay = "_" + DateUtil.dateToStr(runningRecord.getCreateTime(), "YYYYMMdd");
        }
//          List<RunningNode> runningNodeList = runningService.getRunningNodeByRunningRecordId("", runingRecordId);
        List<RunningNode> runningNodeList = runningService.getRunningNodeByRunningRecordId(tableDay, runingRecordId);
        return Response.success(runningNodeList);
    }

}

