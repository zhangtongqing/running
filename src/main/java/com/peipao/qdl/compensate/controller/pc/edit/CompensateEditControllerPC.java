package com.peipao.qdl.compensate.controller.pc.edit;

import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.JsonUtils;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.activity.dao.ActivityMemberDao;
import com.peipao.qdl.activity.model.Activity;
import com.peipao.qdl.activity.model.ActivityMember;
import com.peipao.qdl.activity.service.ActivityCacheService;
import com.peipao.qdl.appeal.model.AppealStatusEnum;
import com.peipao.qdl.compensate.model.Compensate;
import com.peipao.qdl.compensate.service.CompensateService;
import com.peipao.qdl.running.model.RunningEffectiveEnum;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.running.model.RunningRecord;
import com.peipao.qdl.running.service.RunningRecordService;
import com.peipao.qdl.running.service.RunningService;
import com.peipao.qdl.running.service.utils.RunningUtilService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.statistics.service.UserStatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：CompensateEditControllerPC
 * 功能描述：运动补偿管理
 * 作者：Liu Fan
 * 版本：2.0.11
 * 创建日期：2018/1/26 13:58
 * 修订记录：
 */
@Api(value = "/compensate/pc",description = "运动补偿管理")
@RestController
@RequestMapping({"/compensate/pc"})
public class CompensateEditControllerPC {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private RunningService runningService;
    @Autowired
    private RunningRecordService runningRecordService;
    @Autowired
    private CompensateService compensateService;
    @Autowired
    private UserStatisticService userStatisticService;
    @Autowired
    private RunningUtilService runningUtilService;
    @Autowired
    private ActivityMemberDao activityMemberDao;
    @Autowired
    EventBus eventBus;
    @Autowired
    private ActivityCacheService activityCacheService;

