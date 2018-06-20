package com.peipao.qdl.running.dao;

import com.peipao.qdl.running.model.RunningRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 方法名称：RunningRecordDao
 * 功能描述：
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/4 15:32
 * 修订记录：
 */
@Repository
public interface RunningRecordDao {

    List<RunningRecord> getByUserIdAndSemesterId(@Param("userId") Long userId, @Param("semesterId") Long semesterId, @Param("from") Integer from, @Param("num") Integer num) throws Exception;

    List getStudentRunningRecord(@Param("semesterId") Long semesterId, @Param("teacherId") Long teacherId, @Param("studentId") Long studentId, @Param("isEffective") byte isEffective, @Param("type") Integer type, @Param("from") Integer from, @Param("num") Integer num) throws Exception;

    Long getStudentRunningRecordTotal(@Param("semesterId") Long semesterId, @Param("teacherId") Long teacherId, @Param("studentId") Long studentId, @Param("isEffective") byte isEffective, @Param("type") Integer type) throws Exception;

    List getStudentNonEffectiveRunningRecord(@Param("semesterId") Long semesterId, @Param("teacherId") Long teacherId, @Param("studentId") Long studentId, @Param("isEffective") byte isEffective, @Param("status") Integer status, @Param("from") Integer from, @Param("num") Integer num) throws Exception;

    Long getStudentNonEffectiveRunningRecordTotal(@Param("semesterId") Long semesterId, @Param("teacherId") Long teacherId, @Param("studentId") Long studentId, @Param("isEffective") byte isEffective, @Param("status") Integer status) throws Exception;

    List getStudentRunningRecordDuration(@Param("semesterId") Long semesterId, @Param("studentId") Long studentId, @Param("isEffective") byte isEffective) throws Exception;

    RunningRecord getRunningRecordById(@Param("runningRecordId") String runningRecordId) throws Exception;

    void insertRunningRecord(@Param("runningRecord") RunningRecord runningRecord) throws Exception;

    void setRunningEffective(@Param("runningRecordId") String runningRecordId, @Param("isEffective") byte isEffective, @Param("status") Integer status) throws Exception;

    void updateRunningRecordById(@Param("runningRecord") RunningRecord runningRecord) throws Exception;

    /**
     * @param paramsMap String runningRecordId, Integer status, Date appealTime
     * @throws Exception
     */
    void updateRunningRecordForAppeal(Map<String, Object> paramsMap) throws Exception;

    Integer getRecordCountToday(Map<String, Object> paramsMap) throws Exception;

    void updateNodeTimeAndStatus(Map<String, Object> paramsMap) throws Exception;

    List<Map<String, Object>> getStudentAppealList(Map<String, Object> paramsMap) throws Exception;

    void updateEffectiveStatusOnly(Map<String, Object> paramsMap) throws Exception;

    Map<String, Object> getRunningRecordByIdWithUsernameAndImage(@Param("runningRecordId") String runningRecordId) throws Exception;

//    long getMorningRecordByUser(Map<String, Object> paramsMap) throws Exception;
}
