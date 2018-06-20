package com.peipao.qdl.school.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.ReturnConstant;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.StringUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.school.model.School;
import com.peipao.qdl.school.model.Semester;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.user.model.User;
import com.peipao.qdl.user.model.UserTypeEnum;
import com.peipao.qdl.user.service.UserService;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理学校中的业务逻辑
 * @author meteor.wu
 * @since 2017/6/29
 **/
@RestController
@RequestMapping({"/school"})
@Api(value = "/school", description = "学校管理查询")
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

    @Autowired
    private UserService userService;

    @Register
    @RequestMapping(value = {"/pc/getSemesterList"},method = {RequestMethod.GET})
//    @SystemControllerLog(description = "查询本校学期列表")
    @ApiOperation(value = "查询本校学期列表--53--ok")
    public Response<List> getSemesterList(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {

        return Response.success(schoolService.getSemesterList(userId));
    }

    @Register
    @RequestMapping(value = {"/pc/getSemesterYearList"},method = {RequestMethod.GET})
    @ApiOperation(value = "查询学校学年列表")
    public Response<List> getSemesterYearList(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {

        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        return Response.success(schoolService.getSemesterYearList(userSchool.getSchoolId()));
    }

    @Register
    @RequestMapping(value = {"/pc/updateSemester"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "更新学期")
    @ApiOperation(value = "更新学期(设置-校园设置--学期设置)--29--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
                    @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
                    @ApiResponse(code = ReturnConstant.SCHOOL_NOT_EXIST.value, message = ReturnConstant.SCHOOL_NOT_EXIST.desc),
                    @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
                    @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
                    @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> updateSemester(@ApiParam(required = true,value = "token") @RequestParam String token,
                                      @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                      @ApiParam(required = true, value = "上下学期开始时间必填") @RequestBody JSONObject json,
                                      @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {

        schoolService.updateSemester(userId, json);
        return Response.success(json);
    }

    @Register
    @RequestMapping(value = {"/pc/updateLevels"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "官方后台-修改运动等级")
    @ApiOperation(value = "官方后台-修改运动等级", notes = "updateLevels")
    public Response<?> updateLevels(
        @ApiParam(required = true,value = "token") @RequestParam String token,
        @ApiParam(required = true, value = "userId") @RequestParam Long userId,
        @ApiParam(required = true,value = "签名") @RequestParam String sign,
        @ApiParam(required = true, value = "等级列表对象属性") @RequestBody JSONObject json
    ) throws Exception {
        schoolService.updateLevels(userId, json);
        return Response.success(json);
    }

    @Register
    @RequestMapping(value = {"/pc/getLevels"},method = {RequestMethod.GET})
//    @SystemControllerLog(description = "官方后台-查询运动等级列表")
    @ApiOperation(value = "官方后台-查询运动等级列表", notes = "getLevels")
    public Response<?> getLevels(
        @ApiParam(required = true,value = "token") @RequestParam String token,
        @ApiParam(required = true, value = "userId") @RequestParam Long userId,
        @ApiParam(required = true,value = "签名") @RequestParam String sign
    ) throws Exception {
        return Response.success(schoolService.getLevelList(userId));
    }

    @Register
    @RequestMapping(value = {"/pub/getCurrentWeekIndexAndWeekRange"},method = {RequestMethod.GET})
    @ApiOperation(value = "查询当前周次和周范围(课程)---ok")
    public Response<?> getCurrentWeekIndexAndWeekRange(@ApiParam(required = true,value = "token") @RequestParam String token,
                                   @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                   @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {

        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }

        User user = userService.validateUser(userId);
        if ( user.getUserType() == UserTypeEnum.STUDENT.getValue() &&  userSchool.getCourseId() == null) {
            userSchool.setCourseId(0L);
        }
        Semester semester = schoolService.getSemesterById(userSchool.getSemesterId());

        Map<String, Object> map = new HashMap<>();
        int currentWeekIndex = DateUtil.getWeekCount(DateUtil.dateToStr(semester.getStartTime(), DateUtil.PATTERN_DATE),
                DateUtil.dateToStr(Calendar.getInstance().getTime(), DateUtil.PATTERN_DATE));
        int maxWeekIndex = 25;
        currentWeekIndex = currentWeekIndex > maxWeekIndex ? maxWeekIndex : currentWeekIndex;

        map.put("currentWeekIndex", currentWeekIndex);
        map.put("minWeekIndex", 1);
        map.put("maxWeekIndex", maxWeekIndex);
        map.put("courseId", userSchool.getCourseId());
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/pc/getCurrentSemesterDetail"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询本校学期列表")
    @ApiOperation(value = "查询本校学期列表(设置--校园设置--学期设置)--28--ok")
    public Response<?> getCurrentSemesterDetail(@ApiParam(required = true,value = "token") @RequestParam String token,
                                   @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                   @ApiParam(required = true,value = "签名") @RequestParam String sign,
                                   @ApiParam(required = true, value = "json:city,isCon,pageindex, pagesize") @RequestBody JSONObject json) throws Exception {
        String semesterYear = json.containsKey("semesterYear") && json.getString("semesterYear").length() > 0 ? json.getString("semesterYear") : null;
        List<Map<String, Object>> map = schoolService.getCurrentSemesterDetail(userId, semesterYear);
        return Response.success(map);
    }



    @Register
    @RequestMapping(value = {"/pc/getSchoolBasic"},method = {RequestMethod.GET})
//    @SystemControllerLog(description = "查询校园基本信息")
    @ApiOperation(value = "查询校园基本信息(设置-校园设置-校园基本信息)--35-ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
                    @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
                    @ApiResponse(code = ReturnConstant.SCHOOL_NOT_EXIST.value, message = ReturnConstant.SCHOOL_NOT_EXIST.desc),
                    @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
                    @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
                    @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getSchoolBasic(@ApiParam(required = true,value = "token") @RequestParam String token,
                                   @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                   @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {

        Map<String, Object> map = schoolService.getSchoolBasic(userId);
        return Response.success(map);
    }


    @Register
    @RequestMapping(value = {"/pc/getSchoolList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询学校列表")
    @ApiOperation(value = "查询学校列表(官方后台主页)--1--ok")
    public Response<Map<String, Object>> getSchoolList(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:city,isCon,pageindex, pagesize") @RequestBody JSONObject json,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
//        String[] params = new String[]{"pageindex","pagesize"};//必填项
//        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
//            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
//        }
        int[] page = MyPage.isValidParams(json);
        String name = null;
        if (json.containsKey("name")) {
            name = json.getString("name");
        }
        Integer isCon = json.containsKey("isCon") && json.get("isCon") != null ? json.getInt("isCon") : null;
        String city = json.containsKey("city") ? json.getString("city") : null;

        String sortName = json.containsKey("sortName") ? json.getString("sortName") : null;
        if(StringUtil.isNotEmpty(sortName) && !sortName.equals("usernum")){
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);
        }
        String sortType = json.containsKey("sortType") ? json.getString("sortType") : null;
        Map<String, Object> map = schoolService.getSchoolList(userId, city, isCon, page[0], page[1], name,sortName,sortType);
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/pc/updateSchoolBySchoolManager"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "编辑学校")
    @ApiOperation(value = "编辑学校(后台主页)--2--ok")
    public Response<School> updateSchoolBySchoolManager(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:schoolId, logoURL") @RequestBody School school,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        if (school.getSchoolId() == null) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        schoolService.updateSchoolBySchoolManager(userId, school);
        return Response.success();
    }

    /*********************************************************************************************************************/

    @Register
    @RequestMapping(value = {"/pc/updateSchool"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "编辑学校(官方后台主页)")
    @ApiOperation(value = "编辑学校(官方后台主页)--2--ok")
    public Response<?> updateSchool(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:schoolId, isAuth,isCon,contractTime,contact,mobile") @RequestBody School school,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        if (school.getSchoolId() == null) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        schoolService.updateSchool(userId, school);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("schoolId", school.getSchoolId());
        paramsMap.put("userType", UserTypeEnum.STUDENT.getValue());
        paramsMap.put("status", WebConstants.Boolean.TRUE.ordinal());
        Integer count = userService.getSchoolUserCount(paramsMap);

        JSONObject json = new JSONObject();
        json.put("key", school.getSchoolId());
        json.put("schoolId", school.getSchoolId());
        json.put("name", school.getName());
        json.put("mobile", school.getMobile());
        json.put("contact", school.getContact());
        json.put("contractTime", DateUtil.dateToStr(school.getContractTime(), DateUtil.PATTERN_DATE));
        json.put("city", school.getCity());
        json.put("code", school.getCode());
        json.put("usernum", count);
        json.put("isAuth", school.getAuth() ? 1 : 0);
        json.put("isCon", school.getIsCon());
        return Response.success(json);
    }

    @Register
    @RequestMapping(value = {"/pc/getCurrentSemesterDetailBySchoolId"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "通过schoolId查询本校学期列表")
    @ApiOperation(value = "通过schoolId查询本校学期列表(官方后台--校园信息--学校信息)--4--ok")
    public Response<?> getCurrentSemesterDetailBySchoolId(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:schoolId") @RequestBody JSONObject json,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("schoolId")){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("schoolId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        List<Map<String, Object>> map = schoolService.getCurrentSemesterDetailBySchoolId(userId, json.getLong("schoolId"));
        return Response.success(map);
    }


    @Register
    @RequestMapping(value = {"/pc/getSchoolBasicById"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "使用schoolId查询学校信息")
    @ApiOperation(value = "使用schoolId查询学校信息(官方后台--校园管理--学校信息)")
    public Response<Map<String, Object>> getSchoolBasicById(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:schoolId") @RequestBody JSONObject json,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {

        if (!json.containsKey("schoolId")){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("schoolId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Map<String, Object> map = schoolService.getSchoolBasicById(userId, json.getLong("schoolId"));
        return Response.success(map);
    }
}
