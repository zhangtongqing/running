package com.peipao.qdl.luckdraw.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.MyPageConstants;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.luckdraw.model.ActivityPrize;
import com.peipao.qdl.luckdraw.model.LuckDrawRule;
import com.peipao.qdl.luckdraw.service.ActivityLuckRecordService;
import com.peipao.qdl.luckdraw.service.ActivityPrizeService;
import com.peipao.qdl.luckdraw.service.LuckDrawRuleService;
import com.peipao.qdl.luckdraw.service.LuckDrawUtilService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * web前端抽奖接口
 *
 * @author Meteor.wu
 * @since 2017/12/19 11:23
 */
@Api(value = "/luckdraw/pc", description = "活动跑步抽奖模块-pc接口")
@RestController
@RequestMapping({"/luckdraw/pc"})
public class LuckDrawControllerPC {

    @Autowired
    private ActivityPrizeService activityPrizeService;

    @Autowired
    private LuckDrawRuleService luckDrawRuleService;

    @Autowired
    private ActivityLuckRecordService activityLuckRecordService;

    @Autowired
    private LuckDrawUtilService luckDrawUtilService;

    @Register
    @RequestMapping(value = {"/addLuckDrawRule"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "添加抽奖规则")
    @ApiOperation(value = "添加抽奖规则")
    public Response<LuckDrawRule> addLuckDrawRule(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "activityId,shareImg,userDayLimit,colligateRate必填") @RequestBody LuckDrawRule luckDrawRule) throws Exception {
        if (luckDrawRule.getActivityId() == null || luckDrawRule.getShareImg() == null || luckDrawRule.getUserDayLimit() == null
                || luckDrawRule.getColligateRate() == null || luckDrawRule.getIsUse() == null) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }

        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("activityId", luckDrawRule.getActivityId());
        paramsMap.put("logicDelete", WebConstants.Boolean.FALSE.ordinal());
        LuckDrawRule ldr = luckDrawRuleService.getLuckDrawRuleByActivityId(paramsMap);
        if(null != ldr) {
            return Response.fail(ResultMsg.LUCK_DRAW_RULE_EXIST);
        }
        luckDrawRule.setCreateTime(Calendar.getInstance().getTime());
        luckDrawRule.setCreateUserId(userId);
        luckDrawRule.setLogicDelete(WebConstants.Boolean.FALSE.ordinal());
        luckDrawRuleService.addLuckDrawRule(userId, luckDrawRule);
        return Response.success(luckDrawRule);
    }

    @Register
    @RequestMapping(value = {"/getLuckDrawById"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "根据id查询抽奖详情")
    @ApiOperation(value = "根据id查询抽奖详情")
    public Response<JSONObject> getLuckDrawById(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "ruleId(必填)") @RequestBody LuckDrawRule luckDrawRule) throws Exception {
        if (luckDrawRule.getRuleId() == null ) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        luckDrawRule = luckDrawRuleService.getLuckDrawById(luckDrawRule.getRuleId());
        return Response.success(object2Json(luckDrawRule));
    }

    private JSONObject object2Json(LuckDrawRule luckDrawRule) {
        JSONObject json = new JSONObject();
        json.put("ruleId", luckDrawRule != null ? luckDrawRule.getRuleId() : "");
        json.put("activityId", luckDrawRule != null ? luckDrawRule.getActivityId() : "");
        json.put("shareImg", luckDrawRule != null ? luckDrawRule.getShareImg() : "");
        json.put("userDayLimit", luckDrawRule != null ? luckDrawRule.getUserDayLimit() : "");
        json.put("colligateRate", luckDrawRule != null ? luckDrawRule.getColligateRate() : "");
        json.put("luckType", luckDrawRule != null ? luckDrawRule.getLuckType() : 0);
        json.put("luckLimit", luckDrawRule == null ? null : luckDrawRule.getLuckLimit());
        json.put("isUse", luckDrawRule != null ? luckDrawRule.getIsUse() : "");
        return json;
    }

    @Register
    @RequestMapping(value = {"/getLuckDrawByActivityId"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "根据id查询抽奖详情")
    @ApiOperation(value = "根据id查询抽奖详情")
    public Response<JSONObject> getLuckDrawByActivityId(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "ruleId(必填)") @RequestBody LuckDrawRule luckDrawRule) throws Exception {
        if (luckDrawRule.getActivityId() == null ) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("activityId", luckDrawRule.getActivityId());
        paramsMap.put("logicDelete", WebConstants.Boolean.FALSE.ordinal());
        luckDrawRule = luckDrawRuleService.getLuckDrawRuleByActivityId(paramsMap);//规则：单日同一用户中奖次数上限
        return Response.success(object2Json(luckDrawRule));
    }

    @Register
    @RequestMapping(value = {"/updateLuckDrawRule"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "更新抽奖规则信息")
    @ApiOperation(value = "更新抽奖规则信息")
    public Response<JSONObject> updateLuckDrawRule(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "ruleId(必填)") @RequestBody LuckDrawRule luckDrawRule) throws Exception {
        if (luckDrawRule.getRuleId() == null || luckDrawRule.getActivityId() == null || luckDrawRule.getShareImg() == null || luckDrawRule.getUserDayLimit() == null
                || luckDrawRule.getColligateRate() == null || luckDrawRule.getIsUse() == null) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        LuckDrawRule ldr = luckDrawRuleService.getLuckDrawById(luckDrawRule.getRuleId());
        if(null == ldr) {
            return Response.fail(ResultMsg.LUCKRULE_NOT_EXIST);//活动抽奖规则不存在，请检查
        }
        if(!luckDrawRule.getActivityId().equals(ldr.getActivityId())) {
            return Response.fail(ResultMsg.ACTIVITY_CHANGE_ERROR);//抽奖规则中活动ID改变，请检查
        }
        luckDrawRule.setLogicDelete(WebConstants.Boolean.TRUE.ordinal());//不能修改逻辑删除字段
        luckDrawRule.setUpdateUserId(userId);
        luckDrawRule.setUpdateTime(Calendar.getInstance().getTime());
        luckDrawRuleService.updateLuckDrawRule(userId, luckDrawRule);
        return Response.success(object2Json(luckDrawRule));
    }

    @Register
    @RequestMapping(value = {"/getActivityPrizeList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询奖品列表")
    @ApiOperation(value = "查询奖品列表")
    public Response<Map<String, Object>> getActivityPrizeList(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "activityId(必填)") @RequestBody ActivityPrize activityPrize) throws Exception {
        if (activityPrize.getActivityId() == null ) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("activityId", activityPrize.getActivityId());
        paramsMap.put("logicDelete", WebConstants.Boolean.FALSE.ordinal());
        List<ActivityPrize> activityPrizeList = activityPrizeService.getActivityPrizeByActivityId(paramsMap);
        activityPrizeList.stream().forEach(i -> i.setKey(i.getPrizeId()));
        return Response.success(MyPage.processPage(Long.parseLong(String.valueOf(activityPrizeList.size())), MyPageConstants.PAGE_SIZE_APP, 1, activityPrizeList));

    }

    @Register
    @RequestMapping(value = {"/addActivityPrize"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "添加奖品")
    @ApiOperation(value = "添加奖品")
    public Response<ActivityPrize> addActivityPrize(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "") @RequestBody ActivityPrize activityPrize) throws Exception {
        if (activityPrize.getActivityId() == null || activityPrize.getPrizeTitle() == null || activityPrize.getPrizeTotal() == null || activityPrize.getPrizeWeight() == null
                || activityPrize.getUserLimit() == null || activityPrize.getStartTime() == null || activityPrize.getEndTime() == null ) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        if(null == activityPrize.getPrizeSendTotal()) {
            activityPrize.setPrizeSendTotal(0);
        }
        activityPrize.setCreateUserId(userId);
        activityPrize.setCreateTime(Calendar.getInstance().getTime());
        activityPrizeService.addActivityPrize(activityPrize);
        activityPrize.setKey(activityPrize.getPrizeId());
        return Response.success(activityPrize);
    }

    @Register
    @RequestMapping(value = {"/getActivityPrizeById"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询奖品列表")
    @ApiOperation(value = "查询奖品列表")
    public Response<ActivityPrize> getActivityPrizeById(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "prizeId(必填)") @RequestBody ActivityPrize activityPrize) throws Exception {
        if (activityPrize.getPrizeId() == null ) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }

        return Response.success(activityPrizeService.getActivityPrizeById(activityPrize.getPrizeId()));
    }

    @Register
    @RequestMapping(value = {"/updateActivityPrize"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "编辑奖品")
    @ApiOperation(value = "编辑奖品")
    public Response<ActivityPrize> updateActivityPrize(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "") @RequestBody ActivityPrize activityPrize) throws Exception {
        if (activityPrize.getPrizeId() == null || activityPrize.getPrizeTitle() == null || activityPrize.getPrizeTotal() == null || activityPrize.getPrizeWeight() == null
                || activityPrize.getUserLimit() == null || activityPrize.getStartTime() == null || activityPrize.getEndTime() == null ) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        ActivityPrize ap = activityPrizeService.getActivityPrizeById(activityPrize.getPrizeId());
        if(null == ap) {
            return Response.fail(ResultMsg.ACTIVITY_PRIZE_NOT_FOUND);//活动奖品不存在，不能修改编辑
        }
        if(!ap.getActivityId().equals(activityPrize.getActivityId())) {
            return Response.fail(ResultMsg.ACTIVITY_CHANGE_ERROR);//抽奖规则中活动ID改变，请检查
        }
        int sendTotal = ap.getPrizeSendTotal();
        int currTotal = activityPrize.getPrizeTotal();
        if(currTotal < sendTotal) {
            return Response.fail(ResultMsg.PRIZE_TOTAL_ERROR);//活动奖品总数量不能小于已派奖数量
        }
        //查出来的比页面传过来的大，则以查出来的为准
        activityPrize.setPrizeSendTotal(sendTotal);//覆盖页面传过来的
        activityPrize.setLogicDelete(WebConstants.Boolean.FALSE.ordinal());//不能修改逻辑删除字段
        activityPrize.setUpdateUserId(userId);
        activityPrize.setUpdateTime(Calendar.getInstance().getTime());
        activityPrizeService.updateActivityPrize(activityPrize);
        //开始准备返回值
        activityPrize.setKey(activityPrize.getPrizeId());
        activityPrize.setPrizeSendTotal(sendTotal);
        return Response.success(activityPrize);
    }

    @Register
    @RequestMapping(value = {"/deleteById"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "删除奖品")
    @ApiOperation(value = "删除奖品")
    public Response<?> deleteById(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "") @RequestBody JSONObject json) throws Exception {
        String[] params = new String[]{"prizeId"};//必填项
        if (!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        long prizeId = json.getLong("prizeId");
        ActivityPrize ap = activityPrizeService.getActivityPrizeById(prizeId);
        if(null == ap) {
            return Response.fail(ResultMsg.ACTIVITY_PRIZE_NOT_FOUND);//活动奖品不存在，不能修改编辑
        }
        if(ap.getPrizeSendTotal() > 0) {
            return Response.fail(ResultMsg.PRIZE_IS_NOT_LONELY);//活动奖品已有中奖者，不能删除
        }
        activityPrizeService.logicDeleteActivityPrizeByPrizeId(prizeId, userId);
        return Response.success(prizeId);
    }

    @Register
    @RequestMapping(value = {"/getLuckRecordForPC"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询中奖记录")
    @ApiOperation(value = "查询中奖记录")
    public Response<Map<String, Object>> getLuckRecordByActivityId(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "") @RequestBody JSONObject json) throws Exception {
        String[] params = new String[]{"activityId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        int[] pageParams = MyPage.checkPageParams(json);//检查并封装分页参数
        json.put("isLuck", WebConstants.Boolean.TRUE.ordinal());
        return Response.success(activityLuckRecordService.getLuckRecordForPC(json, pageParams));
    }

    @Register
    @RequestMapping(value = {"/luckRecordExprot"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "导出中奖记录")
    @ApiOperation(value = "导出中奖记录")
    public Response<JSONObject> luckRecordExprot(
            HttpServletResponse response, HttpServletRequest request,
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "") @RequestBody JSONObject json) throws Exception {
        String[] params = new String[]{"activityId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        json.put("isLuck", WebConstants.Boolean.TRUE.ordinal());
        Map<String, Object> map = activityLuckRecordService.getLuckRecordForPC(json, null);
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("data");
        if (list != null && list.size() > 0) {
            String path = luckDrawUtilService.createluckRecordExcel(response, request, list, map.get("name").toString());
            json.put("path", path);
            json.put("size", list.size());
        } else {
            json.put("size", 0);
        }

        return Response.success(json);
    }


}
