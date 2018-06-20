package com.peipao.qdl.luckdraw.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.LuckTypeEnum;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.activity.model.Activity;
import com.peipao.qdl.activity.service.ActivityCacheService;
import com.peipao.qdl.luckdraw.model.LuckDrawRule;
import com.peipao.qdl.luckdraw.service.ActivityLuckRecordService;
import com.peipao.qdl.luckdraw.service.ActivityPrizeService;
import com.peipao.qdl.luckdraw.service.LuckDrawRuleService;
import com.peipao.qdl.luckdraw.service.LuckDrawUtilService;
import com.peipao.qdl.running.model.RunningEffectiveEnum;
import com.peipao.qdl.running.model.RunningRecord;
import com.peipao.qdl.running.service.RunningService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 方法名称：LuckDrawControllerAPP
 * 功能描述：手机端抽奖模块
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/12/15 16:15
 * 修订记录：
 */

@Api(value = "/luckdraw/app", description = "活动跑步抽奖模块-app接口")
@RestController
@RequestMapping({"/luckdraw/app"})
public class LuckDrawControllerAPP {
    private static final Logger log = LoggerFactory.getLogger(LuckDrawControllerAPP.class);
    @Autowired
    private RunningService runningService;
    @Autowired
    private ActivityCacheService activityCacheService;
    @Autowired
    private ActivityLuckRecordService activityLuckRecordService;
    @Autowired
    private LuckDrawUtilService luckDrawUtilService;
    @Autowired
    private LuckDrawRuleService luckDrawRuleService;
    @Autowired
    private ActivityPrizeService activityPrizeService;

