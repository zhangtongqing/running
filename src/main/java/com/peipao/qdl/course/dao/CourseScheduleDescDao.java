package com.peipao.qdl.course.dao;

import com.peipao.qdl.course.model.CourseScheduleDesc;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Meteor.wu
 * @since 2018/3/2 16:50
 */
@Repository
public interface CourseScheduleDescDao {
    List<CourseScheduleDesc> getCourseScheduleDescList(@Param("courseId") Long courseId) throws Exception;
    void insertCourseScheduleDesc(CourseScheduleDesc courseScheduleDesc) throws Exception;
    void deleteCoursescheduleDescById(@Param("descId") Long descId) throws Exception;
}
