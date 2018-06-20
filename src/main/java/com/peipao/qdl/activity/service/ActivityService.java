package com.peipao.qdl.activity.service;


import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.activity.model.Activity;

import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
public interface ActivityService {
    List<Map<String, Object>> getMyActivityList(Long userId, Integer from, Integer num) throws Exception;
    List<Map<String, Object>> getActivityListBySchoolId(Long userId, Integer from, Integer num) throws Exception;
    List<Map<String, Object>> getOfficialActivityList(Long userId, Integer from, Integer num) throws Exception;
    Activity getActivityDetailForApp(Long userId, Long activityId) throws Exception;
    void enroll(Long userId, Long activityId) throws Exception;
    void sign(Long userId, Long activityId, Double longitude, Double latitude, String signAddress) throws Exception;
    Map<String, Object> addActivity(Long userId, Activity activity) throws Exception;
    Map<String, Object> addOfficalActivity(Long userId, Activity activity) throws Exception;
    void deleteActivity(Long userId, Long activityId) throws Exception;
    void officiaDeletelActivity(Long userId, Long activityId) throws Exception;
    Map<String, Object> getStudentFinishActivityList(Long userId, Long studentId, int pageindex, int pagesize) throws Exception;
    Map<String, Object> getStudentNonFinishActivityList(Long userId, Long studentId, int pageindex, int pagesize) throws Exception;
    Map<String, Object> getActivityMemberList(Long activityId, Long userId) throws Exception;
    Map<String, Object> getActivityList(Long userId, Integer type, Integer status, String name, int pageindex, int pagesize) throws Exception;
    Map<String, Object> getActivityListBySchoolId(Long userId, Long schoolId, int pageindex, int pagesize) throws Exception;
    Activity getActivityDetailForWeb(Long userId, Long activityId) throws Exception;
    Activity getOfficialActivityDetailForWeb(Long userId, Long activityId) throws Exception;
    Map<String, Object> getActivityMemberListForWeb(Long userId, Long activityId, int pageindex, int pagesize, int status) throws Exception;
    Map<String, Object> getOfficialActivityMemberListForWeb(Long userId, Long activityId, int pageindex, int pagesize, int status) throws Exception;
    Map<String, Object> getActivityBasicForWeb(Long userId, Long activityId) throws Exception;
    Integer countActivityByUserId(Long userId) throws Exception;

    Map<String, Object> getOfficialActivityListForWeb(Long userId, Integer type, Integer status, String name, int pageindex, int pagesize) throws Exception;

    Map<String, Object> updateActivity(Long userId, Activity activity) throws Exception;
    Map<String, Object> updateOfficialActivity(Long userId, Activity activity) throws Exception;

    Map<String, Object> getAllSchoolActivityList(Long userId, int pageindex, int pagesize) throws Exception;

    /**
     * 跑步成功后或者申诉成功后修改用户活动跑步数据
     */
    void updateActivityMemberAfterRunning(Long activityId, Long userId, int duration, int calorie, float length, int isEffective, String runningRecordId) throws Exception;

    Activity getActivityDetailForShare(long activityId) throws Exception;

    List<Map<String, Object>> getActivityAccess(long userId) throws Exception;

    MyPageInfo getStudentActivityList(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception;

    MyPageInfo getActivityMemberListForWebNew(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception;

    MyPageInfo getActivityList(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception;

    List<Map<String,Object>> getActivityRunningRecordByUserId(Map<String, Object> paramsMap);

    List<Map<String, Object>> getActivityByMemberList(Long activityId, int size);
}
