package com.peipao.qdl.course.dao;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 方法名称：CourseStatisticDao
 * 功能描述：CourseStatisticDao
 * 作者：Liu Fan
 * 版本：2.1.0
 * 创建日期：2018/3/7 11:42
 * 修订记录：
 */
@Repository
public interface CourseStatisticDao {
    List<Map<String, Object>> getCourseStatisticByStudent(Map<String, Object> paramsMap) throws Exception;

    //参数：schoolId、 userType、 status、 courseId
    Integer getCourseStudentCount(Map<String, Object> paramsMap) throws Exception;
}
