package com.peipao.qdl.activity.dao;


import com.peipao.qdl.activity.model.Activity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
@Repository
public interface ActivityDao {
    List<Map<String, Object>> queryActivityListByUserId(@Param("userId") Long userId, @Param("from") Integer from, @Param("num") Integer num) throws Exception;
    List<Map<String, Object>> queryActivityListBySchoolId(@Param("userId") Long userId, @Param("schoolId") Long schoolId, @Param("from") Integer from, @Param("num") Integer num) throws Exception;
    List<Map<String, Object>> queryActivityListByPublishType(@Param("userId") Long userId, @Param("publishType") Byte publishType, @Param("from") Integer from, @Param("num") Integer num) throws Exception;
    Activity getActivityById(@Param("activityId") Long activityId) throws Exception;

    void insertActivity(Activity activity) throws Exception;
    void deleteActivity(@Param("activityId") Long activityId) throws Exception;

    /*
    查询有效数据
     */
    List<Map<String, Object>> queryEffectiveActivityListByUserId(@Param("userId") Long userId, @Param("from") Integer from, @Param("num") Integer num) throws Exception;
    Long queryEffectiveActivityListByUserIdCount(@Param("userId") Long userId) throws Exception;

    List<Map<String, Object>> queryNonEffectiveActivityListByUserId(@Param("userId") Long userId, @Param("from") Integer from, @Param("num") Integer num) throws Exception;
    Long queryNonEffectiveActivityListByUserIdCount(@Param("userId") Long userId) throws Exception;

    List<Map<String, Object>> getActivityList(@Param("schoolId") Long schoolId, @Param("type") Integer type, @Param("status") Integer status, @Param("name") String name, @Param("from") int from, @Param("num") int num) throws Exception;
    Long getActivityListCount(@Param("schoolId") Long schoolId, @Param("type") Integer type, @Param("status") Integer status, @Param("name") String name) throws Exception;

    void updateActivityEnrollAndSign(@Param("activityId") Long activityId, @Param("enrollcount") int enrollcount, @Param("signcount") int signcount, @Param("allSucCount") int allSucCount);

    Integer countActivityByUserId(@Param("userId") Long userId) throws Exception;

    List<Map<String, Object>> getOfficialActivityListForWeb(@Param("type") Integer type, @Param("status") Integer status, @Param("name") String name, @Param("from") Integer from, @Param("num") Integer num) throws Exception;
    Long getOfficialActivityListForWebCount(@Param("type") Integer type, @Param("status") Integer status, @Param("name") String name) throws Exception;

    void updateActivity(Activity activity) throws Exception;

    List<Map<String, Object>> queryAllSchoolActivityList(@Param("from") Integer from, @Param("num") Integer num) throws Exception;
    Long queryAllSchoolActivityListCount() throws Exception;

    Map<String, Object> getActivityDetailForShare(@Param("activityId") Long activityId) throws Exception;

    Map<String, Object> getQuotiety() throws Exception;
    List<Map<String, Object>> getActivityAccess(@Param("userId") long userId) throws Exception;

    List<Map<String,Object>> getActivityLists(Map<String, Object> paramsMap) throws Exception;
}
