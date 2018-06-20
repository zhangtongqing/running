package com.peipao.qdl.activity.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ReturnConstant;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.activity.model.Activity;
import com.peipao.qdl.activity.service.ActivityService;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/6/26
 **/
@Api(value = "/activity/app", description = "课程")
@RestController
@RequestMapping("/activity/app")
public class ActivityControllerAPP {
    @Autowired
    private ActivityService activityService;

    @Register
    @RequestMapping(value = "/getMyActivity", method = RequestMethod.POST)
    @ApiOperation(value = "查询用户参与的活动(活动中心-我参与)--18--ok", notes = "学生移动端")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
                    @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value,message = ReturnConstant.PARAMETER_INCORRECT.desc),
                    @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
                    @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<List<Map<String, Object>>> getMyActivity(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true,value = "userId") @RequestParam Long userId,
            @ApiParam(required = true,value = "json:from  num") @RequestBody JSONObject json,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        int[] page = MyPage.isValidAppParams(json);
        List<Map<String, Object>> ret = activityService.getMyActivityList(userId, page[0], page[1]);
        return Response.success(ret);
    }

    @Register
    @RequestMapping(value = {"/getOfficialActivity"},method = {RequestMethod.POST})
    @ApiOperation("查询官方发布的活动(活动中心-官方)--19--ok")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
                   @ApiResponse(code = 10004,message = "参数错误"),
                   @ApiResponse(code = 10300,message = "学校不存在"),
                   @ApiResponse(code = 9001,message = "其他异常")})
    public Response<List<Map<String, Object>>> getOfficialActivity(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true,value = "userId") @RequestParam Long userId,
        @ApiParam(required = true,value = "json:from  num") @RequestBody JSONObject json,
        @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
            int[] page = MyPage.isValidAppParams(json);
            List<Map<String, Object>> ret = activityService.getOfficialActivityList(userId, page[0], page[1]);
            return Response.success(ret);
    }

    @Register
    @RequestMapping(value = {"/getLocalSchoolActivity"},method = {RequestMethod.POST})
    @ApiOperation("查询本校发布的活动(活动中心-本校)--20--ok")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
                   @ApiResponse(code = 10004,message = "参数错误"),
                   @ApiResponse(code = 10300,message = "学校不存在"),
                   @ApiResponse(code = 9001,message = "其他异常")})
    public Response<List<Map<String, Object>>> getLocalSchoolActivity(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true,value = "userId") @RequestParam Long userId,
            @ApiParam(required = true,value = "json:from  num") @RequestBody JSONObject json,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {

        int[] page=MyPage.isValidAppParams(json);
        List<Map<String, Object>> ret = activityService.getActivityListBySchoolId(userId, page[0], page[1]);
        return Response.success(ret);
    }

    @Register
    @RequestMapping(value = {"/getActivityDetail"},method = {RequestMethod.POST})
    @ApiOperation("查询活动详情(活动中心-活动详情)--40--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
                   @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
                   @ApiResponse(code = ReturnConstant.SCHOOL_NOT_EXIST.value, message = ReturnConstant.SCHOOL_NOT_EXIST.desc),
                   @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Activity> getActivityDetailForApp(@ApiParam(required = true,value = "token") @RequestParam String token,
                                                @ApiParam(required = true,value = "userId") @RequestParam Long userId,
                                                @ApiParam(required = true,value = "活动ID") @RequestBody JSONObject json,
                                                @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("activityId")) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("activityId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }

        Long activityId = json.getLong("activityId");
        Activity activity = activityService.getActivityDetailForApp(userId, activityId);
        List<Map<String, Object>> headlist = activityService.getActivityByMemberList(activityId, WebConstants.Activity.ACTIVTITY_MEMBERLIST_SIZE);
        activity.setActivityMember(headlist);
        return Response.success(activity);
    }

    @Register
    @RequestMapping(value = {"/getActivityMemberList"},method = {RequestMethod.POST})
    @ApiOperation("查询活动成员列表(活动中心-活动详情-活动成员)--21--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
                   @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
                   @ApiResponse(code = ReturnConstant.SCHOOL_NOT_EXIST.value, message = ReturnConstant.SCHOOL_NOT_EXIST.desc),
                   @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getActivityMemberListForApp(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true,value = "userId") @RequestParam Long userId,
            @ApiParam(required = true,value = "json:activityId，") @RequestBody JSONObject json,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {

        if (!json.containsKey("activityId")) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        Long activityId = json.getLong("activityId");
        Map<String, Object> map = activityService.getActivityMemberList(activityId, userId);
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/enroll"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "活动报名")
    @ApiOperation("活动报名--16--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
                   @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
                   @ApiResponse(code = ReturnConstant.ACTIVITY_NOT_EXIST.value,message = ReturnConstant.ACTIVITY_NOT_EXIST.desc),
                   @ApiResponse(code = ReturnConstant.ACTIVITY_PARTICIPATE_NOFIT.value,message = ReturnConstant.ACTIVITY_PARTICIPATE_NOFIT.desc),
                   @ApiResponse(code = ReturnConstant.ACTIVITY_ENROLL_AT_TIME.value,message = ReturnConstant.ACTIVITY_ENROLL_AT_TIME.desc),
                   @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> enroll(@ApiParam(required = true, value = "token") @RequestParam String token,
                              @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                              @ApiParam(required = true, value = "json包含activityId") @RequestBody JSONObject json,
                              @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("activityId")){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("activityId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long activityId = json.getLong("activityId");
        activityService.enroll(userId, activityId);
        return Response.success(ReturnStatus.ACTIVITY_ENROLL_SUCESS);
    }

    @Register
    @RequestMapping(value = {"/sign"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "活动签到")
    @ApiOperation("活动签到--17--ok")
    public Response<?> sign(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json包含activityId,longitude,latitude,signAddress") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("activityId") || !json.containsKey("longitude") || !json.containsKey("latitude") || !json.containsKey("signAddress")  ){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("activityId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long activityId = json.getLong("activityId");
        Double longitude = json.getDouble("longitude");
        Double latitude = json.getDouble("latitude");
        String signAddress = json.getString("signAddress");
        activityService.sign(userId, activityId, longitude, latitude, signAddress);
        return Response.success();
    }

    @Register
    @RequestMapping(value = {"/getActivityAccess"},method = {RequestMethod.GET})
    @ApiOperation("运动首页显示可参加的活动")
    public Response<?> getActivityAccess(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {

        return Response.success(activityService.getActivityAccess(userId));
    }
}