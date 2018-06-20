package com.peipao.qdl.user.controller;


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
import com.peipao.qdl.running.model.RunningEffectiveEnum;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.runningrule.model.vo.RunningRuleVo;
import com.peipao.qdl.runningrule.service.utils.RuleUtilService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.school.service.UserLevelService;
import com.peipao.qdl.sms.model.SmsVo;
import com.peipao.qdl.sms.service.SmsService;
import com.peipao.qdl.statistics.service.RunningStatisticService;
import com.peipao.qdl.user.model.User;
import com.peipao.qdl.user.service.UserCacheService;
import com.peipao.qdl.user.service.UserService;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping({"/user"})
@Api(value = "/user", description = "用户信息处理API")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserLevelService userLevelService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private RuleUtilService ruleUtilService;
    @Autowired
    private RunningStatisticService runningStatisticService;

    @Register
    @RequestMapping(value = {"/updateUser/base"}, method = {RequestMethod.PUT})
    @SystemControllerLog(description="更新用户基本信息")
    @ApiOperation(value = "更新用户基本信息", notes = "更新用户信息(除过电话、密码)")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> updateUserBasicInfo(@ApiParam(required = true, value = "token") @RequestParam String token,
                                           @ApiParam(required = true, value = "token") @RequestParam Long userId,
                                           @ApiParam(required = true, value = "可以更新一个或者多个用户属性") @RequestBody User user,
                                           @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (StringUtil.isNotEmpty(user.getMobile())) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        user.setUserId(userId);
        user.setUsername(null);
        user.setSex(null);
        user.setAdmission(null);
        user.setClassname(null);
        user.setStudentNO(null);

        userCacheService.updateUser(user);

        return Response.success();
    }

    @Register
    @RequestMapping(value = {"/pc/updateUser/base"}, method = {RequestMethod.POST})
    @SystemControllerLog(description="更新用户基本信息pc")
    @ApiOperation(value = "更新用户基本信息", notes = "更新用户信息(除过电话、密码)")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> updateUserBasicInfoForWeb(@ApiParam(required = true, value = "token") @RequestParam String token,
                                           @ApiParam(required = true, value = "token") @RequestParam Long userId,
                                           @ApiParam(required = true, value = "可以更新一个或者多个用户属性") @RequestBody User user,
                                           @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        user.setUserId(userId);
        userCacheService.updateUser(user);
//        userService.updatePhone();

        return Response.success();
    }

    @Register
    @RequestMapping(value = {"/app/getUserForPersonalInfo"}, method = {RequestMethod.GET})
    @SystemControllerLog(description="根据用户ID获取用户信息")
    @ApiOperation(value = "根据用户ID获取用户信息(我的-个人资料)", notes = "注册用户权限")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getUserForPersonalInfo(@ApiParam(required = true, value = "token") @RequestParam String token,
                                   @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                   @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        Map<String, Object> user = userService.getUserForPersonalInfo(userId);
        return Response.success(user);
    }

    @RequestMapping(value = {"/updatePassword"}, method = {RequestMethod.PUT})
    @SystemControllerLog(description="修改密码app")
    @ApiOperation(value = "修改密码", notes = "注册用户权限")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.USER_PASSWORD_INCORRECT.value, message = ReturnConstant.USER_PASSWORD_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response updatePassword(@ApiParam(required = true, value = "token") @RequestParam String token,
                                   @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                   @ApiParam(required = true, value = "json包含password（新密码）, oldPassword") @RequestBody JSONObject json,
                                   @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {

        updatePassword(json, userId);
        return Response.success();
    }

    @RequestMapping(value = {"/pc/updatePassword"}, method = {RequestMethod.POST})
    @SystemControllerLog(description="修改密码pc")
    @ApiOperation(value = "修改密码", notes = "注册用户权限")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.USER_PASSWORD_INCORRECT.value, message = ReturnConstant.USER_PASSWORD_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response updatePasswordForWeb(@ApiParam(required = true, value = "token") @RequestParam String token,
                                   @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                   @ApiParam(required = true, value = "json包含password（新密码）, oldPassword") @RequestBody JSONObject json,
                                   @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {


        updatePassword(json, userId);
        return Response.success();
    }

    private Response<T> updatePassword(JSONObject json, Long userId) throws Exception {
        if (!json.containsKey("oldPassword") || !json.containsKey("password")){
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }

        String oldPassword = json.getString("oldPassword");
        String password = json.getString("password");

        String enPassword = StringUtil.decode(password);
        String enOldPassword = StringUtil.decode(oldPassword);

        userService.updatePassword(userId, enPassword, enOldPassword);
        return null;
    }

    @Register
    @RequestMapping(value = {"/app/updateMobile"}, method = {RequestMethod.PUT})
    @SystemControllerLog(description="更换手机号码")
    @ApiOperation("更换手机号码(个人设置修改手机号)")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_VERIFICATION_CODE_INCORRECT.value, message = ReturnConstant.USER_VERIFICATION_CODE_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_VERIFICATION_CODE_EXPIRED.value, message = ReturnConstant.USER_VERIFICATION_CODE_EXPIRED.desc),
            @ApiResponse(code = ReturnConstant.USER_SAME_PHONE.value, message = ReturnConstant.USER_SAME_PHONE.desc),
            @ApiResponse(code = ReturnConstant.USER_PASSWORD_INCORRECT.value, message = ReturnConstant.USER_PASSWORD_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_ALREADY_EXIST.value, message = ReturnConstant.USER_ALREADY_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> updateMobile(@ApiParam(required = true, value = "token") @RequestParam String token,
                                    @ApiParam(required = true, value = "token") @RequestParam Long userId,
                                    @ApiParam(required = true, value = "json包含password, <br>mobile(新电话号码), verCode 验证码") @RequestBody JSONObject json,
                                    @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("verCode") || !json.containsKey("password")|| !json.containsKey("mobile")){
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }

        String verCode = json.getString("verCode");
        String password = json.getString("password");
        String mobile = json.getString("mobile");
        if (StringUtil.isEmpty(verCode) || StringUtil.isEmpty(password) || StringUtil.isEmpty(mobile)) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        if (!ValidateUtil.isMobile(mobile)) {
            return Response.fail(ReturnStatus.USER_FAULT_PHONE);
        }

        SmsVo smsVo = SmsService.smsMap.get(mobile);
        if (smsVo == null || StringUtil.isEmpty(verCode) || !verCode.equals(smsVo.getCode())) {
            return Response.customStatus(ReturnStatus.USER_VERIFICATION_CODE_INCORRECT);
        }

        if (new Date().after(smsVo.getExpireTime())) {
            return Response.customStatus(ReturnStatus.USER_VERIFICATION_CODE_EXPIRED);
        }

        userService.updatePhone(userId, mobile, password);
        return Response.success();
    }

    @Register
    @RequestMapping(value = {"/checkPasswordWithPhone"}, method = {RequestMethod.PUT})
    @SystemControllerLog(description="手机号+密码校验匹配")
    @ApiOperation("手机号+密码校验匹配")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_PASSWORD_INCORRECT.value, message = ReturnConstant.USER_PASSWORD_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.USER_FAULT_PHONE.value, message = ReturnConstant.USER_FAULT_PHONE.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> checkPassword(@ApiParam(required = true, value = "token") @RequestParam String token,
                                     @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                     @ApiParam(required = true, value = "必须包含password, mobile") @RequestBody User user,
                                     @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (StringUtil.isEmpty(user.getPassword()) || StringUtil.isEmpty(user.getMobile())) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        if (!ValidateUtil.isMobile(user.getMobile())) {
            return Response.fail(ReturnStatus.USER_FAULT_PHONE);
        }

        User userTemp = userService.getUserByMobile(user.getMobile());
        if (userTemp == null) {
            return Response.customStatus(ReturnStatus.USER_NOT_EXIST);
        }
        if (userTemp.getUserId().longValue() != userId) {

            return Response.fail(ReturnStatus.MOBILE__NUMBER_NOT_MISMATCH);
        }
        String password = StringUtil.decode(user.getPassword());
        if (!userTemp.getPassword().equals(password)) {
            return Response.customStatus(ReturnStatus.USER_PASSWORD_INCORRECT);
        }
        return Response.success();
    }

    @Register
    @RequestMapping(value = {"/pub/getImageUploadToken"}, method = {RequestMethod.GET})
    //@SystemControllerLog(description="七牛上传token获取")
    @ApiOperation("七牛上传token获取")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<String> getImageUploadToken(@ApiParam(required = true, value = "token") @RequestParam String token,
                                                 @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                                 @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        String upToken = userService.getImageUploadToken(userId);
        return Response.success(upToken);
    }

    @Register
    @RequestMapping(value = {"/app/getUserForMainPage"}, method = {RequestMethod.GET})
//    @SystemControllerLog(description="查询个人主页信息")
    @ApiOperation(value = "查询个人主页信息(我的)--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getUserInforForMainPage(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {

        //进入 ‘我的’时，刷新我的等级
        /**********************************跑步记录更新后，更新学生运动等级信息***************************************/
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null != userSchool && null != userSchool.getSchoolId() && null != userSchool.getSemesterId()) {
            userLevelService.updateUserLevel(userId, userSchool.getSemesterId());
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Map<String, Object> map = userService.getUserForMainPage(userId);
        return Response.success(map);
    }


    /**
     *  获取用户中心数据
     *  auth:张同情 date:2018-04-09
     * @param token
     * @param userId
     * @param sign
     * @return
     * @throws Exception
     */
    @Register
    @RequestMapping(value = {"/app/getUserCenterData"}, method = {RequestMethod.POST})
    @ApiOperation(value = "获取用户中心数据--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getUserCenterData(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json: userId 查询用户Id") @RequestBody JSONObject json)
            throws Exception {

        String[] params = new String[]{"userId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long _userId = Long.valueOf(json.getInt("userId"));
        Map<String, Object> data = new HashMap<>();
        /**********************************名片区数据***************************************/
        Map<String, Object> card = userService.getUserForMainPage(_userId);

        /**********************************学期累计数据***************************************/
        UserSchool userSchool = schoolService.getParaByUserId(_userId, new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", _userId);
        paramsMap.put("schoolId", userSchool.getSchoolId());
        List<Map<String, Object>> statistics =  getTheStatistics(paramsMap, userSchool.getSemesterId());
        /**********************************获取自由跑、晨跑、活动跑 真实公里数据***************************************/
        BigDecimal realData = runningStatisticService.getRunRealData(_userId, paramsMap);
        Map<String,Object> realMap = new HashMap<>();
        realMap.put("level", card.get("level") != null && card.get("level").toString().replaceAll("\\s*", "").equals("") ? "lv1 运动素人" : card.get("level"));
        realMap.put("runningRealCount", realData);

        //-------------组装返回数据-----------------------
        data.put("realdata", realMap);
        data.put("card", card);
        data.put("statistics", statistics);
        //data.put("runWeekData",runWeekList);
        return Response.success(data);
    }


    /**
     * 获取7天跑步公里数据
     * @param token
     * @param userId
     * @param sign
     * @return
     * @throws Exception
     */
    @Register
    @RequestMapping(value = {"/app/getRunning7Data"}, method = {RequestMethod.POST})
//    @SystemControllerLog(description="查询个人主页信息")
    @ApiOperation(value = "获取个人主页7天跑步趋势数据--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getRunning7Data(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json: userId 查询用户Id") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"userId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long _userId = Long.valueOf(json.getInt("userId"));

        Map<String, Object> data = new HashMap<>();
        UserSchool userSchool = schoolService.getParaByUserId(_userId, new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", _userId);
        paramsMap.put("schoolId", userSchool.getSchoolId());
        /**********************************过去7天有效里程趋势***************************************/
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
        String beginTime = DateUtil.dateToStr(calendar.getTime(), "YYYY-MM-dd") + " 00:00:00";
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 6);
        String endTime = DateUtil.dateToStr(calendar.getTime(), "YYYY-MM-dd") + " 23:59:59";
        List<Date> dateArr = DateUtil.getBetweenDates(DateUtil.parseDate(beginTime, "yyyy-MM-dd HH:mm:ss"), DateUtil.parseDate(endTime, "yyyy-MM-dd HH:mm:ss"));
        LOGGER.info("beginTime:{},endTime:{}",beginTime,endTime);

        paramsMap.put("semesterId", userSchool.getSemesterId());
        paramsMap.put("isEffective", RunningEffectiveEnum.Success.getCode());//仅查询有效记录
        paramsMap.put("beginTime",beginTime);
        paramsMap.put("endTime",endTime);
        List<Map<String, Object>> runningData7day =  runningStatisticService.get7DayRunningLength(_userId, paramsMap);
        List<Map<String, Object>> runWeekList = new ArrayList<Map<String, Object>>();
        init7DaysInfo(dateArr, runningData7day, runWeekList);

        LOGGER.info("runningData7day ={}", runWeekList);

        //-------------组装返回数据-----------------------
        data.put("runWeekData",runWeekList);
        return Response.success(data);
    }


    private void init7DaysInfo(List<Date> dates, List<Map<String, Object>> weekListMap, List<Map<String, Object>> weekDayList) {
        for(Date date :dates){
            String tempDate = DateUtil.dateToStr(date,"yyyy-MM-dd");
            boolean hasDay = false;
            Map<String, Object> tempMap = new HashMap<>();
            for(Map<String, Object> map : weekListMap) {
                String dayStr = map.get("day").toString();
                if(dayStr.equals(tempDate)) {
                    hasDay = true;
                    tempMap = map;
                    break;
                }
            }
            if(hasDay){
                weekDayList.add(tempMap);
            }else {
                Map<String, Object> dayMap = new HashMap<>();
                dayMap.put("count",0);
                dayMap.put("day",tempDate);
                weekDayList.add(dayMap);
            }
        }
    }
    public static void main(String[] args){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
        String beginTime = DateUtil.dateToStr(calendar.getTime(), "YYYY-MM-dd") + " 00:00:00";
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 6);
        String endTime = DateUtil.dateToStr(calendar.getTime(), "YYYY-MM-dd") + " 23:59:59";
        LOGGER.info("beginTime:{},endTime:{}", beginTime, endTime);

        List<Date> data = DateUtil.getBetweenDates(DateUtil.parseDate(beginTime, "yyyy-MM-dd HH:mm:ss"), DateUtil.parseDate(endTime, "yyyy-MM-dd HH:mm:ss"));

        LOGGER.info("data.size={},data={}",data.size(),data);
    }

    /**
     * 获取学期 累计数据
     * @param paramsMap
     * @param semesterId
     * @return
     * @throws Exception
     */
    private List<Map<String, Object>> getTheStatistics(Map<String, Object> paramsMap, long semesterId) throws Exception {
        List<Map<String, Object>> resList = runningStatisticService.getMyStatistics(paramsMap);
        if(!CollectionUtils.isEmpty(resList)) {
            RunningRuleVo runningRule = ruleUtilService.getRunningRuleBySemesterIdAndType(semesterId, RunningEnum.MORNINGTRAINING.getValue());
            if(null == runningRule || runningRule.getIsUse() == WebConstants.Boolean.FALSE.ordinal()) {
                resList.forEach(map -> map.remove("morningTrainCount"));
            }
            return resList;
        }else {
            return new ArrayList<>();
        }
    }

