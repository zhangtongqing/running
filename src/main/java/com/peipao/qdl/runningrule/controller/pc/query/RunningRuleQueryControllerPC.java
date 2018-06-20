package com.peipao.qdl.runningrule.controller.pc.query;

import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.running.model.RunningRecord;
import com.peipao.qdl.running.service.RunningService;
import com.peipao.qdl.runningrule.model.RailNode;
import com.peipao.qdl.runningrule.model.vo.RunningRuleVo;
import com.peipao.qdl.runningrule.service.utils.RuleCacheService;
import com.peipao.qdl.runningrule.service.utils.RuleUtilService;
import com.peipao.qdl.school.model.School;
import com.peipao.qdl.school.model.Semester;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolCacheService;
import com.peipao.qdl.school.service.SchoolService;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：RunningRuleControllerPC
 * 功能描述：跑步规则接口
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/12 11:58
 * 修订记录：
 */

@Api(value = "runningRule",description = "跑步规则接口")
@RestController
@RequestMapping({"/runningRule/pc"})
public class RunningRuleQueryControllerPC {
    protected static final Logger log = LoggerFactory.getLogger(RunningRuleQueryControllerPC.class);
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private SchoolCacheService schoolCacheService;
    @Autowired
    private RuleUtilService ruleUtilService;
    @Autowired
    private RuleCacheService ruleCacheService;
    @Autowired
    private RunningService runningService;

    @Register
    @RequestMapping(value = {"/getRunningRule"},method = {RequestMethod.POST})
//    @SystemControllerLog(description="查询跑步规则")
    @ApiOperation(value = "查询跑步规则--PC", notes = "查询跑步规则")
    public Response<Map> getRunningRule(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "用户Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = false, value = "json: semesterId 学期ID 非必填") @RequestBody(required = false) JSONObject json
    ) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        Long semesterId = null;
        if(ValidateUtil.jsonValidateWithKey(json, "semesterId") && json.getLong("semesterId") > 0L) {
            semesterId = json.getLong("semesterId");
            userSchool.setSemesterId(semesterId);
        }

        Semester semester = schoolCacheService.getSemesterBySchoolId(userSchool.getSchoolId());
        if(null == semester) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }

        log.info("getRunningRule 请求参数={}",json);
        //**************************************** 业务数据查询开始 **************************************************/
        List<RunningRuleVo> runningRuleList = ruleUtilService.getRunningRuleListBySemesterId(userSchool.getSemesterId());

        //加载本学期设置的电子围栏数据
        List<Map<String,Object>> runningRuleNodeList = ruleCacheService.getRailNodeListByRailName(userSchool.getSemesterId());

        //加载本学期设置的晨练打卡点数据
        List<RailNode> signNodeList = ruleCacheService.getMorningSignNodeListBySemesterId(userSchool.getSemesterId());
        //**************************************** 查询结束 返回结果 **************************************************/
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("runningRuleList", runningRuleList);
        resMap.put("runningRuleNodeList", runningRuleNodeList);
        resMap.put("signNodeList", signNodeList);
        resMap.put("semesterYear", semester.getSemesterYear());//学年 ：2017-2018
        resMap.put("semesterType", semester.getSemesterType());//学期类型 ：1上学期  2下学期

        return Response.success(resMap);
    }



    @Register
    @RequestMapping(value = {"/getSchoolRailNodeList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description="查询学校围栏规则")
    @ApiOperation(value = "查询跑步规则--PC", notes = "查询学校围栏规则")
    public Response<Map> getSchoolRailNodeList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "用户Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json: runningRecordId") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"runningRecordId"};//必填项检查
        if (!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        RunningRecord runningRecord = runningService.getRunningRecordById(json.getString("runningRecordId"));
        if(null == runningRecord) {
            return Response.fail(ReturnStatus.RUNNING_RECORD_NOT_EXIST);//跑步记录不存在
        }
        //加载跑步记录所对应的那个学期设置的电子围栏数据
        List<Map<String,Object>> runningRuleNodeList = ruleCacheService.getRailNodeListByRailName(runningRecord.getSemesterId());
        //**************************************** 查询结束 返回结果 **************************************************/
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("runningRuleNodeList", runningRuleNodeList);
        return Response.success(resMap);
    }


    @Register
    @RequestMapping(value = {"/getRunningRuleBySchool"},method = {RequestMethod.POST})
    //@SystemControllerLog(description="查询跑步规则(青动力官方管理系统--校园管理--学校信息)")
    @ApiOperation(value = "查询跑步规则(青动力官方管理系统--校园管理--学校信息)--pc--新增接口", notes = "官方大后台方法")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
            @ApiResponse(code = 10004,message = "参数错误"),
            @ApiResponse(code = 9001,message = "其他异常")})

    public Response<Map> getRunningRuleBySchool(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "用户Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json: schoolId 学校Id[必填]") @RequestBody JSONObject json
    ) throws Exception {
        if(null == json.get("schoolId")) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);
        }
        Long schoolId = json.getLong("schoolId");
        School school = schoolCacheService.getSchoolById(schoolId);//学校信息
        if(null == school) {
            return Response.fail(ReturnStatus.SCHOOL_NOT_EXIST);//学校不存在
        }
        Semester semester = schoolCacheService.getSemesterBySchoolId(schoolId);

        //**************************************** 业务数据查询开始 **************************************************/
        List<RunningRuleVo> runningRuleList = ruleUtilService.getRunningRuleListBySemesterId(semester.getSemesterId());
        //加载本学期设置的电子围栏数据
        List<Map<String,Object>> runningRuleNodeList = ruleCacheService.getRailNodeListByRailName(semester.getSemesterId());

        //加载本学期设置的晨练打卡点数据
        List<RailNode> signNodeList = ruleCacheService.getMorningSignNodeListBySemesterId(semester.getSemesterId());
        //**************************************** 查询结束 返回结果 **************************************************/
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("runningRuleList", runningRuleList);
        resMap.put("runningRuleNodeList", runningRuleNodeList);
        resMap.put("signNodeList", signNodeList);
        return Response.success(resMap);
    }

    @Register
    @RequestMapping(value = {"/getSemesterTarget"},method = {RequestMethod.GET})
    @ApiOperation(value = "查询跑步规则(青动力官方管理系统--校园管理--学校信息)--pc--新增接口", notes = "官方大后台方法")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
            @ApiResponse(code = 10004,message = "参数错误"),
            @ApiResponse(code = 9001,message = "其他异常")})

    public Response<Map> getSemesterTarget(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "用户Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign
    ) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        return Response.success(ruleUtilService.getTargetValuesJson(userSchool.getSemesterId()));
    }
}
