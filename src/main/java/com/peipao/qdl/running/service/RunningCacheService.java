package com.peipao.qdl.running.service;

import com.peipao.qdl.running.dao.RunningDao;
import com.peipao.qdl.running.model.Running;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 跑步running缓存
 *
 * @author Meteor.wu
 * @since 2018/2/1 10:32
 */
@Service
public class RunningCacheService {

    @Autowired
    private RunningDao runningDao;

    @CachePut(value = "peipao", key = "'running'+#running.getActivityId()")
    public Running insertRunning(Running running) throws Exception {
        runningDao.insertRunning(running);
        return runningDao.getById(running.getRunningId());
    }

    @CachePut(value = "peipao", key = "'running'+#running.getActivityId()")
    public Running updateRunning(Running running) throws Exception {
        runningDao.updateRunning(running);
        return runningDao.getById(running.getRunningId());
    }

    @CacheEvict(value = "peipao", key = "'running'+#activityId")
    public void deleteByActivityId(Long activityId) throws Exception {
        runningDao.deleteById(activityId);
    }

    @Cacheable(value = "peipao", key = "'running'+#activityId")
    public Running getByActivityId(Long activityId) throws Exception{
        return runningDao.getByActivityId(activityId);
    }
}
