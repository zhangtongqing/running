package com.peipao.qdl.running.controller.app.edit;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.StringUtil;
import com.peipao.qdl.activity.dao.ActivityMemberDao;
import com.peipao.qdl.activity.model.Activity;
import com.peipao.qdl.activity.model.ActivityMember;
import com.peipao.qdl.activity.service.ActivityCacheService;
import com.peipao.qdl.running.model.NodeStatusEnum;
import com.peipao.qdl.running.model.RunningEffectiveEnum;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.running.model.RunningRecord;
import com.peipao.qdl.running.service.RunningRecordService;
import com.peipao.qdl.running.service.RunningService;
import com.peipao.qdl.running.service.utils.RunningUtilService;
import com.peipao.qdl.runningrule.model.vo.RunningRuleVo;
import com.peipao.qdl.runningrule.service.utils.RuleUtilService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.school.service.UserLevelService;
import com.peipao.qdl.statistics.model.UserStatistic;
import com.peipao.qdl.statistics.service.UserStatisticService;
import com.peipao.qdl.statistics.service.utils.RunningStatisticUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
public class RunningEditControllerAPP {
    @Autowired
    private RunningService runningService;
    @Autowired
    private UserStatisticService userStatisticService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private RunningRecordService runningRecordService;
    @Autowired
    private ActivityCacheService activityCacheService;
    @Autowired
    private UserLevelService userLevelService;
    @Autowired
    private ActivityMemberDao activityMemberDao;
    @Autowired
    private RunningUtilService runningUtilService;
    @Autowired
    private RunningStatisticUtil runningStatisticUtil;
    @Autowired
    private RuleUtilService ruleUtilService;

//    protected static final Logger LOG = LoggerFactory.getLogger(RunningControllerAPP.class);

