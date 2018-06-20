package com.peipao.qdl.running.dao;


import com.peipao.qdl.running.model.Running;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 方法名称：RunningDao
 * 功能描述：
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/4 15:25
 * 修订记录：
 */
@Repository
public interface RunningDao {
    void insertRunning(Running running) throws Exception;
    void updateRunning(Running running) throws Exception;

    Running getById(@Param("runningId") Long runningId) throws Exception;

    Running getByActivityId(@Param("activityId") Long activityId) throws Exception;

    void deleteById(@Param("runningId") Long runningId) throws Exception;

}
