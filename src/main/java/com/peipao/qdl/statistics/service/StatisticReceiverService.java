package com.peipao.qdl.statistics.service;

import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.event.EventExceptionService;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.exception.EventException;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.statistics.model.UserStatistic;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.Date;

/**
 * 方法名称：StatisticReceiverService
 * 功能描述：StatisticReceiverService
 * 作者：Liu Fan
 * 版本：2.0.11
 * 创建日期：2018/1/29 18:15
 * 修订记录：
 */

@Service
public class StatisticReceiverService implements Consumer<Event<JSONObject>> {
    @Autowired
    private UserStatisticService userStatisticService;
    @Autowired
    private EventExceptionService eventExceptionService;

    protected static final Logger LOG = LoggerFactory.getLogger(StatisticReceiverService.class);

    @Override
    public void accept(Event<JSONObject> ev) {
        if(null != ev.getData()) {
            JSONObject json = ev.getData();
            int runningCount = 1;//默认增加一次运动次数
            int runningType = json.getInt("runningType");
            long userId = json.getLong("userId");
            float runningLength = Float.parseFloat(json.getString("runningLength"));
            float runningScore = Float.parseFloat(json.getString("runningScore"));
            long schoolId = json.getLong("schoolId");
            long semesterId = json.getLong("semesterId");
            UserStatistic userStatistic = null;
            try {
                //String aaa = json.getString("aaaaaaaaaaaaa");
                userStatistic = userStatisticService.getByUserIdAndSemesterId(userId, semesterId);
                if(null == userStatistic){
                    userStatistic = new UserStatistic(schoolId, semesterId, userId);
                }
                if(RunningEnum.FREERUNNING.getValue() == runningType) {//--自由跑
                    userStatistic.setCompensateFreeRunningCount(userStatistic.getCompensateFreeRunningCount() + runningCount);
                    userStatistic.setCompensateFreeRunningLength(userStatistic.getCompensateFreeRunningLength() + runningLength);
                } else if(RunningEnum.MORNINGRUNNING.getValue() == runningType) {//--晨跑
                    userStatistic.setCompensateMorningRunningCount(userStatistic.getCompensateMorningRunningCount() + runningCount);
                    userStatistic.setCompensateMorningRunningLength(userStatistic.getCompensateMorningRunningLength() + runningLength);
                } else if(runningType == RunningEnum.ACTIVITYRUN.getValue()) {//--活动跑
                    userStatistic.setCompensateActivityScore(userStatistic.getCompensateActivityScore() + runningScore);
                    userStatistic.setCompensateActivityCount(userStatistic.getCompensateActivityCount() + runningCount);
                }
            } catch (Exception e) {
                LOG.debug(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") +
                        " 查询用户统计信息失败(申诉补偿后更新统计)  参数:json=" + json.toString(), e);
                e.printStackTrace();
                //查询用户统计信息失败(申诉补偿后更新统计)
                eventExceptionService.eventExceptionHandler(new EventException(ResultMsg.STATISTIC_QUERY_ERROR, ev.getKey().toString(), json.toString()), e);
                return;
            }

            try {
                userStatisticService.insertOrUpdateStatistic(userStatistic);
            } catch (Exception e) {
                LOG.debug(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") +
                        " 保存用户统计信息失败(申诉补偿后更新统计)  参数:json=" + json.toString(), e);
                e.printStackTrace();
                //保存用户统计信息失败(申诉补偿后更新统计)
                eventExceptionService.eventExceptionHandler(new EventException(ResultMsg.STATISTIC_UPDATE_ERROR, ev.getKey().toString(), json.toString()), e);
            }

        }
    }
}
