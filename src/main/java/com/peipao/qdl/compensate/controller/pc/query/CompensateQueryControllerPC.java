package com.peipao.qdl.compensate.controller.pc.query;

import com.peipao.framework.annotation.Register;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.framework.util.JsonUtils;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.compensate.service.CompensateService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 方法名称：CompensateQueryControllerPC
 * 功能描述：CompensateQueryControllerPC
 * 作者：Liu Fan
 * 版本：2.0.11
 * 创建日期：2018/1/26 13:59
 * 修订记录：
 */
@Api(value = "/compensate/pc",description = "运动补偿查询")
@RestController
@RequestMapping({"/compensate/pc"})
public class CompensateQueryControllerPC {

    @Autowired
    private SchoolService schoolService;
    @Autowired
    private CompensateService compensateService;

    @Register
    @RequestMapping(value = {"/getCompensateByUser"}, method = {RequestMethod.POST})
//    @SystemControllerLog(description = "成绩管理-查询成绩补偿汇总信息")
    @ApiOperation(value = "成绩管理-查询成绩补偿汇总信息")
    public Response<?> getCompensateByUser(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "老师userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "RequestBody查询条件:studentId[必填]") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"studentId"};//必填项检查
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long studentId = json.getLong("studentId");
        UserSchool userSchool = schoolService.getParaByUserId(studentId,  new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        json.put("semesterId", userSchool.getSemesterId());
        Map<String, Object> paramsMap = JsonUtils.getMap4Json(json.toString());
        Map<String, Object> compensateMainMap = compensateService.getCompensateMainInfoForStudent(paramsMap);
        if(CollectionUtils.isEmpty(compensateMainMap)) {
            compensateMainMap = new HashMap<String, Object>();
            compensateMainMap.put("compensateScore", 0f);
            compensateMainMap.put("morningRunningCount", 0);
            compensateMainMap.put("runningLength", 0f);
        }
        return Response.success(compensateMainMap);
    }



    @Register
    @RequestMapping(value = {"/getCompensateListByUser"}, method = {RequestMethod.POST})
//    @SystemControllerLog(description = "成绩管理-查询用户补偿记录列表")
    @ApiOperation(value = "成绩管理-查询用户补偿记录列表")
    public Response<?> getCompensateListByUser(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "老师userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "RequestBody查询条件:studentId[必填]") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"studentId"};//必填项检查
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long studentId = json.getLong("studentId");
        UserSchool userSchool = schoolService.getParaByUserId(studentId,  new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        json.put("semesterId", userSchool.getSemesterId());
        Map<String, Object> paramsMap = JsonUtils.getMap4Json(json.toString());
        MyPageInfo myPageInfo = compensateService.getCompensateListForStudent(paramsMap, 0, 1000);//0,1000 默认翻页参数
        return Response.success(myPageInfo);
    }

}