    @Register
    @RequestMapping(value = {"/luckRun"}, method = {RequestMethod.POST})
    @SystemControllerLog(description = "活动跑步抽奖")
    @ApiOperation(value = "活动跑步抽奖")
    public Response<JSONObject> luckRun(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "runningRecordId:跑步记录主表id(必填)") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"runningRecordId"};//必填项
        if (!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        String runningRecordId = json.getString("runningRecordId");
        RunningRecord runningRecord = runningService.getRunningRecordById(runningRecordId);
        if (null == runningRecord || !runningRecord.getUserId().equals(userId)) {
            return Response.fail(ReturnStatus.RUNNING_RECORD_NOT_EXIST);//跑步记录不存在
        }
        if(runningRecord.getIsEffective() != RunningEffectiveEnum.Success.getCode()) {
            return Response.fail(ReturnStatus.RUNNING_IS_NOT_EFFECTIVE);//运动记录没有达标
        }

        Long activityId = runningRecord.getActivityId();
        if (null == activityId || activityId.equals(0L)) {
            return Response.fail(ReturnStatus.ACTIVITY_NOT_EXIST);//活动不存在
        }

        Activity activity = activityCacheService.getActivityById(runningRecord.getActivityId());
        if (null == activity) {
            return Response.fail(ReturnStatus.ACTIVITY_NOT_EXIST);//活动不存在
        }
        if (activity.getHasLuckDraw() == WebConstants.Boolean.FALSE.ordinal() || null != activity.getSchoolId()) {
            return Response.fail(ResultMsg.ACTIVITY_NO_LUCK_DRAW);//本活动没有抽奖环节
        }

        JSONObject resJson = luckDrawUtilService.cheakAndLuckDraw(userId, activity, runningRecordId);
        return Response.success(resJson);
    }


    /**
     * 获取抽奖类型 0=默认=普通抽奖 ，1=轮盘抽奖
     * @param userId
     * @param token
     * @param sign
     * @param json
     * @return
     * @throws Exception
     */
    @Register
    @RequestMapping(value = {"/genLuckType"}, method = {RequestMethod.POST})
    @SystemControllerLog(description = "获取抽奖类型")
    @ApiOperation(value = "获取抽奖类型")
    public Response<JSONObject> genLuckType(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "runningRecordId:跑步记录主表id(必填)") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"runningRecordId"};//必填项
        if (!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        String runningRecordId = json.getString("runningRecordId");
        RunningRecord runningRecord = runningService.getRunningRecordById(runningRecordId);
        if (null == runningRecord || !runningRecord.getUserId().equals(userId)) {
            return Response.fail(ReturnStatus.RUNNING_RECORD_NOT_EXIST);//跑步记录不存在
        }
        if(runningRecord.getIsEffective() != RunningEffectiveEnum.Success.getCode()) {
            return Response.fail(ReturnStatus.RUNNING_IS_NOT_EFFECTIVE);//运动记录没有达标
        }

        Long activityId = runningRecord.getActivityId();
        if (null == activityId || activityId.equals(0L)) {
            return Response.fail(ReturnStatus.ACTIVITY_NOT_EXIST);//活动不存在
        }

        Activity activity = activityCacheService.getActivityById(runningRecord.getActivityId());
        if (null == activity) {
            return Response.fail(ReturnStatus.ACTIVITY_NOT_EXIST);//活动不存在
        }
        if (activity.getHasLuckDraw() == WebConstants.Boolean.FALSE.ordinal() || null != activity.getSchoolId()) {
            return Response.fail(ResultMsg.ACTIVITY_NO_LUCK_DRAW);//本活动没有抽奖环节
        }
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("activityId", activityId);
        paramsMap.put("logicDelete", WebConstants.Boolean.FALSE.ordinal());
        LuckDrawRule luckDrawRule = luckDrawRuleService.getLuckDrawRuleByActivityId(paramsMap);//规则：单日同一用户中奖次数上限
        if (null == luckDrawRule) {
            return Response.fail(ResultMsg.LUCKRULE_NOT_EXIST);//活动抽奖规则不存在
        }

        JSONObject resJson = new JSONObject();
        resJson.put("luckType",luckDrawRule.getLuckType()); //获取抽奖类型
        if(LuckTypeEnum.roulette.getCode().equals(luckDrawRule.getLuckType())){
            resJson.put("luckName", LuckTypeEnum.roulette.getDesc());
        }else{
            resJson.put("luckName", LuckTypeEnum._defaul.getDesc());
        }
        return Response.success(resJson);
    }


    /**
     * 轮盘抽奖
     * @param userId
     * @param token
     * @param sign
     * @param json
     * @return
     * @throws Exception
     */
    @Register
    @RequestMapping(value = {"/rouletteLuck"}, method = {RequestMethod.POST})
    @SystemControllerLog(description = "轮盘抽奖")
    @ApiOperation(value = "轮盘抽奖")
    public Response<?> rouletteLuck(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "runningRecordId:跑步记录主表id(必填)") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"runningRecordId"};//必填项
        if (!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        String runningRecordId = json.getString("runningRecordId");
        RunningRecord runningRecord = runningService.getRunningRecordById(runningRecordId);
        if (null == runningRecord || !runningRecord.getUserId().equals(userId)) {
            return Response.fail(ReturnStatus.RUNNING_RECORD_NOT_EXIST);//跑步记录不存在
        }
        if(runningRecord.getIsEffective() != RunningEffectiveEnum.Success.getCode()) {
            return Response.fail(ReturnStatus.RUNNING_IS_NOT_EFFECTIVE);//运动记录没有达标
        }

        Long activityId = runningRecord.getActivityId();
        if (null == activityId || activityId.equals(0L)) {
            return Response.fail(ReturnStatus.ACTIVITY_NOT_EXIST);//活动不存在
        }
        Activity activity = activityCacheService.getActivityById(runningRecord.getActivityId());
        log.info("轮盘活动名 = {},活动Id = {}", activity.getName(), activity.getActivityId());
        if (null == activity) {
            return Response.fail(ReturnStatus.ACTIVITY_NOT_EXIST);//活动不存在
        }

        if (activity.getHasLuckDraw() == WebConstants.Boolean.FALSE.ordinal() || null != activity.getSchoolId()) {
            return Response.fail(ResultMsg.ACTIVITY_NO_LUCK_DRAW);//本活动没有抽奖环节
        }
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("activityId", activityId);
        paramsMap.put("logicDelete", WebConstants.Boolean.FALSE.ordinal());
        LuckDrawRule luckDrawRule = luckDrawRuleService.getLuckDrawRuleByActivityId(paramsMap);//规则：单日同一用户中奖次数上限
        if (null == luckDrawRule) {
            return Response.fail(ResultMsg.LUCKRULE_NOT_EXIST);//活动抽奖规则不存在
        }

        //轮盘抽奖
        Map<String,Object> resJson = luckDrawUtilService.rouletteLuck(userId, activity, runningRecordId);
        return Response.success(resJson);
    }



    @Register
    @RequestMapping(value = {"/myLuckRecord"}, method = {RequestMethod.GET})
    //@SystemControllerLog(description = "查询我的中奖纪录")
    @ApiOperation(value = "查询我的中奖纪录")
    public Response<?> myLuckRecord(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign
    ) throws Exception {
        JSONArray jsonArray = new JSONArray();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("userId", userId);
        paramsMap.put("isLuck", WebConstants.Boolean.TRUE.ordinal());
        List<Map<String, Object>> activityIds = activityLuckRecordService.getActivityIdsByUserId(paramsMap);
        List<Map<String,Object>> acitivityLuckRecord = new ArrayList<>();
        if(!CollectionUtils.isEmpty(activityIds)) {
            for(Map<String, Object> idMap : activityIds){
                Long activityId = Long.parseLong(idMap.get("activityId").toString());
                String activityName = idMap.get("name").toString();
                paramsMap.put("activityId", activityId);
                //条件查询

                List<Map<String, Object>> recordList = activityLuckRecordService.myLuckRecord(paramsMap);
                if(!CollectionUtils.isEmpty(recordList)) {
                    for(Map<String,Object> data : recordList){
                        Map<String,Object> record = new HashMap<>();
                        record.put("prizeTitle", data.get("prizeTitle")) ;
                        record.put("prizeImg",data.get("prizeImg"));
                        record.put("createTime", data.get("createTime"));
                        record.put("prizeCode", data.get("prizeCode"));
                        record.put("runningRecordId", data.get("runningRecordId"));
                        record.put("activitName",activityName);
                        acitivityLuckRecord.add(record);
                    }
                }
            }
        }


        //活动奖品排序，时间倒序
        Collections.sort(acitivityLuckRecord,new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                int ret = 0;
                //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                ret = o2.get("createTime").toString().compareTo(o1.get("createTime").toString());//逆序的话就用o2.compareTo(o1)即可
                return ret;
            }
        });
        Map data = new HashMap<>();
        data.put("size",acitivityLuckRecord != null ? acitivityLuckRecord.size() : 0);
        data.put("prizeList",acitivityLuckRecord);
        return Response.success(data);
    }


    /**
     * 分享查询
     * @param userId
     * @param token
     * @param sign
     * @return
     * @throws Exception
     */
    @Register
    @RequestMapping(value = {"/luckRecord"}, method = {RequestMethod.POST})
    //@SystemControllerLog(description = "查询我的中奖纪录")
    @ApiOperation(value = "查询我的中奖纪录")
    public Response<?> luckRecord(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "runningRecordId:跑步记录主表id(必填)") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"runningRecordId"};//必填项
        if (!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        String runningRecordId = json.getString("runningRecordId");
        RunningRecord runningRecord = runningService.getRunningRecordById(runningRecordId);
        if (null == runningRecord || !runningRecord.getUserId().equals(userId)) {
            return Response.fail(ReturnStatus.RUNNING_RECORD_NOT_EXIST);//跑步记录不存在
        }
        if(runningRecord.getIsEffective() != RunningEffectiveEnum.Success.getCode()) {
            return Response.fail(ReturnStatus.RUNNING_IS_NOT_EFFECTIVE);//运动记录没有达标
        }

        Long activityId = runningRecord.getActivityId();
        if (null == activityId || activityId.equals(0L)) {
            return Response.fail(ReturnStatus.ACTIVITY_NOT_EXIST);//活动不存在
        }
        Activity activity = activityCacheService.getActivityById(runningRecord.getActivityId());
        log.info("轮盘活动名 = {},活动Id = {}", activity.getName(), activity.getActivityId());
        if (null == activity) {
            return Response.fail(ReturnStatus.ACTIVITY_NOT_EXIST);//活动不存在
        }

        if (activity.getHasLuckDraw() == WebConstants.Boolean.FALSE.ordinal() || null != activity.getSchoolId()) {
            return Response.fail(ResultMsg.ACTIVITY_NO_LUCK_DRAW);//本活动没有抽奖环节
        }

        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("userId", userId);
        paramsMap.put("isLuck", WebConstants.Boolean.TRUE.ordinal());
        paramsMap.put("activityId",activity.getActivityId());
        paramsMap.put("runningRecordId",runningRecordId);
        List<Map<String, Object>> recordList = activityLuckRecordService.myLuckRecord(paramsMap);
        Map<String, Object> res = null;
        if(null != recordList){
            res = recordList.get(0);
        }
        return Response.success(res);
    }


    @Register
    @RequestMapping(value = {"/activityLuckRecord"}, method = {RequestMethod.POST})
    //@SystemControllerLog(description = "查询活动中奖纪录")
    @ApiOperation(value = "查询活动中奖纪录")
    public Response<JSONObject> activityLuckRecord(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "activityId:活动id(必填)") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"activityId"};//必填项
        if (!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long activityId = json.getLong("activityId");
        Activity activity = activityCacheService.getActivityById(activityId);
        if (null == activity) {
            return Response.fail(ReturnStatus.ACTIVITY_NOT_EXIST);//活动不存在
        }
        JSONObject resJson = new JSONObject();
        resJson.put("activityName", activity.getName());
        JSONArray jsonArray = new JSONArray();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("activityId", activityId);
        paramsMap.put("isLuck", WebConstants.Boolean.TRUE.ordinal());
        List<Map<String, Object>> prizeIds = activityLuckRecordService.getPrizeIdsByActivityId(paramsMap);
        if(!CollectionUtils.isEmpty(prizeIds)) {
            for(Map<String, Object> idMap : prizeIds) {
                Long prizeId = Long.parseLong(idMap.get("prizeId").toString());
                String prizeTitle = idMap.get("prizeTitle").toString();
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("prizeTitle", prizeTitle);
                paramsMap.put("prizeId", prizeId);
                List<Map<String, Object>> recordList = activityLuckRecordService.activityLuckRecord(paramsMap);
                if(!CollectionUtils.isEmpty(recordList)) {
                    jsonObj.put("recordList", recordList);
                } else {
                    jsonObj.remove("prizeTitle", prizeTitle);
                }
                if(jsonObj.containsKey("prizeTitle")) {
                    jsonArray.add(jsonObj);
                }
            }
        }
        resJson.put("list", jsonArray);
        return Response.success(resJson);
    }

}
