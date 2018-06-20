package com.peipao.qdl.appeal.service;

import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.event.EventExceptionService;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.exception.EventException;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.activity.dao.ActivityMemberDao;
import com.peipao.qdl.activity.model.Activity;
import com.peipao.qdl.activity.model.ActivityMember;
import com.peipao.qdl.activity.service.ActivityCacheService;
import com.peipao.qdl.appeal.model.AppealStatusEnum;
import com.peipao.qdl.running.model.Running;
import com.peipao.qdl.running.model.RunningEffectiveEnum;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.running.model.RunningRecord;
import com.peipao.qdl.running.service.RunningCacheService;
import com.peipao.qdl.running.service.RunningRecordService;
import com.peipao.qdl.running.service.RunningService;
import com.peipao.qdl.runningrule.model.vo.RunningRuleVo;
import com.peipao.qdl.runningrule.service.utils.RuleUtilService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 方法名称：申诉自动审核处理服务
 * 功能描述：AppealReceiverService
 * 作者：Liu Fan
 * 版本：2.0.11
 * 创建日期：2018/1/29 11:18
 * 修订记录：
 */
@Service
public class AppealReceiverService implements Consumer<Event<JSONObject>> {
    protected static final Logger LOG = LoggerFactory.getLogger(AppealReceiverService.class);
    @Autowired
    private RunningService runningService;
    @Autowired
    private RuleUtilService ruleUtilService;
    @Autowired
    private RunningRecordService runningRecordService;
    @Autowired
    private ActivityCacheService activityCacheService;
    @Autowired
    private ActivityMemberDao activityMemberDao;
    @Autowired
    private RunningCacheService runningCacheService;
    @Autowired
    private EventExceptionService eventExceptionService;

