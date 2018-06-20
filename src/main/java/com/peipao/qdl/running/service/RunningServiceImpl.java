package com.peipao.qdl.running.service;


import com.peipao.qdl.running.dao.RunningDao;
import com.peipao.qdl.running.dao.RunningNodeDao;
import com.peipao.qdl.running.dao.RunningRecordDao;
import com.peipao.qdl.running.model.Running;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.running.model.RunningNode;
import com.peipao.qdl.running.model.RunningRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
@Service
public class RunningServiceImpl implements RunningService {
    @Autowired
    private RunningRecordDao runningRecordDao;
    @Autowired
    private RunningDao runningDao;
    @Autowired
    private RunningCacheService runningCacheService;
    @Autowired
    private RunningLineCacheService runningLineCacheService;
    @Autowired
    private RunningNodeDao runningNodeDao;

    @Transactional
    @Override
    public void createRunning(Running running) throws Exception {
        running.setType(RunningEnum.FREERUNNING.getValue());
        if (running.getPassNode()) {
            running.setType(RunningEnum.RANDOMRUNNING.getValue());
        }
        if (running.getByOrder()) {
            running.setType(RunningEnum.ORIENTRUNNING.getValue());
            running.setNodeCount(running.getRunningLineList().size());
        }
        if (!running.getPassNode()) {
            running.setNodeCount(0);
        }
        runningCacheService.insertRunning(running);// insert 主表
        if (running.getPassNode()) {
            runningLineCacheService.insertRunningLineByBatch(running.getRunningId(), running.getRunningLineList());//　insert 子表
        }
    }

    @Transactional
    @Override
    public void updateRunning(Running running) throws Exception {
        running.setType(RunningEnum.FREERUNNING.getValue());
        if (running.getPassNode()) {
            running.setType(RunningEnum.RANDOMRUNNING.getValue());
        }
        if (running.getByOrder()) {
            running.setType(RunningEnum.ORIENTRUNNING.getValue());
            running.setNodeCount(running.getRunningLineList().size());
        }
        if (!running.getPassNode()) {
            running.setNodeCount(0);
        }
        if (running.getRunningId() == null) {
            runningCacheService.insertRunning(running);// insert 主表
        } else {
            runningCacheService.updateRunning(running);// 更新主表
        }
        runningLineCacheService.deleteRunningLineByBatch(running.getRunningId());// 删除子表
        if (running.getPassNode()) {
            runningLineCacheService.insertRunningLineByBatch(running.getRunningId(), running.getRunningLineList());// 重新插入子表
        }
    }

    @Override
    @Transactional
    public void deleteRunningByActivityId(Long activityId) throws Exception {
        runningCacheService.deleteByActivityId(activityId);
        Running running = runningDao.getByActivityId(activityId);
        if (running != null && running.getRunningId() != null) {
            runningLineCacheService.deleteRunningLineByBatch(running.getRunningId());// 删除子表
        }
    }

    @Override
    public RunningRecord getRunningRecordById(String runningRecordId) throws Exception {
        return runningRecordDao.getRunningRecordById(runningRecordId);
    }

    public List<RunningRecord> getByUserIdAndSemesterId(Long userId, Long semesterId, Integer from, Integer num) throws Exception {
        return runningRecordDao.getByUserIdAndSemesterId(userId, semesterId, from, num);
    }

    @Transactional
    @Override
    public void updateRunningRecordForAppeal(Map<String, Object> paramsMap) throws Exception {
        runningRecordDao.updateRunningRecordForAppeal(paramsMap);
    }

    @Override
    public Map<String, Object> getRunningRecordByIdWithUsernameAndImage(String runningRecordId) throws Exception {
        return runningRecordDao.getRunningRecordByIdWithUsernameAndImage(runningRecordId);
    }

    @Override
    public List<RunningNode> getRunningNodeByRunningRecordId(String tableDay, String runningRecordId) throws Exception {
        return runningNodeDao.getByRunningRecordId(tableDay, runningRecordId);
    }
}
