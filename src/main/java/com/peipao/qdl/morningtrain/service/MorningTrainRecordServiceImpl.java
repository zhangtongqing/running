package com.peipao.qdl.morningtrain.service;

import com.peipao.qdl.morningtrain.dao.MorningTrainRecordDao;
import com.peipao.qdl.morningtrain.model.MorningTrainRecord;
import com.peipao.qdl.morningtrain.model.MorningTrainRecordVo;
import com.peipao.qdl.statistics.model.UserStatistic;
import com.peipao.qdl.statistics.service.UserStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 方法名称：
 * 功能描述：
 * 作者：Liu Fan
 * 版本：2.1.0
 * 创建日期：2018/3/1 11:49
 * 修订记录：
 */
@Service
public class MorningTrainRecordServiceImpl implements MorningTrainRecordService {
    @Autowired
    private MorningTrainRecordDao morningTrainRecordDao;
    @Autowired
    private UserStatisticService userStatisticService;

    @Override
    public MorningTrainRecordVo getMorningTrainRecordToday(Map<String, Object> paramsMap) throws Exception {
        return morningTrainRecordDao.getMorningTrainRecordToday(paramsMap);
    }

    @Override
    public MorningTrainRecord findMorningTrainRecordToday(Map<String, Object> paramsMap) throws Exception {
        return morningTrainRecordDao.findMorningTrainRecordToday(paramsMap);
    }

    @Override
    @Transactional
    public void insertMorningTrainRecord(MorningTrainRecord morningTrainRecord) throws Exception {
        morningTrainRecordDao.insertMorningTrainRecord(morningTrainRecord);
    }

    @Override
    @Transactional
    public void updateMorningTrainRecord(MorningTrainRecord morningTrainRecord) throws Exception {
        morningTrainRecordDao.updateMorningTrainRecord(morningTrainRecord);
    }

    @Override
    @Transactional
    public void updateMorningTrainRecordAndStatistic(long schoolId, MorningTrainRecord morningTrainRecord) throws Exception {
        morningTrainRecordDao.updateMorningTrainRecord(morningTrainRecord);
        /******************************************开始更新学生综合统计信息*****************************************/
        int morningTrainCount = 1;
        UserStatistic userStatistic = userStatisticService.getByUserIdAndSemesterId(morningTrainRecord.getUserId(), morningTrainRecord.getSemesterId());
        if(null != userStatistic){
            Lock lock = new ReentrantLock();
            lock.lock();
                if(userStatistic.getMorningTrainCount() > 0) {
                    morningTrainCount = morningTrainCount + userStatistic.getMorningTrainCount();
                }
            lock.unlock();
        } else {
            userStatistic = new UserStatistic(schoolId, morningTrainRecord.getSemesterId(), morningTrainRecord.getUserId());
        }
        userStatistic.setMorningTrainCount(morningTrainCount);
        /**********************************开始保存跑步记录，保存学生统计信息***************************************/
        userStatisticService.insertOrUpdateStatistic(userStatistic);
    }
}
