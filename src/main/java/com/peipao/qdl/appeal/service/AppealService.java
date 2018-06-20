package com.peipao.qdl.appeal.service;

import com.peipao.qdl.appeal.model.FeedbackRecord;
import com.peipao.qdl.appeal.model.QA;

import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
public interface AppealService {
//    void addAppeal(Appeal appeal) throws Exception;

//    int getAppealStatusByRunningId(Long runningId) throws Exception;
//
//    void updateAppeal(Appeal appeal) throws Exception;

//    List<Appeal> getAppealListByStatus(Long userId, String schoolCode, Byte status) throws Exception;

    void addFeekbackRecord(Long userId, FeedbackRecord feedbackRecord) throws Exception;

    void updateFeedback(Long userId, Long feedbackId, int operate) throws Exception;

    List<Map<String, Object>> getFeedbackRecord(Long userId) throws Exception;

    void addQA(Long userId, QA qa) throws Exception;
    void updateQA(Long userId, QA qa) throws Exception;

    Map<String, Object> getQAList(String title, int pageindex, int pagesize) throws Exception;
    String getQAContent(Long qaId) throws Exception;
    QA getQaById(Long qaId) throws Exception;
    void deleteQA(Long userId, Long qaId) throws Exception;

    Map<String, Object> getFeedbackList(Byte operate, Byte platform, Byte userType, Long userId, int pageindex, int pagesize) throws Exception;


}
