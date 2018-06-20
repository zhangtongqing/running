package com.peipao.qdl.course.dao;


import com.peipao.qdl.course.model.Course;
import com.peipao.qdl.course.model.CourseMember;
import com.peipao.qdl.course.model.CourseSchedule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/7/4
 **/
@Repository
public interface CourseDao {
    //@Param("courseId") Long courseId, @Param("semesterId") Long semesterId, @Param("value") String value
    List<Map<String, Object>> getSelectCourseList(Map<String, Object> paramsMap) throws Exception;

    /*
    @Param("weekIndex") Integer weekIndex, @Param("userId") Long userId,
    @Param("semesterId") Long semesterId, @Param("courseId") Long courseId
     */
    List<Map<String, Object>> queryCourseScheduleListWithName(Map<String, Object> paramsMap) throws Exception;

    CourseSchedule queryCourseScheduleById(@Param("courseScheduleId") Long courseScheduleId) throws Exception;

    void insertCourseMember(CourseMember courseMember) throws Exception;

    CourseMember queryCourseMemberByUserId(@Param("userId") Long userId, @Param("courseScheduleId") Long courseScheduleId) throws Exception;

    void updateCourseMember(CourseMember courseMember) throws Exception;

    void updateCourseScheduleSomeCount(@Param("courseScheduleId") Long courseScheduleId, @Param("signcount") Integer signcount) throws Exception;

    Course queryCourseById(@Param("courseId") Long courseId) throws Exception;

    Integer checkHaveSign(@Param("courseScheduleId") Long courseScheduleId, @Param("userId") Long userId) throws Exception;
    List<Map<String, Object>> queryCourseListForWeb(@Param("userId") Long userId, @Param("schoolId") Long schoolId, @Param("semesterId") Long semesterId) throws Exception;

    List<Map<String, Object>> queryCourseMemberListWeb(@Param("courseId") Long courseId, @Param("courseScheduleId") Long courseScheduleId, @Param("from") int from, @Param("num") int num) throws Exception;

    //@Param("courseId") Long courseId, @Param("courseScheduleId") Long courseScheduleId
    List<Map<String, Object>> getCourseScheduleMember(Map<String, Object> paramsMap) throws Exception;
    Integer getCourseScheduleMemberCount(Map<String, Object> paramsMap) throws Exception;
    Long queryCourseMemberListCount(@Param("courseId") Long courseId) throws Exception;

    void addCourse(Course course) throws Exception;

    void updateCourse(Course course) throws Exception;

    void deleteCourse(@Param("courseId") Long courseId) throws Exception;
    void deleteCourseSchedule(@Param("courseScheduleId") Long courseScheduleId) throws Exception;


    List<Map<String, Object>> queryCourseListWithMemberCount(@Param("schoolId") Long schoolId, @Param("semesterId") Long semesterId, @Param("userId") Long userId,
                                                             @Param("courseId") Long courseId, @Param("value") String value) throws Exception;

    List<Map<String, Object>> queryCourseNameAndId(@Param("semesterId") Long semesterId, @Param("userId") Long userId, @Param("courseName") String courseName) throws Exception;

    List<Map<String, Object>> queryCourseScheduleListBycourseIdForWeb(Map<String, Object> paramsMap) throws Exception;
    Long queryCourseScheduleListBycourseIdForWebCount(Map<String, Object> paramsMap) throws Exception;

    List<Map<String, Object>> queryStudentCourseList(@Param("userId") Long userId, @Param("semesterId") Long semesterId, @Param("from") int from, @Param("num") int num) throws Exception;
    Long queryStudentCourseListCount(@Param("userId") Long userId, @Param("semesterId") Long semesterId) throws Exception;

    void updateCourseSchedule(CourseSchedule courseSchedule) throws Exception;

    void insertCourseSchedule(CourseSchedule courseSchedule) throws Exception;

    Map<String, Object> queryEnrollCountAndSignCount(@Param("courseScheduleId") Long courseScheduleId) throws Exception;

    Map<String, Object> queryCourseScheduleWithTeacherName(@Param("courseScheduleId") Long courseScheduleId) throws Exception;

    List<String> queryCourseNameByIds(@Param("ids") String ids) throws Exception;

    Map<String, Object> queryCourseSumEnrollCountAndSignCount(@Param("courseId") Long courseId) throws Exception;

    List<Map<String, Object>> queryCourseMemberByIds(@Param("courseScheduleId") Long courseScheduleId, @Param("ids") String ids) throws Exception;

    CourseMember queryCourseMemberById(@Param("courseMemberId") Long courseMemberId);

    Integer countCourseByUserId(@Param("userId") Long userId) throws Exception;
    Long getSchoolIdByCourseId(@Param("courseId") Long courseId) throws Exception;
    Map<String, Object> getCourseScheduleBasicInfo(@Param("courseScheduleId") Long courseScheduleId) throws Exception;

    void deleteCourseMember(@Param("courseMemberId") Long courseMemberId) throws Exception;
    Map<String, Object> getCourseScheduleById(@Param("courseScheduleId") Long courseScheduleId) throws Exception;

    List<Map<String, Object>> getCoursescheduleDescList(Map<String, Object> paramsMap) throws Exception;

    //删除课程表子表
    void deleteCourseScheduleByDescId(@Param("descId") Long descId) throws Exception;

    List<Map<String, String>> getCourseAndTeacher(@Param("schoolId") Long schoolId, @Param("semesterId") Long semesterId) throws Exception;
}

