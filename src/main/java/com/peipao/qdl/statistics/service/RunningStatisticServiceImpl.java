package com.peipao.qdl.statistics.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.statistics.dao.RunningStatisticDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 方法名称：RunningStatisticServiceImpl
 * 功能描述：RunningStatisticServiceImpl
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/15 13:00
 * 修订记录：
 */
@Repository
public class RunningStatisticServiceImpl implements RunningStatisticService {

    @Autowired
    private RunningStatisticDao runningStatisticDao;

    @Override
    public Map<String, Object> getMyCurrentStatistic(Map<String, Object> paramsMap) throws Exception {
        return runningStatisticDao.getMyCurrentStatistic(paramsMap);
    }

    @Override
    public List<Map<String, Object>> getMyStatistics(Map<String, Object> paramsMap) throws Exception {
        return runningStatisticDao.getMyStatistics(paramsMap);
    }

    @Override
    public Map<String, Object> getStudentStatistics(Map<String, Object> paramsMap) throws Exception {
        return runningStatisticDao.getStudentStatistics(paramsMap);
    }

    @Override
    public Integer getMorningRunningTargetStatistics(Map<String, Object> paramsMap) throws Exception {
        return runningStatisticDao.getMorningRunningTargetStatistics(paramsMap);
    }


    @Override
    public Map<String, Object> getMorningRunningStatistics(Map<String, Object> paramsMap) throws Exception {
        return runningStatisticDao.getMorningRunningStatistics(paramsMap);
    }

    @Override
    public Integer getFreeRunningTargetStatistics(Map<String, Object> paramsMap) throws Exception {
        return runningStatisticDao.getFreeRunningTargetStatistics(paramsMap);
    }

    @Override
    public Map<String, Object> getFreeRunningStatistics(Map<String, Object> paramsMap) throws Exception {
        return runningStatisticDao.getFreeRunningStatistics(paramsMap);
    }

    @Override
    public MyPageInfo getMyStdentTargetList(Map<String, Object> paramsMap, int[] pageParams) throws Exception {
        PageHelper.startPage(pageParams[0], pageParams[1]);
        List<Map<String, Object>> list = runningStatisticDao.getMyStdentTargetList(paramsMap);
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        MyPageInfo pageInfo = new MyPageInfo(p);
        return pageInfo;
    }

    /**
     * 不在数据库中计算，是为了代码可以被灵活被复用
     *
     * @param userId
     * @param paramsMap
     * @return
     * @throws Exception
     */
    @Override
    @Cacheable(value = "peipao", key = "'runningRealLength'+#userId")
    public BigDecimal getRunRealData(Long userId, Map<String, Object> paramsMap) throws Exception {
        List<Map<String, Object>> realData = runningStatisticDao.getRunRealData(paramsMap);
        BigDecimal total = new BigDecimal(0.00);
        if(null != realData && realData.size() > 0){
            Set<Map.Entry<String, Object>> sets = realData.get(0).entrySet();
            for(Map.Entry<String, Object> entry : sets){
                total = total.add(BigDecimal.valueOf((Double) entry.getValue()));
            }
        }
        return total;
    }

    @Override
    @Cacheable(value = "peipao", key = "'usercenter:runningLength'+#userId")
    public List<Map<String, Object>> get7DayRunningLength(Long userId, Map<String, Object> paramsMap) throws Exception {
        List<Map<String, Object>> runningLengList = runningStatisticDao.get7DayRunningLength(paramsMap);
        return runningLengList;
    }


}
