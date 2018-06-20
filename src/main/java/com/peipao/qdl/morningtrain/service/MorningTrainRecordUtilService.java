package com.peipao.qdl.morningtrain.service;

import com.peipao.framework.util.DateUtil;
import com.peipao.qdl.morningtrain.model.MorningTrainRecord;
import com.peipao.qdl.morningtrain.model.MorningTrainRecordVo;
import com.peipao.qdl.runningrule.model.vo.RunningRuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 方法名称：
 * 功能描述：
 * 作者：Liu Fan
 * 版本：2.1.0
 * 创建日期：2018/3/2 9:31
 * 修订记录：
 */
@Service
public class MorningTrainRecordUtilService {
    @Autowired
    private MorningTrainRecordService morningTrainRecordService;

    public MorningTrainRecord getMorningTrainRecordForToday(RunningRuleVo runningRule, long semesterId, long userId) throws Exception {
        Date today = new Date();
        //判断今日是否已经有晨练
        Map<String, Object> paramsMap = new HashMap<>();
        String datStr = DateUtil.dateToStr(today, "YYYY-MM-dd");
        Date startDate = DateUtil.parseDate(datStr + " " + runningRule.getStartTime() + ":00", "yyyy-MM-dd HH:mm:ss");
        Date endDate = DateUtil.parseDate(datStr + " " + runningRule.getEndTime() + ":00", "yyyy-MM-dd HH:mm:ss");
        paramsMap.put("semesterId", semesterId);
        paramsMap.put("userId", userId);
        paramsMap.put("startDate", startDate);
        paramsMap.put("endDate", endDate);
        return morningTrainRecordService.findMorningTrainRecordToday(paramsMap);
    }
}
