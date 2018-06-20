package com.peipao.qdl.activity.dao;


import com.peipao.qdl.activity.model.ActivityMember;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/8/14
 **/
@Repository
public interface ActivityMemberDao {
    void insertActivityMember(@Param("userId") Long userId, @Param("activityId") Long activityId,
                              @Param("enrollTime") Date enrollTime, @Param("createTime") Date createTime) throws Exception;
    void updateActivityMember(ActivityMember activityMember) throws Exception;

    List<Map<String, Object>> queryActivityMemberListForWeb(@Param("activityId") Long activityId, @Param("from") int from, @Param("num") int num, @Param("status") int status) throws Exception;
    Long queryActivityMemberListForWebCount(@Param("activityId") Long activityId, @Param("status") Integer status) throws Exception;

    List<Map<String, Object>> getActivityMemberList(@Param("activityId") Long activityId) throws Exception;
    Map<String, Object> getMyRankInActivityMemberList(@Param("activityId") Long activityId, @Param("userId") Long userId) throws Exception;

    ActivityMember queryActivityMemberById(@Param("activityId") Long activityId, @Param("userId") Long userId) throws Exception;

    List<Map<String,Object>> queryActivityMemberListForWebNew(Map<String, Object> paramsMap);

    Map<String, Object>  getStatisticsData(@Param("activityId") Long activityId);

    List<Map<String,Object>> getActivityRunningRecordByUserId(Map<String, Object> paramsMap);

    List<Map<String, Object>> getActivityByMemberList(@Param("activityId") Long activityId, @Param("size") int size);
}
