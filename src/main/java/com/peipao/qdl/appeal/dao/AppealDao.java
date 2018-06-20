package com.peipao.qdl.appeal.dao;


import com.peipao.qdl.appeal.model.Appeal;
import com.peipao.qdl.appeal.model.FeedbackRecord;
import com.peipao.qdl.appeal.model.QA;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
@Repository
public interface AppealDao {
    void insertAppeal(Appeal appeal) throws Exception;
    int queryAppealStatusByRunningId(@Param("runningId") Long runningId) throws Exception;
    void updateAppeal(Appeal appeal) throws Exception;

    void insertFeedbackRecord(FeedbackRecord feedbackRecord) throws Exception;

    List<Map<String, Object>> queryFeedbackRecord(@Param("userId") Long userId) throws Exception;
    List<Map<String, Object>> queryFeedbackRecordList(@Param("operate") Byte operate, @Param("platform") Byte platform, @Param("userType") Byte userType, @Param("from") int from, @Param("num") int num) throws Exception;
    Long queryFeedbackRecordListCount(@Param("operate") Byte operate, @Param("platform") Byte platform, @Param("userType") Byte userType) throws Exception;
    void updateFeedbackRecord(FeedbackRecord feedbackRecord) throws Exception;

    void insertQA(QA qa) throws Exception;
    void updateQA(QA qa) throws Exception;
    List<Map<String, Object>> queryQAList(@Param("title") String title, @Param("from") int from, @Param("num") int num) throws Exception;
    Long queryQAListCount(@Param("title") String title) throws Exception;

    String queryQAContent(@Param("qaId") Long qaId) throws Exception;
    QA queryQAById(@Param("qaId") Long qaId) throws Exception;
    void deleteQA(@Param("qaId") Long qaId) throws Exception;
}
