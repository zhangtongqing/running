package com.peipao.qdl.statistics.controller.pc.query;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.framework.util.JsonUtils;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.running.model.RunningEffectiveEnum;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.running.service.RecordService;
import com.peipao.qdl.runningrule.model.vo.RunningRuleVo;
import com.peipao.qdl.runningrule.service.utils.RuleUtilService;
import com.peipao.qdl.school.model.Semester;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.model.enums.SemesterType;
import com.peipao.qdl.school.service.SchoolCacheService;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.statistics.service.RunningStatisticService;
import com.peipao.qdl.statistics.service.UserStatisticService;
import com.peipao.qdl.statistics.service.UserStatisticUtilService;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 方法名称：StatisticsQueryControllerPC
 * 功能描述：StatisticsQueryControllerPC
 * 作者：Liu Fan
 * 版本：2.0.11
 * 创建日期：2018/1/26 13:59
 * 修订记录：
 */
@Api(value = "/statistics/pc",description = "学生跑步成绩统计信息")
@RestController
@RequestMapping({"/statistics/pc"})
public class StatisticsQueryControllerPC {
    @Autowired
    private UserStatisticService userStatisticService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private UserStatisticUtilService userStatisticUtilService;
    @Autowired
    private SchoolCacheService schoolCacheService;
    @Autowired
    private RunningStatisticService runningStatisticService;
    @Autowired
    private RuleUtilService ruleUtilService;
    @Autowired
    private RecordService recordService;


