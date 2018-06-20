package com.peipao.qdl.user.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.annotation.Visitor;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.ReturnConstant;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.StringUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.school.model.Level;
import com.peipao.qdl.school.model.School;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.school.service.UserLevelService;
import com.peipao.qdl.schooluser.model.Student;
import com.peipao.qdl.schooluser.service.SchoolUserService;
import com.peipao.qdl.sms.model.SmsVo;
import com.peipao.qdl.sms.service.SmsService;
import com.peipao.qdl.user.model.User;
import com.peipao.qdl.user.model.UserTypeEnum;
import com.peipao.qdl.user.service.UserCacheService;
import com.peipao.qdl.user.service.UserService;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@Api(value = "/login",description = "登录api")
@RestController
@RequestMapping({"/login"})
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private SchoolUserService schoolUserService;

    @Autowired
    private UserLevelService userLevelService;

    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Visitor
    @RequestMapping(value = {"/app/forgetPassword"},method = {RequestMethod.PUT})
    @SystemControllerLog(description="忘记密码")
    @ApiOperation("忘记密码")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
                   @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
                   @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
                   @ApiResponse(code = ReturnConstant.USER_FAULT_PHONE.value, message = ReturnConstant.USER_FAULT_PHONE.desc),
                   @ApiResponse(code = ReturnConstant.USER_VERIFICATION_CODE_INCORRECT.value, message = ReturnConstant.USER_FAULT_PHONE.desc),
                   @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> forgetPassword(@ApiParam(required = true,value = "json包含mobile和password，password需要md5加密,verCode") @RequestBody JSONObject user,
                                      @ApiParam(required = true,value = "sign") @RequestParam String sign) throws Exception {
        if (!user.containsKey("password") || !user.containsKey("mobile") || !user.containsKey("verCode")) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        String newPassword = user.getString("password");
        if (StringUtil.isEmpty(newPassword)) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        if (!ValidateUtil.isMobile(user.get("mobile").toString())) {
            Response.fail(ReturnStatus.USER_FAULT_PHONE);
        }
        //手机验证码检查
        String verCode = user.get("verCode").toString();
        SmsVo sms = SmsService.smsMap.get(user.get("mobile").toString());
        if (sms == null || new Date().after(sms.getExpireTime()) || !verCode.equals(sms.getCode())) {
            return Response.fail(ReturnStatus.USER_VERIFICATION_CODE_INCORRECT);
        }

        userService.updatePasswordByPhone(user.get("mobile").toString(), newPassword);
        return Response.success();
    }

    @Visitor
    @RequestMapping(value = {"/sendVerificationCode"},method = {RequestMethod.POST})
    @SystemControllerLog(description="发送验证码")
    @ApiOperation(value = "发送手机验证码")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
                    @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
                    @ApiResponse(code = ReturnConstant.USER_FAULT_PHONE.value, message = ReturnConstant.USER_FAULT_PHONE.desc),
                    @ApiResponse(code = ReturnConstant.USER_ALREADY_EXIST.value, message = ReturnConstant.USER_ALREADY_EXIST.desc),
                    @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<String> sendVerificationCode(@ApiParam(required = true, value = "json包含mobile") @RequestBody JSONObject json,
                                                 @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("mobile")) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        String mobile = json.getString("mobile");
        if (!ValidateUtil.isMobile(mobile)) {
            return Response.fail(ReturnStatus.USER_FAULT_PHONE);
        }
        // 检查手机是否已注册
        User tmpUser = userService.getUserByMobile(mobile);
        if (tmpUser != null) {
            return Response.fail(ReturnStatus.USER_ALREADY_EXIST);
        }

        String veriCode = smsService.sendVerificationCode(mobile);

        return Response.success(veriCode);
    }

    @Visitor
    @RequestMapping(value = {"/sendVerificationCodeNotCheck"},method = {RequestMethod.POST})
    @SystemControllerLog(description="发送手机验证码(notCheck),修改密码")
    @ApiOperation("发送手机验证码(notCheck),修改密码")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
                    @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
                    @ApiResponse(code = ReturnConstant.USER_FAULT_PHONE.value, message = ReturnConstant.USER_FAULT_PHONE.desc),
                    @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<String> sendVerificationCodeNotCheck(@ApiParam(required = true,value = "json包含mobile") @RequestBody JSONObject json,
                                                         @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("mobile")) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        String mobile = json.getString("mobile");
        if (!ValidateUtil.isMobile(mobile)) {
            return Response.fail(ReturnStatus.USER_FAULT_PHONE);
        }

        // 校验手机号必须存在
        User user = userService.getUserByMobile(mobile);
        if (user == null) {
            return Response.fail(ReturnStatus.USER_NOT_EXIST);
        }

        String veriCode = smsService.sendVerificationCode(mobile);

        SmsVo vo = new SmsVo();
        vo.setPhone(mobile);
        vo.setExpireTime(DateUtil.getDateAfterMinutes(new Date(), 30));
        vo.setCode(veriCode);
        SmsService.smsMap.put(mobile, vo);

        return Response.success(veriCode);
    }


    @Visitor
    @RequestMapping(value = {"/app/register"},method = {RequestMethod.POST})
    @SystemControllerLog(description="用户注册第一步")
    @ApiOperation(value = "用户注册第一步")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
                   @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
                   @ApiResponse(code = ReturnConstant.USER_FAULT_PHONE.value, message = ReturnConstant.USER_FAULT_PHONE.desc),
                   @ApiResponse(code = ReturnConstant.USER_ALREADY_EXIST.value, message = ReturnConstant.USER_ALREADY_EXIST.desc),
                   @ApiResponse(code = ReturnConstant.USER_VERIFICATION_CODE_INCORRECT.value,message = ReturnConstant.USER_VERIFICATION_CODE_INCORRECT.desc),
                   @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> registerUser(@ApiParam(required = true, value = "json包含mobile和password，密码md5加密,verCode") @RequestBody JSONObject json,
                                                      @ApiParam(required = true, value = "验证码") @RequestParam String sign) throws Exception {
        if (!json.containsKey("mobile") || !json.containsKey("verCode") || !json.containsKey("password")){
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        String mobile = json.getString("mobile");
        String verCode = json.getString("verCode");
        String password = json.getString("password");


        if (StringUtil.isEmpty(mobile) || StringUtil.isEmpty(password) || StringUtil.isEmpty(verCode)) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }

        if (!ValidateUtil.isMobile(mobile)) {
            return Response.fail(ReturnStatus.USER_FAULT_PHONE);
        }

        //手机验证码检查
        SmsVo sms = SmsService.smsMap.get(mobile);
        if (sms == null) {
            //内存中没有去redis中去找
            String key = "verifyCode:"+mobile;
            BoundValueOperations<String, String> ops = redisTemplate.boundValueOps(key);
            if(null == ops.get()){
                return Response.fail(ReturnStatus.USER_VERIFICATION_CODE_EXPIRED);
            }else{
                if(!verCode.equals(ops.get())){ //对比
                    return Response.fail(ReturnStatus.USER_VERIFICATION_CODE_INCORRECT);
                }
            }
        }else if(new Date().after(sms.getExpireTime())){
            return Response.fail(ReturnStatus.USER_VERIFICATION_CODE_EXPIRED);
        }else if(!verCode.equals(sms.getCode())){
            return Response.fail(ReturnStatus.USER_VERIFICATION_CODE_INCORRECT);
        }


        User user = new User();
        user.setMobile(mobile);
        user.setPassword(password);
        user.setUserType(UserTypeEnum.STUDENT.getValue());
        Map<String, Object> retUser = userService.createUser(user);
        return Response.success(retUser);
    }

    @Register
    @RequestMapping(value = "/app/registerBasic",method = {RequestMethod.POST})
    @SystemControllerLog(description="用户注册第二步")
    @ApiOperation(value = "学生注册第二步")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
                   @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
                   @ApiResponse(code = ReturnConstant.USER_FAULT_PHONE.value, message = ReturnConstant.USER_FAULT_PHONE.desc),
                   @ApiResponse(code = ReturnConstant.USER_ALREADY_EXIST.value, message = ReturnConstant.USER_ALREADY_EXIST.desc),
                   @ApiResponse(code = ReturnConstant.USER_VERIFICATION_CODE_INCORRECT.value,message = ReturnConstant.USER_VERIFICATION_CODE_INCORRECT.desc),
                   @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<User> registerBasic(@ApiParam(required = true,value = "包含imageURL,sex，username，nickname, height,weight") @RequestBody User user,
                                        @ApiParam(required = true,value = "token") @RequestParam String token,
                                        @ApiParam(required = true,value = "userId") @RequestParam Long userId,
                                        @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        if ( user.getSex() == null || StringUtil.isEmpty(user.getUsername()) || StringUtil.isEmpty(user.getNickname())
                || StringUtil.isEmpty(user.getImageURL()) || user.getWeight() == null || user.getHeight() == null) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        user.setUserId(userId);
        userCacheService.updateUser(user);
        return Response.success();
    }

    @Register
    @RequestMapping(value = "/app/registerSchool",method = RequestMethod.POST)
    @SystemControllerLog(description="用户注册第三步")
    @ApiOperation(value = "学生注册第三步")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
                   @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
                   @ApiResponse(code = ReturnConstant.USER_FAULT_PHONE.value, message = ReturnConstant.USER_FAULT_PHONE.desc),
                   @ApiResponse(code = ReturnConstant.USER_ALREADY_EXIST.value, message = ReturnConstant.USER_ALREADY_EXIST.desc),
                   @ApiResponse(code = ReturnConstant.USER_VERIFICATION_CODE_INCORRECT.value,message = ReturnConstant.USER_VERIFICATION_CODE_INCORRECT.desc),
                   @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> registerSchool(@ApiParam(required = true, value = "包含 schoolCode、studentNO、admission(2017-09)") @RequestBody User user,
                                         @ApiParam(required = true, value = "token") @RequestParam String token,
                                         @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                         @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (StringUtil.isEmpty(user.getSchoolCode()) || StringUtil.isEmpty(user.getStudentNO())
                || user.getUsername() == null ) {  // || user.getAdmission().compareTo(new Date()) >= 0
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }

        //校验姓名
        String userName = StringUtils.deleteWhitespace(user.getUsername()); //去除所有空格
        if(!ValidateUtil.checkUserName(userName)){
            return Response.fail(ReturnStatus.USERNAME_FORMAT_ERROR);
        }
        user.setUsername(userName);//自动去空格后回填
        School school = schoolService.getSchoolBySchoolCode(user.getSchoolCode());
        if (school == null) {
            return Response.fail(ReturnStatus.SCHOOL_NOT_EXIST);
        }
        Integer count = userService.checkStudentRegister(school.getSchoolId(), user.getStudentNO());
        if (count != null && count > 0) {
            throw new BusinessException(ReturnStatus.STUDENTNO_EXIST);
        }

        if (school.getAuth()) {
            Student student = new Student();
            student.setStudentNO(user.getStudentNO());
            student.setSchoolId(school.getSchoolId());
            Student map = schoolUserService.getByStudent(student);

            if (map != null) {
                if (map != null && map.getStatus() == WebConstants.Boolean.TRUE.ordinal()) {
                    return Response.fail(ReturnStatus.STUDENTNO_HAVE_AUTH);
                }
                if (!map.getUsername().equals(user.getUsername())) {
                    return Response.fail(ReturnStatus.USER_USERNAME_ERROR);
                }
                student = new Student();
                student.setUserId(map.getUserId());
                student.setStatus(WebConstants.Boolean.TRUE.ordinal());
                student.setuId(userId);
                schoolUserService.updateStudent(student);

                user.setUsername(map.getUsername());
                if (map.getSex() != null) {
                    user.setSex(map.getSex());
                }
                if (map.getClassname() != null) {
                    user.setClassname(map.getClassname());
                }
                if (map.getAdmission() != null) {
                    user.setAdmission(map.getAdmission());
                }
            }else{
                return Response.fail(ReturnStatus.USER_NEED_AUTH);
            }
        }

        Level level = userLevelService.findZeroLevelBySchoolId(WebConstants.qdl_level_school_id);
        if(null != level) {
            user.setLevelId(level.getLevelId());
        }
        user.setSchoolId(school.getSchoolId());
        user.setUserId(userId);
        if (user.getSex() == null) {
            user.setSex((byte)1);//性别给个默认值，防止出错
        }
        userCacheService.updateUser(user);
        return Response.success(user.getSchoolId());
    }

    @Visitor
    @RequestMapping(value = {"/app/login"},method = {RequestMethod.POST})
    @SystemControllerLog(description="用户登录app")
    @ApiOperation(value = "用户登录")
    public Response<Map<String, Object>> loginUserApp(
        @ApiParam(required = true, value = "签名") @RequestParam String sign,
        @ApiParam(required = true, value = "包含mobile password(md5加密)") @RequestBody User user
    ) throws Exception {
        if (StringUtil.isEmpty(user.getMobile()) || StringUtil.isEmpty(user.getPassword())) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        if (!ValidateUtil.isMobile(user.getMobile())) {
            return Response.fail(ReturnStatus.USER_FAULT_PHONE);
        }
        Map<String, Object> userTemp = userService.userLogin(user);

        /*********** 青动力帐号登录平台分割：运动端申请的帐号只能登录学生运动端App版 ***********/
        if(null != userTemp.get("userType")) {
            int userType = Integer.parseInt(userTemp.get("userType").toString());
            if(userType != UserTypeEnum.STUDENT.getValue()) {
                return Response.fail(ResultMsg.USER_TYPE_RUNNER_ONLY);//非跑步帐号不能进行此操作
            }
        } else {
            return Response.fail(ResultMsg.USER_TYPE_MISSING);//未能识别用户身份
        }
        userTemp.put("registerStep", 3);
        if(userTemp.get("schoolId") == null){
            userTemp.put("registerStep", 1);
        }
        return Response.success(userTemp);
    }

    @Visitor
    @RequestMapping(value = {"/pc/login"},method = {RequestMethod.POST})
    @SystemControllerLog(description="用户登录pc")
    @ApiOperation(value = "用户登录")
    public Response<Map<String, Object>> loginUserWeb(
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "包含nickname password(md5加密)") @RequestBody User user
    ) throws Exception {
        if (StringUtil.isEmpty(user.getNickname()) || StringUtil.isEmpty(user.getPassword())) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Map<String, Object> userTemp = userService.userLogin(user);
        /*********** 青动力帐号登录平台分割：青动力平台管理端帐号只能登录青动力平台管理端PC版 ***********/
        if(checkUserType(userTemp)) {
            return Response.fail(ResultMsg.USER_TYPE_SCHOOL_ONLY);//非校方帐号不能进行此操作
        }
        return Response.success(userTemp);
    }

    @Visitor
    @RequestMapping(value = {"/app/schoolLogin"},method = {RequestMethod.POST})
    @SystemControllerLog(description="用户登录--教务端APP")
    @ApiOperation(value = "用户登录--教务端APP")
    public Response<Map<String, Object>> schoolLogin(
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "包含 nickname password(md5加密)") @RequestBody User user
    ) throws Exception {
        if (StringUtil.isEmpty(user.getNickname()) || StringUtil.isEmpty(user.getPassword())) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Map<String, Object> userTemp = userService.userLogin(user);
        /*********** 青动力帐号登录平台分割：青动力平台管理端帐号只能登录青动力平台管理端APP端 ***********/
        if(checkUserType(userTemp)) {
            return Response.fail(ResultMsg.USER_TYPE_SCHOOL_ONLY);//非校方帐号不能进行此操作
        }
        return Response.success(userTemp);
    }

    @Visitor
    @RequestMapping(value = {"/pc/officialLogin"},method = {RequestMethod.POST})
    @SystemControllerLog(description="用户登录pc")
    @ApiOperation(value = "用户登录")
    public Response<Map<String, Object>> officialLogin(
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "包含nickname password(md5加密)") @RequestBody User user
    ) throws Exception {
        if (StringUtil.isEmpty(user.getNickname()) || StringUtil.isEmpty(user.getPassword())) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Map<String, Object> userTemp = userService.userLogin(user);
        /*********** 青动力帐号登录平台分割：青动力平台管理端帐号只能登录青动力平台管理端PC版 ***********/
        if(null != userTemp.get("userType")) {
            int userType = Integer.parseInt(userTemp.get("userType").toString());
            if(userType != UserTypeEnum.OFFICIALMANAGER.getValue()) {
                return Response.fail(ResultMsg.USER_TYPE_OFFICIAL_ONLY);//非官方帐号不能进行此操作
            }
        } else {
            return Response.fail(ResultMsg.USER_TYPE_MISSING);//未能识别用户身份
        }
        return Response.success(userTemp);
    }

    @Register
    @RequestMapping(value = {"/checkToken"},method = {RequestMethod.PUT})
    @SystemControllerLog(description="token校验登录")
    @ApiOperation("token校验登录")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<JSONObject> checkToken(@ApiParam(required = true,value = "token") @RequestParam String token,
                                     @ApiParam(required = true,value = "userId") @RequestParam Long userId,
                                     @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        JSONObject json = new JSONObject();
        json.accumulate("userId", userId);
        json.accumulate("token", token);
        return Response.success(json);
    }


    private boolean checkUserType(Map<String, Object> userTemp) {
        boolean error = false;
        if(null != userTemp.get("userType")) {
            int userType = Integer.parseInt(userTemp.get("userType").toString());
            if(userType != UserTypeEnum.SCHOOLMANAGER.getValue() && userType != UserTypeEnum.TEACHER.getValue()) {
                error = true;
            }
        } else {
            error = true;
        }
        return error;
    }
}
