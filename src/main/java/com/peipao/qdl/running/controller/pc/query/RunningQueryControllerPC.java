package com.peipao.qdl.running.controller.pc.query;

import com.peipao.framework.annotation.Register;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.JsonUtils;
import com.peipao.framework.util.StringUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.activity.dao.ActivityMemberDao;
import com.peipao.qdl.activity.model.Activity;
import com.peipao.qdl.activity.model.ActivityMember;
import com.peipao.qdl.activity.model.ActivityTypeEnum;
import com.peipao.qdl.activity.service.ActivityCacheService;
import com.peipao.qdl.appeal.model.AppealStatusEnum;
import com.peipao.qdl.compensate.service.CompensateService;
import com.peipao.qdl.running.model.*;
import com.peipao.qdl.running.service.*;
import com.peipao.qdl.runningrule.model.vo.RunningRuleVo;
import com.peipao.qdl.runningrule.service.utils.RuleCacheService;
import com.peipao.qdl.runningrule.service.utils.RuleUtilService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.statistics.service.UserStatisticUtilService;
import com.peipao.qdl.user.model.User;
import com.peipao.qdl.user.service.UserCacheService;
import io.swagger.annotations.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：RunningQueryControllerPC
 * 功能描述：跑步记录管理
 * 作者：Liu Fan
 * 版本：
 * 创建日期：2018/1/25 15:46
 * 修订记录：
 */

@Api(value = "/running/pc",description = "跑步记录管理")
@RestController
@RequestMapping({"/running/pc"})
public class RunningQueryControllerPC {
    @Autowired
    private FilePathService filePathService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private RunningRecordService runningRecordService;
    @Autowired
    private RunningService runningService;
    @Autowired
    private CompensateService compensateService;
    @Autowired
    private UserStatisticUtilService userStatisticUtilService;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private RuleUtilService ruleUtilService;
    @Autowired
    private RuleCacheService ruleCacheService;
    @Autowired
    private ActivityCacheService activityService;
    @Autowired
    private RunningCacheService runningCacheService;
    @Autowired
    private ActivityMemberDao activityMemberDao;
    @Autowired
    private RunningLineCacheService runningLineCacheService;

    @Register
    @RequestMapping(value = {"/getRunningNodeFileById"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "根据跑步ID查询跑步节点文件路径-app")
    @ApiOperation(value = "根据跑步ID查询跑步节点文件路径(点击显示轨迹)--app--新增接口")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
            @ApiResponse(code = 10004,message = "参数错误"),
            @ApiResponse(code = 9001,message = "其他异常")})
    public Response<?> getRunningNodeFileById(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "runningRecordId:跑步记录Id") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"runningRecordId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);//必填项请求参数不完整，请检查
        }
        String runningRecordId = json.getString("runningRecordId");
        //以文件形式存储的node节点数据, 需要查询文件服务器，返回文件路径集合
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }

        RunningRecord runningRecord = runningService.getRunningRecordById(runningRecordId);
