package com.peipao.qdl.running.service;


import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.compensate.model.Compensate;
import com.peipao.qdl.running.model.RunningRecord;

import java.util.Map;

/**
 * 方法名称：RunningRecordService
 * 功能描述：RunningRecordService
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/20 13:40
 * 修订记录：
 */
public interface RunningRecordService {


    RunningRecord getRunningRecordById(String runningRecordId) throws Exception;
    void updateNodeTimeAndStatus(Map<String, Object> paramsMap) throws Exception;
    MyPageInfo getStudentAppealList(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception;
    void updateEffectiveStatusOnly(Map<String, Object> paramsMap) throws Exception;
    void updateEffectiveStatusOnlyForCompensate(Map<String, Object> paramsMap, Compensate compensate) throws Exception;
    Integer getRecordCountToday(Map<String, Object> paramsMap) throws Exception;

//    long getMorningRecordByUser(Map<String, Object> paramsMap) throws Exception;
//    CompensateMain getCompensateMainByUser(Map<String, Object> paramsMap) throws Exception;
//    void setRunningRecordByRule(RunningRuleValid runningRuleValid, RunningRule runningRule, RunningRecord runningRecord) throws Exception;

}
