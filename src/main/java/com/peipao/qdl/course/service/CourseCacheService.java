package com.peipao.qdl.course.service;

import com.peipao.qdl.course.dao.CourseDao;
import com.peipao.qdl.course.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 课程redis缓存
 *
 * @author Meteor.wu
 * @since 2018/2/27 15:49
 */
@Service
public class CourseCacheService {
    @Autowired
    private CourseDao courseDao;

    @Cacheable(value = "peipao", key = "'course'+#courseId")
    public Course getCourseById(Long courseId) throws Exception {
        return courseDao.queryCourseById(courseId);
    }

    @CachePut(value = "peipao", key = "'course'+#result.getCourseId()")
    public Course addCourse(Course course) throws Exception {
        courseDao.addCourse(course);
        return courseDao.queryCourseById(course.getCourseId());
    }

    @CachePut(value = "peipao", key = "'course'+#result.getCourseId()")
    public Course updateCourse(Course course) throws Exception {
        courseDao.updateCourse(course);
        return courseDao.queryCourseById(course.getCourseId());
    }

    @CacheEvict(value = "peipao", key = "'course'+#courseId")
    public void deleteCourse(Long courseId) throws Exception {
        courseDao.deleteCourse(courseId);
    }

}
