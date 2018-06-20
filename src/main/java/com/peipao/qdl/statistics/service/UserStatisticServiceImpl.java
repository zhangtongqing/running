package com.peipao.qdl.statistics.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.activity.model.Activity;
import com.peipao.qdl.activity.model.ActivityTypeEnum;
import com.peipao.qdl.activity.service.ActivityService;
import com.peipao.qdl.running.dao.RunningRecordDao;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.running.model.RunningRecord;
import com.peipao.qdl.runningrule.model.vo.RunningRuleVo;
import com.peipao.qdl.runningrule.service.utils.RuleUtilService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.statistics.dao.UserStatisticDao;
import com.peipao.qdl.statistics.model.UserStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

//import java.text.DecimalFormat;

/**
 * 方法名称：UserStatisticServiceImpl
 * 功能描述：
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/4 17:45
 * 修订记录：
 */
@Service
public class UserStatisticServiceImpl implements UserStatisticService {
    @Autowired
    private UserStatisticDao userStatisticDao;
    @Autowired
    private RunningRecordDao runningRecordDao;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private RuleUtilService ruleUtilService;


    @Override
    public UserStatistic getByUserIdAndSemesterId(Long userId, Long semesterId) throws Exception {
        return userStatisticDao.getByUserIdAndSemesterId(userId, semesterId);
    }

