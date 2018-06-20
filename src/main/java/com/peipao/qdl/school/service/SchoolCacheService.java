package com.peipao.qdl.school.service;

import com.peipao.qdl.school.dao.SchoolDao;
import com.peipao.qdl.school.model.School;
import com.peipao.qdl.school.model.Semester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * schoolCache
 *
 * @author Meteor.wu
 * @since 2018/1/22 15:19
 */
@Service
public class SchoolCacheService {

    @Autowired
    private SchoolDao schoolDao;

    @Cacheable(value = "peipao", key = "'school'+#schoolId")
    public School getSchoolById(Long schoolId) throws Exception {
        return schoolDao.querySchoolById(schoolId);
    }

    @Transactional
    @CachePut(value = "peipao", key = "'school'+#school.getSchoolId()")
    public School updateSchool(School school) throws Exception {
        schoolDao.updateSchool(school);
        return schoolDao.querySchoolById(school.getSchoolId());
    }

    @Cacheable(value = "peipao", key = "'semester'+#schoolId")
    public Semester getSemesterBySchoolId(Long schoolId) throws Exception {
        return schoolDao.getSemesterBySchoolId(schoolId);
    }
}
