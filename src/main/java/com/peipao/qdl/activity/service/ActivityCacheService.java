package com.peipao.qdl.activity.service;

import com.peipao.qdl.activity.dao.ActivityDao;
import com.peipao.qdl.activity.model.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author Meteor.wu
 * @since 2018/1/31 15:43
 */
@Service
public class ActivityCacheService {

    @Autowired
    private ActivityDao activityDao;

    @Cacheable(value = "peipao", key = "'activity'+#activityId")
    public Activity getActivityById(Long activityId) throws Exception {
        return activityDao.getActivityById(activityId);
    }

    @CachePut(value = "peipao", key = "'activity'+#activity.getActivityId()")
    public Activity insertActivity(Activity activity) throws Exception {
        activityDao.insertActivity(activity);
        return activityDao.getActivityById(activity.getActivityId());
    }

    @CachePut(value = "peipao", key = "'activity'+#activity.getActivityId()")
    public Activity updateActivity(Activity activity) throws Exception {
        activityDao.updateActivity(activity);
        return activityDao.getActivityById(activity.getActivityId());
    }

    @CachePut(value = "peipao", key = "'activity'+#activityId")
    public Activity updateActivityEnrollAndSign(Long activityId, int enrollcount, int signcount, int allSucCount) throws Exception {
        activityDao.updateActivityEnrollAndSign(activityId, enrollcount, signcount, allSucCount);
        return activityDao.getActivityById(activityId);//TODO:METEOR.WU 可以优化，用原生redis的方法直接来更新reids 缓存
    }

    @CacheEvict(value = "peipao", key = "'activity'+#activityId")
    public void deleteActivity(Long activityId) throws Exception {
        activityDao.deleteActivity(activityId);
    }
}
