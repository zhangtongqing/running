package com.peipao.qdl.luckdraw.service;

import com.peipao.qdl.luckdraw.model.ActivityLuckRecord;
import net.sf.json.JSONObject;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 方法名称：ActivityLuckRecordService
 * 功能描述：ActivityLuckRecordService
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/12/19 10:56
 * 修订记录：
 */

public interface ActivityLuckRecordService {
    ActivityLuckRecord getLuckRecordByActivity(Map<String, Object> paramsMap) throws Exception;

    int getLuckCountByUser(Map<String, Object> paramsMap) throws Exception;
    int getPrizeCountByUser(Map<String, Object> paramsMap) throws Exception;
    void insertActivityLuckRecord(ActivityLuckRecord activityLuckRecord) throws Exception;

    Map<String, Object> getLuckRecordForPC(JSONObject json, int[] pageParams) throws Exception;

    List<Map<String, Object>> getActivityIdsByUserId(Map<String, Object> paramsMap) throws Exception;
    List<Map<String, Object>> getPrizeIdsByActivityId(Map<String, Object> paramsMap) throws Exception;
    List<Map<String, Object>> myLuckRecord(Map<String, Object> paramsMap) throws Exception;
    List<Map<String, Object>> activityLuckRecord(Map<String, Object> paramsMap) throws Exception;

    Long countLuckRecordForPC(Long activityId) throws Exception;

    int getPrizeCountByPrizeId(Long prizeId) throws Exception;
}