//        if(null != runningRecord && !runningRecord.getSemesterId().equals(userSchool.getSemesterId())) {
//            return Response.fail(ReturnStatus.NODE_USER_ERROR);//用户与运动记录不匹配
//        }

        User user = userCacheService.getUserById(runningRecord.getUserId());
        if(null == user) {
            return Response.fail(ReturnStatus.USER_NOT_EXIST);//用户不存在
        }

        String basePath = WebConstants.fileServerPath;
        basePath = basePath.replace("schoolId", userSchool.getSchoolId().toString());
        basePath = basePath.replace("userId", user.getUserId().toString());
        basePath = basePath.replace("runningRecordId", runningRecordId);

        String returnBody = null;
        //JSONArray jsonArray = new JSONArray();
        String filePath = "";
        //JSONObject resJson = new JSONObject();
        returnBody = filePathService.getPathFromFileServer(runningRecordId);
        JSONObject fileJson = null;
        if(StringUtils.isNotBlank(returnBody)) {
            String yyyymmdd = null;
            fileJson = JSONObject.fromObject(returnBody);
            //以下判断文件如果存在
            if(null != fileJson.get("date") && StringUtils.isNotBlank(fileJson.getString("date"))) {
                yyyymmdd = fileJson.getString("date");
                JSONArray tempArray = fileJson.getJSONArray("files");
                basePath = basePath.replace("yyyymmdd", yyyymmdd);
                basePath = filePathService.getFileServerUrl() + basePath;
                for (Object fileName : tempArray) {
                    String paramString = "?token={token}&userId={userId}&schoolId={schoolId}&runningRecordId={runningRecordId}&t=pc&v=&sign=";
                    paramString = paramString.replace("{token}", user.getToken());
                    paramString = paramString.replace("{userId}", user.getUserId().toString());
                    paramString = paramString.replace("{schoolId}", userSchool.getSchoolId().toString());
                    paramString = paramString.replace("{runningRecordId}", runningRecordId);
                    filePath = basePath + fileName.toString() + paramString;
                }
            }
        }
        if(StringUtil.isNotEmpty(filePath)) {
            return Response.success(filePathService.getNodeJsonString(filePath));
        } else {
            return Response.fail(ResultMsg.NODE_FILE_PATH_EMTPY);//节点文件寻址失败
        }
    }

    @Register
    @RequestMapping(value = {"/getStudentAppealList"}, method = {RequestMethod.POST})
    //@SystemControllerLog(description = "查询学生跑步记录申诉列表")
    @ApiOperation(value = "查询学生跑步记录申诉列表")
    public Response<?> getStudentAppealList(
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
        json.put("semesterId", userSchool.getSemesterId());
        if(!ValidateUtil.jsonValidateWithKey(json, "status")) {
            json.put("all", "all");//默认查询全部走了申诉流程的数据
            json.put("status", AppealStatusEnum.NORMAL.getValue());//排除 status = AppealStatusEnum.NORMAL 的数据，就是走了申诉流程的所有
        }
        userStatisticUtilService.createCourseArrayString(json, userId, userSchool.getSemesterId());
        ValidateUtil.createQueryTypeParams(json);
        Map<String, Object> paramsMap = JsonUtils.getMap4Json(json.toString());
        paramsMap.put("activityRunType", RunningEnum.ACTIVITYRUN.getValue());//活动跑
        MyPageInfo pageInfo = runningRecordService.getStudentAppealList(paramsMap, page[0], page[1]);
        return Response.success(pageInfo);
    }


    @Register
    @RequestMapping(value = {"/getRunningRecordAppealInfo"},method = {RequestMethod.POST})
