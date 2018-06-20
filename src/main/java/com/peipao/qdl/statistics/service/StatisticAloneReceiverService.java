//package com.peipao.qdl.statistics.service;
//
//import com.peipao.framework.constant.ResultMsg;
//import com.peipao.framework.event.EventExceptionService;
//import com.peipao.framework.exception.EventException;
//import com.peipao.framework.model.Response;
//import com.peipao.framework.util.DateUtil;
//import com.peipao.framework.util.ValidateUtil;
//import com.peipao.qdl.running.model.RunningEnum;
//import com.peipao.qdl.statistics.model.UserStatistic;
//import net.sf.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import reactor.bus.Event;
//import reactor.fn.Consumer;
//
//import java.util.Date;
//
///**
// * 方法名称：StatisticReceiverService
// * 功能描述：StatisticReceiverService
// * 作者：Liu Fan
// * 版本：2.0.11
// * 创建日期：2018/1/29 18:15
// * 修订记录：
// */
//
//@Service
//public class StatisticAloneReceiverService implements Consumer<Event<JSONObject>> {
//    @Autowired
//    private UserStatisticService userStatisticService;
//    @Autowired
//    private EventExceptionService eventExceptionService;
//
//    protected static final Logger LOG = LoggerFactory.getLogger(StatisticAloneReceiverService.class);
//
//    @Override
//    public void accept(Event<JSONObject> ev) {
//        if(null != ev.getData()) {
//            JSONObject json = ev.getData();
//            int runningCount = 0;
//            float runningLength = 0f;
//            float runningScore = 0f;
//            long userId = json.getLong("userId");
//            long schoolId = json.getLong("schoolId");
//            long semesterId = json.getLong("semesterId");
//            int runningType = json.getInt("runningType");
//            if(runningType == RunningEnum.MORNINGRUNNING.getValue()) {//晨跑
//                runningCount = json.getInt("morningRunningCount");//默认增加晨跑次数
//            } else if(runningType == RunningEnum.FREERUNNING.getValue()) {//自由跑
//                runningLength = Float.parseFloat(json.getString("runningLength"));
//            } else if(runningType == RunningEnum.ACTIVITYRUN.getValue()) {//活动跑
//                runningScore = Float.parseFloat(json.getString("compensateScore"));
//            }
//            UserStatistic userStatistic = null;
//            try {
//                userStatistic = userStatisticService.getByUserIdAndSemesterId(userId, semesterId);
//                if(null == userStatistic){
//                    userStatistic = new UserStatistic(schoolId, semesterId, userId);
//                }
//                if(RunningEnum.FREERUNNING.getValue() == runningType) {//--自由跑
//                    userStatistic.setCompensateFreeRunningLength(userStatistic.getCompensateFreeRunningLength() + runningLength);
//                } else if(RunningEnum.MORNINGRUNNING.getValue() == runningType) {//--晨跑
//                    userStatistic.setCompensateMorningRunningCount(userStatistic.getCompensateMorningRunningCount() + runningCount);
//                } else if(runningType == RunningEnum.ACTIVITYRUN.getValue()) {//--活动跑
//                    userStatistic.setCompensateActivityScore(userStatistic.getCompensateActivityScore() + runningScore);
//                }
//            } catch (Exception e) {
//                LOG.debug(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") +
//                        " 查询用户统计信息失败(成绩补偿修改)  参数:json=" + json.toString(), e);
//                e.printStackTrace();
//                //查询用户统计信息失败(成绩补偿修改)
//                eventExceptionService.eventExceptionHandler(new EventException(ResultMsg.STATISTIC_QUERY_ERROR, ev.getKey().toString(), json.toString()), e);
//                return;
//            }
//            try {
//                userStatisticService.insertOrUpdateStatistic(userStatistic);
//            } catch (Exception e) {
//                LOG.debug(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") +
//                        " 保存用户统计信息失败(成绩补偿修改)  参数:json=" + json.toString(), e);
//                e.printStackTrace();
//                //保存用户统计信息失败(成绩补偿修改)
//                eventExceptionService.eventExceptionHandler(new EventException(ResultMsg.STATISTIC_UPDATE_ERROR, ev.getKey().toString(), json.toString()), e);
//            }
//        }
//    }
//}
