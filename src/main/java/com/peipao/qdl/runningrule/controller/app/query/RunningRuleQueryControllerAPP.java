package com.peipao.qdl.runningrule.controller.app.query;

import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.Visitor;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.ReturnConstant;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.morningtrain.model.MorningTrainRecordVo;
import com.peipao.qdl.morningtrain.service.MorningTrainRecordService;
import com.peipao.qdl.running.model.RunningEffectiveEnum;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.running.service.RunningRecordService;
import com.peipao.qdl.runningrule.model.RailNode;
import com.peipao.qdl.runningrule.model.vo.RunningRuleVo;
import com.peipao.qdl.runningrule.service.utils.RuleCacheService;
import com.peipao.qdl.runningrule.service.utils.RuleUtilService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolCacheService;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.statistics.service.RunningStatisticService;
import io.swagger.annotations.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 方法名称：RunningRuleControllerAPP
 * 功能描述：跑步规则接口
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/12 11:58
 * 修订记录：
 */

@Api(value = "runningRule",description = "跑步规则接口")
@RestController
@RequestMapping({"/runningRule/app"})
public class RunningRuleQueryControllerAPP {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private RunningStatisticService runningStatisticService;
    @Autowired
    private RunningRecordService runningRecordService;
    @Autowired
    private SchoolCacheService schoolCacheService;
    @Autowired
    private RuleUtilService ruleUtilService;
    @Autowired
    private RuleCacheService ruleCacheService;
    @Autowired
    private MorningTrainRecordService morningTrainRecordService;

    @Register
    @RequestMapping(value = {"/getMySchoolTarget"},method = {RequestMethod.GET})
//    @SystemControllerLog(description="查询运动首页本学期运动目标完成情况")
    @ApiOperation(value = "查询运动首页本学期运动目标完成情况--APP", notes = "查询运动首页本学期运动目标完成情况")
    public Response<JSONObject> getMySchoolTarget(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "用户Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign
    ) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        //**************************************** 业务数据查询开始 **************************************************/
        //查询学校名称
        String schoolName = schoolCacheService.getSchoolById(userSchool.getSchoolId()).getName();
        //查询本学期目标
//        List<Map<String, Object>> targetList = ruleService.getSchoolTargetBySemesterId(userSchool.getSemesterId());//加载得分规则

        JSONObject resJson = ruleUtilService.getTargetValuesJson(userSchool.getSemesterId());//加载得分规则
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("semesterId", userSchool.getSemesterId());
        paramsMap.put("userId", userId);
        //查询自己本学期目标完成情况
        Map<String, Object> myTargetMap = runningStatisticService.getMyCurrentStatistic(paramsMap);
        if(CollectionUtils.isEmpty(myTargetMap)) {
            myTargetMap = new HashMap<>();
            myTargetMap.put("morningRunningCount", "0");
            myTargetMap.put("freeRunningLength", 0);
        }
        int morningRunningCount = Integer.parseInt(myTargetMap.get("morningRunningCount").toString());
        float freeRunningLength = Float.parseFloat(myTargetMap.get("freeRunningLength").toString());
        //**************************************** 查询结束 返回结果 **************************************************/
        resJson.put("morningRunningCount", morningRunningCount);
        resJson.put("freeRunningLength", freeRunningLength);
        resJson.put("schoolName", schoolName);
        return Response.success(resJson);
    }

    @Register
    @RequestMapping(value = {"/getRunningRule"},method = {RequestMethod.POST})
