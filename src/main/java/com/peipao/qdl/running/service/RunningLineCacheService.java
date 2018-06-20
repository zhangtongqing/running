package com.peipao.qdl.running.service;

import com.peipao.qdl.running.dao.RunningLineDao;
import com.peipao.qdl.running.model.RunningLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Meteor.wu
 * @since 2018/2/1 11:24
 */
@Service
public class RunningLineCacheService {
    @Autowired
    private RunningLineDao runningLineDao;

    @Cacheable(value = "peipao", key = "'runningline'+#runningId")
    public List<RunningLine> getRunningLineByRunningId(Long runningId) throws Exception {
        return runningLineDao.getRunningLineByRunningId(runningId);
    }

    @CachePut(value = "peipao", key = "'runningline'+#runningId")
    public List<RunningLine> insertRunningLineByBatch(Long runningId, List<RunningLine> runningLineList) throws Exception{
        if (runningLineList != null && runningLineList.size() > 0) {
            runningLineDao.insertRunningLineByBatch(runningId, runningLineList);
            return runningLineDao.getRunningLineByRunningId(runningId);
        } else {
            return runningLineList;
        }
    }

    @CacheEvict(value = "peipao", key = "'runningline'+#runningId")
    public void deleteRunningLineByBatch(Long runningId) throws Exception{
        runningLineDao.deleteRunningLineByBatch(runningId);
    }
}
