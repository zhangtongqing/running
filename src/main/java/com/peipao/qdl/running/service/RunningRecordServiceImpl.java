package com.peipao.qdl.running.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.compensate.dao.CompensateDao;
import com.peipao.qdl.compensate.dao.CompensateMainDao;
import com.peipao.qdl.compensate.model.Compensate;
import com.peipao.qdl.compensate.model.CompensateMain;
import com.peipao.qdl.running.dao.RunningRecordDao;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.running.model.RunningRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：RunningRecordServiceImpl
 * 功能描述：RunningRecordServiceImpl
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/7/20 14:14
 * 修订记录：
 */
@Service
public class RunningRecordServiceImpl implements RunningRecordService {
    @Autowired
    private RunningRecordDao runningRecordDao;
    @Autowired
    private CompensateDao compensateDao;
    @Autowired
    private CompensateMainDao compensateMainDao;

    @Override
    public Integer getRecordCountToday(Map<String, Object> paramsMap) throws Exception {
        return this.runningRecordDao.getRecordCountToday(paramsMap);
    }

    @Override
    public RunningRecord getRunningRecordById(String runningRecordId) throws Exception {
        return this.runningRecordDao.getRunningRecordById(runningRecordId);
    }

    @Override
    @Transactional
    @Async
    public void updateNodeTimeAndStatus(Map<String, Object> paramsMap) throws Exception {
        runningRecordDao.updateNodeTimeAndStatus(paramsMap);
    }

    @Override
    public MyPageInfo getStudentAppealList(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception {
        PageHelper.startPage(pageindex, pagesize);
        List<Map<String, Object>> list = runningRecordDao.getStudentAppealList(paramsMap);
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        MyPageInfo pageInfo = new MyPageInfo(p);
        return pageInfo;
    }

    @Override
    @Transactional
    public void updateEffectiveStatusOnly(Map<String, Object> paramsMap) throws Exception {
        runningRecordDao.updateEffectiveStatusOnly(paramsMap);
    }

    @Override
    @Transactional
    public void updateEffectiveStatusOnlyForCompensate(Map<String, Object> paramsMap, Compensate compensate) throws Exception {
        compensateDao.insertCompensate(compensate);
        Map<String, Object> pMap = new HashMap<String, Object>();
        pMap.put("userId", compensate.getUserId());
        pMap.put("semesterId", compensate.getSemesterId());
        CompensateMain compensateMain = compensateMainDao.getCompensateMainByUser(pMap);
        Map<String, Object> compensateMainMap = new HashMap<String, Object>();
        compensateMainMap.put("userId", compensate.getUserId());
        compensateMainMap.put("semesterId", compensate.getSemesterId());
        compensateMainMap.put("updateUserId", compensate.getUpdateUserId());
        compensateMainMap.put("updateTime", compensate.getUpdateTime());

        if(RunningEnum.FREERUNNING.getValue() == compensate.getRunningType()) {//--自由跑
                /*----------------------------- 自由跑 ------*/
            compensateMainMap.put("runningLength", compensate.getRunningLength());
        } else if(RunningEnum.MORNINGRUNNING.getValue() == compensate.getRunningType()) {//--晨跑
                /*----------------------------- 晨跑 --------*/
            compensateMainMap.put("runningCount", compensate.getRunningCount());
        } else if(compensate.getRunningType() == RunningEnum.ACTIVITYRUN.getValue()) {//--活动
                /*----------------------------- 活动跑 --------*/
            compensateMainMap.put("compensateScore", compensate.getCompensateScore());
        }

        if(null == compensateMain) {//新增
            compensateMainDao.insertCompensateMain(compensateMainMap);
        } else{//修改
            compensateMainMap.put("compensateId", compensateMain.getCompensateId());
            compensateMainDao.updateCompensateMainForRecord(compensateMainMap);
        }
        runningRecordDao.updateEffectiveStatusOnly(paramsMap);
    }



}
