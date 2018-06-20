package com.peipao.qdl.running.service;


import com.peipao.qdl.running.model.Running;
import com.peipao.qdl.running.model.RunningNode;
import com.peipao.qdl.running.model.RunningRecord;

import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
public interface RunningService {

    /**
     * 创建活动跑步规则
     */
    void createRunning(Running running) throws Exception;

    void updateRunning(Running running) throws Exception;

    void deleteRunningByActivityId(Long activityId) throws Exception;

    RunningRecord getRunningRecordById(String RunningId) throws Exception;

    void updateRunningRecordForAppeal(Map<String, Object> paramsMap) throws Exception;

    Map<String, Object> getRunningRecordByIdWithUsernameAndImage(String runningRecordId) throws Exception;

    List<RunningNode> getRunningNodeByRunningRecordId(String tableDay, String runningRecordId) throws Exception;
}
