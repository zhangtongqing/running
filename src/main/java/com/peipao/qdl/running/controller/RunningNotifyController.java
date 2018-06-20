package com.peipao.qdl.running.controller;


import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.annotation.Visitor;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.running.model.NodeStatusEnum;
import com.peipao.qdl.running.model.RunningRecord;
import com.peipao.qdl.running.service.RunningRecordService;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 方法名称：跑步节点文件回调通知
 * 功能描述：RunningNotifyController
 * 作者：Liu Fan
 * 版本：
 * 创建日期：2017/11/7 11:45
 * 修订记录：
 */

@Api(value = "/nodefile",description = "跑步节点文件回调通知")
@RestController
@RequestMapping({"/nodefile"})
public class RunningNotifyController {
    @Autowired
    private RunningRecordService runningRecordService;

    @Visitor
    @RequestMapping(value = {"/notify"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "跑步节点文件回调通知")
    @ApiOperation(value = "跑步节点文件回调通知", notes = "跑步节点文件回调通知")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
            @ApiResponse(code = 10004,message = "参数错误"),
            @ApiResponse(code = 9001,message = "其他异常")})
    public String notify(
            @ApiParam(required = true, value = "runningRecordId=跑步记录ID；nodeTime=文件保存时间,格式:YYYY/MM/DD") @RequestBody JSONObject json
    ) throws Exception {
        //TODO 仅接收来自白名单的服务器通知
        String[] params = new String[]{"runningRecordId", "nodeTime"};//必填项检查
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return "FAIL1";//必填项请求参数不完整，请检查
        }
        String runningRecordId = json.getString("runningRecordId");
        String nodeTime = json.getString("nodeTime");
        RunningRecord runningRecord = runningRecordService.getRunningRecordById(runningRecordId);
        if(null == runningRecord) {
            return "FAIL2";//该跑步记录不存在
        }
        if(runningRecord.getNodeStatus() == NodeStatusEnum.SQLWAY.getCode()) {
            return "FAIL3";//跑步记录状态错误，请检查
        }
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("runningRecordId", runningRecordId);
        paramsMap.put("nodeTime", nodeTime);
        paramsMap.put("nodeStatus", NodeStatusEnum.SUCCESS.getCode());
        try {
            runningRecordService.updateNodeTimeAndStatus(paramsMap);
        }catch (Exception e){
            return "FAIL4";//update出错
        }
        return "SUCCESS";
    }
}
