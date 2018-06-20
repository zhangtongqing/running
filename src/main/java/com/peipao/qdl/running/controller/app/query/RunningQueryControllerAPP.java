package com.peipao.qdl.running.controller.app.query;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.running.service.RecordService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
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
 * 方法名称：RunningEditControllerAPP
 * 功能描述：RunningEditControllerAPP
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/15 16:56
 * 修订记录：
 */

@Api(value = "/running/app",description = "跑步记录管理")
@RestController
@RequestMapping({"/running/app"})
public class RunningQueryControllerAPP {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private RecordService recordService;

    @Register
    @RequestMapping(value = {"/getMyRunningRecord"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询个人运动记录列表-app")
    @ApiOperation(value = "getMyRunningRecord", notes = "查询个人运动记录列表")
    public Response<?> getMyRunningRecord(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "分页参数") @RequestBody JSONObject json
    ) throws Exception {
        int[] page = MyPage.checkPageParams(json);
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("userId", userId);
        paramsMap.put("semesterId", userSchool.getSemesterId());
        paramsMap.put("activityRunType", RunningEnum.ACTIVITYRUN.getValue());
        MyPageInfo pageInfo = recordService.getMyRunningRecord(paramsMap, page);
        return Response.success(pageInfo);
    }


    /**
     * 查询个人晨练记录列表
     * @param token
     * @param userId
     * @param sign
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/getMyMorningExercisesRecord"},method = {RequestMethod.POST})
    @ApiOperation(value = "getMyMorningExercisesRecord", notes = "查询个人晨练记录列表")
    public Response<?> getMyMorningExercisesRecord(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "分页参数") @RequestBody JSONObject json
    ) throws Exception {
        int[] page = MyPage.checkPageParams(json);
        UserSchool userSchool = schoolService.getParaByUserId(userId, new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }

        String[] params = new String[]{"isEffective"};//必填项 有效标识
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        int isEffective = Integer.parseInt(json.get("isEffective").toString());
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("userId", userId);
        paramsMap.put("semesterId", userSchool.getSemesterId());
        paramsMap.put("isEffective",isEffective == 1 ? 88 : -1);
        MyPageInfo pageInfo = recordService.getMyMorningExercisesRecord(paramsMap, page);
        return Response.success(pageInfo);
    }

}

