package com.peipao.qdl.statistics.service;


import com.peipao.framework.page.MyPageInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：RunningStatisticService
 * 功能描述：RunningStatisticService
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/15 13:00
 * 修订记录：
 */
public interface RunningStatisticService {

    Map<String, Object> getMyCurrentStatistic(Map<String, Object> paramsMap) throws Exception;
    List<Map<String, Object>> getMyStatistics(Map<String, Object> paramsMap) throws Exception;
    Map<String, Object> getStudentStatistics(Map<String, Object> paramsMap) throws Exception;
    Integer getMorningRunningTargetStatistics(Map<String, Object> paramsMap) throws Exception;
    Map<String, Object> getMorningRunningStatistics(Map<String, Object> paramsMap) throws Exception;
    Integer getFreeRunningTargetStatistics(Map<String, Object> paramsMap) throws Exception;
    Map<String, Object> getFreeRunningStatistics(Map<String, Object> paramsMap) throws Exception;
    MyPageInfo getMyStdentTargetList(Map<String, Object> paramsMap, int[] pageParams) throws Exception;
    BigDecimal getRunRealData(Long userId, Map<String, Object> paramsMap) throws Exception;
    List<Map<String, Object>> get7DayRunningLength(Long userId, Map<String, Object> paramsMap)throws Exception;
}
