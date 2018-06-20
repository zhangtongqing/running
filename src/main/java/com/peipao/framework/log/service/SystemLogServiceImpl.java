package com.peipao.framework.log.service;


import com.peipao.framework.log.dao.SystemLogAdminDao;
import com.peipao.framework.log.dao.SystemLogDao;
import com.peipao.framework.log.model.SystemLog;
import com.peipao.framework.log.model.SystemLogVo;
import com.peipao.framework.page.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 方法名称：SystemLogServiceImpl
 * 功能描述：SystemLogServiceImpl
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/8/3 16:41
 * 修订记录：
 */
@Service
public class SystemLogServiceImpl implements SystemLogService {
    @Autowired
    private SystemLogDao systemLogDao;
    @Autowired
    private SystemLogAdminDao systemLogAdminDao;

    @Override
    @Transactional
    @Async
    public void insertSystemLog(SystemLog systemLog) throws Exception {
        systemLogDao.insertSystemLog(systemLog);
    }

    @Override
    @Transactional
    @Async
    public void insertSystemLogAdmin(SystemLog systemLog) throws Exception {
        systemLogAdminDao.insertSystemLogAdmin(systemLog);
    }

    @Override
    public Map<String, Object> getSystemLogs(Integer pageindex, Integer pagesize, String startTime, String endTime) throws Exception {
        Long totalCount = systemLogDao.getSystemLogsTotal(startTime, endTime);
        List<SystemLogVo> list = systemLogDao.getSystemLogs((pageindex-1) * pagesize, pagesize, startTime, endTime);
        list.forEach(i->i.setKey(i.getLogId()));
        Map<String, Object> map = MyPage.processPage(totalCount, pagesize, pageindex, list);
        return map;
    }
}
