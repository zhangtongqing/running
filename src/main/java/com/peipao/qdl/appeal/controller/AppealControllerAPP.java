package com.peipao.qdl.appeal.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.annotation.Visitor;
import com.peipao.framework.constant.ReturnConstant;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.util.StringUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.appeal.model.AppealStatusEnum;
import com.peipao.qdl.appeal.model.FeedbackRecord;
import com.peipao.qdl.appeal.service.AppealService;
import com.peipao.qdl.running.model.RunningRecord;
import com.peipao.qdl.running.service.RunningService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import io.swagger.annotations.*;
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
 * @author meteor.wu
 * @since 2017/6/27
 **/
@Api(value = "/appeal/app",description = "申诉")
@RestController
@RequestMapping({"/appeal/app"})
public class AppealControllerAPP {
    @Autowired
    EventBus eventBus;

    @Autowired
    private AppealService appealService;
    @Autowired
    private RunningService runningService;
    @Autowired
    private SchoolService schoolService;

    @Register
    @RequestMapping(value = {"/appeal"},method = {RequestMethod.POST})
    @ApiOperation(value = "跑步申诉", notes = "学生移动端")
    public Response<?> appeal(
        @ApiParam(required = true, value = "token") @RequestParam String token,
        @ApiParam(required = true, value = "userId") @RequestParam Long userId,
        @ApiParam(required = true, value = "签名") @RequestParam String sign,
        @ApiParam(required = true, value = "json参数:{runningRecordId:跑步记录Id}") @RequestBody JSONObject json
    ) throws Exception {
        if(null == json.get("runningRecordId")) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);//缺少参数
        }
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        json.accumulate("semesterId", userSchool.getSemesterId());//学期ID
        String runningRecordId = json.getString("runningRecordId");
        RunningRecord runningRecord = runningService.getRunningRecordById(runningRecordId);
        if(null == runningRecord || runningRecord.getUserId().longValue() != userId) {
            return Response.fail(ReturnStatus.RUNNING_RECORD_NOT_EXIST);//跑步记录不存在
        }
        if(runningRecord.getStatus() != AppealStatusEnum.NORMAL.getValue()) {
            return Response.fail(ReturnStatus.APPEAL_STATUS_ERROR);//跑步记录不符合申诉条件
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("runningRecordId", runningRecordId);
        paramsMap.put("status", AppealStatusEnum.AUDIT.getValue());
        paramsMap.put("appealTime", new Date());//申诉时间
        runningService.updateRunningRecordForAppeal(paramsMap);
        paramsMap = null;
        //发布一个自动审核事件
        eventBus.notify("autoAppealEvent", Event.wrap(json));
        return Response.success();
    }

    @Register
    @RequestMapping(value = {"/getAppealStatusByRunningRecordId"},method = {RequestMethod.POST})
//    @ApiOperation(value = "根据跑步ID查询申诉状态", notes = "学生移动端")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
                    @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value,message = ReturnConstant.PARAMETER_INCORRECT.desc),
                    @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Integer> getAppealStatus(@ApiParam(required = true, value = "token") @RequestParam String token,
                                             @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                             @ApiParam(required = true, value = "跑步ID") @RequestBody JSONObject json,
                                             @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        String  runningRecordId = json.getString("runningRecordId");

//        int status = appealService.getAppealStatusByRunningId(runningRecordId);
        RunningRecord runningRecord = runningService.getRunningRecordById(runningRecordId);
        if (runningRecord == null) {
            return Response.success(null);
        }
        return Response.success(runningRecord.getStatus());
    }



    @Register
    @RequestMapping(value = {"/addFeedback"}, method = {RequestMethod.POST})
    @SystemControllerLog(description = "添加帮助与反馈")
    @ApiOperation(value = "添加帮助与反馈")
    public Response<?> addFeedback(@ApiParam(required = true, value = "token") @RequestParam String token,
                                        @ApiParam(required = true, value = "学校ID") @RequestBody FeedbackRecord feedbackRecord,
                                        @ApiParam(required = true, value = "老师userId") @RequestParam Long userId,
                                        @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (feedbackRecord.getType() == null || StringUtil.isEmpty(feedbackRecord.getContent()) ) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        feedbackRecord.setUserId(userId);
        appealService.addFeekbackRecord(userId, feedbackRecord);
        return Response.success();
    }

    @Register
    @RequestMapping(value = {"/getFeedbackByUserId"}, method = {RequestMethod.GET})
