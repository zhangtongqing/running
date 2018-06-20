package com.peipao.framework.log.dao;


import com.peipao.framework.log.model.SystemLog;
import com.peipao.framework.log.model.SystemLogVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 方法名称：SystemLogDao
 * 功能描述：SystemLogDao
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/8/3 16:41
 * 修订记录：
 */
@Repository
public interface SystemLogDao {
    void insertSystemLog(@Param("systemLog") SystemLog systemLog) throws Exception;
    Long getSystemLogsTotal(@Param("startTime") String startTime, @Param("endTime") String endTime) throws Exception;
    List<SystemLogVo> getSystemLogs(@Param("from") Integer from, @Param("num") Integer num, @Param("startTime") String startTime, @Param("endTime") String endTime) throws Exception;
}
