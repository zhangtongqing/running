package com.peipao.qdl.statistics.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 方法名称：RunningStatisticDao
 * 功能描述：
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/15 13:00
 * 修订记录：
 */
@Repository
public interface RunningStatisticDao {

    /**
     * getMyCurrentStatistic
     * @param paramsMap<String, Object> (long semesterId, long userId)
     * @return list<Map<String, Object>>
     * @throws Exception
     */
    Map<String, Object> getMyCurrentStatistic(Map<String, Object> paramsMap) throws Exception;

    /**
     * getMyStatistics
     * @param paramsMap (long schoolId, long userId)
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getMyStatistics(Map<String, Object> paramsMap) throws Exception;

    /**
     * getMyStatistics
     * @param paramsMap (long schoolId, long semesterId, long userId)
     * @return
     * @throws Exception
     */
    Map<String, Object> getStudentStatistics(Map<String, Object> paramsMap) throws Exception;

    /**
     * getMorningRunningTargetStatistics(晨跑达标人数)
     * @param paramsMap (long semesterId, int morningRunningTarget)
     * @return
     * @throws Exception
     */
    Integer getMorningRunningTargetStatistics(Map<String, Object> paramsMap) throws Exception;
    Map<String, Object> getMorningRunningStatistics(Map<String, Object> paramsMap) throws Exception;
    /**
     * getFreeRunningTargetStatistics(自由跑达标人数)
     * @param paramsMap (long semesterId, int freeRunningTarget)
     * @return
     * @throws Exception
     */
    Integer getFreeRunningTargetStatistics(Map<String, Object> paramsMap) throws Exception;
    Map<String, Object> getFreeRunningStatistics(Map<String, Object> paramsMap) throws Exception;
    /**
     * getMyStdentTargetList(教务端成绩list)
     * @param paramsMap (long schoolId, long semesterId, String courseArray)
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getMyStdentTargetList(Map<String, Object> paramsMap) throws Exception;

    /**
     * app端获取真实跑步公里数据
     * @param paramsMap
     * @return
     */
    List<Map<String,Object>> getRunRealData(Map<String, Object> paramsMap);

    /**
     * 获取过去7天有效跑步数据：包括 晨跑、自由跑、活动跑
     * @param paramsMap
     * @return
     */
    List<Map<String,Object>> get7DayRunningLength(Map<String, Object> paramsMap);
}
