package com.peipao.qdl.user.service.impl;


import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.util.MD5Util;
import com.peipao.framework.util.StochasticUtil;
import com.peipao.framework.util.StringUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.schooluser.service.SchoolUserService;
import com.peipao.qdl.user.dao.UserDao;
import com.peipao.qdl.user.model.User;
import com.peipao.qdl.user.model.UserStateEnum;
import com.peipao.qdl.user.model.UserTypeEnum;
import com.peipao.qdl.user.service.UserCacheService;
import com.peipao.qdl.user.service.UserService;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

//import cn.boce.cloud.common.queue.util.SocialMessagePushHelper;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private SchoolUserService schoolUserService;

    @Autowired
    private UserCacheService userCacheService;

    @Override
    public User getUserByMobile(String mobile) throws Exception {
        return userDao.getUserByMobile(mobile);
    }

    @Transactional
    public Map<String, Object> createUser(User user) throws Exception {
        if(StringUtil.isNotEmpty(user.getMobile())) {
            User u = userDao.getUserByMobile(user.getMobile());
            if (null != u && !u.getUserId().equals(user.getUserId())) { // 检查手机是否已注册
                throw new BusinessException(ReturnStatus.USER_ALREADY_EXIST);
            }
        }
        String enPassword = StringUtil.decode(user.getPassword());
        user.setPassword(enPassword);
        user.setCreateTime(Calendar.getInstance().getTime());
        user.setStatus(UserStateEnum.NORMAL.getValue());
        user.setToken(StochasticUtil.getUUID());
        if(null == user.getUserId() || 0L == user.getUserId()) {
            userCacheService.insertUser(user);
        } else {
            userCacheService.updateUser(user);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getUserId());
        map.put("token", user.getToken());
        return map;
    }

    @Override
    public void addOfficialUser(Long userId, User user) throws Exception {
        schoolUserService.validateOfficialManager(userId);
//        if (userDao.getUserByMobile(user.getMobile()) != null) {    // 检查手机是否已注册
//            throw new BusinessException(ReturnStatus.USER_ALREADY_EXIST);
//        }
        user.setPassword(MD5Util.getMD5("000000"));
        user.setUserType(UserTypeEnum.OFFICIALMANAGER.getValue());

        Map<String, Object>  map = createUser(user);
        user.setUserId(Long.valueOf(map.get("userId").toString()));
    }

    @Transactional
    @Override
    public void updatePassword(Long userId, String password, String oldPassword) throws Exception {
        User user = checkUserExist(userId);

        if (!oldPassword.equals(user.getPassword())) {
            throw new BusinessException(ReturnStatus.USER_OLD_PASSWORD_INCORRECT);//原密码错误
        }
        if (user.getPassword().equals(password))
            throw new BusinessException(ReturnStatus.USER_SAME_PASSWORD);
        if (StringUtil.isNotEmpty(user.getMobile()) && user.getMobile().equals(password))
            throw new BusinessException(ReturnStatus.USER_SAME_PHONE_PASSWORD);

        userCacheService.updateUserPassWord(userId, password);
    }

    @Transactional
    @Override
    public void updatePhone(Long userId, String phone, String password) throws Exception {
        User user = checkUserExist(userId);
        if (user == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        if (user.getMobile().equals(phone)) {
            throw new BusinessException(ReturnStatus.USER_SAME_PHONE);
        }
        if (!user.getPassword().equals(StringUtil.decode(password))) {
            throw new BusinessException(ReturnStatus.USER_PASSWORD_INCORRECT);
        }

        User tmpUser1 = getUserByMobile(phone);
        if (tmpUser1 != null) {
            throw new BusinessException(ReturnStatus.USER_ALREADY_EXIST);
        }
        user.setMobile(phone);
        userCacheService.updateUser(user);
    }

    private User checkUserExist(Long userId) throws Exception {
        User user = userCacheService.getUserById(userId);
        if (user == null)
            throw new BusinessException(ReturnStatus.USER_NOT_EXIST);

        return user;
    }

    public User validateUser(Long id) throws Exception {
        User user = userCacheService.getUserById(id);
        if (user == null)
            throw new BusinessException(ReturnStatus.USER_NOT_EXIST);
        return user;
    }

    @Override
    public User validateSchoolUser(Long userId) throws Exception {
        User currUser = validateUser(userId);//当前操作的用户(教师或学校管理员)
        if (currUser.getUserType() != UserTypeEnum.TEACHER.getValue()
                && currUser.getUserType() != UserTypeEnum.SCHOOLMANAGER.getValue()) {
            //当前操作用户即不是老师也不是学校管理员
            throw new BusinessException(ResultMsg.USER_TYPE_SCHOOL_ONLY);//非校方帐号不能进行此操作
        }
        return currUser;
    }

    @Override
    public void updatePasswordByPhone(String mobile, String password) throws Exception {
        if (mobile.equals(password))
            throw new BusinessException(ReturnStatus.USER_SAME_PHONE_PASSWORD);
        User user = getUserByMobile(mobile);
        if (user != null) {
            String enPassword = StringUtil.decode(password);
            userDao.updatePasswordByPhone(mobile, enPassword);
        } else {
            throw new BusinessException(ReturnStatus.USER_NOT_EXIST);
        }
    }

    public List<User> getUsersByIds(List<String> ids) throws Exception {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        return userDao.getUsersByIds(ids.stream().distinct().collect(Collectors.toList()));
    }

    @Override
    public Map<String, Object> userLogin(User user) throws Exception {
        if (StringUtil.isNotEmpty(user.getNickname())){
            boolean b = ValidateUtil.isMobile(user.getNickname());
            if (b){
                user.setMobile(user.getNickname());
            }
        }
        Map<String, Object> map = null;
        List<Map<String, Object>> userList = null;
        if (StringUtil.isNotEmpty(user.getMobile())) {
             userList = userDao.queryUserLoginParams(null, user.getMobile());
            if(null != userList && userList.size() > 0){
                map = userDao.queryUserLoginParams(null, user.getMobile()).get(0);  // 手机登录
            }
        }else{
            userList = userDao.queryUserLoginParams(user.getNickname(),null);
            if(null != userList && userList.size() > 0){
                map = userDao.queryUserLoginParams(user.getNickname(),null).get(0);  //
            }
        }

        if (map == null) {
            throw new BusinessException(ReturnStatus.USER_NOT_EXIST);
        }

        if (map.get("userId") == null) {
            throw new BusinessException(ReturnStatus.USER_USERNAME_INCORRECT);
        }

        String passwrod = String.valueOf(map.get("password"));
        String enpassword = StringUtil.decode(user.getPassword());
        if (!enpassword.equals(passwrod)) {
            throw new BusinessException(ReturnStatus.USER_PASSWORD_INCORRECT);
        }

        // TODO:METEOR.WU
        // 根据schoolId 查询semesterid
        map.remove("password");
        if(null != map.get("userType") && (
                UserTypeEnum.TEACHER.getValue() == Integer.parseInt(map.get("userType").toString())
                || UserTypeEnum.SCHOOLMANAGER.getValue() == Integer.parseInt(map.get("userType").toString())
                || UserTypeEnum.OFFICIALMANAGER.getValue() == Integer.parseInt(map.get("userType").toString())
        )) {
            //如果是后台管理账号（包括老师账号登陆时，不更新token）//token设置过期等后期完善
        } else {
            User userTemp = new User();
            userTemp.setUserId(Long.valueOf(map.get("userId").toString()));
            userTemp.setToken(StochasticUtil.getUUID());
            userCacheService.updateUser(userTemp);
            map.put("token", userTemp.getToken());
        }
        return map;
    }

    @Override
    public User getUserByUser(User user) throws Exception {
        return userDao.queryUserByUserCondtion(user);
    }

    @Override
    public String getImageUploadToken(Long userId) {
        Auth auth = Auth.create(WebConstants.qiuniu.ACCESSKEY, WebConstants.qiuniu.SECRETKEY);
        return auth.uploadToken(WebConstants.qiuniu.BUCKET);
    }

    @Override
    public Map<String, Object> getUsernameAndUserType(Long userId) throws Exception {
        Map<String, Object> map = userDao.queryUsernameAndUserType(userId);
        if (map == null) {
            throw new BusinessException(ReturnStatus.USER_NOT_EXIST);
        }
        return map;
    }

    @Override
    public String getMobileById(Long userid) throws Exception {
        User user = userCacheService.getUserById(userid);
        if (user == null) {
            throw new BusinessException(ReturnStatus.USER_NOT_EXIST);
        }
        return user.getMobile();
    }

    @Override
    public Integer getSchoolUserCount(Map<String, Object> paramsMap) throws Exception {
        return userDao.getSchoolUserCount(paramsMap);
    }

    @Override
    public Integer getSchoolUserCountByTheType(Long schoolId, Byte status) throws Exception {
        return userDao.getSchoolUserCountByTheType(schoolId, status);
    }

    @Override
    public Map<String, Object> getUserForMainPage(Long userId) throws Exception {
        Map<String, Object> map = userDao.queryUserForMainPage(userId);
        if (map == null) {
            throw new BusinessException(ReturnStatus.USER_NOT_EXIST);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(map.get("title").toString()).append(" ").append(map.get("levelName").toString());
        map.put("level", stringBuilder.toString());
        String admission = getAdmission(map);
        if(null != admission){
            map.put("admission",admission +"级");
        }else{
            map.put("admission","");
        }
        map.remove("levelName");
        map.remove("title");
        return map;
    }


    public String getAdmission(Map<String, Object> admissionMap){
        String admission = null;
        if(null != admissionMap.get("admission") &&  admissionMap.get("admission").toString().length() >= 4){
            admission = admissionMap.get("admission").toString().substring(0,4);
            if(StringUtils.isNumeric(admission)){
               return admission;
            }else {
                return null;
            }
        }
        return admission;
    }

    @Override
    public Map<String, Object> getUserForPersonalInfo(Long userId) throws Exception {
        Map<String, Object> map = userDao.queryUserForPersonalInfo(userId);
        if (map == null) {
            throw new BusinessException(ReturnStatus.USER_NOT_EXIST);
        }
        return map;
    }

    @Override
    public Map<String, Object> getUserBasicInfoForweb(Long userId) throws Exception {
        // mobile, t1.nick_name AS nickname, t1.sex, t1.username
        User user = userCacheService.getUserById(userId);
        if (user == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", user.getMobile());
        map.put("nickname", user.getNickname());
        map.put("sex", user.getSex());
        map.put("username", user.getUsername());
        return map;
    }

    @Override
    public Map<String, Object> getTeacherBySchoolId(Long schoolId, int pageindex, int pagesize) throws Exception {
        List<User> list = userDao.queryTeacherBySchoolId(schoolId, (pageindex - 1) * pagesize, pagesize);
        List<Map<String, Object>> ret = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", list.get(i).getUserId());
            map.put("userId", list.get(i).getUserId());
            map.put("username", list.get(i).getUsername());
            map.put("nickname", list.get(i).getNickname());
            map.put("mobile", list.get(i).getMobile());
            map.put("sex", list.get(i).getSex());
            map.put("userType", list.get(i).getUserType());

            StringJoiner joiner = new StringJoiner(",");
            list.get(i).getCourseList().forEach(k->joiner.add(k.getName()));
            if (joiner.toString().equals("null")) {
                map.put("name", "");
            }else{
                map.put("name", joiner.toString());
            }

            ret.add(map);
        }
        Long allcount = userDao.queryTeacherBySchoolIdCount(schoolId);
        return MyPage.processPage(allcount, pagesize, pageindex, ret);
    }

    @Override
    public Map<String, Object> getTeacherInfoById(Long userId) throws Exception {
        User teacher = userDao.getTeacherInfoById(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", teacher.getUserId());
        map.put("username", teacher.getUsername());
        map.put("nickname", teacher.getNickname());
        map.put("mobile", teacher.getMobile());
        map.put("sex", teacher.getSex());
        map.put("userType", teacher.getUserType());
        StringJoiner joiner = new StringJoiner(",");
        teacher.getCourseList().forEach(k->joiner.add(k.getName()));
        if (joiner.toString().equals("null")) {
            map.put("courseName", "");
        }else{
            map.put("courseName", joiner.toString());
        }
        return map;
    }

    @Override
    public Map<String, Object> getStudentInfo(Long userId, Long studentId) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(studentId, Calendar.getInstance().getTime());
        if (userSchool == null || userSchool.getSemesterId() == null) {
            throw new BusinessException(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }

        Map<String, Object> map =  userDao.queryStudentInfo(studentId, userSchool.getSemesterId());
        if (map == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        return map;
    }

    @Override
    public Map<String, Object> getStudentInfoById(Long userId, Long studentId) throws Exception {
        schoolUserService.validateTeacher(userId);
        return userDao.queryStudentInfoById(studentId);
    }

    @Override
    public Map<String, Object> getStudentByUsernameOrStudentNO(Long schoolId, String value, int pageindex, int pagesize) throws Exception {

        List<Map<String, Object>> list = userDao.queryStudentByUsernameOrStudentNO(schoolId, value, (pageindex-1)*pagesize, pagesize);
        Long allcount = userDao.queryStudentByUsernameOrStudentNOCount(schoolId, value);
        return MyPage.processPage(allcount, pagesize, pageindex, list);
    }

    @Override
    public Integer checkStudentRegister(Long schoolId, String studentNO) throws Exception {
        return userDao.checkStudentRegister(schoolId, studentNO);
    }

    @Override
    public Map<String, Object> getStudentList( Long userId, Long schoolId, int pageindex, int pagesize) throws Exception {
        schoolUserService.validateOfficialManager(userId);

        List<Map<String, Object>> list=  userDao.queryStudentList(schoolId, (pageindex-1)*pagesize, pagesize);
        Long allcount = userDao.queryStudentListCount(schoolId);
        return MyPage.processPage(allcount, pagesize, pageindex, list);
    }

    @Override
    public Map<String, Object> getUserInfoWithSchoolName(Long userId) throws Exception {
        return userDao.queryUserInfoWithSchoolName(userId);
    }

    @Override
    public void deleteOfficialUser(Long userId, Long delId) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        userCacheService.deleteUserById(delId);
    }

    @Override
    public void resetOfficialUserPassword(Long userId, Long delId) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        User user = schoolUserService.validateOfficialManager(delId);

        String md5password = MD5Util.getMD5("000000");
        String password = StringUtil.decode(md5password);
        updatePassword(delId, password, user.getPassword());
    }

    @Override
    public Map<String, Object> getOfficialUserList(Long userId, int pageindex, int pagesize) throws Exception {
        List<Map<String, Object>> list = userDao.queryOfficialUserList((pageindex-1)*pagesize, pagesize);
        Long allcount = userDao.queryOfficialUserListCount();
        return MyPage.processPage(allcount, pagesize, pageindex, list);
    }

    @Override
    public Long getUnselectCourseStudentCount(Long schoolId) throws Exception {
        return userDao.queryUnselectCourseStudentCount(schoolId);
    }

    @Override
    public void setUserParamToNull(User user) throws Exception {
        userDao.setUserParamToNull(user);
    }

    @Override
    public Map<String, Object> getUserInfoForDiscover(Long userId) throws Exception {
        return userDao.getUserInfoForDiscover(userId);
    }

    @Override
    public Map<String, Object> getUserInfoForDiscoverPc(Long userId) throws Exception {
        return userDao.getUserInfoForDiscoverPc(userId);
    }

    @Override
    public Integer countByCourseId(Long courseId) throws Exception {
        return userDao.countByCourseId(courseId);
    }

    @Override
    @Transactional
    public void updateUserCourseToNull(Map<String, Object> paramsMap) throws Exception {
        userDao.updateUserCourseToNull(paramsMap);
    }

    @Override
    public List<Map<String, Object>> getUserIdsByCourseId(Long courseId) throws Exception {
        return userDao.getUserIdsByCourseId(courseId);
    }
}
