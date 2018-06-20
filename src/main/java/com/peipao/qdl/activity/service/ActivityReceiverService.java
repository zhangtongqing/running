package com.peipao.qdl.activity.service;

import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.event.EventExceptionService;
import com.peipao.framework.exception.EventException;
import com.peipao.framework.util.DateUtil;
import com.peipao.qdl.statistics.service.StatisticReceiverService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.Date;

/**
 * 方法名称：
 * 功能描述：
 * 作者：Liu Fan
 * 版本：2.0.11
 * 创建日期：2018/1/31 13:37
 * 修订记录：
 */

@Service
public class ActivityReceiverService implements Consumer<Event<JSONObject>> {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private EventExceptionService eventExceptionService;

    protected static final Logger LOG = LoggerFactory.getLogger(StatisticReceiverService.class);

    @Override
    public void accept(Event<JSONObject> ev) {
        if(null != ev.getData()) {
            JSONObject json = ev.getData();
            Long activityId = json.getLong("activityId");
            Long userId = json.getLong("userId");
            int duration = json.getInt("duration");
            int calorie = json.getInt("calorie");
            float length = Float.parseFloat(json.getString("length"));
            int isEffective = json.getInt("isEffective");
            String runningRecordId = json.getString("runningRecordId");
            try {
                activityService.updateActivityMemberAfterRunning(
                        activityId,
                        userId,
                        duration,
                        calorie,
                        length,
                        isEffective,
                        runningRecordId
                );
            } catch (Exception e) {
                LOG.debug(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") +
                        " 更新用户活动参与信息失败(申诉补偿后更新)  参数:json=" + json.toString(), e);
                e.printStackTrace();
                //查询用户统计信息失败(申诉补偿后更新统计)
                eventExceptionService.eventExceptionHandler(new EventException(ResultMsg.STATISTIC_QUERY_ERROR, ev.getKey().toString(), json.toString()), e);
            }
        }
    }
}
