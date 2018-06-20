package com.peipao.qdl.statistics.service.utils;

import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.qdl.activity.dao.ActivityMemberDao;
import com.peipao.qdl.activity.model.Activity;
import com.peipao.qdl.activity.model.ActivityMember;
import com.peipao.qdl.running.model.RunningEffectiveEnum;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.running.model.RunningRecord;
import com.peipao.qdl.statistics.model.UserStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 方法名称：RunningStatisticUtil
 * 功能描述：RunningStatisticUtil
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/1/15 18:17
 * 修订记录：
 */
@Service
public class RunningStatisticUtil {

    @Autowired
    private ActivityMemberDao activityMemberDao;

    public void setUserStatisticByRunningRecord(UserStatistic userStatistic, RunningRecord runningRecord, Activity activity) throws Exception {

        int runningCount = 1;//跑步次数
        Float runningLength = 0f;//有效里程
        Float runningRealLength = 0f;//实际里程
        Float runningScore = 0f;//得分

        if(null != runningRecord.getValidKilometeorCount()) {
            runningLength = runningRecord.getValidKilometeorCount();//有效里程
        }
        if(null != runningRecord.getKilometeorCount()) {
            runningRealLength = runningRecord.getKilometeorCount();//实际里程
        }
        if(runningLength == 0f) {
            runningCount = 0;
        }
        Boolean flag = false;
        Boolean notActivity = false;
        if(runningRecord.getIsEffective() == RunningEffectiveEnum.Success.getCode()) {
            flag = true;
        }
        if(null == runningRecord.getActivityId() || runningRecord.getActivityId() == 0L ) {
            notActivity = true;
        }
        if(notActivity) {
            int type = runningRecord.getType();
            if(type == RunningEnum.MORNINGRUNNING.getValue()) {//晨跑
                if(null != userStatistic.getMorningRunningCount()) {
                    //有效里程 >0 的记录才需要增加一次运动次数
                    runningCount = runningCount + userStatistic.getMorningRunningCount();
                }
                if(null != userStatistic.getMorningRunningLength()) {
                    runningLength = runningLength + userStatistic.getMorningRunningLength();
                }
                if(null != userStatistic.getMorningRunningRealLength()) {
                    runningRealLength = runningRealLength + userStatistic.getMorningRunningRealLength();
                }
                if(flag){
                    userStatistic.setMorningRunningCount(runningCount);
                    userStatistic.setMorningRunningLength(runningLength);
                }
                userStatistic.setMorningRunningRealLength(runningRealLength);
            } else if (type == RunningEnum.FREERUNNING.getValue()) {//自由跑
                if(null != userStatistic.getFreeRunningCount()) {
                    runningCount = runningCount + userStatistic.getFreeRunningCount();
                }
                if(null != userStatistic.getFreeRunningLength()) {
                    runningLength = runningLength + userStatistic.getFreeRunningLength();
                }
                if(null != userStatistic.getFreeRunningRealLength()) {
                    runningRealLength = runningRealLength + userStatistic.getFreeRunningRealLength();
                }
                if(flag){
                    userStatistic.setFreeRunningCount(runningCount);
                    userStatistic.setFreeRunningLength(runningLength);
                }
                userStatistic.setFreeRunningRealLength(runningRealLength);
            }
        } else {
            //如果是活动跑步
            Long userId = runningRecord.getUserId();
            Long activityId = runningRecord.getActivityId();
            ActivityMember activityMember = activityMemberDao.queryActivityMemberById(activityId, userId);
            if(null == activityMember || null == activity) {
                throw new BusinessException(ReturnStatus.ACTIVITY_NOT_EXIST);//活动不存在
            }
            runningScore = runningRecord.getRunningScore();//活动得分在前面已经设置好了
            //如果是活动跑步，那么给跑步记录更新本次跑步得分
            int sucCount = activityMember.getSucCount();//当前活动已经成功次数    //该列有默认值0
            int signCount = activity.getEffectiveSignCount();//总共能参与几次    //signCount = 0 表示活动可以无限次参加并获得成绩
            if(signCount > 0 && sucCount >= signCount) {
                flag = false;//如果已经达到该活动可以后的奖励的次数限制，则后续跑步记录只记录真实运动里程
            }

            if(null != userStatistic.getActivityRunningCount()) {
                runningCount = runningCount + userStatistic.getActivityRunningCount();
            }
            if(null != userStatistic.getActivityRunningLength()) {
                runningLength = runningLength + userStatistic.getActivityRunningLength();
            }
            if(null != userStatistic.getActivityRunningRealLength()) {
                runningRealLength = runningRealLength + userStatistic.getActivityRunningRealLength();
            }
            if(null != userStatistic.getActivityRunningScore()) {
                runningScore = runningScore + userStatistic.getActivityRunningScore();//活动跑步得分
            }

            //按照新需求，只有校方活动才累计活动有效里程和活动分数
            if(null == activity.getSchoolId()) {
                flag = false;
            }
            if(flag){//只有在本次跑步有效,并且是官方活动时 才更新统计表以下几个字段
                userStatistic.setActivityRunningCount(runningCount);
                userStatistic.setActivityRunningLength(runningLength);
                userStatistic.setActivityRunningScore(runningScore);
            }
            userStatistic.setActivityRunningRealLength(runningRealLength);
        }
    }
}
