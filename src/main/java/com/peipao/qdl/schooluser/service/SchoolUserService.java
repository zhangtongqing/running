package com.peipao.qdl.schooluser.service;



import com.peipao.qdl.schooluser.model.Student;
import com.peipao.qdl.user.model.User;
import net.sf.json.JSONObject;
import org.apache.poi.ss.usermodel.Workbook;
import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/7/3
 **/
public interface SchoolUserService {

    Student addStudent2Library(Student student, Long userId) throws Exception;
    void updateStudent(Student student, Long userId) throws Exception;
    void updateStudent(Student student) throws Exception;
    void deleteStudent(Long userId, Long studentId) throws Exception;
    Student unbind(Long userId, Long studentId) throws Exception;
    Map<String, Object> getStudentList(Long userId, JSONObject json, int pageindex, int pagesize) throws Exception;
    Map<String, Object> downloadStudentList(Long userId, JSONObject json) throws Exception;

    Long addTeacher(User user, Long userId) throws Exception;
    void updateTeacher(Long userId, User teacher) throws Exception;
    void resetTeacherPassword(Long userId, Long teacherId) throws Exception;
    void deleteTeacher(Long userId, Long teacherId) throws Exception;
    Map<String, Object> getTeacherList(Long userId, int pageindex, int pagesize) throws Exception;
    Map<String, Object> getTeacherInfoById(Long userId) throws Exception;
    Map<String, Object> getTeacherListBySchoolId(Long userId, Long schoolId, int pageindex, int pagesize) throws Exception;

    User validateTeacher(Long userId) throws Exception;
    User validateSchoolManager(Long userId) throws Exception;
    User validateOfficialManager(Long userId) throws Exception;
    Map<String, Object> validateTeacherAndStudent(Long teacherId, Long studentId) throws Exception;

    Map<String,Object> loadStudent2Library(Long userid, Workbook wb) throws Exception;
    List<Map<String, Object>> getTeacherNameList(Long userId) throws Exception;

    Student getByStudent(Student student) throws Exception;

    List<String> getClassList(Long userId, String classname) throws Exception;

    Map<String, Object> user2Map(User user, boolean isNew) throws Exception;

    List<Map<String, Object>> getNumberGroupByClass(Map<String, Object> paramsMap) throws Exception;
}