//    @SystemControllerLog(description="查询跑步规则")
    @ApiOperation(value = "查询跑步规则", notes = "getRunningRule")
    public Response<Map> getRunningRule(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "用户Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json: type 跑步类型[必填] 1,晨跑;4,自由跑;") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"type"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }

        Map<String, Object> map = new HashMap<>();
        //**************************************** 业务数据查询开始 **************************************************/
        List<Map<String,Object>> runningRuleNodeList = new ArrayList<>();

        RunningRuleVo runningRule = ruleUtilService.getRunningRuleBySemesterIdAndType(userSchool.getSemesterId(), json.getInt("type"));
        if(null != runningRule) {
            if(WebConstants.Boolean.TRUE.ordinal() == runningRule.getHasRail()) {//如果围栏开启
                //加载本学期设置的电子围栏数据
                //runningRuleNodeList = ruleCacheService.getRailNodeListBySemesterId(userSchool.getSemesterId());
                runningRuleNodeList = ruleCacheService.getRailNodeListByRailName(userSchool.getSemesterId());
            }
            Date today = new Date();
            Long currTime = today.getTime();//当前用户开始跑步的时间
            //校验开始跑步的前置条件*******************************************************************************
            if(runningRule.getType() == RunningEnum.MORNINGRUNNING.getValue()) {//晨跑
                //判断晨跑是否在规定时间内
                String datStr = DateUtil.dateToStr(today, "YYYY-MM-dd");
                Long startTime = DateUtil.parseDate(datStr + " " + runningRule.getStartTime() + ":00", "yyyy-MM-dd HH:mm:ss").getTime();
                Long endTime = DateUtil.parseDate(datStr + " " + runningRule.getEndTime() + ":00", "yyyy-MM-dd HH:mm:ss").getTime();
                String tipString = "晨跑只能在" + runningRule.getStartTime() + "~" + runningRule.getEndTime() + "开始";
                if(currTime < startTime || currTime > endTime) {//必须在晨跑规定的时间范围内开始晨跑(晨跑结束时间不做要求)
                    map.put("runFlag", WebConstants.Boolean.FALSE.ordinal());//是否可以跑步标识，0=不能
                    map.put("reson", tipString);//
                }
                if(null == map.get("runFlag")) {
                    //晨跑按照开始时间 查询跑步记录，与结束时间无关
                    String startDateMin = DateUtil.dateToStr(new Date(), "YYYY-MM-dd").concat(" 00:00:00");
                    String startDateMax = DateUtil.dateToStr(new Date(), "YYYY-MM-dd").concat(" 23:59:59");
                    Map<String, Object> paramsMap = new HashMap<String, Object>();
                    paramsMap.put("userId", userId);
                    paramsMap.put("type", runningRule.getType());
                    paramsMap.put("isEffective", RunningEffectiveEnum.Success.getCode());
                    paramsMap.put("startDateMin", startDateMin);
                    paramsMap.put("startDateMax", startDateMax);
                    int i = runningRecordService.getRecordCountToday(paramsMap);
                    if(i >= runningRule.getSportCountMax()) {
                        map.put("runFlag", WebConstants.Boolean.FALSE.ordinal());//是否可以跑步标识，0=不能
                        map.put("reson", ReturnConstant.MORNINGRUNNING_COUNT_ERROR.desc);//今日该类型运动已达次数上限，不能再完成
                    }
                }
            }
        } else {
            map.put("runFlag", WebConstants.Boolean.FALSE.ordinal());//是否可以跑步标识，0=不能
            map.put("reson", "没有获得该类型运动规则");
        }
        map.put("runningRule", runningRule);
        map.put("runningRuleNodeList", runningRuleNodeList);
        if(null == map.get("runFlag")) {
            map.put("runFlag", WebConstants.Boolean.TRUE.ordinal());
            map.put("reson", "");
        }
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/getMorningTrainRule"},method = {RequestMethod.POST})
//    @SystemControllerLog(description="查询晨练规则")
    @ApiOperation(value = "查询晨练规则", notes = "getMorningTrainRule")
    public Response<Map> getMorningTrainRule(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "用户Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign
    ) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        Map<String, Object> map = new HashMap<>();
        MorningTrainRecordVo morningTrainRecord = null;
                //**************************************** 业务数据查询开始 **************************************************/
        List<RailNode> runningRuleNodeList = new ArrayList<>();
        RunningRuleVo runningRule = ruleUtilService.getRunningRuleBySemesterIdAndType(userSchool.getSemesterId(), RunningEnum.MORNINGTRAINING.getValue());

        if(null != runningRule && runningRule.getIsUse() == WebConstants.Boolean.TRUE.ordinal()) {
            if (WebConstants.Boolean.TRUE.ordinal() == runningRule.getHasRail()) {//如果围栏开启
                //加载本学期设置的晨练打卡点数据
                runningRuleNodeList = ruleCacheService.getMorningSignNodeListBySemesterId(userSchool.getSemesterId());
            }
            Date today = new Date();
            Long currTime = today.getTime();//当前用户开始运动的时间
            /************************************* 校验开始运动的前置条件 ********************************************/
            //判断今日是否已经完成晨练
            Map<String, Object> paramsMap = new HashMap<>();
            String datStr = DateUtil.dateToStr(today, "YYYY-MM-dd");
            Date startDate = DateUtil.parseDate(datStr + " " + runningRule.getStartTime() + ":00", "yyyy-MM-dd HH:mm:ss");
            Date endDate = DateUtil.parseDate(datStr + " " + runningRule.getEndTime() + ":00", "yyyy-MM-dd HH:mm:ss");
            String tipString = "晨练只能在" + runningRule.getStartTime() + "~" + runningRule.getEndTime() + "开始";
            paramsMap.put("semesterId", userSchool.getSemesterId());
            paramsMap.put("userId", userId);
            paramsMap.put("startDate", startDate);
            paramsMap.put("endDate", endDate);
            morningTrainRecord = morningTrainRecordService.getMorningTrainRecordToday(paramsMap);
            if(null != morningTrainRecord) {
                if(morningTrainRecord.getIsEffective() == RunningEffectiveEnum.Success.getCode()) {
                    map.put("runFlag", WebConstants.Boolean.FALSE.ordinal());//是否可以晨练标识，0=不能
                    map.put("reson", "今日已晨练");//今日已晨练
                } else {
                    morningTrainRecord.setCurrTimeLong(new Date().getTime());
                }
            }
            if(null == map.get("runFlag")) {
                //判断晨跑是否在规定时间内
                if (currTime < startDate.getTime() || currTime > endDate.getTime()) {//必须在晨跑规定的时间范围内开始晨跑(晨跑结束时间不做要求)
                    map.put("runFlag", WebConstants.Boolean.FALSE.ordinal());//是否可以晨练标识，0=不能
                    map.put("reson", tipString);//
                }
            }
        } else {
            runningRule = null;
            map.put("runFlag", WebConstants.Boolean.FALSE.ordinal());//是否可以跑步标识，0=不能
            map.put("reson", "没有获得该类型运动规则");
        }
        map.put("runningRule", runningRule);
        map.put("runningRuleNodeList", runningRuleNodeList);
        map.put("morningTrainRecord", morningTrainRecord);//今日晨练记录
        if(null == map.get("runFlag")) {
            map.put("runFlag", WebConstants.Boolean.TRUE.ordinal());
            map.put("morningTrainRecord", morningTrainRecord);
        }
        return Response.success(map);
    }


    @Register
    @RequestMapping(value = {"/getRailNodeList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description="查询跑步围栏信息")
    @ApiOperation(value = "查询跑步围栏信息", notes = "getRailNodeList")
    public Response<Map> getRailNodeList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "用户Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json: type 跑步类型[必填] 1,晨跑;4,自由跑;") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"type"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        Map<String, Object> map = new HashMap<>();
        //**************************************** 业务数据查询开始 **************************************************/
        List<Map<String,Object>> runningRuleNodeList = new ArrayList<>();
        RunningRuleVo runningRule = ruleUtilService.getRunningRuleBySemesterIdAndType(userSchool.getSemesterId(), json.getInt("type"));
        if(null != runningRule) {
            if (WebConstants.Boolean.TRUE.ordinal() == runningRule.getHasRail()) {//如果围栏开启
                //加载本学期设置的电子围栏数据
                runningRuleNodeList = ruleCacheService.getRailNodeListByRailName(userSchool.getSemesterId());
            }
        }
        map.put("runningRuleNodeList", runningRuleNodeList);
        return Response.success(map);
    }



    @Visitor
    @RequestMapping(value = {"/getRuleTip"},method = {RequestMethod.GET})