    @Register
    @RequestMapping(value = {"/upload"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "上传跑步成绩-app")
    @ApiOperation(value = "upload", notes = "上传跑步数据")
    public Response<?> upload(
        @ApiParam(required = true, value = "token") @RequestParam String token,
        @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
        @ApiParam(required = true, value = "签名") @RequestParam String sign,
        @ApiParam(required = true, value = "跑步记录数据") @RequestBody RunningRecord runningRecord
    ) throws Exception {
        Activity activity = null;
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        String runningRecordFlag = null;
        RunningRecord rr = runningService.getRunningRecordById(runningRecord.getRunningRecordId());
        if (null == rr) {
            runningRecordFlag = "INSERT";
            runningRecord.setCreateTime(new Date());
            runningRecord.setSemesterId(userSchool.getSemesterId());
            runningRecord.setUserId(userId);
            //如果以文件形式上传跑步节点数据,则初始化上传状态为上传中
            runningRecord.setNodeStatus(NodeStatusEnum.UPLOADING.getCode());
        } else {
            return Response.success();
        }
        if(null == runningRecord.getStartDate()) {
            return Response.fail(ReturnStatus.RUNNING_START_TIME_EMPTY);//运动开始时间异常
        }

        //上传时间CreateTime  必须大于  运动开始时间runningRecord.getStartDate
        Date runningStartDate = runningRecord.getStartDate();
        //如果大于的话返回的是正整数，等于是0，小于的话就是负整数
        int c = runningStartDate.compareTo(runningRecord.getCreateTime());
        if(c > 0) {
            return Response.fail(ReturnStatus.RUNNING_START_TIME_ERROR);//运动开始时间异常
        }
        if(runningRecord.getIsEffective() != RunningEffectiveEnum.Success.getCode() && StringUtil.isNotEmpty(runningRecord.getInvalidReason())) {
            String invalidReason = runningRecord.getInvalidReason();
            if(invalidReason.contains("，")) {//处理中文逗号
                invalidReason = invalidReason.replaceAll("\\，", ",");
            }

            if(invalidReason.lastIndexOf(",") == invalidReason.length()-1) {//处理最末尾逗号
                invalidReason = invalidReason.substring(0, invalidReason.length()-1);
                runningRecord.setInvalidReason(invalidReason);
            }
        }
        if(runningRecord.getIsEffective() != RunningEffectiveEnum.Success.getCode() && StringUtil.isEmpty(runningRecord.getInvalidReason())) {
            runningRecord.setInvalidReason(runningRecord.getIsEffective() + "");//如果运动记录无效，而没有无效原因的话，初始化填充无效原因
        }

        if(runningRecord.getIsEffective() == RunningEffectiveEnum.Success.getCode()) {
            runningRecord.setInvalidReason(null);//有效数据，不应该有无效原因
        }

        //如果当前跑步不是活动跑步
        if (null == runningRecord.getActivityId() || runningRecord.getActivityId() == 0L) {
            runningRecord.setActivityId(0L);
            boolean isMaxCount = false;
            if(runningRecord.getType() == RunningEnum.MORNINGRUNNING.getValue()) {//如果跑步类型==晨跑
                Map<String, Object> paramsMap = new HashMap<String, Object>();
                paramsMap.put("semesterId", userSchool.getSemesterId());
                paramsMap.put("type", RunningEnum.MORNINGRUNNING.getValue());
                int sportCountMax = ruleUtilService.getSportCountMaxByRunningType(userSchool.getSemesterId(), RunningEnum.MORNINGRUNNING.getValue());
                //如果是晨跑，则按照跑步规则，每天只能完成规定次数的晨跑
                //晨跑按照开始时间 查询跑步记录，与结束时间无关
                String startDateMin = DateUtil.dateToStr(runningRecord.getStartDate(), "YYYY-MM-dd").concat(" 00:00:00");
                String startDateMax = DateUtil.dateToStr(runningRecord.getStartDate(), "YYYY-MM-dd").concat(" 23:59:59");
                paramsMap = new HashMap<String, Object>();
                paramsMap.put("userId", userId);
                paramsMap.put("type", RunningEnum.MORNINGRUNNING.getValue());
                paramsMap.put("isEffective", RunningEffectiveEnum.Success.getCode());
                paramsMap.put("startDateMin", startDateMin);
                paramsMap.put("startDateMax", startDateMax);
                int morningRunNum = runningRecordService.getRecordCountToday(paramsMap);
                if(morningRunNum >= sportCountMax) {
                    //2.0.11需求要求：达到运动规则上限后，仍然可以上传记录，但是不增加里程和次数
                    isMaxCount = true;//表示已经达到了上限
                    //return Response.fail(ReturnStatus.MORNINGRUNNING_COUNT_ERROR);//今日该类型运动已达次数上限，不能再完成
                }
            }

//            1.根据跑步类型查询出跑步规则
//            2.跑步记录有效，更新统计表
//            3.跑步无效，更新统计表，但仅更新学生跑步真实里程
//            Map<String, Object> paramsMap = new HashMap<>();
//            paramsMap.put("semesterId", userSchool.getSemesterId());
//            paramsMap.put("type", RunningEnum.MORNINGRUNNING.getValue());
//            List<RunningRuleVo> runningRuleList = ruleService.getRunningRuleBySchool(paramsMap);//加载得分规则

            RunningRuleVo runningRule = ruleUtilService.getRunningRuleBySemesterIdAndType(userSchool.getSemesterId(), runningRecord.getType());
            if (null == runningRule) {
                return Response.fail(ReturnStatus.RUNNING_RULE_EMPTY);//没有跑步规则，无法开始跑步
            }

            //*****************************************开始更新学生跑步成绩等信息*************************************//
            runningUtilService.setRunningRecordByRule(runningRule, runningRecord, isMaxCount);
        } else {
            /********* 如果是跑步类型的活动 ***************************************************************************/
            //1.根据活动ID 查询出活动信息(活动会给跑步奖励分数)
            //2.按照活动积分或置换课时的规则进行 跑步成绩或课时的数据更新
            //3.更新统计表活动相关字段数据
            //3.1    更新数据时，如果跑步记录无效，也要更新统计表，记录学生跑步里程等字段信息
            //3.2    更新数据时，如果跑步记录有效，......
            //新需求，如果是官方的活动，仅记录真实跑步里程，有效里程不更新
            //新需求，如果是官方的活动，不得分

            runningRecord.setType((byte)RunningEnum.ACTIVITYRUN.getValue());//活动跑，没有跑步类型，type=0
            activity = activityCacheService.getActivityById(runningRecord.getActivityId());
            if (null == activity) {
                return Response.fail(ReturnStatus.ACTIVITY_NOT_EXIST);//活动不存在
            }
            Float rewardScore = 0f;
            boolean isSchoolActivity = false;
            if(null != activity.getSchoolId()) {
                isSchoolActivity = true;
            }
            //如果是官方活动，不计分
            if (null != activity.getRewardScore() && isSchoolActivity) {
                rewardScore = activity.getRewardScore();
            }
            ActivityMember activityMember = activityMemberDao.queryActivityMemberById(activity.getActivityId(), userId);

            /******************************************开始更新学生跑步成绩等信息**************************************/
            if (runningRecord.getIsEffective() == RunningEffectiveEnum.Success.getCode()) {
                if(isSchoolActivity) {//如果是学校活动，才记录有效里程和活动奖励得分
                    runningRecord.setValidKilometeorCount(runningRecord.getKilometeorCount());//如果跑步记录有效，活动类的有效跑步里程和真是里程相同
                    runningRecord.setRunningScore(Float.parseFloat(rewardScore.toString()));//如果活动获得学分
                } else {
                    runningRecord.setValidKilometeorCount(0f);//官方活动不记录有效里程
                    runningRecord.setRunningScore(0f);//官方活动不获得学分
                }
                if(null != activityMember && null != activityMember.getSucCount()) {
                    int sucCount = activity.getEffectiveSignCount();//活动可参与次数上限
                    int myCount = activityMember.getSucCount();
                    if (myCount >= sucCount) {//如果已经达到活动可参与次数上限，则只记录真是跑步里程，不记录有效性及奖励
                        runningRecord.setRunningScore(0f);//如果活动获得学分
                        runningRecord.setValidKilometeorCount(0f);//活动次数超限后，有效里程不记录，只记录真实里程
                    }
                }
            } else {
                runningRecord.setRunningScore(0f);//无效记录0分
            }
        }

        /******************************************开始更新学生综合统计信息*****************************************/
        UserStatistic userStatistic = userStatisticService.getByUserIdAndSemesterId(userId, userSchool.getSemesterId());
        String userStatisticFlag = null;
        if (null == userStatistic) {
            userStatisticFlag = "INSERT";
            userStatistic = new UserStatistic(userSchool.getSchoolId(), userSchool.getSemesterId(), userId);
        } else {
            userStatisticFlag = "UPDATE";
        }
        runningStatisticUtil.setUserStatisticByRunningRecord(userStatistic, runningRecord, activity);//isSetEffective=false(此处不是:设为有效操作)
        /**********************************开始保存跑步记录，保存学生统计信息***************************************/
        userStatisticService.insertOrUpdateRecordAndStatistic(activity, userStatistic, runningRecord, userStatisticFlag, runningRecordFlag);


        /**********************************跑步记录更新后，更新学生运动等级信息***************************************/
        userLevelService.updateUserLevelAsync(userId, userSchool.getSemesterId());
        return Response.success();
    }

}

