package com.peipao.qdl.statistics.dao;


import com.peipao.qdl.statistics.model.UserStatistic;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 方法名称：UserStatisticDao
 * 功能描述：
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/4 17:46
 * 修订记录：
 */
@Repository
public interface UserStatisticDao {

    //根据学生ID获得学生本学期跑步统计数据
    UserStatistic getByUserIdAndSemesterId(@Param("userId") Long userId, @Param("semesterId") Long semesterId) throws Exception;
//    @Cacheable("getRankingInCountry")//查询全国排名，缓存
    List<Map<String, Object>> getRankingInCountry() throws Exception;
//    @Cacheable("getRankingInSchool")//查询全校排名，缓存
    List<Map<String, Object>> getRankingInSchool(@Param("schoolId") Long schoolId) throws Exception;
//    @Cacheable("getRankingInCourse")//查询本部排名，缓存
    List<Map<String, Object>> getRankingInCourse(@Param("schoolId") Long schoolId, @Param("semesterId") Long semesterId, @Param("courseId") Long courseId) throws Exception;

    Float getMyRankingLength(@Param("userId") Long userId) throws Exception;

    void insertUserStatistic(UserStatistic userStatistic) throws Exception;

    void updateUserStatistic(UserStatistic userStatistic) throws Exception;

    List<Map<String, Object>> getStudentScoreInfoList(Map<String, Object> paramsMap) throws Exception;

    List<Map<String, Object>> getStudentScoreInfoListExport(Map<String, Object> paramsMap) throws Exception;

    void updateStatisticByCourseSign(Map<String, Object> paramsMap)throws Exception;

    List<Map<String, Object>> getWeekCount(Map<String, Object> paramsMap)throws Exception;

    List<Map<String,Object>> getRankingInSchoolByMornigRun(@Param("schoolId") Long schoolId) throws Exception;

    Float getMyRankingLengthByMorningRun(@Param("userId") long userId)throws Exception;

    List<Map<String,Object>> getRankingInCourseByMorningRun(@Param("schoolId") Long schoolId, @Param("semesterId") Long semesterId, @Param("courseId") Long courseId);

    List<Map<String,Object>> getRankingInCountryByMorningRun() throws Exception;
}