//    @SystemControllerLog(description="查询成绩规则H5-app")
    @ApiOperation(value = "查询成绩规则H5--app--49")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
            @ApiResponse(code = 10004,message = "参数错误"),
            @ApiResponse(code = 9001,message = "其他异常")})
    public Response<JSONArray> getRuleTip(
//            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "用户Id") @RequestParam Long userId
//            @ApiParam(required = true, value = "签名") @RequestParam String sign
    ) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        //**************************************** 业务数据查询开始 **************************************************/
        List<RunningRuleVo> runningRuleList = ruleUtilService.getRunningRuleListBySemesterId(userSchool.getSemesterId());
        //**************************************** 查询结束 返回结果 **************************************************/
        JSONArray resJsonArray = new JSONArray();

        for (RunningRuleVo runningRule : runningRuleList) {
            if(runningRule.getIsUse() == WebConstants.Boolean.TRUE.ordinal()) {
                JSONObject runningRuleJson = new JSONObject();
                JSONObject json = new JSONObject();
                json.accumulate("startTime", null == runningRule.getStartTime() ?  "" : runningRule.getStartTime());
                json.accumulate("endTime", null == runningRule.getEndTime() ?  "" : runningRule.getEndTime());
                json.accumulate("sportCountMax", runningRule.getSportCountMax());//每天运动次数上限

                json.accumulate("paceMin",null==runningRule.getSpeedMin()?"":runningRule.getSpeedMin());//最小配速
                json.accumulate("paceMax",null==runningRule.getSpeedMax()?"":runningRule.getSpeedMax());//最大配速

                json.accumulate("validKiometerMin",runningRule.getValidKiometerMin());//每次跑步计入有效里程范围范围（小）
                json.accumulate("validKiometerMax",runningRule.getValidKiometerMax());//每次跑步计入有效里程范围范围（大）

                json.accumulate("hasRail",runningRule.getHasRail());//是否有围栏 1=有

                json.accumulate("target",runningRule.getTarget());//学期目标

                if(runningRule.getType() == RunningEnum.FREERUNNING.getValue()) {//自由跑
                    runningRuleJson.accumulate("name", "freeRunning");
                    runningRuleJson.accumulate("list", json);//自由跑得分规则
                } else if(runningRule.getType() == RunningEnum.MORNINGRUNNING.getValue()) {//晨跑
                    runningRuleJson.accumulate("name", "morningRunning");
                    runningRuleJson.accumulate("list", json);//晨跑得分规则
                } else if(runningRule.getType() == RunningEnum.MORNINGTRAINING.getValue()) {//晨练
                    runningRuleJson.accumulate("name", "morningTraining");
                    runningRuleJson.accumulate("list", json);//晨练得分规则
                }
                resJsonArray.add(runningRuleJson);
            }
        }
        return Response.success(resJsonArray);
    }
}
