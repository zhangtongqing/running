package com.peipao.qdl.user.dao;


import com.peipao.framework.constant.WebConstants;
import com.peipao.qdl.user.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserDao {

    void insertUser(User user) throws Exception;

    void updateUser(User user) throws Exception;

    void updatePassword(@Param("userId") Long userId, @Param("password") String password) throws Exception;

    // 通过手机好吗判断是否存在，（新增时不能重名）
    User getUserByMobile(@Param("mobile") String mobile) throws Exception;

    User getUserById(@Param("userId") Long userId) throws Exception;

    List<User> getUsersByIds(@Param(WebConstants.User.ID) List<String> id) throws Exception;

    Long queryIdByPhone(@Param("mobile") String mobile) throws Exception;

    void deleteUserById(@Param("id") Long id) throws Exception;

    void updatePasswordByPhone(@Param("mobile") String mobile, @Param("password") String password) throws Exception;

    List<Map<String, Object>> queryUserLoginParams(@Param("nickname") String nickname, @Param("mobile") String mobile) throws Exception;

    User queryUserByUserCondtion(User user) throws Exception;

    Map<String, Object> queryUsernameAndUserType(@Param("userId") Long userId) throws Exception;


    String queryMobileById(@Param("userId") Long userId) throws Exception;

    /*
    @Param("schoolId") Long schoolId, @Param("userType") Integer userType, @Param("status") Byte status
     */
    Integer getSchoolUserCount(Map<String, Object> paramsMap)  throws Exception;
    Integer getSchoolUserCountByTheType(@Param("schoolId") Long schoolId, @Param("status") Byte status)  throws Exception;

    Map<String, Object> queryUserForMainPage(@Param("userId") Long userId) throws Exception;

    Map<String, Object> queryUserForPersonalInfo(@Param("userId") Long userId) throws Exception;

    Map<String, Object> queryUserBasicInfoForweb(@Param("userId") Long userId) throws Exception;

    List<User> queryTeacherBySchoolId(@Param("schoolId") Long schoolId, @Param("from") int from, @Param("num") int num) throws Exception;
    User getTeacherInfoById(@Param("userId") Long userId) throws Exception;
    Long queryTeacherBySchoolIdCount(@Param("schoolId") Long schoolId) throws Exception;

    Map<String, Object> queryStudentInfo(@Param("userId") Long userId, @Param("semesterId") Long semesterId) throws Exception;

    List<Map<String, Object>> queryStudentByUsernameOrStudentNO(@Param("schoolId") Long schoolId, @Param("value") String value, @Param("from") int from, @Param("num") int num) throws Exception;
    Long queryStudentByUsernameOrStudentNOCount(@Param("schoolId") Long schoolId, @Param("value") String value) throws Exception;

    Integer checkStudentRegister(@Param("schoolId") Long schoolId, @Param("studentNO") String studentNO) throws Exception;

    List<Map<String, Object>> queryStudentList(@Param("schoolId") Long schoolId, @Param("from") int from, @Param("num") int num) throws Exception;
    Long queryStudentListCount(@Param("schoolId") Long schoolId) throws Exception;

    Map<String, Object> queryUserInfoWithSchoolName(@Param("userId") Long userId) throws Exception;

    List<Map<String, Object>> queryOfficialUserList(@Param("from") int from, @Param("num") int num) throws Exception;
    Long queryOfficialUserListCount() throws Exception;

    void updateUserLevel(@Param("levelId") Long levelId, @Param("userId") Long userId) throws Exception;

    Map<String, Object> queryStudentInfoById(@Param("userId") Long userId) throws Exception;

    Long queryUnselectCourseStudentCount(@Param("schoolId") Long schoolId) throws Exception;

    void setUserParamToNull(User user) throws Exception;

    Map<String, Object> getUserInfoForDiscover(Long userId) throws Exception;

    Map<String, Object> getUserInfoForDiscoverPc(Long userId) throws Exception;

    Long getSchoolIdByUserId(@Param("userId") Long userId) throws Exception;

    Map<String, Object> getSchoolIdUserTypeByUserId(@Param("userId") Long userId) throws Exception;

    Integer countByCourseId(@Param("courseId") Long courseId) throws Exception;

    void updateUserCourseToNull(Map<String, Object> paramsMap) throws Exception;

    List<Map<String, Object>> getUserIdsByCourseId(@Param("courseId") Long courseId) throws Exception;
}
