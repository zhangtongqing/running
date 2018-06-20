package com.peipao.framework.log.service;



import com.peipao.framework.log.model.SystemLog;

import java.util.Map;

/**
 * 方法名称：
 * 功能描述：
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/8/3 16:36
 * 修订记录：
 */
public interface SystemLogService {
    void insertSystemLog(SystemLog systemLog) throws Exception;
    void insertSystemLogAdmin(SystemLog systemLog) throws Exception;
    Map<String, Object> getSystemLogs(Integer pageindex, Integer pagesize, String startTime, String endTime) throws Exception;
}