    private static final  float DEFAULT_QUOTIETY =  0.8f ;//默认里程对比系数
    @Override
    public void accept(Event<JSONObject> ev) {
        if(null != ev.getData()) {
            JSONObject json = ev.getData();
            String[] params = new String[]{"runningRecordId", "semesterId"};//必填项检查
            if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
                throw new BusinessException(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
            }
            String runningRecordId = json.getString("runningRecordId");
            Long semesterId = json.getLong("semesterId");
            RunningRecord runningRecord;
            try {
                runningRecord = runningService.getRunningRecordById(runningRecordId);
            } catch (Exception e) {
                LOG.debug(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") +
                        " 查询跑步记录失败(申诉)  参数:json=" + json.toString(), e);
                e.printStackTrace();
                eventExceptionService.eventExceptionHandler(new EventException(ResultMsg.RUNNING_RECORD_QUERY_FAIL, ev.getKey().toString(), json.toString()), e);
                return;
            }
            if(null != runningRecord) {
                int runningType = runningRecord.getType();
                if(runningRecord.getActivityId() > 0L) {
                    runningType = RunningEnum.ACTIVITYRUN.getValue();//活动跑
                }
                boolean invalidFlag = false;
        /*---------------------------------------- 开始做筛选，进行自动审核 -----------------------------------------*/
                float ruleLength = 0f;//当前跑步规定了最小需要达到的里程
                int myRunningCount = 0;//用户当前类型的跑步次数
                int sportCountMax = 99999;//当前跑步规定了最多跑多少次
                RunningRuleVo runningRule;
                if(runningType != RunningEnum.ACTIVITYRUN.getValue()) {
                    //****** 非活动 ******//
                    try {
                        runningRule = ruleUtilService.getRunningRuleBySemesterIdAndType(semesterId, runningRecord.getType());
                    } catch (Exception e) {
                        LOG.debug(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") +
                                " 查询学期运动规则失败(申诉)  参数:json=" + json.toString(), e);
                        e.printStackTrace();
                        eventExceptionService.eventExceptionHandler(new EventException(ResultMsg.RUNNING_RULE_QUERY_FAIL, ev.getKey().toString(), json.toString()), e);
                        return;
                    }
                    if(null != runningRule) {
                        ruleLength = runningRule.getValidKiometerMin();//最小有效里程要求
                        sportCountMax = runningRule.getSportCountMax();
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
                            try {
                                myRunningCount = runningRecordService.getRecordCountToday(paramsMap);
                            } catch (Exception e) {
                                LOG.debug(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") +
                                        " 查询用户日晨跑次数失败(申诉)  参数:json=" + json.toString(), e);
                                e.printStackTrace();
                                eventExceptionService.eventExceptionHandler(new EventException(ResultMsg.RUNNING_COUNT_QUERY_FAIL, ev.getKey().toString(), json.toString()), e);
                                return;
                            }
                        }
                    }
                } else {
                    //****** 活动 ******//
                    Running running;
                    Activity activity;
                    try {
                        activity = activityCacheService.getActivityById(runningRecord.getActivityId());
                    } catch (Exception e) {
                        LOG.debug(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") +
                                " 查询活动信息失败(申诉)   参数:json=" + json.toString(), e);
                        e.printStackTrace();
                        eventExceptionService.eventExceptionHandler(new EventException(ResultMsg.RUNNING_ACTIVITY_QUERY_FAIL, ev.getKey().toString(), json.toString()), e);
                        return;
                    }

                    try {
                        running = runningCacheService.getByActivityId(activity.getActivityId());
                    } catch (Exception e) {
                        LOG.debug(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") +
                                " 查询活动跑步规则信息失败(申诉)   参数:json=" + json.toString(), e);
                        e.printStackTrace();
                        eventExceptionService.eventExceptionHandler(new EventException(ResultMsg.ACTIVITY_RULE_QUERY_FAIL, ev.getKey().toString(), json.toString()), e);
                        return;
                    }
                    if(null != running &&  null != running.getLength()) {
                        ruleLength = running.getLength();
                    }
                    sportCountMax = activity.getEffectiveSignCount();//活动最多可以参与的次数
                    ActivityMember activityMember;
                    try {
                        activityMember = activityMemberDao.queryActivityMemberById(activity.getActivityId(), runningRecord.getUserId());
                    } catch (Exception e) {
                        LOG.debug(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") +
                                " 查询用户活动参与信息失败(申诉)   参数:json=" + json.toString(), e);
                        e.printStackTrace();
                        eventExceptionService.eventExceptionHandler(new EventException(ResultMsg.ACTIVITY_MEMBER_QUERY_FAIL, ev.getKey().toString(), json.toString()), e);
                        return;
                    }
                    if(null != activityMember && null != activityMember.getSucCount()) {
                        myRunningCount = activityMember.getSucCount();
                        if (sportCountMax > 0 && myRunningCount >= sportCountMax) {//如果已经达到活动可参与次数上限
                            runningRecord.setStatus(AppealStatusEnum.INVALID.getValue());//状态改为申诉成功,不更新其他数据
                            invalidFlag = true;
                        }
                    }
                    //TODO 之前的 活动 EffectiveSignCount=0代表 无限次，现在修改为9999代表无限次，需要排查老代码，将0的判断修正
                }

                if(!invalidFlag) {
                    //1.无效原因为"里程不足"时，如果纪录里程未达到 最低里程要求的 80%，则系统自动判断为 “申诉驳回”，该条申诉纪录正常显示在申诉列表中；
                    if(runningRecord.getInvalidReason().contains(RunningEffectiveEnum.UnderMinDistance.getCode() + "")) {
                        if(runningRecord.getKilometeorCount() < ruleLength * DEFAULT_QUOTIETY) {
                            //未达到 最低里程要求的 80%,自动驳回
                            runningRecord.setStatus(AppealStatusEnum.INVALID.getValue());//状态改为驳回
                            invalidFlag = true;
                        }
                    }
                }

                if(!invalidFlag) {
                    //2.如果用户当天晨跑有效次数已达上限(显示申诉成功)：补偿有效晨跑次数 不增加， 晨跑有效里程 不增加
                    //如果用户该活动有效次数已达上限：不补偿该活动单次应得学分，活动有效跑步次数不增加，活动有效里程不增加
                    if(myRunningCount >= sportCountMax) {
                        runningRecord.setStatus(AppealStatusEnum.INVALID.getValue());//状态改为申诉成功,不更新其他数据
                        invalidFlag = true;
                    }
                }
                if(invalidFlag) {
                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put("runningRecordId", runningRecord.getRunningRecordId());
                    paramsMap.put("isEffective", runningRecord.getIsEffective());//
                    paramsMap.put("status", runningRecord.getStatus());
                    paramsMap.put("appealUserId", WebConstants.qdl_robot_id);//官方机器人ID
                    //paramsMap.put("appealTime", new Date());//审核时间
                    try {
                        runningRecordService.updateEffectiveStatusOnly(paramsMap);//更新数据
                    } catch (Exception e) {
                        LOG.debug(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") +
                                " 更新跑步记录状态失败(申诉)   参数:json=" + json.toString(), e);
                        e.printStackTrace();
                        eventExceptionService.eventExceptionHandler(new EventException(ResultMsg.ACTIVITY_MEMBER_UPDATE_FAIL, ev.getKey().toString(), json.toString()), e);
                    }
                }
            }
        }
    }

}
