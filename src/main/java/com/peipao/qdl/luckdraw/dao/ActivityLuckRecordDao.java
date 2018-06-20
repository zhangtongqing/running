package com.peipao.qdl.luckdraw.dao;


import com.peipao.qdl.luckdraw.model.ActivityLuckRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 方法名称：
 * 功能描述：
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/12/19 10:43
 * 修订记录：
 */
@Repository
public interface ActivityLuckRecordDao {
    ActivityLuckRecord getLuckRecordByActivity(Map<String, Object> paramsMap) throws Exception;
    int getLuckCountByUser(Map<String, Object> paramsMap) throws Exception;
    int getPrizeCountByUser(Map<String, Object> paramsMap) throws Exception;
    void insertActivityLuckRecord(@Param("activityLuckRecord") ActivityLuckRecord activityLuckRecord) throws Exception;


    List<Map<String, Object>> getLuckRecordForPC(Map<String, Object> parmas) throws Exception;
    Long countLuckRecordForPC(Map<String, Object> parmas) throws Exception;

    List<Map<String, Object>> getActivityIdsByUserId(Map<String, Object> paramsMap) throws Exception;
    List<Map<String, Object>> getPrizeIdsByActivityId(Map<String, Object> paramsMap) throws Exception;
    List<Map<String, Object>> myLuckRecord(Map<String, Object> paramsMap) throws Exception;
    List<Map<String, Object>> activityLuckRecord(Map<String, Object> paramsMap) throws Exception;
    int getPrizeCountByPrizeId(@Param("prizeId") Long prizeId) throws Exception;
}
