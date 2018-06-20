package com.peipao.qdl.running.controller.pc.edit;

import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.appeal.model.AppealStatusEnum;
import com.peipao.qdl.running.model.RunningEffectiveEnum;
import com.peipao.qdl.running.model.RunningRecord;
import com.peipao.qdl.running.service.RunningRecordService;
import com.peipao.qdl.running.service.RunningService;
import com.peipao.qdl.running.service.utils.RunningUtilService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 方法名称：RunningEditControllerPC
 * 功能描述：跑步记录管理
 * 作者：Liu Fan
 * 版本：2.0.11
 * 创建日期：2018/1/26 14:19
 * 修订记录：
 */
@Api(value = "/running/pc",description = "跑步记录管理")
@RestController
@RequestMapping({"/running/pc"})
public class RunningEditControllerPC {
    @Autowired
    private RunningRecordService runningRecordService;
    @Autowired
    private RunningService runningService;
    @Autowired
    private RunningUtilService runningUtilService;


    @Register
    @RequestMapping(value = {"/appealInvalid"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "将学生申诉的跑步记录设为无效")
    @ApiOperation(value = "将学生申诉的跑步记录设为无效")
    public Response setRunningInvalid(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json:runningRecordId 跑步记录Id") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"runningRecordId"};//必填项检查
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        RunningRecord runningRecord = runningService.getRunningRecordById(json.getString("runningRecordId"));
        if(null == runningRecord) {
            return Response.fail(ReturnStatus.RUNNING_RECORD_NOT_EXIST);//跑步记录不存在
        }
        if(runningRecord.getIsEffective() == RunningEffectiveEnum.Success.getCode() || runningRecord.getStatus() != AppealStatusEnum.AUDIT.getValue()) {
            //如果记录本来就是有效的，不能强制改为无效,后续如果有轻质设为无效，看需求定
            return Response.fail(ReturnStatus.RUNNING_RECORD_INCOMPLETE);//跑步记录信息不正确
        }
        //Date now = new Date();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("runningRecordId", runningRecord.getRunningRecordId());
        paramsMap.put("isEffective", runningRecord.getIsEffective());
        paramsMap.put("status", AppealStatusEnum.INVALID.getValue());
        paramsMap.put("appealUserId", userId);//进行审核操作的老师ID
        //paramsMap.put("appealTime", now);//审核时间

        runningRecordService.updateEffectiveStatusOnly(paramsMap);
        runningRecord.setStatus(AppealStatusEnum.INVALID.getValue());
        //runningRecord.setAppealTime(now);
        runningRecord.setAppealUserId(userId);
        return Response.success(runningUtilService.createDataForList(runningRecord));
    }

}