    @Register
    @RequestMapping(value = {"/getStudentScoreInfoList"}, method = {RequestMethod.POST})
    //@SystemControllerLog(description = "成绩管理-成绩查询")
    @ApiOperation(value = "成绩管理-成绩查询")
    public Response<?> getStudentScoreInfoList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "老师userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "RequestBody查询条件") @RequestBody JSONObject json
    ) throws Exception {
        int[] page = MyPage.isValidParams(json);
        json.remove("pageindex");
        json.remove("pagesize");
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        if(ValidateUtil.jsonValidateWithKey(json, "semesterId")) {
            userSchool.setSemesterId(json.getLong("semesterId"));
        } else {
            json.put("semesterId", userSchool.getSemesterId());
        }
        json.put("schoolId", userSchool.getSchoolId());
        ValidateUtil.createQueryTypeParams(json);
        ValidateUtil.createSortParams(json);
        createClassNameString(json);
        userStatisticUtilService.createCourseIdString(json, userId, userSchool.getSemesterId());
        if (json.getString("courseArray").startsWith("0")) {
            json.put("unselectCourse", "待定");
        }
        Map<String, Object> paramsMap = JsonUtils.getMap4Json(json.toString());
        MyPageInfo pageInfo = userStatisticService.getStudentScoreInfoList(paramsMap, page[0], page[1]);
        return Response.success(pageInfo);
    }

    @Register
    @RequestMapping(value = {"/getStudentScoreInfoListExport"}, method = {RequestMethod.POST})
    //@SystemControllerLog(description = "成绩管理-成绩查询导出")
    @ApiOperation(value = "成绩管理-成绩查询导出")
    public Response<?> getStudentScoreInfoListExport(
            HttpServletResponse response, HttpServletRequest request,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "老师userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = false, value = "RequestBody查询条件") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"morningRunningCount","morningRunningScore","freeRunningLength","freeRunningScore"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);//必填项请求参数不完整，请检查
        }
        if (json.getInt("morningRunningCount") <= 0 || json.getInt("morningRunningScore") <= 0
                || json.getInt("freeRunningLength") <= 0 || json.getInt("freeRunningScore") <= 0) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }

        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        if(ValidateUtil.jsonValidateWithKey(json, "semesterId")) {
            userSchool.setSemesterId(json.getLong("semesterId"));
        } else {
            json.put("semesterId", userSchool.getSemesterId());
        }
        json.put("schoolId", userSchool.getSchoolId());
        //如果学校开启了SemesterId学期的晨练功能
        boolean morningTrainFlag = false;
        RunningRuleVo runningRule = ruleUtilService.getRunningRuleBySemesterIdAndType(userSchool.getSemesterId(), RunningEnum.MORNINGTRAINING.getValue());
        if(null != runningRule && runningRule.getIsUse() == WebConstants.Boolean.TRUE.ordinal()) {
            morningTrainFlag = true;
            params = new String[]{"morningTrainCount","morningTrainScore"};//必填项
            if (json.getInt("morningTrainCount") <= 0 || json.getInt("morningTrainScore") <= 0) {
                return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
            }
        }

        ValidateUtil.createQueryTypeParams(json);
        ValidateUtil.createSortParams(json);
        createClassNameString(json);
        userStatisticUtilService.createCourseIdString(json, userId, userSchool.getSemesterId());
        if (json.getString("courseArray").startsWith("0")) {
            json.put("unselectCourse", "待定");
        }
        Map<String, Object> paramsMap = JsonUtils.getMap4Json(json.toString());
        List<Map<String, Object>> list = userStatisticService.getStudentScoreInfoListExport(paramsMap, morningTrainFlag);
        paramsMap.clear();
        if (list != null && list.size() > 0) {
            Semester semester = schoolCacheService.getSemesterBySchoolId(userSchool.getSchoolId());
            String fileName = semester.getSemesterYear() + "学年 ";
            if(semester.getSemesterType() == SemesterType.FIRST_SEMESTER.getValue()) {
                fileName = fileName + SemesterType.FIRST_SEMESTER.getTypeName();
            } else if(semester.getSemesterType() == SemesterType.SECOND_SEMESTER.getValue()) {
                fileName = fileName + SemesterType.SECOND_SEMESTER.getTypeName();
            }
            String path = userStatisticUtilService.createStudentScoreInfoListExport(response, request, list, fileName, morningTrainFlag);
            paramsMap.put("path", path);
            paramsMap.put("size", list.size());
        } else {
            paramsMap.put("path", "");
            paramsMap.put("size", 0);
        }
        return Response.success(paramsMap);
    }

    @Register
    @RequestMapping(value = {"/getStudentStatistics"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询学生综合成绩")
    @ApiOperation(value = "getStudentStatistics", notes = "查询学生综合成绩")
    public Response<?> getStudentStatistics(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = false, value = "RequestBody查询条件") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"studentId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);//必填项请求参数不完整，请检查
        }
        long studentId = json.getLong("studentId");
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", studentId);
        paramsMap.put("semesterId", userSchool.getSemesterId());
        Map<String, Object> resMap = runningStatisticService.getStudentStatistics(paramsMap);
        if(!CollectionUtils.isEmpty(resMap)) {
            RunningRuleVo runningRule = ruleUtilService.getRunningRuleBySemesterIdAndType(userSchool.getSemesterId(), RunningEnum.MORNINGTRAINING.getValue());
            if(null == runningRule || runningRule.getIsUse() == WebConstants.Boolean.FALSE.ordinal()) {
                resMap.remove("morningTrainCount");
            }
            return Response.success(resMap);
        } else {
            return Response.success(new JSONObject());
        }
    }



    @Register
    @RequestMapping(value = {"/getStudentRunningList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "个人主页-运动数据列表-PC")
    @ApiOperation(value = "个人主页-运动数据列表")
    public Response<MyPageInfo> getStudentRunningList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json:studentId：学生ID[必填]; semesterId:学期Id[非必填,不填默认当前学期]; 翻页参数:pageindex,pagesize") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"studentId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);//必填项请求参数不完整，请检查
        }
        int[] page = MyPage.isValidParams(json);
        UserSchool userSchool = schoolService.getParaByUserId(json.getLong("studentId"),  new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("semesterId", userSchool.getSemesterId());
        paramsMap.put("isEffective", RunningEffectiveEnum.Success.getCode());
        paramsMap.put("userId", userSchool.getUserId());
        if(ValidateUtil.jsonValidateWithKey(json, "type")) {
            paramsMap.put("type", json.getInt("type"));
        }
        MyPageInfo myPageInfo = recordService.getStudentRunningList(paramsMap, page[0], page[1]);
        return Response.success(myPageInfo);
    }

    @Register
    @RequestMapping(value = {"/getStudentActivityRunningList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "个人主页-跑步活动数据列表-PC")
    @ApiOperation(value = "个人主页-跑步活动数据列表")
    public Response<MyPageInfo> getStudentActivityRunningList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json:studentId：学生ID[必填]; 翻页参数:pageindex,pagesize") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"studentId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);//必填项请求参数不完整，请检查
        }
        int[] page = MyPage.isValidParams(json);
        UserSchool userSchool = schoolService.getParaByUserId(json.getLong("studentId"),  new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("semesterId", userSchool.getSemesterId());
        paramsMap.put("isEffective", RunningEffectiveEnum.Success.getCode());
        paramsMap.put("userId", userSchool.getUserId());
        MyPageInfo myPageInfo = recordService.getStudentActivityRunningList(paramsMap, page[0], page[1]);
        return Response.success(myPageInfo);
    }



    private void createClassNameString(JSONObject json) {
        if(ValidateUtil.jsonValidateWithKey(json, "classnameArray")) {
            String classnameArrayStr = "'" + json.getString("classnameArray") + "'";
            if(classnameArrayStr.contains(",")) {
                classnameArrayStr = classnameArrayStr.replaceAll(",", "','");
            }
            json.put("classnameArray", classnameArrayStr);
            if (json.getString("classnameArray").contains("未指定")) {// 班级里面用户选择了未指定，那么添加一个查询条件
                json.put("unselectClassname", "未指定");
            }
        } else {
            json.remove("classnameArray");
        }

    }

}
