package com.peipao.qdl.running.dao;


import com.peipao.qdl.running.model.RunningLine;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 方法名称：RunningLineDao
 * 功能描述：RunningLineDao
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/7 10:25
 * 修订记录：
 */
@Repository
public interface RunningLineDao {
    List<RunningLine> getRunningLineByRunningId(@Param("runningId") Long runningId) throws Exception;
    void insertRunningLineByBatch(@Param("runningId") Long runningId, @Param("runningLineList") List<RunningLine> runningLineList) throws Exception;
    void deleteRunningLineByBatch(@Param("runningId") Long runningId) throws Exception;
}