//    @Register
//    @RequestMapping(value = {"/app/getUserBasicInfo"}, method = {RequestMethod.GET})
//    @ApiOperation(value = "查询用户基本信息")
//    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
//            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
//            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
//            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
//    public Response<String> getUserBasicInfo(@ApiParam(required = true, value = "token") @RequestParam String token,
//                                             @ApiParam(required = true, value = "userId") @RequestParam Long userId,
//                                             @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
//        String upToken = userService.getImageUploadToken(userId);
//        return Response.success(upToken);
//    }

    @Register
    @RequestMapping(value = {"/pc/getUserBasicInfoForweb"}, method = {RequestMethod.GET})
    //@SystemControllerLog(description="查询个人基本信息")
    @ApiOperation(value = "查询个人基本信息(设置-个人设置)--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getUserBasicInfoForweb(@ApiParam(required = true, value = "token") @RequestParam String token,
                                             @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                             @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        Map<String, Object> map = userService.getUserBasicInfoForweb(userId);
        map.put("userId", userId);
        return Response.success(map);
    }


    @Register
    @RequestMapping(value = {"/pc/getUsernameAndUserType"},method = {RequestMethod.GET})
    //@SystemControllerLog(description="用户登录")
    @ApiOperation(value = "用户登录--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getUsernameAndUserType(
            @ApiParam(required = true, value = "签名") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        return Response.success(userService.getUsernameAndUserType(userId));
    }

    @Register
    @RequestMapping(value = {"/app/getMobileById"},method = {RequestMethod.GET})
    //@SystemControllerLog(description="查询手机号")
    @ApiOperation(value = "查询手机号")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<String> getMobileById(
            @ApiParam(required = true, value = "签名") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        return Response.success(userService.getMobileById(userId));
    }

    @Register
    @RequestMapping(value = {"/pc/getStudentInfo"},method = {RequestMethod.POST})
    //@SystemControllerLog(description="查询学生信息(学生主页top)")
    @ApiOperation(value = "查询学生信息(学生主页top)--63--ok")
    public Response<Map> getStudentInfo(
            @ApiParam(required = true, value = "签名") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("studentId")){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("studentId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long studentId = json.getLong("studentId");
        return Response.success(userService.getStudentInfo(userId, studentId));
    }

    @Register
    @RequestMapping(value = {"/pub/getStudentInfoById"},method = {RequestMethod.POST})
    //@SystemControllerLog(description="查询学生信息(学生主页top)")
    @ApiOperation(value = "查询学生信息(学生主页top)--63--ok")
    public Response<Map> getStudentInfoById(
            @ApiParam(required = true, value = "签名") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("studentId")){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("studentId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long studentId = json.getLong("studentId");
        return Response.success(userService.getStudentInfoById(userId, studentId));
    }

    @Register
    @RequestMapping(value = {"/pc/getStudentByUsernameOrStudentNO"},method = {RequestMethod.POST})
    //@SystemControllerLog(description="查询学生信息(全局搜索)")
    @ApiOperation(value = "查询学生信息(全局搜索)--63--ok")
    public Response<Map> getStudentByUsernameOrStudentNO(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:value pageindex, pagesize") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("value")){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        String value = json.getString("value");
        int[] page = MyPage.isValidParams(json);
        Map<String, Object> map = userService.getStudentByUsernameOrStudentNO(userSchool.getSchoolId(), value, page[0], page[1]);
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/pc/getStudentList"},method = {RequestMethod.POST})
    //@SystemControllerLog(description="查询学生列表")
    @ApiOperation(value = "查询学生列表(官方后台--校园管理--学校信息--学生用户)--6--ok")
    public Response<Map> getStudentList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:schoolId pageindex, pagesize") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("schoolId")){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }

        Long schoolId = json.getLong("schoolId");
        int[] page = MyPage.isValidParams(json);
        Map<String, Object> map = userService.getStudentList(userId, schoolId, page[0], page[1]);
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/pc/addOfficialUser"},method = {RequestMethod.POST})
    @SystemControllerLog(description="添加官方管理员")
    @ApiOperation(value = "添加官方管理员(官方后台--系统管理--用户管理)--19--ok")
    public Response<?> addOfficialUser(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "user") @RequestBody User user,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (StringUtil.isEmpty(user.getUsername()) || StringUtil.isEmpty(user.getNickname()) || StringUtil.isEmpty(user.getMobile()) || user.getSex() == null
                || user.getCourseId() == null ){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        userService.addOfficialUser(userId, user);
        return Response.success(user);
    }

    @Register
    @RequestMapping(value = {"/pc/editOfficialUser"},method = {RequestMethod.POST})
    @SystemControllerLog(description="编辑官方管理员")
    @ApiOperation(value = "编辑官方管理员(官方后台--系统管理--用户管理)--19--ok")
    public Response<?> editOfficialUser(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "user") @RequestBody User user,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (null == user.getUserId()
                || StringUtil.isEmpty(user.getUsername())
                || StringUtil.isEmpty(user.getNickname())
                || user.getSex() == null
                || user.getCourseId() == null
        ){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        userService.addOfficialUser(userId, user);
        return Response.success(user);
    }

    @Register
    @RequestMapping(value = {"/pc/deleteOfficialUser"},method = {RequestMethod.POST})
    @SystemControllerLog(description="删除官方用户")
    @ApiOperation(value = "删除官方用户(官方后台--系统管理--用户管理)--20--ok")
    public Response<?> deleteOfficialUser(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "user") @RequestBody User user,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if ( user.getUserId() == null ){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        userService.deleteOfficialUser(userId, user.getUserId());

        return Response.success(user.getUserId());
    }

    @Register
    @RequestMapping(value = {"/pc/resetOfficialUserPassword"},method = {RequestMethod.POST})
    @SystemControllerLog(description="重置官方用户密码")
    @ApiOperation(value = "重置官方用户密码(官方后台--系统管理--用户管理)--21--ok")
    public Response<?> resetOfficialUserPassword(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "user") @RequestBody User user,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if ( user.getUserId() == null ){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        userService.resetOfficialUserPassword(userId, user.getUserId());

        return Response.success(user.getUserId());
    }

    @Register
    @RequestMapping(value = {"/pc/getOfficialUserList"},method = {RequestMethod.POST})
    //@SystemControllerLog(description="查询官方用户列表")
    @ApiOperation(value = "查询官方用户列表(官方后台--系统管理--用户管理)--21--ok")
    public Response<?> getOfficialUserList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "user") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        int[] page = MyPage.isValidParams(json);
        Map map = userService.getOfficialUserList(userId, page[0], page[1]);
        return Response.success(map);
    }


}
