package com.peipao.qdl.running.dao;



import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 方法名称：RecordDao
 * 功能描述：
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/16 15:32
 * 修订记录：
 */
@Repository
public interface RecordDao {
    /**
     * app端查询个人运动记录
     * @param paramsMap (long semesterId, long userId)
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getMyRunningRecord(Map<String, Object> paramsMap) throws Exception;

    List<Map<String, Object>> getStudentRunningList(Map<String, Object> paramsMap) throws Exception;

    List<Map<String, Object>> getStudentActivityRunningList(Map<String, Object> paramsMap) throws Exception;

    List<Map<String,Object>> getMyMorningExercisesRecord(Map<String, Object> paramsMap);
}
