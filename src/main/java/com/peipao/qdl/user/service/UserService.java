package com.peipao.qdl.user.service;


import com.peipao.qdl.user.model.User;

import java.util.List;
import java.util.Map;


public interface UserService {


    // for home page only
//    List<String> getUserIds(List<? extends UserInterface> post) throws Exception;

//    void setUser(List<? extends UserInterface> post, Map<String, User> users) throws Exception;

    // for all modules
//    void setUser(List<? extends UserInterface> post) throws Exception;

    User getUserByMobile(String phone) throws Exception;

    Map<String, Object> createUser(User user) throws Exception;

    void updatePassword(Long userId, String password, String oldPassword) throws Exception;

    void updatePhone(Long userId, String phone, String password) throws Exception;

    User validateUser(Long id) throws Exception;

    User validateSchoolUser(Long id) throws Exception;

    void updatePasswordByPhone(String phone, String password) throws Exception;

    List<User> getUsersByIds(List<String> ids) throws Exception;

    Map<String, Object> userLogin(User user) throws Exception;

    User getUserByUser(User user) throws Exception;

    String getImageUploadToken(Long userId);

    Map<String, Object> getUsernameAndUserType(Long userId) throws Exception;

    String getMobileById(Long userid) throws Exception;

    Integer getSchoolUserCount(Map<String, Object> paramsMap)  throws Exception;
    Integer getSchoolUserCountByTheType(Long schoolId, Byte status)  throws Exception;

    Map<String, Object> getUserForMainPage(Long userId) throws Exception;

    Map<String, Object> getUserForPersonalInfo(Long userId) throws Exception;

    Map<String, Object> getUserBasicInfoForweb(Long userId) throws Exception;

    Map<String, Object> getTeacherBySchoolId(Long schoolId, int pageindex, int pagesize) throws Exception;

    Map<String, Object> getTeacherInfoById(Long userId) throws Exception;

    Map<String, Object> getStudentInfo(Long userId, Long studentId) throws Exception;
    Map<String, Object> getStudentInfoById(Long userId, Long studentId) throws Exception;

    Map<String, Object> getStudentByUsernameOrStudentNO(Long schoolId, String value, int pageindex, int pagesize) throws Exception;

    Integer checkStudentRegister(Long schoolId, String studentNO) throws Exception;

    Map<String, Object> getStudentList(Long userId, Long schoolId, int pageindex, int pagesize) throws Exception;

    Map<String, Object> getUserInfoWithSchoolName(Long userId) throws Exception;

    void addOfficialUser(Long userId, User user) throws Exception;
    void deleteOfficialUser(Long userId, Long delId) throws Exception;
    void resetOfficialUserPassword(Long userId, Long delId) throws Exception;
    Map<String, Object> getOfficialUserList(Long userId, int pageindex, int pagesize) throws Exception;

    Long getUnselectCourseStudentCount(Long schoolId) throws Exception;

    void setUserParamToNull(User user) throws Exception;


    Map<String, Object> getUserInfoForDiscover(Long userId) throws Exception;

    Map<String, Object> getUserInfoForDiscoverPc(Long userId) throws Exception;

    Integer countByCourseId(Long courseId) throws Exception;

    void updateUserCourseToNull(Map<String, Object> paramsMap) throws Exception;

    List<Map<String, Object>> getUserIdsByCourseId(Long courseId) throws Exception;
}