//    @SystemControllerLog(description="查询跑步记录申诉详情")
    @ApiOperation(value = "查询跑步记录申诉详情", notes = "getRunningRule")
    public Response<Map> getRunningRecordAppealInfo(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "用户Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json: runningRecordId[String必填]") @RequestBody JSONObject json
    ) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        Long schoolId = userSchool.getSchoolId();
        Long semesterId = userSchool.getSemesterId();

        RunningRecord runningRecord = runningService.getRunningRecordById(json.getString("runningRecordId"));
        if(null == runningRecord) {
            return Response.fail(ReturnStatus.RUNNING_RECORD_NOT_EXIST);//跑步记录不存在
        }
        JSONObject runningRuleJson = new JSONObject();
        float speedMin = 0f;//配速要求
        float speedMax = 0f;//配速要求
        float validKiometerMin = 0f;//里程要求
        float validKiometerMax = 0f;//里程要求
        String runningTime = "无";
        long runningId = 0L;
        boolean hasRail = false;//是否有围栏
        int runningType = runningRecord.getType();
        if(runningRecord.getActivityId() > 0L) {
            runningType = RunningEnum.ACTIVITYRUN.getValue();
        }
        int myRunningCount = 0;//用户当前类型的跑步次数
        int sportCountMax = 99999;//当前跑步规定了最多跑多少次

        if(runningType != RunningEnum.ACTIVITYRUN.getValue()) {//如果是一个非活动跑步
            RunningEnum runningEnum = RunningEnum.valueOf(runningRecord.getType());
            if(null == runningEnum) {
                return Response.fail(ResultMsg.RUNNING_TYPE_ERROR);//跑步类型错误
            }
            runningRuleJson.put("runningType", runningEnum.getChinesename());
            RunningRuleVo runningRule = ruleUtilService.getRunningRuleBySemesterIdAndType(userSchool.getSemesterId(), runningRecord.getType());
            if(null != runningRule) {
                sportCountMax = runningRule.getSportCountMax();
                speedMin = runningRule.getSpeedMin();
                speedMax = runningRule.getSpeedMax();
                validKiometerMin = runningRule.getValidKiometerMin();
                validKiometerMax = runningRule.getValidKiometerMax();
                if(StringUtil.isNotEmpty(runningRule.getStartTime()) && StringUtil.isNotEmpty(runningRule.getEndTime())) {
                    runningTime = runningRule.getStartTime() + " - " + runningRule.getEndTime();
                }
                if(runningRule.getHasRail() == WebConstants.Boolean.TRUE.ordinal()) {
                    hasRail = true;
                }
                if(runningRecord.getType() == RunningEnum.MORNINGRUNNING.getValue()) {//如果跑步类型==晨跑
                    //如果是晨跑，则按照跑步规则，每天只能完成规定次数的晨跑
                    //晨跑按照开始时间 查询跑步记录，与结束时间无关
                    String startDateMin = DateUtil.dateToStr(runningRecord.getStartDate(), "YYYY-MM-dd").concat(" 00:00:00");
                    String startDateMax = DateUtil.dateToStr(runningRecord.getStartDate(), "YYYY-MM-dd").concat(" 23:59:59");
                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put("userId", runningRecord.getUserId());
                    paramsMap.put("type", RunningEnum.MORNINGRUNNING.getValue());
                    paramsMap.put("isEffective", RunningEffectiveEnum.Success.getCode());
                    paramsMap.put("startDateMin", startDateMin);
                    paramsMap.put("startDateMax", startDateMax);
                    myRunningCount = runningRecordService.getRecordCountToday(paramsMap);
                }
            }
        } else {
            validKiometerMax = 99999f;//活动跑里程上限无要求，默认99999当作无限
            Activity activity = activityService.getActivityById(runningRecord.getActivityId());
            if(null == activity) {
                throw new BusinessException(ReturnStatus.ACTIVITY_NOT_EXIST);//活动不存在
            }
            if(activity.getType() == ActivityTypeEnum.NON_RUNNING.ordinal()) {
                throw new BusinessException(ResultMsg.ACTIVITY_IS_NOT_RUNNING);//这是一个非跑步活动
            }
            sportCountMax = activity.getEffectiveSignCount();//活动最多可以参与的次数
            ActivityMember activityMember = activityMemberDao.queryActivityMemberById(activity.getActivityId(), runningRecord.getUserId());
            if(null != activityMember && null != activityMember.getSucCount()) {
                myRunningCount = activityMember.getSucCount();
            }
            Running running = runningCacheService.getByActivityId(activity.getActivityId());
            if(null == running) {
                throw new BusinessException(ResultMsg.ACTIVITY_RUNNING_RULE_NULL);//跑步活动规则不存在，请检查
            }
            runningId = running.getRunningId();
            if(running.getPassNode()) {
                hasRail = true;//如果活动存在跑步路线节点
            }
            runningRuleJson.put("runningType", activity.getName());//活动名称

            speedMin = running.getSpeedMin();
            speedMax = running.getSpeedMax();
            validKiometerMin = running.getLength();
            runningTime = DateUtil.dateToStr(activity.getStartTime(), "yyyy-MM-dd HH:mm:ss") + " - " +
                            DateUtil.dateToStr(activity.getEndTime(), "yyyy-MM-dd HH:mm:ss");
        }
        if (sportCountMax > 0 && myRunningCount >= sportCountMax) {//如果已经达到活动可参与次数上限
            runningRuleJson.put("sportCountFlag", WebConstants.Boolean.TRUE.ordinal());
        } else {
            runningRuleJson.put("sportCountFlag", WebConstants.Boolean.FALSE.ordinal());
        }
        runningRuleJson.put("speedMin", speedMin);
        runningRuleJson.put("speedMax", speedMax);
        runningRuleJson.put("validKiometerMin", validKiometerMin);
        runningRuleJson.put("validKiometerMax", validKiometerMax);
        runningRuleJson.put("runningTime", runningTime);

        JSONObject runningRecordJson = new JSONObject();
        String runningRecordTime = "无";
        if(null != runningRecord.getStartDate() && null != runningRecord.getEndDate()) {
            runningRecordTime = DateUtil.dateToStr(runningRecord.getStartDate(), "yyyy-MM-dd HH:mm:ss");
        }
        runningRecordJson.put("activityId", runningRecord.getActivityId());//活动ID
        runningRecordJson.put("runningTime", runningRecordTime);//跑步日期时间
        runningRecordJson.put("equallyPace", runningRecord.getEquallyPace());//平均配速
        runningRecordJson.put("kilometeorCount", runningRecord.getKilometeorCount());//跑步里程
        runningRecordJson.put("effective", runningRecord.getIsEffective());//是否有效，有效=88
        runningRecordJson.put("invalidReason", runningRecord.getInvalidReason());//无效原因
        runningRecordJson.put("imgUrl", runningRecord.getThumbUrl());//跑步轨迹图片地址
        runningRecordJson.put("duration", runningRecord.getDurationStr());//跑步时长
        runningRecordJson.put("status", runningRecord.getStatus());//跑步记录状态
        runningRecordJson.put("startTime", DateUtil.dateToStr(runningRecord.getStartDate(), "HH:mm:ss"));//跑步开始时间

        JSONObject resJson = new JSONObject();
        resJson.put("runningRuleInfo", runningRuleJson);//跑步规则信息
        resJson.put("runningRecordInfo", runningRecordJson);//跑步规则信息
        resJson.put("compensateInfo", new JSONObject());
        if(runningRecord.getStatus() == AppealStatusEnum.EFFECTIVE.getValue()) {//被设为有效的记录，可能存在补偿信息
            HashMap<String, Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("userId", runningRecord.getUserId());
            paramsMap.put("runningRecordId", runningRecord.getRunningRecordId());
            paramsMap.put("semesterId", semesterId);
            String updateUserName = "";
            Map<String, Object> compensateMap = compensateService.getCompensateInfoForStudent(paramsMap);
            if(!CollectionUtils.isEmpty(compensateMap)) {
                Long updateUserId = Long.parseLong(compensateMap.get("updateUserId").toString());
                User updateUser = userCacheService.getUserById(updateUserId);//修改操作人--老师
                if(null != updateUser) {
                    updateUserName = updateUser.getUsername();
                }
                compensateMap.remove("updateUserId");
                compensateMap.put("updateUser", updateUserName);
                String updateTime = DateUtil.dateToStr((Date)compensateMap.get("updateTime"), "yyyy-MM-dd HH:mm:ss");
                compensateMap.remove("updateTime");
                compensateMap.put("updateTime", updateTime);
                resJson.put("compensateInfo", compensateMap);
            }
        }
        resJson.put("runningRuleNodeList", new JSONArray());
        if(runningType != RunningEnum.ACTIVITYRUN.getValue()) {
            if(hasRail) {
                List<Map<String,Object>> runningRuleNodeList = ruleCacheService.getRailNodeListByRailName(userSchool.getSemesterId());
                resJson.put("runningRuleNodeList", runningRuleNodeList);
            }
        } else {
            if(hasRail) {
                List<RunningLine> runningLineList = runningLineCacheService.getRunningLineByRunningId(runningId);
                resJson.put("runningRuleNodeList", runningLineList);
            }
        }
        return Response.success(resJson);
    }
}
