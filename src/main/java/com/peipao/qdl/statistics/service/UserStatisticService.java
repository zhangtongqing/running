package com.peipao.qdl.statistics.service;


import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.activity.model.Activity;
import com.peipao.qdl.running.model.RunningRecord;
import com.peipao.qdl.statistics.model.UserStatistic;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：UserStatisticService
 * 功能描述：UserStatisticService
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/4 17:45
 * 修订记录：
 */
public interface UserStatisticService {
    UserStatistic getByUserIdAndSemesterId(Long userId, Long semesterId) throws Exception;

    void insertOrUpdateRecordAndStatistic(Activity activity, UserStatistic userStatistic, RunningRecord runningRecord, String userStatisticFlag, String flag) throws Exception;
    
    void insertOrUpdateStatistic(UserStatistic userStatistic) throws Exception;

    void setUserStatisticByActivityWithOutRunning(Long userId, Activity activity) throws Exception;

    MyPageInfo getStudentScoreInfoList(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception;

    List<Map<String, Object>> getStudentScoreInfoListExport(Map<String, Object> paramsMap, boolean morningTrainFlag) throws Exception;

    List<Map<String, Object>> getStudentScoreInfoList(Map<String, Object> paramsMap) throws Exception;

    Float getMyRankingLength(Long userId) throws Exception;

    void insertOrUpdateStatisticByCourseSign(Map<String, Object> paramsMap)throws Exception;

    Float getMyRankingLengthByMorningRun(long userId)throws Exception;

//    List getValidLengthAndScoreByUser(Long userId, Long semesterId, Float sportsUpline) throws Exception;
//    Map<String, Object> getStudentCourseItem(Long userId, Long semesterId) throws Exception;
//    Map<String, Object> getStudentActivityItem(Long userId, Long semesterId) throws Exception;
//    void setUserStatisticByCourseSign(Long userId, Float courseScore, Integer duration, UserSchool userSchool) throws Exception;
//    List getScoreListIfNull(Long semesterId) throws Exception;
//    List getByUserId(Long userId, Long schoolId, Long semesterId, Float sportsUpline, Float activityUpline, Float attendanceUpline) throws Exception;
//    List getTheSemesterScoreAndRunningLength(Long userId, Long statisticId, Float sportsUpline, Float activityUpline, Float attendanceUpline) throws Exception;
//    List getMyRankingInCourse(Long userId, Long schoolId, Long semesterId, Long courseId) throws Exception;
//    List getMyRankingInSchool(Long userId, Long schoolId) throws Exception;
//    List getMyRankingInCountry(Long userId) throws Exception;
//    List getValidLengthAndMorningCountByUser(Long userId, Long semesterId) throws Exception;
//    void insertUserStatistic(UserStatistic userStatistic) throws Exception;
//    void updateUserStatistic(UserStatistic userStatistic) throws Exception;
//    void setUserStatisticByRunningRecord(UserStatistic userStatistic, RunningRecord runningRecord, Boolean isSetEffective) throws Exception;
//    List getRankingInCourse(Long schoolId, Long semesterId, Long courseId) throws Exception;//方法转到缓存查询中
//    List getRankingInSchool(Long schoolId) throws Exception;
//    List getRankingInCountry() throws Exception;

}
