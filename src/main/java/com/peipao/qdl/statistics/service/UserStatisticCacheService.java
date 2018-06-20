package com.peipao.qdl.statistics.service;

import com.peipao.qdl.statistics.dao.UserStatisticDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：UserStatisticCacheService
 * 功能描述：排名缓存
 * 作者：Liu Fan
 * 版本：2.0.11
 * 创建日期：2018/2/5 10:42
 * 修订记录：
 */

@Service
public class UserStatisticCacheService {
    @Autowired
    private UserStatisticDao userStatisticDao;

    @Cacheable(value = "peipao", key = "'rankingInCourse'+#courseId")
    public List<Map<String, Object>> getRankingInCourse(Long schoolId, Long semesterId, Long courseId) throws Exception {
        return userStatisticDao.getRankingInCourse(schoolId, semesterId, courseId);
    }

    @Cacheable(value = "peipao", key = "'rankingInSchool'+#schoolId")
    public List<Map<String, Object>> getRankingInSchool(Long schoolId) throws Exception {
        return userStatisticDao.getRankingInSchool(schoolId);
    }

    @Cacheable(value = "peipao", key = "'rankingInCountry'")
    public List<Map<String, Object>> getRankingInCountry() throws Exception {
        return userStatisticDao.getRankingInCountry();
    }

    @Cacheable(value = "peipao", key = "'getMorningRunWeekCount'+#teacherId")
    public List<Map<String, Object>> getMorningRunWeekCount(Long teacherId, Map<String, Object> paramsMap) throws Exception {
        return userStatisticDao.getWeekCount(paramsMap);
    }

    @Cacheable(value = "peipao", key = "'getFreeRunWeekCount'+#teacherId")
    public List<Map<String, Object>> getFreeRunWeekCount(Long teacherId, Map<String, Object> paramsMap) throws Exception {
        return userStatisticDao.getWeekCount(paramsMap);
    }

    @Cacheable(value = "peipao", key = "'rankingInSchoolByMoringRun'+#schoolId")
    public List<Map<String,Object>> getRankingInSchoolByMorningRun(Long schoolId) throws Exception {
        return userStatisticDao.getRankingInSchoolByMornigRun(schoolId);
    }

    @Cacheable(value = "peipao", key = "'rankingInCourseByMoringRun'+#courseId")
    public List<Map<String,Object>> getRankingInCourseByMorningRun(Long schoolId, Long semesterId, Long courseId) {
        return userStatisticDao.getRankingInCourseByMorningRun(schoolId, semesterId, courseId);
    }

    @Cacheable(value = "peipao", key = "'rankingInCountryMorningRun'")
    public List<Map<String, Object>> getRankingInCountryByMorningRun() throws Exception {
        return userStatisticDao.getRankingInCountryByMorningRun();
    }
}
