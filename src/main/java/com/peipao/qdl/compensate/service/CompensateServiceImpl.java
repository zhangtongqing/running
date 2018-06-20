package com.peipao.qdl.compensate.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.compensate.dao.CompensateDao;
import com.peipao.qdl.compensate.dao.CompensateMainDao;
import com.peipao.qdl.compensate.model.Compensate;
import com.peipao.qdl.compensate.model.CompensateMain;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.statistics.model.UserStatistic;
import com.peipao.qdl.statistics.service.UserStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：CompensateServiceImpl
 * 功能描述：CompensateServiceImpl
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/11/30 17:13
 * 修订记录：
 */
@Service
public class CompensateServiceImpl implements CompensateService {
    @Autowired
    private CompensateDao compensateDao;
    @Autowired
    private CompensateMainDao compensateMainDao;
    @Autowired
    private UserStatisticService userStatisticService;

    public Map<String, Object> getCompensateInfoForStudent(Map<String, Object> paramsMap) throws Exception {
        return compensateDao.getCompensateInfoForStudent(paramsMap);
    }

    @Override
    public MyPageInfo getCompensateListForStudent(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception {
        PageHelper.startPage(pageindex, pagesize);
        List<Map<String, Object>> list = compensateDao.getCompensateListForStudent(paramsMap);
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        MyPageInfo pageInfo = new MyPageInfo(p);
        return pageInfo;
    }

    @Override
    public Map<String, Object> getCompensateMainInfoForStudent(Map<String, Object> paramsMap) throws Exception {
        return compensateMainDao.getCompensateMainInfoForStudent(paramsMap);
    }

//    @Override
//    @Transactional
//    public void insertCompensateMain(Map<String, Object> paramsMap) throws Exception {
//        compensateMainDao.insertCompensateMain(paramsMap);
//    }

//    @Override
//    @Transactional
//    public void updateCompensateMainForRecord(Map<String, Object> paramsMap) throws Exception {
//        compensateMainDao.updateCompensateMainForRecord(paramsMap);
//    }

    @Override
    @Transactional
    public void updateStudentScoreOverall(Compensate compensate, UserSchool userSchool) throws Exception {
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
        compensateMainMap.put("runningCount", compensate.getRunningCount());
        compensateMainMap.put("runningLength", compensate.getRunningLength());
        compensateMainMap.put("compensateScore", compensate.getCompensateScore());
        if (null == compensateMain) {//新增
            compensateMainDao.insertCompensateMain(compensateMainMap);
        } else {//修改
            compensateMainMap.put("compensateId", compensateMain.getCompensateId());
            compensateMainDao.updateCompensateMainForRecord(compensateMainMap);
        }

        UserStatistic userStatistic = userStatisticService.getByUserIdAndSemesterId(userSchool.getUserId(), userSchool.getSemesterId());
        if(null == userStatistic){
            userStatistic = new UserStatistic(userSchool.getSchoolId(), userSchool.getSemesterId(), userSchool.getUserId());
        }
        if(RunningEnum.FREERUNNING.getValue() == compensate.getRunningType()) {//--自由跑
            userStatistic.setCompensateFreeRunningLength(userStatistic.getCompensateFreeRunningLength() + compensate.getRunningLength());
        } else if(RunningEnum.MORNINGRUNNING.getValue() == compensate.getRunningType()) {//--晨跑
            userStatistic.setCompensateMorningRunningCount(userStatistic.getCompensateMorningRunningCount() + compensate.getRunningCount());
        } else if(compensate.getRunningType() == RunningEnum.ACTIVITYRUN.getValue()) {//--活动跑
            userStatistic.setCompensateActivityScore(userStatistic.getCompensateActivityScore() + compensate.getCompensateScore());
        }
        userStatisticService.insertOrUpdateStatistic(userStatistic);
    }

    @Override
    public CompensateMain getCompensateMainByUser(Map<String, Object> paramsMap) throws Exception {
        return compensateMainDao.getCompensateMainByUser(paramsMap);
    }
}