    @Register
    @RequestMapping(value = {"/appealCompensate"},method = {RequestMethod.POST})
    @SystemControllerLog(description="申诉处理-设为有效并补偿成绩")
    @ApiOperation(value = "申诉处理-设为有效并补偿成绩", notes = "appealCompensate")
    public Response<Map> appealCompensate(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "用户Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json: runningRecordId[String必填]，runningLength[float非必填(自由跑必填)]") @RequestBody JSONObject json
    ) throws Exception {
        //如果是晨跑，补偿跑步次数；如果不是晨跑，补偿跑步里程
        String[] params = new String[]{"runningRecordId"};//必填项检查
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        Long semesterId = userSchool.getSemesterId();//学期ID

        String runningRecordId = json.getString("runningRecordId");
        RunningRecord runningRecord = runningService.getRunningRecordById(runningRecordId);
        if(null == runningRecord) {
            return Response.fail(ReturnStatus.RUNNING_RECORD_NOT_EXIST);//跑步记录不存在
        }
        if(runningRecord.getIsEffective() == RunningEffectiveEnum.Success.getCode() || runningRecord.getStatus() != AppealStatusEnum.AUDIT.getValue()) {
            //如果记录本来就是有效的，不能强制改为无效,后续如果有设为无效，看需求定
            return Response.fail(ReturnStatus.RUNNING_RECORD_INCOMPLETE);//跑步记录信息不正确
        }

        int runningType = runningRecord.getType();
        if(runningRecord.getActivityId() > 0L) {
            runningType = RunningEnum.ACTIVITYRUN.getValue();//活动跑=100
        }

        boolean updateLengthFlag = false;//是否修改计分里程,默认: 不修改 = false
        int runningCount = 1;//默认增加一次运动次数
        float runningLength = 0f;
        float runningScore = 0f;//只有校方活动有分数奖励概念

        if(RunningEnum.FREERUNNING.getValue() == runningType) {//--自由跑
            /*----------------------------- 自由跑 ------------------------------------------------------------------*/
            //自由跑:设为有效时，增加有效跑步次数一次， 增加自由跑计分里程（以教师“核定本次自由跑计分里程”为准）
            //只有自由跑，需要计分里程参数
            if(ValidateUtil.jsonValidateWithKey(json, "runningLength")) {
                runningLength = Float.parseFloat(json.getString("runningLength"));
                if(runningLength > WebConstants.runningLength || runningLength < 0) {
                    return Response.fail(ReturnStatus.COMPENSATE_PARAMS_TOO_LONG);//补偿成绩参数超过限制
                }
                updateLengthFlag = true;//状态改为申诉成功,更新计分里程等其他数据
            } else {
                return Response.fail(ResultMsg.RUNNING_LENGTH_IS_MISSING);//请填写记分里程
            }
        } else if(RunningEnum.MORNINGRUNNING.getValue() == runningType) {//--晨跑
            /*----------------------------- 晨跑 --------------------------------------------------------------------*/
            runningLength = runningRecord.getKilometeorCount();
            updateLengthFlag = true;//状态改为申诉成功,更新计分里程等其他数据
        } else if(runningType == RunningEnum.ACTIVITYRUN.getValue()) {//--活动
            /*----------------------------- 活动 --------------------------------------------------------------------*/
            Activity activity = activityCacheService.getActivityById(runningRecord.getActivityId());
            if(null == activity) {
                throw new BusinessException(ReturnStatus.ACTIVITY_NOT_EXIST);//活动不存在
            }
            int myRunningCount = 0;
            int sportCountMax = activity.getEffectiveSignCount();//活动最多可以参与的次数
            ActivityMember activityMember = activityMemberDao.queryActivityMemberById(activity.getActivityId(), runningRecord.getUserId());
            if(null != activityMember && null != activityMember.getSucCount()) {
                myRunningCount = activityMember.getSucCount();
            }
            if(sportCountMax > 0 && myRunningCount >= sportCountMax) {//如果已经达到活动可参与次数上限
                //updateLengthFlag ===== false //状态改为申诉成功,不更新其他数据
                updateLengthFlag = false;
            } else {
                runningScore = activity.getRewardScore();
                runningLength = runningRecord.getKilometeorCount();
                updateLengthFlag = true;//状态改为申诉成功,更新计分里程等其他数据
            }
        }

        //Date now = new Date();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("runningRecordId", runningRecord.getRunningRecordId());
        paramsMap.put("isEffective", RunningEffectiveEnum.Success.getCode());
        paramsMap.put("status", AppealStatusEnum.EFFECTIVE.getValue());
        paramsMap.put("appealUserId", userId);//进行审核操作的老师ID
        //paramsMap.put("appealTime", now);//审核时间

        if(updateLengthFlag) {//增加补偿数据，更新计分里程
            /*----------------------------------------- Compensate 补偿信息 -----------------------------------------*/
            Compensate compensate = new Compensate();
            compensate.setRunningType(runningType);
            compensate.setRunningRecordId(runningRecord.getRunningRecordId());
            compensate.setSemesterId(semesterId);
            compensate.setUserId(runningRecord.getUserId());//跑步学生ID
            compensate.setUpdateUserId(userId);//操作老师ID
            compensate.setUpdateTime(new Date());//老师操作修改成绩的时间

            compensate.setRunningLength(runningLength);
            compensate.setRunningCount(runningCount);
            compensate.setCompensateScore(runningScore);

            compensate.setRemark("申诉-" + runningRecord.getRunningRecordId());
            //根据跑步记录申诉补偿成绩
            runningRecordService.updateEffectiveStatusOnlyForCompensate(paramsMap, compensate);

            //json内已经包含的参数：runningRecordId [String必填]，runningLength [需要覆盖该参数]
            json.put("runningType", runningType);//跑步类型
            json.put("userId", runningRecord.getUserId());//学生ID
            json.put("runningLength", runningLength);//补偿里程
            json.put("runningScore", runningScore);//补偿分数
            json.put("schoolId", userSchool.getSchoolId());//学校ID
            json.put("semesterId", userSchool.getSemesterId());//学期ID
            //发布一个更新用户统计表信息事件
            eventBus.notify("updateUserStatisticEvent", Event.wrap(json));
             /*-------------------------------------------------------------------------------------------------------*/
            if(runningType == RunningEnum.ACTIVITYRUN.getValue()) {//--活动
                //发布一个增加活动成功参与次数的事件
                JSONObject paramsJson = new JSONObject();
                paramsJson.accumulate("activityId", runningRecord.getActivityId());
                paramsJson.accumulate("userId", runningRecord.getUserId());
                paramsJson.accumulate("duration", runningRecord.getDuration().intValue());
                paramsJson.accumulate("calorie", runningRecord.getCalorieCount().intValue());
                paramsJson.accumulate("length", runningRecord.getKilometeorCount());
                paramsJson.accumulate("isEffective", RunningEffectiveEnum.Success.getCode());
                paramsJson.accumulate("runningRecordId", runningRecord.getRunningRecordId());
                eventBus.notify("updateUserActivityEvent", Event.wrap(paramsJson));
            }
            /*-------------------------------------------------------------------------------------------------------*/
        } else {
            runningRecordService.updateEffectiveStatusOnly(paramsMap);
        }
        runningRecord.setStatus(AppealStatusEnum.EFFECTIVE.getValue());
        runningRecord.setIsEffective((byte)RunningEffectiveEnum.Success.getCode());
        //runningRecord.setAppealTime(now);
        return Response.success(runningUtilService.createDataForList(runningRecord));
    }


    @Register
    @RequestMapping(value = {"/updateCompensateByUser"}, method = {RequestMethod.POST})
    @SystemControllerLog(description = "成绩管理-成绩补偿修改")
    @ApiOperation(value = "成绩管理-成绩补偿修改")
    public Response<?> updateCompensateByUser(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "老师userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "RequestBody:studentId[必填];runningType[必填];morningRunningCount;runningLength;compensateScore;remark") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"studentId", "runningType"};//必填项检查
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        int runningType = json.getInt("runningType");

        UserSchool userSchool = schoolService.getParaByUserId(json.getLong("studentId"),  new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        json.put("schoolId", userSchool.getSchoolId());//学校ID
        json.put("semesterId", userSchool.getSemesterId());
        json.put("userId", userSchool.getUserId());
        int morningRunningCount = 0;
        float runningLength = 0f;
        float compensateScore = 0f;
        String remark = null;

        if(runningType == RunningEnum.MORNINGRUNNING.getValue()) {//晨跑
            params = new String[]{"morningRunningCount"};//必填项检查
            if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
                return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
            }
            morningRunningCount = json.getInt("morningRunningCount");
        } else if(runningType == RunningEnum.FREERUNNING.getValue()) {//自由跑
            params = new String[]{"runningLength"};//必填项检查
            if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
                return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
            }
            runningLength = Float.parseFloat(json.getString("runningLength"));
        } else if(runningType == RunningEnum.ACTIVITYRUN.getValue()) {//活动跑
            params = new String[]{"compensateScore"};//必填项检查
            if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
                return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
            }
            compensateScore = Float.parseFloat(json.getString("compensateScore"));
        } else {
            return Response.fail(ResultMsg.RUNNING_TYPE_ERROR);//跑步类型错误
        }

        if(ValidateUtil.jsonValidateWithKey(json, "remark")) {
            remark = json.getString("remark");
        }

        if(morningRunningCount > WebConstants.morningRunningCount || morningRunningCount < WebConstants.morningRunningCountF
                || runningLength > WebConstants.runningLength || runningLength < WebConstants.runningLengthF
                || compensateScore > WebConstants.compensateScore || compensateScore < WebConstants.compensateScoreF
                ) {
            return Response.fail(ReturnStatus.COMPENSATE_PARAMS_TOO_LONG);//补偿成绩参数超过限制
        }
        /*----------------------------------------- Compensate 补偿信息 ---------------------------------------------*/
        Compensate compensate = new Compensate();
        compensate.setRunningType(runningType);
        compensate.setSemesterId(userSchool.getSemesterId());
        compensate.setUserId(userSchool.getUserId());//跑步学生ID
        compensate.setUpdateUserId(userId);//操作老师ID
        compensate.setUpdateTime(new Date());//老师操作修改成绩的时间
        compensate.setRunningCount(morningRunningCount);
        compensate.setRunningLength(runningLength);
        compensate.setCompensateScore(compensateScore);
        compensate.setRemark(remark);
        Map<String, Object> paramsMap = JsonUtils.getMap4Json(json.toString());

        compensateService.updateStudentScoreOverall(compensate, userSchool);//全面更新学生成绩信息
        /*-----------------------------------------------------------------------------------------------------------*/
        paramsMap = new HashMap<String, Object>();
        paramsMap.put("semesterId", userSchool.getSemesterId());
        paramsMap.put("studentId", userSchool.getUserId());
        paramsMap.put("schoolId", userSchool.getSchoolId());
        List<Map<String, Object>> list = userStatisticService.getStudentScoreInfoList(paramsMap);
        return Response.success(list.get(0));
    }

}
