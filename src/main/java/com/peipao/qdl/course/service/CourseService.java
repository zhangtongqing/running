package com.peipao.qdl.course.service;

import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.course.model.Course;
import com.peipao.qdl.course.model.CourseSchedule;
import com.peipao.qdl.school.model.UserSchool;
import io.swagger.models.auth.In;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/7/4
 **/
public interface CourseService {
    /**
     * 查询课程名和代课老师名字
     *
     * @return 返回课程名和代课老师名字
     * @author meteor.wu
     * @version 1.0
     * @since 2017/7/4
     */
    //Long courseId, Long semesterId, String value
    List<Map<String, Object>> getSelectCourseList(Map<String, Object> paramsMap, Long courseId) throws Exception;

    List<Map<String, Object>> getCoursescheduleDescList(Long courseId, UserSchool userSchool) throws Exception;


    /**
     * 根据周次查询所有体育课
     *
     * @param userId    userId
     * @param weekIndex 第x周
     * @return
     * @author meteor.wu
     * @version 1.0
     * @since 2017/7/4
     */
    List<Map<String, Object>> getCourseScheduleList(Long userId, Integer weekIndex) throws Exception;

    /**
     * 根据课程表ID查询课程参与的人员
     *
     * @param courseScheduleId 课程表ID
     * @author meteor.wu
     * @version 1.0
     * @since 2017/7/4
     */
    Map<String, Object> getCourseScheduleMemberWeb(Long userId, Long courseScheduleId, int pageindex, int pagesize) throws Exception;

    MyPageInfo getCourseScheduleMember(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception;
    Integer getCourseScheduleMemberCount(Map<String, Object> paramsMap) throws Exception;

    /**
     * 学生上课签到
     *
     * @param userId           学生userId
     * @param courseScheduleId 课程表ID
     * @author meteor.wu
     * @version 1.0
     * @since 2017/7/4
     */
    void sign(Long userId, Long courseScheduleId, Double longitude, Double latitude) throws Exception;

    Map<String, Object> sign(Long userId, Long courseScheduleId, Long studentId, int type, Long courseMemberId) throws Exception;

    /**
     * @param userId 老师或者管理员ID
     * @return
     * @author meteor.wu
     * @version 1.0
     * @since 2017/7/4
     */

    Map<String, Object> getCourseListForWeb(Long userId) throws Exception;

    Course validateCourse(Long courseId) throws Exception;

    void chooseCourse(Long userId, Long courseId) throws Exception;

    void chooseCourse(Long userId, Long studentId, Long courseId) throws Exception;

    Integer getRecordCount(Long userId, Long semesterId) throws Exception;

    void addCourse(Long userId, Course course) throws Exception;

    void updateCourse(Long userId, Course course) throws Exception;

    void deleteCourse(Long userId, Long courseId) throws Exception;

    List<Map<String, Object>> getCourseListWithMemberCount(Long userId, Long schoolId, Long semesterId, String value) throws Exception;

    List<Map<String, Object>> getCourseNameAndId(Long userId) throws Exception;
    List<Map<String, Object>> getAllCourseNameAndId(Long userId) throws Exception;

    List<Map<String, Object>> getAllCourseNameAndIdArray(Long userId, Long semesterId, String courseName) throws Exception;

    Map<String, Object> getCourseScheduleListBycourseIdForWeb(Long userId, Long courseId, int pageindex, int pagesize) throws Exception;

    Map<String, Object> getCourseScheduleBasicInfo(Long userId, Long courseScheduleId, Long semesterId) throws Exception;

    Map<String, Object> getStudentCourseList(Long userId, Long studentId, int pageindex, int pagesize) throws Exception;

    MyPageInfo getCourseStatisticByStudent(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception;

    void deleteCourseSchedule(Long userId, Long courseScheduleId) throws Exception;

    void createCourseSchedule(Long userId, int startWeekIndex, int endWeekIndex, Long courseId, JSONArray array) throws Exception;

    void updateCourseSchedule(Long userId, JSONObject json) throws Exception;

    Map<String, Object> getEnrollCountAndSignCount(Long userId, Long courseScheduleId) throws Exception;

    Map<String, Object> getCourseScheduleWithTeacherName(Long userId, Long courseScheduleId) throws Exception;

    List<String> getCourseNameByIds(String ids) throws Exception;

    List<Map<String, Object>> getCourseListBySchoolId(Long userId, Long schoolId) throws Exception;

    Map<String, Object> validateTeacherAndCourseSchedule(Long teacherId, Long courseScheduleId) throws Exception;

    CourseSchedule validateCourseSchedule(Long courseScheduleId) throws Exception;

    Integer countCourseByUserId(Long userId) throws Exception;

    Map<String, Object> getUnselectCourseStudent(Long schoolId) throws Exception;

    int loadStudentCourseExcel(Long userid, Workbook wb) throws Exception;

    Map<String, Object> getCourseScheduleDetail(Long userId, Long courseId) throws Exception;

    Map<String, Object> getCourseScheduleInfo(Long courseScheduleId) throws Exception;

    void signSetting(Long userId, Long courseId, int needSignLocation, Double longitude, Double latitude, int signIntevalTime) throws Exception;

    Map<String, Object> getCourseSchedule(Long userId, Long courseId) throws Exception;

    Map<String, Object> getCourseScheduleById(Long courseScheduleId) throws Exception;

    //删除课程表主表+删除课程表子表
    void deleteCoursescheduleDescById(Long descId) throws Exception;

    /**
     * 查询课程名和代课老师名字
     *
     * @param schoolId 学校ID
     * @return 返回课程名和代课老师名字
     * @author meteor.wu
     * @version 1.0
     * @since 2017/7/4
     */
    List<Map<String, String>> getCourseAndTeacher(Long schoolId, Long semesterId) throws Exception;

}
