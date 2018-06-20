package com.peipao.qdl.running.dao;


import com.peipao.qdl.running.model.RunningNode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 方法名称：RunningNodeDao
 * 功能描述：RunningNodeDao
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/5 16:32
 * 修订记录：
 */
@Repository
public interface RunningNodeDao {
    List<RunningNode> getByRunningRecordId(@Param("tableDay") String tableDay, @Param("runningRecordId") String runningRecordId);
//    void insertRunningNodeByBatch(@Param("tableDay") String tableDay, @Param("runningNodeList") List<RunningNode> runningNodeList);
//    void updateRunningNodeListById(@Param("tableDay") String tableDay, @Param("runningNodeList") List<RunningNode> runningNodeList) throws Exception;
//    void deleteNodeByRecordId(@Param("tableDay") String tableDay, @Param("runningRecordId") String runningRecordId) throws Exception;
//    void createRunningNodeTable(@Param("tableName") String tableName) throws Exception;
//    Integer checkTableExist(@Param("schemaName") String schemaName, @Param("tableName") String tableName) throws Exception;
}
