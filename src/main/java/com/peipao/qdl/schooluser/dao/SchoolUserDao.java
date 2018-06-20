package com.peipao.qdl.schooluser.dao;


import com.peipao.qdl.schooluser.model.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/7/3
 **/
@Repository
public interface SchoolUserDao {
    void insertStudentLibrary(Student student) throws Exception;
    void updateStudent(Student student) throws Exception;

    Student queryByStudent(Student student) throws Exception;

    void deleteStudent(@Param("userId") Long userId) throws Exception;
    List<Student> getStudentList(@Param("schoolId") Long schoolId, @Param("status") int status, @Param("username") String username, @Param("studentNO") String studentNO,
                                 @Param("mobile") String mobile, @Param("classname") String classname, @Param("unselect") String unselect, @Param("from") int from, @Param("num") int num) throws Exception;
    Long getStudentListCount(@Param("schoolId") Long schoolId, @Param("status") int status, @Param("username") String username, @Param("studentNO") String studentNO,
                             @Param("mobile") String mobile, @Param("classname") String classname, @Param("unselect") String unselect) throws Exception;

    /*
     * 根据查询条件查询所有的学生库信息
     */
    List<Map<String, Object>> getAllStudentList(@Param("schoolId") Long schoolId, @Param("status") int status, @Param("username") String username, @Param("studentNO") String studentNO,
                                                @Param("mobile") String mobile, @Param("classname") String classname, @Param("unselect") String unselect) throws Exception;

    List<Map<String, Object>> getTeacherNameList(@Param("schoolId") Long schoolId) throws Exception;

    List<String> getClassList(@Param("schoolId") Long schoolId, @Param("classname") String classname ) throws Exception;

    List<Map<String, Object>> getNumberGroupByClass(Map<String, Object> paramsMap) throws Exception;
}
