package com.peipao.qdl.activity.dao;


import com.peipao.qdl.activity.model.ActivityPartinRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/8/14
 **/
@Repository
public interface ActivityPartInRecordDao {
    void insertActvityPartinRecord(ActivityPartinRecord activityPartinRecord);
    void updateActivityPartinRecord(ActivityPartinRecord record) throws Exception;
    ActivityPartinRecord getPartinRecordByRunningRecordId(@Param("runningRecordId") String runningRecordId, @Param("isEffective") int isEffective) throws Exception;
    List<Map<String, Object>> getStudentActivityList(Map<String, Object> paramsMap) throws Exception;
}