//    @SystemControllerLog(description = "查询自己提交的反馈")
    @ApiOperation(value = "查询自己提交的反馈")
    public Response<List<Map<String, Object>>> getFeedbackByUserId(@ApiParam(required = true, value = "token") @RequestParam String token,
                                                   @ApiParam(required = true, value = "老师userId") @RequestParam Long userId,
                                                   @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        List<Map<String, Object>> list = appealService.getFeedbackRecord(userId);
        return Response.success(list);
    }

    @Register
    @RequestMapping(value = {"/getFeedbackList"}, method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询反馈列表")
    @ApiOperation(value = "查询反馈列表")
    public Response<?> getFeedbackList(@ApiParam(required = true, value = "token") @RequestParam String token,
                                       @ApiParam(required = true, value = "老师userId") @RequestParam Long userId,
                                       @ApiParam(required = true, value = "json:titile,content") @RequestBody JSONObject json,
                                       @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        int[] page = MyPage.isValidParams(json);
        Byte operate = (byte)json.getInt("operate");
        Byte platform = (byte)json.getInt("platform");
        Byte userType = (byte)json.getInt("userType");
        Map<String, Object> map = appealService.getFeedbackList(operate, platform, userType, userId, page[0], page[1]);
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/operateFeedback"}, method = {RequestMethod.POST})
    @SystemControllerLog(description = "处理反馈")
    @ApiOperation(value = "处理反馈")
    public Response<?> operateFeedback(@ApiParam(required = true, value = "token") @RequestParam String token,
                                       @ApiParam(required = true, value = "老师userId") @RequestParam Long userId,
                                       @ApiParam(required = true, value = "json:feedbackId operate") @RequestBody JSONObject json,
                                       @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {

        if (!json.containsKey("feedbackId") || !ValidateUtil.isDigits(json.getString("feedbackId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        if (!json.containsKey("operate") || !ValidateUtil.isDigits(json.getString("operate"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }

        Long feedbackId = json.getLong("feedbackId");
        int operate = json.getInt("operate");
        appealService.updateFeedback(userId, feedbackId, operate);
        return Response.success(feedbackId);
    }

    @Visitor
    @RequestMapping(value = {"/getQAList"}, method = {RequestMethod.POST})
    @ApiOperation(value = "根据常见问题标题列表")
    public Response<?> getQAList(
            @ApiParam(required = true, value = "userId") @RequestBody JSONObject json
    ) throws Exception {
        int[] page = MyPage.isValidParams(json);

        String title = json.containsKey("title") ? json.getString("title") : null;
        Map<String, Object> ret = appealService.getQAList(title, page[0], page[1]);
        List<Map<String, Object>> list = (List<Map<String, Object>>)ret.get("data");
        list.forEach(i->i.remove("username"));
        list.forEach(i->i.remove("createTime"));
        return Response.success(ret);
    }

    @Register
    @RequestMapping(value = {"/getQAListForWeb"}, method = {RequestMethod.POST})
    @ApiOperation(value = "根据常见问题标题列表")
    public Response<?> getQAListForWeb(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "老师userId") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {

        Map<String, Object> ret = appealService.getQAList(null, 1, 100);
        List<Map<String, Object>> list = (List<Map<String, Object>>)ret.get("data");
        list.forEach(i->i.put("key", i.get("qaId")));
        return Response.success(ret);
    }

    @Visitor
    @RequestMapping(value = {"/getQAContent"}, method = {RequestMethod.POST})
    @ApiOperation(value = "根据id查询常见问题内容")
    public Response<?> getQAContent(
            @ApiParam(required = true, value = "老师userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "qaId") @RequestBody JSONObject json
    ) throws Exception {
        if (!json.containsKey("qaId") || !ValidateUtil.isDigits(json.getString("qaId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long qaId = json.getLong("qaId");
        return Response.success(appealService.getQAContent(qaId));
    }
}