    public void setUserStatisticByActivityWithOutRunning(Long userId, Activity activity) throws Exception {
        if(null != activity.getSchoolId()) {
            Float rewardScore = 0f;//活动得分
//        Float perAttendanceScore = 0f;//每课时得分
//        Integer activityRewardDuration = 0;//活动置换课时
//        Float activityRewardDurationScore = 0f;//置换的课时，换算成分数
//        Float attendanceScore = 0f;//课程得分

            UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
            if(null == userSchool || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
                throw new BusinessException(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
            }
            if (activity.getType() == ActivityTypeEnum.RUNNING.ordinal()) {
                return;
            }
//        ExamItem examItem = examItemService.getEvaluationStardard(userSchool.getSchoolId(), userSchool.getSemesterId());
//        if(null != examItem && null != examItem.getPerAttendanceScore()) {
//            perAttendanceScore = examItem.getPerAttendanceScore();//从规则中查询出每课时得分
//        }

            UserStatistic userStatistic = getUserStatisticByUser(userId, userSchool);
//        if(null != userStatistic.getAttendanceScore()) {
//            attendanceScore = userStatistic.getAttendanceScore();
//        }
            int activityRunningCount =0;
            if(null != userStatistic.getActivityRunningCount()) {
                activityRunningCount = userStatistic.getActivityRunningCount();
            }
            userStatistic.setActivityRunningCount(activityRunningCount + 1);//参与活动的次数+1
//          if(null != activity.getRewardDuration()) {
//            activityRewardDuration = Integer.parseInt(activity.getRewardDuration().toString());//本次活动置换的课时
//            activityRewardDurationScore = activityRewardDuration * perAttendanceScore;//计算出置换的课时可以的多少分
//            attendanceScore = attendanceScore + activityRewardDurationScore;//课程得分再加上置换课时得分
//            userStatistic.setAttendanceDuration(userStatistic.getAttendanceDuration() + Integer.parseInt(activity.getRewardDuration().toString()));//将置换的课时累加到 课程课时中
//            userStatistic.setAttendanceScore(attendanceScore);//课程分数中包含了置换课时换算来的分数
//            if(null != userStatistic.getActivityRewardDuration()) {
//                activityRewardDuration = activityRewardDuration + userStatistic.getActivityRewardDuration();
//            }
//          }
            if(null != activity.getRewardScore()) {
                rewardScore = Float.parseFloat(activity.getRewardScore().toString());//活动得分
                if(null != userStatistic.getActivityRunningScore()) {
                    rewardScore = rewardScore + userStatistic.getActivityRunningScore();
                }
            }
//        userStatistic.setActivityRewardDuration(activityRewardDuration);//参与活动所置换的总课时
            userStatistic.setActivityRunningScore(rewardScore);//参与活动所获得的总分数
            insertOrUpdateStatistic(userStatistic);
        } else {
            return;
        }
    }

    @Override
    public MyPageInfo getStudentScoreInfoList(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception {
//        createdExamItemParams(paramsMap);
        PageHelper.startPage(pageindex, pagesize);
        List<Map<String, Object>> list = userStatisticDao.getStudentScoreInfoList(paramsMap);
        list.forEach(i->i.put("key", i.get("userId")));

        RunningRuleVo runningRule = ruleUtilService.getRunningRuleBySemesterIdAndType(Long.parseLong(paramsMap.get("semesterId").toString()), RunningEnum.MORNINGTRAINING.getValue());
        if(null == runningRule || runningRule.getIsUse() == WebConstants.Boolean.FALSE.ordinal()) {
            list.forEach(i->i.remove("morningTrainCount"));
        }

        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        return new MyPageInfo(p);
    }

    @Override
    public List<Map<String, Object>> getStudentScoreInfoListExport(Map<String, Object> paramsMap, boolean morningTrainFlag) throws Exception {
        List<Map<String, Object>> list = userStatisticDao.getStudentScoreInfoListExport(paramsMap);
        list.forEach(map->calcScore(paramsMap, map, morningTrainFlag));
        return list;
    }

    @Override
    public List<Map<String, Object>> getStudentScoreInfoList(Map<String, Object> paramsMap) throws Exception {
//        createdExamItemParams(paramsMap);
        List<Map<String, Object>> list = userStatisticDao.getStudentScoreInfoList(paramsMap);
        return list;
    }

    @Override
    public Float getMyRankingLength(Long userId) throws Exception {
        return userStatisticDao.getMyRankingLength(userId);
    }

    @Override
    @Transactional
    public void insertOrUpdateStatisticByCourseSign(Map<String, Object> paramsMap) throws Exception {
        long userId = Long.parseLong(paramsMap.get("userId").toString());
        long schoolId = Long.parseLong(paramsMap.get("schoolId").toString());
        long semesterId = Long.parseLong(paramsMap.get("semesterId").toString());
        UserStatistic userStatistic = userStatisticDao.getByUserIdAndSemesterId(userId, semesterId);
        if(null == userStatistic) {
            int attendanceCount = Integer.parseInt(paramsMap.get("attendanceCount").toString());
            if(attendanceCount < 0) {
                attendanceCount = 0;
            }
            userStatistic = new UserStatistic(schoolId, semesterId, userId);
            userStatistic.setAttendanceCount(attendanceCount);
            userStatisticDao.insertUserStatistic(userStatistic);
        } else {
            paramsMap.put("statisticId", userStatistic.getStatisticId());
            userStatisticDao.updateStatisticByCourseSign(paramsMap);
        }
    }

    @Override
    public Float getMyRankingLengthByMorningRun(long userId) throws Exception {
        return userStatisticDao.getMyRankingLengthByMorningRun(userId);
    }

    private void calcScore(Map<String, Object> paramsMap, Map<String, Object> map, boolean morningTrainFlag) {
        if(!morningTrainFlag) {//如果学校关闭了SemesterId学期的晨练功能
            map.remove("morningTrainCount");
        }
        /*-------------------------------------------  页面传入参数 -------------------------------------------------*/
        float morningRunningCount = Float.parseFloat(paramsMap.get("morningRunningCount").toString());
        float morningRunningScore = Float.parseFloat(paramsMap.get("morningRunningScore").toString());
        float freeRunningLength = Float.parseFloat(paramsMap.get("freeRunningLength").toString());
        float freeRunningScore = Float.parseFloat(paramsMap.get("freeRunningScore").toString());
        int morningTrainCount = 0;
        float morningTrainScore = 0f;
        float attendanceCount = 1f;// 1次上课换算为 (attendanceScore) 分
        float attendanceScore = Float.parseFloat(paramsMap.get("attendanceScore").toString());// 上课得分

        if(morningTrainFlag) {//如果开启了晨练目标,则页面需传入以下2个参数
            morningTrainCount = Integer.parseInt(paramsMap.get("morningTrainCount").toString());
            morningTrainScore = Float.parseFloat(paramsMap.get("morningTrainScore").toString());
        }

        /*-----------------------------------------------------------------------------------------------------------*/
        float perMorningRunning = morningRunningScore / morningRunningCount;//每一次晨跑获得的单位的分
        float perFreeRunning = freeRunningScore / freeRunningLength;//每一次自由跑获得的单位的分
        float perMorningTrain = 0f;
        float perAttendance = attendanceScore;//暂定为1次课程为基本单位，无需计算该值
        if(morningTrainFlag) {
            perMorningTrain = morningTrainScore / morningTrainCount;//每一次晨练获得的单位的分
        }

        /*-----------------------------------------------------------------------------------------------------------*/
        float morningRunningCountAll = Float.parseFloat(map.get("morningRunningCountAll").toString());//晨跑总次数
        float freeRunningLengthAll = Float.parseFloat(map.get("freeRunningLengthAll").toString());//自由跑总次数
        float morningTrainCountAll = 0f;
        if(morningTrainFlag) {//如果学校关闭了SemesterId学期的晨练功能
            morningTrainCountAll = Float.parseFloat(map.get("morningTrainCount").toString());//晨练总次数
        }
        float attendanceCountAll = Float.parseFloat(map.get("attendanceCount").toString());//上课总次数
        /*-----------------------------------------------------------------------------------------------------------*/
        float fMorningRunningScore = perMorningRunning * morningRunningCountAll;//晨跑--计算得分
        float fFreeRuningScore = perFreeRunning * freeRunningLengthAll;//自由跑--计算得分
        float fAttendanceScore = perAttendance * attendanceCountAll;//上课--计算得分
        float fMorningTrainScore = 0f;
        if(morningTrainFlag) {
            fMorningTrainScore = perMorningTrain * morningTrainCountAll;//晨练--计算得分
        }
        /*-----------------------------------------------------------------------------------------------------------*/
        map.put("morningRunningScore", fMorningRunningScore);//有效晨跑总得分
        map.put("freeRunningScore", fFreeRuningScore);//自由跑总得分
        map.put("attendanceScore", fAttendanceScore);//上课总得分
        if(morningTrainFlag) {
            map.put("morningTrainScore", fMorningTrainScore);//晨练总得分
        }
        /*-----------------------------------------------------------------------------------------------------------*/
        float schoolActivityScoreAll = Float.parseFloat(map.get("schoolActivityScoreAll").toString());
        //晨跑得分+自由跑得分+课程考勤得分+活动得分，保留1位小数，向上取十分位为0或者5,例如综合成绩70.32分导出为70.5分70.58分导出为71分
        float score = fMorningRunningScore + fFreeRuningScore + fAttendanceScore + schoolActivityScoreAll;
        if(morningTrainFlag) {//如果开启了晨练目标
            score = score + fMorningTrainScore;
        }

        int iScore = (int)score;//整数部分
        float fS = score%1;//小数部分
        if(fS > 0.5f) {
            score = iScore + 1.0f;
        } else if(fS > 0f && fS < 0.5f) {
            score = iScore + 0.5f;
        }
        map.put("score", score);//综合得分
    }

    @Override
    @Transactional
    public void insertOrUpdateRecordAndStatistic(
            Activity activity, UserStatistic userStatistic, RunningRecord runningRecord, String userStatisticFlag, String runningRecordFlag) throws Exception {

    	if(runningRecordFlag.equals("INSERT")) {
            runningRecordDao.insertRunningRecord(runningRecord);
            if(userStatisticFlag.equals("INSERT")) {
                userStatisticDao.insertUserStatistic(userStatistic);
            } else if(userStatisticFlag.equals("UPDATE")) {
                userStatisticDao.updateUserStatistic(userStatistic);
            }
            //meteor.wu
            // 跑步类型活动，完成后需要更新活动表中的信息
            // runningRecord.getid

            //如果已经达到活动可参与次数上限，仅记录跑步记录，不更新活动参与信息了
            //if(activity.getEnrollCount() && activity.getEnrollCount() > 0f )
            //if(null != runningRecord.getValidKilometeorCount() && runningRecord.getValidKilometeorCount() > 0f)
            if(null != activity && null != activity.getEnrollCount() && activity.getEnrollCount() > 0f)
            {
                if(null != activity) {
                    activityService.updateActivityMemberAfterRunning(
                            activity.getActivityId(),
                            runningRecord.getUserId(),
                            runningRecord.getDuration().intValue(),
                            runningRecord.getCalorieCount().intValue(),
                            runningRecord.getKilometeorCount(),
                            runningRecord.getIsEffective(),
                            runningRecord.getRunningRecordId()
                    );
                }
            }
        } else if(runningRecordFlag.equals("UPDATE")) {
            runningRecordDao.updateRunningRecordById(runningRecord);
        }

    }

    @Override
    @Transactional
    public void insertOrUpdateStatistic(UserStatistic userStatistic) throws Exception {
        if(null == userStatistic.getStatisticId()) {
            userStatisticDao.insertUserStatistic(userStatistic);
        }else {
            userStatisticDao.updateUserStatistic(userStatistic);
        }
    }


    private UserStatistic getUserStatisticByUser(Long userId, UserSchool userSchool) throws Exception {
        UserStatistic userStatistic = userStatisticDao.getByUserIdAndSemesterId(userId, userSchool.getSemesterId());
        if(null == userStatistic){
            userStatistic = new UserStatistic(userSchool.getSchoolId(), userSchool.getSemesterId(), userId);
        }
        return userStatistic;
    }



//    @Override
//    public List getRankingInCourse(Long schoolId, Long semesterId, Long courseId) throws Exception {
//        return userStatisticDao.getRankingInCourse(schoolId, semesterId, courseId);
//    }

//    @Override
//    public List getRankingInSchool(Long schoolId) throws Exception {
//        return userStatisticDao.getRankingInSchool(schoolId);
//    }

//    @Override
//    public List getRankingInCountry() throws Exception {
//        return userStatisticDao.getRankingInCountry();
//    }

}
